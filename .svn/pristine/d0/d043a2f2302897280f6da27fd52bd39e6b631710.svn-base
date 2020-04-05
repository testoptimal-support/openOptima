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
 * Edge represents an edge that connects two vertexs in a graph.  An edge can
 * be either directed or undirected.  The default is undirected.  
 * 
 * @author Yaxiong Lin
 * @version 1.0
 * 
 */
public class Edge implements Flag {
	/**
	 * Used to generate the internal id for each edge.
	 */
	private static int nextIntId=0;
	
	/**
	 * Edge's starting vertex.  For undirected edge, fromVertex and toVertex are
	 * both the starting and ending vertex as the edge can be traversed in both ways.
	 */
	private Vertex fromVertex;
	
	/**
	 * Edge's end vertex. For undirected edge, fromVertex and toVertex are
	 * both the starting and ending vertex as the edge can be traversed in both ways.
	 */
	private Vertex toVertex;
	
	/**
	 * Internal id for the edge.
	 */
	private int intId = nextIntId++;
	
	/**
	 * Edge external representation. Default to null.  If not null it will be used
	 * to construct the displayName for this edge by getDisplayName() method.
	 */
	private String marker;
	
	/**
	 * If this edge is a directed edge or undirected edge.  True for directed edge.
	 */
	private boolean directed;
	
	/**
	 * Number of instances of this edge can be traversed.  Use this attribute to
	 * represent the parallel edges that are identical.
	 * @see #minCount
	 * @see #maxCount
	 */
	private int count=1;
	
	/**
	 * If this edge is active.  When set to false, this edge will not be visible to
	 * many methods, for example getEdgesFrom(), etc.
	 */
	private boolean active = true;
	
	/**
	 * Maximum number of instances of this edge allowed.
	 * @see #count
	 * @see #minCount
	 */
	private int maxCount = Integer.MAX_VALUE;
	
	/**
	 * Minimum number of instances of this edge must have.
	 * @see #count
	 * @see #maxCount
	 */
	private int minCount = 1;
	
	/**
	 * Returns the maximum number of instances of this edge allowed.
	 * @return
	 */
	public int getMaxTraverseCount() {
		return this.maxCount;
	}

	/**
	 * Returns the minimum number of instances of this edge must have.
	 * @return
	 */
	public int getMinTraverseCount() {
		return this.minCount;
	}

	/**
	 * Returns true if the number of instances of this edge is over the maximum 
	 * allowed.
	 * @return
	 */
	public boolean isOverTraversed() {
		return this.getCount() > this.maxCount;
	}

	/**
	 * Returns true if the number of instances of this edge is between minimum
	 * and maximum set for this edge.
	 * @return
	 */
	public boolean isSatisfied() {
		return this.getCount() >= this.minCount && this.getCount() <= this.maxCount;
	}

	/**
	 * Returns true if this edge has the number of instances less than the minimum required.
	 * @return
	 */
	public boolean isUnsatisfied() {
		return this.getCount() < this.minCount || this.getCount() > this.maxCount;
	}

	/**
	 * Sets the minimum and maximum numbers of instances for this edge.
	 * @param minCount_p
	 * @param maxCount_p
	 */
	public void setMinMaxCount(int minCount_p, int maxCount_p) {
		this.minCount = minCount_p;
		this.maxCount = maxCount_p;
	}

	/**
	 * Sets the number of instances for this edge.
	 * @param count_p
	 */
	public void setTraverseCount(int count_p) {
		this.setCount(count_p);
	}
	
	/**
	 * Returns the internal ID for this edge.
	 * @return
	 */
	public int getIntId() { return this.intId; }
	
	/**
	 * Returns true if this edge is active.
	 * @return
	 */
	public boolean isActive () { return this.active; }
	
	/**
	 * Sets this edge to active.
	 * @return true if this edge was inactive, false if this edge was already active.
	 */
	public boolean activate () { return setActive (true); }

