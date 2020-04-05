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
 * Represents a node in the network.
 * @author Yaxiong Lin
 *
 */
public class Node extends Vertex {

	public void addArc(Arc arc_p) {
		super.addEdge((Edge) arc_p);
	}

	/**
	 * removes an arc adjacent to this vertex.
	 * @param arc_p Edge
	 * @return true if arc is found and removed, false otherwise
	 */
	public boolean removeArc(Arc arc_p) {
		return super.removeEdge ((Edge) arc_p);
	}

	/**
	 * removes an arc adjacent to this vertex.
	 * @param intId_p internal id
	 * @return true if arc is found and removed, false otherwise
	 */
	protected boolean removeArc(int intId_p) {
		return super.removeEdge(intId_p);
	}
	
	protected java.util.ArrayList <Edge> getArcs () { return this.getEdges(); }
	
	protected java.util.ArrayList <Edge> getArcsOut () { 
		return getEdgesOut(); 
	}
	
	protected java.util.ArrayList <Edge> getArcsInto () { 
		return this.getEdgesInto(); 
	}

	
	protected void init (int id_p, String marker_p, int vertexType_p) {
		super.init(id_p, marker_p, vertexType_p);
	}

	/**
	 * Constructor.
	 * 
	 * @param id_p
	 * @param marker_p
	 * @param vertexType_p
	 */
	public Node (int id_p, String marker_p, int vertexType_p) {
		super (id_p, marker_p, vertexType_p);
	}

	/**
	 * Constructor.
	 * @param id_p
	 * @param marker_p
	 */
	public Node (int id_p, String marker_p) {
		super (id_p, marker_p, simpleVertex);
	}
	
	/**
	 * Constructor.
	 * @param id_p
	 */
	public Node (int id_p) {
		super (id_p, null, simpleVertex);
	}
	
	/**
	 * Returns true if this node is the same as the node passed in.
	 * @param vertex_p
	 * @return
	 */
	public boolean isSameAs (Node vertex_p) {
		return isSameAs((Vertex) vertex_p);
	}
	

}
