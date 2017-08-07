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
 * @Copyright 2017�Ϻ��¾����缼�����޹�˾ @version��1.0.1
 * @author��Junko
 * @date��2017��7��10������12:16:30
 * @Description������ExcelԪ������Ϣ���ɶ�Ӧ�߼���XML =============================================
 */

public class FactoryMapping implements Parameter {

	protected static ArrayList<ArrayList<String>> TableConf = ExcelUtil
			.readExecl(GetProperties.getKeyValue("ExcelPath"));
	protected static Log log = LogFactory.getLog(FactoryMapping.class);
	/**
	 * ���TD���ñ�Ĺ�������
	 */
	// protected final static HashMap<String, String> FilterCondition =
	// getFilterCondition();

	/**
	 * @version: 1.0.1
	 * @author: Junko
	 * @date: 2017��7��13������3:17:30
	 * @Description: ��TD���ñ�ȡ��������Ĺ�������
	 * @return TD���ñ��Ÿ�����Ĺ�������
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
	 * @date: 2017��7��10������2:42:32
	 * @Description: ��Excel�����ļ�ȡ��������������Ӧ��kv��
	 * @return �Ѿ�HashMap����ʽ����Excel����Ϣ
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
	 * @date: 2017��7��10������2:43:20
	 * @Description: ����Excel���õ���Ϣ���ɸ�������߼�XML�Ĺ���������ɾ���ϴ�����XML�ļ�
	 * @param TableCloumnKV
	 */
	public static void GenMapping(HashMap<String, String> TableCloumnKV) {

		ArrayList<String> AllKey = new ArrayList<String>();
		AllKey.addAll(TableCloumnKV.keySet());

		// �������XMLĿ¼�µ��ļ�
		org.tools.DelXmlFolder.delAllFile("generaXml\\");
		org.tools.DelXmlFolder.delAllFile("xml\\CheckXml\\");
		org.tools.DelXmlFolder.delAllFile("xml\\InsertXml\\");
		org.tools.DelXmlFolder.delAllFile("xml\\UpsertXml\\");
		org.tools.DelXmlFolder.delAllFile("xml\\ZipXml\\");
		org.tools.DelXmlFolder.delAllFile("xml\\Append\\");
		org.tools.DelXmlFolder.delAllFile("xml\\InitXml\\");
		log.info("��չ���Ŀ¼�ɹ���");
		// �õ����ñ�
		List<Map<String, Object>> lt = XmlService.getAutodev();
		int isSuccess = 0;
		int count = 0;
		for (int i = 0; i < TableCloumnKV.size(); i++) {
			String Type = TableCloumnKV.get(AllKey.get(i)).toLowerCase();
			String MappingNm = "M_" + AllKey.get(i);
			String Refolder = "generaXml\\";
			try {
				switch (Type) {

				case "ȫɾȫ��":
					Expression.main(new String[] { "0", AllKey.get(i) });

					org.tools.ConFileContent
							.writeLog(
									XmlService.getXMLStrng(org.tools.ConFileContent
											.ReplaceColumnNm(Refolder + "I_" + MappingNm + ".xml", Type), lt, Type),
									Type);
					log.info("����XML�� " + "I_" + MappingNm + ".xml");
					Init.main(new String[] { "0", AllKey.get(i) });
					org.tools.ConFileContent.writeLog(
							org.tools.ConFileContent.ReplaceColumnNm(Refolder + MappingNm + ".xml", Type), "init");
					log.info("����XML�� " + MappingNm + ".xml");
					Check.main(new String[] { "0", AllKey.get(i), "check" });
					org.tools.ConFileContent.writeLog(XmlService.getXMLStrng(
							org.tools.ConFileContent.ReplaceColumnNm(
									Refolder + "M_CHECK_" + Platfrom + "_" + AllKey.get(i) + "_CK.xml", "check"),lt,"check"),
							"check");
					log.info("����XML�� " + "M_CHECK_" + Platfrom + "_" + AllKey.get(i) + "_CK.xml");
					break;

				case "append":
					Append.main(new String[] { "0", AllKey.get(i) });

					org.tools.ConFileContent
							.writeLog(
									XmlService.getXMLStrng(org.tools.ConFileContent
											.ReplaceColumnNm(Refolder + "A_" + MappingNm + ".xml", Type), lt, Type),
									Type);
					log.info("����XML�� " + "A_" + MappingNm + ".xml");
					Init.main(new String[] { "0", AllKey.get(i) });
					org.tools.ConFileContent.writeLog(
							org.tools.ConFileContent.ReplaceColumnNm(Refolder + MappingNm + ".xml", "init"), "init");
					log.info("����XML�� " + MappingNm + ".xml");
					Check.main(new String[] { "0", AllKey.get(i), "check" });
					org.tools.ConFileContent.writeLog(XmlService.getXMLStrng(
							org.tools.ConFileContent.ReplaceColumnNm(
									Refolder + "M_CHECK_" + Platfrom + "_" + AllKey.get(i) + "_CK.xml", "check"),lt,"check"),
							"check");
					log.info("����XML�� " + "M_CHECK_" + Platfrom + "_" + AllKey.get(i) + "_CK.xml");
					break;

				case "upsert":
					Upsert.main(new String[] { "0", AllKey.get(i) });

					org.tools.ConFileContent
							.writeLog(
									XmlService.getXMLStrng(org.tools.ConFileContent
											.ReplaceColumnNm(Refolder + "U_" + MappingNm + ".xml", Type), lt, Type),
									Type);
					log.info("����XML�� " + "U_" + MappingNm + ".xml");
					Init.main(new String[] { "0", AllKey.get(i) });
					org.tools.ConFileContent.writeLog(
							org.tools.ConFileContent.ReplaceColumnNm(Refolder + MappingNm + ".xml", "ȫɾȫ��"), "init");
					log.info("����XML�� " + MappingNm + ".xml");
					Check.main(new String[] { "0", AllKey.get(i), Type });
					org.tools.ConFileContent.writeLog(XmlService.getXMLStrng(
							org.tools.ConFileContent.ReplaceColumnNm(
									Refolder + "M_CHECK_" + Platfrom + "_" + AllKey.get(i) + "_CK.xml", "check"),lt,"check"),
							"check");
					log.info("����XML�� " + "M_CHECK_" + Platfrom + "_" + AllKey.get(i) + "_CK.xml");
					break;

				case "������":
					log.info("1:" + AllKey.get(i));
					ZipperTable.main(new String[] { "0", AllKey.get(i) });
					log.info("����XML�� " + "H_" + MappingNm + ".xml");
					org.tools.ConFileContent
							.writeLog(
									XmlService.getXMLStrng(org.tools.ConFileContent
											.ReplaceColumnNm(Refolder + "H_" + MappingNm + ".xml", Type), lt, Type),
									Type);
					log.info("����XML�� " + "H_" + MappingNm + ".xml");
					ZipInit.main(new String[] { "0", AllKey.get(i) });

					org.tools.ConFileContent.writeLog(
							org.tools.ConFileContent.ReplaceColumnNm(Refolder + MappingNm + ".xml", "ȫɾȫ��"), "init");
					log.info("����XML�� " + MappingNm + ".xml");
					ZipCheck.main(new String[] { "0", AllKey.get(i), Type });
					org.tools.ConFileContent.writeLog(XmlService.getXMLStrng(
							org.tools.ConFileContent.ReplaceColumnNm(
									Refolder + "M_CHECK_" + Platfrom + "_" + AllKey.get(i) + "_CK.xml", "zcheck"),
							lt, Type), "check");
					log.info("����XML�� " + "M_CHECK_" + Platfrom + "_" + AllKey.get(i) + "_CK.xml");
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
			log.info("XML���ɳɹ����ɹ�����" + count + "��XML");
		} else {
			log.error("����XML���ִ���" + isSuccess + "����������־��������");
		}
	}

	public static void main(String[] args) {
		// TODO Auto-generated method stub

		GenMapping(GetTableList());

	}

}