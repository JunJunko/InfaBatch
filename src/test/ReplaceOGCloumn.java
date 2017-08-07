package test;

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


public class ReplaceOGCloumn {

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

	public static String ReplaceColumnNm(String filename) {
		StringBuffer Data = new StringBuffer();
		try {
			FileInputStream in = new FileInputStream(filename);
			InputStreamReader inReader = new InputStreamReader(in, "GBK");
			BufferedReader bufReader = new BufferedReader(inReader);
			String line = null;

			String ConReg = ".*CONNECTOR.*"; // 判断字符串中是否含有CONNECTOR
			String Conregex = "TOFIELD =\"(.*?)\"";
//			String TableNM = "TOINSTANCE =\"(.*?)\"";
			String TableNM = ".*FROMINSTANCE =\"SQ_(.*?)\".*";
			String TransType = ".*FROMINSTANCE =\"SQ_.*";
			String SYS = "WECHAT";

			while ((line = bufReader.readLine()) != null) {

				if (line.matches(ConReg) && line.matches(TransType)) {
					 System.out.println(line);
					Pattern pattern = Pattern.compile(Conregex);
					Pattern pattern2 = Pattern.compile(TableNM);

					Matcher m1 = pattern.matcher(line);
					Matcher m2 = pattern2.matcher(line);

					if (m1.find() && m2.find()) {
						String ReplaceStr = m1.group(1);
						String TBNM = m2.group(1);
//						System.out.println(TBNM);
						// System.out.println(ReplaceStr);
						// System.out.println("第" + i + "行：" + sourceStrArray);
						// System.out.println(line.replaceAll("TOFIELD=\"(.*?)_out2\"",
						// "TOFIELD=\""+ReplaceStr+"\""));
						// System.out.println("++++++++++++++++++"+ReplaceStr);
						if (org.tools.RePlaceOG.OG().contains(ReplaceStr)) {
							Data.append(line.replaceAll(" TOFIELD=\".*?\"", " TOFIELD=\"" + ReplaceStr + "_OG" + "\""));
							// <CONNECTOR FROMFIELD ="TITLE" FROMINSTANCE
							// ="FIT_wechat_vote_user" FROMINSTANCETYPE
							// ="Filter" TOFIELD ="TITLE_OG" TOINSTANCE
							// ="O_DLPM_WECHAT_VOTE_USER_CK" TOINSTANCETYPE
							// ="Target Definition"/>

							String a = line.replaceAll(" FROMINSTANCE =\".*?\"", " FROMINSTANCE =\"exp_transform\"")
									.replace("FROMINSTANCETYPE =\"Source Qualifier\"",
											"FROMINSTANCETYPE =\"Expression\"")
									.replaceAll(" TOFIELD =\".*?\"", " TOFIELD =\"" + ReplaceStr + "_OG" + "\"")
									.replace("md5_T", "")
									.replaceAll("TOINSTANCE =\".*?\"",
											"TOINSTANCE =\"O_"+SYS+"_" + TBNM.replace("SRT", "").toUpperCase()+"\"")
									.replace("TOINSTANCETYPE =\"Expression\"", "TOINSTANCETYPE =\"Target Definition\"");
							Data.append(a);
							// System.out.println(line);
//							System.out.println(a);
							Data.append("\n");
						} else {
							Data.append(line + "\n");
						}
					} else {
						Data.append(line + "\n");
					}
					// System.out.println("第" + i + "行：" +line);

				} else {
					Data.append(line + "\n");
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
			System.out.println("读取" + filename + "出错！");
		}
		// return "";
		return Data.toString();
	}

	public static String writeLog(String str) {
		try {
			String path = "tt.xml";
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

	public static void main(String[] args) {
		// TODO Auto-generated method stub

		writeLog(ReplaceColumnNm("F:\\g工作资料\\shsnc\\无限极\\check_xml\\ALL_QQSURVEY_INITIALIZATION.XML"));
//		 System.out.println(ReplaceColumnNm("F:\\g工作资料\\shsnc\\无限极\\check_xml\\ALL_QQSURVEY_INITIALIZATION.XML"));
	}

}
