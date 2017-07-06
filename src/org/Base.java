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

	/**
	 * Create source for Employee Source
	 */
	protected Source createEmployeeSource() {
		List<Field> fields = new ArrayList<Field>();
		Field field1 = new Field("EmployeeID", "EmployeeID", "", NativeDataTypes.FlatFile.INT, "10", "0",
				FieldKeyType.PRIMARY_KEY, FieldType.SOURCE, true);
		fields.add(field1);

		Field field2 = new Field("LastName", "LastName", "", NativeDataTypes.FlatFile.STRING, "20", "0",
				FieldKeyType.NOT_A_KEY, FieldType.SOURCE, false);
		fields.add(field2);

		Field field3 = new Field("FirstName", "FirstName", "", NativeDataTypes.FlatFile.STRING, "10", "0",
				FieldKeyType.NOT_A_KEY, FieldType.SOURCE, false);
		fields.add(field3);

		Field field4 = new Field("Title", "Title", "", NativeDataTypes.FlatFile.STRING, "30", "0",
				FieldKeyType.NOT_A_KEY, FieldType.SOURCE, false);
		fields.add(field4);

		Field field5 = new Field("TitleOfCourtesy", "TitleOfCourtesy", "", NativeDataTypes.FlatFile.STRING, "25", "0",
				FieldKeyType.NOT_A_KEY, FieldType.SOURCE, false);
		fields.add(field5);

		Field field6 = new Field("BirthDate", "BirthDate", "", NativeDataTypes.FlatFile.DATETIME, "19", "0",
				FieldKeyType.NOT_A_KEY, FieldType.SOURCE, false);
		fields.add(field6);

		Field field7 = new Field("HireDate", "HireDate", "", NativeDataTypes.FlatFile.DATETIME, "19", "0",
				FieldKeyType.NOT_A_KEY, FieldType.SOURCE, false);
		fields.add(field7);

		Field field8 = new Field("Address", "Address", "", NativeDataTypes.FlatFile.STRING, "60", "0",
				FieldKeyType.NOT_A_KEY, FieldType.SOURCE, false);
		fields.add(field8);

		Field field9 = new Field("City", "City", "", NativeDataTypes.FlatFile.STRING, "15", "0", FieldKeyType.NOT_A_KEY,
				FieldType.SOURCE, false);
		fields.add(field9);

		Field field10 = new Field("Region", "Region", "", NativeDataTypes.FlatFile.STRING, "15", "0",
				FieldKeyType.NOT_A_KEY, FieldType.SOURCE, false);
		fields.add(field10);

		Field field11 = new Field("PostalCode", "PostalCode", "", NativeDataTypes.FlatFile.STRING, "10", "0",
				FieldKeyType.NOT_A_KEY, FieldType.SOURCE, false);
		fields.add(field11);

		Field field12 = new Field("Country", "Country", "", NativeDataTypes.FlatFile.STRING, "15", "0",
				FieldKeyType.NOT_A_KEY, FieldType.SOURCE, false);
		fields.add(field12);

		Field field13 = new Field("HomePhone", "HomePhone", "", NativeDataTypes.FlatFile.STRING, "24", "0",
				FieldKeyType.NOT_A_KEY, FieldType.SOURCE, false);
		fields.add(field13);

		Field field14 = new Field("Extension", "Extension", "", NativeDataTypes.FlatFile.STRING, "4", "0",
				FieldKeyType.NOT_A_KEY, FieldType.SOURCE, false);
		fields.add(field14);

		Field field15 = new Field("Notes", "Notes", "", NativeDataTypes.FlatFile.STRING, "350", "0",
				FieldKeyType.NOT_A_KEY, FieldType.SOURCE, false);
		fields.add(field15);

		Field field16 = new Field("ReportsTo", "ReportsTo", "", NativeDataTypes.FlatFile.INT, "10", "0",
				FieldKeyType.NOT_A_KEY, FieldType.SOURCE, false);
		fields.add(field16);

		ConnectionInfo info = getFlatFileConnectionInfo();
		info.getConnProps().setProperty(ConnectionPropsConstants.SOURCE_FILENAME, "Employees.csv");
		Source employeeSource = new Source("Employee", "Employee", "This is Employee Table", "Employee", info);
		employeeSource.setFields(fields);
		return employeeSource;
	}

	protected Source createNetezzaSource() {
		List<Field> fields = new ArrayList<Field>();
		Field field1 = new Field("EmployeeID", "EmployeeID", "", NativeDataTypes.Netezza.CHAR, "10", "0",
				FieldKeyType.PRIMARY_KEY, FieldType.SOURCE, true);
		fields.add(field1);

		Field field2 = new Field("LastName", "LastName", "", NativeDataTypes.Netezza.CHAR, "20", "0",
				FieldKeyType.NOT_A_KEY, FieldType.SOURCE, false);
		fields.add(field2);

		Field field3 = new Field("FirstName", "FirstName", "", NativeDataTypes.Netezza.CHAR, "10", "0",
				FieldKeyType.NOT_A_KEY, FieldType.SOURCE, false);
		fields.add(field3);

		Field field4 = new Field("Title", "Title", "", NativeDataTypes.Netezza.CHAR, "30", "0", FieldKeyType.NOT_A_KEY,
				FieldType.SOURCE, false);
		fields.add(field4);

		ConnectionInfo info = getNetezzaConnectionInfo();

		Source employeeSource = new Source("Netezza_Employee", "Netezza_Employee", "This is Employee Table",
				"Netezza_Employee", info);
		employeeSource.setFields(fields);
		return employeeSource;
	}

	/**
	 * Create source for Company
	 */
	protected Source createMaraSource() {
		List<Field> fields = new ArrayList<Field>();
		Field field1 = new Field("MANDT", "MANDT", "MANDT", NativeDataTypes.FlatFile.STRING, "10", "0",
				FieldKeyType.NOT_A_KEY, FieldType.SOURCE, false);
		fields.add(field1);
		Field field2 = new Field("MATNR", "MATNR", "MATNR", NativeDataTypes.FlatFile.STRING, "10", "0",
				FieldKeyType.NOT_A_KEY, FieldType.SOURCE, false);
		fields.add(field2);
		Field field3 = new Field("BUKRS", "BUKRS", "BUKRS", NativeDataTypes.FlatFile.STRING, "20", "0",
				FieldKeyType.NOT_A_KEY, FieldType.SOURCE, false);
		fields.add(field3);

		Field field4 = new Field("MAKTX", "MAKTX", "MAKTX", NativeDataTypes.FlatFile.STRING, "20", "0",
				FieldKeyType.NOT_A_KEY, FieldType.SOURCE, false);
		fields.add(field4);
		ConnectionInfo info = getFlatFileConnectionInfo();/*
															 * info.getConnProps
															 * (). setProperty
															 * (ConnectionPropsConstants
															 * .SOURCE_FILENAME,
															 * "Employees.csv");
															 */
		Source mara = new Source("MARA", "MARA", "MARA", "MARA", info);
		mara.setFields(fields);
		return mara;
	}

	/**
	 * Create source for Employee Source
	 */
	protected Source createOrderDetailSource() {
		List<Field> fields = new ArrayList<Field>();
		Field field1 = new Field("OrderID", "OrderID", "", NativeDataTypes.FlatFile.INT, "10", "0",
				FieldKeyType.FOREIGN_KEY, FieldType.SOURCE, false);
		fields.add(field1);

		Field field2 = new Field("ProductID", "ProductID", "", NativeDataTypes.FlatFile.INT, "10", "0",
				FieldKeyType.FOREIGN_KEY, FieldType.SOURCE, false);
		fields.add(field2);

		Field field3 = new Field("UnitPrice", "UnitPrice", "", NativeDataTypes.FlatFile.NUMBER, "28", "4",
				FieldKeyType.NOT_A_KEY, FieldType.SOURCE, false);
		fields.add(field3);

		Field field4 = new Field("Quantity", "Quantity", "", NativeDataTypes.FlatFile.INT, "10", "0",
				FieldKeyType.NOT_A_KEY, FieldType.SOURCE, false);
		fields.add(field4);

		Field field5 = new Field("Discount", "Discount", "", NativeDataTypes.FlatFile.INT, "10", "0",
				FieldKeyType.NOT_A_KEY, FieldType.SOURCE, false);
		fields.add(field5);

		Field field6 = new Field("StringFld", "StringFld", "", NativeDataTypes.FlatFile.STRING, "5", "0",
				FieldKeyType.NOT_A_KEY, FieldType.SOURCE, false);
		fields.add(field6);

		Field field7 = new Field("String2Fld", "String2Fld", "", NativeDataTypes.FlatFile.STRING, "5", "0",
				FieldKeyType.NOT_A_KEY, FieldType.SOURCE, false);
		fields.add(field7);

		ConnectionInfo info = getFlatFileConnectionInfo();
		info.getConnProps().setProperty(ConnectionPropsConstants.SOURCE_FILENAME, "Order_Details.csv");
		Source ordDetailSource = new Source("OrderDetail", "OrderDetail", "This is Order Detail Table", "OrderDetail",
				info);
		ordDetailSource.setFields(fields);
		return ordDetailSource;
	}

	/**
	 * Create source for Items table
	 */
	protected Source createItemsSource() {
		Source itemsSource;
		List<Field> fields = new ArrayList<Field>();
		Field field1 = new Field("ItemId", "ItemId", "", NativeDataTypes.FlatFile.INT, "10", "0",
				FieldKeyType.PRIMARY_KEY, FieldType.SOURCE, true);
		fields.add(field1);

		Field field2 = new Field("Item_Name", "Item_Name", "", NativeDataTypes.FlatFile.STRING, "72", "0",
				FieldKeyType.NOT_A_KEY, FieldType.SOURCE, false);
		fields.add(field2);

		Field field3 = new Field("Item_Desc", "Item_Desc", "", NativeDataTypes.FlatFile.STRING, "72", "0",
				FieldKeyType.NOT_A_KEY, FieldType.SOURCE, false);
		fields.add(field3);

		Field field4 = new Field("Price", "Price", "", NativeDataTypes.FlatFile.NUMBER, "10", "2",
				FieldKeyType.NOT_A_KEY, FieldType.SOURCE, false);
		fields.add(field4);

		Field field5 = new Field("Wholesale_cost", "Wholesale_cost", "", NativeDataTypes.FlatFile.NUMBER, "10", "2",
				FieldKeyType.NOT_A_KEY, FieldType.SOURCE, false);
		fields.add(field5);

		Field field6 = new Field("Manufacturer_id", "Manufacturer_id", "", NativeDataTypes.FlatFile.INT, "10", "0",
				FieldKeyType.NOT_A_KEY, FieldType.SOURCE, false);
		fields.add(field6);

		ConnectionInfo info = getFlatFileConnectionInfo();
		info.getConnProps().setProperty(ConnectionPropsConstants.SOURCE_FILENAME, "items.csv");
		itemsSource = new Source("Items", "Items", "This is Items table", "Items", info);
		itemsSource.setFields(fields);
		return itemsSource;
	}

	/**
	 * Create source for Products table
	 */
	protected Source createProductsSource() {
		Source productSource;
		List<Field> fields = new ArrayList<Field>();

		Field foriegnfield = new Field("ItemId", "ItemId", "", NativeDataTypes.FlatFile.INT, "10", "0",
				FieldKeyType.FOREIGN_KEY, FieldType.SOURCE, false);
		foriegnfield.setReferenceConstraint("Items", "ItemId");
		fields.add(foriegnfield);

		Field field1 = new Field("Item_No", "Item_No", "", NativeDataTypes.FlatFile.INT, "10", "0",
				FieldKeyType.PRIMARY_KEY, FieldType.SOURCE, true);
		fields.add(field1);

		Field field2 = new Field("Item_Name", "Item_Name", "", NativeDataTypes.FlatFile.STRING, "72", "0",
				FieldKeyType.NOT_A_KEY, FieldType.SOURCE, false);
		fields.add(field2);

		Field field3 = new Field("Item_Desc", "Item_Desc", "", NativeDataTypes.FlatFile.STRING, "72", "0",
				FieldKeyType.NOT_A_KEY, FieldType.SOURCE, false);
		fields.add(field3);

		Field field4 = new Field("Cust_Price", "Cust_Price", "", NativeDataTypes.FlatFile.NUMBER, "10", "2",
				FieldKeyType.NOT_A_KEY, FieldType.SOURCE, false);
		fields.add(field4);

		Field field5 = new Field("Product_Category", "Product_Category", "", NativeDataTypes.FlatFile.STRING, "30", "0",
				FieldKeyType.NOT_A_KEY, FieldType.SOURCE, false);
		fields.add(field5);

		ConnectionInfo info = getFlatFileConnectionInfo();
		info.getConnProps().setProperty(ConnectionPropsConstants.SOURCE_FILENAME, "products.csv");
		productSource = new Source("Products", "Products", "This is products table", "Products", info);
		productSource.setFields(fields);
		return productSource;
	}

	/**
	 * This method creates the source for manufacturers table
	 * 
	 * @return Source object
	 */
	protected Source createManufacturersSource() {
		Source manufacturerSource;
		List<Field> fields = new ArrayList<Field>();
		Field field1 = new Field("Manufacturer_Id", "Manufacturer_Id", "", NativeDataTypes.FlatFile.INT, "10", "0",
				FieldKeyType.PRIMARY_KEY, FieldType.SOURCE, true);
		fields.add(field1);

		Field field2 = new Field("Manufacturer_Name", "Manufacturer_Name", "", NativeDataTypes.FlatFile.STRING, "72",
				"0", FieldKeyType.NOT_A_KEY, FieldType.SOURCE, false);
		fields.add(field2);

		ConnectionInfo info = getFlatFileConnectionInfo();
		info.getConnProps().setProperty(ConnectionPropsConstants.SOURCE_FILENAME, "Manufacturer.csv");
		manufacturerSource = new Source("Manufacturers", "Manufacturers", "This is Manufacturers table",
				"Manufacturers", info);
		manufacturerSource.setFields(fields);
		return manufacturerSource;
	}

	/**
	 * Create Orders Source
	 * 
	 * @return
	 */
	protected Source createOrdersSource() {
		Source ordersSource;
		List<Field> fields = new ArrayList<Field>();
		Field field1 = new Field("OrderID", "OrderID", "", NativeDataTypes.FlatFile.INT, "10", "0",
				FieldKeyType.PRIMARY_KEY, FieldType.SOURCE, true);
		fields.add(field1);

		Field field2 = new Field("CustomerID", "Customer", "", NativeDataTypes.FlatFile.STRING, "5", "0",
				FieldKeyType.NOT_A_KEY, FieldType.SOURCE, false);
		fields.add(field2);

		Field field3 = new Field("EmployeeID", "EmployeeID", "", NativeDataTypes.FlatFile.INT, "10", "0",
				FieldKeyType.FOREIGN_KEY, FieldType.SOURCE, false);
		fields.add(field3);

		Field field5 = new Field("OrderDate", "OrderDate", "", NativeDataTypes.FlatFile.DATETIME, "19", "0",
				FieldKeyType.NOT_A_KEY, FieldType.SOURCE, false);
		fields.add(field5);

		Field field6 = new Field("RequiredDate", "RequiredDate", "", NativeDataTypes.FlatFile.DATETIME, "19", "0",
				FieldKeyType.NOT_A_KEY, FieldType.SOURCE, false);
		fields.add(field6);

		Field field7 = new Field("ShippedDate", "ShippedDate", "", NativeDataTypes.FlatFile.DATETIME, "19", "0",
				FieldKeyType.NOT_A_KEY, FieldType.SOURCE, false);
		fields.add(field7);

		Field field8 = new Field("ShipVia", "ShipVia", "", NativeDataTypes.FlatFile.INT, "10", "0",
				FieldKeyType.FOREIGN_KEY, FieldType.SOURCE, false);
		fields.add(field8);

		Field field9 = new Field("Freight", "Freight", "", NativeDataTypes.FlatFile.NUMBER, "28", "4",
				FieldKeyType.NOT_A_KEY, FieldType.SOURCE, false);
		fields.add(field9);

		Field field10 = new Field("ShipName", "ShipName", "", NativeDataTypes.FlatFile.STRING, "40", "",
				FieldKeyType.NOT_A_KEY, FieldType.SOURCE, false);
		fields.add(field10);

		Field field11 = new Field("ShipAddress", "ShipAddress", "", NativeDataTypes.FlatFile.STRING, "60", "",
				FieldKeyType.NOT_A_KEY, FieldType.SOURCE, false);
		fields.add(field11);

		Field field12 = new Field("ShipCity", "ShipCity", "", NativeDataTypes.FlatFile.STRING, "15", "",
				FieldKeyType.NOT_A_KEY, FieldType.SOURCE, false);
		fields.add(field12);

		Field field13 = new Field("ShipRegion", "ShipRegion", "", NativeDataTypes.FlatFile.STRING, "15", "",
				FieldKeyType.NOT_A_KEY, FieldType.SOURCE, false);
		fields.add(field13);

		Field field14 = new Field("ShipPostalCode", "ShipPostalCode", "", NativeDataTypes.FlatFile.STRING, "10", "",
				FieldKeyType.NOT_A_KEY, FieldType.SOURCE, false);
		fields.add(field14);

		Field field15 = new Field("ShipCountry", "ShipCountry", "", NativeDataTypes.FlatFile.STRING, "15", "",
				FieldKeyType.NOT_A_KEY, FieldType.SOURCE, false);
		fields.add(field15);

		ConnectionInfo info = getFlatFileConnectionInfo();
		info.getConnProps().setProperty(ConnectionPropsConstants.SOURCE_FILENAME, "Orders.csv");
		ordersSource = new Source("Orders", "Orders", "This is Orders table", "Orders", info);
		ordersSource.setFields(fields);
		return ordersSource;
	}

	/**
	 * Method to create Job Info relational source for Oracle database
	 * 
	 * @param dbName
	 *            database name
	 */
	protected Source createOracleJobSource(String dbName) {
		Source jobSource = null;

		List<Field> fields = new ArrayList<Field>();
		Field jobIDField = new Field("JOB_ID", "JOB_ID", "", NativeDataTypes.Oracle.VARCHAR2, "10", "0",
				FieldKeyType.PRIMARY_KEY, FieldType.SOURCE, true);
		fields.add(jobIDField);

		Field jobTitleField = new Field("JOB_TITLE", "JOB_TITLE", "", NativeDataTypes.Oracle.VARCHAR2, "35", "0",
				FieldKeyType.NOT_A_KEY, FieldType.SOURCE, false);
		fields.add(jobTitleField);

		Field minSalField = new Field("MIN_SALARY", "MIN_SALARY", "", NativeDataTypes.Oracle.NUMBER_PS, "6", "0",
				FieldKeyType.NOT_A_KEY, FieldType.SOURCE, false);
		fields.add(minSalField);

		Field maxSalField = new Field("MAX_SALARY", "MAX_SALARY", "", NativeDataTypes.Oracle.NUMBER_PS, "6", "0",
				FieldKeyType.NOT_A_KEY, FieldType.SOURCE, false);
		fields.add(maxSalField);

		ConnectionInfo info = getRelationalConnInfo(SourceTargetType.Oracle, dbName);
		jobSource = new Source("JOBS", "JOBS", "This is JOBS table", "JOBS", info);
		jobSource.setFields(fields);
		return jobSource;
	}

	// DbaSegmentETL
	protected Source createOracleViewSource(String dbName) {
		Source tabSource = null;

		List<Field> fields = new ArrayList<Field>();
		Field OWNER = new Field("OWNER", "OWNER", "", NativeDataTypes.Oracle.VARCHAR2, "30", "0",
				FieldKeyType.NOT_A_KEY, FieldType.SOURCE, false);
		fields.add(OWNER);
		Field SEGMENT_NAME = new Field("SEGMENT_NAME", "SEGMENT_NAME", "", NativeDataTypes.Oracle.VARCHAR2, "81", "0",
				FieldKeyType.NOT_A_KEY, FieldType.SOURCE, false);
		fields.add(SEGMENT_NAME);
		Field PARTITION_NAME = new Field("PARTITION_NAME", "PARTITION_NAME", "", NativeDataTypes.Oracle.VARCHAR2, "30",
				"0", FieldKeyType.NOT_A_KEY, FieldType.SOURCE, false);
		fields.add(PARTITION_NAME);
		Field SEGMENT_TYPE = new Field("SEGMENT_TYPE", "SEGMENT_TYPE", "", NativeDataTypes.Oracle.VARCHAR2, "18", "0",
				FieldKeyType.NOT_A_KEY, FieldType.SOURCE, false);
		fields.add(SEGMENT_TYPE);
		Field SEGMENT_SUBTYPE = new Field("SEGMENT_SUBTYPE", "SEGMENT_SUBTYPE", "", NativeDataTypes.Oracle.VARCHAR2,
				"10", "0", FieldKeyType.NOT_A_KEY, FieldType.SOURCE, false);
		fields.add(SEGMENT_SUBTYPE);
		Field TABLESPACE_NAME = new Field("TABLESPACE_NAME", "TABLESPACE_NAME", "", NativeDataTypes.Oracle.VARCHAR2,
				"30", "0", FieldKeyType.NOT_A_KEY, FieldType.SOURCE, false);
		fields.add(TABLESPACE_NAME);
		Field HEADER_FILE = new Field("HEADER_FILE", "HEADER_FILE", "", NativeDataTypes.Oracle.VARCHAR2, "15", "0",
				FieldKeyType.NOT_A_KEY, FieldType.SOURCE, false);
		fields.add(HEADER_FILE);
		Field HEADER_BLOCK = new Field("HEADER_BLOCK", "HEADER_BLOCK", "", NativeDataTypes.Oracle.VARCHAR2, "15", "0",
				FieldKeyType.NOT_A_KEY, FieldType.SOURCE, false);
		fields.add(HEADER_BLOCK);
		Field BYTES = new Field("BYTES", "BYTES", "", NativeDataTypes.Oracle.VARCHAR2, "15", "0",
				FieldKeyType.NOT_A_KEY, FieldType.SOURCE, false);
		fields.add(BYTES);
		Field BLOCKS = new Field("BLOCKS", "BLOCKS", "", NativeDataTypes.Oracle.VARCHAR2, "15", "0",
				FieldKeyType.NOT_A_KEY, FieldType.SOURCE, false);
		fields.add(BLOCKS);
		Field EXTENTS = new Field("EXTENTS", "EXTENTS", "", NativeDataTypes.Oracle.VARCHAR2, "15", "0",
				FieldKeyType.NOT_A_KEY, FieldType.SOURCE, false);
		fields.add(EXTENTS);
		Field INITIAL_EXTENT = new Field("INITIAL_EXTENT", "INITIAL_EXTENT", "", NativeDataTypes.Oracle.VARCHAR2, "15",
				"0", FieldKeyType.NOT_A_KEY, FieldType.SOURCE, false);
		fields.add(INITIAL_EXTENT);
		Field NEXT_EXTENT = new Field("NEXT_EXTENT", "NEXT_EXTENT", "", NativeDataTypes.Oracle.VARCHAR2, "15", "0",
				FieldKeyType.NOT_A_KEY, FieldType.SOURCE, false);
		fields.add(NEXT_EXTENT);
		Field MIN_EXTENTS = new Field("MIN_EXTENTS", "MIN_EXTENTS", "", NativeDataTypes.Oracle.VARCHAR2, "15", "0",
				FieldKeyType.NOT_A_KEY, FieldType.SOURCE, false);
		fields.add(MIN_EXTENTS);
		Field MAX_EXTENTS = new Field("MAX_EXTENTS", "MAX_EXTENTS", "", NativeDataTypes.Oracle.VARCHAR2, "15", "0",
				FieldKeyType.NOT_A_KEY, FieldType.SOURCE, false);
		fields.add(MAX_EXTENTS);
		Field MAX_SIZE = new Field("MAX_SIZE", "MAX_SIZE", "", NativeDataTypes.Oracle.VARCHAR2, "15", "0",
				FieldKeyType.NOT_A_KEY, FieldType.SOURCE, false);
		fields.add(MAX_SIZE);
		Field RETENTION = new Field("RETENTION", "RETENTION", "", NativeDataTypes.Oracle.VARCHAR2, "7", "0",
				FieldKeyType.NOT_A_KEY, FieldType.SOURCE, false);
		fields.add(RETENTION);
		Field MINRETENTION = new Field("MINRETENTION", "MINRETENTION", "", NativeDataTypes.Oracle.VARCHAR2, "15", "0",
				FieldKeyType.NOT_A_KEY, FieldType.SOURCE, false);
		fields.add(MINRETENTION);
		Field PCT_INCREASE = new Field("PCT_INCREASE", "PCT_INCREASE", "", NativeDataTypes.Oracle.VARCHAR2, "15", "0",
				FieldKeyType.NOT_A_KEY, FieldType.SOURCE, false);
		fields.add(PCT_INCREASE);
		Field FREELISTS = new Field("FREELISTS", "FREELISTS", "", NativeDataTypes.Oracle.VARCHAR2, "15", "0",
				FieldKeyType.NOT_A_KEY, FieldType.SOURCE, false);
		fields.add(FREELISTS);
		Field FREELIST_GROUPS = new Field("FREELIST_GROUPS", "FREELIST_GROUPS", "", NativeDataTypes.Oracle.VARCHAR2,
				"15", "0", FieldKeyType.NOT_A_KEY, FieldType.SOURCE, false);
		fields.add(FREELIST_GROUPS);
		Field RELATIVE_FNO = new Field("RELATIVE_FNO", "RELATIVE_FNO", "", NativeDataTypes.Oracle.VARCHAR2, "15", "0",
				FieldKeyType.NOT_A_KEY, FieldType.SOURCE, false);
		fields.add(RELATIVE_FNO);
		Field BUFFER_POOL = new Field("BUFFER_POOL", "BUFFER_POOL", "", NativeDataTypes.Oracle.VARCHAR2, "7", "0",
				FieldKeyType.NOT_A_KEY, FieldType.SOURCE, false);
		fields.add(BUFFER_POOL);
		Field FLASH_CACHE = new Field("FLASH_CACHE", "FLASH_CACHE", "", NativeDataTypes.Oracle.VARCHAR2, "7", "0",
				FieldKeyType.NOT_A_KEY, FieldType.SOURCE, false);
		fields.add(FLASH_CACHE);
		Field CELL_FLASH_CACHE = new Field("CELL_FLASH_CACHE", "CELL_FLASH_CACHE", "", NativeDataTypes.Oracle.VARCHAR2,
				"7", "0", FieldKeyType.NOT_A_KEY, FieldType.SOURCE, false);
		fields.add(CELL_FLASH_CACHE);
		ConnectionInfo info = getRelationalConnInfo(SourceTargetType.Oracle, dbName);
		tabSource = new Source("DBA_SEGMENTS", "DBA_SEGMENTS", "This is oracle table", "DBA_SEGMENTS", info);
		tabSource.setFields(fields);
		return tabSource;
	}

	protected Source createSASSource(String dbName) {
		ConnectionInfo info = getRelationalConnInfo(SourceTargetType.SAS, dbName);
		info.getConnProps().setProperty(ConnectionPropsConstants.CONNECTIONNAME, "SAS");
		Source sasSrc = new Source("DATATYPES_IN", "DATATYPES_IN", "DATATYPES_IN", "DATATYPES_IN", info);

		List<Field> fields = new ArrayList<Field>();
		Field ind = SASHelper.configureField(new Field("ind", "ind", "ind", NativeDataTypes.SAS.NUMBER, "5", "0",
				FieldKeyType.NOT_A_KEY, FieldType.SOURCE, false), "5.");
		fields.add(ind);
		Field fldShortText = SASHelper.configureField(new Field("fldShortText", "fldShortText", "fldShortText",
				NativeDataTypes.SAS.STRING, "25", "0", FieldKeyType.NOT_A_KEY, FieldType.SOURCE, false), "$25.");
		fields.add(fldShortText);
		Field fldLongText = SASHelper.configureField(new Field("fldLongText", "fldLongText", "fldLongText",
				NativeDataTypes.SAS.STRING, "90", "0", FieldKeyType.NOT_A_KEY, FieldType.SOURCE, false), "$90.");
		fields.add(fldLongText);
		Field fldNumeric7_2 = SASHelper.configureField(new Field("fldNumeric7_2", "fldNumeric7_2", "fldNumeric7_2",
				NativeDataTypes.SAS.NUMBER, "6", "2", FieldKeyType.NOT_A_KEY, FieldType.SOURCE, false), "7.2");
		fields.add(fldNumeric7_2);
		Field fldNumeric15 = SASHelper.configureField(new Field("fldNumeric15", "fldNumeric15", "fldNumeric15",
				NativeDataTypes.SAS.NUMBER, "15", "0", FieldKeyType.NOT_A_KEY, FieldType.SOURCE, false), "15.");
		fields.add(fldNumeric15);
		Field fldDate = SASHelper.configureField(new Field("fldDate", "fldDate", "fldDate", NativeDataTypes.SAS.DATE,
				"6", "0", FieldKeyType.NOT_A_KEY, FieldType.SOURCE, false), "DATE5.");
		fields.add(fldDate);
		Field fldDateTime = SASHelper.configureField(new Field("fldDateTime", "fldDateTime", "fldDateTime",
				NativeDataTypes.SAS.DATETIME, "12", "0", FieldKeyType.NOT_A_KEY, FieldType.SOURCE, false),
				"DATETIME24.");
		fields.add(fldDateTime);
		Field fldTime = SASHelper.configureField(new Field("fldTime", "fldTime", "fldTime", NativeDataTypes.SAS.TIME,
				"6", "0", FieldKeyType.NOT_A_KEY, FieldType.SOURCE, false), "TIME.");
		fields.add(fldTime);
		Field fldYYYMMDD = SASHelper.configureField(new Field("fldYYYMMDD", "fldYYYMMDD", "fldYYYMMDD",
				NativeDataTypes.SAS.DATE, "6", "0", FieldKeyType.NOT_A_KEY, FieldType.SOURCE, false), "YYMMDD.");
		fields.add(fldYYYMMDD);

		sasSrc.setFields(fields);

		SASHelper.configureSASSource(sasSrc, "TDATASET", "", "", "");
		return sasSrc;
	}

	protected Target createSASTarget(String dbName, String name) {
		ConnectionInfo info = getRelationalConnInfo(SourceTargetType.SAS, dbName);

		Target sasTgt = new Target(name, name, name, name, info);
		List<Field> fields = new ArrayList<Field>();
		Field ind = SASHelper.configureField(new Field("ind", "ind", "ind", NativeDataTypes.SAS.NUMBER, "5", "0",
				FieldKeyType.NOT_A_KEY, FieldType.TARGET, false), "5.");
		fields.add(ind);
		Field fldShortText = SASHelper.configureField(new Field("fldShortText", "fldShortText", "fldShortText",
				NativeDataTypes.SAS.STRING, "25", "0", FieldKeyType.NOT_A_KEY, FieldType.TARGET, false), "$25.");
		fields.add(fldShortText);
		Field fldLongText = SASHelper.configureField(new Field("fldLongText", "fldLongText", "fldLongText",
				NativeDataTypes.SAS.STRING, "90", "0", FieldKeyType.NOT_A_KEY, FieldType.TARGET, false), "$90.");
		fields.add(fldLongText);
		Field fldNumeric7_2 = SASHelper.configureField(new Field("fldNumeric7_2", "fldNumeric7_2", "fldNumeric7_2",
				NativeDataTypes.SAS.NUMBER, "6", "2", FieldKeyType.NOT_A_KEY, FieldType.TARGET, false), "7.2");
		fields.add(fldNumeric7_2);
		Field fldNumeric15 = SASHelper.configureField(new Field("fldNumeric15", "fldNumeric15", "fldNumeric15",
				NativeDataTypes.SAS.NUMBER, "15", "0", FieldKeyType.NOT_A_KEY, FieldType.TARGET, false), "15.");
		fields.add(fldNumeric15);
		Field fldDate = SASHelper.configureField(new Field("fldDate", "fldDate", "fldDate", NativeDataTypes.SAS.DATE,
				"6", "0", FieldKeyType.NOT_A_KEY, FieldType.TARGET, false), "DATE5.");
		fields.add(fldDate);
		Field fldDateTime = SASHelper.configureField(new Field("fldDateTime", "fldDateTime", "fldDateTime",
				NativeDataTypes.SAS.DATETIME, "12", "0", FieldKeyType.NOT_A_KEY, FieldType.TARGET, false),
				"DATETIME24.");
		fields.add(fldDateTime);
		Field fldTime = SASHelper.configureField(new Field("fldTime", "fldTime", "fldTime", NativeDataTypes.SAS.TIME,
				"6", "0", FieldKeyType.NOT_A_KEY, FieldType.TARGET, false), "TIME.");
		fields.add(fldTime);
		Field fldYYYMMDD = SASHelper.configureField(new Field("fldYYYMMDD", "fldYYYMMDD", "fldYYYMMDD",
				NativeDataTypes.SAS.DATE, "6", "0", FieldKeyType.NOT_A_KEY, FieldType.TARGET, false), "YYMMDD.");
		fields.add(fldYYYMMDD);

		sasTgt.setFields(fields);
		SASHelper.configureSASTarget(sasTgt, "TDATASET", "", "",
				"ShortTextIndex^2false^2^2fldShortText^3^1NumericIndex^2false^2^2fldNumeric15^3^1", "NumericIndex");

		return sasTgt;
	}

	/**
	 * Method to create ekko sap source
	 * 
	 * @param dbName
	 *            dbd name
	 */
	protected Source createSAPekkoSource(String dbName) {

		Source ekkoSource = null;

		List<Field> fields = new ArrayList<Field>();

		Field mandtField = new Field("MANDT", "Client", "Client", NativeDataTypes.SAP.CLNT, "3", "0",
				FieldKeyType.PRIMARY_KEY, FieldType.SOURCE, true);
		fields.add(mandtField);

		Field eblnField = new Field("EBELN", "Purchasing document number", "Purchasing document number",
				NativeDataTypes.SAP.CHAR, "10", "0", FieldKeyType.PRIMARY_FOREIGN_KEY, FieldType.SOURCE, true);
		fields.add(eblnField);

		Field bukrsField = new Field("BUKRS", "Company Code", "Company Code", NativeDataTypes.SAP.CHAR, "4", "0",
				FieldKeyType.NOT_A_KEY, FieldType.SOURCE, true);
		fields.add(bukrsField);

		Field pincrField = new Field("PINCR", "Item number interval", "Item number interval", NativeDataTypes.SAP.NUMC,
				"5", "0", FieldKeyType.NOT_A_KEY, FieldType.SOURCE, false);
		fields.add(pincrField);

		ConnectionInfo info = getRelationalConnInfo(SourceTargetType.SAP_R3, dbName);
		info.getConnProps().setProperty(ConnectionPropsConstants.SOURCE_TABLE_NAME, "sourcetable");
		info.getConnProps().setProperty(ConnectionPropsConstants.ORDER_BY_PORTS, "1");
		info.getConnProps().setProperty(ConnectionPropsConstants.SELECT_OPTION, "Select Single");
		info.getConnProps().setProperty(ConnectionPropsConstants.CONNECTIONNAME, "SAP_R3");
		ekkoSource = new Source("EKKO", "Purchasing Document Header11", "Purchasing Document Header1", "EKKO", info);
		ekkoSource.setDatabaseSubtype(StringConstants.SAP_TRANSPARENT_TABLE);
		ekkoSource.setFields(fields);
		return ekkoSource;
	}

	// DBD name as argument
	protected Source createSimpleSAPSource() {

		Source sapSrc = null;

		try {
			sapSrc = folder.fetchSourcesFromRepository(new INameFilter() {

				@Override
				public boolean accept(String name) {
					// TODO Auto-generated method stub
					return name.equals("A004");
				}
			}).get(0);
		} catch (RepoOperationException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (MapFwkReaderException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return sapSrc;
	}

	/**
	 * Method to create ekko sap source
	 * 
	 * @param dbName
	 *            dbd name
	 */
	protected Source createSAPekpoSource(String dbName) {
		Source ekkoSource = null;

		List<Field> fields = new ArrayList<Field>();
		Field mandtField = new Field("MANDT", "Client", "Client", NativeDataTypes.SAP.CLNT, "3", "0",
				FieldKeyType.NOT_A_KEY, FieldType.SOURCE, true);
		fields.add(mandtField);

		Field eblnField = new Field("EBELN", "Purchasing document number", "Purchasing document number",
				NativeDataTypes.SAP.CHAR, "10", "0", FieldKeyType.PRIMARY_KEY, FieldType.SOURCE, true);
		fields.add(eblnField);

		Field bukrsField = new Field("BUKRS", "Company Code", "Company Code", NativeDataTypes.SAP.CHAR, "4", "0",
				FieldKeyType.NOT_A_KEY, FieldType.SOURCE, true);
		fields.add(bukrsField);

		Field ktmngField = new Field("KTMNG", "Target quantity", "Target quantity", NativeDataTypes.SAP.QUAN, "13", "3",
				FieldKeyType.NOT_A_KEY, FieldType.SOURCE, false);
		fields.add(ktmngField);

		ConnectionInfo info = getRelationalConnInfo(SourceTargetType.SAP_R3, dbName);
		info.getConnProps().setProperty(ConnectionPropsConstants.CONNECTIONNAME, "SAP_R3");
		ekkoSource = new Source("EKPO", "Purchasing Document Item1", "Purchasing Document Item1", "EKPO", info);
		ekkoSource.setDatabaseSubtype(StringConstants.SAP_TRANSPARENT_TABLE);
		ekkoSource.setFields(fields);
		return ekkoSource;
	}

	/**
	 * Method to create Job Info relational source for Teradata database
	 * 
	 * @param dbName
	 *            database name
	 */
	protected Source createTeradataJobSource(String dbName) {
		Source jobSource = null;

		List<Field> fields = new ArrayList<Field>();
		Field jobIDField = new Field("JOB_ID", "JOB_ID", "", NativeDataTypes.Teradata.VARCHAR, "10", "0",
				FieldKeyType.PRIMARY_KEY, FieldType.SOURCE, true);
		fields.add(jobIDField);

		Field jobTitleField = new Field("JOB_TITLE", "JOB_TITLE", "", NativeDataTypes.Teradata.VARCHAR, "35", "0",
				FieldKeyType.NOT_A_KEY, FieldType.SOURCE, false);
		fields.add(jobTitleField);

		Field minSalField = new Field("MIN_SALARY", "MIN_SALARY", "", NativeDataTypes.Teradata.DECIMAL, "6", "0",
				FieldKeyType.NOT_A_KEY, FieldType.SOURCE, false);
		fields.add(minSalField);

		Field maxSalField = new Field("MAX_SALARY", "MAX_SALARY", "", NativeDataTypes.Teradata.DECIMAL, "6", "0",
				FieldKeyType.NOT_A_KEY, FieldType.SOURCE, false);
		fields.add(maxSalField);

		ConnectionInfo info = getRelationalConnInfo(SourceTargetType.Teradata, dbName);
		jobSource = new Source("JOBS", "JOBS", "This is JOBS table", "JOBS", info);
		jobSource.setFields(fields);
		return jobSource;
	}

	public Source createTabledataJobSource(String dbName) {
		Source jobSource = null;
		List<Field> fields = new ArrayList<Field>();
		Field inid = new Field("SCALAR_INPUT_Integration_Id", "JOB_ID", "", NativeDataTypes.Teradata.VARCHAR, "10", "0",
				FieldKeyType.PRIMARY_KEY, FieldType.SOURCE, true);
		fields.add(inid);
		ConnectionInfo info = getRelationalConnInfo(SourceTargetType.Teradata, dbName);
		jobSource = new Source("Table", "Table", "This is TableType table", "Table", info);
		jobSource.setFields(fields);
		return jobSource;
	}

	public Source createSAPCompanyCodeSource(String dbName) {
		Source jobSource = null;

		List<Field> fields = new ArrayList<Field>();
		// Field plvar = new Field("SCALAR_INPUT_PLVAR", "JOB_ID", "",
		// NativeDataTypes.Oracle.VARCHAR, "10", "0",
		// FieldKeyType.PRIMARY_KEY, FieldType.SOURCE, true);
		// fields.add(plvar);

		// Field otype = new Field("SCALAR_INPUT_OTYPE", "JOB_TITLE", "",
		// NativeDataTypes.Oracle.LONG, "35", "0",
		// FieldKeyType.NOT_A_KEY, FieldType.SOURCE, false);
		// fields.add(otype);
		//
		// Field objId = new Field("SCALAR_INPUT_OBJID", "MIN_SALARY", "",
		// NativeDataTypes.Oracle.CHAR, "6", "0", FieldKeyType.NOT_A_KEY,
		// FieldType.SOURCE, false);
		// fields.add(objId);
		//
		// Field begDate = new Field("SCALAR_INPUT_BEGIN_DATE", "MAX_SALARY",
		// "",
		// NativeDataTypes.Oracle.DATE, "6", "0", FieldKeyType.NOT_A_KEY,
		// FieldType.SOURCE, false);
		// fields.add(begDate);
		// Field endDate = new Field("SCALAR_INPUT_END_DATE", "JOB_ID", "",
		// NativeDataTypes.Oracle.DATE, "10", "0",
		// FieldKeyType.PRIMARY_KEY, FieldType.SOURCE, true);
		// fields.add(endDate);
		Field inid = new Field("SCALAR_INPUT_Integration_Id", "JOB_ID", "", NativeDataTypes.Oracle.VARCHAR, "10", "0",
				FieldKeyType.PRIMARY_KEY, FieldType.SOURCE, true);
		fields.add(inid);

		ConnectionInfo info = getRelationalConnInfo(SourceTargetType.Oracle, dbName);
		jobSource = new Source("COMPANYCODE", "COMPANYCODE", "This is COMPANYCODE table", "COMPANYCODE", info);
		jobSource.setFields(fields);
		return jobSource;
	}

	protected ConnectionInfo getRelationalConnInfo(SourceTargetType dbType, String dbName) {
		ConnectionInfo connInfo = null;
		connInfo = new ConnectionInfo(dbType);
		connInfo.getConnProps().setProperty(ConnectionPropsConstants.DBNAME, dbName);
		return connInfo;
	}

	/**
	 * Method to create relational target
	 */
	protected Target createRelationalTarget(SourceTargetType DBType, String name) {
		Target target = new Target(name, name, name, name, new ConnectionInfo(DBType));
		return target;
	}

	protected Target createDbasegmentTarget(SourceTargetType DBType, String name) {
		Target sasTgt;
		List<Field> fields = new ArrayList<Field>();
		Field OWNER = new Field("OWNER", "OWNER", "", NativeDataTypes.Oracle.VARCHAR2, "30", "0",
				FieldKeyType.NOT_A_KEY, FieldType.SOURCE, false);
		fields.add(OWNER);
		Field SEGMENT_NAME = new Field("SEGMENT_NAME", "SEGMENT_NAME", "", NativeDataTypes.Oracle.VARCHAR2, "81", "0",
				FieldKeyType.NOT_A_KEY, FieldType.SOURCE, false);
		fields.add(SEGMENT_NAME);
		Field PARTITION_NAME = new Field("PARTITION_NAME", "PARTITION_NAME", "", NativeDataTypes.Oracle.VARCHAR2, "30",
				"0", FieldKeyType.NOT_A_KEY, FieldType.SOURCE, false);
		fields.add(PARTITION_NAME);
		Field SEGMENT_TYPE = new Field("SEGMENT_TYPE", "SEGMENT_TYPE", "", NativeDataTypes.Oracle.VARCHAR2, "18", "0",
				FieldKeyType.NOT_A_KEY, FieldType.SOURCE, false);
		fields.add(SEGMENT_TYPE);
		Field SEGMENT_SUBTYPE = new Field("SEGMENT_SUBTYPE", "SEGMENT_SUBTYPE", "", NativeDataTypes.Oracle.VARCHAR2,
				"10", "0", FieldKeyType.NOT_A_KEY, FieldType.SOURCE, false);
		fields.add(SEGMENT_SUBTYPE);
		Field TABLESPACE_NAME = new Field("TABLESPACE_NAME", "TABLESPACE_NAME", "", NativeDataTypes.Oracle.VARCHAR2,
				"30", "0", FieldKeyType.NOT_A_KEY, FieldType.SOURCE, false);
		fields.add(TABLESPACE_NAME);
		Field HEADER_FILE = new Field("HEADER_FILE", "HEADER_FILE", "", NativeDataTypes.Oracle.VARCHAR2, "15", "0",
				FieldKeyType.NOT_A_KEY, FieldType.SOURCE, false);
		fields.add(HEADER_FILE);
		Field HEADER_BLOCK = new Field("HEADER_BLOCK", "HEADER_BLOCK", "", NativeDataTypes.Oracle.VARCHAR2, "15", "0",
				FieldKeyType.NOT_A_KEY, FieldType.SOURCE, false);
		fields.add(HEADER_BLOCK);
		Field BYTES = new Field("BYTES", "BYTES", "", NativeDataTypes.Oracle.VARCHAR2, "15", "0",
				FieldKeyType.NOT_A_KEY, FieldType.SOURCE, false);
		fields.add(BYTES);
		Field BLOCKS = new Field("BLOCKS", "BLOCKS", "", NativeDataTypes.Oracle.VARCHAR2, "15", "0",
				FieldKeyType.NOT_A_KEY, FieldType.SOURCE, false);
		fields.add(BLOCKS);
		Field EXTENTS = new Field("EXTENTS", "EXTENTS", "", NativeDataTypes.Oracle.VARCHAR2, "15", "0",
				FieldKeyType.NOT_A_KEY, FieldType.SOURCE, false);
		fields.add(EXTENTS);
		Field INITIAL_EXTENT = new Field("INITIAL_EXTENT", "INITIAL_EXTENT", "", NativeDataTypes.Oracle.VARCHAR2, "15",
				"0", FieldKeyType.NOT_A_KEY, FieldType.SOURCE, false);
		fields.add(INITIAL_EXTENT);
		Field NEXT_EXTENT = new Field("NEXT_EXTENT", "NEXT_EXTENT", "", NativeDataTypes.Oracle.VARCHAR2, "15", "0",
				FieldKeyType.NOT_A_KEY, FieldType.SOURCE, false);
		fields.add(NEXT_EXTENT);
		Field MIN_EXTENTS = new Field("MIN_EXTENTS", "MIN_EXTENTS", "", NativeDataTypes.Oracle.VARCHAR2, "15", "0",
				FieldKeyType.NOT_A_KEY, FieldType.SOURCE, false);
		fields.add(MIN_EXTENTS);
		Field MAX_EXTENTS = new Field("MAX_EXTENTS", "MAX_EXTENTS", "", NativeDataTypes.Oracle.VARCHAR2, "15", "0",
				FieldKeyType.NOT_A_KEY, FieldType.SOURCE, false);
		fields.add(MAX_EXTENTS);
		Field MAX_SIZE = new Field("MAX_SIZE", "MAX_SIZE", "", NativeDataTypes.Oracle.VARCHAR2, "15", "0",
				FieldKeyType.NOT_A_KEY, FieldType.SOURCE, false);
		fields.add(MAX_SIZE);
		Field RETENTION = new Field("RETENTION", "RETENTION", "", NativeDataTypes.Oracle.VARCHAR2, "7", "0",
				FieldKeyType.NOT_A_KEY, FieldType.SOURCE, false);
		fields.add(RETENTION);
		Field MINRETENTION = new Field("MINRETENTION", "MINRETENTION", "", NativeDataTypes.Oracle.VARCHAR2, "15", "0",
				FieldKeyType.NOT_A_KEY, FieldType.SOURCE, false);
		fields.add(MINRETENTION);
		Field PCT_INCREASE = new Field("PCT_INCREASE", "PCT_INCREASE", "", NativeDataTypes.Oracle.VARCHAR2, "15", "0",
				FieldKeyType.NOT_A_KEY, FieldType.SOURCE, false);
		fields.add(PCT_INCREASE);
		Field FREELISTS = new Field("FREELISTS", "FREELISTS", "", NativeDataTypes.Oracle.VARCHAR2, "15", "0",
				FieldKeyType.NOT_A_KEY, FieldType.SOURCE, false);
		fields.add(FREELISTS);
		Field FREELIST_GROUPS = new Field("FREELIST_GROUPS", "FREELIST_GROUPS", "", NativeDataTypes.Oracle.VARCHAR2,
				"15", "0", FieldKeyType.NOT_A_KEY, FieldType.SOURCE, false);
		fields.add(FREELIST_GROUPS);
		Field RELATIVE_FNO = new Field("RELATIVE_FNO", "RELATIVE_FNO", "", NativeDataTypes.Oracle.VARCHAR2, "15", "0",
				FieldKeyType.NOT_A_KEY, FieldType.SOURCE, false);
		fields.add(RELATIVE_FNO);
		Field BUFFER_POOL = new Field("BUFFER_POOL", "BUFFER_POOL", "", NativeDataTypes.Oracle.VARCHAR2, "7", "0",
				FieldKeyType.NOT_A_KEY, FieldType.SOURCE, false);
		fields.add(BUFFER_POOL);
		Field FLASH_CACHE = new Field("FLASH_CACHE", "FLASH_CACHE", "", NativeDataTypes.Oracle.VARCHAR2, "7", "0",
				FieldKeyType.NOT_A_KEY, FieldType.SOURCE, false);
		fields.add(FLASH_CACHE);
		Field CELL_FLASH_CACHE = new Field("CELL_FLASH_CACHE", "CELL_FLASH_CACHE", "", NativeDataTypes.Oracle.VARCHAR2,
				"7", "0", FieldKeyType.NOT_A_KEY, FieldType.SOURCE, false);
		fields.add(CELL_FLASH_CACHE);
		ConnectionInfo info = getRelationalConnInfo(SourceTargetType.Oracle, name);
		sasTgt = new Target("DBA_SEGMENTS", "DBA_SEGMENTS", "This is oracle table", "DBA_SEGMENTS", info);
		sasTgt.setFields(fields);

		return sasTgt;
	}

	/**
	 * This method creates the target for the mapping
	 * 
	 * @return
	 */
	public Target createFlatFileTarget(String name) {
		Target tgt = new Target(name, name, "", name, new ConnectionInfo(SourceTargetType.Flat_File));
		tgt.getConnInfo().getConnProps().setProperty(ConnectionPropsConstants.FLATFILE_CODEPAGE, "MS1252");
		tgt.getConnInfo().getConnProps().setProperty(ConnectionPropsConstants.OUTPUT_FILENAME, name + ".out");
		return tgt;
	}

	/**
	 * This method creates the target for relational database
	 * 
	 * @param name
	 * @param relationalType
	 * @return
	 */
	public Target createRelationalTarget(String name, SourceTargetType relationalType) {
		Target tgt = new Target(name, name, "", name, new ConnectionInfo(relationalType));
		return tgt;
	}

	/**
	 * This method generates the output xml
	 * 
	 * @throws Exception
	 *             exception
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
		// String XmlData =
		// org.tools.ConFileContent.readToString("M_"+org.tools.GetProperties.getKeyValue("TableNm")+".xml").replace("<ATTRIBUTE
		// NAME=\"Parameter Filename\" VALUE=\"\"/>", "<ATTRIBUTE
		// NAME=\"Parameter Filename\"
		// VALUE=\"$PMRootDir/EDWParam/edw.param\"/>");
		// System.out.println("<ATTRIBUTE NAME=\"Parameter Filename\"
		// VALUE=\"$PMRootDir/EDWParam/edw.param\"/>");

		// org.tools.ConFileContent.writeLog(org.tools.ConFileContent
		// .ReplaceColumnNm("M_CHECK_"+org.tools.GetProperties.getKeyValue("System")+"_"+
		// org.tools.GetProperties.getKeyValue("TableNm").toUpperCase() +
		// "_CK.xml"));
		// }

		// org.tools.ConFileContent
		// .writeLog(org.tools.ConFileContent.ReplaceColumnNm("M_" +
		// org.tools.GetProperties.getKeyValue("System")
		// + "_" + org.tools.GetProperties.getKeyValue("TableNm").toUpperCase()
		// + ".xml"));
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

				// NullEable = false;
				// System.out.println(a.get(1).toString() + "," +
				// org.tools.DataTypeTrans.Trans(a.get(2), "MSSQL") + ""
				// + len + "," + precision);
				Field field = new Field(a.get(1).toString(), a.get(1).toString(), "",
						org.tools.DataTypeTrans.Trans(a.get(2), DbType), len, precision, FieldKeyType.NOT_A_KEY,
						FieldType.SOURCE, false);

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
				// ArrayList<String> BCloum = new
				// ArrayList<String>(org.tools.RePlaceBigCloumn.BigCloumn());
				// if (!BCloum.contains(a.get(2).toUpperCase().substring(0,
				// a.get(2).toString().indexOf("(")))) {

				field = new Field(a.get(1).toString(), a.get(1).toString(), "",
						org.tools.DataTypeTrans.Trans(a.get(2), DbType), len, precision, ColType, FieldType.SOURCE,
						NullEable);

				// } else {
				// org.tools.GetProperties.writeProperties("ISBigCloumn",
				// "_LARGE");
				// TableNme = "_LARGE";
				// System.out.println(a.get(2).toUpperCase().substring(0,
				// a.get(2).toString().indexOf("(")));
				// }
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