	/**
	 * Sets this edge to inactive.
	 * @return true if this edge was active, false if this edge was already inactive .
	 */
	public boolean inactivate () { return setActive (false); }

	/**
	 * Returns true if the edge active/inactive value was changed, that is true if going 
	 * from true to false or false to true.
	 * @param active_p  true to active the edge and false to inactivate the edge.
	 * @return
	 */
	public boolean setActive (boolean active_p) { 
		boolean ret = this.active;
		this.active = active_p; 
		return (ret!=this.active);
	}

	/**
	 * Returns the number of instances of this edge in the graph, same as getTraverseCount().
	 * @see #getTraverseCount() 
	 * @return 
	 */
	public int getCount() { return this.count; }
	
	/**
	 * Sets the number of instances of this edge.
	 * @param count_p
	 */
	public void setCount (int count_p) {
		this.count = count_p;
	}
	
	/**
	 * Returns true if this edge is a directed edge.
	 * @return
	 */
	public boolean isDirected () { return this.directed; }
	
	/**
	 * Sets this edge to directed (true) or undirected (false).
	 * @param directed_p true for directed edge and false for undirected edge.
	 */
	public void setDirected (boolean directed_p) { this.directed = directed_p; }
	
	/**
	 * Initializes this edge.
	 * @param fromVertex_p
	 * @param toVertex_p
	 * @param marker_p
	 * @param directed_p
	 */
	protected void init (Vertex fromVertex_p, Vertex toVertex_p, String marker_p, boolean directed_p) {
		this.fromVertex = fromVertex_p;
		this.toVertex = toVertex_p;
		this.marker = marker_p;
		this.directed = directed_p;
		this.fromVertex.addEdge(this);
		this.toVertex.addEdge(this);
	}


	/**
	 * Constructuor
	 * @param fromVertex_p
	 * @param toVertex_p
	 * @param marker_p
	 */
	public Edge (Vertex fromVertex_p, Vertex toVertex_p, String marker_p) {
		init (fromVertex_p, toVertex_p, marker_p, false);
	}

	/**
	 * Constructor
	 * @param fromVertex_p
	 * @param toVertex_p
	 * @param directed_p
	 */
	public Edge (Vertex fromVertex_p, Vertex toVertex_p, boolean directed_p) {
		init (fromVertex_p, toVertex_p, null, directed_p);
	}
	

	/**
	 * Constructor
	 * @param fromVertex_p
	 * @param toVertex_p
	 */
	public Edge (Vertex fromVertex_p, Vertex toVertex_p) {
		init (fromVertex_p, toVertex_p, null, false);
	}
	
	/**
	 * Sets the marker for this edge.
	 * @param marker_p
	 */
	public void setMarker (String marker_p) { this.marker = marker_p; }
	
	/**
	 * Returns the marker of this edge.
	 * @return
	 */
	public String getMarker () { return this.marker; }

	/**
	 * Returns the from vertex as this edge was defined.
	 * @return
	 */
	public Vertex getFromVertex() { return this.fromVertex; }
	
	/**
	 * Returns the to vertex as this edge was defined.
	 * @return
	 */
	public Vertex getToVertex() { return this.toVertex; }
	
	/**
	 * Returns true if this edge starts on the vertex_p passed in.  Note undirected
	 * edge can start on either of the vertices.
	 * @param vertex_p
	 * @return
	 */
	public boolean startsAt(Vertex vertex_p) { 
		if (this.directed) return this.fromVertex.isSameAs(vertex_p);
		else return this.fromVertex.isSameAs(vertex_p) || this.toVertex.isSameAs(vertex_p);
	}

	/**
	 * Returns true if this edge ends on the vertex_p passed in.  Note undirected edge
	 * can end on either of the vertices.
	 * @param vertex_p
	 * @return
	 */
	public boolean endsAt(Vertex vertex_p) { 
		if (this.directed) return this.toVertex.isSameAs(vertex_p);
		else return this.fromVertex.isSameAs(vertex_p) || this.toVertex.isSameAs(vertex_p);
	}
	
