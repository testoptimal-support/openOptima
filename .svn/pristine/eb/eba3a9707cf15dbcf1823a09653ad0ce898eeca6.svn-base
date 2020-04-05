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
package openOptima.network.postman;

import openOptima.network.Arc;
import openOptima.network.Node;

/**
 * Arc for postman problem.
 * @author Yaxiong Lin
 *
 */
public class PostmanArc extends Arc {

	private double firstDist=1.0;
	private double repeatDist=1.0;
	
	/**
	 * Distance for the subsequent traverses.
	 * @return
	 */
	public double getRepeatDist() { return this.repeatDist; }
	
	/**
	 * Sets the repeat distance for this arc.
	 * @param repeatDist_p
	 */
	public void setRepeatDist(double repeatDist_p) { this.repeatDist = repeatDist_p; }

	/**
	 * Returns the distance for the first time traverse.
	 * @return
	 */
	public double getFirstDist() { return this.firstDist; }
	
	/**
	 * Sets the first time traverse distance.
	 * @param firstDist_p
	 */
	public void setFirstDist(double firstDist_p) { this.firstDist = firstDist_p; }

	/**
	 * Constructor.
	 * @param fromNode_p
	 * @param toNode_p
	 * @param dist_p
	 * @param directed_p
	 */
	public PostmanArc (Node fromNode_p, Node toNode_p, double dist_p, boolean directed_p) {
		super (fromNode_p, toNode_p, dist_p, directed_p);
		this.firstDist = dist_p;
		this.repeatDist = dist_p;
	}

	/**
	 * Constructor.
	 * @param fromNode_p
	 * @param toNode_p
	 * @param dist_p
	 * @param repeatDist_p
	 * @param directed_p
	 */
	public PostmanArc (Node fromNode_p, Node toNode_p, double dist_p, double repeatDist_p, boolean directed_p) {
		super (fromNode_p, toNode_p, dist_p, directed_p);
		this.firstDist = dist_p;
		this.repeatDist = repeatDist_p;
	}
	
	/**
	 * default to directed arc
	 * @param fromNode_p
	 * @param toNode_p
	 * @param dist_p
	 */
	public PostmanArc (Node fromNode_p, Node toNode_p, double dist_p) {
		super (fromNode_p, toNode_p, dist_p, true);
		this.firstDist = dist_p;
		this.repeatDist = dist_p;
	}

}
