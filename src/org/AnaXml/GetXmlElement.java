package org.AnaXml;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;

import org.tools.UpdateXml.MyEntityResolver;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.NodeList;
import org.xml.sax.SAXException;

public class GetXmlElement {

	public static void GetAllElement() {
		// NodeList ComponAttr = null;
		DocumentBuilder dBuilder = null;
		Element SOURCE = null;
		Element TARGET = null;
		Element TRANTYPE = null;
		Element CONNECTOR = null;
		Document doc = null;
		String filePath = "xml\\M_AUTO.xml";
		File xmlFile = new File(filePath);
		StringBuffer Componet = new StringBuffer();
		ArrayList<String> ConnDenpt = new ArrayList<String>();
		DocumentBuilderFactory dbFactory = DocumentBuilderFactory.newInstance();
		try {
			dBuilder = dbFactory.newDocumentBuilder();
			dBuilder.setEntityResolver(new MyEntityResolver());
			doc = dBuilder.parse(xmlFile);
		} catch (SAXException | IOException | ParserConfigurationException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		doc.getDocumentElement().normalize();

		NodeList employees = doc.getElementsByTagName("SOURCE");
		for (int i = 0; i < employees.getLength(); i++) {
			SOURCE = (Element) employees.item(i);

			String SourceName = SOURCE.getAttribute("NAME");
			Componet.append("Source: " + SourceName + "\n");
		}
		// System.out.println(Componet.toString());

		NodeList Transformation = doc.getElementsByTagName("TRANSFORMATION");
		for (int i = 0; i < Transformation.getLength(); i++) {
			TRANTYPE = (Element) Transformation.item(i);

			String TRANSFORMATION = TRANTYPE.getAttribute("TYPE");
			String TRANSNM = TRANTYPE.getAttribute("NAME");

			if (TRANSFORMATION.equals("Custom Transformation")) {
				TRANSFORMATION = TRANTYPE.getAttribute("TEMPLATENAME");
			}
			Componet.append(TRANSFORMATION + ": " + TRANSNM + "\n");
		}

		NodeList Target = doc.getElementsByTagName("TARGET");
		for (int i = 0; i < Target.getLength(); i++) {
			TARGET = (Element) Target.item(i);

			String TargetName = TARGET.getAttribute("NAME");
			Componet.append("TargetName: " + TargetName + "\n");
		}

		NodeList Conn = doc.getElementsByTagName("CONNECTOR");
		for (int i = 0; i < Conn.getLength(); i++) {
			CONNECTOR = (Element) Conn.item(i);

			String FROMINSTANCE = CONNECTOR.getAttribute("FROMINSTANCE");
			String TOINSTANCE = CONNECTOR.getAttribute("TOINSTANCE");
			if (!ConnDenpt.contains(FROMINSTANCE + ":" + TOINSTANCE)) {
				ConnDenpt.add(FROMINSTANCE + ":" + TOINSTANCE);
			}
		}
		System.out.println(Componet.toString());
		System.out.println(ConnDenpt.toString());

	}

	public static void main(String[] args) {
		// TODO Auto-generated method stub
		GetAllElement();
	}

}
