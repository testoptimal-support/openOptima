/**
 * openOptima: framework and implementations for commonly used algorithms in Graph Theory
 * and Network Optimization in Operations Research.  
 * 
 * Copyright (C) 2008 Yaxiong Lin
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
package openOptima.network;

import openOptima.graph.Graph;
import openOptima.graph.Vertex;

/**
 * Network containing arcs and nodes.
 * @author Yaxiong Lin
 *
 */
public class Network extends Graph {
	/**
	 * returns the number of edges with negative dist as of the graph 
	 * was created.  Any edge's dist set later will not be counted.
	 * @return
	 */
	
	private int negDistArcCount = 0;

	/**
	 * returns the number of edges with negative dist as of the graph 
	 * was created.  Any edge's dist set later will not be counted.
	 * @return integer
	 */
	public int getNegDistArcCount() { return this.negDistArcCount; }
	
	/**
	 * Constructor.
	 *
	 */
	public Network () { super(); }
	
	/**
	 *  Constructor with an array of arcs.
	 * @param arcList_p arch list
	 */
	public Network (java.util.ArrayList arcList_p) {
		super(arcList_p);
	}
	
	/**
	 * Returns an array list of arcs for this network.
	 * @param activeOnly_p true to only return the arcs that are active.
	 * @return array
	 */
	public java.util.ArrayList getArcList (boolean activeOnly_p) {
		return getEdgeList (activeOnly_p);
	}
	
	/**
	 * Adds a list of arcs to this network.
	 * @param arcList_p arch list
	 */
	public void addArcs (java.util.ArrayList arcList_p) {
		super.addEdges (arcList_p);
		Arc arcObj;
		for (int i=0; i<arcList_p.size(); i++) {
			arcObj = (Arc) arcList_p.get(i);
			if (arcObj.getDist()<0) this.negDistArcCount++;
		}
	}
	
	/**
	 * returns # of edge instances including the repeating of the edges excluding inactive
	 * edges (edgeCount &lt; 0).
	 * @param activeOnly_p if active only arcs to be retrieved
	 * @return number of arcs
	 *
	 */
	public int getArcInstanceCount (boolean activeOnly_p) {
		return super.getEdgeInstanceCount(activeOnly_p);
	}
	
	/**
	 * Returns the network type.
	 * @see Graph#Mixed 
	 * @see Graph#Directed
	 * @see Graph#Undirected
	 * @see Graph#Undefined
	 * @return graph type code
	 */
	public int getNetworkType () {
		return getGraphType();
	}
	
	
	/**
	 * Returns the node object for the node id.
	 * @param id_p node id
	 * @return node object
	 */
	public Node getNode (int id_p) {
		return (Node) super.getVertex(id_p);
	}
	
	/**
	 * Adds a node to the network.
	 * @param node_p node object
	 * @return true if added
	 */
	public boolean addNode (Node node_p) {
		return super.addVertex((Vertex) node_p);
	}
	
	/**
	 * Adds a new node with the id and marker specified.
	 * @param id_p node id
	 * @param marker_p node marker
	 * @return node object
	 */
	public Node addNode (int id_p, String marker_p) {
		Node nodeObj = this.getNode(id_p);
		if (nodeObj==null) {
			nodeObj = new Node (id_p, marker_p);
			this.addNode(nodeObj);
		}
		return nodeObj;
	}

	/** 
	 * Adds a new node with the node id.
	 * @param id_p node id
	 * @return node object
	 */
	public Node addNode (int id_p) {
		return addNode(id_p, null);
	}


	/**
	 * removes a vertex and all of adjacent edges from this graph
	 * @param node_p vertex object
	 * @return true if removed
	 */
	public boolean removeNode (Node node_p) {
		return super.removeVertex(node_p);
	}
	
	/**
	 * Adds an arc to this network.
	 * 
	 * @param fromNode_p node object
	 * @param toNode_p target node
	 * @param dist_p distance
	 * @param directed_p directed true
	 * @return arc object
	 */
	public Arc addArc (Node fromNode_p, Node toNode_p, double dist_p, boolean directed_p) {
		Arc arcObj = new Arc (fromNode_p, toNode_p, dist_p, directed_p);
		addArc(arcObj);
		return arcObj;
	}

