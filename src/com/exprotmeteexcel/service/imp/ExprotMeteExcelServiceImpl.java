package com.exprotmeteexcel.service.imp;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Properties;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.apache.poi.hssf.usermodel.HSSFDataValidation;

import com.exprotmeteexcel.bean.MateBean;
import com.exprotmeteexcel.bean.MateColumnsBean;
import com.exprotmeteexcel.bean.PropertiesMap;
import com.exprotmeteexcel.dao.impl.BaseDbDaoI;
import com.exprotmeteexcel.service.ExprotMeteExcelService;
import com.exprotmeteexcel.utl.DateTran.DataTypeTrans;
import com.exprotmeteexcel.utl.ExcelUtility;
import com.exprotmeteexcel.utl.FileUtil;
import com.exprotmeteexcel.utl.Getjdbcconfig;
import com.exprotmeteexcel.utl.PmrepExeUtl;
import com.exprotmeteexcel.utl.StringUtil;
import com.exprotmeteexcel.utl.Utl;
import com.exprotmeteexcel.utl.global.BiaotiBean;
import com.exprotmeteexcel.utl.global.SQLGlobal;
import com.informatica.powercenter.sdk.mapfwk.exception.ConnectionFailedException;
import com.informatica.powercenter.sdk.mapfwk.exception.RepoConnectionObjectOperationException;
import com.informatica.powercenter.sdk.mapfwk.repository.RepoConnectionInfo;

/**
 * ԭ���ݵ���Service��
 * 
 * @author wujunqing
 *
 */
public class ExprotMeteExcelServiceImpl implements ExprotMeteExcelService {

	private static final Log log = LogFactory.getLog(ExprotMeteExcelServiceImpl.class);

	/**
	 * �õ����õ���ԭ���ݵ����͡��ֶΡ����ȵ�
	 * 
	 * @param path
	 *            ��·��
	 * 
	 * @param mb
	 *            ��Ҫ����ԭ���� �����ݿ���䷢�Ͷ���
	 * @return List �������
	 */
	@SuppressWarnings("null")
	@Override
	public List<MateColumnsBean> getTableColumn(String path, MateBean mb) {
		// TODO Auto-generated method stub
		Getjdbcconfig dbcof = new Getjdbcconfig(path);
		Properties yp = Utl.getProperties(path);
		// properties\Pub.properties
		Properties p = Utl.getProperties("properties/Pub.properties");
		BaseDbDaoI db = FactoryBaseDbDaoServiceImp.getBaseDbDaoI(path);
		if (!Utl.isEmpty(db)) {
			List<MateColumnsBean> listmc = new ArrayList<MateColumnsBean>();
			List<Map<String, Object>> su = new ArrayList<Map<String, Object>>();
			su = db.getTableColumnByMeta(mb);
			List<Object[]> lt = ExcelUtility.getReadExcelContent("xls\\config\\SOURCE2TD.xlsx", 1);
			/*
			 * String dbinfo; String owner; String tableName; String
			 * tranTableName; String tableRemark; String columnsName; String
			 * tranColumnName; String columnRemark; String columnDataType;
			 * String tranColumnDataType; String isNull; String defaultValue;
			 */
			/*
			 * String primaryKey; String piValue; String remark; String pl;
			 * String synchronousType; String dateCount; String dateSize; String
			 * updateTime; String synchronousLogic; String valid; String
			 * isGigDateCol;
			 */
			// log.info("����������" + su.size());
			if (su != null) {
				for (int i = 0; i < su.size(); i++) {
					MateColumnsBean col = new MateColumnsBean();
					// ԭ��������Ϣ
					col.setDbinfo(dbcof.getDbtype() + ":(" + dbcof.getIp() + ":" + dbcof.getPort() + ";sid:"
							+ dbcof.getDatabasename() + ")");
					// ���������
					col.setOwner(su.get(i).get("OWNER").toString());
					// ����
					col.setTableName(su.get(i).get("TABLE_NAME").toString());

					// �ֶ�����
					col.setColumnsName(su.get(i).get("COLUMN_NAME").toString());
					// ת������
					// ������ǰ׺O_XXX��������ƽ̨��ƣ�������������߼����׺��_H���롰ת����������
					String str1 = Utl.isEmpty(yp.getProperty("prefix")) ? "" : yp.getProperty("prefix");
					col.setTranTableName(str1 + su.get(i).get("TABLE_NAME").toString().toUpperCase());
					//
					col.setTableRemark(su.get(i).get("TABLE_REMARKS") == null
							|| "".equals(su.get(i).get("TABLE_REMARKS").toString()) ? ""
									: su.get(i).get("TABLE_REMARKS").toString());

					// ת���ֶ�
					// �ֶ��������ؼ��ֵļ�_OG���롰ת���ֶ�������

					List<String> trnsCloumn = new ArrayList<String>();
					Collections.addAll(trnsCloumn, p.get("OGCloumn").toString().toUpperCase().split(","));
					String trnsCloumnName = trnsCloumn.contains(su.get(i).get("COLUMN_NAME").toString().toUpperCase())
							? su.get(i).get("COLUMN_NAME").toString() + "_OG"
							: Utl.isChinese(su.get(i).get("COLUMN_NAME").toString())
									? "\"" + su.get(i).get("COLUMN_NAME").toString() + "\""
									: su.get(i).get("COLUMN_NAME").toString();
					col.setTranColumnName(trnsCloumnName.toUpperCase());

					col.setColumnRemark(su.get(i).get("REMARKS") == null || "".equals(su.get(i).get("REMARKS")) ? null
							: su.get(i).get("REMARKS").toString());
					// �Ƿ���ֶ�

					List<String> BigCloumn = new ArrayList<String>();
					Collections.addAll(BigCloumn, p.get("BigCloumn").toString().split(","));
					String typename = Utl.isEmpty(su.get(i).get("TYPE_NAME").toString()) ? ""
							: su.get(i).get("TYPE_NAME").toString().indexOf("(") > 0
									? su.get(i).get("TYPE_NAME").toString().substring(0,
											su.get(i).get("TYPE_NAME").toString().indexOf("("))
									: su.get(i).get("TYPE_NAME").toString();

					String isBigCol = BigCloumn.contains(typename.toUpperCase()) ? "��" : "��";
					// �ֶγ���

					String datasize = "(" + su.get(i).get("COLUMN_SIZE").toString() + ")";
					String transdatasize = "��".equals(isBigCol) ? "(1234)"
							: "(" + su.get(i).get("COLUMN_SIZE").toString() + ")";

					// �ֶ�����ת��
					String datatype = typename;
					String trandatatype = DataTypeTrans.TransByTd(datatype.toUpperCase(), db.getDb_type(), lt);
					col.setColumnDataType(datatype + datasize);
					String TranColumnDataType = "integer".equals(trandatatype) ? "int"
							: "bigint".equals(trandatatype) ? "bigint"
									: "timestamp".equals(trandatatype) ? "timestamp(6)"
											: "smallint".equals(trandatatype) ? "smallint"
													: (trandatatype + transdatasize);

					col.setTranColumnDataType(TranColumnDataType);

					// �ֶ��ܷ�Ϊ��: NOT NULL:��Ϊ�� ; NULL:��Ϊnull
					col.setIsNull("NO".equals(su.get(i).get("ISNULL").toString()) ? "NOT NULL" : "NULL");

					col.setDefaultValue(
							su.get(i).get("COLUMN_DEF") == null || "".equals(su.get(i).get("COLUMN_DEF").toString())
									? "" : su.get(i).get("COLUMN_DEF").toString());
					col.setPrimaryKey(
							su.get(i).get("PRIMARYKEY") == null || "".equals(su.get(i).get("PRIMARYKEY").toString())
									? null : su.get(i).get("PRIMARYKEY").toString());
					col.setPiValue("");
					col.setRemark("");
					col.setPl("");
					col.setSynchronousType("");
					col.setDateCount("");
					col.setDateSize("");
					col.setUpdateTime(null);
					col.setSynchronousLogic("");
					col.setValid("");

					col.setIsGigDateCol(isBigCol);
					col.setSystem(yp.getProperty("System") + "(" + path + ")");
					listmc.add(col);

				}
			}
			return listmc;
		} else {
			log.error("���ݿ�����Ϊ��");
			return null;
		}
	}

