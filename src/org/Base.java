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

import com.exprotmeteexcel.utl.ExcelUtility;
import com.informatica.powercenter.sdk.mapfwk.connection.ConnectionInfo;
import com.informatica.powercenter.sdk.mapfwk.connection.ConnectionPropsConstants;
import com.informatica.powercenter.sdk.mapfwk.connection.SourceTargetType;
import com.informatica.powercenter.sdk.mapfwk.core.Field;
import com.informatica.powercenter.sdk.mapfwk.core.FieldKeyType;
import com.informatica.powercenter.sdk.mapfwk.core.FieldType;
import com.informatica.powercenter.sdk.mapfwk.core.Folder;
import com.informatica.powercenter.sdk.mapfwk.core.MapFwkOutputContext;
import com.informatica.powercenter.sdk.mapfwk.core.Mapping;
import com.informatica.powercenter.sdk.mapfwk.core.NativeDataTypes;
import com.informatica.powercenter.sdk.mapfwk.core.Session;
import com.informatica.powercenter.sdk.mapfwk.core.Source;
import com.informatica.powercenter.sdk.mapfwk.core.Target;
import com.informatica.powercenter.sdk.mapfwk.core.Workflow;
import com.informatica.powercenter.sdk.mapfwk.repository.RepoConnectionInfo;
import com.informatica.powercenter.sdk.mapfwk.repository.RepoPropsConstants;
import com.informatica.powercenter.sdk.mapfwk.repository.Repository;

import org.tools.ExcelUtil;

import com.exprotmeteexcel.utl.DateTran.DataTypeTrans;

