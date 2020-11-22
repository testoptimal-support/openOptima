/**
 * openOptima: framework and implementations for commonly used algorithms in Graph Theory
 * and Network Optimization in Operations Research.  Copyright (C) 2008 Yaxiong Lin
 * 
 * This library is free software; you can redistribute it and/or modify it under the 
 * terms of the GNU Lesser General Public License as published by the Free Software 
 * Foundation; either version 2.1 of the License, or (at your option) any later version.
 * 
 * This library is distributed in the hope that it will be useful, but WITHOUT ANY 
 * WARRANTY; without even the implied warranty of MERCHANTABILITY or FITNESS FOR A 
 * PARTICULAR PURPOSE. See the GNU Lesser General Public License for more details.
 * 
 * You should have received a copy of the GNU Lesser General Public License along with 
 * this library; if not, write to the Free Software Foundation, Inc., 59 Temple Place, 
 * Suite 330, Boston, MA 02111-1307 USA 
 * 
 * @author Yaxiong Lin
 * @version 1.0
 * 
 */
package openOptima.network.postman;

import openOptima.NoSolutionException;
import openOptima.NotImplementedException;

/**
 * Interface for the postman algorithm implementation classes.
 * @author Yaxiong Lin
 *
 */
public interface PostmanAlgorithmIntf {
	
	/**
	 * initializes the network on which the optimization will be performed.
	 * @param networkObj_p problem obj
	 * @throws NotImplementedException not implemented
	 */
	public void init (PostmanNetwork networkObj_p) throws NotImplementedException;
	
	/**
	 * returns the single-postman path starting at fromNode_p 
	 * @param fromNode_p Node
	 * @return PostmanPath, null if postman path does not exist
	 * @throws Exception exception
	 */
	public PostmanPath getPostmanPath (int fromNode_p) throws Exception;

	/**
	 * solves the n-postman problem and returns an array of single-postman paths starting at fromNode_p
	 * @param fromNode_p Node
	 * @param n_p postmen count
	 * @return null if postman path does not exist
	 * @throws NoSolutionException no solution
	 * @throws InterruptedException interrupted
	 * @throws NotImplementedException not implemented
	 */
	public PostmanPath [] getPostmanPaths (int fromNode_p, int n_p) throws NoSolutionException, 
		NotImplementedException, InterruptedException;

	/**
	 * returns search progress status in percentage 0 - 100%.
	 * @return percentage
	 */
	public int getProgressPercentage ();
	
	/**
	 * return the general runtime stat
	 * @return string
	 */
	public String getStat();

}
