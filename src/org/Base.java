package org;
/*
 * Base.java Created on Nov 4, 2005.
 *
 * Copyright 2004 Informatica Corporation. All rights reserved.
 * INFORMATICA PROPRIETARY/CONFIDENTIAL. Use is subject to license terms.
 */

import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;
import java.util.Properties;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import com.informatica.powercenter.sdk.mapfwk.connection.ConnectionInfo;
import com.informatica.powercenter.sdk.mapfwk.connection.ConnectionPropsConstants;
import com.informatica.powercenter.sdk.mapfwk.connection.SourceTargetType;
import com.informatica.powercenter.sdk.mapfwk.core.Field;
import com.informatica.powercenter.sdk.mapfwk.core.FieldKeyType;
import com.informatica.powercenter.sdk.mapfwk.core.FieldType;
import com.informatica.powercenter.sdk.mapfwk.core.Folder;
import com.informatica.powercenter.sdk.mapfwk.core.INameFilter;
import com.informatica.powercenter.sdk.mapfwk.core.MapFwkOutputContext;
import com.informatica.powercenter.sdk.mapfwk.core.Mapping;
import com.informatica.powercenter.sdk.mapfwk.core.NativeDataTypes;
import com.informatica.powercenter.sdk.mapfwk.core.SASHelper;
import com.informatica.powercenter.sdk.mapfwk.core.Session;
import com.informatica.powercenter.sdk.mapfwk.core.Source;
import com.informatica.powercenter.sdk.mapfwk.core.StringConstants;
import com.informatica.powercenter.sdk.mapfwk.core.Target;
import com.informatica.powercenter.sdk.mapfwk.core.Workflow;
import com.informatica.powercenter.sdk.mapfwk.exception.MapFwkReaderException;
import com.informatica.powercenter.sdk.mapfwk.exception.RepoOperationException;
import com.informatica.powercenter.sdk.mapfwk.repository.RepoConnectionInfo;
import com.informatica.powercenter.sdk.mapfwk.repository.RepoPropsConstants;
import com.informatica.powercenter.sdk.mapfwk.repository.Repository;

import org.tools.ExcelUtil;

/**
 *
 *
 */
public abstract class Base {
	// ///////////////////////////////////////////////////////////////////////////////////
	// Instance variables
	// ///////////////////////////////////////////////////////////////////////////////////
	protected Repository rep;
	protected Folder folder;
	protected Session session;
	protected Workflow workflow;
	protected String mapFileName;
	protected String TableNme;
	protected Mapping mapping;
	protected int runMode = 0;
	// public static String profilepath = "QQSURVEY.properties";
	protected ArrayList<String> BigCloumn = null;
	protected static List<List<String>> TableList = null;
	protected ArrayList<ArrayList<String>> TableConf = ExcelUtil
			.readExecl(org.tools.GetProperties.getKeyValue("ExcelPath"));
	// protected String TableNm =
	// org.tools.GetProperties.getKeyValue("TableNm");

	/**
	 * Common execute method
	 */
	public void execute() throws Exception {
		init();
		createMappings();
		createSession();
		createWorkflow();
		generateOutput();

	}

	/**
	 * Initialize the method
	 */
	protected void init() {
		createRepository();
		createFolder();
		createSources();
		createTargets();
	}

	/**
	 * Create a repository
	 */
	protected void createRepository() {
		rep = new Repository("dev_store_edw", "dev_store_edw", "This repository contains API test samples");
		RepoConnectionInfo repo = new RepoConnectionInfo();
		repo.setCodepage("MS936");
		rep.setRepoConnectionInfo(repo);
	}

	/**
	 * Creates a folder
	 */
	protected void createFolder() {
		folder = new Folder("WECHAT", "WECHAT", org.tools.GetProperties.getKeyValue("System"));
		rep.addFolder(folder);

	}

	/**
	 * Create sources
	 */
	protected abstract void createSources(); // override in base class to create
												// appropriate sources

	/**
	 * Create targets
	 */
	protected abstract void createTargets(); // override in base class to create
												// appropriate targets

	/**
	 * Creates a mapping It needs to be overriddden for the sample
	 * 
	 * @return Mapping
	 */
	protected abstract void createMappings() throws Exception; // override in
																// base class

