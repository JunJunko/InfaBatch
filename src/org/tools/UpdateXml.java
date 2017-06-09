package org.tools;

import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.IOException;
import java.io.StringBufferInputStream;
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
import org.xml.sax.EntityResolver;
import org.xml.sax.InputSource;
import org.xml.sax.SAXException;

public class UpdateXml {
	static List<String> Keyword = org.tools.RePlaceOG.OG();
	static List<String> DT = org.tools.RePlaceOG.DT();

	public static void updateAttributeValue(String filePath, String Type) {

		Element SOURCE = null;
		Element SOURCEFIELD = null;
		NodeList SourceLab = null;
		DocumentBuilder dBuilder = null;
		Document doc = null;
		NodeList ComponAttr = null;
		DocumentBuilderFactory dbFactory = DocumentBuilderFactory.newInstance();
		try {
			dBuilder = dbFactory.newDocumentBuilder();
			dBuilder.setEntityResolver(new MyEntityResolver());
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
		case "拉链表":
		case "upsert":
		case "check":

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
							System.out.println(name);
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
			// TD的SQ增加Source Filter条件

			NodeList Connect = doc.getElementsByTagName("TRANSFORMATION");
			for (int i = 0; i < Connect.getLength(); i++) {

				Element ConnLab = (Element) Connect.item(i);
				String compon = ConnLab.getAttribute("NAME");
				// System.out.println(ConnLab.getAttribute("NAME"));
				if (compon.substring(0, 5).equals("SQ_O_")) {
					// System.out.println(compon);
					ComponAttr = ConnLab.getElementsByTagName("TABLEATTRIBUTE");
					for (int j = 0; j < ComponAttr.getLength(); j++) {
						Element Attr = (Element) ComponAttr.item(j);
						// System.out.println(Attr.getAttribute("NAME"));
						if (Attr.getAttribute("NAME").equals("Source Filter"))
							// if(compon.substring(compon.length()-2,
							// compon.length()).equals("_H")){
							if (Type.equals("拉链表")) {

							Attr.setAttribute("VALUE", org.tools.GetProperties.getPubKeyValue("ZipSourceFilter"));

							} else {
							Attr.setAttribute("VALUE", org.tools.GetProperties.getPubKeyValue("UpsertSourceFilter"));
							}
						// System.out.println(Attr.getAttribute("NAME")+"
						// "+Attr.getAttribute("VALUE"));
					}

				}
			}

			// break;

		case "全删全插":
		case "append":
		case "init":

			// 修改Tagert关键字加og
			NodeList Tag = doc.getElementsByTagName("TARGET");

			for (int k = 0; k < Tag.getLength(); k++) {
				SOURCE = (Element) Tag.item(k);

				SourceLab = SOURCE.getElementsByTagName("TARGETFIELD");
				// System.out.println(SourceLab);
				for (int j = 0; j < SourceLab.getLength(); j++) {
					SOURCEFIELD = (Element) SourceLab.item(j);
					name = SOURCEFIELD.getAttribute("NAME");
					switch (Type) {
					case "拉链表":
						String a = SOURCEFIELD.getAttribute("NAME")+"seize_a_seat";

							if (!DT.contains(a)
									&& !a.substring(0, 3).equals("DW_")) {

								// System.out.println(name);
								name = SOURCEFIELD.getAttribute("NAME").substring(0,
										SOURCEFIELD.getAttribute("NAME").length() - 1);
							}
						 else {
							name = SOURCEFIELD.getAttribute("NAME");
						}
						break;
					case "upsert":
						name = SOURCEFIELD.getAttribute("NAME").replace("_out", "");
						break;
						
					}
					if (Keyword.contains(name)) {
						// System.out.println(name);
						SOURCEFIELD.setAttribute("NAME", name + "_OG");
					} else if (DT.contains(name)) {

						SOURCEFIELD.setAttribute("DATATYPE", "date");
						SOURCEFIELD.setAttribute("PRECISION", "10");
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
				String TOFIELD = ConnLab.getAttribute("TOFIELD");

				switch (Type) {
				case "拉链表":
					// System.out.println(ConnLab.getAttribute("TOFIELD").length());
					if (ConnLab.getAttribute("TOFIELD").length() > 2) {
						if (!ConnLab.getAttribute("TOFIELD").substring(0, 3).equals("DW_"))

							TOFIELD = ConnLab.getAttribute("TOFIELD").substring(0,
									ConnLab.getAttribute("TOFIELD").length() - 1);
					}
					break;
				case "upsert":
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

	public static class MyEntityResolver implements EntityResolver {
		@SuppressWarnings("deprecation")
		public InputSource resolveEntity(String publicId, String systemId) {
			return new InputSource(new StringBufferInputStream(""));
		}
	}

	public static void main(String[] args) {

		updateAttributeValue("D:\\workspace\\Uoo-master\\xml\\CheckXml\\M_CHECK_ELN_SURVEY_ITEM_BACK_CK.xml", "check");
		// System.out.println("ITEM_APNTMNT_OPERATION_TIME22".substring(0,
		// "ITEM_APNTMNT_OPERATION_TIME22".length()-1));

	}

}
