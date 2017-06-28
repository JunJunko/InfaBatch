package com.exprotmeteexcel.dao.impl;

import java.sql.CallableStatement;
import java.sql.Connection;
import java.sql.DatabaseMetaData;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;
import java.sql.Statement;
import java.sql.Types;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Properties;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.exprotmeteexcel.bean.MateBean;
import com.exprotmeteexcel.dao.BaseDbDao;

/**
 * SQL��dao�࣬����/�ر����ӣ�ִ�нű�
 * 
 * @author admin
 * 
 */

public abstract class BaseDbDaoI implements BaseDbDao {
	protected String db_url = "";// �����ַ���
	protected String db_userName = "";// �û���
	protected String db_userPass = "";// �û�����
	protected int db_overtime = 10;// ��
	protected String db_ssid = "";// ���ݿ�ʵ��
	protected int fetchSize = 1000;
	protected String db_type = "";//
	protected int BatchSize = 5000;
	protected String DRIVER;

	private static final Logger log = LoggerFactory.getLogger(BaseDbDaoI.class);

	/**
	 * �� jdbc����
	 * 
	 * @param driver
	 *            ������
	 * @param url
	 *            ����ַ
	 * @param username
	 *            ���û���
	 * @param password
	 *            ������
	 * @return ����������
	 * @throws ClassNotFoundException
	 * @throws SQLException
	 */

	public Connection getConnection() throws SQLException {

		String DRIVER = this.DRIVER;

		try {
			Class.forName(DRIVER);
		} catch (ClassNotFoundException e) {
			e.printStackTrace();
		}
		return DriverManager.getConnection(this.db_url, this.db_userName, this.db_userPass);

	}
	
	
	public Connection getConnection(Properties props) throws SQLException {

		String DRIVER = this.DRIVER;
		props.put("user", this.db_userName);
		props.put("password", this.db_userPass);

		try {
			Class.forName(DRIVER);
		} catch (ClassNotFoundException e) {
			e.printStackTrace();
		}
		return DriverManager.getConnection(this.db_url,props);

	}

	/**
	 * �ر�����
	 * 
	 * @param conn
	 *            ��������
	 * @param stmt
	 *            �����ݿ���䷢�Ͷ���
	 * @param rs
	 *            �������
	 */
	public static void closeAll(Connection conn, Statement stmt, ResultSet rs) {
		try {
			if (rs != null) {
				rs.close();
			}
		} catch (Exception e) {
			log.error("������Ϣ", e);
		}
		try {
			if (stmt != null) {
				stmt.close();
			}
		} catch (Exception e) {
			log.error("������Ϣ", e);
		}
		try {
			if (conn != null && !conn.isClosed()) {
				conn.close();
			}
		} catch (Exception e) {
			log.error("������Ϣ", e);
		}
	}

	/**
	 * ����ִ�� INSERT��UPDATE �� DELETE ���
	 * 
	 * @param sql
	 * @param param
	 * @return
	 */
	public Boolean execute(String sql, String[] param) {

		Connection conn = null;
		PreparedStatement ps = null;
		boolean rs = false;
		try {
			long d1 = System.currentTimeMillis();
			log.info("ִ��SQL��" + sql);
			conn = getConnection();
			ps = conn.prepareStatement(sql);
			ps.setFetchSize(fetchSize);
			if (param != null) {
				for (int i = 0; i < param.length; i++) {
					ps.setString(i + 1, param[i]);
				}
			}
			rs = ps.execute();
			log.info("ִ��SQL��ʱ��" + (System.currentTimeMillis() - d1) + "����");

		} catch (SQLException e) {
			log.error("������Ϣ", e);
		} finally {
			closeAll(conn, ps, null);
		}
		return rs;
	}

	/**
	 * ����ִ�� INSERT��UPDATE �� DELETE ���
	 * 
	 * @param sql
	 * @param param
	 * @return
	 */
	public Boolean executeByCon(String sql, String[] param, Connection conn) {

		PreparedStatement ps = null;
		boolean rs = false;
		try {
			long d1 = System.currentTimeMillis();
			log.info("ִ��SQL��" + sql);
			ps = conn.prepareStatement(sql);
			ps.setFetchSize(fetchSize);
			if (param != null) {
				for (int i = 0; i < param.length; i++) {
					ps.setString(i + 1, param[i]);
				}
			}
			rs = ps.execute();
			log.info("ִ��SQL��ʱ��" + (System.currentTimeMillis() - d1) + "����");

		} catch (SQLException e) {
			log.error("������Ϣ", e);
		} finally {
			closeAll(conn, ps, null);
		}
		return rs;
	}

