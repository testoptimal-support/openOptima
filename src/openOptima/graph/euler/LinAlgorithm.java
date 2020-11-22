package openOptima.graph.euler;
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

import openOptima.NoSolutionException;
import openOptima.graph.Edge;
import openOptima.graph.Graph;
import openOptima.graph.Vertex;


public class LinAlgorithm implements EulerPathAlgorithmIntf {

	protected java.util.ArrayList eulerPath;
	private Graph graphObj;
	private java.util.HashMap edgeUsedCount;
	private boolean eulerian = false; 
	private int unusedEdgeCount;
	private	java.util.HashMap<Vertex, java.util.HashMap<String, Integer>> vertexUsedCountList = new java.util.HashMap<Vertex, java.util.HashMap<String, Integer>>();

	/**
	 * constructor.
	 *
	 */
	public LinAlgorithm () {
	}


	/**
	 * sets the graph/network for which the optimization will be performed.
	 */
	public void init (Graph graphObj_p) {
		this.graphObj = graphObj_p;
		this.unusedEdgeCount = this.graphObj.getEdgeInstanceCount(true);
		this.eulerPath = new java.util.ArrayList (this.unusedEdgeCount);
		this.edgeUsedCount = new java.util.HashMap (this.unusedEdgeCount);
		this.eulerian = false;
	}


	
	/*
	 * returns the arc orginating from the fromVertex_p.  Directed arc takes the priority
	 */
	protected Edge findEdge (Edge lastEdge_p, Vertex fromVertex_p, java.util.HashMap loopVertice) {
		if (this.unusedEdgeCount<=0) return null; // all edges used up
		
		java.util.HashMap<String, Integer> pathUsedCountList = this.vertexUsedCountList.get(fromVertex_p);
		if (pathUsedCountList==null) {
			pathUsedCountList = new java.util.HashMap<String, Integer>();
			this.vertexUsedCountList.put(fromVertex_p, pathUsedCountList);
		}
		
		java.util.ArrayList edgeList = this.graphObj.getEdgesFrom(fromVertex_p, true);
		Edge edgeFound = null;
		Integer usedCountFound = null;
		Integer pathCountFound = null;
		String lastEdgeID = ((lastEdge_p==null)?"START":String.valueOf(lastEdge_p.hashCode()))+"_";
		
		for (int i=0; i<edgeList.size(); i++) {
			Edge arcObj = (Edge) edgeList.get(i);
			
			Integer usedCountObj = (Integer) this.edgeUsedCount.get(arcObj);
			if (usedCountObj==null) {
				usedCountObj = new Integer (0);
				this.edgeUsedCount.put(arcObj, usedCountObj);
			}
			
			if (usedCountObj.intValue()<arcObj.getCount() && 
				arcObj.startsAt(fromVertex_p)) {
				
				// prefer direct arc.  For TestOptimal, we are dealing with directed arcs only
//				if (arcObj.isDirected()) {
//					edgeFound = arcObj;
//					usedCountFound = usedCountObj;
//					break;
//				}

				// avoid subloop in the current loop if possible
//				if (edgeFound!=null && loopVertice.containsKey(arcObj.getFromVertex()) &&
//					loopVertice.containsKey(arcObj.getToVertex())) continue;

				String curPathID = lastEdgeID+arcObj.hashCode();
				Integer pathCountObj = pathUsedCountList.get(curPathID);
				if (pathCountObj==null) {
					pathCountObj = new Integer(0);
					pathUsedCountList.put(curPathID, pathCountObj);
				}

				if (edgeFound==null || pathCountFound>pathCountObj) {
					edgeFound = arcObj;
					usedCountFound = usedCountObj;
					pathCountFound = pathCountObj;
					if (pathCountFound==0) break;
				}
			}
		}
		if (edgeFound==null) return null;
		this.edgeUsedCount.put(edgeFound, new Integer(usedCountFound+1));
		pathUsedCountList.put(lastEdgeID+edgeFound.hashCode(), new Integer(pathCountFound+1));
		this.unusedEdgeCount--;
		return edgeFound;
	}
	
	
	/**
	 * finds a loop in the graph using the unused up edges.
	 * @param startVertex_p
	 * @return
	 * @throws NoSolutionException
	 */
	private java.util.ArrayList findLoop (Vertex startVertex_p) throws NoSolutionException {
		java.util.ArrayList loopEdges = new java.util.ArrayList ();
		java.util.HashMap loopVertice = new java.util.HashMap();
		Edge edgeObj = null;
		Vertex curVertex = startVertex_p;
		do {
			edgeObj = findEdge(edgeObj, curVertex, loopVertice);
			if (edgeObj==null) {
				if (loopEdges.isEmpty()) return loopEdges;;
				throw new NoSolutionException ("Can not find loop at " + startVertex_p);
			}
			loopEdges.add(edgeObj);
			loopVertice.put(edgeObj.getFromVertex(), edgeObj);
			loopVertice.put(edgeObj.getToVertex(), edgeObj);
			if (edgeObj.isDirected()) curVertex = edgeObj.getToVertex();
			else {
				if (edgeObj.getFromVertex().isSameAs(curVertex)) curVertex = edgeObj.getToVertex();
				else curVertex = edgeObj.getFromVertex();
			}
		} while (!startVertex_p.isSameAs(curVertex) && this.unusedEdgeCount>0);
		if (!startVertex_p.isSameAs(curVertex)) throw new NoSolutionException ("Euler tour can not be found.");
		return loopEdges;
	}
	

