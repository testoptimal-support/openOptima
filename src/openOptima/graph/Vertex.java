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

import openOptima.Flag;

/**
 * This class describes a vertex in a graph.
 * @author Yaxiong Lin
 *
 */
public class Vertex implements Flag {
	private static int nextIntId=0;
	
	/**
	 * symbol for simple vertex. The is the default value for the vertex. Most of the graph vertex falls into this category.
	 */
	public static final int simpleVertex = 1;
	
	/**
	 * symbol for  for initial vertext of the graph.  This is used in the network to indicate the starting vertex
	 * in the graph/network.
	 */
	public static final int initialVertex = 10;
	
	/**
	 * symbol for  for final vertext of the graph.  This is used in the network to indicate the sink vertex
	 * in the graph/network.
	 */
	public static final int finalVertex = 20;
	
	/**
	 * symbol for super vertex in the graph.  This is used in some graph to indicate the vertex is a collection
	 * of vertices.
	 */
	public static final int superVertex= 30;
	
	/**
	 * symbol for pseudo (artifical) vertex in the graph.  This is used in some graph to indicate the vertex is added
	 * artificially to help describe some concept.
	 */
	public static final int pseudoVertex= 40;
	
	
	private int id; // unique identifier for this vertex, assigned by the calling app
	private int intId = nextIntId++; // internal vertex id
	private String marker; // alternate identify of this vertex
	private int vertexType = simpleVertex; // initialVertex, finalVertex, simpleVertex, superVertex. Default to simpleVertex
	protected int getIntId() { return this.intId; }
	private boolean active = true;

	private java.util.ArrayList <Edge> edgeList = new java.util.ArrayList <Edge>(); // Vertex
	
	/**
	 * Registers an edge to this vertex.
	 * @param edge_p
	 */
	public void addEdge(Edge edge_p) {
		if (edge_p==null || this.edgeList.contains(edge_p)) return;
		this.edgeList.add(edge_p);
	}

	/**
	 * Removes an edge adjacent to this vertex.
	 * @param edge_p Edge
	 * @return true if edge is found and removed, false otherwise
	 */
	public boolean removeEdge(Edge edge_p) {
		if (edge_p==null) return false;
		this.edgeList.remove(edge_p);
		return true;
	}

	/**
	 * Removes an edge adjacent to this vertex.
	 * @param intId_p internal id
	 * @return true if edge is found and removed, false otherwise
	 */
	protected boolean removeEdge(int intId_p) {
		for (int i=0; i<this.edgeList.size(); i++) {
			Edge edgeObj = (Edge) this.edgeList.get(i);
			if (edgeObj.getIntId()== intId_p) {
				this.edgeList.remove(i);
				return true;
			}
		}
		return false;
	}
	
	protected java.util.ArrayList <Edge> getEdges () { return this.edgeList; }
	
	public java.util.ArrayList <Edge> getEdgesOut () { 
		java.util.ArrayList  <Edge> retList = new java.util.ArrayList <Edge>();
		for (int i=0; i<this.edgeList.size(); i++) {
			Edge edgeObj = (Edge) this.edgeList.get(i);
			if (!edgeObj.isDirected() || edgeObj.startsAt(this)) retList.add(edgeObj);
		}
		return retList; 
	}
	
	public java.util.ArrayList <Edge> getEdgesInto () { 
		java.util.ArrayList <Edge> retList = new java.util.ArrayList <Edge>();
		for (int i=0; i<this.edgeList.size(); i++) {
			Edge edgeObj = (Edge) this.edgeList.get(i);
			if (!edgeObj.isDirected() || edgeObj.endsAt(this)) retList.add(edgeObj);
		}
		return retList; 
	}

	/**
	 * returns the edges that has the fromVertex of this vertext
	 * @return
	 */
	public java.util.ArrayList <Edge> getEdgesFrom () { 
		java.util.ArrayList <Edge> retList = new java.util.ArrayList <Edge> ();
		for (int i=0; i<this.edgeList.size(); i++) {
			Edge edgeObj = (Edge) this.edgeList.get(i);
			if (edgeObj.getFromVertex().isSameAs(this)) retList.add(edgeObj);
		}
		return retList; 
	}
	
	/**
	 * returns the edges that has the toVertex of this vertext
	 * @return
	 */
	public java.util.ArrayList <Edge> getEdgesTo () { 
		java.util.ArrayList <Edge> retList = new java.util.ArrayList <Edge> ();
		for (int i=0; i<this.edgeList.size(); i++) {
			Edge edgeObj = (Edge) this.edgeList.get(i);
			if (edgeObj.getToVertex().isSameAs(this)) retList.add(edgeObj);
		}
		return retList; 
	}

	/**
	 * Returns true if this vertex is a type of SimpleVertex.
	 * @return
	 */
	public boolean isSimpleVertex() { return this.vertexType == simpleVertex; }
	
	/**
	 * Returns true if this vertex is a type of pseudoVertex.
	 * @return
	 */
	public boolean isPseudoVertex() { return this.vertexType == pseudoVertex; }
	
	/**
	 * Returns true if this vertex is a type of initialVertex.
	 * @return
	 */
	public boolean isInitialVertex() { return this.vertexType == initialVertex; }

	/**
	 * Returns true if this vertex is a type of finalVertex.
	 * @return
	 */
	public boolean isFinalVertex() { return this.vertexType == finalVertex; }
	
	/**
	 * Returns true if this vertex is a type of superVertex.
	 * @return
	 */
	public boolean isSuperVertex() { return this.vertexType == superVertex; }

