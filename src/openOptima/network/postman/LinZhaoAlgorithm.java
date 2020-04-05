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
 * This class is an impelementation of the algorithm in <a href="http://www.sciencedirect.com/science?_ob=ArticleURL&_udi=B6VC5-48M30KF-BD&_user=130561&_rdoc=1&_fmt=&_orig=search&_sort=d&view=c&_acct=C000010878&_version=1&_urlVersion=0&_userid=130561&md5=67ef9b3f0197e0651d78860717380cb9">
 *  A New Algorithm for the Directed Chinese Postman Problem</A> by Yaxiong Lin & Yongchang Zhao
 *  on <code>Computers and Operations Research</code>
 * 
 * The algorithm has been extended to cover the undirected and mixed graph/network 
 * by adding a heuristic search algorithm and to support optional traversal and max traversal.
 * The implementation of the extended algorithm can be found at my web site at <a href="http://lin.charterinternet.com/LinOptimizer/Lin-Home.html">
 * http://lin.charterinternet.com/LinOptimizer/Lin-Home.html</a>.
 * 
 * @author Yaxiong Lin
 * @version 1.0
 * 
 */
package openOptima.network.postman;

import openOptima.AlgorithmTask;
import openOptima.NoSolutionException;
import openOptima.NotImplementedException;
import openOptima.PerformanceStatIntf;
import openOptima.graph.Vertex;
import openOptima.graph.euler.LinAlgorithm;
import openOptima.network.Arc;
import openOptima.network.Node;
import openOptima.network.shortestpath.DijkstraAlgorithm;
import openOptima.network.shortestpath.ShortestPath;


/**
 * This is an implementation of <a href="http://www.sciencedirect.com/science?_ob=ArticleURL&_udi=B6VC5-48M30KF-BD&_user=130561&_rdoc=1&_fmt=&_orig=search&_sort=d&view=c&_acct=C000010878&_version=1&_urlVersion=0&_userid=130561&md5=67ef9b3f0197e0651d78860717380cb9">
 * the algorithm by Lin / Zhao published on Computers and Operations Research, 1988.</a>
 * The algorithm has been extended to cover the undirected and mixed graph/network 
 * by adding a heuristic search algorithm and to support optional traversal and max traversal.
 * The implementation of the extended algorithm can be found at my web site at <a href="http://lin.charterinternet.com/LinOptimizer/Lin-Home.html">
 * http://lin.charterinternet.com/LinOptimizer/Lin-Home.html</a>.
 * 
 * @author Yaxiong Lin
 * @version 1.0
 * 
 */
public class LinZhaoAlgorithm extends AlgorithmTask implements PostmanAlgorithmIntf, PerformanceStatIntf {

	public static final String version = "1.2.001";
	/**
	 * Postman network object
	 */
	protected PostmanNetwork networkObj;
	
	/**
	 * indicator if optimization has completed.
	 */
	protected boolean optimized;
	
	/**
	 * keeps track of # of search iterations for informational purpose
	 */
	protected int iterationCount=0;
	
	protected boolean debugging = false;
	
	protected long millisTook;
	
	protected long lastIterationStartMillis; 
	protected long lastIterationEndMillis; 
	private DijkstraAlgorithm spOptimzer = null;
	
	public String getStat() {
		StringBuffer tempBuf = new StringBuffer("LinZhao Algorithm for Directed Postman Problem, Computers & Operations Research, 1988\n");
		tempBuf.append(this.networkObj.getStat()).append("\n");
		if (this.optimized) {
			tempBuf.append("Optimization completed with ").append(this.iterationCount).append(" iterations, ");
			tempBuf.append(this.millisTook).append(" milliseconds");
		}
		else {
			tempBuf.append("Executing iteration# ").append(this.iterationCount).append(", last iteration took ");
			tempBuf.append(this.getLastIterationMillis()).append(" milliseconds");
		}
		return tempBuf.toString();
	}
	
	/**
	 * returns the number of milliseconds took in the last algorithm iteration.
	 * @return
	 */
	public long getLastIterationMillis() { return this.lastIterationEndMillis - this.lastIterationStartMillis; }
	
	/**
	 * list of working arcs from ArcDualVar used during search
	 */
	protected java.util.ArrayList arcList;  // list of all ArcDualVar
	
	/**
	 * list of arcs yet to be optimized.
	 */
	protected java.util.ArrayList optArcList;
	
