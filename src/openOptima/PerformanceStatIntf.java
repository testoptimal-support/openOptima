package openOptima;

public interface PerformanceStatIntf {

	/**
	 * Returns a long integer that describes the effort or iterations the algorithm spent on the problem.
	 * @return integer
	 * @throws NotImplementedException exception
	 */
	public long getIterationCount() throws NotImplementedException;
	
	/**
	 * Returns # of milliseconds the algorithm took to complete the execution.
	 * @return milliseconds
	 * @throws NotImplementedException on exception
	 */
	public long getMillisTook() throws NotImplementedException;
}
