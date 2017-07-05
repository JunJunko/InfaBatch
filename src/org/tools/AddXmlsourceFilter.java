package org.tools;

import java.io.File;
import java.io.IOException;
import java.util.Arrays;
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

public class AddXmlsourceFilter {

	/**
	 * @author Junko
	 * @param filePath文件路径
	 */
	public static void updateAttributeValue(String filePath) {

		NodeList ComponAttr = null;
		DocumentBuilder dBuilder = null;
		Document doc = null;
		String[] a = {"mas_tag_classify"
				,"mas_tag_group"
				,"mas_tag_library"
				,"mas_user_point_rule"
				,"res_activity_receive_address"
				,"res_banner"
				,"res_region"

};
		
		List<String> list = Arrays.asList(a);  
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

//		NodeList employees = doc.getElementsByTagName("MAPPING");

		NodeList Connect = doc.getElementsByTagName("TRANSFORMATION");
		for (int i = 0; i < Connect.getLength(); i++) {

			Element ConnLab = (Element) Connect.item(i);
			String compon = ConnLab.getAttribute("NAME");
//			 System.out.println(ConnLab.getAttribute("NAME"));
			if (compon.substring(0, 5).equals("SQ_O_")) {
//				System.out.println(compon);
				ComponAttr = ConnLab.getElementsByTagName("TABLEATTRIBUTE");
				for (int j = 0; j < ComponAttr.getLength(); j++) {
					Element Attr = (Element) ComponAttr.item(j);
//					System.out.println(Attr.getAttribute("NAME"));
					if(Attr.getAttribute("NAME").equals("Source Filter") )
//						if(compon.substring(compon.length()-2, compon.length()).equals("_H")){
						if(list.contains(compon.replace("SQ_O_TYJ_", ""))){
							
							Attr.setAttribute("VALUE", "DW_END_DT=to_date('2999-12-31','YYYY-MM-DD')");
//							System.out.println(compon.replace("SQ_O_WECHAT_", ""));
						}else{
//							System.out.println(compon.substring(compon.length()-2, compon.length()));
//							System.out.println(compon);
							Attr.setAttribute("VALUE", "DW_OPER_FLAG=1");
						}
//						System.out.println(Attr.getAttribute("NAME")+"            "+Attr.getAttribute("VALUE"));
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
            transformer.setOutputProperty(OutputKeys.ENCODING, "gbk");  
            StreamResult result = new StreamResult(new File(filePath));  
            transformer.transform(domSource, result);  
		} catch (TransformerConfigurationException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (TransformerException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} 

		// loop for each employee
		// for (int i = 0; i < employees.getLength(); i++) {
		// SOURCE = (Element) employees.item(i);
		//
		//// System.out.println(SOURCE.getAttribute("DBDNAME"));
		// String DBDNAME = SOURCE.getAttribute("DBDNAME");
		// if (DBDNAME.equals("ODS")) {
		// SourceLab = SOURCE.getElementsByTagName("SOURCEFIELD");
		//// System.out.println(SourceLab);
		// for (int j = 0; j < SourceLab.getLength(); j++) {
		// SOURCEFIELD = (Element) SourceLab.item(j);
		// String name = SOURCEFIELD.getAttribute("NAME");
		// if(org.tools.RePlaceOG.OG().contains(name)){
		// System.out.println(name);
		// SOURCEFIELD.setAttribute("NAME", name+"_OG");
		//
		// }
		//// System.out.println(name);
		//
		// TransformerFactory transformerFactory =
		// TransformerFactory.newInstance();
		// Transformer transformer;
		// try {
		// transformer = transformerFactory.newTransformer();
		// doc.getDocumentElement().normalize();
		// DOMSource domSource = new DOMSource(doc);
		// // 设置编码类型
		// transformer.setOutputProperty(OutputKeys.ENCODING, "UTF-8");
		// StreamResult result = new StreamResult(new File(filePath));
		// transformer.transform(domSource, result);
		// } catch (TransformerConfigurationException e) {
		// // TODO Auto-generated catch block
		// e.printStackTrace();
		// } catch (TransformerException e) {
		// // TODO Auto-generated catch block
		// e.printStackTrace();
		// }
		//
		// }
		// }
		//
		// // prefix id attribute with M
		//
		// // }else{
		// // //prefix id attribute with F
		// // emp.setAttribute("id", "F"+emp.getAttribute("id"));
		// // }
		// }

	}

	public static void main(String[] args) {

		updateAttributeValue("F:\\g工作资料\\shsnc\\无限极\\check_xml\\ALL_TYJ_UPSERT_CHAIN_INCREMENT.XML");

	}

}
