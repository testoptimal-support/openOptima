package openOptima.network.tsp;

import openOptima.network.Node;

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
public class TSPNode extends Node {

	private int minVisitCount=1;
	private int maxVisitCount=1;
	
	
	public double getMinVisitCount() { return this.minVisitCount; }
	public void setMinVisitCount(int minVisitCount_p) { this.minVisitCount = minVisitCount_p; }

	public double getMaxVisitCount() { return this.maxVisitCount; }
	public void setMaxVisitCount(int maxVisitCount_p) { this.maxVisitCount = maxVisitCount_p; }

	public TSPNode (int id_p, int minVisitCount_p) {
		super (id_p);
	}

	public TSPNode (int id_p, String marker_p, int minVisitCount_p) {
		super (id_p, marker_p);
	}
	
	public String toString() {
		return super.toString() + ", minVisitCount=" + this.minVisitCount;
	}	

}
