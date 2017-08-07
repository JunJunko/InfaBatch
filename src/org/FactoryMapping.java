package org;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.tools.ExcelUtil;
import org.tools.GetProperties;

import com.exprotmeteexcel.service.imp.XmlService;

/**
 * =============================================
 * 
 * @Copyright 2017上海新炬网络技术有限公司 @version：1.0.1
 * @author：Junko
 * @date：2017年7月10日下午12:16:30
 * @Description：根據Excel元數據信息生成对应逻辑的XML =============================================
 */

public class FactoryMapping implements Parameter {

	protected static ArrayList<ArrayList<String>> TableConf = ExcelUtil
			.readExecl(GetProperties.getKeyValue("ExcelPath"));
	protected static Log log = LogFactory.getLog(FactoryMapping.class);
	/**
	 * 存放TD配置表的过滤条件
	 */
	// protected final static HashMap<String, String> FilterCondition =
	// getFilterCondition();

	/**
	 * @version: 1.0.1
	 * @author: Junko
	 * @date: 2017年7月13日下午3:17:30
	 * @Description: 从TD配置表取出各个表的过滤条件
	 * @return TD配置表存放各个表的过滤条件
	 */
	// public static HashMap<String, String> getFilterCondition() {
	// ExprotMeteExcelServiceImpl Condition = new ExprotMeteExcelServiceImpl();
	// HashMap<String, String> TableFilterCondition = new HashMap<String,
	// String>();
	// String path = GetProperties.getKeyValue("teradatajdbcpath");
	// String plaform = GetProperties.getKeyValue("System");
	// List<Map<String, Object>> lt = new ArrayList<Map<String, Object>>();
	// lt = Condition.getConfigTableFilter(path, plaform);
	// for (int i = 0; i < lt.size(); i++) {
	// TableFilterCondition.put(lt.get(i).get("TABLE_NM").toString() +
	// "SOURCE_FILTER",
	// lt.get(i).get("SOURCE_FILTER").toString());
	// TableFilterCondition.put(lt.get(i).get("TABLE_NM").toString() +
	// "TARGET_FILTER",
	// lt.get(i).get("TARGET_FILTER").toString());
	// }
	// return TableFilterCondition;
	//
	// }

	/**
	 * @version: 1.0.1
	 * @author: Junko
	 * @date: 2017年7月10日下午2:42:32
	 * @Description: 从Excel配置文件取出表名和列名对应的kv对
	 * @return 已经HashMap的形式返回Excel的信息
	 */
	public static HashMap<String, String> GetTableList() {
		HashMap<String, String> TL = new HashMap<String, String>();

		for (int i = 0; i < TableConf.size(); i++) {
			ArrayList<String> a = (ArrayList<String>) TableConf.get(i);
			if (!TL.containsKey(a.get(0)) & !a.get(4).isEmpty()) {
				TL.put(a.get(0), a.get(4));

			}
		}
		System.out.print(TL);
		return TL;
	}

