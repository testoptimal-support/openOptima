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
package openOptima;

/**
 * Factory class to create an instance of a java class.
 * @author Yaxiong Lin
 *
 */
public class ObjectFactory {
	/**
	 * dynamically creates an instance of a class by the class name.
	 * @param className
	 * @return
	 * @throws ClassNotFoundException check for class path to ensure the appropriate jar file is included in the class path
	 * @throws IllegalAccessException 
	 * @throws InstantiationException
	 */
	public static Object newInstance (String className) 
		throws ClassNotFoundException, IllegalAccessException, InstantiationException {
		Class theClass  = Class.forName(className);
		return theClass.newInstance();
	}
}
