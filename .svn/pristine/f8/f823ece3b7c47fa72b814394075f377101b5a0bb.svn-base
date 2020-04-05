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
package openOptima.graph;

/**
 * This class represents a graph that contains vertices and edges.
 * @author Yaxiong Lin
 *
 */
public class Graph {
	private java.util.HashMap vertexIntIdList = new java.util.HashMap(); //<intId, Vertext>
	private java.util.HashMap vertexIdList = new java.util.HashMap(); //<id, vertext>
	private java.util.ArrayList edgeList = new java.util.ArrayList (); // Edge
	
	private String marker;
	
	/**
	 * sets graph marker
	 */
	public void setMarker(String marker_p) { this.marker = marker_p; }
	
	/**
	 * returns the graph marker.
	 */
	public String getMarker() { return this.marker; }
	
	/**
	 * symbol for mixed graph type
	 */
	public static final int Mixed = 10;
	
	/**
	 * symbol for directed graph type
	 */
	public static final int Directed = 20;
	
	/**
	 * symbol for undirected graph type
	 */
	public static final int Undirected = 30;
	
	/**
	 * symbol for undefined graph type.  this indicates graph type is unknown.
	 */
	private static final int Undefined = 0;
	
	
	private int graphType = Undefined; 
	
	
	/**
	 * Returns true if this is a directed graph, that is the graph only contains
	 * the directed edges.
	 * @return
	 */
	public boolean isDirected() {
		if (this.getGraphType()==Directed) return true;
		else return false;
	}
	
	/**
	 * Returns true if this graph contains both directed and undirected edges.
	 * @return
	 */
	public boolean isMixed() {
		if (this.getGraphType()==Mixed) return true;
		else return false;
	}
	
	/**
	 * Returns true if this graph contains only undirected edges.
	 * @return
	 */
	public boolean isUndirected() {
		if (this.getGraphType()==Undirected) return true;
		else return false;
	}

	/**
	 * Constructor
	 *
	 */
	public Graph () { }
	
	/**
	 * Constructor
	 * @param edgeList_p
	 */
	public Graph (java.util.ArrayList edgeList_p) {
		addEdges(edgeList_p);
	}
	
	/**
	 * Returns all active edges in this graph if activeOnly_p is set to true or
	 * returns all inactive and inactive edges in this graph if activeOnly_p is set to false.
	 * @param activeOnly_p
	 * @return
	 */
	public java.util.ArrayList getEdgeList (boolean activeOnly_p) {
		return makeCopy(this.edgeList, activeOnly_p);
	}
	
	/**
	 * Adds the edges in the array passed in to this graph.
	 * @param edgeList_p
	 */
	public void addEdges (java.util.ArrayList edgeList_p) {
		Edge edgeObj;
		for (int i=0; i<edgeList_p.size(); i++) {
			edgeObj = (Edge) edgeList_p.get(i);
			this.addVertex(edgeObj.getFromVertex());
			this.addVertex(edgeObj.getToVertex());
		}
		
		this.edgeList.addAll(edgeList_p);
	}
	
	/**
	 * Returns # of edge instances including the repeating of the edges and excluding inactive
	 * edges.
	 *
	 */
	public int getEdgeInstanceCount (boolean activeOnly_p) {
		int instCount = 0;
		for (int i=0; i<this.edgeList.size(); i++) {
			Edge edgeObj = (Edge) this.edgeList.get(i);
			if (edgeObj.isActive() || !activeOnly_p) instCount = instCount + edgeObj.getCount();
		}
		return instCount;
	}
	
	/**
	 * Returns the graph type: Directed, Undirected and Mixed.  All edges active or inactive are checked 
	 * 
	 * @return
	 */
	public int getGraphType () {
		if (this.graphType!=Undefined || this.edgeList.isEmpty()) return this.graphType;
		Edge edgeObj = (Edge) this.edgeList.get(0);
		this.graphType = edgeObj.isDirected()?Directed:Undirected;
		for (int i=1; i<this.edgeList.size(); i++) {
			edgeObj = (Edge) this.edgeList.get(i);
			this.graphType = evalGraphType(this.graphType, edgeObj.isDirected());
		}
		return this.graphType;
	}
	
	
	
	private int evalGraphType (int graphType_p, boolean newEdgeDirected_p) {
		switch (graphType_p) {
		case Undefined:
			graphType_p = newEdgeDirected_p?Directed:Undirected;
			break;
		case Directed:
			if (!newEdgeDirected_p) graphType_p = Mixed;
			break;
		case Undirected:
			if (newEdgeDirected_p) graphType_p = Mixed;
			break;
		default:
		}
		return graphType_p;
	}
	
	
	/**
	 * Returns the vertex object for the vertex id passed in.
	 * @param id_p
	 * @return
	 */
	public Vertex getVertex (int id_p) {
		Integer idObj = new Integer(id_p);
		return (Vertex) this.vertexIdList.get(idObj);
	}
	