	/**
	 * @version: 1.0.1
	 * @author: Junko
	 * @date: 2017年7月10日下午2:43:20
	 * @Description: 根据Excel配置的信息生成各种入仓逻辑XML的工厂方法，删除上次生成XML文件
	 * @param TableCloumnKV
	 */
	public static void GenMapping(HashMap<String, String> TableCloumnKV) {

		ArrayList<String> AllKey = new ArrayList<String>();
		AllKey.addAll(TableCloumnKV.keySet());

		// 清空生成XML目录下的文件
		org.tools.DelXmlFolder.delAllFile("generaXml\\");
		org.tools.DelXmlFolder.delAllFile("xml\\CheckXml\\");
		org.tools.DelXmlFolder.delAllFile("xml\\InsertXml\\");
		org.tools.DelXmlFolder.delAllFile("xml\\UpsertXml\\");
		org.tools.DelXmlFolder.delAllFile("xml\\ZipXml\\");
		org.tools.DelXmlFolder.delAllFile("xml\\Append\\");
		org.tools.DelXmlFolder.delAllFile("xml\\InitXml\\");
		log.info("清空工作目录成功！");
		// 得到配置表
		List<Map<String, Object>> lt = XmlService.getAutodev();
		int isSuccess = 0;
		int count = 0;
		for (int i = 0; i < TableCloumnKV.size(); i++) {
			String Type = TableCloumnKV.get(AllKey.get(i)).toLowerCase();
			String MappingNm = "M_" + AllKey.get(i);
			String Refolder = "generaXml\\";
			try {
				switch (Type) {

				case "全删全插":
					Expression.main(new String[] { "0", AllKey.get(i) });

					org.tools.ConFileContent
							.writeLog(
									XmlService.getXMLStrng(org.tools.ConFileContent
											.ReplaceColumnNm(Refolder + "I_" + MappingNm + ".xml", Type), lt, Type),
									Type);
					log.info("生成XML： " + "I_" + MappingNm + ".xml");
					Init.main(new String[] { "0", AllKey.get(i) });
					org.tools.ConFileContent.writeLog(
							org.tools.ConFileContent.ReplaceColumnNm(Refolder + MappingNm + ".xml", Type), "init");
					log.info("生成XML： " + MappingNm + ".xml");
					Check.main(new String[] { "0", AllKey.get(i), "check" });
					org.tools.ConFileContent.writeLog(XmlService.getXMLStrng(
							org.tools.ConFileContent.ReplaceColumnNm(
									Refolder + "M_CHECK_" + Platfrom + "_" + AllKey.get(i) + "_CK.xml", "check"),lt,"check"),
							"check");
					log.info("生成XML： " + "M_CHECK_" + Platfrom + "_" + AllKey.get(i) + "_CK.xml");
					break;

				case "append":
					Append.main(new String[] { "0", AllKey.get(i) });

					org.tools.ConFileContent
							.writeLog(
									XmlService.getXMLStrng(org.tools.ConFileContent
											.ReplaceColumnNm(Refolder + "A_" + MappingNm + ".xml", Type), lt, Type),
									Type);
					log.info("生成XML： " + "A_" + MappingNm + ".xml");
					Init.main(new String[] { "0", AllKey.get(i) });
					org.tools.ConFileContent.writeLog(
							org.tools.ConFileContent.ReplaceColumnNm(Refolder + MappingNm + ".xml", "init"), "init");
					log.info("生成XML： " + MappingNm + ".xml");
					Check.main(new String[] { "0", AllKey.get(i), "check" });
					org.tools.ConFileContent.writeLog(XmlService.getXMLStrng(
							org.tools.ConFileContent.ReplaceColumnNm(
									Refolder + "M_CHECK_" + Platfrom + "_" + AllKey.get(i) + "_CK.xml", "check"),lt,"check"),
							"check");
					log.info("生成XML： " + "M_CHECK_" + Platfrom + "_" + AllKey.get(i) + "_CK.xml");
					break;

				case "upsert":
					Upsert.main(new String[] { "0", AllKey.get(i) });

					org.tools.ConFileContent
							.writeLog(
									XmlService.getXMLStrng(org.tools.ConFileContent
											.ReplaceColumnNm(Refolder + "U_" + MappingNm + ".xml", Type), lt, Type),
									Type);
					log.info("生成XML： " + "U_" + MappingNm + ".xml");
					Init.main(new String[] { "0", AllKey.get(i) });
					org.tools.ConFileContent.writeLog(
							org.tools.ConFileContent.ReplaceColumnNm(Refolder + MappingNm + ".xml", "全删全插"), "init");
					log.info("生成XML： " + MappingNm + ".xml");
					Check.main(new String[] { "0", AllKey.get(i), Type });
					org.tools.ConFileContent.writeLog(XmlService.getXMLStrng(
							org.tools.ConFileContent.ReplaceColumnNm(
									Refolder + "M_CHECK_" + Platfrom + "_" + AllKey.get(i) + "_CK.xml", "check"),lt,"check"),
							"check");
					log.info("生成XML： " + "M_CHECK_" + Platfrom + "_" + AllKey.get(i) + "_CK.xml");
					break;

				case "拉链表":
					log.info("1:" + AllKey.get(i));
					ZipperTable.main(new String[] { "0", AllKey.get(i) });
					log.info("生成XML： " + "H_" + MappingNm + ".xml");
					org.tools.ConFileContent
							.writeLog(
									XmlService.getXMLStrng(org.tools.ConFileContent
											.ReplaceColumnNm(Refolder + "H_" + MappingNm + ".xml", Type), lt, Type),
									Type);
					log.info("生成XML： " + "H_" + MappingNm + ".xml");
					ZipInit.main(new String[] { "0", AllKey.get(i) });

					org.tools.ConFileContent.writeLog(
							org.tools.ConFileContent.ReplaceColumnNm(Refolder + MappingNm + ".xml", "全删全插"), "init");
					log.info("生成XML： " + MappingNm + ".xml");
					ZipCheck.main(new String[] { "0", AllKey.get(i), Type });
					org.tools.ConFileContent.writeLog(XmlService.getXMLStrng(
							org.tools.ConFileContent.ReplaceColumnNm(
									Refolder + "M_CHECK_" + Platfrom + "_" + AllKey.get(i) + "_CK.xml", "zcheck"),
							lt, Type), "check");
					log.info("生成XML： " + "M_CHECK_" + Platfrom + "_" + AllKey.get(i) + "_CK.xml");
					break;

				}

			} catch (Exception e) {
				// log.error(e.printStackTrace());
				// ByteArrayOutputStream baos = new ByteArrayOutputStream();
				// e.printStackTrace(new PrintStream(baos));
				// String exception = baos.toString();
				// log.error( exception);

				isSuccess += 1;
			}

			count++;

		}
		if (isSuccess == 0) {
			log.info("XML生成成功，成功生成" + count + "个XML");
		} else {
			log.error("生成XML出现错误" + isSuccess + "个，请检查日志。。。。");
		}
	}

	public static void main(String[] args) {
		// TODO Auto-generated method stub

		GenMapping(GetTableList());

	}

}