	/**
	 * Create session
	 */
	protected abstract void createSession() throws Exception;

	/**
	 * Create workflow
	 */
	protected abstract void createWorkflow() throws Exception;



	protected ConnectionInfo getRelationalConnInfo(SourceTargetType dbType, String dbName) {
		ConnectionInfo connInfo = null;
		connInfo = new ConnectionInfo(dbType);
		connInfo.getConnProps().setProperty(ConnectionPropsConstants.DBNAME, dbName);
		return connInfo;
	}

	
	public void generateOutput() throws Exception {

		MapFwkOutputContext outputContext = new MapFwkOutputContext(MapFwkOutputContext.OUTPUT_FORMAT_XML,
				MapFwkOutputContext.OUTPUT_TARGET_FILE, mapFileName);

		try {
			intializeLocalProps();
		} catch (IOException ioExcp) {
			System.err.println(ioExcp.getMessage());
			System.err.println("Error reading pcconfig.properties file.");
			System.err.println("pcconfig.properties file is present in \\javamappingsdk\\samples directory");
			System.exit(0);
		}

		boolean doImport = false;
		if (runMode == 1)
			doImport = true;
		rep.save(outputContext, doImport);
		System.out.println("Mapping generated in " + mapFileName);
		
	}
	
	/**
	 * Method to create relational target
	 */
	protected Target createRelationalTarget(SourceTargetType DBType, String name) {
		Target target = new Target(name, name, name, name, new ConnectionInfo(
				DBType));
		return target;
	}

	protected void setMapFileName(Mapping mapping) {
		StringBuffer buff = new StringBuffer();
		buff.append(System.getProperty("user.dir") + "\\generaXml");
		buff.append(java.io.File.separatorChar);
		buff.append(mapping.getName());
		buff.append(".xml");
		mapFileName = buff.toString();
	}

	protected void addFieldsToSource(Source src, List<Field> fields) {
		int size = fields.size();
		for (int i = 0; i < size; i++) {
			src.addField((Field) fields.get(i));
		}
	}

	protected ConnectionInfo getFlatFileConnectionInfo() {

		ConnectionInfo infoProps = new ConnectionInfo(SourceTargetType.Flat_File);
		infoProps.getConnProps().setProperty(ConnectionPropsConstants.FLATFILE_SKIPROWS, "1");
		infoProps.getConnProps().setProperty(ConnectionPropsConstants.FLATFILE_DELIMITERS, ";");
		infoProps.getConnProps().setProperty(ConnectionPropsConstants.DATETIME_FORMAT, "A  21 yyyy/mm/dd hh24:mi:ss");
		infoProps.getConnProps().setProperty(ConnectionPropsConstants.FLATFILE_QUOTE_CHARACTER, "DOUBLE");

		return infoProps;

	}

	protected ConnectionInfo getNetezzaConnectionInfo() {

		ConnectionInfo infoProps = new ConnectionInfo(SourceTargetType.Netezza);
		return infoProps;

	}

	protected ConnectionInfo getRelationalConnectionInfo(SourceTargetType dbType) {
		ConnectionInfo infoProps = new ConnectionInfo(dbType);
		infoProps.getConnProps().setProperty(ConnectionPropsConstants.CONNECTIONNAME, "PS4339");
		return infoProps;
	}

	// get an FTP connection
	protected ConnectionInfo getFTPConnectionInfo() {
		ConnectionInfo infoProps = new ConnectionInfo(SourceTargetType.FTP);
		infoProps.setConnectionName("ftp_con");
		return infoProps;
	}

	/**
	 * Method to get relational connection info object
	 */
	protected ConnectionInfo getRelationalConnectionInfo(SourceTargetType dbType, String dbName) {
		ConnectionInfo connInfo = new ConnectionInfo(dbType);
		connInfo.getConnProps().setProperty(ConnectionPropsConstants.DBNAME, dbName);
		return connInfo;
	}

