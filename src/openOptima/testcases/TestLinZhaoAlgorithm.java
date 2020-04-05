package openOptima.testcases;

import junit.framework.TestCase;
import openOptima.network.GraphRandomGenerator;
import openOptima.network.Network;
import openOptima.network.postman.PostmanNetwork;
import openOptima.network.postman.PostmanPath;
import openOptima.network.postman.PostmanProblem;
import openOptima.network.shortestpath.ShortestPathProblem;

public class TestLinZhaoAlgorithm extends TestCase {
	
	public  TestLinZhaoAlgorithm () {
		super();
	}
	
	private PostmanProblem getProblemObj () {
		try {
			return new PostmanProblem ("openOptima.network.postman.LinZhaoAlgorithm");
		}
		catch (Exception e) {
			e.printStackTrace();
			return null;
		}
	}
	
	public void testCase1 () {
		PostmanProblem linZhaoOpt = getProblemObj();
		assertTrue(linZhaoOpt!=null);
		
		try {
			// "Test case #1: Direct CPP paper, linZhao"
			PostmanNetwork probObj= new PostmanNetwork();
			probObj.addArc(1,2,8.0, true);
			probObj.addArc(2,4,5.0,true);
			probObj.addArc(3,2,2,true);
			probObj.addArc(3,5,3,true);
			probObj.addArc(4,3,1,true);
			probObj.addArc(4,5,2,true);
			probObj.addArc(5,1,5.0, true);
			probObj.addArc(5,2,7,true);
	
			linZhaoOpt.init(probObj);
			PostmanPath pathObj = linZhaoOpt.getPostmanPath(1);
			assertTrue (pathObj.getPathDist()<=44);
		}
		catch (Exception e) {
			assertTrue(false);
		}
	}
	
	public void testCase2() {
		PostmanProblem linZhaoOpt = getProblemObj();
		assertTrue(linZhaoOpt!=null);
		
		try {
			// "Test case #2: Direct CPP paper. linZhao"
			PostmanNetwork probObj= new PostmanNetwork();
			probObj.addArc(4,2,1,true);
			probObj.addArc(4,1,1,true);
			probObj.addArc(1,2,1,true);
			probObj.addArc(1,3,1,true);
			probObj.addArc(2,3,1,true);
			probObj.addArc(3,4,1,true);
			
			linZhaoOpt.init(probObj);
			PostmanPath pathObj = linZhaoOpt.getPostmanPath(1);
			assertTrue (pathObj.getPathDist()<=10);
		}
		catch (Exception e) {
			assertTrue(false);
		}
}
	
	public void testCase3() {
		PostmanProblem linZhaoOpt = getProblemObj();
		assertTrue(linZhaoOpt!=null);
		
		try {
			// "Test case #3: Direct CPP paper, linZhao"
			PostmanNetwork probObj= new PostmanNetwork();
			probObj.addArc(1,2,8, true);
			probObj.addArc(2,4,5, true);
			probObj.addArc(3,2,2, true);
			probObj.addArc(3,5,3, true);
			probObj.addArc(4,3,1, true);
			probObj.addArc(4,5,2, true);
			probObj.addArc(5,2,7, true);
			probObj.addArc(5,1,5, true);
			
			linZhaoOpt.init(probObj);
			PostmanPath pathObj = linZhaoOpt.getPostmanPath(1);
			assertTrue (pathObj.getPathDist()<=44);
		}
		catch (Exception e) {
			assertTrue(false);
		}
	}
	
	public void testCase4() {
		PostmanProblem linZhaoOpt = getProblemObj();
		assertTrue(linZhaoOpt!=null);
		
		try {
			// "Test case #4: Direct CPP paper. linZhao"
			PostmanNetwork probObj= new PostmanNetwork();
			probObj.addArc(1,2,5279.0, true);
			probObj.addArc(2,1,5279, true);
			probObj.addArc(2,3,3826, true);
			probObj.addArc(3,2,3826, true);
			probObj.addArc(3,1,4912, true);
			probObj.addArc(1,3,4912, true);
			probObj.addArc(4,1,3474, true);
			probObj.addArc(1,4,3474, true);
			probObj.addArc(4,3,3474, true);
			probObj.addArc(3,4,3474, true);
			
			linZhaoOpt.init(probObj);
			PostmanPath pathObj = linZhaoOpt.getPostmanPath(1);
			assertTrue (pathObj.getPathDist()<=41930);
		}
		catch (Exception e) {
			assertTrue(false);
		}
	}
	
