package openOptima.network.tsp;

import openOptima.NoSolutionException;
import openOptima.NotImplementedException;

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
public interface TSPAlgorithmIntf {

	/**
	 * initializes the network on which the optimization will be performed.
	 * @param probObj_p
	 */
	public void init (TravellingSalesmanProblem probObj_p) throws NotImplementedException;
	
	/**
	 * returns the single-postman path starting at fromNode_p 
	 * @param fromNode_p Node
	 * @return PostmanPath, null if postman path does not exist
	 */
	public TSPPath getPostmanPath (int fromNode_p) throws NoSolutionException;

	/**
	 * solves the n-postman problem and returns an array of single-postman paths starting at fromNode_p
	 * @param fromNode_p Node
	 * @return null if postman path does not exist
	 */
	public TSPPath [] getPostmanPaths (int fromNode_p, int n_p) throws NoSolutionException, NotImplementedException;

}