	/**
	 * ������ԭ���ݱ�
	 * 
	 * @param path
	 *            ��·��
	 * @return Boolean ���ɹ� ʧ��
	 */

	@Override
	public Boolean ExprotExcel(List<MateColumnsBean> meta, String outputprth) {
		// TODO Auto-generated method stub
		Boolean bl = false;
		OutputStream out = null;
		ExcelUtility<MateColumnsBean> ex = new ExcelUtility<MateColumnsBean>();
		File file = null;
		file = new File(outputprth);
		File parentDir = file.getParentFile();
		System.out.print(parentDir);
		if (!parentDir.isDirectory()) {
			file.mkdir();
		}
		List<HSSFDataValidation> hsl = new ArrayList<HSSFDataValidation>();
		HSSFDataValidation hs = ExcelUtility.createHSSFDataValidation(1, 65535, 13, 13, BiaotiBean.pi);
		hsl.add(hs);
		HSSFDataValidation hs2 = ExcelUtility.createHSSFDataValidation(1, 65535, 20, 20, BiaotiBean.lj);
		hsl.add(hs2);
		try {
			out = new FileOutputStream(outputprth);
			ex.exportExcel(BiaotiBean.HEADER, meta, out, "yyyy-MM-dd HH:mm:ss", hsl);
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			return bl;
		} finally {
			if (out != null) {
				try {
					out.close();
				} catch (IOException e) {
				}
			}
		}

		bl = true;
		return bl;
	}