	/**
	 * Returns true if this edge is adjacent (either starts or ends) to the vertex_p passed in.
	 * @param vertex_p
	 * @return
	 */
	public boolean adjacentTo (Vertex vertex_p) {
		return this.fromVertex.isSameAs(vertex_p) || this.toVertex.isSameAs(vertex_p);
	}
	
	/**
	 * Returns true if this edge is adjacent to the edge_p passed in.
	 * @param edge_p
	 * @return
	 */
	public boolean adjacentTo (Edge edge_p) {
		return edge_p.adjacentTo(this.fromVertex) || edge_p.adjacentTo(this.toVertex);
	}

	/**
	 * Returns true if this edge is a loop to itself.
	 * @return
	 */
	public boolean isSelfLoop() {
		return this.fromVertex == this.toVertex;
	}
	
	/**
	 * Returns true if this edge is the same as the edge_p passed in.
	 * @param edge_p
	 * @return
	 */
	public boolean isSameAs (Edge edge_p) { 
		if (edge_p==null) return false;
		return this.intId==edge_p.getIntId();
	}
	
	/**
	 * Returns true if this edge is a reverse of the edge edge_p passed in as the edges
	 * were defined.
	 * @param edge_p
	 * @return
	 */
	public boolean isReverseOf (Edge edge_p) { 
		if (edge_p==null) return false;
		if (this.fromVertex.isSameAs(edge_p.getToVertex()) && 
			this.toVertex.isSameAs(edge_p.getFromVertex()) )
			return true;
		else return false;
	}
	
	/**
	 * Removes this edge and thus removes its registration with the vertices which it is adjacent to.
	 *
	 */
	public void remove () {
		this.fromVertex.removeEdge(this);
		this.toVertex.removeEdge(this);
	}
	
	/**
	 * Returns a String representation of this edge.
	 */
	public String toString() {
		return "Edge (" + String.valueOf(this.fromVertex.getId()) + (this.directed?"->":",") + String.valueOf(this.toVertex.getId()) + "), count=" + this.count;
	}	

	/**
	 * Returns a String representation of this edge but reverse the vertex order as the edge
	 * was defined. For example, if this edge is defined as (2->3), this method will
	 * return (3<-2).
	 * @param printReverse_p
	 * @return
	 */
	public String toString(boolean printReverse_p) {
		if (printReverse_p) 
			return this.getDisplayName(printReverse_p) + " count=" + this.count;
		else return this.getDisplayName(printReverse_p) + " count=" + this.count;
	}	

	/**
	 * returns the display name of this edge/arc in the format of (i,j).  If the marker 
	 * has been set, it returns (i,j:marker) instead.  Note i and j are node display names
	 * which can be just node id (integer) or id:marker if marker has been set for the 
	 * node.
	 * @param printReverse_p
	 * @return
	 */
	public String getDisplayName (boolean printReverse_p) {
		StringBuffer retBuf = new StringBuffer();
		if (printReverse_p)
			retBuf.append("(").append(this.toVertex.getDisplayName()).append((this.directed?"<-":",")).append(this.fromVertex.getDisplayName());
		else retBuf.append("(").append(this.fromVertex.getDisplayName()).append((this.directed?"->":",")).append(this.toVertex.getDisplayName());

		if (this.getMarker()!=null) retBuf.append(":").append(this.getMarker());
		retBuf.append(")");
			
		return retBuf.toString();
	}
	
	public String describe() {
		return this.fromVertex.getId() + ", " + this.toVertex.getId() + ", " + (this.directed?"1":"0") + ", " + this.minCount;
	}

	private java.util.HashMap<String, String> flagList = new java.util.HashMap<String, String>();
	
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

	public String getFlag(String flagName_p) {
		return this.flagList.get(flagName_p);
	}
}

