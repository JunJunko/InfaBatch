package org.tools;

import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;

/**
 * =============================================
 * 
 * @Copyright 2017上海新炬网络技术有限公司 @version：1.0.1
 * @author：Junko
 * @date：2017年7月11日下午4:09:14
 * @Description: 根据Excel配置文件的主键信息拼出router组件的表达式
 *               =============================================
 */
public class getPrimaryKeyTran2Expresion {

	/**
	 * @version: 1.0.1
	 * @author: Junko
	 * @date: 2017年7月11日下午4:05:17
	 * @Description: 根据Excel配置文件的主键信息拼出router组件的表达式
	 * @param ReList
	 * @param TableNM
	 * @param Type
	 * @return
	 */
	public static String getPrimayKeyList(List<String> SroucePKList, List<String> TargetPKList, String Type) {

		String isNullValue = SroucePKList.toString().replace("[", "").replace("]", "");
		String compareValue = TargetPKList.toString().replace("[", "").replace("]", "");

		String Fri = "";
		String Sec = "";

		// if (Type.equals("Data_Inserts")) {
		// if (TargetPKList.size() == 1) {
		// Fri = isNullValue;
		// Sec = isNullValue + " = " + compareValue;
		//
		// } else {
		// Fri = "concat("+isNullValue+")";
		// Sec = "concat(" + compareValue + ")" + " = " + "concat(" +
		// isNullValue + ")";
		// }
		// }else{
		// if (TargetPKList.size() == 1) {
		// Fri = compareValue;
		// Sec = isNullValue + " = " + compareValue;
		//
		// } else {
		// Fri = "concat("+compareValue+")";
		//// Sec = "concat(" + compareValue + ")" + " = " + "concat(" +
		// isNullValue + ")";
		// }
		// }
		String Output = "";
		switch (Type) {
		case "Data_Inserts":
			if (TargetPKList.size() == 1) {
				Fri = isNullValue;
				Sec = isNullValue + " = " + compareValue;
			} else {
				Fri = "concat(" + isNullValue + ")";
				Sec = "concat(" + compareValue + ")" + " = " + "concat(" + isNullValue + ")";
			}
			Output = "isnull(" + Fri + ") or (" + Sec + " AND MD5ALL != IN_MD5ALL)";

			break;
		case "Datsa_UDs":
			if (TargetPKList.size() == 1) {
				Fri = compareValue;
				// Sec = isNullValue + " = " + compareValue;

			} else {
				Fri = "concat(" + compareValue + ")";
				// Sec = "concat(" + compareValue + ")" + " = " + "concat(" +
				// isNullValue + ")";
			}
			Output = "isnull(" + Fri + ")";
			break;
		case "Join":
			Output = TargetPKList.get(0) + "=" + SroucePKList.get(0);
			if (TargetPKList.size() > 1)
				for (int i = 1; i < TargetPKList.size(); i++) {
					Output += " AND " + TargetPKList.get(i) + "=" + SroucePKList.get(i);
				}
			break;
		}

		// System.out.println(isNullValue);

		return Output;
	}

	public static void main(String[] args) {
		// TODO Auto-generated method stub
		List<String> a = new ArrayList<String>();
		List<String> b = new ArrayList<String>();
		a.add("ACOSH3");
		a.add("aa");
		b.add("IN_ACOSH3");
		b.add("aa2");
		// System.out.println(a.toString());
		System.out.println(getPrimayKeyList(a, b, "Data_Inserts"));
		System.out.println(getPrimayKeyList(a, b, "Datsa_UDs"));
	}

}