	/**
	 * ����ִ�� INSERT��UPDATE �� DELETE ����Լ� SQL DDL�����ݶ������ԣ����
	 * 
	 * @param sql
	 * @param param
	 * @return
	 */
	public int executeUpdate(String sql, String[] param) {

		Connection conn = null;
		PreparedStatement ps = null;
		int rs = 0;
		try {
			long d1 = System.currentTimeMillis();
			log.info("ִ��SQL��" + sql);
			conn = getConnection();
			ps = conn.prepareStatement(sql);
			ps.setFetchSize(fetchSize);
			if (param != null) {
				for (int i = 0; i < param.length; i++) {
					ps.setString(i + 1, param[i]);
				}
			}
			rs = ps.executeUpdate();
			log.info("ִ��SQL��ʱ��" + (System.currentTimeMillis() - d1) + "����");
			return rs;

		} catch (SQLException e) {
			log.error("������Ϣ", e);
		} finally {
			closeAll(conn, ps, null);
		}
		return rs;
	}

	/**
	 * ��ͨsql��ѯ
	 * 
	 * @param sql
	 * @param param
	 * @return
	 */
	public List<Map<String, String>> executeQuery(String sql, String[] param) {

		List<Map<String, String>> results = new ArrayList<Map<String, String>>();
		Connection conn = null;
		PreparedStatement ps = null;
		ResultSet rs = null;
		try {
			long d1 = System.currentTimeMillis();
			log.info("ִ��SQL��" + sql);

			conn = getConnection();
			ps = conn.prepareStatement(sql);
			ps.setFetchSize(fetchSize);
			if (param != null) {
				for (int i = 0; i < param.length; i++) {
					ps.setString(i + 1, param[i]);
				}
			}
			rs = ps.executeQuery();
			ResultSetMetaData rsmd = rs.getMetaData();
			int column = rsmd.getColumnCount();

			while (rs.next()) {
				Map<String, String> map = new HashMap<String, String>();
				for (int i = 1; i <= column; i++) {
					String cols_name = rsmd.getColumnName(i);
					Object cols_value;
					if (Types.CLOB == rsmd.getColumnType(i) || Types.VARBINARY == rsmd.getColumnType(i)) {
						map.put(cols_name, "".equals(rs.getString(cols_name)) || rs.getString(cols_name) == null ? ""
								: rs.getString(cols_name).trim());
					} else {
						cols_value = rs.getObject(cols_name);
						if (cols_value == null) {
							cols_value = "";
						}
						map.put(cols_name, cols_value.toString());
					}

				}
				results.add(map);
			}

			log.info("ִ��SQL��ʱ��" + (System.currentTimeMillis() - d1) + "����");

		} catch (SQLException e) {
			log.error("������Ϣ", e);
		} finally {
			closeAll(conn, ps, rs);
		}
		return results;
	}

	/**
	 * ��ͨsql��ѯ
	 * 
	 * @param sql
	 * @param param
	 * @return
	 */
	public List<Map<String, String>> executeQueryByConn(String sql, String[] param, Connection conn) {

		List<Map<String, String>> results = new ArrayList<Map<String, String>>();
		PreparedStatement ps = null;
		ResultSet rs = null;
		try {
			long d1 = System.currentTimeMillis();
			log.info("ִ��SQL��" + sql);

			ps = conn.prepareStatement(sql);
			ps.setFetchSize(fetchSize);
			if (param != null) {
				for (int i = 0; i < param.length; i++) {
					ps.setString(i + 1, param[i]);
				}
			}
			rs = ps.executeQuery();

			ResultSetMetaData rsmd = rs.getMetaData();
			int column = rsmd.getColumnCount();

			while (rs.next()) {
				Map<String, String> map = new HashMap<String, String>();
				for (int i = 1; i <= column; i++) {
					String cols_name = rsmd.getColumnName(i);
					Object cols_value;

					if (Types.CLOB == rsmd.getColumnType(i) || Types.BLOB == rsmd.getColumnType(i)
							|| Types.VARBINARY == rsmd.getColumnType(i)) {
						map.put(cols_name, "".equals(rs.getString(cols_name)) || rs.getString(cols_name) == null ? ""
								: rs.getString(cols_name).trim());
					} else {
						cols_value = rs.getObject(cols_name);
						if (cols_value == null) {
							cols_value = "";
						}
						map.put(cols_name, cols_value.toString());
					}
				}
				results.add(map);
			}

			log.info("ִ��SQL��ʱ��" + (System.currentTimeMillis() - d1) + "����");

		} catch (SQLException e) {
			log.error("������Ϣ", e);
		} finally {
			closeAll(null, ps, rs);
		}

		return results;
	}

