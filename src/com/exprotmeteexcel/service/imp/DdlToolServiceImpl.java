package com.exprotmeteexcel.service.imp;

import java.io.File;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Properties;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import com.exprotmeteexcel.bean.MateColumnsBean;
import com.exprotmeteexcel.bean.PropertiesMap;
import com.exprotmeteexcel.main.ExportExcel;
import com.exprotmeteexcel.service.DdlToolService;
import com.exprotmeteexcel.service.ExprotMeteExcelService;
import com.exprotmeteexcel.utl.FileUtil;
import com.exprotmeteexcel.utl.Utl;

public class DdlToolServiceImpl implements DdlToolService {
	private static final Log log = LogFactory.getLog(ExportExcel.class);

	/**
	 * �õ����õ���ԭ���ݵ����͡��ֶΡ����ȵ�
	 * 
	 * @param ownTable
	 *            ����
	 * 
	 * @param cols
	 *            ���ֶ���Ϣ �����ݿ���䷢�Ͷ���
	 * @return String �����DDL
	 */
	public String getDdlStr(String ownTable, List<MateColumnsBean> cols) {
		StringBuffer sb = new StringBuffer();
		StringBuffer PRIMARY = new StringBuffer();

		String TableRemark = "";
		Boolean llt = false;
		ExprotMeteExcelService ex = new ExprotMeteExcelServiceImpl();
		Map<String, PropertiesMap> pm = ex.getPropertiesMapList("properties\\businessconfig");
		String system = cols.get(0).getSystem();
		String spath = system.substring(system.indexOf("(") + 1, system.indexOf(")"));
		System.out.println(spath);
		PropertiesMap p = ex.getPropertiesMap(spath);
		if (!Utl.isEmpty(p)) {
			sb.append("--------------------------CREATE TABLE " + ownTable + "-------------------------------------"
					+ "\n" + "");
			sb.append("DROP TABLE " + p.getDDLSchema() + "." + cols.get(0).getTranTableName() + ";" + "\n" + "");
			sb.append("CREATE MULTISET TABLE " + p.getDDLSchema() + "." + cols.get(0).getTranTableName() + " " + "\n"
					+ " (");
			for (int i = 0; i < cols.size(); i++) {
				if (!Utl.isEmpty(cols.get(i).getTableRemark())) {
					TableRemark = cols.get(i).getTableRemark();
				}
				String ColumnName = cols.get(i).getTranColumnName();
				String ColumnDataType = cols.get(i).getTranColumnDataType();
				String ColumnRemark = cols.get(i).getColumnRemark();
				String IsNull = cols.get(i).getIsNull();
				String colTitle = !Utl.isEmpty(ColumnRemark) ? "TITLE '" + ColumnRemark + "'" : "";
				String datetype = ColumnDataType.indexOf("(") > 0
						? ColumnDataType.toString().substring(0, ColumnDataType.toString().indexOf("("))
						: ColumnDataType;

				String UNICODE = isStringType(datetype.toUpperCase()) ? "CHARACTER SET UNICODE" : "";

				if (!Utl.isEmpty(cols.get(i).getPiValue())) {
					PRIMARY.append("PI".equals(cols.get(i).getPiValue().toString().toUpperCase())
							? cols.get(i).getTranColumnName().toString() + "," : "");
				}
				if (i == 0) {
					sb.append(ColumnName + " " + ColumnDataType + " " + UNICODE + " " + colTitle + " " + IsNull);
				} else {
					sb.append("," + "\n" + " " + ColumnName + " " + ColumnDataType + " " + UNICODE + " " + colTitle
							+ " " + IsNull);
				}

				if (!Utl.isEmpty(cols.get(i).getSynchronousLogic())) {
					if ("������".equals(cols.get(i).getSynchronousLogic().toString())) {
						llt = true;
					}
				}

			}
			if (llt) {
				sb.append("," + "\n" + " DW_START_DT   TITLE '��ʼ����' DATE FORMAT 'YYYY-MM-DD' NULL ");
				sb.append("," + "\n" + " DW_END_DT   TITLE '��������' DATE FORMAT 'YYYY-MM-DD' NULL ");
				sb.append("," + "\n" + " DW_ETL_DT   TITLE '��������' DATE FORMAT 'YYYY-MM-DD' NULL ");
				sb.append("," + "\n" + " DW_UPD_TM TIMESTAMP(0)  TITLE '����ʱ��'  DEFAULT CURRENT_TIMESTAMP(0) ");

			} else {
				sb.append("," + "\n" + " DW_OPER_FLAG SMALLINT  TITLE '������ʶ' DEFAULT 1 ");
				sb.append("," + "\n" + " DW_ETL_DT    TITLE '��������' DATE FORMAT 'YYYY-MM-DD' NULL ");
				sb.append("," + "\n" + " DW_UPD_TM TIMESTAMP(0) TITLE '����ʱ��' DEFAULT CURRENT_TIMESTAMP(0) ");

			}
			sb.append(")" + "\n" + " ");

			if (!Utl.isEmpty(PRIMARY.toString())) {

				sb.append("PRIMARY INDEX (" + "\n" + "" + PRIMARY.substring(0, PRIMARY.length() - 1) + "" + "\n" + ");"
						+ "\n" + "");
			} else {

				return "noPiValue";

			}

			if (!Utl.isEmpty(TableRemark)) {
				sb.append("COMMENT ON TABLE " + p.getDDLSchema() + "." + cols.get(0).getTranTableName() + " IS '"
						+ TableRemark + "';" + "\n" + "");
			}

			System.out.println(sb.toString());
			return sb.toString();
		} else {
			return null;
		}

	}

