package org.tools;

import java.beans.Statement;
import java.io.Closeable;
import java.io.IOException;
import java.io.InputStream;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.Properties;

public class MysqlConn {

	public static void Insert(String sql) throws IOException {
		Properties prop = new Properties();
		InputStream in = MysqlConn.class.getClassLoader().getResourceAsStream("org//tools//db.properties");
		prop.load(in);
		String ip = prop.getProperty("ip");
		// System.out.println(ip);
		String username = prop.getProperty("username");
		String password = prop.getProperty("passwd");
		String database = prop.getProperty("database");
		String url = "jdbc:mysql://" + ip + ":3306/" + database + "?user=" + username + "&password=" + password
				+ "&useServerPrepStmts=false&rewriteBatchedStatements=true";
		System.out.println(url);
		Connection conn = null;
		java.sql.Statement stmt = null;
		try {
			Class.forName("com.mysql.jdbc.Driver");

			conn = DriverManager.getConnection(url);
			stmt = conn.createStatement();

			stmt.executeUpdate(sql);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	public static void Realsase(Connection conn, Statement statement) {
		try {
			if (conn != null)
				conn.close();
		} catch (SQLException e) {
			e.printStackTrace();
		}
		try {
			if (statement != null)
				conn.close();
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}

	// public static void main(String[] args) throws IOException {
	//
	// Insert("1");
	// }

}