	/**
	 * map of original arc from the this.network to its corresponding its ArcDualVar object
	 */
	protected java.util.HashMap arcArcMap; // map of key = Arc, value ArcDualVar
	
	/**
	 * list of temporary reverse arcs created during search
	 */
	protected java.util.ArrayList reverseArcList; // contains reverse arcs during algorithm execution

	/**
	 * list of circle paths found during the search. This is then used to construct
	 * the postman path after the optimum traverse for each arc is found.
	 */
	protected java.util.ArrayList circleList; // list of circles found during search
	
	/**
	 * list of nodes in the network which have been traversed by any of the circles
	 */
	protected java.util.HashMap searchedNodeList; // list of nodes that have been optimized meaning has optimized edges adjacent to it.

	
	/**
	 * self arcs are removed from the network before the optimization and added later
	 */
	protected java.util.ArrayList selfArcList;
	
	/**
	 * default constructor.
	 *
	 */
	public LinZhaoAlgorithm () {
		
	}
	
	/**
	 * Execute the algorithm and return the optimal postman path starting at fromNode_p.
	 * @param fromNode_p starting node #
	 * @return PostmanPath object
	 * @throws NoSolutionException if no solution can be found, typically this is caused
	 *  by the graph not strongly connected.
	 */
	public PostmanPath getPostmanPath(int fromNode_p) throws Exception {
		Node startNode = this.networkObj.getNode(fromNode_p);
		if (startNode==null) throw new NoSolutionException ("Node not found: " + fromNode_p);
		
		return this.getPostmanPath (startNode);
	}

	
	/**
	 * Execute the algorithm and return the optimal postman path starting at fromNode_p.
	 * @param fromNode_p starting node object
	 * @return PostmanPath object
	 * @throws NoSolutionException if no solution can be found, typically this is caused
	 *  by the graph not strongly connected.
	 */
	public PostmanPath getPostmanPath(Node fromNode_p) throws NoSolutionException, InterruptedException {
		if (!this.optimized) this.optimize();
		
		PostmanPath pathObj = new PostmanPath ();
		pathObj.setPathArcs(this.getEulerPath(fromNode_p));
		return pathObj;
	}
	
	/**
	 * finds the Euler tour using EulerGraph algorithm
	 * @param fromNode_p
	 * @return
	 * @throws NoSolutionException
	 */
	protected java.util.ArrayList getEulerPath (Node fromNode_p) throws NoSolutionException {
		
		LinAlgorithm eulerObj = new LinAlgorithm ();
		eulerObj.init(this.networkObj);
		java.util.ArrayList pathArcs = eulerObj.getEulerPath(fromNode_p);
		if (!eulerObj.isEulerian()) {
			throw new NoSolutionException ("Euler tour not found.");
		}

		return pathArcs;
		
	} 
	
	
	/**
	 * finds the n-postmen paths.   This is not supported by this algorithm.
	 * Use com.jOptima.algorithm.network.postman.LinHeuristicAlgorithm class instead.
	 */
	public PostmanPath[] getPostmanPaths(int fromNode_p, int n_p) throws NoSolutionException, NotImplementedException {
		throw new NotImplementedException ("n-postman is not supported.");
	}
	
	/**
	 * sets the network object to the algorithm.
	 * @throws NotImplementedException if the graph object contains undirected arcs.
	 */
	public void init(PostmanNetwork probObj_p) throws NotImplementedException {
		if (!probObj_p.isDirected()) 
			throw new NotImplementedException ("The network is either not a directed graph or contains no arcs.  Only directed graph/network with at least two arcs is supported.  For mixed and undirected graph/network, use com.jOptima.algorithm.network.postman.LinHeuristicAlgorithm class.");
		internalInit(probObj_p);
	}
	
