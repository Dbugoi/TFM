package es.ucm.fdi.ici.c2122.practica5.grupo05.CBRengine;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Set;
import java.util.TreeSet;
import java.util.concurrent.locks.ReadWriteLock;
import java.util.concurrent.locks.ReentrantReadWriteLock;
import es.ucm.fdi.gaia.jcolibri.cbrcore.CBRCase;
import es.ucm.fdi.gaia.jcolibri.cbrcore.CBRCaseBase;
import es.ucm.fdi.gaia.jcolibri.cbrcore.CaseBaseFilter;
import es.ucm.fdi.gaia.jcolibri.cbrcore.Connector;
import es.ucm.fdi.gaia.jcolibri.exception.InitializingException;

/**
 * Cached case base that only persists cases when closing. learn() and forget() are not synchronized
 * with the persistence until close() is invoked.
 * <p>
 * This class presents better performance that LinelCaseBase as only access to the persistence once.
 * This case base is used for evaluation.
 * 
 * @author Juan A. Recio-García
 */
public class CachedLinearCaseBase implements CBRCaseBase {
	private static final ReadWriteLock lock = new ReentrantReadWriteLock(true);
	private CustomPlainTextConnector connector;
	private Collection<CBRCase> originalCases;
	private Collection<CBRCase> workingCases;
	private Collection<CBRCase> casesToRemove;

	/**
	 * Closes the case base saving or deleting the cases of the persistence media
	 */
	public void close() {
		workingCases.removeAll(casesToRemove);

		Collection<CBRCase> casesToStore = new ArrayList<>(workingCases);
		casesToStore.removeAll(originalCases);

		Set<Integer> casesIdsToRemove = new TreeSet<>();
		casesToRemove.forEach(c -> casesIdsToRemove.add((int) c.getID()));

		lock.writeLock().lock();
		connector.deleteCases(casesIdsToRemove);
		connector.storeCases(casesToStore);
		connector.close();
		lock.writeLock().unlock();
	}

	/**
	 * Forgets cases. It only removes the cases from the storage media when closing.
	 */
	public void forgetCases(Collection<CBRCase> cases) {
		workingCases.removeAll(cases);
		casesToRemove.addAll(cases);
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
	public Collection<CBRCase> getCases(CaseBaseFilter filter) {
		// TODO
		return null;
	}

	/**
	 * Initializes the Case Base with the cases read from the given connector.
	 */
	@Override
	public void init(Connector connector) throws InitializingException {
		this.connector = (CustomPlainTextConnector) connector;

		lock.readLock().lock();
		originalCases = this.connector.retrieveAllCases();
		lock.readLock().unlock();

		workingCases = new ArrayList<>(originalCases);
		casesToRemove = new ArrayList<>();
	}


	/**
	 * Learns cases that are only saved when closing the Case Base.
	 */
	public void learnCases(Collection<CBRCase> cases) {
		workingCases.addAll(cases);
	}

}

