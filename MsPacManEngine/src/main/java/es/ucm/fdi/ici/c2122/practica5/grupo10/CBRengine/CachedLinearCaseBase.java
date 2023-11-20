package es.ucm.fdi.ici.c2122.practica5.grupo10.CBRengine;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

import es.ucm.fdi.gaia.jcolibri.cbrcore.CBRCase;
import es.ucm.fdi.gaia.jcolibri.cbrcore.CBRCaseBase;
import es.ucm.fdi.gaia.jcolibri.cbrcore.CaseBaseFilter;
import es.ucm.fdi.gaia.jcolibri.cbrcore.Connector;
import es.ucm.fdi.gaia.jcolibri.exception.InitializingException;
import es.ucm.fdi.ici.c2122.practica5.grupo10.mspacman.MsPacManDescription;

/**
 * Cached case base that only persists cases when closing.
 * learn() and forget() are not synchronized with the persistence until close() is invoked.
 * <p>
 * This class presents better performance that LinelCaseBase as only access to the persistence once.
 * This case base is used for evaluation.
 * 
 * @author Juan A. Recio-García
 */
public class CachedLinearCaseBase implements CBRCaseBase {

	private Connector connector;
	private Collection<CBRCase> originalCases;
	private Map<String, Collection<CBRCase>> workingCases;
	private Collection<CBRCase> casesToRemove;
	private int nCases;
	private final int CASES_LIMIT = 2000; //TO SET MAXIMUN NUMBER OF CASES
	/**
	 * Closes the case base saving or deleting the cases of the persistence media
	 */
	public void close() {
		Collection<CBRCase> casesToStore = new ArrayList<>();
		for (Collection<CBRCase> c : workingCases.values()) {
			casesToStore.addAll(c);
		}
		casesToStore.removeAll(originalCases);

		connector.storeCases(casesToStore);
		connector.close();
	}

	/**
	 * Forgets cases. It only removes the cases from the storage media when closing.
	 */
	public void forgetCases(Collection<CBRCase> cases) {
	}

	/**
	 * Returns working cases.
	 */
	public Collection<CBRCase> getCases() {
		//not used
		return new ArrayList<CBRCase>();
	}
	
	/**
	 * TODO.
	 */
	public Collection<CBRCase> getCases(CaseBaseFilter filter) {
		//return subcategory of cases according to filter
		String key = (String) ((MovesFilter) filter).getFilter();
		if(workingCases.containsKey(key)) {
			return workingCases.get(key);
		}
		else return new ArrayList<CBRCase>();
	}

	/**
	 * Initializes the Case Base with the cases read from the given connector.
	 */
	public void init(Connector connector) throws InitializingException {
		nCases = 0;
		this.connector = connector;
		originalCases = this.connector.retrieveAllCases();	
		//store cases in subcategories for faster access, inside a Map
		workingCases = new HashMap<String, Collection<CBRCase>>();
		Iterator<CBRCase> iterator = originalCases.iterator();
		CBRCase caso;
		String key;
		while (iterator.hasNext()) {
			nCases++;
			caso = iterator.next();
			key = ((MsPacManDescription)caso.getDescription()).getValidDirections();
			//if category already exists, put case inside it
			if(workingCases.containsKey(key))
				workingCases.get(key).add(caso);
			//if category doesn't exit, create it with the case inside
			else {
				Collection<CBRCase> newCol = new ArrayList<CBRCase>();
				newCol.add(caso);
				workingCases.put(key, newCol);
			}
		}
		casesToRemove = new ArrayList<>();
	}
	

	/**
	 * Learns cases that are only saved when closing the Case Base.
	 */
	public void learnCases(Collection<CBRCase> cases) {
		//insert case in correct category only if the total limit hasn't been passed
		if(nCases < CASES_LIMIT) {
			nCases++;
			CBRCase caso = cases.iterator().next();
			String key = ((MsPacManDescription)caso.getDescription()).getValidDirections();
			if(workingCases.containsKey(key))
				workingCases.get(key).add(caso);
			else {
				Collection<CBRCase> newCol = new ArrayList<CBRCase>();
				newCol.add(caso);
				workingCases.put(key, newCol);
			}
		}
	}
	
	public boolean isFull() {
		return nCases >= CASES_LIMIT;
	}

	public Integer getCasesSize() {
		int casesSize=0;
		for (Collection<CBRCase> colection : workingCases.values()) {
			casesSize += colection.size();
        }
			
		return casesSize;
	}

}