	/**
	 * ȡ�����ñ�AUTO_ETL_CONFIG��Ҫ����ԭ����
	 * 
	 * @param path
	 *            ��Ӧ��ҵ���ϵ����·��(teradatajdbcpath)
	 * @param path
	 * 
	 * @throws IOException
	 */
	@SuppressWarnings("unchecked")
	@Override
	public MateBean getTdMate(String path, String plaform) {
		// TODO Auto-generated method stub
		MateBean mb = new MateBean();
		List<Map<String, Object>> lt = new ArrayList<Map<String, Object>>();
		BaseDbDaoI db = FactoryBaseDbDaoServiceImp.getBaseDbDaoI(path);
		String sql = "select OWNER_NM as OWNER,TABLE_NM as TABLE_NAME,FLAG as FLAG,PLAFORM as PLAFORM from  AUTO_ETL_CONFIG where FLAG =1 and PLAFORM=?";
		lt = db.getDateForMap(sql, plaform);
		mb.setDbSid(db.getDb_ssid());
		mb.setDbconfpath(path);
		mb.setDbtype(db.getDb_type());
		mb.setMatedate(lt);
		System.out.println(lt);
		return mb;

	}

	public List getAutodev(String path, String plaform) {
		// TODO Auto-generated method stub
		List<Map<String, Object>> lt = new ArrayList<Map<String, Object>>();
		BaseDbDaoI db = FactoryBaseDbDaoServiceImp.getBaseDbDaoI(path);
		String sql = "select * from  AUTO_ETL_CONFIG where  PLAFORM=? and (SOURCE_FILTER is not null or TARGET_FILTER is not null)";
		lt = db.getDateForMap(sql, plaform);

		return lt;

	}

	public List<Map<String, Object>> getConfigTableFilter(String path, String plaform) {
		// TODO Auto-generated method stub
		List<Map<String, Object>> lt = new ArrayList<Map<String, Object>>();
		BaseDbDaoI db = FactoryBaseDbDaoServiceImp.getBaseDbDaoI(path);
		String sql = "select * from  AUTO_ETL_CONFIG where FLAG =1 and PLAFORM=?";
		lt = db.getDateForMap(sql, plaform);

		return lt;

	}

	public String getSql(String Type) {
		String sql = "";
		if ("oracle".equals(Type)) {
			sql = SQLGlobal.GET_COLUMN_ORACLESQL;
		} else if ("mssql".equals(Type)) {
			sql = SQLGlobal.GET_COLUMN_SQLSERVERSQL;
		} else if ("teradate".equals(Type)) {
			sql = SQLGlobal.GET_COLUMN_TERADATASQL;
		} else if ("mysql".equals(Type)) {
			sql = SQLGlobal.GET_COLUMN_MYSQLSQL;
		}

		return sql;

	}

	/**
	 * �õ����õ����ĸ����ԭ���ݱ�
	 * 
	 * @param path
	 *            ��·��
	 * @return list �������
	 */
	@Override
	public List<Map<String, Object>> getTableColumn(String path) {

		BaseDbDaoI db = FactoryBaseDbDaoServiceImp.getBaseDbDaoI(path);
		return db.getTableColumn();
	}

	/**
	 * �õ����õ�����ԭ���ݱ�
	 * 
	 * @param path
	 *            ��·��
	 * @param status
	 *            �������ɹ���ʧ��
	 * @return update ��update���ñ�״̬
	 */
	@Override
	public Boolean updateTdMate(String path, String status, String businessName) {
		BaseDbDaoI db = FactoryBaseDbDaoServiceImp.getBaseDbDaoI(path);
		String sql = "";
		if ("running".equals(status)) {
			sql = "update AUTO_ETL_CONFIG  set FLAG =0 where PLAFORM =? ";
		} else if ("ok".equals(status)) {
			sql = "update AUTO_ETL_CONFIG  set FLAG =2 where PLAFORM =? and FLAG =0";
		} else {
			sql = "update AUTO_ETL_CONFIG  set FLAG =-1 where PLAFORM =? and FLAG =0";
		}
		String[] param = { businessName };

		return db.execute(sql, param);
	}

