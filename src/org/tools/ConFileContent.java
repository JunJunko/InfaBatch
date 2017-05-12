package org.tools;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class ConFileContent {

	public static String readToString(String fileName) {
		String encoding = "GBK";
		File file = new File(fileName);
		Long filelength = file.length();
		byte[] filecontent = new byte[filelength.intValue()];
		try {
			FileInputStream in = new FileInputStream(file);
			in.read(filecontent);
			in.close();
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
		try {
			return new String(filecontent, encoding);
		} catch (UnsupportedEncodingException e) {
			System.err.println("The OS does not support " + encoding);
			e.printStackTrace();
			return null;
		}
	}

	public static String writeLog(String str, String Type) {
		String Platfrom = org.tools.GetProperties.getKeyValue("System");
		try {
			String path = "";
			switch (Type) {
			case "ȫɾȫ��":
				path = "xml\\M_" + Platfrom + "_" + org.tools.GetProperties.getKeyValue("TableNm").toUpperCase() + "_I"
						+ ".xml";
				break;

			case "upsert":
				path = "xml\\M_" + Platfrom + "_" + org.tools.GetProperties.getKeyValue("TableNm").toUpperCase() + "_U"
						+ ".xml";
				break;

			case "������":
				path = "xml\\M_" + Platfrom + "_" + org.tools.GetProperties.getKeyValue("TableNm").toUpperCase() + "_H"
						+ ".xml";
				break;

			}
			File file = new File(path);
			if (!file.exists())
				file.createNewFile();
			FileOutputStream out = new FileOutputStream(file, false); // ���׷�ӷ�ʽ��true
			StringBuffer sb = new StringBuffer();
			sb.append(str + "\n");
			out.write(sb.toString().getBytes("GBK"));// ע����Ҫת����Ӧ���ַ���
			out.close();
		} catch (IOException ex) {
			ex.printStackTrace();
			System.out.println(ex.getStackTrace());
		}
		return str;
	}

	public static String ReplaceColumnNm(String filename) {
		StringBuffer Data = new StringBuffer();
		// org.tools.UpdateXml.updateAttributeValue(filename);
		try {
			FileInputStream in = new FileInputStream(filename);
			InputStreamReader inReader = new InputStreamReader(in, "GBK");
			BufferedReader bufReader = new BufferedReader(inReader);
			String line = null;
			while ((line = bufReader.readLine()) != null) {

				Data.append(line + "\n");

			}
			bufReader.close();
			inReader.close();
			in.close();
		} catch (Exception e) {
			e.printStackTrace();
			System.out.println("��ȡ" + filename + "����");
		}
		return Data.toString()
				.replace(
						"BUSINESSNAME=\"DW_ETL_DT\" DESCRIPTION=\"\" DATATYPE=\"timestamp\" KEYTYPE=\"NOT A KEY\" PRECISION=\"19\"",
						"BUSINESSNAME=\"DW_ETL_DT\" DESCRIPTION=\"\" DATATYPE=\"date\" KEYTYPE=\"NOT A KEY\" PRECISION=\"10\"")
				.replace(
						"BUSINESSNAME=\"DW_START_DT\" DESCRIPTION=\"\" DATATYPE=\"timestamp\" KEYTYPE=\"NOT A KEY\" PRECISION=\"19\"",
						"BUSINESSNAME=\"DW_START_DT\" DESCRIPTION=\"\" DATATYPE=\"date\" KEYTYPE=\"NOT A KEY\" PRECISION=\"10\"")
				.replace(
						"BUSINESSNAME=\"DW_END_DT\" DESCRIPTION=\"\" DATATYPE=\"timestamp\" KEYTYPE=\"NOT A KEY\" PRECISION=\"19\"",
						"BUSINESSNAME=\"DW_END_DT\" DESCRIPTION=\"\" DATATYPE=\"date\" KEYTYPE=\"NOT A KEY\" PRECISION=\"10\"")
				 .replace("\"Update else Insert\" VALUE=\"NO", "\"Update else Insert\" VALUE=\"YES")
				.replace("NAME=\"Sorter Cache Size\" VALUE=\"8388608\"", "NAME=\"Sorter Cache Size\" VALUE=\"auto\"")
				.replace("<POWERMART", "<!DOCTYPE POWERMART SYSTEM \"powrmart.dtd\"><POWERMART")
		// .replace("Expression DMO Tx\" REUSABLE=\"NO\"", "Expression DMO Tx\"
		// REUSABLE=\"YES\"")
		;

	}

	public static void main(String args[]) {
		// String XmlData =
		// readToString("M_"+Platfrom+"_"+org.tools.GetProperties.getKeyValue("TableNm")+".xml").replace("<ATTRIBUTE
		// NAME=\"Parameter Filename\" VALUE=\"\"/>", "<ATTRIBUTE
		// NAME=\"Parameter Filename\"
		// VALUE=\"$PMRootDir/EDWParam/edw.param\"/>");
		// System.out.println("<ATTRIBUTE NAME=\"Parameter Filename\"
		// VALUE=\"$PMRootDir/EDWParam/edw.param\"/>");
		// writeLog(ReplaceColumnNm("D:\\workspace\\Uoo-master\\xml\\M_TYJ_TLK_ONSITE_SERVICE_1_H.xml"),
		// "upsert");
		// ReplaceColumnNm("D:\\workspace\\Uoo-master\\xml\\M_TYJ_TLK_ONSITE_SERVICE_1_H.xml");
		org.tools.UpdateXml.updateAttributeValue("D:\\workspace\\Uoo-master\\xml\\M_TLK_ONSITE_SERVICE_1_H.xml", '1');
	}

}
