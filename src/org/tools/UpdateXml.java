package org.tools;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;
import java.io.PrintStream;
import java.io.StringBufferInputStream;
import java.util.HashMap;
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

import org.FactoryMapping;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.NodeList;
import org.xml.sax.EntityResolver;
import org.xml.sax.InputSource;
import org.xml.sax.SAXException;

/**
 * =============================================
 * 
 * @Copyright 2017上海新炬网络技术有限公司 @version：1.0.1
 * @author：Junko
 * @date：2017年7月11日下午4:10:48
 * @Description: 根据入仓逻辑修改XML的属性 =============================================
 */
public class UpdateXml {
	static List<String> Keyword = org.tools.RePlaceOG.OG();
	static List<String> DT = org.tools.RePlaceOG.DT();
	protected static Log log = LogFactory.getLog(UpdateXml.class);

	/**
	 * @version: 1.0.1
	 * @author: Junko
	 * @date: 2017年7月11日下午4:11:12
	 * @Description: 根据入仓逻辑修改XML的属性,修改了关键字，目标表字段名, CONNECTOR标签
	 * @param filePath
	 * @param Type
	 */
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
			ByteArrayOutputStream baos = new ByteArrayOutputStream();  
			e.printStackTrace(new PrintStream(baos));  
			String exception = baos.toString();  
			log.error( exception);			
			}

		String name = "";

		switch (Type) {
		/*
		 * 1、拉链表 2、upsert 3、全删全插
		 */

		case "check":
		case "zcheck":
			NodeList employees = doc.getElementsByTagName("CONNECTOR");

			for (int i = 0; i < employees.getLength(); i++) {
				SOURCE = (Element) employees.item(i);

				String DBDNAME = SOURCE.getAttribute("TOINSTANCETYPE");
				if (SOURCE.getAttribute("FROMFIELD").length() > 3)
					if (DBDNAME.equals("Source Qualifier")
							&& SOURCE.getAttribute("FROMFIELD").substring(0, 3).equals("IN_")) {
						name = SOURCE.getAttribute("FROMFIELD");
						SOURCE.setAttribute("FROMFIELD", name.replace("IN_", ""));

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

			

			// break;
		case "拉链表":
		case "upsert":
		case "全删全插":
		case "append":
		case "init":
		
			NodeList Connect = doc.getElementsByTagName("TRANSFORMATION");
//			for (int i = 0; i < Connect.getLength(); i++) {
//
//				Element ConnLab = (Element) Connect.item(i);
//				String compon = ConnLab.getAttribute("NAME");
//				// System.out.println(ConnLab.getAttribute("NAME"));
//				if (compon.substring(0, 5).equals("SQ_O_")) {
//					// System.out.println(compon);
//					ComponAttr = ConnLab.getElementsByTagName("TABLEATTRIBUTE");
//					for (int j = 0; j < ComponAttr.getLength(); j++) {
//						Element Attr = (Element) ComponAttr.item(j);
//						// System.out.println(Attr.getAttribute("NAME"));
//						if (Attr.getAttribute("NAME").equals("Source Filter"))
//							// if(compon.substring(compon.length()-2,
//							// compon.length()).equals("_H")){
//							if (Type.equals("拉链表") || Type.equals("zcheck")) {
//
//							Attr.setAttribute("VALUE", org.tools.GetProperties.getPubKeyValue("ZipSourceFilter"));
//
//							} else {
//							Attr.setAttribute("VALUE", org.tools.GetProperties.getPubKeyValue("UpsertSourceFilter"));
//							}
//						// System.out.println(Attr.getAttribute("NAME")+"
//						// "+Attr.getAttribute("VALUE"));
//					}
//
//				}
//			}
			// 修改Tagert关键字加IN_
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
					case "upsert":
						String a = SOURCEFIELD.getAttribute("NAME") + "seize_a_seat";

						if (!a.substring(0, 3).equals("DW_")) {
							if (a.substring(0, 3).equals("IN_")) {
								name = SOURCEFIELD.getAttribute("NAME").substring(3,
										SOURCEFIELD.getAttribute("NAME").length() - 1);
							} else {
								name = SOURCEFIELD.getAttribute("NAME").substring(0,
										SOURCEFIELD.getAttribute("NAME").length() - 1);
							}

						} else if (SOURCEFIELD.getAttribute("NAME").equals("DW_START_DT1")) {
							// System.out.println(SOURCEFIELD.getAttribute("NAME").substring(0,
							// SOURCEFIELD.getAttribute("NAME").length() - 2) +
							// "------------------"+
							// SOURCEFIELD.getAttribute("NAME").substring(0,
							// SOURCEFIELD.getAttribute("NAME").length() -
							// 1)+"length:"+SOURCEFIELD.getAttribute("NAME").length());

							name = "DW_START_DT";
						} else {
							name = SOURCEFIELD.getAttribute("NAME");
						}
						break;
//					case "upsert":
//						a = SOURCEFIELD.getAttribute("NAME") + "seize_a_seat";
//
//						if (!a.substring(0, 3).equals("DW_")) {
//
//							System.out.println(name);
//							name = SOURCEFIELD.getAttribute("NAME").substring(0,
//									SOURCEFIELD.getAttribute("NAME").length() - 1)
//							// .substring(3,
//							// SOURCEFIELD.getAttribute("TOFIELD").length())
//							;
//						} else {
//							name = SOURCEFIELD.getAttribute("NAME");
//						}
//						break;
					// case "upsert":
					// name = SOURCEFIELD.getAttribute("NAME").replace("_out",
					// "");
					// break;

					}
					if (Keyword.contains(name)) {
						// System.out.println(name);
						SOURCEFIELD.setAttribute("NAME", name + "_OG");
					} else if (DT.contains(name)) {

						SOURCEFIELD.setAttribute("DATATYPE", "date");
						SOURCEFIELD.setAttribute("PRECISION", "10");
						SOURCEFIELD.setAttribute("NAME", name);
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
				case "upsert":
					// System.out.println(ConnLab.getAttribute("TOFIELD").length());
					if (ConnLab.getAttribute("TOFIELD").length() > 2) {
						if ((!ConnLab.getAttribute("TOFIELD").substring(0, 3).equals("DW_")) && (ConnLab.getAttribute("TOFIELD").substring(0, 3).equals("IN_"))) {
							TOFIELD = ConnLab.getAttribute("TOFIELD").substring(3,
									ConnLab.getAttribute("TOFIELD").length() - 1);
						} else if (!ConnLab.getAttribute("TOFIELD").substring(0, 3).equals("IN_") && (!ConnLab.getAttribute("TOFIELD").substring(0, 3).equals("DW_"))) {

							TOFIELD = ConnLab.getAttribute("TOFIELD").substring(0,
									ConnLab.getAttribute("TOFIELD").length() - 1);
						}else if (ConnLab.getAttribute("TOFIELD").equals("DW_START_DT1")) {

							TOFIELD = "DW_START_DT";
						}
					}
					break;
//				case "upsert":
//					// TOFIELD = ConnLab.getAttribute("TOFIELD").replace("_out",
//					// "");
//					// 该用新逻辑
//					if (ConnLab.getAttribute("TOFIELD").length() > 2) {
//						if (!ConnLab.getAttribute("TOFIELD").substring(0, 3).equals("DW_"))
//
//							TOFIELD = ConnLab.getAttribute("TOFIELD").substring(0,
//									ConnLab.getAttribute("TOFIELD").length() - 1)
//							// .substring(3,
//							// ConnLab.getAttribute("TOFIELD").length())
//							;
//					}
//					break;
				}
				
				ConnLab.setAttribute("TOFIELD", TOFIELD);
//				if (Keyword.contains(TOFIELD)) {
//					// System.out.println(TOFIELD);
//					ConnLab.setAttribute("TOFIELD", TOFIELD + "_OG");
//
//				} else {
//					ConnLab.setAttribute("TOFIELD", TOFIELD);
//				}
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
			ByteArrayOutputStream baos = new ByteArrayOutputStream();  
			e.printStackTrace(new PrintStream(baos));  
			String exception = baos.toString();  
			log.error( exception);			
		} catch (TransformerException e) {
			// TODO Auto-generated catch block
			ByteArrayOutputStream baos = new ByteArrayOutputStream();  
			e.printStackTrace(new PrintStream(baos));  
			String exception = baos.toString();  
			log.error( exception);			
		}

	}

	public static class MyEntityResolver implements EntityResolver {
		@SuppressWarnings("deprecation")
		public InputSource resolveEntity(String publicId, String systemId) {
			return new InputSource(new StringBufferInputStream(""));
		}
	}

}