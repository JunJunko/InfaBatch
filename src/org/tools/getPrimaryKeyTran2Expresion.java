package org.tools;

import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;


public class getPrimaryKeyTran2Expresion {
	/**
	 * describe:
	 * 根据源表的主键生成Route主键所需的表达式<p>
	 * 
	 * @author Junko
	 * @param  1、ExcelUtil.readExecl返回的List， 2、属性文件当前的TableNM属性， 3、Route的分组
	 * @return Route组件所需的表达式
	 *
	 */
	public static String getPrimayKeyList(ArrayList<ArrayList<String>> ReList, String TableNM, String Type) {
		HashMap<String, List<String>> AllKey = new HashMap<String, List<String>>();
		List<String> AllTableNM = new ArrayList<String>();
		List<String> PrimaryColumn = new ArrayList<String>();
		// 遍历所有表名到List
		for (int i = 0; i < ReList.size(); i++) {
			ReList.get(i).get(0);
			if (!AllTableNM.contains(ReList.get(i).get(0)))
				AllTableNM.add(ReList.get(i).get(0));
			// System.out.println(ReList.get(i).get(0));

		}

		for (int i = 0; i < AllTableNM.size(); i++) {
			PrimaryColumn = new ArrayList<String>();
			for (int j = 0; j < ReList.size(); j++) {
				if (AllTableNM.get(i).equals(ReList.get(j).get(0))) {
					// System.out.println(AllTableNM.get(i)+"-----------"+ReList.get(j).get(0));
					if (ReList.get(j).get(5).equals("pri")) {

						PrimaryColumn.add(ReList.get(j).get(1));
						// System.out.println(PrimaryColumn);
					}

				}

			}
			AllKey.put(AllTableNM.get(i), PrimaryColumn);
		}
		String SourceValue = AllKey.get(TableNM).toString().replace("[", "").replace("]", "");

		List<String> InValueList = new ArrayList<String>();
		String compareValue = "";
		
		for (int i = 0; i < AllKey.get(TableNM).size(); i++) {
			InValueList.add("IN_" + AllKey.get(TableNM).get(i));
		}
		String InValue = InValueList.toString().replace("[", "").replace("]", "");
		String isNullValue = InValue.replace(",", "||");;
		if (Type.equals("Data_UDs")) {
		      isNullValue = SourceValue.replace(",", "||");
		}

		if (SourceValue.indexOf(",") > 0) {
			compareValue = "concat(" + SourceValue + ")" + " = " + "concat(" + InValue + ")";
		} else {
			compareValue = SourceValue + " = " + InValue;
		}

		String Output = "isnull(" + isNullValue + ") or (" + compareValue + " AND MD5ALL != IN_MD5ALL)";
//		System.out.println(Data_UDs);
		return Output;
	}

	public static void main(String[] args) {
		// TODO Auto-generated method stub
		System.out.println(getPrimayKeyList(org.tools.getExcelData.readExecl("D:\\EXCEL\\OUT2_OUT_CBI_20170704155607660.xls"), "SDO_ELLIPSOIDS", "Datsa_UDs"));
	}

}