	protected void intializeLocalProps() throws IOException {

		Properties properties = new Properties();
		String filename = "pcconfig.properties";
		InputStream propStream = getClass().getClassLoader().getResourceAsStream(filename);

		if (propStream != null) {
			properties.load(propStream);
			rep.getRepoConnectionInfo()
					.setPcClientInstallPath(properties.getProperty(RepoPropsConstants.PC_CLIENT_INSTALL_PATH));
			rep.getRepoConnectionInfo()
					.setPcServerInstallPath(properties.getProperty(RepoPropsConstants.PC_SERVER_INSTALL_PATH));
			rep.getRepoConnectionInfo()
					.setTargetFolderName(properties.getProperty(RepoPropsConstants.TARGET_FOLDER_NAME));
			rep.getRepoConnectionInfo().setTargetRepoName(properties.getProperty(RepoPropsConstants.TARGET_REPO_NAME));
			rep.getRepoConnectionInfo().setRepoServerHost(properties.getProperty(RepoPropsConstants.REPO_SERVER_HOST));
			rep.getRepoConnectionInfo().setAdminPassword(properties.getProperty(RepoPropsConstants.ADMIN_PASSWORD));
			rep.getRepoConnectionInfo().setAdminUsername(properties.getProperty(RepoPropsConstants.ADMIN_USERNAME));
			rep.getRepoConnectionInfo().setRepoServerPort(properties.getProperty(RepoPropsConstants.REPO_SERVER_PORT));
			rep.getRepoConnectionInfo().setServerPort(properties.getProperty(RepoPropsConstants.SERVER_PORT));
			rep.getRepoConnectionInfo().setDatabaseType(properties.getProperty(RepoPropsConstants.DATABASETYPE));
			rep.getRepoConnectionInfo().setSecurityDomain(properties.getProperty(RepoPropsConstants.SECURITY_DOMAIN));
			rep.getRepoConnectionInfo()
					.setKerberosMode(Boolean.parseBoolean(properties.getProperty(RepoPropsConstants.KERBEROS_ENABLED)));

			if (properties.getProperty(RepoPropsConstants.PMREP_CACHE_FOLDER) != null)
				rep.getRepoConnectionInfo()
						.setPmrepCacheFolder(properties.getProperty(RepoPropsConstants.PMREP_CACHE_FOLDER));
		} else {
			throw new IOException(
					"pcconfig.properties file not found.Add Directory containing pcconfig.properties to ClassPath");
		}
	}

	protected Source CheckSouce(String TableNm, String dbName, String DbType) {
		List<Field> fields = new ArrayList<Field>();

		String len = null;
		String precision = null;
		Source tabSource = null;
		List<String> a = null;
		String TableName = null;
		for (int i = 0; i < TableConf.size(); i++) {
			a = (List) TableConf.get(i);
			// TableList.add(a);
			//���˳�Դ����
			if (a.get(0).equals(TableNm.replace("O_" + org.tools.GetProperties.getKeyValue("System") + "_", "")
					.replace("_CK", "").replace("_H", ""))) {
				// TableList.add(a);
				String pattern = ".*?\\((.*?)\\).*?";
				// 创建 Pattern 对象
				Pattern r = Pattern.compile(pattern);

				// 现在创建 matcher 对象
				Matcher m = r.matcher(a.get(2).toString());
				if (m.find()) {
					String[] sourceStrArray = m.group(1).toString().split(",");
					// System.out.print(sourceStrArray.length);
					if (org.tools.DataTypeTrans.Trans(a.get(2), DbType) == "timestamp") {
						len = "26";
						precision = "6";
					} else if (sourceStrArray.length == 2) {
						len = sourceStrArray[0];
						precision = sourceStrArray[1];
					} else {
						len = sourceStrArray[0];
						precision = "0";
					}
				}
			
				Field field = new Field(a.get(1).toString(), a.get(1).toString(), "",
						org.tools.DataTypeTrans.Trans(a.get(2), DbType), len, precision, FieldKeyType.NOT_A_KEY,
						FieldType.SOURCE, false);
				fields.add(field);
				TableName = TableNm;
			}
		}

		ConnectionInfo info = null;
		if (DbType.equals("Oracle")) {
			info = getRelationalConnInfo(SourceTargetType.Oracle, dbName);

		} else if (DbType.equals("TD")) {
			info = getRelationalConnInfo(SourceTargetType.Teradata, dbName);
		} else if (DbType.equals("MSSQL")) {
			info = getRelationalConnInfo(SourceTargetType.Microsoft_SQL_Server, dbName);
		} else if (DbType.equals("Mysql")) {
			info = getRelationalConnInfo(SourceTargetType.ODBC, dbName);
		}
		tabSource = new Source(TableName, TableName, "table", TableName, info);
		// System.out.println(a.get(0).toString());
		tabSource.setFields(fields);
		return tabSource;
	}

