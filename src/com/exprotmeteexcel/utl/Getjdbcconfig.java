package com.exprotmeteexcel.utl;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

public class Getjdbcconfig {

	private String ip;
	private String port;
	private String username;
	private String password;
	private String databasename;
	private String dbtype;

	public void setDatabasename() {

		this.ip = getIp();
		this.port = getPort();
		this.username = getUsername();
		this.password = getPassword();
		this.databasename = getDatabasename();
		this.dbtype = getDbtype();
	}

	public Getjdbcconfig(String dbpath) {
		InputStream inputStream = getpathStream(dbpath);
		Properties p = getJDBCProperties(inputStream);
		this.ip = p.getProperty("ip");
		this.port = p.getProperty("port");
		this.username = p.getProperty("username");
		this.password = p.getProperty("password");
		this.databasename = p.getProperty("databasename");
		this.dbtype = p.getProperty("dbtype");

	};

	Properties getJDBCProperties(InputStream inputStream) {

		Properties p = new Properties();
		try {
			p.load(inputStream);
		} catch (IOException e1) {
			e1.printStackTrace();
		}
		return p;

	};

	public String getIp() {
		return ip;
	}

	public void setIp(String ip) {
		this.ip = ip;
	}

	public String getPort() {
		return port;
	}

	public void setPort(String port) {
		this.port = port;
	}

	public String getUsername() {
		return username;
	}

	public void setUsername(String username) {
		this.username = username;
	}

	public String getPassword() {
		return password;
	}

	public void setPassword(String password) {
		this.password = password;
	}

	public String getDatabasename() {
		return databasename;
	}

	public void setDatabasename(String databasename) {
		this.databasename = databasename;
	}

	public String getDbtype() {
		return dbtype;
	}

	public void setDbtype(String dbtype) {
		this.dbtype = dbtype;
	}

	InputStream getpathStream(String dbpath) {
		InputStream is = null;
		try {
			is = new FileInputStream(dbpath);
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		}

		return is;
	};

}