	/**
	 * Adds an arc to this network.
	 * @param fromNodeId_p from node object
	 * @param toNodeId_p target node
	 * @param dist_p distance
	 * @param directed_p directed arc
	 * @return arc object
	 */
	public Arc addArc (int fromNodeId_p, int toNodeId_p, double dist_p, boolean directed_p) {
		Node fromNodeObj, toNodeObj;
		fromNodeObj = addNode(fromNodeId_p);
		toNodeObj = addNode (toNodeId_p);
		return addArc(fromNodeObj, toNodeObj, dist_p, directed_p);
	}

	/**
	 * Adds an arc to this network.
	 * @param arcObj_p arch object
	 */
	public void addArc (Arc arcObj_p) {
		super.addEdge(arcObj_p);
		if (arcObj_p.getDist()<0) this.negDistArcCount++;
	}
	
	/**
	 * Removes an arc from this network.
	 * @param arc_p arc object
	 * @return true if removed
	 */
	public boolean removeArc (Arc arc_p) {
		return super.removeEdge(arc_p);
	}
	
	/**
	 * Returns the list of arcs that are adjacent to the node passed in.
	 * @param node_p node object
	 * @return arc list
	 */
	public java.util.ArrayList getAdjacentArcs (Node node_p) {
		return getAdjacentArcs(node_p, true);
	}
	
	/**
	 * Returns the list of arcs that are adjacent to the node passed in.
	 * @param node_p node object
	 * @param activeOnly_p set to true to get the active arcs only.
	 * @return list o arcs
	 */
	public java.util.ArrayList getAdjacentArcs (Node node_p, boolean activeOnly_p) {
		return super.getAdjacentEdges(node_p, activeOnly_p);
	}

	/**
	 * Returns a list of arcs starting at node_p.
	 * @param node_p node object
	 * @return list of arcs
	 */
	public java.util.ArrayList getArcsFrom (Node node_p) {
		return getArcsFrom(node_p, true);
	}
	
	/**
	 * Returns a list of arcs starting at node_p.
	 * @param node_p node object
	 * @param activeOnly_p set to true to get active arcs only.
	 * @return list of arcs
	 */
	public java.util.ArrayList getArcsFrom (Node node_p, boolean activeOnly_p) {
		return super.getEdgesFrom(node_p, activeOnly_p);
	}

	/**
	 * Returns a list of arcs ending at node_p.
	 * @param node_p node object
	 * @return list of arcs
	 */
	public java.util.ArrayList getArcsTo (Node node_p) {
		return getArcsTo(node_p, true);
	}
	
	/**
	 * Returns a list of arcs ending at node_p.
	 * @param node_p node object
	 * @param activeOnly_p set to true to get the active arcs only.
	 * @return list of arcs
	 */
	public java.util.ArrayList getArcsTo (Node node_p, boolean activeOnly_p) {
		return super.getEdgesTo(node_p, activeOnly_p);
	}

	/**
	 * Return the number of nodes in this network.
	 * @return count
	 */
	public int getNodeCount() { 
		return super.getVertexCount(); 
	}
	
	/**
	 * Return the number of arcs in this network.
	 * @return count
	 */
	public int getArcCount() { return super.getEdgeCount(); }
	
	/**
	 * sets active status for all self loop arcs to active/inactive passed in.
	 * @param active_p true or false
	 * @return # of arcs affected (value changed)
	 */
	public int setSelfLoopArcActive (boolean active_p) {
		return super.setSelfLoopEdgeActive(active_p);
	}


	/**
	 * Returns a string that is a representation of this graph in a format that can
	 * be read back into openOptima.
	 */
	public String describe() {
		StringBuffer retBuf = new StringBuffer ("");
		retBuf.append("source, target, isDirected, numReq, distance\n");
		java.util.ArrayList edgeList = this.getEdgeList(true);
		for (int i=0; i<edgeList.size(); i++) 
			retBuf.append(((Arc)edgeList.get(i)).describe()).append("\n");		
		return retBuf.toString();
	}

}
