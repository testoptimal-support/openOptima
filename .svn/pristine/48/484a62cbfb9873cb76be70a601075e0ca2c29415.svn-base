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

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.BufferedOutputStream;
import java.io.FileOutputStream;
import java.io.PrintStream;
import java.util.Date;

import openOptima.AlgorithmTask;
import openOptima.CallbackIntf;
import openOptima.NoSolutionException;
import openOptima.NotImplementedException;
import openOptima.ObjectFactory;
import openOptima.PerformanceStatIntf;
import openOptima.ProgressMonitor;
import openOptima.network.GraphRandomGenerator;
import openOptima.reader.ProblemReader;
import openOptima.reader.ReaderInterruptedException;
import openOptima.reader.ReaderListenerIntf;

/**
 * Describes a postman problem and provides the methods to obtain the optimal postman path.
 * @author Yaxiong Lin
 *
 */
public class PostmanProblem implements ReaderListenerIntf, CallbackIntf, ActionListener, PerformanceStatIntf {

	private PostmanAlgorithmIntf optimizerObj;
	private PostmanNetwork networkObj;
	private int lineIdx;
	private int fromNode;
	private int numPostmen;
	private Thread taskObj;
	private ProgressMonitor progressObj;
	private long startedTime;

	/**
	 * stes the fromNode
	 */
	public void setFromNode (int fromNode_p) {
		this.fromNode = fromNode_p;
	}
	
	/**
	 * constructor with a selection of the specific postman problem algorithm implementation class.
	 * @param algorithmClass_p
	 * @throws InstantiationException
	 * @throws IllegalAccessException
	 * @throws ClassNotFoundException
	 */
	public PostmanProblem (String algorithmClass_p) 
		throws InstantiationException, IllegalAccessException, ClassNotFoundException {
		this.optimizerObj = (PostmanAlgorithmIntf) ObjectFactory.newInstance(algorithmClass_p);
	}

	/**
	 * Sets the network object on which the postman path to be found.
	 * @param networkObj_p
	 * @throws NotImplementedException
	 */
	public void init (PostmanNetwork networkObj_p) throws NotImplementedException{
		this.optimizerObj.init(networkObj_p);
		this.networkObj = networkObj_p;
	}
	
	/**
	 * Sets the graph in the string on which the postman path to be found.
	 * @param inputString_p string containing the graph definition with each line
	 *  representing an edge.
	 * @throws NotImplementedException
	 */
	public void initString (String inputString_p, String delimiter_p) 
		throws NotImplementedException, ReaderInterruptedException, ClassNotFoundException, InstantiationException,
		IllegalAccessException {
		// read in the file
		this.lineIdx = 0;
		if (delimiter_p==null) delimiter_p = ",";
		this.networkObj = new PostmanNetwork ();
		ProblemReader.execReadString(inputString_p, delimiter_p, this);
		this.optimizerObj.init(this.networkObj);

	}
	
	/**
	 * Returns the postman path starting at the fromNode_p.
	 * @param fromNode_p
	 * @return
	 * @throws NoSolutionException
	 */
	public PostmanPath getPostmanPath (int fromNode_p) throws Exception {
		this.fromNode = fromNode_p;
		return this.optimizerObj.getPostmanPath(fromNode_p);
	}

