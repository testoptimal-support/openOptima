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
package openOptima.network.shortestpath;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.BufferedOutputStream;
import java.io.FileOutputStream;
import java.io.PrintStream;

import openOptima.AlgorithmTask;
import openOptima.CallbackIntf;
import openOptima.NoSolutionException;
import openOptima.NotImplementedException;
import openOptima.ObjectFactory;
import openOptima.PerformanceStatIntf;
import openOptima.ProgressMonitor;
import openOptima.graph.Vertex;
import openOptima.network.GraphRandomGenerator;
import openOptima.network.Network;
import openOptima.reader.ProblemReader;
import openOptima.reader.ReaderInterruptedException;
import openOptima.reader.ReaderListenerIntf;

/**
 * Defines the shortest path problem and provides methods to find the shortest path.
 * @author Yaxiong Lin
 *
 */
public class ShortestPathProblem implements ReaderListenerIntf, CallbackIntf, ActionListener, PerformanceStatIntf {

	private ShortestPathAlgorithmIntf optimizerObj;
	private Network networkObj;
	private int lineIdx=0;
	private Thread taskObj;
	private ProgressMonitor progressObj;
	protected int fromNode;
	protected int toNode;
	protected ShortestPath [] spList;
	private long startedTime;
	
	/**
	 * Constructor with selection of shortest path algorithm implementation class.
	 * @param algorithmClass_p class
	 * @throws InstantiationException exception
	 * @throws IllegalAccessException exception
	 * @throws ClassNotFoundException exception
	 */
	public ShortestPathProblem (String algorithmClass_p) throws InstantiationException, IllegalAccessException, ClassNotFoundException {
		this.optimizerObj = (ShortestPathAlgorithmIntf) ObjectFactory.newInstance(algorithmClass_p);
	}
	
	/**
	 * reads the graph/network from an ascii file with each line describing the arc with the following format:
	 * <p>i,j,distance,directed
	 * <p>where i,j describes the arc from node i to node j, and directed = Y or 1 to 
	 * indicate if the arc (i,j) is a directed arc and N or 0 to indicate (i,j) is an
	 * undirected arc.
	 * <p>For example:
	 * 1,2,10,Y
	 * 2,3,56,N
	 * 3,5,32,N
	 * 4,5,45,N
	 * 4,2,12,N
	 * 
	 * @param networkInputFile_p network file
	 * @param delimiter_p delimiter
	 * @throws ReaderInterruptedException exception
	 * @throws ClassNotFoundException exception
	 * @throws InstantiationException exception
	 * @throws IllegalAccessException exception
	 */
	public void initFile (String networkInputFile_p, String delimiter_p) throws ReaderInterruptedException, ClassNotFoundException, InstantiationException,
		IllegalAccessException {
		// read in the file
		if (delimiter_p==null) delimiter_p = ",";
		this.lineIdx = 0;
		this.networkObj = new Network ();
		ProblemReader.execReadFile(networkInputFile_p, delimiter_p, this);
		this.optimizerObj.init(this.networkObj);
	}
	
	/**
	 * reads the graph/network from an ascii file with each line describing the arc with the following format. The
	 * first line must be the column label as follows.
	 * <p>source, target,distance, isDirected
	 * <p>where source,target pair describes the arc from node <code>source</code> to node <code>target</code>, and 
	 * isDirected = Y or 1 to indicate if the arc is a directed arc and N or 0 to indicate the arc is an
	 * undirected arc.
	 * <p>For example:
	 * source, target, distance, isDirected
	 * 1,2,10,Y
	 * 2,3,56,N
	 * 3,5,32,N
	 * 4,5,45,N
	 * 4,2,12,N
	 * 
	 * @param networkURL_p network object
	 * @param delimiter_p delimiter
	 * @throws ReaderInterruptedException exception
	 * @throws ClassNotFoundException exception
	 * @throws InstantiationException exception
	 * @throws IllegalAccessException exception
	 */
	public void initURL (String networkURL_p, String delimiter_p) throws ReaderInterruptedException, ClassNotFoundException, InstantiationException,
		IllegalAccessException {
		// read in the file
		this.lineIdx = 0;
		if (delimiter_p==null) delimiter_p = ",";
		this.networkObj = new Network ();
		ProblemReader.execReadFileURL(networkURL_p, delimiter_p, this);
		this.optimizerObj.init(this.networkObj);
	}