/**
 * =============================================
 * 
 * @Copyright 2017上海新炬网络技术有限公司 @version：1.0.1
 * @author：Junko
 * @date：2017年7月11日上午11:06:18
 * @Description: 生成XML的父类，子类继承该父类后重写CreateMapping、Createsource、CreateTargets、CreateWorkflow、CreateSession
 *               后可以生产XML文件 =============================================
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
	protected final String TablePrefix = org.tools.GetProperties.getKeyValue("prefix");
	// public static String profilepath = "QQSURVEY.properties";
	protected ArrayList<String> BigCloumn = null;
	protected static List<List<String>> TableList = null;
	protected ArrayList<ArrayList<String>> TableConf = ExcelUtil
			.readExecl(org.tools.GetProperties.getKeyValue("ExcelPath"));
	protected final static List<String> Keyword = org.tools.RePlaceOG.OG();
	public List<String> SourcePrimaryKeyList = new ArrayList<String>();
	public List<String> TargetPrimaryKeyList = new ArrayList<String>();
	public List<String> SourcePrimaryKeyListNotIN = new ArrayList<String>();

	// protected String TableNm =
	// org.tools.GetProperties.getKeyValue("TableNm");

	/**
	 * @version: 1.0.1
	 * @author: Junko
	 * @date: 2017年7月11日上午11:09:53
	 * @Description: Common execute method
	 * @throws Exception
	 */
	public void execute() throws Exception {
		init();
		createMappings();
		createSession();
		createWorkflow();
		generateOutput();

	}

	/**
	 * @version: 1.0.1
	 * @author: Junko
	 * @date: 2017年7月11日上午11:10:14
	 * @Description: Initialize the method
	 */
	protected void init() {
		createRepository();
		createFolder();
		createSources();
		createTargets();
	}

	/**
	 * @version: 1.0.1
	 * @author: Junko
	 * @date: 2017年7月11日上午11:10:21
	 * @Description: Create a repository
	 */
	protected void createRepository() {
		rep = new Repository("dev_store_edw", "dev_store_edw", "repository");
		RepoConnectionInfo repo = new RepoConnectionInfo();
		repo.setCodepage("MS936");
		rep.setRepoConnectionInfo(repo);
	}

	/**
	 * @version: 1.0.1
	 * @author: Junko
	 * @date: 2017年7月11日上午11:11:33
	 * @Description: Creates a folder
	 */
	protected void createFolder() {
		folder = new Folder("SourceFolder", "SourceFolder", org.tools.GetProperties.getKeyValue("System"));
		rep.addFolder(folder);

	}

	/**
	 * @version: 1.0.1
	 * @author: Junko
	 * @date: 2017年7月11日上午11:11:48
	 * @Description: Create sources
	 */
	protected abstract void createSources(); // override in base class to create
												// appropriate sources

	/**
	 * @version: 1.0.1
	 * @author: Junko
	 * @date: 2017年7月11日上午11:12:00
	 * @Description: Create targets
	 */
	protected abstract void createTargets(); // override in base class to create
												// appropriate targets

	/**
	 * @version: 1.0.1
	 * @author: Junko
	 * @date: 2017年7月11日上午11:12:11
	 * @Description: Creates a mapping It needs to be overriddden for the sample
	 * @throws Exception
	 */
	protected abstract void createMappings() throws Exception; // override in
																// base class

	/**
	 * @version: 1.0.1
	 * @author: Junko
	 * @date: 2017年7月11日上午11:12:32
	 * @Description: Create session
	 * @throws Exception
	 */
	protected abstract void createSession() throws Exception;

	/**
	 * @version: 1.0.1
	 * @author: Junko
	 * @date: 2017年7月11日上午11:12:42
	 * @Description: Create workflow
	 * @throws Exception
	 */
	protected abstract void createWorkflow() throws Exception;

	protected ConnectionInfo getRelationalConnInfo(SourceTargetType dbType, String dbName) {
		ConnectionInfo connInfo = null;
		connInfo = new ConnectionInfo(dbType);
		connInfo.getConnProps().setProperty(ConnectionPropsConstants.DBNAME, dbName);
		return connInfo;
	}

	/**
	 * @version: 1.0.1
	 * @author: Junko
	 * @date: 2017年7月11日上午11:16:18
	 * @Description: 生成XML文件
	 * @throws Exception
	 */
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
	 * @version: 1.0.1
	 * @author: Junko
	 * @date: 2017年7月11日上午11:16:41
	 * @Description: Method to create relational target
	 * @param DBType
	 * @param name
	 * @return
	 */
	protected Target createRelationalTarget(SourceTargetType DBType, String name) {
		Target target = new Target(name, name, name, name, new ConnectionInfo(DBType));
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

	/**
	 * @version: 1.0.1
	 * @author: Junko
	 * @date: 2017年7月11日上午11:17:04
	 * @Description: get an FTP connection
	 * @return
	 */
	protected ConnectionInfo getFTPConnectionInfo() {
		ConnectionInfo infoProps = new ConnectionInfo(SourceTargetType.FTP);
		infoProps.setConnectionName("ftp_con");
		return infoProps;
	}

	/**
	 * @version: 1.0.1
	 * @author: Junko
	 * @date: 2017年7月11日上午11:17:17
	 * @Description: Method to get relational connection info object
	 * @param dbType
	 * @param dbName
	 * @return
	 */
	protected ConnectionInfo getRelationalConnectionInfo(SourceTargetType dbType, String dbName) {
		ConnectionInfo connInfo = new ConnectionInfo(dbType);
		connInfo.getConnProps().setProperty(ConnectionPropsConstants.DBNAME, dbName);
		return connInfo;
	}

	/**
	 * @version: 1.0.1
	 * @author: Junko
	 * @date: 2017年7月11日上午11:17:40
	 * @Description: 初始化属性文件 pcconfig.properties
	 * @throws IOException
	 */
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

	/**
	 * @version: 1.0.1
	 * @author: Junko
	 * @date: 2017年7月11日上午11:18:06
	 * @Description: 定义CK逻辑的源
	 * @param TableNm
	 * @param dbName
	 * @param DbType
	 * @return
	 */
	protected Source CheckSouce(String TableNm, String dbName, String DbType, String ogTbNM) {
		List<Field> fields = new ArrayList<Field>();

		String len = null;
		String precision = null;
		Source tabSource = null;
		List<String> a = null;
		String TableName = null;
		for (int i = 0; i < TableConf.size(); i++) {
			a = (List) TableConf.get(i);

			// TableList.add(a);
			// 过滤成源表名
			if (a.get(0).equals(ogTbNM)) {
				// TableList.add(a);
				String pattern = ".*?\\((.*?)\\).*?";
				// 鍒涘缓 Pattern 瀵硅薄
				Pattern r = Pattern.compile(pattern);

				// 鐜板湪鍒涘缓 matcher 瀵硅薄
				String TagType = a.get(6).split("\\(")[0];
				Matcher m = r.matcher(a.get(6).toString());
				if (m.find()) {
					String[] sourceStrArray = m.group(1).toString().split(",");
					// System.out.print(sourceStrArray.length);
//					if (TagType.equals("timestamp")) {
//						len = "26";
//						precision = "6";
//					} else 
						if (a.get(6).indexOf(",") > 0) {
						len = sourceStrArray[0];
						precision = sourceStrArray[1] == null ? "0" : sourceStrArray[1];
					} else {
//						len = sourceStrArray[0];
						len = "integer".equals(TagType) ? "10"
							: "bigint".equals(TagType) ? "19"
									: "timestamp".equals(TagType) ? "26"
											: "smallint".equals(TagType) ? "5": m.group(1).toString().split(",")[0];
					
					    precision = "integer".equals(TagType) ? "0"
							: "bigint".equals(TagType) ? "0"
									: "timestamp".equals(TagType) ? "6"
											: "smallint".equals(TagType) ? "0": "0";
					}
						

				}else{
					len = "integer".equals(TagType) ? "10"
							: "bigint".equals(TagType) ? "19"
									: "timestamp".equals(TagType) ? "26"
											: "smallint".equals(TagType) ? "5": m.group(1).toString().split(",")[0];
					
					precision = "integer".equals(TagType) ? "0"
							: "bigint".equals(TagType) ? "0"
									: "timestamp".equals(TagType) ? "6"
											: "smallint".equals(TagType) ? "0": "0";
				}
				

				ArrayList<String> BCloum = new ArrayList<String>(org.tools.RePlaceBigCloumn.BigCloumn());

				if (!a.get(6).equals("varchar(1234)")) {
					// String FieldNm =
					// (!Keyword.contains(a.get(1).toUpperCase().toString()) ||
					// !DbType.equals("TD")) ?
					// a.get(1).toUpperCase().toString():
					// a.get(1).toUpperCase().toString()+"_OG";
					String FieldNm = DbType.equals("TD")
							? a.get(7).toString() : a.get(1).toString();
					String DataType = (!DbType.equals("TD")) ? DataTypeTrans.TransByTd(a.get(2), DbType)
							: TagType;
					Field field = new Field(FieldNm, FieldNm, "", DataType, len, precision, FieldKeyType.NOT_A_KEY,
							FieldType.SOURCE, false);
					fields.add(field);
				}
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
		System.out.println(TableName);
		tabSource = new Source(TableName, TableName, "table", TableName, info);
		// System.out.println(a.get(0).toString());
		tabSource.setFields(fields);
		return tabSource;
	}

	/**
	 * @version: 1.0.1
	 * @author: Junko
	 * @date: 2017年7月11日上午11:18:34
	 * @Description: 定义增量表的源
	 * @param TableNm
	 * @param dbName
	 * @param DbType
	 * @return
	 */
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
			if (a.get(0).equals(TableNm)) {
				// TableList.add(a);
				String pattern = ".*?\\((.*?)\\).*?";
				// 鍒涘缓 Pattern 瀵硅薄
				Pattern r = Pattern.compile(pattern);

				// 鐜板湪鍒涘缓 matcher 瀵硅薄
				String TagType = a.get(6).split("\\(")[0];
				Matcher m = r.matcher(a.get(6).toString());
				if (m.find()) {
					String[] sourceStrArray = m.group(1).toString().split(",");
					// System.out.print(sourceStrArray.length);
//					if (TagType.equals("timestamp")) {
//						len = "26";
//						precision = "6";
//					} else 
						if (a.get(6).indexOf(",") > 0) {
						len = sourceStrArray[0];
						precision = sourceStrArray[1] == null ? "0" : sourceStrArray[1];
					} else {
//						len = sourceStrArray[0];
						len = "integer".equals(TagType) ? "10"
							: "bigint".equals(TagType) ? "19"
									: "timestamp".equals(TagType) ? "26"
											: "smallint".equals(TagType) ? "5": m.group(1).toString().split(",")[0];
					
					precision = "integer".equals(TagType) ? "0"
							: "bigint".equals(TagType) ? "0"
									: "timestamp".equals(TagType) ? "6"
											: "smallint".equals(TagType) ? "0": "0";
					}
						

				}else{
					len = "integer".equals(TagType) ? "10"
							: "bigint".equals(TagType) ? "19"
									: "timestamp".equals(TagType) ? "26"
											: "smallint".equals(TagType) ? "5": m.group(1).toString().split(",")[0];
					
					precision = "integer".equals(TagType) ? "0"
							: "bigint".equals(TagType) ? "0"
									: "timestamp".equals(TagType) ? "6"
											: "smallint".equals(TagType) ? "0": "0";
				}
				// System.out.println(a.get(2).toString().substring(0,
				// a.get(2).toString().indexOf("(")));

				// System.out.println(a.get(0).toString());
				// System.out.println(a.get(3).toString().trim().equals("PI")+
				// a.get(3).toString().trim());
				if (a.get(3).toString().trim().equals("PI") || a.get(5).toString().trim().equals("pri")) {
					ColType = FieldKeyType.PRIMARY_KEY;
					NullEable = true;
				} else {
					ColType = FieldKeyType.NOT_A_KEY;
					NullEable = false;
				}
				// NullEable = false;
				// System.out.println(a.get(1).toUpperCase().toString() + "," +
				// org.tools.DataTypeTrans.Trans(a.get(2), "MSSQL") + ""
				// + len + "," + precision);

				if (!a.get(6).equals("varchar(1234)")) {
					String FieldNm = DbType.equals("TD")
							? a.get(7).toString() : a.get(1).toString();
					String DataType = (!DbType.equals("TD")) ? DataTypeTrans.TransByTd(a.get(2), DbType)
							: TagType;

					field = new Field(FieldNm, FieldNm, "", DataType, len, precision, ColType, FieldType.SOURCE,
							NullEable);

					// com.exprotmeteexcel.utl.DateTran.DataTypeTrans.TransByTd
					fields.add(field);
				}
				//

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

	/**
	 * @version: 1.0.1
	 * @author: Junko
	 * @date: 2017年7月14日上午9:58:31
	 * @Description: 定义Upser、Append表的源
	 * @param TableNm
	 * @param dbName
	 * @param DbType
	 * @param ogTbNm
	 * @return
	 */
	protected Source CreateUpserAppendSource(String TableNm, String dbName, String DbType, String ogTbNm) {
		List<Field> fields = new ArrayList<Field>();
		String len = null;
		String precision = null;
		Source tabSource = null;
		List<String> a = null;
		String TableName = null;
		FieldKeyType ColType = null;
		Boolean NullEable = null;
		Field field = null;
		SourcePrimaryKeyList.clear();
		TargetPrimaryKeyList.clear();
		for (int i = 0; i < TableConf.size(); i++) {
			a = (List) TableConf.get(i);
			// TableList.add(a);
			if (a.get(0).equals(ogTbNm)) {
				// TableList.add(a);
				String pattern = ".*?\\((.*?)\\).*?";
				// 鍒涘缓 Pattern 瀵硅薄
				Pattern r = Pattern.compile(pattern);

				// 鐜板湪鍒涘缓 matcher 瀵硅薄
				String TagType = a.get(6).split("\\(")[0];
				Matcher m = r.matcher(a.get(6).toString());
				if (m.find()) {
					String[] sourceStrArray = m.group(1).toString().split(",");
					// System.out.print(sourceStrArray.length);
//					if (TagType.equals("timestamp")) {
//						len = "26";
//						precision = "6";
//					} else 
						if (a.get(6).indexOf(",") > 0) {
						len = sourceStrArray[0];
						precision = sourceStrArray[1] == null ? "0" : sourceStrArray[1];
					} else {
//						len = sourceStrArray[0];
						len = "integer".equals(TagType) ? "10"
							: "bigint".equals(TagType) ? "19"
									: "timestamp".equals(TagType) ? "26"
											: "smallint".equals(TagType) ? "5": m.group(1).toString().split(",")[0];
					
					precision = "integer".equals(TagType) ? "0"
							: "bigint".equals(TagType) ? "0"
									: "timestamp".equals(TagType) ? "6"
											: "smallint".equals(TagType) ? "0": "0";
					}
						

				}else{
					len = "integer".equals(TagType) ? "10"
							: "bigint".equals(TagType) ? "19"
									: "timestamp".equals(TagType) ? "26"
											: "smallint".equals(TagType) ? "5": m.group(1).toString().split(",")[0];
					
					precision = "integer".equals(TagType) ? "0"
							: "bigint".equals(TagType) ? "0"
									: "timestamp".equals(TagType) ? "6"
											: "smallint".equals(TagType) ? "0": "0";
				}
				// System.out.println(a.get(2).toString().substring(0,
				// a.get(2).toString().indexOf("(")));

				// System.out.println(a.get(0).toString());
				// System.out.println(a.get(3).toString().trim().equals("PI")+
				// a.get(3).toString().trim());
				if (a.get(3).toString().trim().equals("PI") || a.get(5).toString().trim().equals("pri")) {
					ColType = FieldKeyType.PRIMARY_KEY;
					NullEable = true;
				} else {
					ColType = FieldKeyType.NOT_A_KEY;
					NullEable = false;
				}

				// NullEable = false;
				// System.out.println(a.get(1).toUpperCase().toString() + "," +
				// org.tools.DataTypeTrans.Trans(a.get(2), "MSSQL") + ""
				// + len + "," + precision);
				String tmp = "";
				String tmp2 = "";

				if (!a.get(6).equals("varchar(1234)")) {
					String FieldNm = DbType.equals("TD")
							? a.get(7).toString() : a.get(1).toString();
					String DataType = (!DbType.equals("TD")) ? DataTypeTrans.TransByTd(a.get(2), DbType)
							: TagType;
					if (a.get(5).equals("pri")) {
						tmp = FieldNm.length() > 3
								&& FieldNm.substring(FieldNm.length() - 3, FieldNm.length()).equals("_OG") ? FieldNm
										: "IN_" + FieldNm;
						
						tmp2 = FieldNm.length() > 3
								&& FieldNm.substring(FieldNm.length() - 3, FieldNm.length()).equals("_OG") ? FieldNm
										:FieldNm;
						SourcePrimaryKeyList.add(tmp);
						SourcePrimaryKeyListNotIN.add(tmp2);
						TargetPrimaryKeyList.add(a.get(1).toString());
					}
					field = new Field(FieldNm, FieldNm, "", DataType, len, precision, ColType, FieldType.SOURCE,
							NullEable);

					// com.exprotmeteexcel.utl.DateTran.DataTypeTrans.TransByTd
					fields.add(field);
				}
				//

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

	/**
	 * @version: 1.0.1
	 * @author: Junko
	 * @date: 2017年7月11日上午11:19:14
	 * @Description: 定义拉链表的源
	 * @param TableNm
	 * @param dbName
	 * @param DbType
	 * @return
	 */
	protected Source CreateZipper(String TableNm, String dbName, String DbType, String ogTbNM) {
		List<Field> fields = new ArrayList<Field>();

		String len = null;
		String precision = null;
		Source tabSource = null;
		List<String> a = null;
		String TableName = null;
		FieldKeyType ColType = null;
		Boolean NullEable = null;
		SourcePrimaryKeyList.clear();
		TargetPrimaryKeyList.clear();
		for (int i = 0; i < TableConf.size(); i++) {
			a = (List<String>) TableConf.get(i);
			// TableList.add(a);
			if (a.get(0).equals(ogTbNM)) {

				// TableList.add(a);
				String pattern = ".*?\\((.*?)\\).*?";
				// 鍒涘缓 Pattern 瀵硅薄
				Pattern r = Pattern.compile(pattern);

				// 鐜板湪鍒涘缓 matcher 瀵硅薄
				String TagType = a.get(6).split("\\(")[0];
				Matcher m = r.matcher(a.get(6).toString());
				if (m.find()) {
					String[] sourceStrArray = m.group(1).toString().split(",");
					// System.out.print(sourceStrArray.length);
//					if (TagType.equals("timestamp")) {
//						len = "26";
//						precision = "6";
//					} else 
						if (a.get(6).indexOf(",") > 0) {
						len = sourceStrArray[0];
						precision = sourceStrArray[1] == null ? "0" : sourceStrArray[1];
					} else {
//						len = sourceStrArray[0];
						len = "integer".equals(TagType) ? "10"
							: "bigint".equals(TagType) ? "19"
									: "timestamp".equals(TagType) ? "26"
											: "smallint".equals(TagType) ? "5": m.group(1).toString().split(",")[0];
					
					precision = "integer".equals(TagType) ? "0"
							: "bigint".equals(TagType) ? "0"
									: "timestamp".equals(TagType) ? "6"
											: "smallint".equals(TagType) ? "0": "0";
					}
						

				}else{
					len = "integer".equals(TagType) ? "10"
							: "bigint".equals(TagType) ? "19"
									: "timestamp".equals(TagType) ? "26"
											: "smallint".equals(TagType) ? "5": m.group(1).toString().split(",")[0];
					
					precision = "integer".equals(TagType) ? "0"
							: "bigint".equals(TagType) ? "0"
									: "timestamp".equals(TagType) ? "6"
											: "smallint".equals(TagType) ? "0": "0";
				}

				if (a.get(3).toString().trim().equals("PI") || a.get(5).toString().trim().equals("pri")) {
					ColType = FieldKeyType.PRIMARY_KEY;
					NullEable = true;
				} else {
					ColType = FieldKeyType.NOT_A_KEY;
					NullEable = false;
				}

				String tmp = "";
				if (!a.get(6).equals("varchar(1234)")) {
					String FieldNm = DbType.equals("TD")
							? a.get(7).toString() : a.get(1).toString();
					String DataType = (!DbType.equals("TD")) ? DataTypeTrans.TransByTd(a.get(2), DbType)
							: TagType;
					if (a.get(5).equals("pri")) {
						tmp = FieldNm.length() > 3
								&& FieldNm.substring(FieldNm.length() - 3, FieldNm.length()).equals("_OG") ? FieldNm
										: "IN_" + FieldNm;
						SourcePrimaryKeyList.add(tmp);
						TargetPrimaryKeyList.add(a.get(1).toString());
					}
					Field field = new Field(FieldNm, FieldNm, "", DataType, len, precision, ColType, FieldType.SOURCE,
							NullEable);

					// com.exprotmeteexcel.utl.DateTran.DataTypeTrans.TransByTd
					fields.add(field);
				}

				TableName = TableNm;
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

	/**
	 * @version: 1.0.1
	 * @author: Junko
	 * @date: 2017年7月11日上午11:19:35
	 * @Description: main方法参数检查，0则输出XML文件，1则直接导入到PowerCenter Repository
	 */
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