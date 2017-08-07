package com.exprotmeteexcel.dao.impl;

import java.sql.Connection;
import java.sql.DatabaseMetaData;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import com.exprotmeteexcel.bean.MateBean;
import com.exprotmeteexcel.utl.global.SQLGlobal;

/**
 * mysql的JDBC操作DAO类
 * 
 * @author admin
 *
 */
public class MysqlDbDaoI extends BaseDbDaoI {

	private static final Log log = LogFactory.getLog(OracleDbDaoI.class);

	/**
	 * 构造方法
	 * 
	 * @param url
	 *            URL路径
	 * @param userName
	 *            用户名
	 * @param passWord
	 *            密码
	 */
	public MysqlDbDaoI(String url, String userName, String passWord) {
		super();
		db_url = url;
		db_userName = userName;
		db_userPass = passWord;
		DRIVER = "com.mysql.jdbc.Driver";
	}

	/**
	 * 构造函数
	 * 
	 * @param url
	 *            URL路径
	 * @param userName
	 *            用户名
	 * @param passWord
	 *            密码
	 * @param overTime
	 */
	public MysqlDbDaoI(String url, String userName, String passWord, int overTime) {
		super();
		db_url = url;
		db_userName = userName;
		db_userPass = passWord;
		db_overtime = overTime;
		DRIVER = "com.mysql.jdbc.Driver";
	}

	/**
	 * 构造函数
	 * 
	 * @param dbName
	 *            数据库名
	 * @param ip
	 *            IP
	 * @param port
	 *            端口
	 * @param userName
	 *            用户名
	 * @param passWord
	 *            密码
	 */
	public MysqlDbDaoI(String dbName, String ip, String port, String userName, String passWord) {
		super();
		db_url = getUrl(dbName, ip, port);
		db_userName = userName;
		db_userPass = passWord;
		db_ssid = dbName;
		DRIVER = "com.mysql.jdbc.Driver";
	}

	/**
	 * 获取连接url
	 * 
	 * @param dbName
	 *            数据库实例名
	 * @param ip
	 *            数据库ip
	 * @param port
	 *            数据库端口
	 * @param userName
	 *            用户名
	 * @param passWord
	 *            密码
	 * @return
	 */
	public String getUrl(String dbName, String ip, String port) {
		return "jdbc:mysql://" + ip + ":" + port + "/" + dbName;
	}

	@Override
	public List<Map<String, Object>> getTableColumn(MateBean ownertables) {
		// TODO Auto-generated method stub
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
		String sql = SQLGlobal.GET_COLUMN_MYSQLSQL.replaceAll(":ownertable", owntable.toString());
		su = getDateForMap(sql);

		return su;
	}

	@Override
	public List<Map<String, Object>> getTableColumn() {
		List<Map<String, Object>> su = null;
		String sql = SQLGlobal.GET_COLUMN_MYSQLSQL_ALL;
		su = getDateForMap(sql);

		return su;
	}

	@Override
	public List<Map<String, Object>> getTableColumnByMeta(MateBean ownertables) {

		// TODO Auto-generated method stub
		Connection conn = null;
		List<Map<String, Object>> su = new ArrayList<Map<String, Object>>();

		List<String> prikey = new ArrayList<String>();
		DatabaseMetaData dbmd = null;
		ResultSet rs = null;
		ResultSet pri = null;
		ResultSet rstable = null;
		String tableRemarks = "";
		int i = 0;
		String[] types = { "TABLE" };
		try {
			long d1 = System.currentTimeMillis();
			log.info("执行取原数据");
			conn = this.getConnection();
			dbmd = conn.getMetaData();
			List<Map<String, Object>> owntab = ownertables.getMatedate();
			for (int j = 0; j < owntab.size(); j++) {
				rs = dbmd.getColumns(owntab.get(j).get("OWNER").toString(), null,
						owntab.get(j).get("TABLE_NAME").toString(), null);

				rstable = dbmd.getTables(owntab.get(j).get("OWNER").toString(), null,
						owntab.get(j).get("TABLE_NAME").toString(), types);
				pri = dbmd.getPrimaryKeys(owntab.get(j).get("OWNER").toString(), null,
						owntab.get(j).get("TABLE_NAME").toString());

				while (rstable.next()) {
					tableRemarks = rstable.getString("REMARKS");
				}
				while (pri.next()) {
					prikey.add(pri.getString("COLUMN_NAME"));
				}
				while (rs.next()) {

					// System.out.println(i+":"+rs.getString("COLUMN_NAME")+rs.getInt("COLUMN_SIZE")+
					// rs.getString("TYPE_NAME"));
					Map<String, Object> mp = new HashMap<String, Object>();
					mp.put("COLUMN_NAME", rs.getString("COLUMN_NAME"));
					mp.put("TABLE_NAME", rs.getString("TABLE_NAME"));
					Object colsize = rs.getInt("DECIMAL_DIGITS") != 0
							? (rs.getInt("COLUMN_SIZE") + "," + rs.getInt("DECIMAL_DIGITS")) : rs.getInt("COLUMN_SIZE");
					mp.put("COLUMN_SIZE", colsize);
					mp.put("TYPE_NAME", rs.getString("TYPE_NAME"));
					mp.put("DB_TYPE", getDb_type());
					mp.put("REMARKS", rs.getString("REMARKS"));
					mp.put("TABLE_REMARKS", tableRemarks);
					// String dbtype = getDb_type();
					mp.put("OWNER",rs.getString("TABLE_CAT"));
					mp.put("ISNULL", rs.getString("IS_NULLABLE"));
					mp.put("COLUMN_DEF", rs.getString("COLUMN_DEF"));
					mp.put("PRIMARYKEY", prikey.contains(rs.getString("COLUMN_NAME")) ? "pri" : null);
					su.add(mp);
					i++;
				}
			}

			log.info("执行 耗时：" + (System.currentTimeMillis() - d1) + "毫秒");
			log.info("执行完成处理行数:" + i);
		} catch (Exception e) {
			log.error(System.currentTimeMillis() + "getTableColumnByMeta方法");
			log.error(System.currentTimeMillis() + "异常原因：" + e.toString());
			log.error("执行到行数:" + i);
			//e.printStackTrace();

		} finally {
			try {
				rs.close();
				pri.close();
				conn.close();
			} catch (SQLException e) {
				// TODO Auto-generated catch block
				log.error(System.currentTimeMillis() + "getTableColumnByMeta方法");
				log.error(System.currentTimeMillis() + "异常原因：" + e.toString());
			}

		}
		return su;
	}
}
