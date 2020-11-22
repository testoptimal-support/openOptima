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
package openOptima.network.shortestpath;

import openOptima.NoSolutionException;
import openOptima.NotImplementedException;
import openOptima.graph.Vertex;
import openOptima.network.Network;

/**
 * Interfaces to be implemented by the shortest path classes.
 * @author Yaxiong Lin
 *
 */
public interface ShortestPathAlgorithmIntf {

	/**
	 * returns list of vertex that are not reachable during shortest path search with the
	 * start node in the first vertex in the list. That is, the algorithm can not find
	 * shortest path from the first vertex to all of the rest of the vertex in the list.
	 * @return list of vertex
	 */
	public java.util.ArrayList<Vertex> getUnReachableVertexList();
	
	/**
	 * returns the shortest path from fromNode_p to toNode_p 
	 * @param fromNode_p Node
	 * @param toNode_p Node
	 * @return ShortestPath, null if shortest path does not exist
	 * @throws NoSolutionException no solution
	 */
	public ShortestPath getShortestPath (int fromNode_p, int toNode_p) throws NoSolutionException ;

	/**
	 * returns an array of shortest paths from fromNode_p to all nodes in the graph.
	 * @param fromNode_p Node
	 * @return null if shortest paths does not exist
	 * @throws NoSolutionException no solution
	 */
	public ShortestPath [] getShortestPaths (int fromNode_p) throws NoSolutionException ;

	/**
	 * returns an array of shortest paths between any pairs of nodes in the graph.
	 * @return null if shortest paths does not exist
	 * @exception NotImplementedException current optimizer does not implement this function
	 * @throws NoSolutionException no solution
	 * @throws NotImplementedException not implemented
	 */
	public ShortestPath [] getShortestPaths () throws NotImplementedException, NoSolutionException ;

	
	/**
	 * initialize the network to find the shortest path on.
	 * @param graphObj_p graph object
	 */
	public void init (Network graphObj_p);

	/**
	 * returns search progress status in percentage 0 - 100%.
	 * @return percentage
	 */
	public int getProgressPercentage ();
	
	/**
	 * Returns the general algorithm status.
	 * @return string
	 */
	public String getStat();

	/**
	 * Sets the start node to find the shortest path from.
	 * @param fromNode_p from
	 */
	public void setStartNode (int fromNode_p);
	
	/**
	 * sets the end node to find the shortest path to.  Setting end node is optional.
	 * 
	 * If not called, all shortest path from the start node will be returned.
	 * @param toNode_p to node
	 */
	public void setEndNode (int toNode_p);
	
}
