package org;

import java.util.ArrayList;
import java.util.HashMap;

import org.tools.ExcelUtil;

public class FactoryMapping implements Parameter {

	// public static String profilepath = "ELN.properties";

	protected static ArrayList<ArrayList<String>> TableConf = ExcelUtil
			.readExecl(org.tools.GetProperties.getKeyValue("ExcelPath"));

	public static HashMap<String, String> GetTableList() {
		HashMap<String, String> TL = new HashMap<String, String>();

		for (int i = 0; i < TableConf.size(); i++) {
			ArrayList<String> a = (ArrayList<String>) TableConf.get(i);
			// System.out.println(a);
			if (!TL.containsKey(a.get(0))) {
				TL.put(a.get(0), a.get(4));

			}
		}

		return TL;
	}

	public static void GenMapping(HashMap<String, String> T) {

		ArrayList<String> AllKey = new ArrayList<String>();
		AllKey.addAll(T.keySet());
		org.tools.DelXmlFolder.delAllFile("generaXml\\");
		org.tools.DelXmlFolder.delAllFile("xml\\CheckXml\\");
		org.tools.DelXmlFolder.delAllFile("xml\\InsertXml\\");
		org.tools.DelXmlFolder.delAllFile("xml\\UpsertXml\\");
		org.tools.DelXmlFolder.delAllFile("xml\\ZipXml\\");
		org.tools.DelXmlFolder.delAllFile("xml\\Append\\");
		org.tools.DelXmlFolder.delAllFile("xml\\InitXml\\");

		for (int i = 0; i < T.size(); i++) {
			// System.out.println(T.keySet());
			String Type = T.get(AllKey.get(i));
			String MappingNm = "M_" + AllKey.get(i);
			String Refolder = "generaXml\\";

			switch (Type) {

			case "全删全插":
				Expression.main(new String[] { "0", AllKey.get(i) });

				Check.main(new String[] { "0", AllKey.get(i), "check" });
				org.tools.ConFileContent.writeLog(
						org.tools.ConFileContent.ReplaceColumnNm(Refolder + "I_" + MappingNm + ".xml", Type), Type);
				Init.main(new String[] { "0", AllKey.get(i) });
				org.tools.ConFileContent.writeLog(
						org.tools.ConFileContent.ReplaceColumnNm(Refolder + MappingNm + ".xml", Type), "init");
				org.tools.ConFileContent.writeLog(org.tools.ConFileContent.ReplaceColumnNm(
						Refolder + "M_CHECK_" + Platfrom + "_" + AllKey.get(i) + "_CK.xml", "check"), "check");
				break;

			case "append":
				Append.main(new String[] { "0", AllKey.get(i) });

				Check.main(new String[] { "0", AllKey.get(i), "check" });
				org.tools.ConFileContent.writeLog(
						org.tools.ConFileContent.ReplaceColumnNm(Refolder + "A_" + MappingNm + ".xml", Type), Type);
				Init.main(new String[] { "0", AllKey.get(i) });
				org.tools.ConFileContent.writeLog(
						org.tools.ConFileContent.ReplaceColumnNm(Refolder + MappingNm + ".xml", Type), "init");
				org.tools.ConFileContent.writeLog(org.tools.ConFileContent.ReplaceColumnNm(
						Refolder + "M_CHECK_" + Platfrom + "_" + AllKey.get(i) + "_CK.xml", "check"), "check");
				break;

			case "upsert":
				JoinerMd5.main(new String[] { "0", AllKey.get(i) });

				org.tools.ConFileContent.writeLog(
						org.tools.ConFileContent.ReplaceColumnNm(Refolder + "U_" + MappingNm + ".xml", Type), Type);
				Init.main(new String[] { "0", AllKey.get(i) });
				org.tools.ConFileContent.writeLog(
						org.tools.ConFileContent.ReplaceColumnNm(Refolder + MappingNm + ".xml", "全删全插"), "init");
				Check.main(new String[] { "0", AllKey.get(i), Type });
				org.tools.ConFileContent.writeLog(org.tools.ConFileContent.ReplaceColumnNm(
						Refolder + "M_CHECK_" + Platfrom + "_" + AllKey.get(i) + "_CK.xml", Type), "check");
				break;

			case "拉链表":
				ZipperTable.main(new String[] { "0", AllKey.get(i) });

				org.tools.ConFileContent.writeLog(
						org.tools.ConFileContent.ReplaceColumnNm(Refolder + "H_" + MappingNm + ".xml", Type), Type);
				ZipInit.main(new String[] { "0", AllKey.get(i) });

				org.tools.ConFileContent.writeLog(
						org.tools.ConFileContent.ReplaceColumnNm(Refolder + MappingNm + ".xml", "全删全插"), "init");

				Check.main(new String[] { "0", AllKey.get(i), Type });

				org.tools.ConFileContent.writeLog(org.tools.ConFileContent.ReplaceColumnNm(
						Refolder + "M_CHECK_" + Platfrom + "_" + AllKey.get(i) + "_CK.xml", Type), "check");
				break;

			}

			//
			// if(T.get(AllKey.get(i)).equals("upsert")){
			// System.out.println(AllKey.get(i));
			// }

		}

	}

	public static void main(String[] args) {
		// TODO Auto-generated method stub
		// System.out.println(GetTableList());
		// GetTableList();
		GenMapping(GetTableList());

	}

}