	/**
	 * reads the graph/network from an JDBC database with an sql query.  Each row describes an arc with the following 
	 * columns, node column names must match the definition below:
	 * <p>source, target,distance, isDirected
	 * <p>where source,target pair describes the arc from node <code>source</code> to node <code>target</code>, and 
	 * isDirected = Y or 1 to indicate if the arc is a directed arc and N or 0 to indicate the arc is an
	 * undirected arc. 
	 * <p>For example:
	 * source, target, distance, isDirected
	 * 1,2,10,Y
	 * 2,3,56,N
	 * 3,5,32,N
	 * 4,5,45,N
	 * 4,2,12,N
 	 * All columns must be of varchar data type.
	 * 
	 * @param conObj_p con object
	 * @param sql_p sql statement
	 * @throws ReaderInterruptedException exception
	 * @throws ClassNotFoundException exception
	 * @throws InstantiationException exception
	 * @throws IllegalAccessException exception
	 */
	public void initJDBC (java.sql.Connection conObj_p, String sql_p) throws ReaderInterruptedException, ClassNotFoundException, InstantiationException,
		IllegalAccessException {
		this.lineIdx = 0;
		this.networkObj = new Network ();

		ProblemReader.execReadJDBC(conObj_p, sql_p, this);
		this.optimizerObj.init(this.networkObj);
	}
	
	/**
	 * initialize this class using the network object passed in. 
	 * @param networkObj_p network
	 */
	public void init (Network networkObj_p) {
		this.optimizerObj.init(networkObj_p);
		this.networkObj = networkObj_p;
	}

	/**
	 * called by the ProblemReader class to process each row/line read.
	 * @param fields_p fields map
	 * @return indicator
	 */
	public boolean processLine(java.util.HashMap fields_p) {
		try {
			boolean directed;
			String temp;
			temp = (String) fields_p.get("isDirected");
			if (temp!=null && (temp.equalsIgnoreCase("Y") || temp.equalsIgnoreCase("1"))) 
				directed = true;
			else directed = false;
			this.networkObj.addArc(Integer.parseInt((String) fields_p.get("source")), 
					Integer.parseInt((String) fields_p.get("target")), Double.parseDouble((String) fields_p.get("distance")), directed);
		}
		catch (Exception e) {
			System.out.println ("parsing data error at line " + this.lineIdx + ": " + e.toString());
			return false;
		}
		
		return true;
	}

	
	/**
	 * Returns the shortest path from the fromNode_p to the toNode_p.
	 * @param fromNode_p from
	 * @param toNode_p to
	 * @return shortest path
	 * @throws NoSolutionException exception
	 */
	public ShortestPath getShortestPath (int fromNode_p, int toNode_p) throws NoSolutionException {
		ShortestPath ret = this.optimizerObj.getShortestPath(fromNode_p, toNode_p);
		return ret;
	}

	public ShortestPath getShortestPathVia (int fromNode_p, int viaNode_p, int toNode_p) throws NoSolutionException {
		ShortestPath seg1 = this.optimizerObj.getShortestPath(fromNode_p, viaNode_p);
		ShortestPath seg2 = this.optimizerObj.getShortestPath(viaNode_p, toNode_p);
		ShortestPath ret = seg1.merge(seg2);
		return ret;
	}


	/**
	 * Returns the list of shortest paths from fromNode_p to the rest of the nodes in the graph/network.
	 * @param fromNode_p from
	 * @return shortest path array
	 * @throws NoSolutionException exception
	 */
	public ShortestPath[] getShortestPaths (int fromNode_p) throws NoSolutionException {
		return this.optimizerObj.getShortestPaths(fromNode_p);
	}

