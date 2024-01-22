package es.ucm.fdi.ici.c2122.practica5.grupo08.ghosts;

import java.util.ArrayList;
import java.util.Collection;

import es.ucm.fdi.gaia.jcolibri.cbrcore.CBRCase;
import es.ucm.fdi.gaia.jcolibri.cbrcore.CBRCaseBase;
import es.ucm.fdi.gaia.jcolibri.cbrcore.CaseBaseFilter;
import es.ucm.fdi.gaia.jcolibri.cbrcore.CaseComponent;
import es.ucm.fdi.gaia.jcolibri.cbrcore.Connector;
import es.ucm.fdi.gaia.jcolibri.exception.InitializingException;

/**
 * Cached case base that only persists cases when closing.
 * learn() and forget() are not synchronized with the persistence until close() is invoked.
 * <p>
 * This class presents better performance that LinelCaseBase as only access to the persistence once.
 * This case base is used for evaluation.
 * 
 * @author Juan A. Recio-García
 */
public class CachedLinearCaseBaseGhost implements CBRCaseBase {

	private Connector connector;
	private Collection<CBRCase> originalCases;
	private Collection<CBRCase> workingCases;
	private Collection<CBRCase> casesToRemove;
	private Collection<CBRCase> casesEdible;
	private Collection<CBRCase> casesNoEdible;

	
	/**
	 * Closes the case base saving or deleting the cases of the persistence media
	 */
	public void close() {
		workingCases.removeAll(casesToRemove);
		
		Collection<CBRCase> casesToStore = new ArrayList<>(workingCases);
		casesToStore.removeAll(originalCases);

		connector.storeCases(casesToStore);
		connector.close();
	}

	/**
	 * Forgets cases. It only removes the cases from the storage media when closing.
	 */
	public void forgetCases(Collection<CBRCase> cases) {
		workingCases.removeAll(cases);
	}

	/**
	 * Returns working cases.
	 */
	public Collection<CBRCase> getCases() {
		return workingCases;
	}

	/**
	 * TODO.
	 */
	@Override
	public Collection<CBRCase> getCases(CaseBaseFilter filter) {
		// TODO Auto-generated method stub
		GhostCaseFilter filt = (GhostCaseFilter)filter;
		
		switch(filt.filter) {
		case EDIBLE:
			return casesEdible;
		case NO_EDIBLE:
			return casesNoEdible;
		default:
			return workingCases;
		}
	}

	/**
	 * Initializes the Case Base with the cases read from the given connector.
	 */
	public void init(Connector connector) throws InitializingException {
		this.connector = connector;
		originalCases = this.connector.retrieveAllCases();	
		workingCases = new java.util.ArrayList<CBRCase>(originalCases);
		casesToRemove = new ArrayList<>();
		
		casesEdible = new ArrayList<CBRCase>();
		casesNoEdible = new ArrayList<CBRCase>();
		for(CBRCase c : originalCases) {
			CaseComponent caseComp = c.getDescription();
			GhostsDescription msPacmanDesc = (GhostsDescription)caseComp;
			boolean edibles = false;
			
			for(int i = 0; i < 4; ++i) {
				if(msPacmanDesc.edible.values[i]) {
					casesEdible.add(c);
					edibles = true;
					break;
				}
			}
			
			if(!edibles)
				casesNoEdible.add(c);
		}
		
	}
	

	/**
	 * Learns cases that are only saved when closing the Case Base.
	 */
	public void learnCases(Collection<CBRCase> cases) {
		workingCases.addAll(cases);
	}

}

