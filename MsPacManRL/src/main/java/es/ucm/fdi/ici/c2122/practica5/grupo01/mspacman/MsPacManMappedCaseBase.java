package es.ucm.fdi.ici.c2122.practica5.grupo01.mspacman;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

import es.ucm.fdi.gaia.jcolibri.cbrcore.CBRCase;
import es.ucm.fdi.gaia.jcolibri.cbrcore.CBRCaseBase;
import es.ucm.fdi.gaia.jcolibri.cbrcore.CaseBaseFilter;
import es.ucm.fdi.gaia.jcolibri.cbrcore.Connector;
import es.ucm.fdi.gaia.jcolibri.exception.InitializingException;
import es.ucm.fdi.ici.c2122.practica5.grupo01.utils.SortedArrayList;

/**
 * Nuestra base de casos para Mspacman estructurada por nivel y junctions
 *
 */
public class MsPacManMappedCaseBase implements CBRCaseBase {

	private Connector connector;
	private Collection<CBRCase> originalCases;
	private Map<Integer,Map<Integer,Collection<CBRCase>>> workingCases;
	private Collection<CBRCase> workingCasesLinear; //esto actua como una instancia paralela a workingCases pero estructurada de forma lineal en vez de mapeada
	private Collection<CBRCase> casesToRemove;
	
	@Override
	public void init(Connector connector) throws InitializingException {
		this.connector = connector;
		originalCases = this.connector.retrieveAllCases();
		workingCases = new HashMap<Integer,Map<Integer,Collection<CBRCase>>>();
		for(CBRCase caso : originalCases) { // para cada caso:
			int nivel = ((MsPacManDescription)caso.getDescription()).getLevel(); // ponemos nivel
			int junction = ((MsPacManDescription)caso.getDescription()).getPosPacman(); // ponemos junction
			if(!workingCases.containsKey(nivel)) workingCases.put(nivel, new HashMap<Integer,Collection<CBRCase>>()); // si en working cases no esta el nivel, lo metemos
			if(!workingCases.get(nivel).containsKey(junction)) workingCases.get(nivel).put(junction, new ArrayList<CBRCase>()); // lo mismo para junctions para cada nivel
			workingCases.get(nivel).get(junction).add(caso);
		}
		workingCasesLinear = new ArrayList<>(originalCases);
		casesToRemove = new ArrayList<>();
	}

	@Override
	public void close() {
		
		Collection<CBRCase> casesToStore = new ArrayList<>();
		
		for(Map.Entry<Integer,Map<Integer,Collection<CBRCase>>> entry : workingCases.entrySet()) {
			for(Map.Entry<Integer,Collection<CBRCase>> entry2 : entry.getValue().entrySet()) {
				entry2.getValue().removeAll(casesToRemove);
				casesToStore.addAll(entry2.getValue());
			}
		}
		
		workingCasesLinear.removeAll(casesToRemove); // para cerrar quitamos todos los casos
		casesToStore.removeAll(originalCases);
		
		class ComparableCBRCase implements Comparable<ComparableCBRCase>{
			CBRCase c;
			public ComparableCBRCase(CBRCase c) {
				this.c = c;
			}
			@Override
			public int compareTo(ComparableCBRCase o) {
				return (int)this.c.getID() - (int)o.c.getID(); // comparamos basado en ID
			}
			
		};
		
		Collection<ComparableCBRCase> comparableCasesToStore = new SortedArrayList<ComparableCBRCase>();
		for(CBRCase caso: casesToStore) {
			comparableCasesToStore.add( new ComparableCBRCase(caso) );
		}
		casesToStore.clear();
		for(ComparableCBRCase caso: comparableCasesToStore) {
			casesToStore.add( caso.c );
		}
		
		connector.storeCases(casesToStore);
		connector.close();
	}

	@Override
	public Collection<CBRCase> getCases() {		
		return workingCasesLinear;
	}

	@Override
	public Collection<CBRCase> getCases(CaseBaseFilter filter) { // obtenemos casos por nivel & junction
		MsPacManCaseBaseFilter filtro = (MsPacManCaseBaseFilter) filter;
		if(!workingCases.containsKey(filtro.getNivel())) workingCases.put(filtro.getNivel(), new HashMap<Integer,Collection<CBRCase>>());
		if(!workingCases.get(filtro.getNivel()).containsKey(filtro.getJunction())) workingCases.get(filtro.getNivel()).put(filtro.getJunction(), new ArrayList<CBRCase>());
		return workingCases.get(filtro.getNivel()).get(filtro.getJunction());
	}

	@Override
	public void learnCases(Collection<CBRCase> cases) {
		for(CBRCase caso : cases) {
			int nivel = ((MsPacManDescription)caso.getDescription()).getLevel();
			int junction = ((MsPacManDescription)caso.getDescription()).getPosPacman();
			if(!workingCases.containsKey(nivel)) workingCases.put(nivel, new HashMap<Integer,Collection<CBRCase>>());
			if(!workingCases.get(nivel).containsKey(junction)) workingCases.get(nivel).put(junction, new ArrayList<CBRCase>());
			workingCases.get(nivel).get(junction).add(caso);
		}
		workingCasesLinear.addAll(cases); // añadimos casos que funcionan
	}

	@Override
	public void forgetCases(Collection<CBRCase> cases) {
		for(CBRCase caso : cases) {
			int nivel = ((MsPacManDescription)caso.getDescription()).getLevel();
			int junction = ((MsPacManDescription)caso.getDescription()).getPosPacman();
			if(!workingCases.containsKey(nivel)) workingCases.put(nivel, new HashMap<Integer,Collection<CBRCase>>());
			if(!workingCases.get(nivel).containsKey(junction)) workingCases.get(nivel).put(junction, new ArrayList<CBRCase>());
			workingCases.get(nivel).get(junction).remove(caso);
		}
		workingCasesLinear.removeAll(cases); // olvidamos casos
	}
	
}
