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
package openOptima.reader;

import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.URL;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;

import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.SAXException;
import org.xml.sax.SAXParseException;

/**
 * Provides a set of methods to read the graph /network from the external sources. For example
 * ascii file, xml file, remotely from an url and jdbc/odbc.
 * @author Yaxiong Lin
 *
 */
public class ProblemReader {
	
	protected static String trimWhiteSpaces (String in_p) {
		if (in_p==null) return "";
		StringBuffer retBuf = new StringBuffer(in_p);
		// first remove the leading white space chars
		while (retBuf.length()>0 && (retBuf.charAt(0)==' '  || retBuf.charAt(0) == '\t')) 
			retBuf.deleteCharAt(0);
		// next removes the trailing white space chars
		while (retBuf.length()>0 && (retBuf.charAt(retBuf.length()-1)==' '  || 
				retBuf.charAt(retBuf.length()-1) == '\t')) 
			retBuf.deleteCharAt(retBuf.length()-1);
		return retBuf.toString();
	}
	
	protected static String [] split (String original_p, String delimiter_p) {
    	String[] fields = original_p.split(delimiter_p);
    	for (int i=0; i<fields.length; i++) fields[i] = trimWhiteSpaces(fields[i]);
    	return fields;
	}

	/**
	 * checks if the line_p passed in is a comment line.  Empty line is considered 
	 * comment line.  Any line starts with two forward slashes <code>//</code> are 
	 * considered comment line.
	 * @param line_p text string
	 * @return true or false
	 */
	protected static boolean isCommentLine (String line_p) {
		if (line_p==null || line_p.trim().equals("")) return true;
		if (line_p.indexOf("//")==0) return true;
		return false;
	}
	
	/**
	 * Reads in the graph from the ascii file fileName_p.  If the fileName_p passed in starts with http:// it
	 * will read the file remotely using http.
	 * @param inputString_p input string
	 * @param delimiter_p delimimter
	 * @param listener_p listener object
	 * @return number of records read
	 * @throws ReaderInterruptedException exception
	 */
	public static int execReadString (String inputString_p, String delimiter_p, ReaderListenerIntf listener_p) throws ReaderInterruptedException {
		if (inputString_p==null || inputString_p.equals("")) return 0;
		int lineCount=0;
    	String line;
    	String[] lineList = inputString_p.split("\n");
    	for (lineCount=0; lineCount<lineList.length; lineCount++)
    		if (!isCommentLine(lineList[lineCount])) break;
    	line=lineList[lineCount];
    	if (line.startsWith("http://") || line.startsWith("HTTP://"))
			return execReadFileURL(line, delimiter_p, listener_p);
		else if (line.indexOf("/")>=0 || line.indexOf("\\")>=0)
			return execReadFileText(line, delimiter_p, listener_p);

    	String [] fieldNameList = null;
    	fieldNameList = split(line, delimiter_p);
		java.util.HashMap fields = new java.util.HashMap(fieldNameList.length);
    
    	for (lineCount++; lineCount<lineList.length; lineCount++) {
    		line = lineList[lineCount];
    		if (!isCommentLine(line)) {
    			fields.clear();
    			String [] fieldValueList = split(line, delimiter_p);
        		for (int i=0; i<fieldNameList.length; i++) {
        			fields.put(fieldNameList[i], fieldValueList[i]);
        		}
        		boolean contFlag = listener_p.processLine(fields);
            	if (!contFlag) throw new ReaderInterruptedException ("execReadString interrupted by listener.");
    		}
    	}
        System.out.println( "ReaderString: " + lineCount + " lines read." );
        return lineCount;
	}
		