	/*
	 * 
	 * @Override public Boolean ReadExcelExprot(String path) { Boolean bl =
	 * false; InputStream is = null; File fl = new File(path); String fileName =
	 * fl.getName(); try { is = new FileInputStream(path); } catch
	 * (FileNotFoundException e) { e.printStackTrace(); } Map<String, Object> mp
	 * = ExcelUtility.readExcel(is, 1);
	 * 
	 * @SuppressWarnings("unchecked") List<Object[]> ls = (List<Object[]>)
	 * mp.get("LISTDATE");
	 * 
	 * @SuppressWarnings("unchecked") Map<String, String> tablemap =
	 * (Map<String, String>) mp.get("MAPDATE"); List<MateColumnsBean> listmc =
	 * new ArrayList<MateColumnsBean>(); Map<String, List<MateColumnsBean>>
	 * tableDdl = new HashMap<String, List<MateColumnsBean>>();
	 * 
	 * if (!Utl.isEmpty(ls)) {
	 * 
	 * for (Object[] obj : ls) {
	 * 
	 * MateColumnsBean col = new MateColumnsBean(); // ԭ��������Ϣ dbinfo
	 * col.setDbinfo(obj[0].toString()); // ��������� owner
	 * col.setOwner(obj[2].toString()); // ���� tableName
	 * col.setTableName(obj[2].toString()); // ת������ tranTableName //
	 * ������ǰ׺O_XXX��������ƽ̨��ƣ�������������߼����׺��_H���롰ת���������� String str =
	 * tablemap.get(obj[2].toString()); String trantablename =
	 * obj[3].toString(); if (!Utl.isEmpty(str)) { trantablename = trantablename
	 * + "_H"; } col.setTranTableName(trantablename); // tableRemark
	 * col.setTableRemark(obj[4].toString()); // �ֶ����� columnsName
	 * col.setColumnsName(obj[5].toString());
	 * 
	 * String isNull; String defaultValue;
	 * 
	 * // ת���ֶ� tranColumnName // �ֶ��������ؼ��ֵļ�_OG���롰ת���ֶ�������
	 * col.setTranColumnName(obj[6].toString()); // columnRemark
	 * col.setColumnRemark(obj[7].toString());
	 * 
	 * // �ֶ����� ColumnDataType col.setColumnDataType(obj[8].toString()); //
	 * �ֶ�����ת�� tranColumnDataType col.setTranColumnDataType(obj[9].toString());
	 * 
	 * // �ֶ��ܷ�Ϊ��: isNull col.setIsNull(obj[10].toString()); // Ĭ��ֵ defaultValue
	 * col.setDefaultValue(obj[11].toString());
	 * 
	 * // ������� primaryKey col.setPrimaryKey(obj[12].toString()); // piValue
	 * col.setPiValue(obj[13] == null || "".equals(obj[13].toString()) ? null :
	 * obj[13].toString()); // remark col.setRemark(obj[14] == null ||
	 * "".equals(obj[14].toString()) ? null : obj[14].toString()); // pl
	 * col.setPl(obj[15] == null || "".equals(obj[15].toString()) ? null :
	 * obj[15].toString()); // synchronousType col.setSynchronousType(obj[16] ==
	 * null || "".equals(obj[16].toString()) ? null : obj[16].toString()); //
	 * dateCount col.setDateCount(obj[17] == null ||
	 * "".equals(obj[17].toString()) ? null : obj[17].toString()); // dateSize
	 * col.setDateSize(obj[18] == null || "".equals(obj[18]) ? null :
	 * obj[18].toString()); // updateTime col.setUpdateTime(obj[19] == null ||
	 * "".equals(obj[19]) ? null : obj[19].toString()); // synchronousLogic
	 * col.setSynchronousLogic(obj[20] == null || "".equals(obj[20].toString())
	 * ? null : obj[20].toString()); // valid col.setValid(obj[21] == null ||
	 * "".equals(obj[21].toString()) ? null : obj[21].toString()); //
	 * isGigDateCol col.setIsGigDateCol(obj[22] == null ||
	 * "".equals(obj[22].toString()) ? null : obj[22].toString());
	 * listmc.add(col); List<MateColumnsBean> collist =
	 * tableDdl.get(obj[2].toString() + "." + obj[2].toString()); if
	 * (Utl.isEmpty(collist)) { List<MateColumnsBean> colist = new
	 * ArrayList<MateColumnsBean>(); colist.add(col);
	 * tableDdl.put(obj[2].toString() + "." + obj[2].toString(), colist); } else
	 * { collist.add(col); tableDdl.put(obj[2].toString() + "." +
	 * obj[2].toString(), collist);
	 * 
	 * }
	 * 
	 * }
	 * 
	 * Properties p = Utl.getProperties("properties\\EXPORTMETA.properties");
	 * Properties yp =
	 * Utl.getProperties(p.getProperty("businesspropertiespath")); String
	 * businessName = yp.getProperty("SourceFolder"); SimpleDateFormat formatter
	 * = new SimpleDateFormat("yyyyMMddHHmmssSSS"); String formatStr =
	 * formatter.format(new Date());
	 * 
	 * String outpath = "xls\\out\\piout\\OUT2_" + businessName + "_" +
	 * formatStr + ".xls"; bl = ExprotExcel(listmc, outpath); }
	 * 
	 * return bl; }
	 */

	/**
	 * ��EXCEL����tableDDl��ת��TRANLIST
	 * 
	 * @param path
	 *            ��·��
	 * @return map ��tableDDl��TRANLIST�ļ���
	 */

