package org;

import java.util.ArrayList;
import java.util.HashMap;

import org.tools.ExcelUtil;

public class FactoryMapping implements Parameter{

	public static String profilepath = "CBI.properties";
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
		System.out.println(T);
//		org.tools.DelXmlFolder.delAllFile("D:\\workspace\\Uoo-master\\xml\\");
		

		for (int i = 0; i < T.size(); i++) {
			// System.out.println(T.keySet());
			String Type = T.get(AllKey.get(i));
			String MappingNm = "M_"+Platfrom+"_"+AllKey.get(i);
			switch (Type) {
			
			case "全删全插":
				Expression.main(new String[]{"0", AllKey.get(i)}); 
			    Check.main(new String[]{"0", AllKey.get(i)});
			    org.tools.ConFileContent.writeLog(org.tools.ConFileContent.ReplaceColumnNm(MappingNm+"_I.xml", Type), Type);
			    Check.main(new String[]{"0", AllKey.get(i)}); 
				org.tools.ConFileContent.writeLog(org.tools.ConFileContent.ReplaceColumnNm("M_CHECK_"+Platfrom+"_"+AllKey.get(i)+"_CK.xml", Type), Type);
			    break;
		
			case "upsert":
				JoinerMd5.main(new String[]{"0", AllKey.get(i)}); 
				org.tools.ConFileContent.writeLog(org.tools.ConFileContent.ReplaceColumnNm(MappingNm+"_U.xml", Type), Type);
				Expression.main(new String[]{"0", AllKey.get(i)}); 
				org.tools.ConFileContent.writeLog(org.tools.ConFileContent.ReplaceColumnNm(MappingNm+"_I.xml", Type), Type);
				Check.main(new String[]{"0", AllKey.get(i)}); 
				org.tools.ConFileContent.writeLog(org.tools.ConFileContent.ReplaceColumnNm("M_CHECK_"+Platfrom+"_"+AllKey.get(i)+"_CK.xml", Type), Type);
				break;
				
			case "拉链表":
				ZipperTable.main(new String[]{"0", AllKey.get(i)}); 
				org.tools.ConFileContent.writeLog(org.tools.ConFileContent.ReplaceColumnNm(MappingNm+"_H.xml", Type), Type);
				ZipExpression.main(new String[]{"0", AllKey.get(i)});
				org.tools.ConFileContent.writeLog(org.tools.ConFileContent.ReplaceColumnNm(MappingNm+"_I.xml", Type), Type);
				Check.main(new String[]{"0", AllKey.get(i)}); 
				org.tools.ConFileContent.writeLog(org.tools.ConFileContent.ReplaceColumnNm("M_CHECK_"+Platfrom+"_"+AllKey.get(i)+"_CK.xml", Type), Type);
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