	/**
	 * Reads in the graph from the ascii file fileName_p.  If the fileName_p passed in starts with http:// it
	 * will read the file remotely using http.
	 * @param fileName_p field name
	 * @param delimiter_p delimiter
	 * @param listener_p listneer object
	 * @return integer
	 * @throws ReaderInterruptedException exception
	 */
	public static int execReadFile (String fileName_p, String delimiter_p, ReaderListenerIntf listener_p) throws ReaderInterruptedException {
		if (fileName_p==null || fileName_p.equals("")) return 0;
		
		if (fileName_p.startsWith("http://") || fileName_p.startsWith("HTTP://"))
			return execReadFileURL(fileName_p, delimiter_p, listener_p);
		else return execReadFileText(fileName_p, delimiter_p, listener_p);
	}
	
	
	/**
	 * Reads in the graph from the url passed in. 
	 * @param urlIn_p url
	 * @param delimiter_p delimiter
	 * @param listener_p listneer object
	 * @return integer
	 * @throws ReaderInterruptedException exception
	 */
	public static int execReadFileURL (String urlIn_p, String delimiter_p, ReaderListenerIntf listener_p) throws ReaderInterruptedException {
		int lineCount=0;
		try {
        	URL  urlIn = new URL (urlIn_p);
        	String line;
        	BufferedReader inputObj = new BufferedReader(new InputStreamReader(urlIn.openStream()));
        	line=inputObj.readLine();
        	String [] fieldNameList = null;
    		java.util.HashMap fields = new java.util.HashMap();
        
        	while ((line=inputObj.readLine())!=null) {
        		if (isCommentLine(line)) continue;
            	if (fieldNameList==null) fieldNameList = split(line, delimiter_p);
            	else {

        			fields.clear();
        			String [] fieldValueList = split(line, delimiter_p);
	        		for (int i=0; i<fieldNameList.length; i++) {
	        			fields.put(fieldNameList[i], fieldValueList[i]);
	        		}
	        		boolean contFlag = listener_p.processLine(fields);
	            	if (!contFlag) throw new ReaderInterruptedException ("ReaderURL interrupted by listener.");
        		}
        		lineCount++;
        	}
        }
        catch (IOException e) {
        	throw new ReaderInterruptedException ("ReaderURL interrupted due to IOException: " + e.toString());
        }
        System.out.println( "ReaderURL: url " + urlIn_p + ", " + lineCount + " lines read." );
        return lineCount;
	}
	

	/**
	 * Reads in the graph from the text file passed in. If the file name ends with .xml it will read the
	 * graph using the GraphXML format.
	 * @param fileName_p file name
	 * @param delimiter_p delimiter
	 * @param listener_p listner object
	 * @return integer
	 * @throws ReaderInterruptedException exeption
	 */
	public static int execReadFileText (String fileName_p, String delimiter_p, ReaderListenerIntf listener_p) throws ReaderInterruptedException {
		
		if (fileName_p==null || fileName_p.equals("")) return 0;
		
		if (fileName_p.endsWith(".xml") || fileName_p.endsWith(".XML")) return execReadGraphXML(fileName_p, null, listener_p);
		
		String lineText;
        int lineCount = 1;
        if (delimiter_p==null) delimiter_p = ",";

        try {
    		BufferedReader fileIn = new BufferedReader( new java.io.FileReader( fileName_p ) );
    		String [] fieldNameList = null;
        	
    		java.util.HashMap fields;
    		fields = new java.util.HashMap();
            while ( ( lineText = fileIn.readLine( ) ) != null)
            {
            	if (isCommentLine(lineText)) continue;
            	if (fieldNameList==null) fieldNameList = split(lineText, delimiter_p);
            	else {
        			fields.clear();
        			String [] fieldValueList = split(lineText, delimiter_p);
	        		for (int i=0; i<fieldNameList.length; i++) {
	        			fields.put(fieldNameList[i], fieldValueList[i]);
	        		}
	            	boolean contFlag = listener_p.processLine(fields);
	            	if (!contFlag) throw new ReaderInterruptedException ("ReaderFile interrupted by listener.");
            	}
            	lineCount++;
            }
        }
        catch (IOException e) {
        	throw new ReaderInterruptedException ("ReaderFile interrupted due to IOException: " + e.toString());
        }
        return lineCount;
	}	
	
