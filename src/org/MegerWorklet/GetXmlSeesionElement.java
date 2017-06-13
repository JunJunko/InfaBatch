package org.MegerWorklet;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import org.dom4j.Document;
import org.dom4j.DocumentException;
import org.dom4j.io.SAXReader;
import org.dom4j.Element;

public class GetXmlSeesionElement {

	public static String GetSession(String filePath) {

		String[] T = filePath.split("\\\\");
		List<String> Type = new ArrayList<String>();
		Type.toArray(T);

		SAXReader reader = new SAXReader();
		Document document = null;

		try {
			reader.setEntityResolver(new IgnoreDTDEntityResolver()); 
			document = (Document) reader.read(new File(filePath));
		} catch (DocumentException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		Element rootElm = document.getRootElement();
		String testxml = rootElm.asXML();
		String result = testxml.substring(testxml.indexOf("<SESSION"), testxml.lastIndexOf("</SESSION>")+10);
		//
//		System.out.println(result);
		return result;
	}

	public static void main(String[] args) {
		// TODO Auto-generated method stub
		System.out.println(GetSession("xml\\CheckXml\\M_CHECK_KMS_COMP_DBCP_CK.xml"));
	}

}