	@Override
	public Map<String, Object> ReadExcel(String path) {

		Boolean bl = false;
		InputStream is = null;
		File fl = new File(path);
		String fileName = fl.getName();
		Map<String, Object> readmap = new HashMap<String, Object>();
		try {
			is = new FileInputStream(path);
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		}
		Map<String, Object> mp = ExcelUtility.readExcel(is, 1);
		@SuppressWarnings("unchecked")
		// ���ֶ���Ϣ
		List<Object[]> ls = (List<Object[]>) mp.get("LISTDATE");
		@SuppressWarnings("unchecked")
		// ������
		Map<String, String> tablemap = (Map<String, String>) mp.get("MAPDATE");
		List<MateColumnsBean> listmc = new ArrayList<MateColumnsBean>();
		Map<String, List<MateColumnsBean>> tableDdl = new HashMap<String, List<MateColumnsBean>>();

		if (!Utl.isEmpty(ls)) {

			for (Object[] obj : ls) {

				MateColumnsBean col = new MateColumnsBean();
				// ԭ��������Ϣ dbinfo
				col.setDbinfo(obj[0].toString());
				// ��������� owner
				col.setOwner(obj[1].toString());
				// ���� tableName
				col.setTableName(obj[2].toString());
				// ת������ tranTableName
				// ������ǰ׺O_XXX��������ƽ̨��ƣ�������������߼����׺��_H���롰ת����������
				String str = tablemap.get(obj[2].toString());
				String trantablename = obj[3].toString();
				if (!Utl.isEmpty(str)) {
					if ("������".equals(str)) {
						trantablename = trantablename + "_H";
					}
				}
				col.setTranTableName(trantablename);
				// tableRemark
				col.setTableRemark(obj[4].toString());
				// �ֶ����� columnsName
				col.setColumnsName(obj[5].toString());

				// ת���ֶ� tranColumnName
				// �ֶ��������ؼ��ֵļ�_OG���롰ת���ֶ�������
				col.setTranColumnName(obj[6].toString());
				// columnRemark
				col.setColumnRemark(obj[7].toString());

				// �ֶ����� ColumnDataType
				col.setColumnDataType(obj[8].toString());
				// �ֶ�����ת�� tranColumnDataType
				col.setTranColumnDataType(obj[9].toString());

				// �ֶ��ܷ�Ϊ��: isNull
				col.setIsNull(obj[10].toString());
				// Ĭ��ֵ defaultValue
				col.setDefaultValue(obj[11].toString());

				// ������� primaryKey
				col.setPrimaryKey(Utl.isEmpty(obj[12]) ? "" : obj[12].toString());
				// piValue
				col.setPiValue(obj[13] == null || "".equals(obj[13].toString()) ? null : obj[13].toString());
				// remark
				col.setRemark(obj[14] == null || "".equals(obj[14].toString()) ? null : obj[14].toString());
				// pl
				col.setPl(obj[15] == null || "".equals(obj[15].toString()) ? null : obj[15].toString());
				// synchronousType
				col.setSynchronousType(obj[16] == null || "".equals(obj[16].toString()) ? null : obj[16].toString());
				// dateCount
				col.setDateCount(obj[17] == null || "".equals(obj[17].toString()) ? null : obj[17].toString());
				// dateSize
				col.setDateSize(obj[18] == null || "".equals(obj[18]) ? null : obj[18].toString());
				// updateTime
				col.setUpdateTime(obj[19] == null || "".equals(obj[19]) ? null : obj[19].toString());
				// synchronousLogic
				col.setSynchronousLogic(obj[20] == null || "".equals(obj[20].toString()) ? null : obj[20].toString());
				// valid
				col.setValid(obj[21] == null || "".equals(obj[21].toString()) ? null : obj[21].toString());
				// isGigDateCol
				col.setIsGigDateCol(obj[22] == null || "".equals(obj[22].toString()) ? null : obj[22].toString());
				col.setSystem(Utl.isEmpty(obj[23]) ? "null" : obj[23].toString());
				listmc.add(col);
				List<MateColumnsBean> collist = tableDdl.get(obj[1].toString() + "." + obj[2].toString());
				MateColumnsBean col2 = null;
				col2 = (MateColumnsBean) col.clone();
				if (Utl.isEmpty(collist)) {

					List<MateColumnsBean> colist = new ArrayList<MateColumnsBean>();
					col2.setTranTableName(obj[3].toString());
					colist.add(col2);
					tableDdl.put(obj[1].toString() + "." + obj[2].toString(), colist);
				} else {
					col2.setTranTableName(obj[3].toString());
					collist.add(col2);
					tableDdl.put(obj[1].toString() + "." + obj[2].toString(), collist);

				}

			}

		}
		readmap.put("TRANLIST", listmc);
		readmap.put("TABLEDDL", tableDdl);
		return readmap;
	}

	/**
	 * �õ����õ�����ԭ���ݱ�
	 * 
	 * @param path
	 *            ��·��
	 * @param listmc:ת�����б�
	 * @return Boolean ��Boolean
	 */
	public Boolean ExcelExprot(String name, List<MateColumnsBean> listmc) {

		Boolean bl = false;
		String outpath = "xls\\out\\piout\\OUT2_" + name + ".xls";
		return ExprotExcel(listmc, outpath);

	}

	/**
	 * �õ����õ����ĸ����ԭ���ݱ�����excal
	 * 
	 * @param path
	 *            ��·��
	 * @return Boolean ���Ƿ�ִ�гɹ�
	 */