	/**
	 * internal initialization, called by public init method.
	 * @param probObj_p
	 */
	protected void internalInit (PostmanNetwork probObj_p) {
		this.networkObj = probObj_p;
		this.optimized = false;
		this.selfArcList = new java.util.ArrayList();
		java.util.ArrayList tempList = this.networkObj.getArcList(true); // false
		PostmanArc arcObj;
		ArcDualVar arcDualVar;
		for (int i=0; i<tempList.size(); i++) {
			arcObj = (PostmanArc) tempList.get(i);
			arcObj.setCount(0);
			if (arcObj.isSelfLoop()) {
				this.selfArcList.add(arcObj);
				arcObj.inactivate();
			}
		}
		
		tempList = this.networkObj.getArcList(true);
		this.arcList = new java.util.ArrayList (tempList.size());
		this.arcArcMap = new java.util.HashMap (tempList.size());
		this.reverseArcList = new java.util.ArrayList();
		this.searchedNodeList = new java.util.HashMap (this.networkObj.getNodeCount());
		this.lastIterationStartMillis = 0;
		this.lastIterationEndMillis = 0;
		
		// creates a dualVar arc for each arc
		for (int i=0; i<tempList.size(); i++) {
			arcObj = (PostmanArc) tempList.get(i);
			arcDualVar = new ArcDualVar(arcObj);
			this.arcList.add(arcDualVar);
			this.arcArcMap.put (arcObj, arcDualVar);
		}
	}
	
	/**
	 * finds the next arc to be optimized.
	 * @return
	 */
	private ArcDualVar findNextArc(java.util.ArrayList optArcList_p) {
		
		for (int i=optArcList_p.size()-1; i>=0; i--) {
			ArcDualVar arcObj = (ArcDualVar) optArcList_p.get(i);
			if (arcObj.checkFlow()>=0) {
				optArcList_p.remove(i);
			}
			else {
				if (arcObj.getFlow()>0) return arcObj;
				
				if (this.searchedNodeList.containsKey(arcObj.arcObj.getFromNode()) ||
					this.searchedNodeList.containsKey(arcObj.arcObj.getToNode()))
					return arcObj;
			}
		}
		if (optArcList_p.isEmpty()) return null;
		else return (ArcDualVar) optArcList_p.get(0);
	}
	
