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

import java.util.ArrayList;
import java.util.HashMap;

import openOptima.AlgorithmTask;
import openOptima.NoSolutionException;
import openOptima.NotImplementedException;
import openOptima.PerformanceStatIntf;
import openOptima.graph.Vertex;
import openOptima.network.Arc;
import openOptima.network.Network;
import openOptima.network.Node;

/**
 * This class is an implementation of Dijkstra shortest path algorithm. See 
 * <a href="http://search.barnesandnoble.com/booksearch/isbnInquiry.asp?r=1&EAN=9780824786021">
 * Optimization Algorithm for Networks and Graphs</A>
 * 
 * @author Yaxiong Lin
 *
 */
public class DijkstraAlgorithm extends AlgorithmTask implements ShortestPathAlgorithmIntf, PerformanceStatIntf {
	protected Network graphObj;
	protected Node startNode;
	protected Node endNode;  // 0 if wants to find shortest paths from startNode to all nodes in the network
	protected HashMap labeledArcList;
	protected HashMap labeledNodeList;
	protected boolean shortestPathFound=false;
	protected boolean negativeAdjust = true;
	protected double stopAtDist = Double.MAX_VALUE;
	protected long millisTook=0;
	protected long lastIterationStartMillis; 
	protected long lastIterationEndMillis; 
	protected long iterationCount;
	private java.util.ArrayList<Vertex> unreachableNodeList;
	
	/**
	 * returns the number of milliseconds took in the last algorithm iteration.
	 * @return milliseconds
	 */
	public long getLastIterationMillis() { return this.lastIterationEndMillis - this.lastIterationStartMillis; }
	
	/**
	 * sets the start node to find the shortest path from.
	 */
	public void setStartNode (int fromNode_p) {
		this.startNode = this.graphObj.getNode(fromNode_p);
	}
	
	/**
	 * sets the end node to fidn the shortest path to.  End node is optional to find shortest path.
	 * 
	 */
	public void setEndNode (int toNode_p) {
		this.endNode = this.graphObj.getNode(toNode_p);
	}
	
	/**
	 * sets the threshhold value to abort the search.  Use this method to stop searching if the shortest path can not be
	 * found within the threshhold value.
	 * @param stopDist_p distance
	 */
	public void setStopAtDist (double stopDist_p) { this.stopAtDist = stopDist_p; }

	/**
	 * constructor with a network/graph object that contains the nodes/arcs.
	 * @param graphObj_p graph object
	 */
	public DijkstraAlgorithm (Network graphObj_p) {
		init (graphObj_p);
	}

	/**
	 * Constructor.
	 *
	 */
	public DijkstraAlgorithm () {} 
	
	/**
	 * Sets the network object to be optimized (find shortest path).
	 */
	public void init (Network graphObj_p) {
		this.graphObj = graphObj_p;
		this.shortestPathFound= false;
	}

	/**
	 * to cause the shortest path algorithm to be run again on the next call to any of the
	 * getShortestPath () methods.
	 *
	 */
	public void reset () { this.shortestPathFound = false; }
	
	/**
	 * sets the threshhold to stop the algorithm when any of the shortest path found is
	 * longer than the stopAt_p. 
	 * @param stopAt_p distance
	 */
	public void setStopAt (double stopAt_p) {
		this.stopAtDist = stopAt_p;
	}

	/**
	 * returns the shortest path object which contains the shortest path edges from
	 * node fromNode_p to node toNode_p.
	 * @param fromNode_p node id (integer)
	 * @param toNode_p node id (integer)
	 * @return ShortestPath object
	 * @exception NoSolutionException if the shortest path can not be found.
	 */
	public ShortestPath getShortestPath(int fromNode_p, int toNode_p) throws NoSolutionException {
		if (this.graphObj==null) throw new NoSolutionException ("init method must be called first.");
		Node fromNode = this.graphObj.getNode(fromNode_p);
		Node toNode = this.graphObj.getNode(toNode_p);
		return getShortestPath(fromNode, toNode);
	}