	public Boolean ExprotTableMateBean(String path) {
		Boolean bn = false;
		MateBean tt = new MateBean();
		Properties p = Utl.getProperties(path);
		// Properties py =
		// Utl.getProperties(p.getProperty("businesspropertiespath"));
		String businessName = p.getProperty("System");
		// 1���õ�ҵ�����ñ�
		ExprotMeteExcelService ex = new ExprotMeteExcelServiceImpl();
		tt = ex.getTdMate(p.getProperty("teradatajdbcpath"), businessName);
		if (!Utl.isEmpty(tt.getMatedate())) {
			// ������Ӧ�ı�
			ex.updateTdMate(p.getProperty("teradatajdbcpath"), "running", businessName);
			// �õ�ÿ�����ñ�Ķ�ԭ������Ϣ
			List<MateColumnsBean> lb = ex.getTableColumn(path, tt);
			SimpleDateFormat formatter = new SimpleDateFormat("yyyyMMddHHmmssSSS");
			String formatStr = formatter.format(new Date());
			String outpath = "xls\\out\\metaout\\OUT_" + businessName + "_" + formatStr + ".xls";
			bn = ex.ExprotExcel(lb, outpath);
			// ��ɺ���±�
			ex.updateTdMate(p.getProperty("teradatajdbcpath"), bn ? "ok" : "", businessName);
		}

		if (bn) {
			log.info("���гɹ���");
		} else {
			log.info("����ʧ�ܣ�");
		}
		return bn;

	}

	/**
	 * �õ�ÿһ��ƽ̨�����ļ�·��
	 * 
	 * @param �����ļ�����
	 * 
	 */
	public Map<String, PropertiesMap> getPropertiesMapList(String path) {
		Map<String, PropertiesMap> lp = new HashMap<String, PropertiesMap>();
		List<String> ls = FileUtil.getFile("properties\\businessconfig");
		for (String str : ls) {
			PropertiesMap pm = new PropertiesMap();
			pm.setPath(str);
			pm.setSystem(Utl.getProperties(str).getProperty("System"));
			pm.setDDLSchema(Utl.getProperties(str).getProperty("DDLSchema"));
			pm.setDATASchema(Utl.getProperties(str).getProperty("DATASchema"));
			lp.put(Utl.getProperties(str).getProperty("System"), pm);
		}

		return lp;

	}

	/**
	 * �õ�ÿһ��ƽ̨�����ļ�·��
	 * 
	 * @param �����ļ�
	 * 
	 */
	public PropertiesMap getPropertiesMap(String path) {

		Properties p = Utl.getProperties(path);
		PropertiesMap pm = new PropertiesMap();
		pm.setPath(path);
		pm.setSystem(p.getProperty("System"));
		pm.setDDLSchema(p.getProperty("DDLSchema"));
		pm.setDATASchema(p.getProperty("DATASchema"));

		return pm;

	}

	public void ReadExcelExprot(String path) {
		Boolean bn = false;
		Map<String, Object> readmap = ReadExcel(path);
		@SuppressWarnings({ "unchecked", "unused" })
		List<MateColumnsBean> listmc = (List<MateColumnsBean>) readmap.get("TRANLIST");
		File file = new File(path);
		String metapath = file.getName();
		String str = metapath.substring(0, metapath.lastIndexOf("."));
		bn = ExcelExprot(str, listmc);

		if (bn) {
			log.info("���гɹ���");
		} else {
			log.info("����ʧ�ܣ�");
		}
	}