	/*
	 * performs the postman problem optimization.
	 * returns the cost
	 */
	public void optimize () throws NoSolutionException, InterruptedException {
		/**
		 * Step 1: Set the initial solution: f(i,j)=0, R(i,j)=d(i,j), w(i,j)=0 for all arc (i,j) in 
		 *   the original network A. Set p(i)=0 for node i for all nodes V in orignal network.
		 */
		long startMillis = System.currentTimeMillis();
		this.optimized = false;
		this.iterationCount = 0;
		
		this.optArcList = new java.util.ArrayList(this.arcList);  // list of arcs not yet optimized
		spOptimzer = new DijkstraAlgorithm (this.networkObj);
		this.circleList = new java.util.ArrayList();
		
		for (int i=0; i<this.optArcList.size(); i++) {
			ArcDualVar tempArc = (ArcDualVar) this.optArcList.get(i);
			if (tempArc.minFij>0) tempArc.arcObj.setDist(0);
			else tempArc.arcObj.setDist(tempArc.originalDist);
		}

		this.runAlgorithm(spOptimzer);
		boolean foundUntraversedOptional = false;

		for (int i=0; i<this.arcList.size(); i++) {
			ArcDualVar tempArc = (ArcDualVar) this.arcList.get(i);
			if (tempArc.getFlow()<=0 && !this.reverseArcList.contains(tempArc.arcObj)) {
				foundUntraversedOptional = true;
				break;
			}
		}
		
		if (foundUntraversedOptional && !connectSubGraphs()) {
			this.runAlgorithm(spOptimzer);
		}
		
		this.optimized = true;
		postOptimize();
		this.resetNetwork();
		this.millisTook = System.currentTimeMillis()-startMillis;
		
		this.getStat();

	}
	
	
	/**
	 * Returns true if there is only one subgraph in the network.  Otherwise return false if there are more than one subgraphs that are not connected 
	 * by traversed arcs, and if found any, adjust the min flow required to ensure
	 * these subgraphs will be connected.  The calling method must then call runAlgoirthm() again to optimize the adjusted arcs.
	 * 
	 * Algorithm Description:
	 * <ol>
	 * 		<li>First find the first traversed arc (flow >0) and add its from and to nodes to the newly labeled node list
	 * 		<li>Set mode to Expand
	 * 		<li>labeled node list - contains nodes that are labeled (connected by traversed arcs)
	 * 		<li>newly labeled node list - contains nodes that are just labeled since last iteration
	 * 		<li>candidate node list - contains nodes that are not labeled and has no traversed arcs attached to it
	 * 		<li>Loop until all nodes are accounted (visited) by this loop
	 * 		<li><ol>
	 * 				<li>if mode is Expand
	 * 					<ol>
	 * 						<li>For each newly labeled node, loop through the outgoing arcs
	 * 						<li>If the outgoing arc is traversed: 
	 * 								if toNode is already labeled do nothing,
	 * 								else add the toNode to the newly labeled node list; 
	 * 								Remove the new node from the candidate node list if present in the list
	 * 						<li>Else add or replace the new node found to the candidate node list with indexDepth = 1
	 * 						<li>Remove current new node from the newly labeled node list
	 * 						<li>Add the new node to the labeled node list
	 * 						<li>If no newly labeled node was added to the newly labeled node list during this iteration, switch mode to Search
	 * 					</ol>
	 * 					set searchIndex = 0;
	 * 				<li>else mode is Search
	 * 					<ol>
	 * 						<li>set searchIndex++;
	 * 						<li>For each candidate node list with indexDepth<=searchIndex, find the new candidate node through its outgoing arcs.
	 * 						<li>If the outgoing arc is a traversed arc
	 * 								if toNode is already labeled, continue (no-op)
	 * 								Else 
	 * 									Back track from the toNode to a labeled node, 
	 * 									Set arcs on the path with minFlow=0 to minFlow=1, 
	 * 									Add all candidate nodes on the path to the newly labeled node list
	 * 									Remove them from candidate node list if they are present
	 * 									Increment subgraph count
	 * 									Switch mode to Expand
	 * 						</li>
	 * 						<li>Else the outgoing arc is untraversed arc
	 * 								if toNode is a labeled node, continue(no op)
	 * 								else add toNode to or update (if already present) candidate node list with indexDepth = current candidate node indexDepth+1
	 * 						</li>
	 * 					</ol>
	 * 			</ol>
	 *		</li>
	 *		<li>
	 * </ol>
	 * @return number of subgraphs found and adjusted.
	 */
	private boolean connectSubGraphs() {
		ArcDualVar traversedArc = null;
		
		// find the first traversed arc and its adjacent nodes
		for (int i=0; i<this.arcList.size(); i++) {
			ArcDualVar tempArc = (ArcDualVar) this.arcList.get(i);
			if (tempArc.getFlow()>0) {
				traversedArc = tempArc;
				break;
			}
		}
		
		if (traversedArc==null) return true;
		
		boolean oneSubgraph = true;
		int totalNodeCount = this.networkObj.getNodeCount();
		java.util.ArrayList labeledNodeList = new java.util.ArrayList(totalNodeCount);
		java.util.ArrayList newlyLabeledNodeList = new java.util.ArrayList();
		java.util.HashMap candidateNodeList = new java.util.HashMap();
		newlyLabeledNodeList.add(traversedArc.arcObj.getFromNode());

		boolean expandMode = true; // expanding labeled nodes through traversed arcs.
		int searchIndex = 0;
		while (labeledNodeList.size() + candidateNodeList.size() < totalNodeCount) {
			if (expandMode) {
				java.util.ArrayList loopNodeList = newlyLabeledNodeList;
				newlyLabeledNodeList = new java.util.ArrayList();
				for (int i=0; i<loopNodeList.size(); i++) {
					Node newNodeObj = (Node) loopNodeList.get(i);
					java.util.ArrayList tempArcList = newNodeObj.getEdgesFrom();
					for (int j=0; j<tempArcList.size(); j++) {
						Arc arcObj = (Arc) tempArcList.get(j);
						ArcDualVar dualArc = (ArcDualVar) this.arcArcMap.get(arcObj);
						if (this.reverseArcList.contains(arcObj) || dualArc==null) continue;
						Node toNode = arcObj.getToNode();
						if (dualArc.arcObj != arcObj) continue; // reverse arc, ignore
						if (dualArc.getFlow()>0) { // traversed arc
							if (labeledNodeList.contains(toNode)) continue; // already labeled
							newlyLabeledNodeList.add(toNode);
							candidateNodeList.remove(toNode);
						}
						else {
							if (labeledNodeList.contains(toNode)) continue;
							CandidateNode candNode = new CandidateNode(newNodeObj, toNode, arcObj);
							candidateNodeList.put(toNode, candNode);
						}
					}
					labeledNodeList.add(newNodeObj);
				}
				
				if (newlyLabeledNodeList.isEmpty()) {
					expandMode = false; // switch mode to find unlabeled node
					searchIndex = 0;
				}
			}
			else {
				searchIndex++;
				java.util.Iterator candNodeIT = candidateNodeList.entrySet().iterator();
				java.util.ArrayList remList = new java.util.ArrayList();
				java.util.ArrayList newList = new java.util.ArrayList();
				while(candNodeIT.hasNext()) {
					java.util.Map.Entry entry = (java.util.Map.Entry) candNodeIT.next();
					CandidateNode candidateNodeObj = (CandidateNode) entry.getValue();
					if (candidateNodeObj.indexDepth>searchIndex) continue;
					Node nodeObj = (Node) entry.getKey();
					
					java.util.ArrayList tempArcList = nodeObj.getEdgesFrom();
					for (int j=0; j<tempArcList.size(); j++) {
						Arc arcObj = (Arc) tempArcList.get(j);
						ArcDualVar dualArc = (ArcDualVar) this.arcArcMap.get(arcObj);
						if (dualArc==null || this.reverseArcList.contains(arcObj)) continue; // reverse arc, ignore
						Node toNode = arcObj.getToNode();
						if (dualArc.getFlow()>0) { // traversed arc
							if (labeledNodeList.contains(toNode)) continue;
							
							// back track and adjust, increment subgraph count
							CandidateNode nextCandNode =(CandidateNode)candidateNodeList.get(nodeObj);
							newlyLabeledNodeList.add(toNode);
							Node nextNode;
							while (nextCandNode!=null) {
								ArcDualVar dualArc2 = (ArcDualVar) this.arcArcMap.get(nextCandNode.arcObj);
								dualArc2.minFij = 1;
								nextCandNode.arcObj.setMinMaxCount(1, Integer.MAX_VALUE);
								this.optArcList.add(dualArc2);
								remList.add(nextCandNode.prevNode);
								nextCandNode = (CandidateNode) candidateNodeList.get(nextCandNode.prevNode);
								if (nextCandNode!=null && labeledNodeList.contains(nextCandNode.prevNode)) nextCandNode=null;
							}
							expandMode = true;
							oneSubgraph = false;
						}
						else {
							if (labeledNodeList.contains(toNode)) continue;
							CandidateNode canNode = (CandidateNode) candidateNodeList.get(toNode);
							if (canNode==null) {
								canNode = new CandidateNode(nodeObj, toNode, arcObj);
								newList.add(canNode);
							}
							else {
								if (canNode.indexDepth<=searchIndex) continue;
								canNode.arcObj = arcObj;
								canNode.indexDepth = searchIndex;
								canNode.prevNode = nodeObj;
							} //if
						} // if 
					} // for
				} // while
				for (int k=0; k<remList.size(); k++) candidateNodeList.remove(remList.get(k));
				for (int k=0; k<newList.size(); k++) {
					CandidateNode canNode = (CandidateNode) newList.get(k);
					candidateNodeList.put(canNode.curNode, canNode);
				}
			} // if
		} // while
		return oneSubgraph;
	}
	