	/**
	 * returns the shortest path object which contains the shortest path edges from
	 * node fromNode_p to node toNode_p.
	 * @param fromNode_p from node object
	 * @param toNode_p to node object
	 * @return ShortestPath object that contains the edges of the shortest path
	 * @throws NoSolutionException exception
	 */
	public ShortestPath getShortestPath(Node fromNode_p, Node toNode_p) throws NoSolutionException {
		if (fromNode_p==null || toNode_p==null) return null;
		if (!this.shortestPathFound || fromNode_p!=this.startNode || (this.endNode!=null && toNode_p!=this.endNode)) {
			if (!optimize(fromNode_p, toNode_p)) {
				throw new NoSolutionException ("Unable to find path from " + fromNode_p.getMarker() + " to " + toNode_p.getMarker());
			}
		}
		return genShortestPath (fromNode_p, toNode_p);
	}


	/**
	 * returns the shortest paths from the node fromNode_p to all nodes in the graph.
	 * @param fromNode_p node id (integer)
	 * @exception NoSolutionException if the shortest path can not be found from the node
	 * fromNode_p to any of the node in the graph.
	 * @return array of ShortestPath
	 */
	public ShortestPath [] getShortestPaths(int fromNode_p) throws NoSolutionException  {
		if (this.graphObj==null) throw new NoSolutionException ("init method must be called first.");
		Node fromNode = this.graphObj.getNode(fromNode_p);
		return getShortestPaths(fromNode);
	}

	/**
	 * returns the shortest paths from the node fromNode_p to all nodes in the graph.
	 * @param fromNode_p node id (Node)
	 * @exception NoSolutionException if the shortest path can not be found from the node
	 * fromNode_p to any of the node in the graph.
	 * @return array of ShortestPath
	 */
	public ShortestPath[] getShortestPaths(Node fromNode_p) throws NoSolutionException {
		this.unreachableNodeList = new java.util.ArrayList<Vertex>();
		if (fromNode_p==null) return null;
		if (!this.shortestPathFound || !(fromNode_p==this.startNode) || this.endNode!=null) {
			if (!optimize(fromNode_p, null)) {
				StringBuffer tempBuf = new StringBuffer("unable to find the path(s) from ");
				tempBuf.append(fromNode_p.getMarker()).append(" to node(s): ");
				this.unreachableNodeList.add(fromNode_p);
				java.util.ArrayList vertexList = this.graphObj.getVertexList(true);
				for (int i=0; i<vertexList.size(); i++) {
					Vertex vertexObj = (Vertex) vertexList.get(i);
					if (this.labeledNodeList.containsKey(vertexObj)) continue;
					tempBuf.append(vertexObj.getMarker()).append(", ");
					this.unreachableNodeList.add(vertexObj);
				}
				throw new NoSolutionException (tempBuf.substring(0, tempBuf.length()-2));
			}
		}
		
		java.util.ArrayList pathList = new java.util.ArrayList();
		java.util.Iterator it = this.labeledNodeList.entrySet().iterator();
		while (it.hasNext()) {
			java.util.Map.Entry entry = (java.util.Map.Entry) it.next();
			LabeledNode labeledNode = (LabeledNode) entry.getValue();
			if (labeledNode.nodeObj!=fromNode_p) {
				ShortestPath pathObj = genShortestPath(fromNode_p, labeledNode.nodeObj);
				if (pathObj!=null) pathList.add(pathObj);
			}
		}
		return (ShortestPath[]) pathList.toArray(new ShortestPath [pathList.size()]);
		
	}

	/**
	 * finding shortest paths between all pairs of nodes in the graph is not supported
	 * by Dijkstra algorithm.  The alternative is to call Dijkstra for each node in the graph.
	 */
	public ShortestPath[] getShortestPaths() throws NotImplementedException {
		throw new NotImplementedException ("Dijkstra algorithm does not support shortest path between all pairs of nodes. Try to call getShortestPaths(fromNode_p) for each node in the graph.");
	}

