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
 * SQL的dao类，创建/关闭连接，执行脚本
 * 
 * @author admin
 * 
 */

public abstract class BaseDbDaoI implements BaseDbDao {
	protected String db_url = "";// 连接字符串
	protected String db_userName = "";// 用户名
	protected String db_userPass = "";// 用户密码
	protected int db_overtime = 10;// 秒
	protected String db_ssid = "";// 数据库实例
	protected int fetchSize = 1000;
	protected String db_type = "";//
	protected int BatchSize = 5000;
	protected String DRIVER;

	private static final Logger log = LoggerFactory.getLogger(BaseDbDaoI.class);

	/**
	 * 获 jdbc连接
	 * 
	 * @param driver
	 *            ：驱动
	 * @param url
	 *            ：地址
	 * @param username
	 *            ：用户名
	 * @param password
	 *            ：密码
	 * @return 返回连接器
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
	 * 关闭连接
	 * 
	 * @param conn
	 *            ：连接器
	 * @param stmt
	 *            ：数据库语句发送对象
	 * @param rs
	 *            ：结果集
	 */
	public static void closeAll(Connection conn, Statement stmt, ResultSet rs) {
		try {
			if (rs != null) {
				rs.close();
			}
		} catch (Exception e) {
			log.error("错误信息", e);
		}
		try {
			if (stmt != null) {
				stmt.close();
			}
		} catch (Exception e) {
			log.error("错误信息", e);
		}
		try {
			if (conn != null && !conn.isClosed()) {
				conn.close();
			}
		} catch (Exception e) {
			log.error("错误信息", e);
		}
	}

	/**
	 * 用于执行 INSERT、UPDATE 或 DELETE 语句
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
			log.info("执行SQL：" + sql);
			conn = getConnection();
			ps = conn.prepareStatement(sql);
			ps.setFetchSize(fetchSize);
			if (param != null) {
				for (int i = 0; i < param.length; i++) {
					ps.setString(i + 1, param[i]);
				}
			}
			rs = ps.execute();
			log.info("执行SQL耗时：" + (System.currentTimeMillis() - d1) + "毫秒");

		} catch (SQLException e) {
			log.error("错误信息", e);
		} finally {
			closeAll(conn, ps, null);
		}
		return rs;
	}

	/**
	 * 用于执行 INSERT、UPDATE 或 DELETE 语句
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
			log.info("执行SQL：" + sql);
			ps = conn.prepareStatement(sql);
			ps.setFetchSize(fetchSize);
			if (param != null) {
				for (int i = 0; i < param.length; i++) {
					ps.setString(i + 1, param[i]);
				}
			}
			rs = ps.execute();
			log.info("执行SQL耗时：" + (System.currentTimeMillis() - d1) + "毫秒");

		} catch (SQLException e) {
			log.error("错误信息", e);
		} finally {
			closeAll(conn, ps, null);
		}
		return rs;
	}

	/**
	 * 用于执行 INSERT、UPDATE 或 DELETE 语句以及 SQL DDL（数据定义语言）语句
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
			log.info("执行SQL：" + sql);
			conn = getConnection();
			ps = conn.prepareStatement(sql);
			ps.setFetchSize(fetchSize);
			if (param != null) {
				for (int i = 0; i < param.length; i++) {
					ps.setString(i + 1, param[i]);
				}
			}
			rs = ps.executeUpdate();
			log.info("执行SQL耗时：" + (System.currentTimeMillis() - d1) + "毫秒");
			return rs;

		} catch (SQLException e) {
			log.error("错误信息", e);
		} finally {
			closeAll(conn, ps, null);
		}
		return rs;
	}

	/**
	 * 普通sql查询
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
			log.info("执行SQL：" + sql);

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

			log.info("执行SQL耗时：" + (System.currentTimeMillis() - d1) + "毫秒");

		} catch (SQLException e) {
			log.error("错误信息", e);
		} finally {
			closeAll(conn, ps, rs);
		}
		return results;
	}

	/**
	 * 普通sql查询
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
			log.info("执行SQL：" + sql);

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

			log.info("执行SQL耗时：" + (System.currentTimeMillis() - d1) + "毫秒");

		} catch (SQLException e) {
			log.error("错误信息", e);
		} finally {
			closeAll(null, ps, rs);
		}

		return results;
	}

	/**
	 * 执行sql语句的增删改
	 * 
	 * @param sql
	 *            SQL脚本
	 * @param param
	 *            注入参数
	 * @return 影响函数
	 * @throws SQLException
	 */
	public Integer executeSQL(String sql, String[] param) throws SQLException {
		Integer result = 0;
		Connection conn = null;
		PreparedStatement pst = null;
		ResultSet rs = null;
		try {
			long d1 = System.currentTimeMillis();
			log.info("执行SQL：" + sql);

			conn = getConnection();
			pst = (PreparedStatement) conn.prepareStatement(sql);
			if (param != null) {
				for (int i = 0; i < param.length; i++) {
					pst.setString(i + 1, param[i]);
				}
			}
			result = pst.executeUpdate();
			conn.commit();// 提交事务

			log.info("执行SQL 时间：" + (System.currentTimeMillis() - d1) + "毫秒");

		} catch (SQLException e) {
			if (conn != null) {
				conn.rollback();
			} // 回滚事务
			log.error("执行sql发生异常：" + e.toString());
		} finally {
			closeAll(conn, pst, rs);
		}
		return result;
	}

