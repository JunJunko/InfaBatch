package com.exprotmeteexcel.dao.impl;

import java.util.List;
import java.util.Map;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import com.exprotmeteexcel.bean.MateBean;

/**
 * Teradata��JDBC����dao��
 * 
 * @author admin
 *
 */
public class TeradataDbDaoI extends BaseDbDaoI {

	private static final Log log = LogFactory.getLog(OracleDbDaoI.class);

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
	public TeradataDbDaoI(String url, String userName, String passWord) {
		super();
		db_url = url;
		db_userName = userName;
		db_userPass = passWord;
		DRIVER = "com.teradata.jdbc.TeraDriver";
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
	public TeradataDbDaoI(String url, String userName, String passWord, int overTime) {
		super();
		db_url = url;
		db_userName = userName;
		db_userPass = passWord;
		db_overtime = overTime;
		DRIVER = "com.teradata.jdbc.TeraDriver";
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
	public TeradataDbDaoI(String dbName, String ip, String port, String userName, String passWord) {
		super();
		db_url = getUrl(dbName, ip, port);
		db_userName = userName;
		db_userPass = passWord;
		db_ssid = dbName;
		DRIVER = "com.teradata.jdbc.TeraDriver";
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
