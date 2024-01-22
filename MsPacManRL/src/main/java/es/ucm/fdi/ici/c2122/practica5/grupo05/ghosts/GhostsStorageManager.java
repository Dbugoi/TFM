package es.ucm.fdi.ici.c2122.practica5.grupo05.ghosts;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.EnumMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.Vector;
import es.ucm.fdi.gaia.jcolibri.cbrcore.CBRCase;
import es.ucm.fdi.gaia.jcolibri.cbrcore.CBRCaseBase;
import es.ucm.fdi.gaia.jcolibri.cbrcore.CBRQuery;
import es.ucm.fdi.gaia.jcolibri.method.retain.StoreCasesMethod;
import es.ucm.fdi.gaia.jcolibri.method.retrieve.RetrievalResult;
import es.ucm.fdi.gaia.jcolibri.method.retrieve.NNretrieval.NNConfig;
import es.ucm.fdi.gaia.jcolibri.method.retrieve.NNretrieval.NNScoringMethod;
import pacman.game.Game;
import pacman.game.Constants.GHOST;

public class GhostsStorageManager {
	private static final int MAX_CASES = 10000;
	private static final int TICKS_UNTIL_REVISION = 6;
	private static final double SIM_LIMIT = 0.9995;
	private static final double SIM_LIMIT_DECREMENT = 0.1;
	private static final boolean CLEAN_WHEN_CASEBASE_FULL = true;

	public final int maxCases;

	private Game game;
	private CBRCaseBase caseBase;
	private Vector<NonStoredCase> revisedCases;
	private Map<GHOST, Vector<NonStoredCase>> nonRevisedCases;
	private NNConfig simConfig;
	private GhostCaseJudge judge;

	/**
	 * Clase con los datos de un caso {@link CBRCase}, una colección de los casos más similares que
	 * hay en la base de casos ordenada por similitud a dicho caso (de mayor a menor), y un contador
	 * que, al llegar a 0, indica que hay que llamar a
	 * {@link GhostsStorageManager#reviseCase(NonStoredCase)} y revisar el caso.
	 */
	private class NonStoredCase {
		private final CBRCase cbrCase;
		private final Collection<RetrievalResult> similarCases;
		private int ticksUntilRevision;

		private NonStoredCase(CBRCase cbrCase, Collection<RetrievalResult> similarCases) {
			this.cbrCase = cbrCase;
			this.similarCases = similarCases;
			this.ticksUntilRevision = TICKS_UNTIL_REVISION;
		}

		private void tick() {
			ticksUntilRevision--;
		}

		private boolean isReady() {
			return ticksUntilRevision <= 0;
		}

		private CBRCase getCase() {
			return cbrCase;
		}

		public Collection<RetrievalResult> getSimilarCases() {
			return similarCases;
		}
	}

	public GhostsStorageManager(int maxCases) {
		if (maxCases > MAX_CASES)
			throw new IllegalArgumentException("maxCases must be below " + MAX_CASES);
		this.maxCases = maxCases;
		revisedCases = new Vector<>();
		nonRevisedCases = new EnumMap<>(GHOST.class);
		for (GHOST g : GHOST.values())
			nonRevisedCases.put(g, new Vector<>());

		judge = new GhostCaseJudge();
	}

	public void setGame(Game game) {
		this.game = game;
		judge.setGame(game);
	}

	public void setCaseBase(CBRCaseBase caseBase) {
		this.caseBase = caseBase;
	}

	public void setSimConfig(NNConfig simConfig) {
		this.simConfig = simConfig;
	}

	/**
	 * Revisa los casos pendientes del fantasma {@code ghost} y añade el nuevo caso {@code newCase}
	 * a la lista de casos pendientes de ser revisados. {@code similarCases} es el conjunto de casos
	 * similares a {@code newCase}, que se espera que haya sido creado por una llamada a
	 * {@link NNScoringMethod#evaluateSimilarity(Collection, CBRQuery, NNConfig)}, y es necesario a
	 * la hora de guardar {@code newCase} en la base de casos.
	 * 
	 * @param ghost
	 * @param newCase
	 * @param similarCases
	 */
	public void reviseAndAddCase(GHOST ghost, CBRCase newCase,
			Collection<RetrievalResult> similarCases) {
		Vector<NonStoredCase> nonRevised = nonRevisedCases.get(ghost);
		List<NonStoredCase> toRevise = new ArrayList<>();

		for (NonStoredCase nrCase : nonRevised) {
			nrCase.tick();
			if (nrCase.isReady()) {
				toRevise.add(nrCase);
				revisedCases.add(nrCase);
			}
		}
		nonRevised.removeIf(NonStoredCase::isReady);
		toRevise.forEach(this::reviseCase);

		nonRevised.add(new NonStoredCase(newCase, similarCases));
	}