	public class CandidateNode {
		
		/**
		 * The candidate node
		 */
		public Node curNode;
		
		/**
		 * previous node from which this unlabeld node was reached.
		 */
		public Node prevNode; 
		
		/**
		 * arcs that leads to this node
		 */
		public Arc arcObj;
		
		/**
		 * number of arcs (untraversed arcs) from the last labeled node.
		 */
		public int indexDepth = 1;
		
		public CandidateNode (Node prevNode_p, Node curNode_p, Arc arcObj_p) {
			this.curNode = curNode_p;
			this.prevNode = prevNode_p;
			this.arcObj = arcObj_p;
		}
		
		public int addIndexPath() { return ++this.indexDepth; }
		
	}
	
	
	/**
	 * Performs the algorithm steps.  May call this method if additional arcs are added or required flowNum is changed.
	 * @param spOptimzer_p
	 * @throws NoSolutionException
	 * @throws InterruptedException
	 */
	private void runAlgorithm(DijkstraAlgorithm spOptimzer_p) throws NoSolutionException, InterruptedException {
		while (!this.optimized && !optArcList.isEmpty()) {
			
			if (Thread.interrupted()) {
				throw new InterruptedException ("Search interrupted.");
			}
			
			long iterationStartMillis = System.currentTimeMillis();
			this.iterationCount++;

			ArcDualVar arcObj = findNextArc(optArcList);
			if (arcObj==null) break;
	
			Node t = arcObj.arcObj.getFromNode();
			Node s = arcObj.arcObj.getToNode();
			if (arcObj.checkFlow()==-1 && !arcObj.directed) {
				arcObj.arcObj.setDist(arcObj.originalDist);
				if (arcObj.reverseArcObj!=null)
					arcObj.reverseArcObj.setDist(arcObj.originalDist);
			}
			spOptimzer_p.reset();
			ShortestPath [] spPaths = spOptimzer_p.getShortestPaths(s);
			if (spPaths==null || spPaths.length==0) throw new NoSolutionException ("Graph not strongly connected. Search aborted at node: " + s);
			ShortestPath spPath = ShortestPath.selectShortestPath(spPaths, s, t);
			if (spPath==null) throw new NoSolutionException ("Graph not strongly connected. Search aborted at node: " + s);

			Arc [] loopArcList = spPath.getPathArcs();
			
			adjustFlow(arcObj, arcObj.arcObj, true);
			Node lastNode = s;
			for (int i=0; i<loopArcList.length; i++) {
				ArcDualVar tempArc = (ArcDualVar) this.arcArcMap.get(loopArcList[i]);
				if (tempArc.arcObj.getFromNode().isSameAs(lastNode)) {
					adjustFlow(tempArc, loopArcList[i], true);
					lastNode = tempArc.arcObj.getToNode();
				}
				else {
					adjustFlow(tempArc, loopArcList[i], false);
					lastNode = tempArc.arcObj.getFromNode();
				}
			}
			
			for (int j=0; j<this.arcList.size(); j++) {
				ArcDualVar tempArc = (ArcDualVar) this.arcList.get(j);
				// calculate R(i,j) dualR
				double tCost = spPath.getPathDist();
				ShortestPath spI = null;
				if (!s.isSameAs(tempArc.arcObj.getFromNode())) ShortestPath.selectShortestPath(spPaths, s, tempArc.arcObj.getFromNode());
				ShortestPath spJ = null;
				if (!s.isSameAs(tempArc.arcObj.getToNode())) ShortestPath.selectShortestPath(spPaths, s, tempArc.arcObj.getToNode());
				tempArc.Rij = Math.min((spI==null?0:spI.getPathDist()), tCost) +
						tempArc.Rij - Math.min((spJ==null?0:spJ.getPathDist()), tCost);
			}
			
			this.lastIterationStartMillis = iterationStartMillis;
			this.lastIterationEndMillis = System.currentTimeMillis();

		} // while
		return;
	}
	
