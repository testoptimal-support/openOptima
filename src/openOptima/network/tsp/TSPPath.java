package openOptima.network.tsp;

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
import openOptima.network.Arc;
import openOptima.network.Network;
import openOptima.network.Node;

/**
 * This class contains a Chinese Postman path.  
 * 
 * @author Yaxiong Lin
 *
 */
public class TSPPath {
	private Arc [] pathArcs;
	public double getPathDist() {
		java.util.HashMap tempList = new java.util.HashMap(this.pathArcs.length);
		if (this.pathArcs==null) return 0;
		double totalDist = 0;
		for (int i=0; i<this.pathArcs.length; i++) {
			totalDist = totalDist + this.pathArcs[i].getDist();
			tempList.put(this.pathArcs[i], this.pathArcs[i]);
		}
		return totalDist;
	}
	
	public void setPathArcs (Arc[] pathArcs_p) {
		if (pathArcs_p.length==0) this.pathArcs = null;
		else this.pathArcs = pathArcs_p;
	}
	
	public Node getPathStartNode () {
		if (this.pathArcs==null) return null;
		return this.pathArcs[0].getFromNode();
	}

	public Arc[] getPathArcs() { return this.pathArcs; }
	
	public String toString() {
		StringBuffer retBuf = new StringBuffer("Travelling Salesman Path starting at ");
		Node curNode = this.getPathStartNode();
		retBuf.append(curNode.getId()).append(", total cost: ").append(this.getPathDist()).append("\n");
		for (int i=0; i<this.pathArcs.length; i++) {
			if (this.pathArcs[i].isDirected() || this.pathArcs[i].getFromVertex().isSameAs(curNode)) {
				retBuf.append (this.pathArcs[i].toString());
				curNode = this.pathArcs[i].getToNode();
			}
			else {
				retBuf.append (this.pathArcs[i].toString(true));
				curNode = this.pathArcs[i].getFromNode();
			}
			retBuf.append("\n");
		}
		return retBuf.toString();
	}
}
