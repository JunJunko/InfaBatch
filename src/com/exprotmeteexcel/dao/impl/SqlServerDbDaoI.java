package com.exprotmeteexcel.dao.impl;

import java.sql.Connection;
import java.sql.DatabaseMetaData;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.exprotmeteexcel.bean.MateBean;
import com.exprotmeteexcel.utl.global.SQLGlobal;

/**
 * mysql��JDBC����DAO��
 * 
 * @author admin
 *
 */
public class SqlServerDbDaoI extends BaseDbDaoI {

	private static final Logger log = LoggerFactory.getLogger(OracleDbDaoI.class);

	/**
	 * ���췽��
	 * 
	 * @param url
	 *            URL·��
	 * @param userName
	 *            �û���
	 * @param passWord
	 *            ����
	 */
	public SqlServerDbDaoI(String url, String userName, String passWord) {
		super();
		db_url = url;
		db_userName = userName;
		db_userPass = passWord;
		DRIVER = "com.microsoft.sqlserver.jdbc.SQLServerDriver";

	}

	/**
	 * ���캯��
	 * 
	 * @param url
	 *            URL·��
	 * @param userName
	 *            �û���
	 * @param passWord
	 *            ����
	 * @param overTime
	 */
	public SqlServerDbDaoI(String url, String userName, String passWord, int overTime) {
		super();
		db_url = url;
		db_userName = userName;
		db_userPass = passWord;
		db_overtime = overTime;
		DRIVER = "com.microsoft.sqlserver.jdbc.SQLServerDriver";

	}

	/**
	 * ���캯��
	 * 
	 * @param dbName
	 *            ���ݿ���
	 * @param ip
	 *            IP
	 * @param port
	 *            �˿�
	 * @param userName
	 *            �û���
	 * @param passWord
	 *            ����
	 */
	public SqlServerDbDaoI(String dbName, String ip, String port, String userName, String passWord) {
		super();
		db_url = getUrl(dbName, ip, port);
		db_userName = userName;
		db_userPass = passWord;
		db_ssid = dbName;
		DRIVER = "com.microsoft.sqlserver.jdbc.SQLServerDriver";

	}

	/**
	 * ��ȡ����url
	 * 
	 * @param dbName
	 *            ���ݿ�ʵ����
	 * @param ip
	 *            ���ݿ�ip
	 * @param port
	 *            ���ݿ�˿�
	 * @param userName
	 *            �û���
	 * @param passWord
	 *            ����
	 * @return
	 */
	public String getUrl(String dbName, String ip, String port) {
		return "jdbc:sqlserver://" + ip + ":" + port + ";databaseName=" + dbName;
	}

	@Override
	public List<Map<String, Object>> getTableColumn(MateBean ownertables) {

		List<Map<String, Object>> su = new ArrayList<Map<String, Object>>();
		StringBuffer owntable = new StringBuffer();
		List<Map<String, Object>> lm = ownertables.getMatedate();
		for (int i = 0; i < lm.size(); i++) {
			if (i == 0) {
				owntable.append("'" + lm.get(i).get("OWNER_NM") + "." + lm.get(i).get("TABLE_NM") + "'");
			} else {
				owntable.append(",'" + lm.get(i).get("OWNER_NM") + "." + lm.get(i).get("TABLE_NM") + "'");
			}

		}
		String sql = SQLGlobal.GET_COLUMN_SQLSERVERSQL.replaceAll(":ownertable", owntable.toString());
		su = getDateForMap(sql);

		return su;
	}

	@Override
	public List<Map<String, Object>> getTableColumn() {
		List<Map<String, Object>> su = null;
		String sql = SQLGlobal.GET_COLUMN_SQLSERVERSQL_All;
		su = getDateForMap(sql);

		return su;
	}


