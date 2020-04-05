package openOptima.network.tsp;

import openOptima.network.Arc;
import openOptima.network.Network;
import openOptima.network.Node;

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
public class TravellingSalesmanProblem extends Network {

	public TravellingSalesmanProblem () { 
		
	}
	
	public TSPNode addNode (int id_p, int minVisitCount_p) {
		TSPNode nodeObj = new TSPNode(id_p, minVisitCount_p);
		this.addNode(nodeObj);
		return nodeObj;
	}
	
	public TSPNode addNode (int id_p, int minVisitCount_p, String marker_p) {
		TSPNode nodeObj = new TSPNode(id_p, minVisitCount_p);
		nodeObj.setMarker(marker_p);
		this.addNode(nodeObj);
		return nodeObj;
	}
	
	public Arc addArc (Node fromNode_p, Node toNode_p, double dist_p, boolean directed_p) {
		Arc arcObj = new Arc (fromNode_p, toNode_p, dist_p, directed_p);
		this.addArc(arcObj);
		return arcObj;
	}

	public Arc addArc (int fromNodeId_p, int toNodeId_p, double dist_p, boolean directed_p) {
		Node fromNodeObj, toNodeObj;
		fromNodeObj = addNode(fromNodeId_p);
		toNodeObj = addNode (toNodeId_p);
		return addArc(fromNodeObj, toNodeObj, dist_p, directed_p);
	}

}
