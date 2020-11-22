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

import java.util.List;

import openOptima.network.Arc;
import openOptima.network.Node;

/**
 * Describes a shortest path.
 * @author Yaxiong Lin
 *
 */
public class ShortestPath {
	private Arc [] pathArcs;
	private Node startNode;
	private Node endNode;
	
	/**
	 * Returns the total distance of this shortest path.
	 * @return distance
	 */
	public double getPathDist() {
		if (this.pathArcs==null) return 0;
		double totalDist = 0;
		for (int i=0; i<this.pathArcs.length; i++) {
			totalDist=+ this.pathArcs[i].getDist();
		}
		return totalDist;
	}
	
	/**
	 * Sets the attributes for this shortest path.
	 * @param startNode_p from
	 * @param endNode_p to
	 * @param pathArcs_p arcs
	 */
	public void setPathArcs (Node startNode_p, Node endNode_p, Arc[] pathArcs_p) {
		this.startNode = startNode_p;
		this.endNode = endNode_p;
//		if (pathArcs_p.length==0) {
//			this.pathArcs = null;
//			return;
//		}
		this.pathArcs = pathArcs_p;
	}
	
	/**
	 * Returns the starting node of this shortest path.
	 * @return node
	 */
	public Node getPathStartNode () {
		return this.startNode;
	}

	/**
	 * Returns true if this shortest path starts at the node passed in.
	 * @param fromNode_p from node
	 * @return indicator
	 */
	public boolean startsAt (Node fromNode_p) {
		return this.startNode.isSameAs(fromNode_p);
	}
	
	/**
	 * Returns true if this shortest path ends at the node passed in.
	 * @param toNode_p to node
	 * @return indicator
	 */
	public boolean endsAt (Node toNode_p) {
		return this.endNode.isSameAs(toNode_p);
	}
	
	/**
	 * Returns true starts and ends at the nodes passed in.
	 * @param fromNode_p from node
	 * @param toNode_p to node
	 * @return indicator
	 */
	public boolean isFromTo (Node fromNode_p, Node toNode_p) {
		return this.startsAt(fromNode_p) && this.endsAt(toNode_p);
	}
	
	/**
	 * Returns the end node of this shortest path.
	 * @return node
	 */
	public Node getPathEndNode () {
		return this.endNode;
	}

	/**
	 * Returns the list of Arcs in this shortest path.
	 * @return arcs
	 */
	public Arc[] getPathArcs() { 
		return this.pathArcs; 
	}
	
	/**
	 * Returns a String representation of this shortest path.
	 * @return string
	 */
	public String toString() {
		StringBuffer retBuf = new StringBuffer("Shortest Path (");
		Node currentNode = this.startNode;
		retBuf.append(this.getPathStartNode().getId()).append(", ").append(this.getPathEndNode().getId()).append(")\n");
		for (int i=0; i<this.pathArcs.length; i++) {
			if (this.pathArcs[i].getFromNode().isSameAs(currentNode)) {
				retBuf.append("\t").append(this.pathArcs[i].toString()).append("\n");
				currentNode = this.pathArcs[i].getToNode();
			}
			else {
				retBuf.append("\t").append(this.pathArcs[i].toString(true)).append("\n");
				currentNode = this.pathArcs[i].getFromNode();
			}
		}
		return retBuf.toString();
	}
	
	/**
	 * Finds the shortest path that starts and ends at the nodes passed from the list of
	 * shortest paths passed in.
	 * @param spPathList_p shortest path list
	 * @param fromNode_p from node
	 * @param toNode_p to node
	 * @return shortest path
	 */
	public static ShortestPath selectShortestPath (ShortestPath[] spPathList_p, Node fromNode_p, Node toNode_p) {
		if (fromNode_p==toNode_p) return null;
		for (int i=0; i<spPathList_p.length; i++) {
			if (spPathList_p[i].isFromTo (fromNode_p, toNode_p))
				return spPathList_p[i];
		}
		return null;
	}
	

	/**
	 * returns a list of transitions starting from the home state to the first transtion which is
	 * required.
	 * @return arcs 
	 */
	public List<Arc> extractPathToFirstRequiredArc() {
		List<Arc> retList = new java.util.ArrayList<Arc>();
		for (Arc arc: this.pathArcs) {
			if (arc.getMinTraverseCount()>0) {
				return retList;
			}
			retList.add(arc);
		}
		return null;
	}
	
	/**
	 * 
	 * @param newPath_p shortest path object
	 * @return shorest path
	 */
	public ShortestPath merge(ShortestPath newPath_p) {
		if (this.endNode!=newPath_p.startNode) {
			return null;
		}
		
		ShortestPath retPath = new ShortestPath();
		retPath.pathArcs = new Arc[this.pathArcs.length + newPath_p.pathArcs.length];
		retPath.startNode = this.startNode;
		retPath.endNode = newPath_p.endNode;
		int idx = 0;
		for (Arc arc: this.pathArcs) {
			retPath.pathArcs[idx++] = arc;
		}
		
		for (Arc arc: newPath_p.pathArcs) {
			retPath.pathArcs[idx++] = arc;
		}
		return retPath;
	}
}