	/**
	 * �õ�CK�����õ���ԭ���ݵ����͡��ֶΡ����ȵ�
	 * 
	 * @param ownTable
	 *            ����
	 * 
	 * @param cols
	 *            ���ֶ���Ϣ �����ݿ���䷢�Ͷ���
	 * @return String �����DDL
	 */
	public String getCkDdlStr(String ownTable, List<MateColumnsBean> cols) {
		StringBuffer sb = new StringBuffer();
		String TableRemark = "";
		StringBuffer pri = new StringBuffer();
		ExprotMeteExcelService ex = new ExprotMeteExcelServiceImpl();
		Map<String, PropertiesMap> pm = ex.getPropertiesMapList("properties\\businessconfig");
		String system = cols.get(0).getSystem();
		String spath = system.substring(system.indexOf("(") + 1, system.indexOf(")"));
		// System.out.println(system.substring(0, system.indexOf("(")));
		PropertiesMap p = ex.getPropertiesMap(spath);
		if (!Utl.isEmpty(p)) {
			sb.append("--------------------------CREATE TABLE " + ownTable + "-------------------------------------"
					+ "\n" + "");
			sb.append("DROP TABLE " + p.getDATASchema() + "." + cols.get(0).getTranTableName() + "_CK ;" + "\n" + "");
			sb.append("CREATE MULTISET TABLE " + p.getDATASchema() + "." + cols.get(0).getTranTableName() + "_CK "
					+ "\n" + " (");
			for (int i = 0; i < cols.size(); i++) {
				if (!Utl.isEmpty(cols.get(i).getTableRemark())) {
					TableRemark = cols.get(i).getTableRemark();
				}

				String ColumnName = cols.get(i).getTranColumnName();
				String ColumnDataType = cols.get(i).getTranColumnDataType();
				String ColumnRemark = cols.get(i).getColumnRemark();
				// String IsNull = cols.get(i).getIsNull();
				String colTitle = !Utl.isEmpty(ColumnRemark) ? "TITLE '" + ColumnRemark + "'" : "";
				String datetype = ColumnDataType.indexOf("(") > 0
						? ColumnDataType.toString().substring(0, ColumnDataType.toString().indexOf("("))
						: ColumnDataType;
				String UNICODE = isStringType(datetype.toUpperCase()) ? "CHARACTER SET UNICODE" : "";

				if (i == 0) {
					sb.append(ColumnName + " " + ColumnDataType + " " + UNICODE + " " + colTitle + " ");
				} else {
					sb.append("," + "\n" + " " + ColumnName + " " + ColumnDataType + " " + UNICODE + " " + colTitle
							+ " ");
				}
				if ("pri".equals(cols.get(i).getPrimaryKey())) {
					pri.append("," + "\n" + " IN_" + ColumnName + " " + ColumnDataType + " " + UNICODE + " " +  " null ");
				}

			}

			// sb.append("," + "\n" + " IN_ID BIGINT NULL ");
			sb.append(pri.toString());
			sb.append("," + "\n" + " MD5ALL VARCHAR(50) CHARACTER SET UNICODE  NULL ");
			sb.append("," + "\n" + " IN_MD5ALL VARCHAR(50) CHARACTER SET UNICODE  NULL ");

			sb.append(");" + "\n" + " ");

			if (!Utl.isEmpty(TableRemark)) {
				sb.append("COMMENT ON TABLE " + p.getDDLSchema() + "." + cols.get(0).getTranTableName() + " IS '"
						+ TableRemark + "';" + "\n" + "");
			}

			System.out.println(sb.toString());
			return sb.toString();
		} else {
			return null;
		}

	}