	/**
	 * Adds a vertex to this graph.
	 * @param vertex_p
	 * @return
	 */
	public boolean addVertex (Vertex vertex_p) {
		if (vertex_p==null) return false;
		if (this.vertexIdList.containsValue(vertex_p)) return false;
		this.vertexIntIdList.put (new Integer(vertex_p.getIntId()), vertex_p);
		this.vertexIdList.put (new Integer(vertex_p.getId()), vertex_p);
		return true;
	}
	
	/**
	 * Adds a vertex with the vertex id and marker to this graph.
	 * @param id_p
	 * @param marker_p
	 * @return
	 */
	public Vertex addVertex (int id_p, String marker_p) {
		Vertex vertexObj = this.getVertex(id_p);
		if (vertexObj==null) {
			vertexObj = new Vertex (id_p, marker_p);
			this.addVertex(vertexObj);
		}
		return vertexObj;
	}

	/**
	 * Adds a vertext with vertex id to this graph.
	 * @param id_p
	 * @return
	 */
	public Vertex addVertex (int id_p) {
		return addVertex(id_p, null);
	}


	/**
	 * Removes a vertex and all of its adjacent edges from this graph
	 * @param vertex_p
	 * @return
	 */
	public boolean removeVertex (Vertex vertex_p) {
		if (vertex_p==null) return false;
		
		if (this.vertexIntIdList.remove(new Integer (vertex_p.getIntId()))!=null) {
			this.vertexIdList.remove(new Integer(vertex_p.getId()));
			// remove all edges adjacent to this vertex
			java.util.ArrayList tempEdgeList = vertex_p.getEdges();
			for (int i=0; i<tempEdgeList.size(); i++) {
				Edge edgeObj = (Edge) tempEdgeList.get(i);
				edgeObj.remove();
				this.removeEdge(edgeObj);
			}
			return true;
		}

		return false;
	}
	
	/**
	 * Adds an edge to this graph.
	 * @param fromVertex_p
	 * @param toVertex_p
	 * @param directed_p
	 * @return
	 */
	public Edge addEdge (Vertex fromVertex_p, Vertex toVertex_p, boolean directed_p) {
		Edge edgeObj = new Edge (fromVertex_p, toVertex_p, directed_p);
		this.edgeList.add(edgeObj);
		this.graphType = evalGraphType(this.graphType, edgeObj.isDirected());
		return edgeObj;
	}

	/**
	 * Adds an edge to this graph.
	 * @param fromVertexId_p
	 * @param toVertexId_p
	 * @param directed_p
	 * @return
	 */
	public Edge addEdge (int fromVertexId_p, int toVertexId_p, boolean directed_p) {
		Vertex fromVertexObj, toVertexObj;
		fromVertexObj = addVertex(fromVertexId_p);
		toVertexObj = addVertex (toVertexId_p);
		return addEdge(fromVertexObj, toVertexObj, directed_p);
	}

	/**
	 * Adds an edge to this graph.
	 * @param edgeObj_p
	 */
	public void addEdge (Edge edgeObj_p) {
		if (edgeObj_p==null) return;
		this.addVertex(edgeObj_p.getFromVertex());
		this.addVertex(edgeObj_p.getToVertex());
		this.edgeList.add(edgeObj_p);
	}
	
	/**
	 * Removes an edge from this graph.
	 * @param edge_p
	 * @return
	 */
	public boolean removeEdge (Edge edge_p) {
		if (edge_p==null) return false;
		if (this.edgeList.remove(edge_p)) {
			edge_p.remove();
		}
		this.graphType = Undefined;
		return true;
	}
	
	/**
	 * Returns the list of active or inactive edges that are adjacent to the vertex passed in.
	 * @param vertex_p
	 * @return
	 */
	public java.util.ArrayList getAdjacentEdges (Vertex vertex_p) {
		return getAdjacentEdges(vertex_p, true);
	}
	
	/**
	 * Returns the list of active edges that are adjacent to the vertex passed in.
	 * @param vertex_p
	 * @param activeOnly_p
	 * @return
	 */
	public java.util.ArrayList getAdjacentEdges (Vertex vertex_p, boolean activeOnly_p) {
		if (vertex_p==null) return null;
		java.util.ArrayList retList = new java.util.ArrayList ();
		retList.addAll(vertex_p.getEdges ());
		return makeCopy(retList, activeOnly_p);
	}

	/**
	 * Returns the list of active edges that starts on the vertex passed in.
	 * @param vertex_p
	 * @return
	 */
	public java.util.ArrayList getEdgesFrom (Vertex vertex_p) {
		return getEdgesFrom(vertex_p, true);
	}
	
