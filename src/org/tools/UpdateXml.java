package org.tools;

import java.io.File;
import java.io.IOException;
import java.util.List;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.transform.OutputKeys;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerConfigurationException;
import javax.xml.transform.TransformerException;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;

import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.NodeList;
import org.xml.sax.SAXException;

public class UpdateXml {
	static List<String> Keyword = org.tools.RePlaceOG.OG();

	public static void updateAttributeValue(String filePath, char Type) {

		Element SOURCE = null;
		Element SOURCEFIELD = null;
		NodeList SourceLab = null;
		DocumentBuilder dBuilder = null;
		Document doc = null;
		DocumentBuilderFactory dbFactory = DocumentBuilderFactory.newInstance();
		try {
			dBuilder = dbFactory.newDocumentBuilder();
			File xmlFile = new File(filePath);
			doc = dBuilder.parse(xmlFile);
			doc.getDocumentElement().normalize();

			doc.createElement("stuEle");
		} catch (ParserConfigurationException | SAXException | IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		String name = "";

		switch (Type) {
		/*
		 * 1、拉链表 2、upsert 3、全删全插
		 */
		case '1':
		case '2':
			NodeList employees = doc.getElementsByTagName("SOURCE");

			for (int i = 0; i < employees.getLength(); i++) {
				SOURCE = (Element) employees.item(i);

				String DBDNAME = SOURCE.getAttribute("DBDNAME");
				if (DBDNAME.equals("ODS")) {
					SourceLab = SOURCE.getElementsByTagName("SOURCEFIELD");

					for (int j = 0; j < SourceLab.getLength(); j++) {
						SOURCEFIELD = (Element) SourceLab.item(j);
						name = SOURCEFIELD.getAttribute("NAME");
						if (Keyword.contains(name)) {
							// System.out.println(name);
							SOURCEFIELD.setAttribute("NAME", name + "_OG");

						}

					}
				}

			}

			NodeList CONNECTOR = doc.getElementsByTagName("CONNECTOR");
			for (int i = 0; i < CONNECTOR.getLength(); i++) {

				Element ConnLab = (Element) CONNECTOR.item(i);

				String TOINSTANCE = ConnLab.getAttribute("TOINSTANCE").substring(0, 5);

				if (TOINSTANCE.equals("SQ_O_")) {
					String FROMFIELD = ConnLab.getAttribute("TOFIELD");

					if (Keyword.contains(FROMFIELD)) {
						// System.out.println(TOFIELD);
						ConnLab.setAttribute("FROMFIELD", FROMFIELD + "_OG");

					}
				}

			}

			// break;

		case '3':

			// 修改Tagert关键字加og
			NodeList Tag = doc.getElementsByTagName("TARGET");

			for (int k = 0; k < Tag.getLength(); k++) {
				SOURCE = (Element) Tag.item(k);

				SourceLab = SOURCE.getElementsByTagName("TARGETFIELD");
				// System.out.println(SourceLab);
				for (int j = 0; j < SourceLab.getLength(); j++) {
					SOURCEFIELD = (Element) SourceLab.item(j);
					switch (Type) {
					case '1':
						name = SOURCEFIELD.getAttribute("NAME").substring(0, SOURCEFIELD.getAttribute("NAME").length() - 1);
						break;
					case '2':
						name = SOURCEFIELD.getAttribute("NAME").replace("_out", "");
						break;
					}
					if (Keyword.contains(name)) {
						// System.out.println(name);
						SOURCEFIELD.setAttribute("NAME", name + "_OG");
					} else {
						SOURCEFIELD.setAttribute("NAME", name);
					}

				}
			}
			// break;

		}
		// 修改CONNECTOR连线到加了og的字段
		NodeList CONNECTOR = doc.getElementsByTagName("CONNECTOR");
		for (int i = 0; i < CONNECTOR.getLength(); i++) {

			Element ConnLab = (Element) CONNECTOR.item(i);
			// Element TOINSTANCE = (Element)
			// ConnLab.getElementsByTagName("TOINSTANCE");
			String TOINSTANCETYPE = ConnLab.getAttribute("TOINSTANCETYPE");

			// for(int j = 0; j < TOINSTANCE.getLength(); j++){

			if (TOINSTANCETYPE.equals("Target Definition")) {
				String TOFIELD = "";

				switch (Type) {
				case '1':
					TOFIELD = ConnLab.getAttribute("TOFIELD").substring(0, ConnLab.getAttribute("TOFIELD").length()-1);
					break;
				case '2':
					TOFIELD = ConnLab.getAttribute("TOFIELD").replace("_out", "");
					break;
				}
				if (Keyword.contains(TOFIELD)) {
					// System.out.println(TOFIELD);
					ConnLab.setAttribute("TOFIELD", TOFIELD + "_OG");

				} else {
					ConnLab.setAttribute("TOFIELD", TOFIELD);
				}
			}

		}

		TransformerFactory transformerFactory = TransformerFactory.newInstance();
		Transformer transformer;
		try {
			transformer = transformerFactory.newTransformer();
			doc.getDocumentElement().normalize();
			DOMSource domSource = new DOMSource(doc);
			// 设置编码类型
			transformer.setOutputProperty(OutputKeys.ENCODING, "UTF-8");
			StreamResult result = new StreamResult(new File(filePath));
			transformer.transform(domSource, result);
		} catch (TransformerConfigurationException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (TransformerException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

	}

	public static void main(String[] args) {

		updateAttributeValue("D:\\workspace\\Uoo-master\\xml\\M_TLK_ONSITE_SERVICE_1_H.xml", '1');
//		System.out.println("ITEM_APNTMNT_OPERATION_TIME22".substring(0, "ITEM_APNTMNT_OPERATION_TIME22".length()-1));

	}

}
