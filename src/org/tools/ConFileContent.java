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
			case "全删全插":
				path = "xml\\InsertXml\\M_" + Platfrom + "_" + org.tools.GetProperties.getKeyValue("TableNm").toUpperCase() + "_I"
						+ ".xml";
				break;
				
			case "init":
				path = "xml\\InitXml\\M_" + Platfrom + "_" + org.tools.GetProperties.getKeyValue("TableNm").toUpperCase()
						+ ".xml";
				break;

			case "upsert":
				path = "xml\\UpsertXml\\M_" + Platfrom + "_" + org.tools.GetProperties.getKeyValue("TableNm").toUpperCase() + "_U"
						+ ".xml";
				break;
				
			case "append":
				path = "xml\\Append\\M_" + Platfrom + "_" + org.tools.GetProperties.getKeyValue("TableNm").toUpperCase() + "_A"
						+ ".xml";
				break;


			case "拉链表":
				path = "xml\\ZipXml\\M_" + Platfrom + "_" + org.tools.GetProperties.getKeyValue("TableNm").toUpperCase() + "_H"
						+ ".xml";
				break;
			case "check":
				path = "xml\\CheckXml\\M_CHECK_" + Platfrom + "_" + org.tools.GetProperties.getKeyValue("TableNm").toUpperCase() + "_CK"
						+ ".xml";
				break;

			}
			File file = new File(path);
			if (!file.exists())
				file.createNewFile();
			FileOutputStream out = new FileOutputStream(file, false); // 如果追加方式用true
			StringBuffer sb = new StringBuffer();
			sb.append(str + "\n");
			out.write(sb.toString().getBytes("GBK"));// 注意需要转换对应的字符集
			out.close();
		} catch (IOException ex) {
			ex.printStackTrace();
			System.out.println(ex.getStackTrace());
		}
		return str;
	}

	public static String ReplaceColumnNm(String filename, String Type) {
		StringBuffer Data = new StringBuffer();
		org.tools.UpdateXml.updateAttributeValue(filename, Type);
		String UpdateOption = "";
		switch (Type) {
		case "全删全插":
			UpdateOption = "\"Update else Insert\" VALUE=\"NO";
			break;
			
		case "append":
			UpdateOption = "\"Update else Insert\" VALUE=\"YES";
			break;

		case "upsert":
			UpdateOption = "\"Update else Insert\" VALUE=\"YES";
			break;

		case "拉链表":
			UpdateOption = "\"Update else Insert\" VALUE=\"YES";
			break;

		case "check":
			UpdateOption = "\"Update else Insert\" VALUE=\"YES";
			break;

		}
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
			System.out.println("读取" + filename + "出错！");
		}
		return Data.toString()
				.replace("\"Update else Insert\" VALUE=\"NO", UpdateOption)
				.replace("NAME=\"Sorter Cache Size\" VALUE=\"8388608\"", "NAME=\"Sorter Cache Size\" VALUE=\"auto\"")
				.replace("<POWERMART", "<!DOCTYPE POWERMART SYSTEM \"powrmart.dtd\"><POWERMART")
				.replace("FAIL_PARENT_IF_INSTANCE_DID_NOT_RUN=\"NO\" FAIL_PARENT_IF_INSTANCE_FAILS=\"NO\"", "FAIL_PARENT_IF_INSTANCE_DID_NOT_RUN=\"YES\" FAIL_PARENT_IF_INSTANCE_FAILS=\"YES\"")
		// .replace("Expression DMO Tx\" REUSABLE=\"NO\"", "Expression DMO Tx\"
		// REUSABLE=\"YES\"")
		;

	}

	public static void main(String args[]) {
		
		writeLog(ReplaceColumnNm("D:\\workspace\\Uoo-master\\M_TLK_ONSITE_SERVICE_1_H.xml", "拉链表"), "拉链表");
		// org.tools.UpdateXml.updateAttributeValue("D:\\workspace\\Uoo-master\\M_TLK_ONSITE_SERVICE_1_H.xml",
		// "拉链表");
	}

}
