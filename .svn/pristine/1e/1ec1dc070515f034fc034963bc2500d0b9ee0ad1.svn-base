package openOptima.testcases;

import junit.framework.TestCase;
import openOptima.network.GraphRandomGenerator;
import openOptima.network.Network;
import openOptima.network.shortestpath.ShortestPath;
import openOptima.network.shortestpath.ShortestPathProblem;

public class TestDijkstraAlgorithm extends TestCase {
	
	public  TestDijkstraAlgorithm () {
		super();
	}
	
	public void testDirectedNetwork () {
		Network graphObj = new Network();
		
		graphObj.addArc(1, 2, 2, true );
		graphObj.addArc(2, 3, 1, true );
		graphObj.addArc(2, 4, 4, true );
		graphObj.addArc(3, 1, 2, true );
		
		try {
			ShortestPathProblem optObj = new ShortestPathProblem("openOptima.network.shortestpath.DijkstraAlgorithm");
			optObj.init(graphObj);
			ShortestPath spObj = optObj.getShortestPath(2,1);
			System.out.println(spObj.toString());
			assertTrue (spObj.getPathDist()==2);
		}
		catch(Exception e) {
			e.printStackTrace();
		}
	}
	
	public void testUndirectedNetwork () {
		Network graphObj = new Network();
		
		graphObj.addArc(1, 2, 2, false );
		graphObj.addArc(2, 3, 1, false );
		graphObj.addArc(2, 4, 4, false );
		graphObj.addArc(3, 1, 2, false );
		
		try {
			ShortestPathProblem optObj = new ShortestPathProblem("openOptima.network.shortestpath.DijkstraAlgorithm");
			optObj.init(graphObj);
			ShortestPath spObj = optObj.getShortestPath(2,1);
			System.out.println(spObj.toString());
			assertTrue (spObj.getPathDist()==2);
		}
		catch(Exception e) {
			e.printStackTrace();
		}
	}

	public void testMixedNetwork () {
		Network graphObj = new Network();
		
		graphObj.addArc(1, 2, 2, true );
		graphObj.addArc(2, 3, 1, true );
		graphObj.addArc(2, 4, 4, false );
		graphObj.addArc(3, 1, 2, false );
		
		try {
			ShortestPathProblem optObj = new ShortestPathProblem("openOptima.network.shortestpath.DijkstraAlgorithm");
			optObj.init(graphObj);
			ShortestPath spObj = optObj.getShortestPath(2,1);
			System.out.println(spObj.toString());
			assertTrue (spObj.getPathDist()==2);
		}
		catch(Exception e) {
			e.printStackTrace();
		}
	}

	public void testDirected_200x3700 () {
		try {
			GraphRandomGenerator graphRand = new GraphRandomGenerator();
			System.out.println ("generating network...");
			Network graphObj = graphRand.generate(200,3700,0.0,2,10);
			
			System.out.println ("started execution ...");
			long startMillis = System.currentTimeMillis();
			ShortestPathProblem optObj = new ShortestPathProblem("openOptima.network.shortestpath.DijkstraAlgorithm");
			optObj.init(graphObj);
			optObj.getShortestPaths(2);
			System.out.println ("testDirected_200x3700: elapseMillis=" + optObj.getMillisTook() + ", iteration=" + optObj.getIterationCount());
			assertTrue (optObj.getIterationCount()<=150);
			assertTrue (optObj.getMillisTook()<=350);
		}
		catch(Exception e) {
			e.printStackTrace();
		}
	}
	
	
	public void testDirected_500x200000 () {
		try {
			System.out.println ("generating network...");
			GraphRandomGenerator graphRand = new GraphRandomGenerator();
			Network graphObj = graphRand.generate(500,200000,0.0,2,1000);
			
			System.out.println ("started execution ...");
			long startMillis = System.currentTimeMillis();
			ShortestPathProblem optObj = new ShortestPathProblem("openOptima.network.shortestpath.DijkstraAlgorithm");
			optObj.init(graphObj);
			optObj.getShortestPaths(2);
			System.out.println ("testDirected_500x200000: elapseMillis=" + optObj.getMillisTook() + ", iteration=" + optObj.getIterationCount());
			assertTrue (optObj.getIterationCount()<=500);
			assertTrue (optObj.getMillisTook()<=12000);
		}
		catch(Exception e) {
			e.printStackTrace();
		}
	}

	public void testMixed_500x100000 () {
		try {
			GraphRandomGenerator graphRand = new GraphRandomGenerator();
			System.out.println ("generating network...");
			Network graphObj = graphRand.generate(500,100000,0.5,2,1000);
			
			System.out.println ("started execution ...");
			long startMillis = System.currentTimeMillis();
			ShortestPathProblem optObj = new ShortestPathProblem("openOptima.network.shortestpath.DijkstraAlgorithm");
			optObj.init(graphObj);
			optObj.getShortestPaths(2);
			System.out.println ("testMixed_500x100000: elapseMillis=" + optObj.getMillisTook() + ", iteration=" + optObj.getIterationCount());
			assertTrue (optObj.getIterationCount()<=500);
			assertTrue (optObj.getMillisTook()<=7500);
		}
		catch(Exception e) {
			e.printStackTrace();
		}
	}
	
	public void testUndirected_500x100000 () {
		try {
			GraphRandomGenerator graphRand = new GraphRandomGenerator();
			System.out.println ("generating network...");
			Network graphObj = graphRand.generate(500,100000,1.0,2,1000);
			
			System.out.println ("started execution ...");
			long startMillis = System.currentTimeMillis();
			ShortestPathProblem optObj = new ShortestPathProblem("openOptima.network.shortestpath.DijkstraAlgorithm");
			optObj.init(graphObj);
			optObj.getShortestPaths(2);
			System.out.println ("testUndirected_500x100000: elapseMillis=" + optObj.getMillisTook() + ", iteration=" + optObj.getIterationCount());
			assertTrue (optObj.getIterationCount()<=500);
			assertTrue (optObj.getMillisTook()<=11000);
		}
		catch(Exception e) {
			e.printStackTrace();
		}
	}
}