	/**
	 * ִ��sql������ɾ��
	 * 
	 * @param sql
	 *            SQL�ű�
	 * @param param
	 *            ע�����
	 * @return Ӱ�캯��
	 * @throws SQLException
	 */
	public Integer executeSQL(String sql, String[] param) throws SQLException {
		Integer result = 0;
		Connection conn = null;
		PreparedStatement pst = null;
		ResultSet rs = null;
		try {
			long d1 = System.currentTimeMillis();
			log.info("ִ��SQL��" + sql);

			conn = getConnection();
			pst = (PreparedStatement) conn.prepareStatement(sql);
			if (param != null) {
				for (int i = 0; i < param.length; i++) {
					pst.setString(i + 1, param[i]);
				}
			}
			result = pst.executeUpdate();
			conn.commit();// �ύ����

			log.info("ִ��SQL ʱ�䣺" + (System.currentTimeMillis() - d1) + "����");

		} catch (SQLException e) {
			if (conn != null) {
				conn.rollback();
			} // �ع�����
			log.error("ִ��sql�����쳣��" + e.toString());
		} finally {
			closeAll(conn, pst, rs);
		}
		return result;
	}

	/**
	 * ���������ɾ��
	 * 
	 * @param sqls
	 *            �ű�����
	 * @return Ӱ�캯��
	 * @throws SQLException
	 * @throws ClassNotFoundException
	 */
	public Integer executeSQLBatch(String[] sqls) throws SQLException {
		Integer result = 1;
		Connection conn = null;
		PreparedStatement pst = null;
		ResultSet rs = null;
		try {
			long d1 = System.currentTimeMillis();
			log.info("����ִ��SQL������" + sqls.length);
			conn = getConnection();
			pst = (PreparedStatement) conn.createStatement();
			for (String sql : sqls) {
				log.info("����ִ��SQL��" + sql);
				pst.addBatch(sql);
			}
			int[] resultArray = pst.executeBatch();
			for (int i = 0; resultArray.length > 0 && i < resultArray.length; i++) {
				if (resultArray[i] <= 0) {
					result = 0;
				}
			}
			if (result > 0) {
				conn.commit();
			}
			log.info("����ִ��SQL��ʱ��" + (System.currentTimeMillis() - d1) + "����");

		} catch (SQLException e) {
			if (conn != null) {
				conn.rollback();
			}
			log.error("������Ϣ", e);
		} finally {
			closeAll(conn, pst, rs);
		}
		return result;

	}

	/**
	 * ִ�д洢���̵���ɾ��
	 * 
	 * @param sql
	 *            ִ�д洢���̵�SQL�ű�
	 * @param param
	 *            ע�����
	 * @return Ӱ������
	 * @throws SQLException
	 * @throws ClassNotFoundException
	 */
	public Integer executeSQLProc(String sql, String[] param) throws SQLException {
		Integer result = 0;
		Connection conn = null;
		PreparedStatement pst = null;
		ResultSet rs = null;
		CallableStatement cs = null;
		try {

			long d1 = System.currentTimeMillis();
			log.info("ִ�� �洢����SQL��" + sql);

			conn = getConnection();
			cs = (CallableStatement) conn.prepareCall(sql);
			if (param != null) {
				for (int i = 0; i < param.length; i++) {
					cs.setString(i + 1, param[i]);
				}
			}
			result = cs.executeUpdate();
			conn.commit();// �ύ����

			log.info("ִ�д洢���̺�ʱ��" + (System.currentTimeMillis() - d1) + "����");

		} catch (SQLException e) {
			if (conn != null) {
				conn.rollback();
			} // �ع�����
			log.error("������Ϣ", e);
		} finally {
			closeAll(conn, pst, rs);
		}
		return result;
	}