	protected Source CreateCrm(String TableNm, String dbName, String DbType) {
		List<Field> fields = new ArrayList<Field>();
		org.tools.GetProperties.writeProperties("ISBigCloumn", "");
		String len = null;
		String precision = null;
		Source tabSource = null;
		List<String> a = null;
		String TableName = null;
		FieldKeyType ColType = null;
		Boolean NullEable = null;
		Field field = null;
		
		for (int i = 0; i < TableConf.size(); i++) {
			a = (List) TableConf.get(i);
			// TableList.add(a);
			if (a.get(0).equals(TableNm.replace("O_" + org.tools.GetProperties.getKeyValue("System") + "_", "")
					.replace("_CK", ""))) {
				// TableList.add(a);
				String pattern = ".*?\\((.*?)\\).*?";
				// 创建 Pattern 对象
				Pattern r = Pattern.compile(pattern);

				// 现在创建 matcher 对象
				Matcher m = r.matcher(a.get(2).toString());
				if (m.find()) {
					String[] sourceStrArray = m.group(1).toString().split(",");
					// System.out.print(sourceStrArray.length);
					if (org.tools.DataTypeTrans.Trans(a.get(2), DbType) == "timestamp") {
						len = "26";
						precision = "6";
					} else if (sourceStrArray.length == 2) {
						len = sourceStrArray[0];
						precision = sourceStrArray[1];
					} else {
						len = sourceStrArray[0];
						precision = "0";
					}
				}
				// System.out.println(a.get(2).toString().substring(0,
				// a.get(2).toString().indexOf("(")));

				// System.out.println(a.get(0).toString());
				// System.out.println(a.get(3).toString().trim().equals("PI")+
				// a.get(3).toString().trim());
				if (a.get(3).toString().trim().equals("PI")
						|| a.get(1).toString().trim().equals(org.tools.GetProperties.getKeyValue("IDColunmNM"))) {
					ColType = FieldKeyType.PRIMARY_KEY;
					NullEable = true;
				} else {
					ColType = FieldKeyType.NOT_A_KEY;
					NullEable = false;
				}
				// NullEable = false;
				// System.out.println(a.get(1).toString() + "," +
				// org.tools.DataTypeTrans.Trans(a.get(2), "MSSQL") + ""
				// + len + "," + precision);
				ArrayList<String> BCloum = new ArrayList<String>(org.tools.RePlaceBigCloumn.BigCloumn());
				if (!BCloum.contains(a.get(2).toUpperCase().substring(0, a.get(2).toString().indexOf("(")))) {
                    
					 field = new Field(a.get(1).toString(), a.get(1).toString(), "",
							org.tools.DataTypeTrans.Trans(a.get(2), DbType), len, precision, ColType, FieldType.SOURCE,
							NullEable);
					
				} else {
					org.tools.GetProperties.writeProperties("ISBigCloumn", "_LARGE");
					TableNme = "_LARGE";
					System.out.println(a.get(2).toUpperCase().substring(0, a.get(2).toString().indexOf("(")));
				}
				// Field OWNER=new
				// Field("OWNER","OWNER","",NativeDataTypes.Oracle.VARCHAR2,"30","0",FieldKeyType.NOT_A_KEY,FieldType.SOURCE,false);

				fields.add(field);
				TableName = TableNm;
			}
		}

		ConnectionInfo info = null;
		if (DbType.equals("Oracle")) {
			info = getRelationalConnInfo(SourceTargetType.Oracle, dbName);

		} else if (DbType.equals("TD")) {
			info = getRelationalConnInfo(SourceTargetType.Teradata, dbName);
		} else if (DbType.equals("MSSQL")) {
			info = getRelationalConnInfo(SourceTargetType.Microsoft_SQL_Server, dbName);
		} else if (DbType.equals("Mysql")) {
			info = getRelationalConnInfo(SourceTargetType.ODBC, dbName);
		}
		tabSource = new Source(TableName, TableName, "table", TableName, info);
		// System.out.println(a.get(0).toString());
		tabSource.setFields(fields);
		return tabSource;
	}