	public java.util.ArrayList<Vertex> getUnReachableVertexList() {
		return this.optimizerObj.getUnReachableVertexList();
	}
	
	/**
	 * 
	 * @param args args
	 */
	public static void main (String [] args) {
		// -url http://xxx -file c:/xxx -class openOptima.network.shortestpath.Dijkstra -out console or file name -from i -to j
		if (args.length%2!=0) {
			System.out.println ("option arguments must be in pair: option and value.");
			return;
		}
		
		String urlIn=null, fileIn=null, outOpt=null, className=null;
		try {
			ShortestPathProblem spProblem = new ShortestPathProblem("openOptima.network.shortestpath.DijkstraAlgorithm");
			spProblem.fromNode = 1;
			spProblem.toNode = 0;
			int i=0;
			while (i<args.length) {
				if (args[i].equals("-url")) urlIn = args[++i];
				else if (args[i].equals("-file")) fileIn = args[++i];
				else if (args[i].equals("-class")) className = args[++i];
				else if (args[i].equals("-out")) outOpt = args[++i];
				else if (args[i].equals("-from")) spProblem.fromNode = Integer.parseInt(args[++i]);
				else if (args[i].equals("-to")) spProblem.toNode = Integer.parseInt(args[++i]);
				i++;
			}
//			ShortestPathAlgorithm spOptimizer = spProblem.initFile("c:/spNetwork.txt");
//			ShortestPath spObj = spOptimizer.getShortestPath(1, 20);

			if (fileIn!=null) {
				System.out.print("Reading from file: " + fileIn);
				spProblem.initFile(fileIn, ",");
			}
			else if (urlIn!=null) {
				System.out.print("Reading from URL: " + urlIn);
				spProblem.initURL(urlIn, ",");
			}
			else {
				System.out.print("Starting dialog window");
				spProblem.startDialog();
				return;
			}
			
			PrintStream newOut=null;
			if (outOpt!=null) {
	            try {
	                newOut = new PrintStream(new BufferedOutputStream(new FileOutputStream(outOpt, true)));
	                System.setOut(newOut);
	                System.setErr(newOut);
	            }
	            catch (Exception e) {
	            	e.printStackTrace();
	            }
			}
			
			ShortestPath[] spList;
			spList = spProblem.getShortestPaths(spProblem.fromNode);
			for (int j=0; j<spList.length; j++) System.out.println(spList[j]);
			
            if (newOut!=null) newOut.close();

		}
		catch (Exception e) { e.printStackTrace(); }
	}

	/**
	 * stop the progress monitor as the optimization is completed.  Prints the optimized results
	 * to the console.
	 * @param exceptionObj_p exception
	 */
	public void callback(Exception exceptionObj_p) {
		if (exceptionObj_p==null) {
			try {
				this.progressObj.stop();
				StringBuffer result = new StringBuffer();
				ShortestPath [] spList = this.optimizerObj.getShortestPaths(this.fromNode);
				for (int i=0; i<spList.length; i++) {
					result.append(spList[i].toString()).append("\n\n");
				}
				this.progressObj.setText(this.optimizerObj.getStat()+"\n"+result.toString());
			}
			catch (Exception e) {
				this.progressObj.alert("no solution: " + e.toString());
			}
		}
		else {
			this.progressObj.alert("No solution found due to exception " + exceptionObj_p.toString());
		}
	}

	/**
	 * Starts up the progress monitor dialog window.
	 *
	 */
	protected void startDialog () {
		this.progressObj = new ProgressMonitor("Shortest Path Problem", this);
			this.progressObj.setText("GraphRandomGenerator(200,3700,0.0,2,10)\n// node#=200, arc#=3700, arcMixture: 0 directed, 1.0 undirected, 0.5 for half of undirected, minDist=2, maxDist=10");
	}