	/**
	 * Reads in the graph using the JDBC connection with the sql query passed in.
	 * @param conObj_p connection object
	 * @param sqlStatement_p sql statement
	 * @param listener_p listener object
	 * @return integer
	 * @throws ReaderInterruptedException exception
	 */
	public static int execReadJDBC (java.sql.Connection conObj_p, String sqlStatement_p, ReaderListenerIntf listener_p) 
		throws ReaderInterruptedException {
	    int lineCount = 1;
	    
	    try {
	    	PreparedStatement prepObj = conObj_p.prepareStatement(sqlStatement_p);
	    	ResultSet rsObj = prepObj.executeQuery();
			ResultSetMetaData metaData = rsObj.getMetaData();
			int columnCount = metaData.getColumnCount();
			String [] fieldNameList = new String [columnCount];
			for(int i=1;i<columnCount;i++){
    			fieldNameList[i-1] = metaData.getColumnName(i);
			}
			java.util.HashMap fields = new java.util.HashMap (fieldNameList.length);
			while (rsObj.next()) {
	    		try {
	    			fields.clear();
	    			for(int i=1;i<columnCount;i++){
	        			String strType = metaData.getColumnTypeName(i);
	        			if (strType.equalsIgnoreCase("int") || strType.equalsIgnoreCase("integer"))
	        				fields.put(fieldNameList[i], new Integer(rsObj.getInt(i)));
	        			else if (strType.equalsIgnoreCase("numeric") || strType.equalsIgnoreCase("long"))
	        				fields.put(fieldNameList[i], new Long(rsObj.getLong(i)));
	        			else if (strType.equalsIgnoreCase("char") || strType.equalsIgnoreCase("varchar"))
	        				fields.put(fieldNameList[i], trimWhiteSpaces(rsObj.getString(i)));
	        			else if (strType.equalsIgnoreCase("text"))
	        				fields.put(fieldNameList[i], trimWhiteSpaces(rsObj.getString(i)));
	        			else if (strType.equalsIgnoreCase("date") || strType.equalsIgnoreCase("datetime"))
	        				fields.put(fieldNameList[i], rsObj.getDate(i).toString());
	        			else if (strType.equalsIgnoreCase("double"))
	        				fields.put(fieldNameList[i], new Double(rsObj.getDouble(i)));
	        			else if (strType.equalsIgnoreCase("float") || strType.equalsIgnoreCase("decimal"))
	        				fields.put(fieldNameList[i], new Float(rsObj.getFloat(i)));
	        			else 
	        				fields.put(fieldNameList[i], "?"); // data type not supported
	    			}
	    		}
	    		catch (SQLException e) {
	    			// just stop at the column that errored or last column
	    		}
	        	boolean contFlag = listener_p.processLine(fields);
	        	lineCount++;
	        	if (!contFlag) throw new ReaderInterruptedException ("ReaderJDBC interrupted by listener.");
	        }
	    }
	    catch (SQLException e) {
	    	throw new ReaderInterruptedException ("ReaderJDBC interrupted due to SQLException: " + e.toString());
	    }
	    System.out.println( "ReaderJDBC: " + lineCount + " records read." );
	    return lineCount;
	}


	
	/**
	 * Reads in the graph from the GraphXML document.  Only read the edge
	 * element.
	 * @param xmlFile_p xml file
	 * @param fieldNameList_p field list
	 * @param listener_p listener object
	 * @return int
	 */
	public static int execReadGraphXML (String xmlFile_p, String [] fieldNameList_p, ReaderListenerIntf listener_p) {
		if (fieldNameList_p==null || fieldNameList_p.length==0) {
			fieldNameList_p = new String[4];
			fieldNameList_p[0] = "source";
			fieldNameList_p[1] = "target";
			fieldNameList_p[2] = "distance";
			fieldNameList_p[3] = "isDirected";
		}
		
		try{
			int edgeCount=0;
		    DocumentBuilderFactory docBuilderFactory = DocumentBuilderFactory.newInstance();
		    DocumentBuilder docBuilder = docBuilderFactory.newDocumentBuilder();
		    Document doc = docBuilder.parse (new File(xmlFile_p));
		
		    doc.getDocumentElement ().normalize ();
		    System.out.println ("Root element of the doc is " + 
		         doc.getDocumentElement().getNodeName());
		    Element graphXML = (Element) doc.getElementsByTagName("GraphXML").item(0);
		    NodeList listOfGraphs = graphXML.getElementsByTagName("graph");
		    int totalGraphs = listOfGraphs.getLength();
		    System.out.println("Total no of graphs : " + totalGraphs);
		
		    if (totalGraphs>1) {
		    	throw new Exception ("Multiple graphs not supported.");
		    }

		    java.util.HashMap fields = new java.util.HashMap();
		    for(int s=0; s<listOfGraphs.getLength() ; s++){
		
		    	java.util.HashMap nodeIdx = new java.util.HashMap();
		    	Node graphNode = listOfGraphs.item(s);
		        if(graphNode.getNodeType() != Node.ELEMENT_NODE) continue;

		        Element graphElement = (Element)graphNode;
	            NodeList vertexList = graphElement.getElementsByTagName("edge");
	            for (int n=0; n<vertexList.getLength(); n++) {
	            	fields.clear();
		            Element vertexElement = (Element)vertexList.item(n);
			        if(vertexElement.getNodeType() != Node.ELEMENT_NODE) continue;
			        for (int m=0; m<fieldNameList_p.length; m++) {
			        	fields.put(fieldNameList_p[m], vertexElement.getAttribute(fieldNameList_p[m]));
			        }

	            	boolean contFlag = listener_p.processLine(fields);
	            	if (!contFlag) throw new ReaderInterruptedException ("execReadXML interrupted by listener.");
			        edgeCount++;

	            }
		    }//end of for loop with s var
		
		    return edgeCount;
		}
		catch (SAXParseException err) {
			System.out.println ("** Parsing error" + ", line " 
			     + err.getLineNumber () + ", uri " + err.getSystemId ());
			System.out.println(" " + err.getMessage ());
		}
		catch (SAXException e) {
			Exception x = e.getException ();
			((x == null) ? e : x).printStackTrace ();
		}
		catch (Throwable t) {
			t.printStackTrace ();
		}
		return 0;
	}
	
}
