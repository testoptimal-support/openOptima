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

import openOptima.network.Node;

/**
 * Describes a postman path.
 * @author Yaxiong Lin
 *
 */
public class PostmanPath {
	private java.util.ArrayList pathArcs;
	
	/**
	 * Returns the total distance of the postman path.
	 * @return distance
	 */
	public double getPathDist() {
		java.util.HashMap tempList = new java.util.HashMap(this.pathArcs.size());
		if (this.pathArcs==null) return 0;
		double totalDist = 0;
		for (int i=0; i<this.pathArcs.size(); i++) {
			if (tempList.containsKey(this.pathArcs.get(i))) 
				totalDist = totalDist + ((PostmanArc)this.pathArcs.get(i)).getRepeatDist();
			else {
				totalDist = totalDist + ((PostmanArc) this.pathArcs.get(i)).getDist();
				tempList.put(this.pathArcs.get(i), this.pathArcs.get(i));
			}
		}
		return totalDist;
	}
	
	/**
	 * Sets this postman path.
	 * @param pathArcs_p list of arcs
	 */
	public void setPathArcs (java.util.ArrayList  pathArcs_p) {
		if (pathArcs_p.isEmpty()) this.pathArcs = null;
		else this.pathArcs = pathArcs_p;
	}
	
	/**
	 * Returns the starting node of the postman path.
	 * @return node object
	 */
	public Node getPathStartNode () {
		if (this.pathArcs==null || this.pathArcs.isEmpty()) return null;
		return ((PostmanArc)this.pathArcs.get(0)).getFromNode();
	}

	/**
	 * Returns the edges in the postman path in an array list.
	 * @return list of arcs
	 */
	public java.util.ArrayList getPathArcs() { return this.pathArcs; }
	
	/**
	 * Returns a String representation of this postman path.
	 * @return string
	 */
	public String toString() {
		StringBuffer retBuf = new StringBuffer("Postman Path starting at ");
		Node curNode = this.getPathStartNode();
		retBuf.append(curNode.getId()).append(", total cost: ").append(this.getPathDist()).append("\n");
		for (int i=0; i<this.pathArcs.size(); i++) {
			PostmanArc arcObj = (PostmanArc)this.pathArcs.get(i);
			if (arcObj.isDirected() || arcObj.getFromVertex().isSameAs(curNode)) {
				retBuf.append (((PostmanArc)this.pathArcs.get(i)).toString());
				curNode = ((PostmanArc)this.pathArcs.get(i)).getToNode();
			}
			else {
				retBuf.append (arcObj.toString(true));
				curNode = arcObj.getFromNode();
			}
			retBuf.append("\n");
		}
		return retBuf.toString();
	}
}