	/**
	 * �õ�ods�����������ݵ���ԭ���ݵ����͡��ֶΡ����ȵ�
	 * 
	 * @param ownTable
	 *            ����
	 * 
	 * @param cols
	 *            ���ֶ���Ϣ �����ݿ���䷢�Ͷ���
	 * @return String ��ods���DDL
	 */
	public String getOdsDdlStr(String ownTable, List<MateColumnsBean> cols) {
		StringBuffer sb = new StringBuffer();
		ExprotMeteExcelService ex = new ExprotMeteExcelServiceImpl();
		Map<String, PropertiesMap> pm = ex.getPropertiesMapList("properties\\businessconfig");
		String system = cols.get(0).getSystem();
		String spath = system.substring(system.indexOf("(") + 1, system.indexOf(")"));
		// System.out.println(system.substring(0, system.indexOf("(")));
		PropertiesMap p = ex.getPropertiesMap(spath);
		if (!Utl.isEmpty(p)) {

			sb.append("drop table " + p.getDATASchema() + "." + cols.get(0).getTranTableName() + "	; " + "\n" + "");
			sb.append("create table " + p.getDATASchema() + "." + cols.get(0).getTranTableName() + "	 AS "
					+ p.getDDLSchema() + "." + cols.get(0).getTranTableName() + " with NO DATA; " + "\n" + "");

			return sb.toString();
		} else {
			return null;
		}

	}

	/**
	 * �õ����õ����ĸ����ԭ���ݱ�
	 * 
	 * @param path
	 *            ��·��
	 * @return list �������
	 */

	public Boolean exportDdl(String path) {
		Boolean bn = false;
		StringBuffer sb = new StringBuffer();
		StringBuffer cksb = new StringBuffer();
		StringBuffer odssb = new StringBuffer();
		ExprotMeteExcelService ex = new ExprotMeteExcelServiceImpl();
		DdlToolService ds = new DdlToolServiceImpl();
		Map<String, Object> mp = ex.ReadExcel(path);
		Map<String, Object> nopi = new HashMap<String, Object>();
		@SuppressWarnings({ "unchecked", "unused" })
		Map<String, List<MateColumnsBean>> tableDdl = (Map<String, List<MateColumnsBean>>) mp.get("TABLEDDL");

		for (String key : tableDdl.keySet()) {
			// ���û������PIֵ�Ͳ�����DDL
			if ("noPiValue".equals(ds.getDdlStr(key, tableDdl.get(key)).toString())) {
				nopi.put(key, tableDdl.get(key));
			} else {
				sb.append(ds.getDdlStr(key, tableDdl.get(key)));
				cksb.append(ds.getCkDdlStr(key, tableDdl.get(key)));
				odssb.append(ds.getOdsDdlStr(key, tableDdl.get(key)));
			}

		}
		File file = new File(path);
		String metapath = file.getName();
		// DDL��DQL
		String str = metapath.substring(0, metapath.lastIndexOf(".")) + "_DDL.sql";
		// DDL��SQL
		bn = FileUtil.WriteFile(sb.toString(), "DDL\\", str, "GBK");
		// ck��sql
		String ckstr = metapath.substring(0, metapath.lastIndexOf(".")) + "_CK.sql";
		bn = FileUtil.WriteFile(cksb.toString(), "DDL\\", ckstr, "GBK");
		// ods������������
		String odsstr = metapath.substring(0, metapath.lastIndexOf(".")) + "_ODS.sql";
		bn = FileUtil.WriteFile(odssb.toString(), "DDL\\", odsstr, "GBK");
		if (bn) {
			log.info("���гɹ���");
		} else {
			log.info("����ʧ�ܣ�");
		}
		if (!Utl.isEmpty(nopi)) {
			String tablename = "";
			int i = 1;
			for (String key : nopi.keySet()) {
				if (i == 1) {
					tablename = key;
				} else {
					tablename = tablename + "," + key;
				}
				i++;
			}

			log.info("piֵû�����õ�" + i + "����:" + tablename);
			System.out.println("piֵû�����õ�" + i + "����:" + tablename);
		}
		return bn;

	}

	/**
	 * �õ����õ����ĸ����ԭ���ݱ�
	 * 
	 * @param path
	 *            ��·��
	 * @return list �������
	 */

	public Boolean isStringType(String datetype) {
		Properties p = Utl.getProperties("properties\\Pub.properties");
		String[] str = p.getProperty("StringType").split(",");
		List<String> lt = Arrays.asList(str);
		if (lt.contains(datetype)) {
			return true;
		} else {
			return false;
		}
	}

}