	/**
	 * 批处理的增删改
	 * 
	 * @param sqls
	 *            脚本数组
	 * @return 影响函数
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
			log.info("批量执行SQL总数：" + sqls.length);
			conn = getConnection();
			pst = (PreparedStatement) conn.createStatement();
			for (String sql : sqls) {
				log.info("批量执行SQL：" + sql);
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
			log.info("批量执行SQL耗时：" + (System.currentTimeMillis() - d1) + "毫秒");

		} catch (SQLException e) {
			if (conn != null) {
				conn.rollback();
			}
			log.error("错误信息", e);
		} finally {
			closeAll(conn, pst, rs);
		}
		return result;

	}

	/**
	 * 执行存储过程的增删改
	 * 
	 * @param sql
	 *            执行存储过程的SQL脚本
	 * @param param
	 *            注入参数
	 * @return 影响行数
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
			log.info("执行 存储过程SQL：" + sql);

			conn = getConnection();
			cs = (CallableStatement) conn.prepareCall(sql);
			if (param != null) {
				for (int i = 0; i < param.length; i++) {
					cs.setString(i + 1, param[i]);
				}
			}
			result = cs.executeUpdate();
			conn.commit();// 提交事务

			log.info("执行存储过程耗时：" + (System.currentTimeMillis() - d1) + "毫秒");

		} catch (SQLException e) {
			if (conn != null) {
				conn.rollback();
			} // 回滚事务
			log.error("错误信息", e);
		} finally {
			closeAll(conn, pst, rs);
		}
		return result;
	}

	/**
	 * 获数据转为map
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
			log.info("执行 SQL：" + sql);

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

			log.info("执行 SQL耗时：" + (System.currentTimeMillis() - d1) + "毫秒");

		} catch (Exception e) {
			log.error(System.currentTimeMillis() + "getDateForMapStr方法，执行sql：" + sql);
			log.error(System.currentTimeMillis() + "异常原因：" + e.toString());
		} finally {
			closeAll(conn, pstmt, rs);
		}
		return list;
	}

	/**
	 * 获数据转为map
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
			log.info("执行 SQL：" + sql);

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
			log.info("执行SQL 耗时：" + (System.currentTimeMillis() - d1) + "毫秒");
		} catch (Exception e) {
			log.error("错误信息", e);
		} finally {
			closeAll(conn, pstmt, rs);
		}
		return list;
	}

	/**
	 * 获数据数量
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
			log.info("执行 SQL：" + sql);

			conn = getConnection();
			pstmt = conn.prepareStatement(sql);
			rs = pstmt.executeQuery();
			if (rs.next()) {
				count = rs.getInt("count");
			}
			log.info("执行SQL耗时：" + (System.currentTimeMillis() - d1) + "毫秒");
		} catch (Exception e) {
			log.error("错误信息", e);
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
