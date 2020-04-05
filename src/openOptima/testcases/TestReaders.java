package openOptima.testcases;

import java.sql.DriverManager;

import junit.framework.TestCase;
import openOptima.reader.ProblemReader;
import openOptima.reader.ReaderListenerIntf;

public class TestReaders extends TestCase implements ReaderListenerIntf {
	
	public  TestReaders () {
		super();
	}
	
	public boolean processLine(java.util.HashMap fields_p) {
		StringBuffer buf = new StringBuffer("");
		java.util.Iterator it = fields_p.entrySet().iterator();
		while (it.hasNext()) {
			java.util.Map.Entry entry = (java.util.Map.Entry) it.next();
			buf.append(entry.getKey()).append(":").append(entry.getValue()).append("; ");
		}
		System.out.println (buf);
		return true;
	}

	public void testReaderFile () {
		try {
			
			System.out.println ("\n\ntestReaderFile...");
			ProblemReader.execReadFile("c:/sp.txt", ",", this);
		}
		catch (Exception e) {
			e.printStackTrace();
		}
		
//		ProblemReader.parseGraphXML("c:/g.xml");
		
	}
	
	public void testReaderXML () {
		try {
			System.out.println ("\n\ntestReaderXML...");
			ProblemReader.execReadGraphXML("c:/g.xml", null, this);
		}
		catch (Exception e) {
			e.printStackTrace();
		}
		
		
	}
	
	
	public void testReaderJDBC () {
		try {
            Class.forName("sun.jdbc.odbc.JdbcOdbcDriver").newInstance();

            // test ODBC data source
            // assumes an odbc data source name (DSN) myDSN has been defined
        	java.sql.Connection conObj;
        	conObj = DriverManager.getConnection("jdbc:odbc:myDSN", "login id", "my pwd");

			System.out.println ("\n\ntestReaderJDBC using DSN ...");
			ProblemReader.execReadJDBC(conObj, "select * from mars_stat_daily", this);

			System.out.println ("\n\ntestReaderJDBC with connection string ...");
			// test Access DB without DSN
        	conObj = DriverManager.getConnection("jdbc:odbc:Driver={Microsoft Access Driver (*.mdb)};DBQ=c:/test.mdb");
			ProblemReader.execReadJDBC(conObj, "select * from myTable", this);

		
		}
		catch (Exception e) {
			e.printStackTrace();
		}
	}

	public void testReaderURL () {
		try {
			System.out.println ("\n\ntestReaderURL...");
			ProblemReader.execReadFileURL("http://cfdev/mss/mssHome", " ", this);
		}
		catch (Exception e) {
			e.printStackTrace();
		}
	}


	
	
}
