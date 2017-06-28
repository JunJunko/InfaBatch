package com.exprotmeteexcel.utl.global;

public class SQLGlobal {

	public static final String GET_COLUMN_ORACLESQL = "select * from  (select t.owner as owner,     "
			+ "        t.table_name as table_name,                                   "
			+ "        t.column_name as column_name,                                  "
			+ "        t.data_type as data_type,                                    "
			+ "        case when t.data_length is null then -1 else t.data_length end as data_length,                                  "
			+ "        decode(t.nullable,'Y',1,0) as nullable,                     "
			+ "        decode(t.data_precision, null,0,data_precision)  as data_precision ,"
			+ "       t.owner||'.'||t.table_name as ownertable "
			+ " 		 from all_TAB_COLUMNS t ) p where p.ownertable in (:ownertable)";

	public static final String GET_COLUMN_ORACLESQL_ALL = "select * from (select t.owner as owner,"
			+ "             t.table_name as table_name,"
			+ "             t.owner || '.' || t.table_name as ownertable   "
			+ "        from all_tables t ) p ";

	public static final String GET_COLUMN_TERADATASQL = "select * from  (select t.owner as owner,     "
			+ "        t.table_name as table_name,                                   "
			+ "        t.column_name as column_name,                                  "
			+ "        t.data_type as data_type,                                    "
			+ "        case when t.data_length is null then -1 else t.data_length end as data_length,                                  "
			+ "        decode(t.nullable,'Y',1,0) as nullable,                     "
			+ "        decode(t.data_precision, null,0,data_precision)  as data_precision ,"
			+ "       t.owner||'.'||t.table_name as ownertable "
			+ " 		 from all_TAB_COLUMNS t ) p where p.ownertable in (:ownertable)";

	/** (mysql) */
	public static final String GET_COLUMN_MYSQLSQL = "select * from  (SELECT TABLE_SCHEMA AS OWNER, "
			+ "  TABLE_NAME AS TABLE_NAME, " + " COLUMN_NAME AS COLUMN_NAME, " + " DATA_TYPE AS DATA_TYPE, "
			+ " IFNULL(CHARACTER_MAXIMUM_LENGTH,-1) AS DATA_LENGTH, "
			+ " CASE WHEN IS_NULLABLE='YES' THEN 1 ELSE 0 END AS NULLABLE, "
			+ " IFNULL(NUMERIC_SCALE,0)  AS DATA_PRECISION," + " CONCAT(TABLE_SCHEMA ,'.', table_name) OWNERTABLE  "
			+ " FROM information_schema.`COLUMNS`  ) p where p.ownertable in (:ownertable)";

	public static final String GET_COLUMN_MYSQLSQL_ALL = "select * from  (SELECT TABLE_SCHEMA AS OWNER, "
			+ " TABLE_NAME AS TABLE_NAME, " + " CONCAT(TABLE_SCHEMA ,'.', table_name) OWNERTABLE  "
			+ " FROM  information_schema.`TABLES` t where t.TABLE_TYPE ='BASE TABLE' ) p limit 500";

	/** (SQLSERVER) */
	public static final String GET_COLUMN_SQLSERVERSQL = " select * from (SELECT TABLE_SCHEMA AS OWNER, "
			+ " TABLE_NAME AS TABLE_NAME, " 
			+ " COLUMN_NAME AS COLUMN_NAME, " 
			+ " DATA_TYPE AS TYPE_NAME, "
			+ " ISNULL(ISNULL(ISNULL(character_maximum_length,numeric_precision),datetime_precision),1) AS COLUMN_SIZE, "
			+ " IS_NULLABLE AS IS_NULLABLE, "
			+ " convert(varchar(4000), isnull((SELECT C.value FROM sys.tables A1 inner JOIN sys.extended_properties C ON C.major_id = A1.object_id  and minor_id=0 inner join sys.schemas d on a1.schema_id=d.schema_id  where A.TABLE_NAME=a1.name and s.schema_id=d.schema_id),null))  as TABLE_REMARKS ,"
			+ "	convert(varchar(4000), isnull((SELECT C2.value FROM sys.tables A2 inner join sys.schemas d2 on a2.schema_id=d2.schema_id INNER JOIN sys.columns B2 ON B2.object_id = A2.object_id LEFT JOIN sys.extended_properties C2 ON C2.major_id = B2.object_id AND C2.minor_id = B2.column_id WHERE A.TABLE_NAME=a2.name and s.schema_id=d2.schema_id and a.COLUMN_NAME=b2.name),null)) as REMARKS,"
			+ " COLUMN_DEFAULT as COLUMN_DEF,"
			+ " isnull(( select 'pri' from INFORMATION_SCHEMA.KEY_COLUMN_USAGE b,INFORMATION_SCHEMA.TABLE_CONSTRAINTS t where t.CONSTRAINT_TYPE ='PRIMARY KEY' and t.TABLE_SCHEMA=b.TABLE_SCHEMA and t.TABLE_NAME=b.TABLE_NAME and  t.TABLE_SCHEMA=a.TABLE_SCHEMA and t.TABLE_NAME=a.TABLE_NAME and b.COLUMN_NAME=a.COLUMN_NAME),null)  as PRIMARYKEY,"
			+ " (TABLE_SCHEMA+'.'+TABLE_NAME) as ownertable"
			+ " FROM information_schema.COLUMNS A ,sys.schemas s where A.TABLE_SCHEMA=s.name ) p where p.ownertable in (:ownertable)";

	/** (SQLSERVER) */
	public static final String GET_COLUMN_SQLSERVERSQL_All = " select  * from (SELECT  TABLE_SCHEMA AS OWNER,TABLE_NAME AS TABLE_NAME,"
			+ "  TABLE_SCHEMA+'.'+TABLE_NAME AS OWNERTABLE FROM "
			+ " information_schema.TABLES where TABLE_TYPE ='BASE TABLE' ) p  ";

}