	private void findEulerPath (Vertex startVertex_p) throws NoSolutionException {
		Vertex branchVertex = startVertex_p;
		java.util.ArrayList loopEdgeList;
		int branchIdx = 0;
		do {
			loopEdgeList = findLoop (branchVertex);
			for (int i=0; i<loopEdgeList.size(); i++) {
				this.eulerPath.add(branchIdx+i, loopEdgeList.get(i));
			}
			if (loopEdgeList.isEmpty()) {
				if (this.eulerPath.isEmpty()) throw new NoSolutionException ("Unable to generate euler path");
				Edge tempEdge = (Edge) this.eulerPath.get(branchIdx);
				if (tempEdge.getFromVertex().isSameAs(branchVertex)) branchVertex = tempEdge.getToVertex();
				else branchVertex = tempEdge.getFromVertex();
				branchIdx++;
			}
		} while (branchIdx<this.eulerPath.size());
		
		if (this.unusedEdgeCount>0) {
			throw new NoSolutionException ("Euler path does not exist.");
		}
		this.eulerian = true;
		return;
	}

	/**
	 * rebuild the euler path found to start on the new startVertext.
	 * @param newStartVertex_p
	 */
	private Edge[] rebuildEulerPath (Vertex newStartVertex_p) throws NoSolutionException {
		int idx=-1;
		for (int i=0; i<this.eulerPath.size(); i++) {
			Edge curEdge = (Edge) this.eulerPath.get(i);
			if (curEdge.adjacentTo(newStartVertex_p)) {
				idx = i;
				break;
			}
		}

		if (idx<0) throw new NoSolutionException ("Vertext not found in the graph: " + newStartVertex_p.toString());
		
		java.util.ArrayList newPath = new java.util.ArrayList (this.eulerPath.size());
		while(idx>=0) {
			this.eulerPath.add(this.eulerPath.get(0));
			this.eulerPath.remove(0);
			idx--;
		}

		return (Edge []) this.eulerPath.toArray(new Edge [this.eulerPath.size()]);	
	}

	/**
	 * finds the euler path starting at the startVertex_p.
	 * @param startVertex_p vertex
	 * @return list of edges
	 * @throws NoSolutionException no solution
	 */
	public java.util.ArrayList getEulerPath (Vertex startVertex_p) throws NoSolutionException {
		if (this.graphObj==null) throw new NoSolutionException ("Must set graphObj using init () method first.");
		if (!this.eulerian) findEulerPath (startVertex_p);
		else rebuildEulerPath(startVertex_p);
		return (java.util.ArrayList) this.eulerPath.clone();
	}
	
	class EulerEdge {
		public Edge edgeObj;
		public int repeatNum = 0; // 0 first time, first repeat (or second time)
	}
	
	public boolean isEulerian () {
		return eulerian;
	}
	
	
	/*
	 * returns the string representation of the euler tour array list received from getEulerTour method.
	 */
	public String toString () {
		if (!this.eulerian) return "No euler path has been found.  Call getEulerPath() first.";
		StringBuffer retBuf = new StringBuffer("");
		Edge curEdge = (Edge) this.eulerPath.get(0);
		retBuf.append(curEdge).append("\n");
		Vertex lastVertex = curEdge.getToVertex();
		for (int i=1; i<this.eulerPath.size(); i++) {
			curEdge = (Edge) this.eulerPath.get(i);
			if (curEdge.isDirected() || curEdge.getFromVertex().isSameAs(lastVertex)) {
				retBuf.append (curEdge.toString());
				lastVertex = curEdge.getToVertex();
			}
			else {
				retBuf.append (curEdge.toString(true));
				lastVertex = curEdge.getFromVertex();
			}
			retBuf.append("\n");
		}

		return retBuf.toString();
	}



	/**
	 * Returns an array list of edges that forms the Euler tour starting at the startVertex_p.
	 * 
	 */
	public java.util.ArrayList getEulerPath(int startVertex_p) throws NoSolutionException {
		Vertex startVertex = this.graphObj.getVertex(startVertex_p);
		if (this.graphObj==null) throw new NoSolutionException ("Must set graphObj using init () method first.");
		if (startVertex==null) throw new NoSolutionException ("Vertex " + startVertex_p + " does not existin in the graph.");
		return getEulerPath(startVertex);
	}
	
}
