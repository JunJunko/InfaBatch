package org;

import java.util.ArrayList;
import java.util.HashMap;

import org.tools.ExcelUtil;

/**
 * @author Administrator
 *
 */
public class FactoryMapping implements Parameter {

	protected static ArrayList<ArrayList<String>> TableConf = ExcelUtil
			.readExecl(org.tools.GetProperties.getKeyValue("ExcelPath"));

	/**
	 * @author    Junko
	 * <p> Description     从Excel配置文件取出表名和列名对应的kv对
	 * @return    表名和列名的kv对
	 * */
	public static HashMap<String, String> GetTableList() {
		HashMap<String, String> TL = new HashMap<String, String>();

		for (int i = 0; i < TableConf.size(); i++) {
			ArrayList<String> a = (ArrayList<String>) TableConf.get(i);
			if (!TL.containsKey(a.get(0))) {
				TL.put(a.get(0), a.get(4));

			}
		}

		return TL;
	}
	
	/**
	 * @author    Junko
	 * <p> Description     根据Excel配置的信息生成各种入仓逻辑XML的工厂方法，删除上次生成XML文件
	 * @param     表名和列名的kv对
	 * @see       Expression、Check、org.tools.ConFileContent.writeLog、Init、Append、Upsert、ZipperTable、ZipInit
	 */

	public static void GenMapping(HashMap<String, String> TableCloumnKV) {

		ArrayList<String> AllKey = new ArrayList<String>();
		AllKey.addAll(TableCloumnKV.keySet());
		org.tools.DelXmlFolder.delAllFile("generaXml\\");
		org.tools.DelXmlFolder.delAllFile("xml\\CheckXml\\");
		org.tools.DelXmlFolder.delAllFile("xml\\InsertXml\\");
		org.tools.DelXmlFolder.delAllFile("xml\\UpsertXml\\");
		org.tools.DelXmlFolder.delAllFile("xml\\ZipXml\\");
		org.tools.DelXmlFolder.delAllFile("xml\\Append\\");
		org.tools.DelXmlFolder.delAllFile("xml\\InitXml\\");

		for (int i = 0; i < TableCloumnKV.size(); i++) {
			String Type = TableCloumnKV.get(AllKey.get(i)).toLowerCase();
			String MappingNm = "M_" + AllKey.get(i);
			String Refolder = "generaXml\\";

			switch (Type) {

			/*case "全删全插":
				Expression.main(new String[] { "0", AllKey.get(i) });

				
				org.tools.ConFileContent.writeLog(
						org.tools.ConFileContent.ReplaceColumnNm(Refolder + "I_" + MappingNm + ".xml", Type), Type);
				Init.main(new String[] { "0", AllKey.get(i) });
				org.tools.ConFileContent.writeLog(
						org.tools.ConFileContent.ReplaceColumnNm(Refolder + MappingNm + ".xml", Type), "init");
				
				Check.main(new String[] { "0", AllKey.get(i), "check" });
				org.tools.ConFileContent.writeLog(org.tools.ConFileContent.ReplaceColumnNm(
						Refolder + "M_CHECK_" + Platfrom + "_" + AllKey.get(i) + "_CK.xml", "check"), "check");
				break;*/
//
//			case "append":
//			/*	Append.main(new String[] { "0", AllKey.get(i) });
//				
//				org.tools.ConFileContent.writeLog(
//						org.tools.ConFileContent.ReplaceColumnNm(Refolder + "A_" + MappingNm + ".xml", Type), Type);
//				Init.main(new String[] { "0", AllKey.get(i) });
//				org.tools.ConFileContent.writeLog(
//						org.tools.ConFileContent.ReplaceColumnNm(Refolder + MappingNm + ".xml", "init"), "init");*/
//				
//				Check.main(new String[] { "0", AllKey.get(i), "check" });
//				org.tools.ConFileContent.writeLog(org.tools.ConFileContent.ReplaceColumnNm(
//						Refolder + "M_CHECK_" + Platfrom + "_" + AllKey.get(i) + "_CK.xml", "check"), "check");
//				break;
//
			case "upsert":
				Upsert.main(new String[] { "0", AllKey.get(i) });

				org.tools.ConFileContent.writeLog(
						org.tools.ConFileContent.ReplaceColumnNm(Refolder + "U_" + MappingNm + ".xml", Type), Type);
				/*Init.main(new String[] { "0", AllKey.get(i) });
				org.tools.ConFileContent.writeLog(
						org.tools.ConFileContent.ReplaceColumnNm(Refolder + MappingNm + ".xml", "全删全插"), "init");
				Check.main(new String[] { "0", AllKey.get(i), Type });
				org.tools.ConFileContent.writeLog(org.tools.ConFileContent.ReplaceColumnNm(
						Refolder + "M_CHECK_" + Platfrom + "_" + AllKey.get(i) + "_CK.xml", "check"), "check");*/
				break;

			case "拉链表":
						ZipperTable.main(new String[] { "0", AllKey.get(i) });

				org.tools.ConFileContent.writeLog(
						org.tools.ConFileContent.ReplaceColumnNm(Refolder + "H_" + MappingNm + ".xml", Type), Type);
				/*ZipInit.main(new String[] { "0", AllKey.get(i) });

					org.tools.ConFileContent.writeLog(
						org.tools.ConFileContent.ReplaceColumnNm(Refolder + MappingNm + ".xml", "全删全插"), "init");

				ZipCheck.main(new String[] { "0", AllKey.get(i), Type });

				org.tools.ConFileContent.writeLog(org.tools.ConFileContent.ReplaceColumnNm(
						Refolder + "M_CHECK_" + Platfrom + "_" + AllKey.get(i) + "_CK.xml", "check"), "check");*/
				break;

			}

		}

	}

	

	public static void main(String[] args) {
		// TODO Auto-generated method stub
		GenMapping(GetTableList());

	}

}
