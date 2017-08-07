package com.exprotmeteexcel.dao.impl;

import java.util.List;
import java.util.Map;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import com.exprotmeteexcel.bean.MateBean;

/**
 * Teradata的JDBC操作dao类
 * 
 * @author admin
 *
 */
public class TeradataDbDaoI extends BaseDbDaoI {

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
	public TeradataDbDaoI(String url, String userName, String passWord) {
		super();
		db_url = url;
		db_userName = userName;
		db_userPass = passWord;
		DRIVER = "com.teradata.jdbc.TeraDriver";
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
	public TeradataDbDaoI(String url, String userName, String passWord, int overTime) {
		super();
		db_url = url;
		db_userName = userName;
		db_userPass = passWord;
		db_overtime = overTime;
		DRIVER = "com.teradata.jdbc.TeraDriver";
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
	public TeradataDbDaoI(String dbName, String ip, String port, String userName, String passWord) {
		super();
		db_url = getUrl(dbName, ip, port);
		db_userName = userName;
		db_userPass = passWord;
		db_ssid = dbName;
		DRIVER = "com.teradata.jdbc.TeraDriver";
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
		// return "jdbc:teradata://"+ip+"//database="+dbName;

		return "jdbc:teradata://" + ip + "/" + dbName;

		// "jdbc:teradata://172.21.29.177//database=ODS_DATA";

	}

	@Override
	public List<Map<String, Object>> getTableColumn(MateBean ownertables) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public List<Map<String, Object>> getTableColumn() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public List<Map<String, Object>> getTableColumnByMeta(MateBean ownertables) {
		// TODO Auto-generated method stub
		return null;
	}

}
