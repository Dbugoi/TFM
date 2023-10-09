package es.ucm.fdi.ici.c2122.practica5.grupo07.ghosts;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;

import es.ucm.fdi.gaia.jcolibri.cbrcore.CBRCase;
import es.ucm.fdi.gaia.jcolibri.cbrcore.CBRCaseBase;
import es.ucm.fdi.gaia.jcolibri.cbrcore.CBRQuery;
import es.ucm.fdi.gaia.jcolibri.cbrcore.CaseBaseFilter;
import es.ucm.fdi.gaia.jcolibri.cbrcore.Connector;
import es.ucm.fdi.gaia.jcolibri.exception.AttributeAccessException;
import es.ucm.fdi.gaia.jcolibri.exception.InitializingException;

public class GhostsCaseBase implements CBRCaseBase {

	private Connector connector;
	private Collection<CBRCase> cases;
	private HashMap<Object, Collection<CBRCase>> index;

	private int cont;
	public int getNextId() {
		cont++;
		return cont-1;
	}
	/**
	 * Returns the cases that corresponds with the key.
	 */
	public  Collection<CBRCase> getIndexedCases(CBRCase incase)
	{
		GhostsDescription des = (GhostsDescription)incase.getDescription();
		String key = des.getLastmove().toString()+des.getEdible();
		return index.get(key);
	}
	public  Collection<CBRCase> getIndexedCases(GhostsDescription des)
	{
		String key = des.getLastmove().toString()+des.getEdible();
		return index.get(key);
	}
	/**
	 * Private method that executes the indexing of cases.
	 * @param cases
	 */
	private void indexCases(Collection<CBRCase> cases)
	{
		if(index==null)index = new HashMap<>();
		for(CBRCase c: cases)
		{
			GhostsDescription des = (GhostsDescription)c.getDescription();
			String key = des.getLastmove().toString()+des.getEdible();
			if(index.get(key)==null) {
				index.put(key, new ArrayList<CBRCase>() );
				index.get(key).add(c);
			}
			else {
				index.get(key).add(c);
			}
		}
	}
	
	/* (non-Javadoc)
	 * @see jcolibri.cbrcore.CBRCaseBase#init()
	 */
	public void init(Connector connector) {
		this.connector = connector;
		cases = this.connector.retrieveAllCases();	
		indexCases(cases);
		cont=cases.size();
	}
	
	/* (non-Javadoc)
	 * @see jcolibri.cbrcore.CBRCaseBase#close()
	 */
	public void close() {
		this.connector.close();

	}

	/* (non-Javadoc)
	 * @see jcolibri.cbrcore.CBRCaseBase#forgetCases(java.util.Collection)
	 */
	public void forgetCases(Collection<CBRCase> cases) {
		for(CBRCase c: cases)
		{
			GhostsDescription des = (GhostsDescription)c.getDescription();
			String key = des.getLastmove().toString()+des.getEdible();
			if(index.get(key)==null) {
				
			}
			else {
				index.get(key).remove(c);
			}
		}
		
	}

	/* (non-Javadoc)
	 * @see jcolibri.cbrcore.CBRCaseBase#getCases()
	 */
	public Collection<CBRCase> getCases() {
		return cases;
	}

	/* (non-Javadoc)
	 * @see jcolibri.cbrcore.CBRCaseBase#getCases(jcolibri.cbrcore.CaseBaseFilter)
	 */
	public Collection<CBRCase> getCases(CaseBaseFilter filter) {return null;
	}


	/* (non-Javadoc)
	 * @see jcolibri.cbrcore.CBRCaseBase#learnCases(java.util.Collection)
	 */
	public void learnCases(Collection<CBRCase> cases) {
		if(this.cases.size()>10000) {
			return;
		}
		connector.storeCases(cases);
		indexCases(cases);
		this.cases.addAll(cases);

	}

	

}