	/**
	 * Returns the list of edges that starts on the vertex passed in.  The edges
	 * are active only if activeOnly_p is set to true.
	 * @param vertex_p
	 * @param activeOnly_p
	 * @return
	 */
	public java.util.ArrayList getEdgesFrom (Vertex vertex_p, boolean activeOnly_p) {
		if (vertex_p==null) return null;
		java.util.ArrayList retList = new java.util.ArrayList ();
		retList.addAll(vertex_p.getEdgesOut ());
		return makeCopy(retList, activeOnly_p);
	}

	/**
	 * Retruns the list of edges ending at the vertex passed in.
	 * @param vertex_p
	 * @return
	 */
	public java.util.ArrayList getEdgesTo (Vertex vertex_p) {
		return getEdgesTo(vertex_p, true);
	}
	
	/**
	 * Returns the list of edges ending at the vertex passed in. The edges are all
	 * active if activeOnly_p is set to true.
	 * @param vertex_p
	 * @param activeOnly_p
	 * @return
	 */
	public java.util.ArrayList getEdgesTo (Vertex vertex_p, boolean activeOnly_p) {
		if (vertex_p==null) return null;
		java.util.ArrayList retList = new java.util.ArrayList ();
		retList.addAll(vertex_p.getEdgesInto ());
		return makeCopy(retList, activeOnly_p);
	}

	/**
	 * Used internally to make a copy of the edge list.
	 * @param edgeList_p
	 * @param activeOnly_p
	 * @return
	 */
	private java.util.ArrayList makeCopy (java.util.ArrayList edgeList_p, boolean activeOnly_p) {
		java.util.ArrayList retList = new java.util.ArrayList (edgeList_p.size());
		for (int i=0; i<edgeList_p.size(); i++) {
			Edge edgeObj = (Edge) edgeList_p.get(i);
			if (edgeObj.getToVertex().isActive() && 
				(edgeObj.isActive() || !activeOnly_p)) retList.add(edgeObj);
		}
		return retList;
		
	}
	
	/**
	 * Returns the count of the number of vertex in this graph.
	 * @return
	 */
	public int getVertexCount() { 
		return this.getVertexList(true).size();
//		return this.vertexIdList.size(); 
	}
	
	/**
	 * Returns the count of the number of edges in this graph.
	 * @return
	 */
	public int getEdgeCount() { return this.edgeList.size(); }
	
	
	/**
	 * Returns the list of vertices of this graph.
	 * 
	 */
	public java.util.ArrayList <Vertex> getVertexList(boolean activeOnly_p) { 
		java.util.Iterator it = this.vertexIdList.entrySet().iterator();
		java.util.ArrayList retList = new java.util.ArrayList(this.vertexIdList.size());
		while (it.hasNext()) {
			java.util.Map.Entry entry = (java.util.Map.Entry) it.next();
			Vertex vert = (Vertex) entry.getValue();
			if (activeOnly_p && !vert.isActive()) {
				continue;
			}
			retList.add(vert);
		}
		return retList;
	}
	
	/**
	 * sets active status for all self loop edges to active/inactive passed in.
	 * @param active_p true or false
	 * @return # of edges affected (value changed)
	 */
	public int setSelfLoopEdgeActive (boolean active_p) {
		int numChanged = 0;
		for (int i=0; i<this.edgeList.size(); i++) {
			Edge edgeObj = (Edge) this.edgeList.get(i);
			if (edgeObj.isSelfLoop()) {
				if (edgeObj.setActive(active_p)) numChanged++;
			}
		}
		return numChanged;
	}
	
	/**
	 * Returns a String representation of this graph.
	 */
	public String toString() {
		StringBuffer retBuf = new StringBuffer ("Graph {");
		for (int i=0; i<this.edgeList.size(); i++) 
			retBuf.append(this.edgeList.get(i)).append("\n");		
		retBuf.append("}");
		return retBuf.toString();
	}

	/**
	 * Returns a string that is a representation of this graph in a format that can
	 * be read back into openOptima.
	 */
	public String describe() {
		StringBuffer retBuf = new StringBuffer ("//This is generated by openOptima\n");
		retBuf.append("source, target, isDirected\n");
		for (int i=0; i<this.edgeList.size(); i++) 
			retBuf.append(((Edge)this.edgeList.get(i)).describe()).append("\n");		
		return retBuf.toString();
	}
	
	public String getStat () {
		StringBuffer retBuf = new StringBuffer("");
		if (this.graphType==Mixed) retBuf.append("Mixed graph: ");
		else if (this.graphType==Directed) retBuf.append("Directed graph: ");
		else if (this.graphType==Undirected) retBuf.append("Undirected graph: ");
		retBuf.append(this.vertexIdList.size()).append(" nodes, ").append(this.edgeList.size()).append(" edges");
		return retBuf.toString();
	}
}