	public void testCase5() {
		PostmanProblem linZhaoOpt = getProblemObj();
		assertTrue(linZhaoOpt!=null);
		
		try {
			// "Test case #5: Direct CPP paper. linZhao"
			PostmanNetwork probObj= new PostmanNetwork();
			probObj.addArc(1,2,2,true);
			probObj.addArc(1,5,4,1,true);
			probObj.addArc(2,3,1,1,true);
			probObj.addArc(3,4,3,1,true);
			probObj.addArc(3,5,5,1,true).setMinMaxCount(0, 5);
			probObj.addArc(4,1,5,2,true);
			probObj.addArc(5,4,6,1,true);

			linZhaoOpt.init(probObj);
			PostmanPath pathObj = linZhaoOpt.getPostmanPath(1);
			assertTrue (pathObj.getPathDist()<=25);
		}
		catch (Exception e) {
			assertTrue(false);
		}
	}
	
	public void testCase6() {
		PostmanProblem linZhaoOpt = getProblemObj();
		assertTrue(linZhaoOpt!=null);

		try {
			// "Test case #6: Direct CPP paper. linZhao"
			PostmanNetwork probObj= new PostmanNetwork();
			probObj.addArc(1,2,8,8,true);
			probObj.addArc(2,4,5,5,true);
			probObj.addArc(3,2,2,2,true);
			probObj.addArc(3,5,3,3,true);
			probObj.addArc(4,3,1,1,true);
			probObj.addArc(4,5,2,2,true);
			probObj.addArc(5,2,7,7,true);
			probObj.addArc(5,1,5,5,true);
			
			linZhaoOpt.init(probObj);
			PostmanPath pathObj = linZhaoOpt.getPostmanPath(1);
			assertTrue (pathObj.getPathDist()<=44);
		}
		catch (Exception e) {
			assertTrue(false);
		}
	}
	
	public void testCase7() {
		PostmanProblem linZhaoOpt = getProblemObj();
		assertTrue(linZhaoOpt!=null);

		try {
			// "Test case #7: Direct CPP paper. linZhao"
			PostmanNetwork probObj= new PostmanNetwork();
			probObj.addArc(3,3,6,6,true);
			probObj.addArc(3,2,5,5,true);
			probObj.addArc(3,1,6,6,true);
			probObj.addArc(1,3,2,2,true);
			probObj.addArc(2,1,8,8,true);
			probObj.addArc(2,3,3,3,true);
			
			linZhaoOpt.init(probObj);
			PostmanPath pathObj = linZhaoOpt.getPostmanPath(1);
			assertTrue (pathObj.getPathDist()<=37);
		}
		catch (Exception e) {
			assertTrue(false);
		}
	}

	public void testCase8() {
		PostmanProblem linZhaoOpt = getProblemObj();
		assertTrue(linZhaoOpt!=null);

		try {
			// "Test case #8: Auto generated"
			PostmanNetwork probObj= new PostmanNetwork();
			probObj.addArc(3,1,6,6,true);
			probObj.addArc(3,2,4,4,true);
			probObj.addArc(2,1,7,7,true);
			probObj.addArc(3,3,6,6,true);
			probObj.addArc(2,3,4,4,true);
			probObj.addArc(1,2,2,2,true);
			
			linZhaoOpt.init(probObj);
			PostmanPath pathObj = linZhaoOpt.getPostmanPath(1);
			assertTrue (pathObj.getPathDist()<=35);
		}
		catch (Exception e) {
			assertTrue(false);
		}
	}

	public void testDirected_100x2000 () {
		try {
			System.out.println ("generating network...");
			GraphRandomGenerator graphRand = new GraphRandomGenerator();
			PostmanNetwork graphObj = (PostmanNetwork) graphRand.generate(100,2000,0.0,2,1000);
			
			System.out.println ("started execution ...");
			long startMillis = System.currentTimeMillis();
			PostmanProblem optObj = new PostmanProblem("openOptima.network.postman.LinZhaoAlgorithm");
			optObj.init(graphObj);
			optObj.getPostmanPath(2);
			System.out.println ("testDirected_100x2000: elapseMillis=" + optObj.getMillisTook() + ", iteration=" + optObj.getIterationCount());
			assertTrue (optObj.getIterationCount()<=400);
			assertTrue (optObj.getMillisTook()<=8000);
		}
		catch(Exception e) {
			e.printStackTrace();
		}
	}

	public void testDirected_200x10000 () {
		try {
			System.out.println ("generating network...");
			GraphRandomGenerator graphRand = new GraphRandomGenerator();
			PostmanNetwork graphObj = (PostmanNetwork) graphRand.generate(200,10000,0.0,2,1000);
			
			System.out.println ("started execution ...");
			long startMillis = System.currentTimeMillis();
			PostmanProblem optObj = new PostmanProblem("openOptima.network.postman.LinZhaoAlgorithm");
			optObj.init(graphObj);
			optObj.getPostmanPath(2);
			System.out.println ("testDirected_200x10000: elapseMillis=" + optObj.getMillisTook() + ", iteration=" + optObj.getIterationCount());
			assertTrue (optObj.getIterationCount()<=1700);
			assertTrue (optObj.getMillisTook()<=170000);
		}
		catch(Exception e) {
			e.printStackTrace();
		}
	}
	
}