	/**
	 * ����XML��informatica
	 * 
	 * @param path
	 *            ��·��
	 * @return Boolean ���Ƿ�ִ�гɹ�
	 * 
	 */
	public Boolean ExprotXmlToinfo(String path) {
		Boolean bn = false;
		// RepositoryConnectionManager pu =new RepositoryConnectionManagerEx();
		PmrepExeUtl pu = new PmrepExeUtl("C:\\Informatica\\9.6.1\\clients\\PowerCenterClient\\client\\bin",
				new File("F:\\work01\\InfaBatch01\\tmp"), "dev_store_edw", "etldsvr01", "6005", "NC_ZJK", "499099784");

		File file = new File(path);
		try {
			RepoConnectionInfo ri = new RepoConnectionInfo();

			pu.setRepoConnectionInfo(ri);

			pu.doConnect();

			// pu.doCreateConnection(arg0);

			// createFolder repo = new createFolder();

			// createFolder repo = new createFolder();
			// RepositoryConnectionManager repMgr = new
			// PmrepRepositoryConnectionManagerEx();
			// repo.setRepositoryConnectionManager(repMgr);
			// repMgr.importObjectIntoRepository(path,
			// "F:\\work01\\InfaBatch01\\xml\\check.xml");
			pu.doImprotObject(path, "F:\\work01\\InfaBatch01\\xml\\check.xml",
					"F:\\work01\\InfaBatch01\\log\\" + file.getName().toString() + ".log");
		} catch (RepoConnectionObjectOperationException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (ConnectionFailedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return true;

	}

	/**
	 * ����XML��informatica
	 * 
	 * @param path
	 *            ��·��
	 * @return Boolean ���Ƿ�ִ�гɹ�
	 * 
	 */
	public Boolean ExprotXmlToinfoS(Map<String, Object> map) {
		// ExprotXmlToBat(path);
		Boolean bn = false;
		Process p = null;
		int num = 0;
		BufferedReader br = null;
		int runcount = Integer.parseInt(map.get("runcount").toString());
		StringBuffer text = new StringBuffer("��õ���Ϣ��: \n");
		if (runcount > 0) {
			try {

				// ִ������
				p = Runtime.getRuntime().exec(map.get("path").toString());
				// ȡ���������������
				InputStream fis = p.getInputStream();
				// ��һ�����������ȥ��
				InputStreamReader isr = new InputStreamReader(fis);
				// �û���������
				br = new BufferedReader(isr);
				String line = null;
				// ֱ������Ϊֹ

			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();

			}

			int ch;

			try {
				while ((ch = br.read()) != -1) {
					text.append((char) ch);
				}

			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
				log.error(text);
			}
			log.info(text);
			num = StringUtil.appearNumber(text.toString(), "objectImport completed successfully");
			bn = true;
		} else {
			log.info("û��Ҫ�����XML�ļ�");
		}
		log.info("����ɹ�����:" + num);
		int lcon = runcount - num;
		log.info("����ʧ�ܸ���:" + lcon);
		if (lcon > 0) {
			StringBuffer errStr = new StringBuffer();
			StringBuffer errDecStr = new StringBuffer();
			bn = false;
			List<String> lt = (List<String>) map.get("exeStr");
			String tempstr = "";
			Map<String, String> strmap = new HashMap();
			for (String str : lt) {

				int start = text.toString().indexOf(str.trim());
				int end = text.toString().indexOf("Completed at", start);
				// int len = "Completed at".length();
				if (start > 0 && end > 0) {
					tempstr = text.substring(start, end);
				} else if (start > 0) {
					tempstr = text.substring(start);
				}
				strmap.put(str, tempstr);
				if (tempstr.indexOf("objectImport completed successfully") < 0) {
					errStr = new StringBuffer("������� \n");
					errStr.append(str);
					errDecStr.append(tempstr);

				}
			}
			log.error(errStr);
			String errname = "err_" + FileUtil.getfileName(map.get("path").toString()).replace(".bat", ".log");
			String errpath = "logs//error//";
			FileUtil.WriteFile(errDecStr.toString(), errpath, errname, "GBK");

		}

		return bn;
	}

	/**
	 * ����XML��informatica
	 * 
	 * @param path
	 *            ��·��
	 * @return Boolean ���Ƿ�ִ�гɹ�
	 * 
	 */
	public List<String> ExprotXmlFile(File[] files) {
		List<String> lt = new ArrayList<String>();
		lt = FileUtil.getDirfile(files, "xml");
		log.info("ѡ��XML:" + lt);
		return lt;

	}

	/**
	 * ����XML������BAT�ļ�
	 * 
	 * @param path
	 *            ��·��
	 * @return Boolean ���Ƿ�ִ�гɹ�
	 * 
	 */
	public Map<String, Object> ExprotXmltoBat(File[] files) {

		String zpart = System.getProperty("user.dir");
		Map<String, String> mp1 = new HashMap<String, String>();
		Map<String, Object> map = new HashMap<String, Object>();
		List<String> lt = new ArrayList<String>();
		int runcount = 0;
		String path = "";
		String pathFilename = "";

//		mp1.put("InitTargetFolder", Utl.IsFileExists(zpart + "\\config\\TargetFolderXml\\" + "InitTargetFolder.xml")
//				? zpart + "\\config\\TargetFolderXml\\" + "InitTargetFolder.xml" : "");
//		mp1.put("CheckTargetFolder", Utl.IsFileExists(zpart + "\\config\\TargetFolderXml\\" + "CheckTargetFolder.xml")
//				? zpart + "\\config\\TargetFolderXml\\" + "CheckTargetFolder.xml" : "");
//		mp1.put("IncrementTargetFolder",
//				Utl.IsFileExists(zpart + "\\config\\TargetFolderXml\\" + "IncrementTargetFolder.xml")
//						? zpart + "\\config\\TargetFolderXml\\" + "IncrementTargetFolder.xml" : "");
//		
		
		mp1.put("InitTargetFolder", Utl.IsFileExists(".\\config\\TargetFolderXml\\" + "InitTargetFolder.xml")
				? ".\\config\\TargetFolderXml\\" + "InitTargetFolder.xml" : "");
		mp1.put("CheckTargetFolder", Utl.IsFileExists(".\\config\\TargetFolderXml\\" + "CheckTargetFolder.xml")
				? ".\\config\\TargetFolderXml\\" + "CheckTargetFolder.xml" : "");
		mp1.put("IncrementTargetFolder",
				Utl.IsFileExists(".\\config\\TargetFolderXml\\" + "IncrementTargetFolder.xml")
						?".\\config\\TargetFolderXml\\" + "IncrementTargetFolder.xml" : "");

		List<String> mp = ExprotXmlFile(files);
		StringBuffer str = new StringBuffer();
		if (!Utl.isEmpty(mp)) {
			Properties ps = Utl.getProperties(".\\config\\pcconfig.properties");
			String CLIENTPATH = "\""+ps.getProperty("PC_CLIENT_INSTALL_PATH").replaceAll(" ", "\" \"") + "\\pmrep"+"\"";
			String TARGET_REPO_NAME = ps.getProperty("TARGET_REPO_NAME");
			String REPO_SERVER_HOST = ps.getProperty("REPO_SERVER_HOST");
			String REPO_SERVER_PORT = ps.getProperty("REPO_SERVER_PORT");
			String ADMIN_USERNAME = ps.getProperty("ADMIN_USERNAME");
			String ADMIN_PASSWORD = ps.getProperty("ADMIN_PASSWORD");
			if (Utl.IsFileExists(ps.getProperty("PC_CLIENT_INSTALL_PATH"))) {
				str.append(CLIENTPATH + " connect" + " -r " + TARGET_REPO_NAME + " -h " + REPO_SERVER_HOST + " -o "
						+ REPO_SERVER_PORT + " -n " + ADMIN_USERNAME + " -x " + ADMIN_PASSWORD + "  " + "\n" + "");
				for (String s : mp) {
					File file = new File(s);
					String strParentDirectory = file.getParent();
					String strDirectory = strParentDirectory.substring(strParentDirectory.lastIndexOf("\\") + 1,
							strParentDirectory.length());
					String modelpath = "";
					if ("InitXml".equals(strDirectory)) {
						modelpath = mp1.get("InitTargetFolder");
					} else if ("CheckXml".equals(strDirectory)) {
						modelpath = mp1.get("CheckTargetFolder");
					} else {
						modelpath = mp1.get("IncrementTargetFolder");
					}
					if (!Utl.isEmpty(modelpath)) {
						String exeStr = CLIENTPATH + " objectImport" + " -i " + "\""+s.replace(zpart+"\\", "")+ "\""+ " -c "
								+"\""+ modelpath +"\""+ "\n" + "";
						str.append(exeStr);
						runcount++;
						lt.add(exeStr);

					} else {
						log.error(s + ":û�ж�Ӧ�Ŀ����ļ�");

					}
				}
			} else {
				log.error("config\\pcconfig.properties��PC_CLIENT_INSTALL_PATH����·����Ч");

			}
		}
		if (!Utl.isEmpty(str)) {
			path = zpart + "\\bat\\";
			SimpleDateFormat formatter = new SimpleDateFormat("yyyyMMddHHmmssSSS");
			String formatStr = formatter.format(new Date());
			String fileName = "rt_" + formatStr + ".bat";
			log.info("����XML�ű�·��:" + path + fileName);
			FileUtil.WriteFile(str.toString(), path, fileName, "GBK");
			pathFilename = path + fileName;
			map.put("path", pathFilename);
			map.put("runcount", runcount);
			map.put("exeStr", lt);
			return map;

		}
		return null;

	}

	/**
	 * ����XML������Xml��TargetFolder�����ļ�
	 * 
	 * @param path
	 *            ��·��
	 * @return Boolean ���Ƿ�ִ�гɹ�
	 * 
	 */
	public Boolean ExprotXmlModel(String path) {
		Boolean bn = false;
		Properties p = Utl.getProperties(path);
		Map<String, String> mp = new HashMap<String, String>();

		if (Utl.isEmpty(p.getProperty("InitTargetFolder")) || Utl.isEmpty(p.getProperty("CheckTargetFolder"))
				|| Utl.isEmpty(p.getProperty("IncrementTargetFolder")) || Utl.isEmpty(p.getProperty("Repository"))) {
			if (Utl.isEmpty(p.getProperty("InitTargetFolder"))) {
				log.info("InitTargetFolder������Ϊnull");
			}
			if (Utl.isEmpty(p.getProperty("CheckTargetFolder"))) {
				log.info("CheckTargetFolder������Ϊnull");
			}
			if (Utl.isEmpty(p.getProperty("IncrementTargetFolder"))) {
				log.info("IncrementTargetFolder������Ϊnull");
			}
			if (Utl.isEmpty(p.getProperty("Repository"))) {
				log.info("Repository������Ϊnull");
			}

		} else {
			mp.put("InitTargetFolder", p.getProperty("InitTargetFolder"));
			mp.put("CheckTargetFolder", p.getProperty("CheckTargetFolder"));
			mp.put("IncrementTargetFolder", p.getProperty("IncrementTargetFolder"));
			String jobEntryHopsXmlTemplate = FileUtil.readToBuffer("config\\TargetFolderModel.xml", "GBK").toString();
			// ���Ͽ�����
			String jobEntryHopsXml = "";
			for (String key : mp.keySet()) {
				System.out.println("key= " + key + " and value= " + mp.get(key));
				String spath = ".\\config\\TargetFolderXml\\" + key + ".xml";
				if (Utl.IsFileExists(spath)) {
					bn = FileUtil.deleteFile(spath);
				}
				if (!Utl.isEmpty(p.get(key))) {
					// System.out.println(mp.get(key));

					jobEntryHopsXml = jobEntryHopsXmlTemplate
							.replaceAll("\\$\\{Repository\\}", p.getProperty("Repository"))
							.replaceAll("\\$\\{TARGETFOLDERNAME\\}", mp.get(key));

					bn = FileUtil.WriteFile(jobEntryHopsXml, ".\\config\\TargetFolderXml\\", key + ".xml", "GBK");
				} else {
					log.info(path + "��:" + key + " ,������Ϊ��");
				}
			}
		}
		return bn;

	}
}