	/**
	 * Starts the optimizer task thread.
	 * @param callbackObj_p object to call back when optimization is completed.
	 * @return
	 * @throws NoSolutionException
	 */
	public AlgorithmTask runOptimizer (CallbackIntf callbackObj_p) throws NotImplementedException {
		if (this.optimizerObj instanceof AlgorithmTask) {
			((AlgorithmTask) this.optimizerObj).setCallback(callbackObj_p);
			this.taskObj = new Thread((Runnable)this.optimizerObj);
			this.taskObj.setName("openOptima");
			this.taskObj.start();
			Thread.yield();
			return (AlgorithmTask) this.optimizerObj;
		}
		else {
			throw new NotImplementedException (this.optimizerObj.getClass().toString() + " does not support task model.");
		}
	}
		
	
	/**
	 * Returns the n-postman paths starting at fromNode_p.
	 * @param fromNode_p
	 * @param numOfPostmen_p
	 * @return
	 * @throws NoSolutionException
	 * @throws NotImplementedException
	 */
	public PostmanPath[] getPostmanPaths (int fromNode_p, int numOfPostmen_p) 
		throws NoSolutionException, NotImplementedException, InterruptedException {
		return this.optimizerObj.getPostmanPaths(fromNode_p, numOfPostmen_p);
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
	 * @param networkInputFile_p
	 * @throws ReaderInterruptedException
	 * @throws ClassNotFoundException
	 * @throws InstantiationException
	 * @throws IllegalAccessException
	 */
	public void initFile (String networkInputFile_p, String delimiter_p) 
		throws NotImplementedException, ReaderInterruptedException, ClassNotFoundException, InstantiationException,
		IllegalAccessException {
		// read in the file
		if (delimiter_p==null) delimiter_p = ",";
		this.lineIdx = 0;
		this.networkObj = new PostmanNetwork ();
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
	 */
	public void initURL (String networkURL_p, String delimiter_p) 
		throws NotImplementedException, ReaderInterruptedException, ClassNotFoundException, InstantiationException,
		IllegalAccessException {
		// read in the file
		this.lineIdx = 0;
		if (delimiter_p==null) delimiter_p = ",";
		this.networkObj = new PostmanNetwork ();
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
	 */
	public void initJDBC (java.sql.Connection conObj_p, String sql_p) 
		throws NotImplementedException, ReaderInterruptedException, ClassNotFoundException, InstantiationException,
		IllegalAccessException {
		this.lineIdx = 0;
		
		this.networkObj = new PostmanNetwork ();

		ProblemReader.execReadJDBC(conObj_p, sql_p, this);
		this.optimizerObj.init(this.networkObj);
	}
	
	/**
	 * initialize this class using the network object passed in. 
	 * @param networkObj_p
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
	 * 
	 * @param args
	 */
	public static void main (String [] args) {
		// -url http://xxx -file c:/xxx -class openOptima.network.shortestpath.Dijkstra -out console or file name -from i -to j
		if (args.length%2!=0) {
			System.out.println ("option arguments must be in pair: -option value.");
			return;
		}
		
		String urlIn=null, fileIn=null, outOpt=null, className=null;
		int fromNode = 1;
		int i=0;
		while (i<args.length) {
			if (args[i].equals("-url")) urlIn = args[++i];
			else if (args[i].equals("-file")) fileIn = args[++i];
			else if (args[i].equals("-class")) className = args[++i];
			else if (args[i].equals("-out")) outOpt = args[++i];
			else if (args[i].equals("-from")) fromNode = Integer.parseInt(args[++i]);
			i++;
		}
		try {
			PostmanProblem spProblem = new PostmanProblem("openOptima.network.postman.LinZhaoAlgorithm");
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
			
			if (fileIn!=null) {
				System.out.print("Reading from file: " + fileIn);
				spProblem.initFile(fileIn, ",");
				PostmanPath pathObj = spProblem.optimizerObj.getPostmanPath(fromNode);
				System.out.println (pathObj.toString());
			}
			else if (urlIn!=null) {
				System.out.print("Reading from URL: " + urlIn);
				spProblem.initURL(urlIn, ",");
				PostmanPath pathObj = spProblem.optimizerObj.getPostmanPath(fromNode);
				System.out.println (pathObj.toString());
			}
			else {
				System.out.print("Starting dialog window");
				spProblem.startDialog();
				spProblem.setFromNode(fromNode);
			}
            if (newOut!=null) newOut.close();
		}
		catch (Exception e) { e.printStackTrace(); }
	}

	/**
	 * stop the progress monitor as the optimization is completed.  Prints the optimized results
	 * to the console.
	 */
	public void callback(Exception exceptionObj_p) {
		if (exceptionObj_p==null) {
			try {
				this.progressObj.stop();
				this.progressObj.setText(this.optimizerObj.getStat()+"\n"+this.optimizerObj.getPostmanPath(this.fromNode).toString());
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
		this.progressObj = new ProgressMonitor("Chinese Postman Problem", this);
			this.progressObj.setText("GraphRandomGenerator(50,1000,0.0,2,10)\n// node#=50, arc#=1000, arcMixture: 0 directed, 1.0 undirected, 0.5 for half of undirected, minDist=2, maxDist=10");
	}

	/**
	 * performs the action for the event passed in. This is used by ProgressMonitor dialog window.
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
					PostmanNetwork netObj = (PostmanNetwork) GraphRandomGenerator.generate(inString);
					this.init (netObj); 
					this.setFromNode (1);
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

	public long getIterationCount() throws NotImplementedException {
		if (!(this.optimizerObj instanceof PerformanceStatIntf))
			throw new NotImplementedException ("The Postman algorithm provider selected does not support PerformanceStatIntf interface");
		return ((PerformanceStatIntf) this.optimizerObj).getIterationCount();
	}

	public long getMillisTook () throws NotImplementedException {
		if (!(this.optimizerObj instanceof PerformanceStatIntf))
			throw new NotImplementedException ("The Postman algorithm provider selected does not support PerformanceStatIntf interface");
		return ((PerformanceStatIntf) this.optimizerObj).getMillisTook();
	}
}
