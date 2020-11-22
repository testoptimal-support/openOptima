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

import java.util.Random;

import openOptima.NoSolutionException;
import openOptima.network.postman.PostmanNetwork;
import openOptima.network.postman.PostmanPath;
import openOptima.network.postman.PostmanProblem;
import openOptima.network.shortestpath.ShortestPath;
import openOptima.network.shortestpath.ShortestPathProblem;

/**
 * This class contains a set of methods to generate a graph using the random number
 * generator.
 * 
 * @author Yaxiong Lin
 *
 */
public class GraphRandomGenerator {

	private double mixedFactor;
	private int distMin;
	private int distMax;
	private java.util.ArrayList allNodeList;  
	private PostmanNetwork netObj;
	private java.util.ArrayList connectedNodeList;
	private Random randObj;
	private int distRange;
	
	/**
	 * Generates a network graph using random generator.  The command_p takes the 
	 * arguments as defined in method generate (int numNode_p, int numArc_p, double mixedFactor_p, int distMin_p, int distMax_p)
	 * 
	 * @param command_p cmd
	 * @return Network object
	 * @see #generate(int, int, double, int, int)
	 * @throws NoSolutionException no solution
	 */
	public static Network generate (String command_p) throws NoSolutionException {
		int i1 = command_p.indexOf("(")+1;
		int i2 = command_p.indexOf(")");
		command_p = command_p.substring(i1, i2);
		String [] fieldList = command_p.split(","); 
		int numNode = Integer.parseInt(fieldList[0]);
		int numEdge = Integer.parseInt(fieldList[1]);
		double mixedFactor = Double.parseDouble(fieldList[2]);
		int minCost = Integer.parseInt(fieldList[3]);
		int maxCost = Integer.parseInt(fieldList[4]);
		GraphRandomGenerator graphRand = new GraphRandomGenerator();
		PostmanNetwork netObj = (PostmanNetwork) graphRand.generate(numNode, numEdge, mixedFactor, minCost, maxCost);
		return netObj;
	}
	
	/**
	 * Generates a connected graph using the parameters passed in, that is, all nodes are connected
	 * together to for a graph.  However the graph may not be strongly connected.
	 * 
	 * @param numNode_p number of nodes in the graph
	 * @param numArc_p minimum number of arcs
	 * @param mixedFactor_p 0 for directed graph, 1 for undirected graph, .5 for 50% directed and
	 * 50% undirected edges in the graph (statistically).
	 * @param distMin_p minimum of the distance range
	 * @param distMax_p maximum of the distance range
	 * @return Network object
	 * @throws NoSolutionException no solution
	 */
	public Network generate (int numNode_p, int numArc_p, double mixedFactor_p, int distMin_p, int distMax_p) throws NoSolutionException {
		this.mixedFactor = mixedFactor_p;
		this.distMin = distMin_p;
		this.distMax = distMax_p;
		
		if (numArc_p >= numNode_p*(numNode_p-1)) throw new NoSolutionException ("numArc_p is too big");
		if (mixedFactor_p>=0.999999999 && numArc_p >= (numNode_p*(numNode_p-1)/2)) throw new NoSolutionException ("numArc_p is too big");
		if (numArc_p < numNode_p) throw new NoSolutionException ("numArc_p is too small");
		
		this.netObj = new PostmanNetwork();
		for (int i=0; i<numNode_p; i++) netObj.addNode(i+1);
		
		this.allNodeList = netObj.getVertexList(true);
		this.connectedNodeList = new java.util.ArrayList(numNode_p);
		this.randObj = new Random();
		this.distRange = this.distMax - this.distMin;
		this.connectedNodeList.add (this.allNodeList.get(0));
		int arcIdx;
		for (arcIdx = 0; arcIdx<numNode_p; arcIdx++) {
			int nextNode = arcIdx+1;
			if (nextNode>= numNode_p) nextNode = 0;
			addArc(arcIdx, nextNode);
		}
		
		while (arcIdx < numArc_p || this.connectedNodeList.size()<numNode_p) {
			addArc ();
			arcIdx++;
		}
		
		return netObj;
	}
	
	private Arc addArc (int fromNode_p, int toNode_p) {
		Node fromNodeObj;
		Node toNodeObj;
		fromNodeObj = (Node) this.connectedNodeList.get(fromNode_p);
		toNodeObj = (Node) this.allNodeList.get(toNode_p);
		return addArc (fromNodeObj, toNodeObj, directed());
	}
	
	private Arc addArc (Node fromNode_p, Node toNode_p, boolean directed_p) {
		Arc arcObj;
		int dist = this.randObj.nextInt(this.distRange) + this.distMin;
		arcObj = this.netObj.addArc (fromNode_p, toNode_p, dist, directed_p);
		if (!this.connectedNodeList.contains(toNode_p)) this.connectedNodeList.add(toNode_p);
		return arcObj;
	}
	
	private boolean directed () {
		boolean directed = false;
		if (this.mixedFactor<=0) directed = true;
		else if (this.mixedFactor>=0.999999999) directed = false;
		else if (this.randObj.nextDouble() < this.mixedFactor) directed = true;
		else directed = false;
		return directed;
	}
	
	private Arc addArc () {
		Node fromNodeObj;
		Node toNodeObj;
		Arc arcObj = null;
		boolean directed = directed();
		do {
			fromNodeObj = (Node) this.connectedNodeList.get(this.randObj.nextInt(this.connectedNodeList.size()));
			toNodeObj = (Node) this.allNodeList.get(this.randObj.nextInt(this.allNodeList.size()));
			
		} while (arcExists(fromNodeObj, toNodeObj, directed) || fromNodeObj.isSameAs(toNodeObj));
		arcObj = addArc(fromNodeObj, toNodeObj, directed);
		if (!this.connectedNodeList.contains(toNodeObj)) this.connectedNodeList.add(toNodeObj);
		return arcObj;
		
	}
	
	private boolean arcExists (Node fromNode_p, Node toNode_p, boolean directed) {
		java.util.ArrayList arcList = this.netObj.getAdjacentArcs(fromNode_p);
		for (int i=0; i<arcList.size(); i++) {
			Arc arcObj = (Arc) arcList.get(i);
			if (!directed && arcObj.adjacentTo(toNode_p) ||
				directed && arcObj.endsAt(toNode_p)) return true;
		}
		return false;
	}
	
	
	public static void main (String [] args) {

		GraphRandomGenerator graphRand = new GraphRandomGenerator();
		
		try {
			PostmanNetwork netObj = (PostmanNetwork) graphRand.generate(10, 80, 0., 2, 10);
			System.out.println (netObj.toString());
			ShortestPathProblem spObj = new ShortestPathProblem("openOptima.network.shortestpath.DijkstraAlgorithm");
			spObj.init (netObj);
			ShortestPath [] pathObj = spObj.getShortestPaths(2);
			if (pathObj==null) System.out.println ("sp not found"); 
			else {
				for (int i=0; i<pathObj.length; i++) System.out.println(pathObj[i].toString());
			}

			PostmanProblem cppObj = new PostmanProblem("openOptima.network.postman.LinZhaoAlgorithm");
//			cppObj.init (new PostmanNetwork(netObj)); 
			cppObj.init (netObj); 
			
			PostmanPath  cppPathObj = cppObj.getPostmanPath(1);
			if (cppPathObj==null) System.out.println ("cpp not found"); 
			else {
				System.out.println(cppPathObj.toString());
			}

		
		}
		catch (Exception e) {
			e.printStackTrace();
		}
		
		
	}
}
