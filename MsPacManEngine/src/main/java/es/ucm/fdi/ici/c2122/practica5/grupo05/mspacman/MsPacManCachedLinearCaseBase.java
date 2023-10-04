package es.ucm.fdi.ici.c2122.practica5.grupo05.mspacman;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Comparator;
import java.util.List;
import java.util.Set;
import java.util.TreeSet;
import java.util.Vector;
import java.util.stream.Collectors;

import org.apache.xmlbeans.impl.inst2xsd.RussianDollStrategy;

import es.ucm.fdi.gaia.jcolibri.cbrcore.CBRCase;
import es.ucm.fdi.gaia.jcolibri.cbrcore.CBRCaseBase;
import es.ucm.fdi.gaia.jcolibri.cbrcore.CaseBaseFilter;
import es.ucm.fdi.gaia.jcolibri.cbrcore.Connector;
import es.ucm.fdi.gaia.jcolibri.exception.InitializingException;
import es.ucm.fdi.gaia.jcolibri.method.retain.StoreCasesMethod;
import es.ucm.fdi.gaia.jcolibri.method.retrieve.RetrievalResult;
import es.ucm.fdi.gaia.jcolibri.method.retrieve.selection.SelectCases;
import es.ucm.fdi.ici.c2122.practica3.grupo05.utils.PathDistance;
import es.ucm.fdi.ici.c2122.practica5.grupo05.CBRengine.CustomPlainTextConnector;
import pacman.game.Constants.MOVE;
import pacman.game.Game;

public class MsPacManCachedLinearCaseBase implements CBRCaseBase{

	private Connector connector;
	private Collection<CBRCase> originalCases;
	private Collection<CBRCase> workingCases;
	private Collection<CBRCase> casesToRemove;
	private Collection<CBRCase> edibleCases;
	private Collection<CBRCase> chasingCases;
	private Boolean mspacmanDied=false, loop=false;


	/**
	 * Closes the case base saving or deleting the cases of the persistence media
	 */
	public void close() {
		workingCases.removeAll(casesToRemove);
		edibleCases.removeAll(casesToRemove);
		chasingCases.removeAll(casesToRemove);

		Collection<CBRCase> casesToStore = new ArrayList<>(workingCases);
		casesToStore.removeAll(originalCases);

		Set<Integer> casesIdsToRemove = new TreeSet<>();
		casesToRemove.forEach(c -> casesIdsToRemove.add((int) c.getID()));

		// TODO: quitar casting, y modificar clase para que guarde CustomPlainTextConnector
		// en vez de Connector
		((CustomPlainTextConnector)connector).deleteCases(casesIdsToRemove);
		connector.storeCases(casesToStore);
		connector.close();
	}

	/**
	 * Forgets cases. It only removes the cases from the storage media when closing.
	 */
	public void forgetCases(Collection<CBRCase> cases) {
		workingCases.removeAll(cases);
		casesToRemove.addAll(cases);
		edibleCases.removeAll(cases);
		chasingCases.removeAll(cases);
	}

	/**
	 * Returns working cases.
	 */
	public Collection<CBRCase> getCases() {
		return workingCases;
	}

	public Collection<CBRCase> getEdibleCases() {
		return edibleCases;
	}
	
	public Collection<CBRCase> getChasingCases() {
		return chasingCases;
	}

	/**
	 * TODO.
	 */
	public Collection<CBRCase> getCases(CaseBaseFilter filter) {
		// TODO
		return null;
	}

	/**
	 * Initializes the Case Base with the cases read from the given connector.
	 */
	public void init(Connector connector) throws InitializingException {
		this.connector = connector;
		originalCases = this.connector.retrieveAllCases();
		workingCases = new java.util.ArrayList<CBRCase>(originalCases);
		casesToRemove = new ArrayList<>();
		edibleCases = new ArrayList<CBRCase>();
		chasingCases = new ArrayList<CBRCase>();

		edibleCases = new ArrayList<CBRCase>();
		chasingCases = new ArrayList<CBRCase>();

		
		for (CBRCase c : originalCases) {
			MsPacManDescription descripcion = (MsPacManDescription) c.getDescription();
			if (descripcion.getNumEdible()>0) {
				edibleCases.add(c);
			}
			else {
				chasingCases.add(c);
			}
		}
	}


	/**
	 * Learns cases that are only saved when closing the Case Base.
	 */
	public void learnCases(Collection<CBRCase> cases) {
		workingCases.addAll(cases);
		
		for (CBRCase c : cases) {
			MsPacManDescription descripcion = (MsPacManDescription) c.getDescription();
			if (descripcion.getNumEdible()>0) {
				edibleCases.add(c);
			}
			else {
				chasingCases.add(c);
			}
		}
	}




}