	protected Source CreateZipper(String TableNm, String dbName, String DbType) {
		List<Field> fields = new ArrayList<Field>();

		String len = null;
		String precision = null;
		Source tabSource = null;
		List<String> a = null;
		String TableName = null;
		FieldKeyType ColType = null;
		Boolean NullEable = null;
		for (int i = 0; i < TableConf.size(); i++) {
			a = (List<String>) TableConf.get(i);
			// TableList.add(a);
			if (a.get(0).equals(TableNm.replace("O_" + org.tools.GetProperties.getKeyValue("System") + "_", "")
					.replace("_H", ""))) {
				// TableList.add(a);
				String pattern = ".*?\\((.*?)\\).*?";
				// 创建 Pattern 对象
				Pattern r = Pattern.compile(pattern);

				// 现在创建 matcher 对象
				Matcher m = r.matcher(a.get(2).toString());
				if (m.find()) {
					String[] sourceStrArray = m.group(1).toString().split(",");
					// System.out.print(sourceStrArray.length);
					if (org.tools.DataTypeTrans.Trans(a.get(2), DbType) == "timestamp") {
						len = "26";
						precision = "6";
					} else if (sourceStrArray.length == 2) {
						len = sourceStrArray[0];
						precision = sourceStrArray[1];
					} else {
						len = sourceStrArray[0];
						precision = "0";
					}
				}
				// System.out.println(a.get(2).toString().substring(0,
				// a.get(2).toString().indexOf("(")));

				// System.out.println(a.get(0).toString());
				// System.out.println(a.get(3).toString().trim().equals("PI")+
				// a.get(3).toString().trim());
				if (a.get(3).toString().trim().equals("PI") || a.get(1).toString().trim().equals("ID")) {
					ColType = FieldKeyType.PRIMARY_KEY;
					NullEable = true;
				} else {
					ColType = FieldKeyType.NOT_A_KEY;
					NullEable = false;
				}

				Field field = new Field(a.get(1).toString(), a.get(1).toString(), "",
						org.tools.DataTypeTrans.Trans(a.get(2), DbType), len, precision, ColType, FieldType.SOURCE,
						NullEable);

				// Field OWNER=new
				// Field("OWNER","OWNER","",NativeDataTypes.Oracle.VARCHAR2,"30","0",FieldKeyType.NOT_A_KEY,FieldType.SOURCE,false);

				fields.add(field);
				TableName = TableNm;
				// System.out.println(DbType);
			}

		}
		if (DbType.equals("TD")) {
			Field field = new Field("DW_START_DT", "DW_START_DT", "", NativeDataTypes.Teradata.DATE, "10", "0",
					FieldKeyType.PRIMARY_KEY, FieldType.SOURCE, true);
			fields.add(field);

		}

		ConnectionInfo info = null;
		if (DbType.equals("Oracle")) {
			info = getRelationalConnInfo(SourceTargetType.Oracle, dbName);

		} else if (DbType.equals("TD")) {
			info = getRelationalConnInfo(SourceTargetType.Teradata, dbName);
		} else if (DbType.equals("MSSQL")) {
			info = getRelationalConnInfo(SourceTargetType.Microsoft_SQL_Server, dbName);
		} else if (DbType.equals("Mysql")) {
			info = getRelationalConnInfo(SourceTargetType.ODBC, dbName);
		}
		tabSource = new Source(TableName, TableName, "table", TableName, info);
		// System.out.println(a.get(0).toString());
		tabSource.setFields(fields);
		return tabSource;
	}

	public boolean validateRunMode(String value) {
		int val = Integer.parseInt(value);
		if (val > 1 || val < 0) {
			printUsage();
			return false;
		} else {
			runMode = val;
			return true;
		}
	}

	public void printUsage() {
		String errorMsg = "***************** USAGE *************************\n";
		errorMsg += "\n";
		errorMsg += "Valid arguments are:\n";
		errorMsg += "0       => Generate powermart xml file\n";
		errorMsg += "1       => Import powermart xml file into PowerCenter Repository\n";
		errorMsg += "\n";
		errorMsg += "********************************************************\n";
		System.out.println(errorMsg);
	}

}