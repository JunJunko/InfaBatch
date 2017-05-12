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

public class ConFileContent  {
	

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
				path = "xml\\M_" + Platfrom + "_" + org.tools.GetProperties.getKeyValue("TableNm").toUpperCase() + "_I"
						+ ".xml";
				break;

			case "upsert":
				path = "xml\\M_" + Platfrom + "_" + org.tools.GetProperties.getKeyValue("TableNm").toUpperCase() + "_U"
						+ ".xml";
				break;

			case "拉链表":
				path = "xml\\M_" + Platfrom + "_" + org.tools.GetProperties.getKeyValue("TableNm").toUpperCase() + "_H"
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

	public static String ReplaceColumnNm(String filename) {
		StringBuffer Data = new StringBuffer();
		org.tools.UpdateXml.updateAttributeValue(filename);
		try {
			FileInputStream in = new FileInputStream(filename);
			InputStreamReader inReader = new InputStreamReader(in, "GBK");
			BufferedReader bufReader = new BufferedReader(inReader);
			String line = null;

			String TagReg = ".*TARGETFIELD.*"; // 判断字符串中是否含有TARGETFIELD
			String Tagregex = ".* NAME=\".*?\".*?";

			String ConReg = ".*CONNECTOR.*"; // 判断字符串中是否含有CONNECTOR
			String Conregex = "TOFIELD=\"(.*?)_out\"";
			String TransType = ".*FROMINSTANCE=\"UPD_.*";

			while ((line = bufReader.readLine()) != null) {

				if (line.matches(TagReg) && line.matches(Tagregex)) {

					Pattern pattern = Pattern.compile(" NAME=\"(.*?)\"");

					Matcher m = pattern.matcher(line);

					if (m.find()) {
						String ReplaceStr = m.group(1).replace("_out", "");
						// System.out.println(ReplaceStr);
//						System.out.println(ReplaceStr);
						if (org.tools.RePlaceOG.OG().contains(ReplaceStr.replace("_out", ""))) {

							Data.append(line.replaceAll(" NAME=\".*?\"",
									" NAME=\"" + ReplaceStr.replace("_out", "") + "_OG" + "\""));
							Data.append("\n");
						} else {
//							 System.out.println(line);
							Data.append(line.replaceAll(" NAME=\".*?_out\"",
									" NAME=\"" + ReplaceStr.replace("_out", "") + "\""));
							Data.append("\n");
						}
					}
					
				} else if (line.matches(ConReg) && line.matches(TransType)) {
					Pattern pattern = Pattern.compile(Conregex);

					Matcher m1 = pattern.matcher(line);
					if (m1.find()) {
						String ReplaceStr = m1.group(1);
						if (org.tools.RePlaceOG.OG().contains(ReplaceStr)) {
							Data.append(line.replaceAll(" TOFIELD=\".*?\"", " TOFIELD=\"" + ReplaceStr + "_OG" + "\""));
							Data.append("\n");
						} else {
							Data.append(line.replaceAll("TOFIELD=\".*?_out\"", "TOFIELD=\"" + ReplaceStr + "\""));
							Data.append("\n");
						}
					} else {
						Data.append(line + "\n");
					}
					// System.out.println("第" + i + "行：" +line);

				}
			
				else {
					Data.append(line + "\n");
				}
			}
			bufReader.close();
			inReader.close();
			in.close();
		} catch (Exception e) {
			e.printStackTrace();
			System.out.println("读取" + filename + "出错！");
		}
		return Data.toString()
				.replace("<ATTRIBUTE NAME=\"Parameter Filename\" VALUE=\"\"/>",
						"<ATTRIBUTE NAME=\"Parameter Filename\" VALUE=\"$PMRootDir/EDWParam/edw.param\"/>")
				.replace(
						"BUSINESSNAME=\"DW_ETL_DT\" DESCRIPTION=\"\" DATATYPE=\"timestamp\" KEYTYPE=\"NOT A KEY\" PRECISION=\"19\"",
						"BUSINESSNAME=\"DW_ETL_DT\" DESCRIPTION=\"\" DATATYPE=\"date\" KEYTYPE=\"NOT A KEY\" PRECISION=\"10\"")
				.replace(
						"BUSINESSNAME=\"DW_START_DT\" DESCRIPTION=\"\" DATATYPE=\"timestamp\" KEYTYPE=\"NOT A KEY\" PRECISION=\"19\"",
						"BUSINESSNAME=\"DW_START_DT\" DESCRIPTION=\"\" DATATYPE=\"date\" KEYTYPE=\"NOT A KEY\" PRECISION=\"10\"")
				.replace(
						"BUSINESSNAME=\"DW_END_DT\" DESCRIPTION=\"\" DATATYPE=\"timestamp\" KEYTYPE=\"NOT A KEY\" PRECISION=\"19\"",
						"BUSINESSNAME=\"DW_END_DT\" DESCRIPTION=\"\" DATATYPE=\"date\" KEYTYPE=\"NOT A KEY\" PRECISION=\"10\"")
				// .replace("\"Update else Insert\" VALUE=\"NO", "\"Update else
				// Insert\" VALUE=\"YES")
				// .replace("\"Treat source rows as\" VALUE=\"Insert\"",
				// "\"Treat source rows as\" VALUE=\"Data driven\"")
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
		 writeLog(ReplaceColumnNm("D:\\workspace\\Uoo-master\\xml\\M_TYJ_TLK_ONSITE_SERVICE_1_H.xml"), "upsert");
//		ReplaceColumnNm("D:\\workspace\\Uoo-master\\xml\\M_TYJ_TLK_ONSITE_SERVICE_1_H.xml");
	}

}