	/**
	 * Revisa un caso y actualiza su {@link GhostsResult#score}.
	 * 
	 * @param nsCase Caso a revisar
	 */
	private void reviseCase(NonStoredCase nsCase) {
		CBRCase cbrCase = nsCase.getCase();
		GhostsDescription description = (GhostsDescription) cbrCase.getDescription();
		int resultValue = judge.reviseCase(description);

		GhostsResult result = (GhostsResult) cbrCase.getResult();
		result.setScore(resultValue);
	}

	/**
	 * Fuerza a revisar todos los casos pendientes, aunque todavía {@link NonStoredCase#isReady()}
	 * devuelva {@code false}.
	 */
	public void forceRevise() {
		for (GHOST g : GHOST.values())
			forceRevise(g);
	}

	/**
	 * Revisa todos los casos pendientes que haya generado el fantasma {@code ghost}.
	 * 
	 * @param ghost
	 */
	public void forceRevise(GHOST ghost) {
		nonRevisedCases.get(ghost).forEach(this::reviseCase);
		nonRevisedCases.get(ghost).clear();
	}

	/**
	 * Revisa los casos pendientes,
	 */
	public void close() {
		double simLimit = SIM_LIMIT;

		forceRevise();
		revisedCases.forEach(this::retainCase);
		revisedCases.clear();

		if (CLEAN_WHEN_CASEBASE_FULL) {
			while (getTotalCases() > maxCases) {
				cleanupCaseBase(simLimit);
				simLimit -= SIM_LIMIT_DECREMENT; // lo bajamos por si no quitamos suficientes casos
			}
		} else { // clean every time
			do {
				cleanupCaseBase(simLimit);
				simLimit -= SIM_LIMIT_DECREMENT; // lo bajamos por si no quitamos suficientes casos
			} while (getTotalCases() > maxCases);
		}


	}

	/**
	 * Elimina de {@link #caseBase} casos de modo que tras esta operación, para cada caso X, no haya
	 * ningún otro caso con similitud mayor que {@value #SIM_LIMIT}.
	 */
	private void cleanupCaseBase(double simLimit) {
		LinkedHashSet<CBRCase> cases = new LinkedHashSet<>(caseBase.getCases());
		Set<CBRCase> trash = new HashSet<>();

		// O(cases² * (#attributes + log(trash)))
		for (Iterator<CBRCase> iter = cases.iterator(); !cases.isEmpty(); iter = cases.iterator()) {
			CBRCase c = iter.next();
			iter.remove();

			CBRQuery query = new CBRQuery();
			query.setDescription(c.getDescription());

			Set<CBRCase> tmpTrash = new HashSet<>();

			// O(cases * #attributes)
			for (RetrievalResult rr : NNScoringMethod.evaluateSimilarity(cases, query, simConfig)) {
				if (rr.getEval() > simLimit) {
					trash.add(rr.get_case());
					tmpTrash.add(rr.get_case());
				}
			}
			// O(cases * log(trash))
			cases.removeAll(tmpTrash);
		}
		caseBase.forgetCases(trash);
	}

	private void retainCase(NonStoredCase nonStoredCase) {
		// Store the old case right now into the case base
		// Alternatively we could store all them when game finishes in close() method

		// here you should also check if the case must be stored into persistence (too similar to
		// existing ones, etc.)
		if (nonStoredCase.getSimilarCases().isEmpty()) {
			StoreCasesMethod.storeCase(this.caseBase, nonStoredCase.getCase());
			return;
		}

		RetrievalResult mostSimilar = nonStoredCase.getSimilarCases().iterator().next();
		if (mostSimilar.getEval() < SIM_LIMIT) {
			StoreCasesMethod.storeCase(this.caseBase, nonStoredCase.getCase());
			return;
		}

		// si son muy similares, queremos guardar el que haya dado mejor puntuación
		GhostsResult nonStoredRes = (GhostsResult) nonStoredCase.getCase().getResult();
		GhostsResult mostSimilarRes = (GhostsResult) mostSimilar.get_case().getResult();

		if (mostSimilarRes.getScore() < nonStoredRes.getScore()) {
			caseBase.forgetCases(Collections.singleton(mostSimilar.get_case()));
			StoreCasesMethod.storeCase(this.caseBase, nonStoredCase.getCase());
		}

	}

	public int getPendingCases() {
		int notRevised = nonRevisedCases.values().stream().mapToInt(Vector::size).sum();
		return revisedCases.size() + notRevised;
	}

	public int getTotalCases() {
		return getPendingCases() + caseBase.getCases().size();
	}
}