	/*
	 * adds a reverse arc to the addArcList_p passed in.  If this arc is an directed arc,
	 * it does nothing and just returns.  Otherwise continue: if the reverse arc already exists,
	 * it just sets its cost to reverseCost_p. 
	 *
	 */
	protected void addReverseArc (ArcDualVar arcObj_p, double reverseCost_p) {
		if (arcObj_p.reverseArcObj!=null) {
			arcObj_p.reverseArcObj.setDist(reverseCost_p);
			arcObj_p.reverseArcObj.activate();
		}
		else {
			arcObj_p.reverseArcObj = (PostmanArc) this.networkObj.addArc(arcObj_p.arcObj.getToNode(), arcObj_p.arcObj.getFromNode(), reverseCost_p, true);
			this.reverseArcList.add(arcObj_p.reverseArcObj);
			this.arcArcMap.put(arcObj_p.reverseArcObj, arcObj_p);
		}
	}



	/*
	 * Post optimization process. 
	 */
	protected double postOptimize () {
		
		return 0; // use LinHeuristicAlgorithm package for mixed and undirected graph/network.
	}
	
	/*
	 * adjust the flow that cancels each other (flow going in opposite direction),
	 * calculate dual variables, sets shortest path distance for the arcs.
	 */
	public void adjustFlow(ArcDualVar dualArcObj_p, Arc arcObj_p, boolean forwardFlow_p) {
		if (forwardFlow_p) dualArcObj_p.fij++;
		else dualArcObj_p.fij--;

		int checkFlow = dualArcObj_p.checkFlow();
		if (checkFlow==0) {
			dualArcObj_p.arcObj.setDist(dualArcObj_p.Rij);
			if (dualArcObj_p.reverseArcObj!=null) {
				dualArcObj_p.reverseArcObj.inactivate();
			}
		}
		else if (checkFlow>0){ // over satisfied
			dualArcObj_p.arcObj.setDist(0);
			if (dualArcObj_p.reverseArcObj==null) addReverseArc(dualArcObj_p, 0); // set to negative, requires shortest path algorithm to support negative cost
			else dualArcObj_p.reverseArcObj.activate();
		}
		else {
			dualArcObj_p.arcObj.setDist(0);
			if (dualArcObj_p.reverseArcObj!=null) dualArcObj_p.reverseArcObj.inactivate();
		}
		
		this.searchedNodeList.put(arcObj_p.getFromNode(), dualArcObj_p);
		this.searchedNodeList.put(arcObj_p.getToNode(), dualArcObj_p);
		return;
	}
	