	/**
	 * 
	 * @param id_p
	 * @param marker_p
	 * @param vertexType_p
	 */
	protected void init (int id_p, String marker_p, int vertexType_p) {
		this.id = id_p;
		this.marker = marker_p;
		if (vertexType_p>0) this.vertexType = vertexType_p;
	}

	/**
	 * Constructor
	 * @param id_p
	 * @param marker_p
	 * @param vertexType_p
	 */
	public Vertex (int id_p, String marker_p, int vertexType_p) {
		init (id_p, marker_p, vertexType_p);
	}

	/**
	 * Constructor
	 * @param id_p
	 * @param marker_p
	 */
	public Vertex (int id_p, String marker_p) {
		init (id_p, marker_p, simpleVertex);
	}
	
	/**
	 * Constructor
	 * @param id_p
	 */
	public Vertex (int id_p) {
		init (id_p, null, simpleVertex);
	}
	
	
	/**
	 * Returns the id of this vertex.
	 * @return
	 */
	public int getId () { return this.id; }
	
	/**
	 * Sets the marker for this vertex.
	 * @param marker_p
	 */
	public void setMarker (String marker_p) { this.marker = marker_p; }
	
	/**
	 * Returns the marker of this vertex.
	 * @return
	 */
	public String getMarker () { return this.marker; }
	
	/**
	 * Sets the vertex type for this vertex.
	 * @param vertexType_p
	 */
	public void setVertexType (int vertexType_p) { this.vertexType = vertexType_p; }
	

	/**
	 * returns the vertext type.
	 * @return
	 */
	public int getVertexType () { return this.vertexType; }
	
	/**
	 * Returns true if this vertex is the same as the vertex passed in.
	 * @param vertex_p
	 * @return
	 */
	public boolean isSameAs (Vertex vertex_p) {
		if (vertex_p==null) return false;
		return this.intId==vertex_p.getIntId();
	}
	
	/**
	 * Returns a String representation of this vertex.
	 */
	public String toString() {
		if (this.marker==null) return String.valueOf(this.id);
		else return this.marker + "(" + String.valueOf(this.id) + ")";
	}
	
	/**
	 * Returns the display name for this vertex.  If marker has been set, it returns
	 * the node id:marker, otherwise it returns node id.
	 * @return
	 */
	public String getDisplayName() {
		if (this.marker==null) return String.valueOf(this.id);
		else return String.valueOf(this.id) + ":" + this.marker;
	}

	/**
	 * returns number of edges that leads into this vertex. Note undirected edge
	 * is considered both leads into and leads out this vertex
	 * @return
	 */
	public int getEdgesIntoCount () { 
		int edgeCount = 0;
		for (int i=0; i<this.edgeList.size(); i++) {
			Edge edgeObj = (Edge) this.edgeList.get(i);
			if (edgeObj.endsAt(this)) edgeCount++;
		}
		return edgeCount; 
	}
	
	/**
	 * returns number of edges that leads out this vertex. Note undirected edge
	 * is considered both leads into and leads out this vertex
	 * @return
	 */
	public int getEdgesOutCount () { 
		int edgeCount = 0;
		for (int i=0; i<this.edgeList.size(); i++) {
			Edge edgeObj = (Edge) this.edgeList.get(i);
			if (edgeObj.startsAt(this)) edgeCount++;
		}
		return edgeCount; 
	}

	private java.util.HashMap <String, String> flagList = new java.util.HashMap <String, String> ();
	
	public void setFlag (String flagName_p, String flagVal_p) {
		if (flagVal_p==null) this.flagList.remove(flagName_p);
		else this.flagList.put(flagName_p, flagVal_p);
	}
	
	public void clearAllFlags () {
		this.flagList.clear();
	}
	public boolean hasFlag (String flagName_p) {
		return this.flagList.containsKey(flagName_p);
	}
	public void removeFlag (String flagName_p) {
		this.flagList.remove(flagName_p);
	}
	public String[] getAllFlags () {
		String[] retList = new String [this.flagList.size()];
		java.util.Iterator it = this.flagList.keySet().iterator();
		int i=0;
		while (it.hasNext()) {
			java.util.Map.Entry entry = (java.util.Map.Entry) it.next();
			retList[i] = (String) entry.getValue();
		}
		return retList;
	}
	
	/**
	 * returns true if this vertex has an edge to a vertex passed in.
	 * @return true or false
	 */
	public boolean hasEdgeTo (Vertex toVertex_p) { 
		for (int i=0; i<this.edgeList.size(); i++) {
			Edge edgeObj = (Edge) this.edgeList.get(i);
			if (edgeObj.endsAt(toVertex_p)) return true;
		}
		return false; 
	}

	public String getFlag(String flagName_p) {
		return this.flagList.get(flagName_p);
	}
	
	/**
	 * Returns true if this vertex is active.
	 * @return
	 */
	public boolean isActive () { return this.active; }
	
	/**
	 * Sets this vertex to active.
	 * @return true if this vertex was inactive, false if this vertex was already active.
	 */
	public boolean activate () { return setActive (true); }

	/**
	 * Sets this vertex to inactive.
	 * @return true if this vertex was active, false if this vertex was already inactive .
	 */
	public boolean inactivate () { return setActive (false); }

	/**
	 * Returns true if the vertex active/inactive value was changed, that is true if going 
	 * from true to false or false to true.
	 * @param active_p  true to active the vertex and false to inactivate the vertex.
	 * @return
	 */
	public boolean setActive (boolean active_p) { 
		boolean ret = this.active;
		this.active = active_p; 
		return (ret!=this.active);
	}


}