	public List<Map<String, Object>> getTableColumnByMetaOne(MateBean ownertables) {
		// TODO Auto-generated method stub
		Connection conn = null;
		List<Map<String, Object>> su = new ArrayList<Map<String, Object>>();

		List<String> prikey = new ArrayList<String>();
		DatabaseMetaData dbmd = null;
		ResultSet rs = null;
		ResultSet pri = null;
		int i = 0;
		try {
			long d1 = System.currentTimeMillis();
			log.info("ִ��ȡԭ����");
			conn = this.getConnection();
			dbmd = conn.getMetaData();
			List<Map<String, Object>> owntab = ownertables.getMatedate();
			for (int j = 0; j < owntab.size(); j++) {
				rs = dbmd.getColumns(ownertables.getDbSid(), owntab.get(j).get("OWNER").toString(),
						owntab.get(j).get("TABLE_NAME").toString(), null);
				pri = dbmd.getPrimaryKeys(ownertables.getDbSid(), owntab.get(j).get("OWNER").toString(),
						owntab.get(j).get("TABLE_NAME").toString());

				while (pri.next()) {
					prikey.add(pri.getString("COLUMN_NAME"));
				}
				while (rs.next()) {

					// System.out.println(i+":"+rs.getString("COLUMN_NAME")+rs.getInt("COLUMN_SIZE")+
					// rs.getString("TYPE_NAME"));
					Map<String, Object> mp = new HashMap<String, Object>();
					mp.put("COLUMN_NAME", rs.getString("COLUMN_NAME"));
					mp.put("TABLE_NAME", rs.getString("TABLE_NAME"));
					mp.put("COLUMN_SIZE", rs.getInt("COLUMN_SIZE"));
					mp.put("TYPE_NAME", rs.getString("TYPE_NAME"));
					mp.put("DB_TYPE", getDb_type());
					// String dbtype = getDb_type();
					mp.put("OWNER",
							"mysql".equals(getDb_type()) ? rs.getString("TABLE_CAT") : rs.getString("TABLE_SCHEM"));
					mp.put("ISNULL", rs.getObject("IS_NULLABLE"));
					mp.put("COLUMN_DEF", rs.getObject("COLUMN_DEF"));
					mp.put("PRIMARYKEY", prikey.contains(rs.getString("COLUMN_NAME")) ? "pri" : null);
					su.add(mp);
					i++;
					System.out.println("���е�i:" + i);
				}
			}
			log.info("ִ�� ��ʱ��" + (System.currentTimeMillis() - d1) + "����");
			log.info("ִ����ɴ�������:" + i);
		} catch (Exception e) {
			log.error(System.currentTimeMillis() + "getTableColumnByMeta����");
			log.error(System.currentTimeMillis() + "�쳣ԭ��" + e.toString());
			log.error("ִ�е�����:" + i);
			e.printStackTrace();
		} finally {
			try {
				rs.close();
				pri.close();
				conn.close();
			} catch (SQLException e) {
				// TODO Auto-generated catch block
				log.error(System.currentTimeMillis() + "getTableColumnByMeta����");
				log.error(System.currentTimeMillis() + "�쳣ԭ��" + e.toString());
			}

		}
		return su;
	}
	@Override
	public List<Map<String, Object>> getTableColumnByMeta(MateBean ownertables) {
		List<Map<String, Object>> su = new ArrayList<Map<String, Object>>();
		StringBuffer owntable = new StringBuffer();
		List<String> prikey = new ArrayList<String>();
		List<Map<String, Object>> lm = ownertables.getMatedate();
		for (int i = 0; i < lm.size(); i++) {
			if (i == 0) {
				owntable.append("'" + lm.get(i).get("OWNER") + "." + lm.get(i).get("TABLE_NAME") + "'");
			} else {
				owntable.append(",'" + lm.get(i).get("OWNER") + "." + lm.get(i).get("TABLE_NAME") + "'");
			}

		}
		String sql = SQLGlobal.GET_COLUMN_SQLSERVERSQL.replaceAll(":ownertable", owntable.toString());
		List<Map<String, Object>> mtu = getDateForMap(sql);
		if (mtu != null) {
			for (Map<String, Object> rs : mtu) {
				Map<String, Object> mp = new HashMap<String, Object>();
				mp.put("COLUMN_NAME", rs.get("COLUMN_NAME"));
				mp.put("TABLE_NAME", rs.get("TABLE_NAME"));
				mp.put("COLUMN_SIZE", rs.get("COLUMN_SIZE"));
				mp.put("TYPE_NAME", rs.get("TYPE_NAME"));
				mp.put("DB_TYPE", getDb_type());
				// String dbtype = getDb_type();
				mp.put("OWNER", rs.get("OWNER"));
				mp.put("ISNULL", rs.get("IS_NULLABLE"));
				mp.put("COLUMN_DEF", rs.get("COLUMN_DEF"));
				mp.put("PRIMARYKEY", rs.get("PRIMARYKEY"));
				su.add(mp);
			}
		}
		return su;
	}

}