	/**
	 * ������תΪmap
	 * 
	 * @param sql
	 * @return
	 * @throws Exception
	 */
	public List<Map<String, String>> getDateForMapStr(String sql) {
		Connection conn = null;
		ResultSet rs = null;
		PreparedStatement pstmt = null;
		List<Map<String, String>> list = new ArrayList<Map<String, String>>();
		try {

			long d1 = System.currentTimeMillis();
			log.info("ִ�� SQL��" + sql);

			conn = getConnection();
			pstmt = conn.prepareStatement(sql);
			rs = pstmt.executeQuery();
			ResultSetMetaData md = rs.getMetaData();
			int columnCount = md.getColumnCount();
			while (rs.next()) {
				Map<String, String> rowData = new HashMap<String, String>();
				for (int i = 1; i <= columnCount; i++) {
					rowData.put(md.getColumnName(i), rs.getString(i));
				}
				list.add(rowData);
			}

			log.info("ִ�� SQL��ʱ��" + (System.currentTimeMillis() - d1) + "����");

		} catch (Exception e) {
			log.error(System.currentTimeMillis() + "getDateForMapStr������ִ��sql��" + sql);
			log.error(System.currentTimeMillis() + "�쳣ԭ��" + e.toString());
		} finally {
			closeAll(conn, pstmt, rs);
		}
		return list;
	}

	/**
	 * ������תΪmap
	 * 
	 * @param sql
	 * @return
	 */
	public List getDateForMap(String sql) {
		Connection conn = null;
		ResultSet rs = null;
		PreparedStatement pstmt = null;
		List list = new ArrayList();
		try {

			long d1 = System.currentTimeMillis();
			log.info("ִ�� SQL��" + sql);

			conn = getConnection();
			pstmt = conn.prepareStatement(sql);
			rs = pstmt.executeQuery();
			ResultSetMetaData md = rs.getMetaData();
			int columnCount = md.getColumnCount(); // Map rowData;
			while (rs.next()) { // rowData = new HashMap(columnCount);
				Map rowData = new HashMap();
				for (int i = 1; i <= columnCount; i++) {
					rowData.put(md.getColumnName(i), rs.getObject(i));
				}
				list.add(rowData);
			}
			log.info("ִ��SQL ��ʱ��" + (System.currentTimeMillis() - d1) + "����");
		} catch (Exception e) {
			log.error("������Ϣ", e);
		} finally {
			closeAll(conn, pstmt, rs);
		}
		return list;
	}

	/**
	 * ����������
	 * 
	 * @param sql
	 * @return
	 */
	public int getCount(String sql) {
		int count = 0;
		Connection conn = null;
		ResultSet rs = null;
		PreparedStatement pstmt = null;
		try {
			long d1 = System.currentTimeMillis();
			log.info("ִ�� SQL��" + sql);

			conn = getConnection();
			pstmt = conn.prepareStatement(sql);
			rs = pstmt.executeQuery();
			if (rs.next()) {
				count = rs.getInt("count");
			}
			log.info("ִ��SQL��ʱ��" + (System.currentTimeMillis() - d1) + "����");
		} catch (Exception e) {
			log.error("������Ϣ", e);
		} finally {
			closeAll(conn, pstmt, rs);
		}
		return count;
	}

	public String getDb_ssid() {
		return db_ssid;
	}

	public void setDb_ssid(String db_ssid) {
		this.db_ssid = db_ssid;
	}

	public String getDb_type() {
		return db_type;
	}

	public void setDb_type(String db_type) {
		this.db_type = db_type;
	}

	public abstract List<Map<String, Object>> getTableColumnByMeta(MateBean ownertables) ;

	public abstract List<Map<String, Object>> getTableColumn(MateBean ownertables);

	public abstract List<Map<String, Object>> getTableColumn();

}