	/**
	 * construct the shortest path after the algorithm has been run.
	 * @param startNode_p
	 * @param endNode_p
	 * @return
	 */
	private ShortestPath genShortestPath (Node startNode_p, Node endNode_p) {
		if (startNode_p==null || endNode_p==null) return null;
		ShortestPath pathObj = new ShortestPath ();
		java.util.ArrayList edgeList = backTrackShortestPath((LabeledNode)this.labeledNodeList.get(endNode_p));
		pathObj.setPathArcs(startNode_p, endNode_p, (Arc[]) edgeList.toArray(new Arc[edgeList.size()]));
		
		return pathObj;
	}

	
	/*
	 * run the Dijkstra algorithm.
	 * @param startNode_p start node (Node)
	 * @param endNode_p end node (Node), null for finding shortest paths to all nodes.
	 * @returns null if any of the shortest path is not found
	 */
	private boolean optimize (Node startNode_p, Node endNode_p) throws NoSolutionException {

		long startMillis = System.currentTimeMillis();
		this.shortestPathFound = false;
		this.startNode = startNode_p;
		this.endNode = endNode_p;
		this.labeledArcList = new java.util.HashMap();
//		this.labeledNodeList = new java.util.HashMap(this.graphObj.getNodeCount());
		this.labeledNodeList = new java.util.HashMap();
		this.iterationCount=0;
		this.lastIterationStartMillis = 0;
		this.lastIterationEndMillis = 0;
		
		LabeledNode nextLabeledNode=null;
	
		int maxLoop = this.graphObj.getNodeCount();
		int i=0;
		try {
			for (i=0; i<maxLoop; i++) {
				long iterationStart = System.currentTimeMillis();
				nextLabeledNode = labelNext ();
				if (nextLabeledNode.dist>this.stopAtDist) {
					this.shortestPathFound=false;
					break; 
				}
				if (this.endNode!=null && nextLabeledNode.nodeObj == this.endNode) {
					this.shortestPathFound = true;
					break;
				}
				this.lastIterationStartMillis = iterationStart;
				this.lastIterationEndMillis = System.currentTimeMillis();
				this.iterationCount++;
			}
		}
		catch (Exception e) {
			throw new NoSolutionException ("Shortest path not found: " + this.getStatus());
		}
		finally {
			this.millisTook = System.currentTimeMillis() - startMillis;
		}
		if (this.shortestPathFound) {
			return true;
		}
		else {
			return false;
		}
//		return this.shortestPathFound;
	}
	
	/**
	 * returns a string that contains the status of last algorithm execution.
	 * @return
	 */
	private String getStatus () {
		StringBuffer retBuf = new StringBuffer("Shortest path found for ");
		java.util.Iterator it = this.labeledNodeList.entrySet().iterator();
		java.util.Map.Entry entry;
		while (it.hasNext()) {
			entry = (java.util.Map.Entry)  it.next();
			Node nodeObj = (Node) entry.getValue();
			retBuf.append(nodeObj.toString()).append(",");
		}
		return retBuf.substring(0, retBuf.length()-1);
	}
	
	/**
	 * removes the labeled nodes that meet certain criteria.
	 */
	private boolean adjustLabeledNode (double costThreshhold_p) {
		return true;
	}

	/*
	 * finds the next node that has not been labeled with the minimum cost.
	 * @return LabelNode node object found to be labeled
	 * @exception if unable to find an unlabeled node to label, i.e. no solution
	 */
	private LabeledNode labelNext () throws Exception {
		Node newLabeledNode = null;
		java.util.Iterator it;
		java.util.Map.Entry entry;
		Arc tempArc, minTempArc = null;
		LabeledNode labeledNode;

		if (this.labeledArcList.isEmpty()) {
			newLabeledNode = this.startNode;
			labeledNode = new LabeledNode(newLabeledNode, 0, null);
		}
		else {
			it = this.labeledArcList.entrySet().iterator();
			double tempCost, minCost = Integer.MAX_VALUE;
			while (it.hasNext()) {
				entry = (java.util.Map.Entry) it.next();
				tempCost = ((Double) entry.getValue()).doubleValue();
				tempArc = (Arc) entry.getKey();
				if (minCost>tempCost) {
					minTempArc = tempArc;
					minCost = tempCost;
				}
			}
			if (minTempArc==null) throw new Exception ("can not find the next node to label");
			newLabeledNode = minTempArc.getToNode();
			if (this.labeledNodeList.containsKey(newLabeledNode)) {
				newLabeledNode = minTempArc.getFromNode();
				labeledNode = new LabeledNode(newLabeledNode, minCost, minTempArc);
				labeledNode.reversed = true;
			}
			else labeledNode = new LabeledNode(newLabeledNode, minCost, minTempArc);
			
		}
		this.labeledNodeList.put(newLabeledNode, labeledNode);
		
		adjustLabeledNode(labeledNode.dist);

		if (this.endNode==null && this.labeledNodeList.size()==this.graphObj.getNodeCount() || 
			labeledNode.nodeObj == this.endNode ) {
			this.shortestPathFound = true;
			return labeledNode;
		}

		// remove the arcs that have both nodes labeled or directed arc with end node labeled from labeled arc list
		it = this.labeledArcList.entrySet().iterator();
		while (it.hasNext()) {
			entry = (java.util.Map.Entry) it.next();
			tempArc = (Arc) entry.getKey();
			if (tempArc.isDirected() && tempArc.getToNode()==newLabeledNode ||
				this.labeledNodeList.containsKey(tempArc.getFromNode()) && this.labeledNodeList.containsKey(tempArc.getToNode())) 
				it.remove();
		}
		
		// find new arcs to label
		ArrayList tempList = this.graphObj.getArcsFrom(newLabeledNode);
		for (int i=0; i<tempList.size(); i++) {
			tempArc = (Arc) tempList.get(i);
			if (!this.labeledNodeList.containsKey(tempArc.getToNode()) || 
				!this.labeledNodeList.containsKey(tempArc.getFromNode()) && !tempArc.isDirected()) {
				this.labeledArcList.put(tempArc, new Double(labeledNode.dist + tempArc.getDist()));
			}
		}
		
		if (this.labeledArcList.isEmpty() && this.graphObj.getNodeCount() <= this.labeledNodeList.size()) {
			this.shortestPathFound = true;
		}
		return labeledNode;
	}
	
