package es.ucm.fdi.ici.c2122.practica5.grupo03.CBRengine;

import java.util.ArrayList;
import java.util.Collection;
import java.util.function.Predicate;
import java.util.stream.Collectors;

import es.ucm.fdi.gaia.jcolibri.cbrcore.CBRCase;
import es.ucm.fdi.gaia.jcolibri.cbrcore.CBRCaseBase;
import es.ucm.fdi.gaia.jcolibri.cbrcore.CaseBaseFilter;
import es.ucm.fdi.gaia.jcolibri.cbrcore.Connector;
import es.ucm.fdi.gaia.jcolibri.exception.InitializingException;

public class CacheLinearCaseBaseByLevel implements CBRCaseBase {

	private Connector connector;
	private Collection<CBRCase> originalCases;
	private Collection<CBRCase> workingCases;
	
	private Collection<CBRCase> workingCasesL1;
	private Collection<CBRCase> workingCasesL2;
	private Collection<CBRCase> workingCasesL3;
	
	
	private Collection<CBRCase> casesToRemove;
	
	/**
	 * Closes the case base saving or deleting the cases of the persistence media
	 */
	public void close() {
		workingCases.removeAll(casesToRemove);
		workingCasesL1.removeAll(casesToRemove);
		workingCasesL2.removeAll(casesToRemove);
		workingCasesL3.removeAll(casesToRemove);
		
		Collection<CBRCase> casesToStore = new ArrayList<>(workingCases);
		Collection<CBRCase> casesToStoreL1 = new ArrayList<>(workingCasesL1);
		Collection<CBRCase> casesToStoreL2 = new ArrayList<>(workingCasesL2);
		Collection<CBRCase> casesToStoreL3 = new ArrayList<>(workingCasesL3);
		
		casesToStore.removeAll(originalCases);
		casesToStoreL1.removeAll(originalCases);
		casesToStoreL2.removeAll(originalCases);
		casesToStoreL3.removeAll(originalCases);

		connector.storeCases(casesToStore);
		connector.storeCases(casesToStoreL1);
		connector.storeCases(casesToStoreL2);
		connector.storeCases(casesToStoreL3);
		connector.close();
	}

	/**
	 * Forgets cases. It only removes the cases from the storage media when closing.
	 */
	
	public void forgetCases(Collection<CBRCase> cases) {
		workingCases.removeAll(cases);
	}
	
	public void forgetCases(Collection<CBRCase> cases, int level) {
		workingCases.removeAll(cases);
		if(level == 1) {
			workingCasesL1.removeAll(cases);
		}
		else if(level == 2) {
			workingCasesL2.removeAll(cases);
		}
		else {
			workingCasesL3.removeAll(cases);
		}
	}

	/**
	 * Returns working cases.
	 */
	
	public Collection<CBRCase> getCases() {
		return workingCases;
	}
	
	public Collection<CBRCase> getCases(int level) {
		if(level == 1) {
			return workingCasesL1;
		}
		else if(level == 2) {
			return workingCasesL2;
		}
		else {
			return workingCasesL3;
		}
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
		
		Predicate<CBRCase> streamsPredicateL1 = item -> item.toString().contains("Mapid=0");
		Predicate<CBRCase> streamsPredicateL2 = item -> item.toString().contains("Mapid=1");
		Predicate<CBRCase> streamsPredicateL3 = item -> item.toString().contains("Mapid=2");

		workingCasesL1 = workingCases.stream().filter(streamsPredicateL1).collect(Collectors.toList());
		workingCasesL2 = workingCases.stream().filter(streamsPredicateL2).collect(Collectors.toList());
		workingCasesL3 = workingCases.stream().filter(streamsPredicateL3).collect(Collectors.toList());
				
	}
	

	/**
	 * Learns cases that are only saved when closing the Case Base.
	 */
	public void learnCases(Collection<CBRCase> cases) {
		workingCases.addAll(cases);
	}
	
	public void learnCases(Collection<CBRCase> cases, int level) {
		workingCases.addAll(cases);
		if(level == 1) {
			workingCasesL1.addAll(cases);
		}
		else if(level == 2) {
			workingCasesL2.addAll(cases);
		}
		else {
			workingCasesL3.addAll(cases);
		}
		
	}

}
