package openOptima.graphXML;

import openOptima.Flag;
import openOptima.graph.Edge;
import openOptima.graph.Graph;
import openOptima.graph.Vertex;

public class Serializer {

	public static String genGraphXML (Graph graphObj_p, boolean removeSelfLoop_p) {
		StringBuffer xmlBuf = new StringBuffer("<?xml version=\"1.0\"?>\n");
		xmlBuf.append("<!DOCTYPE GraphXML SYSTEM \"GraphXML.dtd\">\n")
			  .append("<GraphXML>\n")
			  .append("<graph id=\"").append(graphObj_p.getMarker()).append("\">\n");
		java.util.ArrayList vertexList = graphObj_p.getVertexList(true);
		String color, linestyle;
		for (int i=0; i<vertexList.size(); i++) {
			Vertex vertexObj = (Vertex) vertexList.get(i);
			color = "green";
			if (vertexObj.isSuperVertex()) {
				color = "blue";
			}
			if (vertexObj.hasFlag(Flag.Overflow)) color = "red";
			
			xmlBuf.append("\t<node name=\"").append(vertexObj.getId()).append("\">\n");
			xmlBuf.append("\t\t<label>").append(vertexObj.getMarker()).append("</label>\n");
			xmlBuf.append("\t\t<style>\n");
			xmlBuf.append("\t\t\t<line colour=\"").append(color).append("\"/>\n");
//			xmlBuf.append("\t\t\t<fill colour=\"").append("").append("\"/>\n");
			xmlBuf.append("\t\t</style>\n");
			xmlBuf.append("\t</node>\n");
		}
		
		java.util.ArrayList edgeList = graphObj_p.getEdgeList(true);
		for (int i=0; i<edgeList.size(); i++) {
			Edge edgeObj = (Edge) edgeList.get(i);
			if (removeSelfLoop_p && edgeObj.isSelfLoop()) continue;


			boolean satistied = false;
			if (edgeObj.getCount()>0) {
				satistied = edgeObj.isSatisfied();
			}

			if (satistied) {
				color = "purple";
				linestyle = "solid";
			}
			else {
				color = "gray";
				linestyle = "dashed";
			}

			if (edgeObj.hasFlag(Flag.Overflow)) color = "red";
			
			xmlBuf.append("\t<edge source=\"").append(edgeObj.getFromVertex().getId())
				  .append("\" target=\"").append(edgeObj.getToVertex().getId())
				  .append("\" isDirected=\"").append(edgeObj.isDirected()?"true":"false").append("\">\n");
			xmlBuf.append("\t\t<style>\n");
			xmlBuf.append("\t\t\t<line linewidth=\"").append("1")
				  .append("\" linestyle=\"").append(linestyle)
				  .append("\" colour=\"").append(color).append("\"/>\n");
			xmlBuf.append("\t\t</style>\n");
			xmlBuf.append("\t</edge>\n");
		}
		
		xmlBuf.append("</graph>\n")
			  .append("</GraphXML>\n");
		return xmlBuf.toString();
	}
}