	/*
	 * back track the shortest path starting from the destination node to find
	 * the arcs that make up the shortest path.
	 * @return ArrayList of arcs that made up the shortest path, null if shortest
	 * path has not been found or no solution.
	 */
	private ArrayList backTrackShortestPath (LabeledNode toNode_p) {
		ArrayList shortestPathList = new ArrayList();
		if (!this.shortestPathFound || toNode_p==null) return shortestPathList;
		LabeledNode currentNode = toNode_p;
		int idx=0;
		while (!currentNode.nodeObj.isSameAs(this.startNode) && idx<this.labeledNodeList.size()-1) {
			LabeledNode labeledNode = (LabeledNode) this.labeledNodeList.get(currentNode.nodeObj);
			if (labeledNode==null) return shortestPathList;
			shortestPathList.add(0, labeledNode.inArc);
			if (labeledNode.reversed)
				currentNode = (LabeledNode) this.labeledNodeList.get(labeledNode.inArc.getToNode());
			else currentNode = (LabeledNode) this.labeledNodeList.get(labeledNode.inArc.getFromNode());
		}
		return shortestPathList;
	}
	
	/*
	 * internal class to describe a labeled node
	 */
	public class LabeledNode {
		public Node nodeObj;
		public double dist;  // cost from startNode to this node
		public Arc inArc;  // arc into this node used to reach this node
		public boolean reversed = false;
		
		public LabeledNode (Node node_p, double dist_p, Arc inArc_p) { 
			this.nodeObj = node_p;
			this.dist = dist_p;
			this.inArc = inArc_p;
		}
		
	}


	/**
	 * returns the percentage of search status. 0 - 100 (%).
	 */
	public int getProgressPercentage() {
		if (this.labeledNodeList==null || this.labeledNodeList.isEmpty() ||
			this.graphObj.getNodeCount()<=0 ) return 0;
		int pct= Math.round((this.labeledNodeList.size())*100/this.graphObj.getNodeCount());
		return pct;
	}

	public void run() {
		try {
			this.optimize(this.startNode, this.endNode);
			this.callbackObj.callback(null);
		}
		catch (Exception e) {
			this.callbackObj.callback(e);
		}
		
	}
	
	public String getStat() {
		StringBuffer tempBuf = new StringBuffer("Dijkstra Algorithm for Shortest Path\n");
		tempBuf.append (this.graphObj.getStat()).append("\n");
		if (this.shortestPathFound) {
			tempBuf.append("Optimization completed with ").append(this.iterationCount).append(" iterations, ");
			tempBuf.append(this.millisTook).append(" milliseconds");
		}
		else {
			tempBuf.append("Executing iteration# ").append(this.iterationCount).append(", last iteration took ");
			tempBuf.append(this.getLastIterationMillis()).append(" milliseconds");
		}
		return tempBuf.toString();
	}

	public long getMillisTook () { return this.millisTook; }
	
	public long getIterationCount() { return this.iterationCount; }

	public ArrayList<Vertex> getUnReachableVertexList() {
		return this.unreachableNodeList;
	}
	
}
