package es.ucm.fdi.ici.c2122.practica5.grupo01.ghosts;


import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

import es.ucm.fdi.gaia.jcolibri.cbrcore.CBRCase;
import es.ucm.fdi.gaia.jcolibri.cbrcore.CBRCaseBase;
import es.ucm.fdi.gaia.jcolibri.cbrcore.CaseBaseFilter;
import es.ucm.fdi.gaia.jcolibri.cbrcore.Connector;
import es.ucm.fdi.gaia.jcolibri.exception.InitializingException;


/**
 * Nuestra base de casos para Ghosts estructurada por nivel y junctions
 *
 */

public class GhostsMappedCaseBase implements CBRCaseBase {

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
		for(CBRCase caso : originalCases) {
			int nivel = ((GhostsDescription)caso.getDescription()).getLevel();
			int junction = ((GhostsDescription)caso.getDescription()).getPosGhost(); // caso por nivel y junction
			if(!workingCases.containsKey(nivel)) workingCases.put(nivel, new HashMap<Integer,Collection<CBRCase>>());
			if(!workingCases.get(nivel).containsKey(junction)) workingCases.get(nivel).put(junction, new ArrayList<CBRCase>());
			workingCases.get(nivel).get(junction).add(caso); // añadimos el caso
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
		
		workingCasesLinear.removeAll(casesToRemove);// para cerrar quitamos todos los casos
		
		casesToStore.removeAll(originalCases);

		connector.storeCases(casesToStore);
		connector.close();
	}

	@Override
	public Collection<CBRCase> getCases() {		
		return workingCasesLinear;
	}

	@Override
	public Collection<CBRCase> getCases(CaseBaseFilter filter) {
		GhostsCaseBaseFilter filtro = (GhostsCaseBaseFilter) filter; // obtenemos casos en funcion del nivel y de la junction
		if(!workingCases.containsKey(filtro.getNivel())) workingCases.put(filtro.getNivel(), new HashMap<Integer,Collection<CBRCase>>());
		if(!workingCases.get(filtro.getNivel()).containsKey(filtro.getJunction())) workingCases.get(filtro.getNivel()).put(filtro.getJunction(), new ArrayList<CBRCase>());
		return workingCases.get(filtro.getNivel()).get(filtro.getJunction());
	}

	@Override
	public void learnCases(Collection<CBRCase> cases) {
		for(CBRCase caso : cases) {
			int nivel = ((GhostsDescription)caso.getDescription()).getLevel();
			int junction = ((GhostsDescription)caso.getDescription()).getPosGhost();
			if(!workingCases.containsKey(nivel)) workingCases.put(nivel, new HashMap<Integer,Collection<CBRCase>>());
			if(!workingCases.get(nivel).containsKey(junction)) workingCases.get(nivel).put(junction, new ArrayList<CBRCase>());
			workingCases.get(nivel).get(junction).add(caso);
		}
		workingCasesLinear.addAll(cases);//añadimos los casos que funcionan
	}

	@Override
	public void forgetCases(Collection<CBRCase> cases) {
		for(CBRCase caso : cases) {
			int nivel = ((GhostsDescription)caso.getDescription()).getLevel();
			int junction = ((GhostsDescription)caso.getDescription()).getPosGhost();
			if(!workingCases.containsKey(nivel)) workingCases.put(nivel, new HashMap<Integer,Collection<CBRCase>>());
			if(!workingCases.get(nivel).containsKey(junction)) workingCases.get(nivel).put(junction, new ArrayList<CBRCase>());
			workingCases.get(nivel).get(junction).remove(caso);
		}
		workingCasesLinear.removeAll(cases);//olvidamos casos
	}
	
}
