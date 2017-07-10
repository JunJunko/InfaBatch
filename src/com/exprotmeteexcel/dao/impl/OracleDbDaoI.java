package com.exprotmeteexcel.dao.impl;

import java.sql.Connection;
import java.sql.DatabaseMetaData;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Properties;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.exprotmeteexcel.bean.MateBean;
import com.exprotmeteexcel.utl.Utl;
import com.exprotmeteexcel.utl.global.SQLGlobal;

/**
 * mysql的JDBC操作DAO类
 * 
 * @author admin
 *
 */
public class OracleDbDaoI extends BaseDbDaoI {

	private static final Logger log = LoggerFactory.getLogger(OracleDbDaoI.class);

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
	public OracleDbDaoI(String url, String userName, String passWord) {
		super();
		db_url = url;
		db_userName = userName;
		db_userPass = passWord;
		DRIVER = "oracle.jdbc.driver.OracleDriver";
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
	public OracleDbDaoI(String url, String userName, String passWord, int overTime) {
		super();
		db_url = url;
		db_userName = userName;
		db_userPass = passWord;
		db_overtime = overTime;
		DRIVER = "oracle.jdbc.driver.OracleDriver";
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
	public OracleDbDaoI(String dbName, String ip, String port, String userName, String passWord) {
		super();
		db_url = getUrl(dbName, ip, port);
		db_userName = userName;
		db_userPass = passWord;
		db_ssid = dbName;
		DRIVER = "oracle.jdbc.driver.OracleDriver";
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
		return "jdbc:oracle:thin:@" + ip + ":" + port + ":" + dbName;
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
		String sql = SQLGlobal.GET_COLUMN_ORACLESQL.replaceAll(":ownertable", owntable.toString());
		su = getDateForMap(sql);

		return su;
	}

	@Override
	public List<Map<String, Object>> getTableColumn() {
		List<Map<String, Object>> su = null;
		String sql = SQLGlobal.GET_COLUMN_ORACLESQL_ALL;
		su = getDateForMap(sql);

		return su;
	}

	@Override
	public List<Map<String, Object>> getTableColumnByMeta(MateBean ownertables) {
		// TODO Auto-generated method stub
		Connection conn = null;
		String[] types = { "TABLE" };
		ResultSet rstable = null;
		List<Map<String, Object>> su = new ArrayList<Map<String, Object>>();

		List<String> prikey = new ArrayList<String>();
		DatabaseMetaData dbmd = null;
		ResultSet rs = null;
		ResultSet pri = null;
		String tableRemarks = "";
		int i = 0;
		try {
			long d1 = System.currentTimeMillis();
			log.info("执行取原数据");
			Properties props = new Properties();
			props.put("remarksReporting", "true");
			conn = this.getConnection(props);
			dbmd = conn.getMetaData();
			List<Map<String, Object>> owntab = ownertables.getMatedate();
			for (int j = 0; j < owntab.size(); j++) {
				rs = dbmd.getColumns(ownertables.getDbSid(), owntab.get(j).get("OWNER").toString(),
						owntab.get(j).get("TABLE_NAME").toString(), null);

				rstable = dbmd.getTables(null, owntab.get(j).get("OWNER").toString(),
						owntab.get(j).get("TABLE_NAME").toString(), types);
				pri = dbmd.getPrimaryKeys(ownertables.getDbSid(), owntab.get(j).get("OWNER").toString(),
						owntab.get(j).get("TABLE_NAME").toString());

				while (rstable.next()) {
					// System.out.println("table_name:" +
					// rstable.getString("TABLE_NAME")+"table_REMARKS:" +
					// rstable.getString(12));
					tableRemarks = rstable.getString("REMARKS");
				}
				while (pri.next()) {
					prikey.add(pri.getString("COLUMN_NAME"));
				}
				while (rs.next()) {

					Map<String, Object> mp = new HashMap<String, Object>();
					mp.put("DB_TYPE", getDb_type());
					mp.put("OWNER", rs.getString("TABLE_SCHEM"));
					mp.put("TABLE_NAME", rs.getString("TABLE_NAME"));
					mp.put("COLUMN_NAME", rs.getString("COLUMN_NAME"));
					mp.put("TYPE_NAME", rs.getString("TYPE_NAME"));
					Object colsize = rs.getInt("COLUMN_SIZE") == 0 ? 22
							: rs.getInt("DECIMAL_DIGITS") != 0
									? (rs.getInt("COLUMN_SIZE") + "," + rs.getInt("DECIMAL_DIGITS"))
									: rs.getInt("COLUMN_SIZE");
					// System.out.println("DECIMAL_DIGITS:"+rs.getInt("DECIMAL_DIGITS")+";###COLUMN_SIZE:"+rs.getInt("COLUMN_SIZE"));
					mp.put("COLUMN_SIZE", colsize);
					mp.put("REMARKS", rs.getString("REMARKS"));
					// System.out.println("REMARKS:"+ rs.getString("REMARKS"));
					mp.put("TABLE_REMARKS", tableRemarks);
					//System.out.println(i+"getObject(12):"+rs.getObject(12));
					//System.out.println(i+"getObject(13):"+rs.getObject(13));
					mp.put("COLUMN_DEF", "");
					mp.put("ISNULL", rs.getString("IS_NULLABLE"));
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
			e.printStackTrace();
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
