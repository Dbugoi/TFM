package es.ucm.fdi.ici.c2122.practica5.grupo06.ghosts;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Map;
import java.util.HashMap;

import es.ucm.fdi.gaia.jcolibri.cbrcore.CBRCase;
import es.ucm.fdi.gaia.jcolibri.cbrcore.CBRCaseBase;
import es.ucm.fdi.gaia.jcolibri.cbrcore.CaseBaseFilter;
import es.ucm.fdi.gaia.jcolibri.cbrcore.Connector;
import es.ucm.fdi.gaia.jcolibri.exception.InitializingException;
import es.ucm.fdi.ici.c2122.practica5.grupo06.mspacman.MsPacmanCaseBaseFilter;

/**
 * Cached case base that only persists cases when closing.
 * learn() and forget() are not synchronized with the persistence until close() is invoked.
 * <p>
 * This class presents better performance that LinelCaseBase as only access to the persistence once.
 * This case base is used for evaluation.
 * 
 * @author Juan A. Recio-García
 */
public class GhostsTreeCaseBase implements CBRCaseBase {

	private Connector connector;
	private Collection<CBRCase> originalCases;
	private Collection<CBRCase> casesToRemove;
		
	/**
	 * Casos estructurados en forma de arbol, para
	 * poder devolver subconjuntos de todos los casos.
	 * Asi podemos calcular la similitud entre un conjunto menor
	 * de casos.
	 * 
	 * La estructura es la siguiente
	 * 		_workingCases
	 * 			-> level X 
	 * 				-> X fantasmas comestibles
	 * 						-> Lista de casos
	 * 			(...)
	 */
	private Map<Integer, Map<Integer, List<CBRCase>>> _workingCases;
	
	/**
	 * Closes the case base saving or deleting the cases of the persistence media
	 */
	public void close() {
		ArrayList<CBRCase> allWorkingCases = new ArrayList<>();
		for (Map<Integer, List<CBRCase>> cMap : _workingCases.values()) {
			for (List<CBRCase> cList : cMap.values()) {
				allWorkingCases.addAll(cList);
			}
		}
		allWorkingCases.removeAll(casesToRemove);
		
		Collection<CBRCase> casesToStore = new ArrayList<>(allWorkingCases);
		//casesToStore.removeAll(originalCases);

		connector.storeCases(casesToStore);
		connector.close();
	}

	/**
	 * Forgets cases. It only removes the cases from the storage media when closing.
	 */
	public void forgetCases(Collection<CBRCase> cases) {
		casesToRemove.addAll(cases);
	}
	
	/**
	 * Forgets case. It only removes the case from the storage media when closing.
	 */
	public void forgetCase(CBRCase casee) {
		casesToRemove.add(casee);
	}

	/**
	 * Returns all working cases.
	 */
	public Collection<CBRCase> getCases() {
		ArrayList<CBRCase> allWorkingCases = new ArrayList<>();
		for (Map<Integer, List<CBRCase>> cMap : _workingCases.values()) {
			for (List<CBRCase> cList : cMap.values()) {
				allWorkingCases.addAll(cList);
			}
		}
		return allWorkingCases;
	}

	/**
	 * Returns working cases on the level and number of edible ghosts setted in the filter.
	 * If there are no saved filtered cases, returns an empty list
	 */
	public Collection<CBRCase> getCases(CaseBaseFilter filter) {
		GhostsCaseBaseFilter ctFilter = (GhostsCaseBaseFilter) filter;
		List<CBRCase> levelEdibleCases;
		if((_workingCases.get(ctFilter.getLevel())) == null)
			levelEdibleCases = new ArrayList<CBRCase>();
		else {
			levelEdibleCases = (_workingCases.get(ctFilter.getLevel())).get(ctFilter.getNumEdible());
			if(levelEdibleCases == null)
				levelEdibleCases = new ArrayList<CBRCase>();
		}
		return levelEdibleCases;
	}

	/**
	 * Initializes the Case Base with the cases read from the given connector.
	 */
	public void init(Connector connector) throws InitializingException {
		this.connector = connector;
		originalCases = this.connector.retrieveAllCases();	
		_workingCases = new HashMap<Integer, Map<Integer, List<CBRCase>>>();
		learnCases(originalCases);
		casesToRemove = new ArrayList<>();
	}	

	/**
	 * Learns cases that are only saved when closing the Case Base.
	 */
	public void learnCases(Collection<CBRCase> cases) {
		for(CBRCase c: cases) {
			GhostsDescription res = (GhostsDescription)c.getDescription();
			if(_workingCases.containsKey(res.getLevel())) {
				int num = (res.getCloseedible() ? 1 : 0) + (res.getMidCedible() ? 1 : 0) +
						(res.getMidFedible() ? 1 : 0) + (res.getFaredible() ? 1 : 0);
				if(_workingCases.get(res.getLevel()).containsKey(num)) {
					_workingCases.get(res.getLevel()).get(num).add(c);
				}else {
					List<CBRCase> newNumCases = new ArrayList<CBRCase>();
					newNumCases.add(c);
					_workingCases.get(res.getLevel()).put(num, newNumCases);
				}
			}
			else {
				HashMap<Integer, List<CBRCase>> newMap = new HashMap<Integer, List<CBRCase>>();
				_workingCases.put(res.getLevel(), newMap);
				List<CBRCase> newLevelCases = new ArrayList<CBRCase>();
				newLevelCases.add(c);
				int num = (res.getCloseedible() ? 1 : 0) + (res.getMidCedible() ? 1 : 0) +
						(res.getMidFedible() ? 1 : 0) + (res.getFaredible() ? 1 : 0);
				_workingCases.get(res.getLevel()).put(num, newLevelCases);
			}
		}
	}

}