	/**
	 * performs the action for the event passed in. This is used by ProgressMonitor dialog window.
	 * @param evt_p event
	 */
	public void actionPerformed(ActionEvent evt_p) {
		try {
			String evtCommand = evt_p.getActionCommand();
			if (evtCommand==null) {
				int pct = this.optimizerObj.getProgressPercentage();
				this.progressObj.setProgress(pct);
				if (pct>0) {
					long secondsLeft = (System.currentTimeMillis()-this.startedTime) * 10 / pct;
					this.progressObj.setText(this.optimizerObj.getStat() + "\nEstimate time to completion " + secondsLeft / 10 + " seconds");
				}
				else this.progressObj.setText(this.optimizerObj.getStat());
			}
			else if (evtCommand.equalsIgnoreCase(ProgressMonitor.START)) {
				this.optimizerObj.setStartNode(this.fromNode);
				this.progressObj.setText(this.networkObj.getStat());
				this.startedTime = System.currentTimeMillis();
				this.runOptimizer(this);
			}
			else if (evtCommand.equalsIgnoreCase(ProgressMonitor.ABORT)) {
				this.taskObj.interrupt();
				this.progressObj.setText("aborting...");
			}
			else if (evtCommand.equalsIgnoreCase(ProgressMonitor.LOAD)) {
				String inString = this.progressObj.getText();
				if (inString==null || inString.equals("")) {
					this.progressObj.alert("Please enter the graph file file/url or graph definition.");
					return;
				}
				if (!inString.startsWith("GraphRandomGenerator")) {
					this.initString(inString, ",");
				}
				else {
					Network netObj = (Network) GraphRandomGenerator.generate(inString);
					this.init (netObj); 
					this.fromNode = 1;
				}
				this.progressObj.setText(this.networkObj.describe());
			}
			else {
				this.progressObj.setProgress(this.optimizerObj.getProgressPercentage());
			}
		}
		catch (Exception exp) {
			this.progressObj.alert(exp.toString());
		}
	}

	/**
	 * Returns the postman path starting at the fromNode_p.
	 * @param callbackObj_p callback object
	 * @return algorithm task
	 * @throws NotImplementedException exception
	 */
	public AlgorithmTask runOptimizer (CallbackIntf callbackObj_p) throws NotImplementedException {
		if (this.optimizerObj instanceof AlgorithmTask) {
			((AlgorithmTask) this.optimizerObj).setCallback(callbackObj_p);
			this.taskObj = new Thread((Runnable)this.optimizerObj);
			this.taskObj.start();
			Thread.yield();
			return (AlgorithmTask) this.optimizerObj;
		}
		else {
			throw new NotImplementedException (this.optimizerObj.getClass().toString() + " does not support task model.");
		}
	}
	
	/**
	 * Sets the graph in the string on which the postman path to be found.
	 * @param inputString_p string containing the graph definition with each line
	 *  representing an edge.
	 * @param delimiter_p delimiter
	 * @throws NotImplementedException exception
	 * @throws ClassNotFoundException exception
	 * @throws IllegalAccessException exception
	 * @throws InstantiationException exception
	 * @throws ReaderInterruptedException exception
	 */ 
	public void initString (String inputString_p, String delimiter_p) 
		throws NotImplementedException, ReaderInterruptedException, ClassNotFoundException, InstantiationException,
		IllegalAccessException {
		// read in the file
		this.lineIdx = 0;
		if (delimiter_p==null) delimiter_p = ",";
		this.networkObj = new Network ();
		ProblemReader.execReadString(inputString_p, delimiter_p, this);
		this.optimizerObj.init(this.networkObj);

	}
	
	public long getIterationCount() throws NotImplementedException {
		if (!(this.optimizerObj instanceof PerformanceStatIntf))
			throw new NotImplementedException ("The ShortestPath algorithm provider selected does not support PerformanceStatIntf interface");
		return ((PerformanceStatIntf) this.optimizerObj).getIterationCount();
	}

	public long getMillisTook () throws NotImplementedException {
		if (!(this.optimizerObj instanceof PerformanceStatIntf))
			throw new NotImplementedException ("The ShortestPath algorithm provider selected does not support PerformanceStatIntf interface");
		return ((PerformanceStatIntf) this.optimizerObj).getMillisTook();
	}
}