	/**
	 * removes the temporary arcs added during the algorithm search and reset
	 * arc distance, etc.
	 *
	 */
	protected void resetNetwork () {
		java.util.Iterator it = this.arcArcMap.entrySet().iterator();
		while (it.hasNext()) {
			java.util.Map.Entry entry = (java.util.Map.Entry) it.next();
			ArcDualVar dualArc = (ArcDualVar) entry.getValue();
			dualArc.arcObj.activate();
			dualArc.arcObj.setDist(dualArc.originalDist);
			dualArc.arcObj.setCount(dualArc.getFlow());
			if (dualArc.reverseArcObj==null) continue;
			this.networkObj.removeArc(dualArc.reverseArcObj);
		}
		
		for (int i=0; i<this.selfArcList.size(); i++) {
			PostmanArc arcObj = (PostmanArc) this.selfArcList.get(i);
			arcObj.activate();
			arcObj.setCount(arcObj.getMinTraverseCount());
		}
	}
	
	/**
	 * internal structure
	 * @author Yaxiong Lin
	 *
	 */
	public class ArcDualVar {
		public PostmanArc arcObj;
		public PostmanArc reverseArcObj;
		public double originalDist;
		public double Rij;
		public double wij;
		public int fij;
		public int fji;
		public int minFij;
		public int maxFij;
		public boolean directed;
		
		public ArcDualVar (PostmanArc arcObj_p) {
			this.arcObj = arcObj_p;
			this.Rij = arcObj_p.getDist();
			this.fij = 0;
			this.wij = 0;
			this.fji = 0;
			this.minFij = this.arcObj.getMinTraverseCount();
			this.maxFij = this.arcObj.getMaxTraverseCount();
			this.originalDist = this.arcObj.getDist();
			this.directed = arcObj_p.isDirected();
			if (this.minFij<=0) this.wij = this.Rij;
		}
		
		/*
		 * returns negative for not satisfied (flow# needed to satisfy), 0 for satisfied, positive#
		 * for over satisfied (flow# over the minFlowNum
		 */
		public int checkFlow () {
			int ret;
			ret = this.fij + this.fji - this.minFij;
			return ret;
		}

		public int getFlow() {
			return this.fij+this.fji;
		}

	}

	/**
	 * returns the percentage of search status. 0 - 100 (%).
	 */
	public int getProgressPercentage() {
		if (this.arcList==null || this.arcList.isEmpty() ||
			this.optArcList==null ) return 0;
		int pct= Math.round((this.arcList.size()-this.optArcList.size())*100/this.arcList.size());
		return pct;
	}

	public void run() {
		try {
			this.optimize();
			this.callbackObj.callback(null);
		}
		catch (Exception e) {
			this.callbackObj.callback(e);
		}
		
	}
	
	public long getMillisTook () { return this.millisTook; }
	
	public long getIterationCount() { return this.iterationCount; }

	
	public String toString() {
		java.util.ArrayList tempList = this.arcList;
		StringBuffer retBuf = new StringBuffer("From, To, dist, min, flow\n");
		for (int i=0; i<tempList.size(); i++) {
			ArcDualVar dualArcObj = (ArcDualVar) tempList.get(i);
			retBuf.append(dualArcObj.arcObj.getFromNode().getMarker()).append(",").append(dualArcObj.arcObj.getToNode().getMarker())
				.append(",").append(dualArcObj.arcObj.getDist()).append(",").append(dualArcObj.minFij).append(",").append(dualArcObj.fij).append("\n");
		}
		return retBuf.toString();
	}
	
	/**
	 * returns the list of vertex nodes that are not reachable during the search.
	 * @return
	 */
	public java.util.ArrayList<Vertex> getUnReachableVertexList() {
		return this.spOptimzer.getUnReachableVertexList();
	}
	
}
