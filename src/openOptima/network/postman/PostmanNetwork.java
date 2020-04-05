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

import openOptima.NotImplementedException;
import openOptima.network.Arc;
import openOptima.network.Network;
import openOptima.network.Node;

/**
 * Network used for postman algorithm
 * @author Yaxiong Lin
 *
 */
public class PostmanNetwork extends Network {
	
	/**
	 * Constructor.
	 *
	 */
	public PostmanNetwork () { super(); }
	

	/**
	 * Adds an arc to this network.
	 */
	public PostmanArc addArc (Node fromNode_p, Node toNode_p, double dist_p, boolean directed_p) {
		PostmanArc arcObj = new PostmanArc (fromNode_p, toNode_p, dist_p, directed_p);
		addArc(arcObj);
		return arcObj;
	}

	/**
	 * Adds an arc to this network.
	 */
	public PostmanArc addArc (int fromNodeId_p, int toNodeId_p, double dist_p, boolean directed_p) {
		Node fromNodeObj, toNodeObj;
		fromNodeObj = addNode(fromNodeId_p);
		toNodeObj = addNode (toNodeId_p);
		return addArc (fromNodeObj, toNodeObj, dist_p, directed_p);
	}
	
	/**
	 * Adds an arc to this network.
	 */
	public PostmanArc addArc (int fromNodeId_p, int toNodeId_p, double dist_p, double repeatDist_p, boolean directed_p) {
		Node fromNodeObj, toNodeObj;
		fromNodeObj = addNode(fromNodeId_p);
		toNodeObj = addNode (toNodeId_p);
		PostmanArc arcObj = addArc (fromNodeId_p, toNodeId_p, dist_p, directed_p);
		arcObj.setRepeatDist(repeatDist_p);
		return arcObj;
	}

	/**
	 * @deprecated
	 * @param netObj_p
	 * @throws NotImplementedException
	 */
	private PostmanNetwork (Network netObj_p) throws NotImplementedException {
		java.util.ArrayList arcList = netObj_p.getArcList(true);
		for (int i=0; i<arcList.size(); i++) {
			Arc arcObj = (Arc) arcList.get(i);
			PostmanArc pArc = new PostmanArc (arcObj.getFromNode(), arcObj.getToNode(), arcObj.getDist(), arcObj.isDirected());
			this.addArc(pArc);
		}

	}
}
