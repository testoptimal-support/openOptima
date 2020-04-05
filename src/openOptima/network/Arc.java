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

import openOptima.graph.Edge;
import openOptima.graph.Vertex;


/**
 * Edge represents an edge that connects two vertexs in a graph.  An edge can
 * be either directed or undirected.  The default is undirected.  The edge may
 * have a distance or cost associated with.
 * @author Yaxiong Lin
 *
 */
public class Arc extends Edge {
	private double dist = 1.0;
	
	/**
	 * Initializes this Arc to the attributes passed in.
	 * @param fromNode_p
	 * @param toNode_p
	 * @param marker_p
	 * @param directed_p
	 */
	protected void init (Node fromNode_p, Node toNode_p, String marker_p, boolean directed_p) {
		super.init(fromNode_p, toNode_p, marker_p, directed_p);
	}

	/**
	 * constructor with node objects.
	 * @param fromNode_p
	 * @param toNode_p
	 * @param dist_p
	 * @param directed_p
	 */
	public Arc (Node fromNode_p, Node toNode_p,  double dist_p, boolean directed_p) {
		super ((Vertex)fromNode_p, (Vertex)toNode_p, directed_p);
		this.dist = dist_p;
	}

	/**
	 * Constructor with node objects.
	 * @param fromNode_p
	 * @param toNode_p
	 * @param directed_p
	 */
	public Arc (Node fromNode_p, Node toNode_p, boolean directed_p) {
		super ((Vertex)fromNode_p, (Vertex)toNode_p, directed_p);
	}
	
	/**
	 * Constructor with node objects.
	 * @param fromNode_p
	 * @param toNode_p
	 * @param dist_p
	 */
	public Arc (Node fromNode_p, Node toNode_p, double dist_p) {
		super((Vertex)fromNode_p, (Vertex)toNode_p, false);
		this.dist = dist_p;
	}
	
	/**
	 * Constructor with node objects.
	 * @param fromNode_p
	 * @param toNode_p
	 */
	public Arc (Node fromNode_p, Node toNode_p) {
		super((Vertex)fromNode_p, (Vertex)toNode_p, false);
	}
	
	/**
	 * Returns the starting node object as this arc was created.
	 * @return
	 */
	public Node getFromNode() { return (Node) super.getFromVertex(); }
	
	/**
	 * Returns the ending node object as this arc was created.
	 * @return
	 */
	public Node getToNode() { return (Node) super.getToVertex(); }
	
	/**
	 * Returns true if this arc starts at the node passed in.  Note for the undirected
	 * arc, both nodes are considered the starting node and thus it will return true
	 * for both nodes.
	 * @param node_p
	 * @return
	 */
	public boolean startsAt(Node node_p) { 
		return super.startsAt(node_p);
	}

	/**
	 * Returns true if this arc ends at the node passed in. Note for the undirected
	 * arc, both nodes are considered the ending node and thus it will return true
	 * for both nodes.
	 * @param node_p
	 * @return
	 */
	public boolean endsAt(Node node_p) { 
		return super.endsAt(node_p);
	}
	
	/**
	 * Returns true if this arc is adjacent to the node passed in. That is, it returns true
	 * if the node passed in is either starting or ending node of this arc.
	 * @param node_p
	 * @return
	 */
	public boolean adjacentTo (Node node_p) {
		return super.adjacentTo(node_p);
	}
	
	/**
	 * Returns true if this arc shares a common node either as the starting or ending node.
	 * @param arc_p
	 * @return
	 */
	public boolean adjacentTo (Arc arc_p) {
		return super.adjacentTo(arc_p);
	}

	/**
	 * Sets the distance of this arc.
	 * @param dist_p
	 */
	public void setDist (double dist_p) { this.dist = dist_p; }
	
	/**
	 * Returns the distance of this arc.
	 * @return
	 */
	public double getDist () { return this.dist; }
	
	/**
	 * Returns true if this arc is longer than the arc_p passed in.
	 * @param arc_p
	 * @return
	 */
	public boolean isLongerThan (Arc arc_p) {
		if (arc_p==null) return false;
		return this.dist>arc_p.getDist();
	}
	
	
	/**
	 * Increases the distance of this arc by deltaDist_p.  Pass in a negative
	 * deltaDist_p to decreate the distance of this arc.
	 * @param deltaDist_p
	 * @return
	 */
	public double addDist (double deltaDist_p) {
		this.dist =+ deltaDist_p;
		return this.dist;
	}

	/**
	 * Returns true if this arc is shorter than the arc_p.
	 * @param arc_p
	 * @return
	 */
	public boolean isShorterThan (Arc arc_p) {
		if (arc_p==null) return false;
		return this.dist<arc_p.getDist();
	}

	/**
	 * Returns true if this arc has the same distance as the arc_p.
	 * @param arc_p
	 * @return
	 */
	public boolean isEqualDist (Arc arc_p) {
		if (arc_p==null) return false;
		return this.dist==arc_p.getDist();
	}
	
	/**
	 * Returns true if this arc is the same as arc_p.
	 * @param arc_p
	 * @return
	 */
	public boolean isSameAs (Arc arc_p) { 
		return super.isSameAs((Edge) arc_p);
	}

	/**
	 * Returns a String representation of this arc.
	 */
	public String toString() {
		return this.getDisplayName(false) + ", dist=" + String.valueOf(this.dist);
	}	

	/**
	 * Returns a String representation of this arc but disply the arc in the reverse direction.
	 * That for directed arc (i->j), if printReverse_p is set to true, it will return (j<-i).
	 */
	public String toString(boolean printReverse_p) {
		return this.getDisplayName(printReverse_p) + ", dist=" + String.valueOf(this.dist);
	}	


	public String describe() {
		return super.describe() + ", " + this.dist;
	}

}

