--------------------------CREATE TABLE SYS.SYSTEM_PRIVILEGE_MAP-------------------------------------
DROP TABLE ODS_DDL.O_CBI_SYSTEM_PRIVILEGE_MAP_H;
CREATE MULTISET TABLE ODS_DDL.O_CBI_SYSTEM_PRIVILEGE_MAP_H 
 (PRIVILEGE decimal(22)  TITLE 'Numeric privilege type code' NOT NULL,
 NAME varchar(40) CHARACTER SET UNICODE TITLE 'Name of the type of privilege' NOT NULL,
 PROPERTY decimal(22)  TITLE 'Property flag of privilege like not export this privilege, etc' NOT NULL,
 DW_START_DT   TITLE '开始日期' DATE FORMAT 'YYYY-MM-DD' NULL ,
 DW_END_DT   TITLE '结束日期' DATE FORMAT 'YYYY-MM-DD' NULL ,
 DW_ETL_DT   TITLE '翻牌日期' DATE FORMAT 'YYYY-MM-DD' NULL ,
 DW_UPD_TM TIMESTAMP(0)  TITLE '更新时间'  DEFAULT CURRENT_TIMESTAMP(0) )
 PRIMARY INDEX (
PRIVILEGE
)
--------------------------CREATE TABLE CTXSYS.DR$OBJECT_ATTRIBUTE-------------------------------------
DROP TABLE ODS_DDL.O_CBI_DR$OBJECT_ATTRIBUTE;
CREATE MULTISET TABLE ODS_DDL.O_CBI_DR$OBJECT_ATTRIBUTE 
 (OAT_ID decimal(22)   NOT NULL,
 OAT_CLA_ID decimal(22)   NULL,
 OAT_OBJ_ID decimal(22)   NULL,
 OAT_ATT_ID decimal(22)   NULL,
 OAT_NAME varchar(30) CHARACTER SET UNICODE  NULL,
 OAT_DESC varchar(80) CHARACTER SET UNICODE  NULL,
 OAT_REQUIRED char(1)   NULL,
 OAT_SYSTEM char(1)   NULL,
 OAT_STATIC char(1)   NULL,
 OAT_DATATYPE char(1)   NULL,
 OAT_DEFAULT varchar(500) CHARACTER SET UNICODE  NULL,
 OAT_VAL_MIN decimal(22)   NULL,
 OAT_VAL_MAX decimal(22)   NULL,
 OAT_LOV char(1)   NULL,
 DW_OPER_FLAG SMALLINT  TITLE '操作标识' DEFAULT 1 ,
 DW_ETL_DT    TITLE '翻牌日期' DATE FORMAT 'YYYY-MM-DD' NULL ,
 DW_UPD_TM TIMESTAMP(0) TITLE '更新时间' DEFAULT CURRENT_TIMESTAMP(0) )
 PRIMARY INDEX (
OAT_VAL_MIN
)
--------------------------CREATE TABLE CTXSYS.DR$THS-------------------------------------
DROP TABLE ODS_DDL.O_CBI_DR$THS_H;
CREATE MULTISET TABLE ODS_DDL.O_CBI_DR$THS_H 
 (THS_ID decimal(22)   NOT NULL,
 THS_NAME varchar(30) CHARACTER SET UNICODE  NOT NULL,
 THS_OWNER# decimal(22)   NOT NULL,
 THS_CASE varchar(1) CHARACTER SET UNICODE  NOT NULL,
 DW_START_DT   TITLE '开始日期' DATE FORMAT 'YYYY-MM-DD' NULL ,
 DW_END_DT   TITLE '结束日期' DATE FORMAT 'YYYY-MM-DD' NULL ,
 DW_ETL_DT   TITLE '翻牌日期' DATE FORMAT 'YYYY-MM-DD' NULL ,
 DW_UPD_TM TIMESTAMP(0)  TITLE '更新时间'  DEFAULT CURRENT_TIMESTAMP(0) )
 PRIMARY INDEX (
THS_ID
)
--------------------------CREATE TABLE SYS.AW$AWREPORT-------------------------------------
DROP TABLE ODS_DDL.O_CBI_AW$AWREPORT_H;
CREATE MULTISET TABLE ODS_DDL.O_CBI_AW$AWREPORT_H 
 (PS# decimal(10)   NULL,
 GEN# decimal(10)   NULL,
 EXTNUM decimal(8)   NULL,
 AWLOB varchar(1234) CHARACTER SET UNICODE  NULL,
 OBJNAME varchar(256) CHARACTER SET UNICODE  NULL,
 PARTNAME varchar(256) CHARACTER SET UNICODE  NULL,
 DW_START_DT   TITLE '开始日期' DATE FORMAT 'YYYY-MM-DD' NULL ,
 DW_END_DT   TITLE '结束日期' DATE FORMAT 'YYYY-MM-DD' NULL ,
 DW_ETL_DT   TITLE '翻牌日期' DATE FORMAT 'YYYY-MM-DD' NULL ,
 DW_UPD_TM TIMESTAMP(0)  TITLE '更新时间'  DEFAULT CURRENT_TIMESTAMP(0) )
 PRIMARY INDEX (
GEN#
)
--------------------------CREATE TABLE SYS.HS$_PARALLEL_METADATA-------------------------------------
DROP TABLE ODS_DDL.O_CBI_HS$_PARALLEL_METADATA_H;
CREATE MULTISET TABLE ODS_DDL.O_CBI_HS$_PARALLEL_METADATA_H 
 (DBLINK varchar(128) CHARACTER SET UNICODE  NOT NULL,
 REMOTE_TABLE_NAME varchar(30) CHARACTER SET UNICODE  NOT NULL,
 REMOTE_SCHEMA_NAME varchar(30) CHARACTER SET UNICODE  NOT NULL,
 PARALLEL varchar(1) CHARACTER SET UNICODE  NOT NULL,
 PARALLEL_DEGREE decimal(22)   NOT NULL,
 RANGE_PARTITIONED varchar(1) CHARACTER SET UNICODE  NOT NULL,
 SAMPLED varchar(1) CHARACTER SET UNICODE  NOT NULL,
 HISTOGRAM varchar(1) CHARACTER SET UNICODE  NOT NULL,
 IND_AVAILABLE varchar(1) CHARACTER SET UNICODE  NOT NULL,
 SAMPLE_CAP varchar(1) CHARACTER SET UNICODE  NOT NULL,
 HIST_COLUMN varchar(30) CHARACTER SET UNICODE  NULL,
 HIST_COLUMN_TYPE varchar(30) CHARACTER SET UNICODE  NULL,
 SAMPLE_COLUMN varchar(30) CHARACTER SET UNICODE  NULL,
 SAMPLE_COLUMN_TYPE varchar(30) CHARACTER SET UNICODE  NULL,
 NUM_PARTITIONS decimal(22)   NULL,
 NUM_PARTITION_COLUMNS decimal(22)   NULL,
 PARTITION_COL_NAMES varchar(16) CHARACTER SET UNICODE  NULL,
 PARTITION_COL_TYPES varchar(16) CHARACTER SET UNICODE  NULL,
 NCOL_MIN_VAL decimal(22)   NULL,
 NCOL_AVG_VAL decimal(22)   NULL,
 NCOL_MAX_VAL decimal(22)   NULL,
 NUM_BUCKETS decimal(22)   NULL,
 DW_START_DT   TITLE '开始日期' DATE FORMAT 'YYYY-MM-DD' NULL ,
 DW_END_DT   TITLE '结束日期' DATE FORMAT 'YYYY-MM-DD' NULL ,
 DW_ETL_DT   TITLE '翻牌日期' DATE FORMAT 'YYYY-MM-DD' NULL ,
 DW_UPD_TM TIMESTAMP(0)  TITLE '更新时间'  DEFAULT CURRENT_TIMESTAMP(0) )
 PRIMARY INDEX (
HISTOGRAM,HIST_COLUMN_TYPE,NUM_PARTITIONS
)
--------------------------CREATE TABLE SYS.DUAL-------------------------------------
DROP TABLE ODS_DDL.O_CBI_DUAL;
CREATE MULTISET TABLE ODS_DDL.O_CBI_DUAL 
 (DUMMY varchar(1) CHARACTER SET UNICODE  NULL,
 DW_OPER_FLAG SMALLINT  TITLE '操作标识' DEFAULT 1 ,
 DW_ETL_DT    TITLE '翻牌日期' DATE FORMAT 'YYYY-MM-DD' NULL ,
 DW_UPD_TM TIMESTAMP(0) TITLE '更新时间' DEFAULT CURRENT_TIMESTAMP(0) )
 PRIMARY INDEX (
DUMMY
)
--------------------------CREATE TABLE CTXSYS.DR$NUMBER_SEQUENCE-------------------------------------
DROP TABLE ODS_DDL.O_CBI_DR$NUMBER_SEQUENCE;
CREATE MULTISET TABLE ODS_DDL.O_CBI_DR$NUMBER_SEQUENCE 
 (NUM decimal(22)   NULL,
 DW_OPER_FLAG SMALLINT  TITLE '操作标识' DEFAULT 1 ,
 DW_ETL_DT    TITLE '翻牌日期' DATE FORMAT 'YYYY-MM-DD' NULL ,
 DW_UPD_TM TIMESTAMP(0) TITLE '更新时间' DEFAULT CURRENT_TIMESTAMP(0) )
 PRIMARY INDEX (
NUM
)
--------------------------CREATE TABLE SYS.KU$_DATAPUMP_MASTER_11_1-------------------------------------
DROP TABLE ODS_DDL.O_CBI_KU$_DATAPUMP_MASTER_11_1;
CREATE MULTISET TABLE ODS_DDL.O_CBI_KU$_DATAPUMP_MASTER_11_1 
 (PROCESS_ORDER decimal(22)   NULL,
 DUPLICATE decimal(22)   NULL,
 DUMP_FILEID decimal(22)   NULL,
 DUMP_POSITION decimal(22)   NULL,
 DUMP_LENGTH decimal(22)   NULL,
 DUMP_ORIG_LENGTH decimal(22)   NULL,
 DUMP_ALLOCATION decimal(22)   NULL,
 COMPLETED_ROWS decimal(22)   NULL,
 ERROR_COUNT decimal(22)   NULL,
 ELAPSED_TIME decimal(22)   NULL,
 OBJECT_TYPE_PATH varchar(200) CHARACTER SET UNICODE  NULL,
 OBJECT_PATH_SEQNO decimal(22)   NULL,
 OBJECT_TYPE varchar(30) CHARACTER SET UNICODE  NULL,
 IN_PROGRESS char(1)   NULL,
 OBJECT_NAME varchar(500) CHARACTER SET UNICODE  NULL,
 OBJECT_LONG_NAME varchar(4000) CHARACTER SET UNICODE  NULL,
 OBJECT_SCHEMA varchar(30) CHARACTER SET UNICODE  NULL,
 ORIGINAL_OBJECT_SCHEMA varchar(30) CHARACTER SET UNICODE  NULL,
 PARTITION_NAME varchar(30) CHARACTER SET UNICODE  NULL,
 SUBPARTITION_NAME varchar(30) CHARACTER SET UNICODE  NULL,
 FLAGS decimal(22)   NULL,
 PROPERTY decimal(22)   NULL,
 TRIGFLAG decimal(22)   NULL,
 CREATION_LEVEL decimal(22)   NULL,
 COMPLETION_TIME timestamp(6)   NULL,
 OBJECT_TABLESPACE varchar(30) CHARACTER SET UNICODE  NULL,
 SIZE_ESTIMATE decimal(22)   NULL,
 OBJECT_ROW decimal(22)   NULL,
 PROCESSING_STATE char(1)   NULL,
 PROCESSING_STATUS char(1)   NULL,
 BASE_PROCESS_ORDER decimal(22)   NULL,
 BASE_OBJECT_TYPE varchar(30) CHARACTER SET UNICODE  NULL,
 BASE_OBJECT_NAME varchar(30) CHARACTER SET UNICODE  NULL,
 BASE_OBJECT_SCHEMA varchar(30) CHARACTER SET UNICODE  NULL,
 ANCESTOR_PROCESS_ORDER decimal(22)   NULL,
 DOMAIN_PROCESS_ORDER decimal(22)   NULL,
 PARALLELIZATION decimal(22)   NULL,
 UNLOAD_METHOD decimal(22)   NULL,
 LOAD_METHOD decimal(22)   NULL,
 GRANULES decimal(22)   NULL,
 SCN decimal(22)   NULL,
 GRANTOR varchar(30) CHARACTER SET UNICODE  NULL,
 XML_CLOB varchar(1234) CHARACTER SET UNICODE  NULL,
 PARENT_PROCESS_ORDER decimal(22)   NULL,
 NAME varchar(30) CHARACTER SET UNICODE  NULL,
 VALUE_T varchar(4000) CHARACTER SET UNICODE  NULL,
 VALUE_N decimal(22)   NULL,
 IS_DEFAULT decimal(22)   NULL,
 FILE_TYPE decimal(22)   NULL,
 USER_DIRECTORY varchar(4000) CHARACTER SET UNICODE  NULL,
 USER_FILE_NAME varchar(4000) CHARACTER SET UNICODE  NULL,
 FILE_NAME varchar(4000) CHARACTER SET UNICODE  NULL,
 EXTEND_SIZE decimal(22)   NULL,
 FILE_MAX_SIZE decimal(22)   NULL,
 PROCESS_NAME varchar(30) CHARACTER SET UNICODE  NULL,
 LAST_UPDATE timestamp(6)   NULL,
 WORK_ITEM varchar(30) CHARACTER SET UNICODE  NULL,
 OBJECT_NUMBER decimal(22)   NULL,
 COMPLETED_BYTES decimal(22)   NULL,
 TOTAL_BYTES decimal(22)   NULL,
 METADATA_IO decimal(22)   NULL,
 DATA_IO decimal(22)   NULL,
 CUMULATIVE_TIME decimal(22)   NULL,
 PACKET_NUMBER decimal(22)   NULL,
 OLD_VALUE varchar(4000) CHARACTER SET UNICODE  NULL,
 SEED decimal(22)   NULL,
 LAST_FILE decimal(22)   NULL,
 USER_NAME varchar(30) CHARACTER SET UNICODE  NULL,
 OPERATION_OG varchar(30) CHARACTER SET UNICODE  NULL,
 JOB_MODE varchar(30) CHARACTER SET UNICODE  NULL,
 CONTROL_QUEUE varchar(30) CHARACTER SET UNICODE  NULL,
 STATUS_QUEUE varchar(30) CHARACTER SET UNICODE  NULL,
 REMOTE_LINK varchar(4000) CHARACTER SET UNICODE  NULL,
 VERSION decimal(22)   NULL,
 JOB_VERSION varchar(30) CHARACTER SET UNICODE  NULL,
 DB_VERSION varchar(30) CHARACTER SET UNICODE  NULL,
 TIMEZONE varchar(64) CHARACTER SET UNICODE  NULL,
 STATE_OG varchar(30) CHARACTER SET UNICODE  NULL,
 PHASE decimal(22)   NULL,
 GUID varchar(16) CHARACTER SET UNICODE  NULL,
 START_TIME timestamp(6)   NULL,
 BLOCK_SIZE decimal(22)   NULL,
 METADATA_BUFFER_SIZE decimal(22)   NULL,
 DATA_BUFFER_SIZE decimal(22)   NULL,
 DEGREE decimal(22)   NULL,
 PLATFORM varchar(101) CHARACTER SET UNICODE  NULL,
 ABORT_STEP decimal(22)   NULL,
 INSTANCE varchar(60) CHARACTER SET UNICODE  NULL,
 CLUSTER_OK decimal(22)   NULL,
 SERVICE_NAME varchar(100) CHARACTER SET UNICODE  NULL,
 DW_OPER_FLAG SMALLINT  TITLE '操作标识' DEFAULT 1 ,
 DW_ETL_DT    TITLE '翻牌日期' DATE FORMAT 'YYYY-MM-DD' NULL ,
 DW_UPD_TM TIMESTAMP(0) TITLE '更新时间' DEFAULT CURRENT_TIMESTAMP(0) )
 PRIMARY INDEX (
XML_CLOB
)
--------------------------CREATE TABLE SYSTEM.MVIEW$_ADV_PARTITION-------------------------------------
DROP TABLE ODS_DDL.O_CBI_MVIEW$_ADV_PARTITION;
CREATE MULTISET TABLE ODS_DDL.O_CBI_MVIEW$_ADV_PARTITION 
 (RUNID# decimal(22)   NOT NULL,
 RANK# decimal(22)   NOT NULL,
 SUMMARY_OWNER varchar(32) CHARACTER SET UNICODE  NULL,
 QUERY_TEXT varchar(1234) CHARACTER SET UNICODE  NULL,
 DW_OPER_FLAG SMALLINT  TITLE '操作标识' DEFAULT 1 ,
 DW_ETL_DT    TITLE '翻牌日期' DATE FORMAT 'YYYY-MM-DD' NULL ,
 DW_UPD_TM TIMESTAMP(0) TITLE '更新时间' DEFAULT CURRENT_TIMESTAMP(0) )
 PRIMARY INDEX (
RANK#
)
--------------------------CREATE TABLE SYS.HS_BULKLOAD_VIEW_OBJ-------------------------------------
DROP TABLE ODS_DDL.O_CBI_HS_BULKLOAD_VIEW_OBJ;
CREATE MULTISET TABLE ODS_DDL.O_CBI_HS_BULKLOAD_VIEW_OBJ 
 (SCHEMA_NAME varchar(30) CHARACTER SET UNICODE  NULL,
 VIEW_NAME varchar(30) CHARACTER SET UNICODE  NULL,
 TEMP_OBJ_ID decimal(22)   NULL,
 DW_OPER_FLAG SMALLINT  TITLE '操作标识' DEFAULT 1 ,
 DW_ETL_DT    TITLE '翻牌日期' DATE FORMAT 'YYYY-MM-DD' NULL ,
 DW_UPD_TM TIMESTAMP(0) TITLE '更新时间' DEFAULT CURRENT_TIMESTAMP(0) )
 PRIMARY INDEX (
SCHEMA_NAME
)
--------------------------CREATE TABLE SYS.AW$AWCREATE10G-------------------------------------
DROP TABLE ODS_DDL.O_CBI_AW$AWCREATE10G;
CREATE MULTISET TABLE ODS_DDL.O_CBI_AW$AWCREATE10G 
 (PS# decimal(10)   NULL,
 GEN# decimal(10)   NULL,
 EXTNUM decimal(8)   NULL,
 AWLOB varchar(1234) CHARACTER SET UNICODE  NULL,
 OBJNAME varchar(256) CHARACTER SET UNICODE  NULL,
 PARTNAME varchar(256) CHARACTER SET UNICODE  NULL,
 DW_OPER_FLAG SMALLINT  TITLE '操作标识' DEFAULT 1 ,
 DW_ETL_DT    TITLE '翻牌日期' DATE FORMAT 'YYYY-MM-DD' NULL ,
 DW_UPD_TM TIMESTAMP(0) TITLE '更新时间' DEFAULT CURRENT_TIMESTAMP(0) )
 PRIMARY INDEX (
GEN#
)
--------------------------CREATE TABLE MDSYS.SDO_CS_SRS-------------------------------------
DROP TABLE ODS_DDL.O_CBI_SDO_CS_SRS;
CREATE MULTISET TABLE ODS_DDL.O_CBI_SDO_CS_SRS 
 (CS_NAME varchar(80) CHARACTER SET UNICODE  NULL,
 SRID decimal(22)   NOT NULL,
 AUTH_SRID decimal(22)   NULL,
 AUTH_NAME varchar(256) CHARACTER SET UNICODE  NULL,
 WKTEXT varchar(2046) CHARACTER SET UNICODE  NULL,
 CS_BOUNDS varchar(1) CHARACTER SET UNICODE  NULL,
 WKTEXT3D varchar(4000) CHARACTER SET UNICODE  NULL,
 DW_OPER_FLAG SMALLINT  TITLE '操作标识' DEFAULT 1 ,
 DW_ETL_DT    TITLE '翻牌日期' DATE FORMAT 'YYYY-MM-DD' NULL ,
 DW_UPD_TM TIMESTAMP(0) TITLE '更新时间' DEFAULT CURRENT_TIMESTAMP(0) )
 PRIMARY INDEX (
SRID
)
--------------------------CREATE TABLE SYS.AW$EXPRESS-------------------------------------
DROP TABLE ODS_DDL.O_CBI_AW$EXPRESS;
CREATE MULTISET TABLE ODS_DDL.O_CBI_AW$EXPRESS 
 (PS# decimal(10)   NULL,
 GEN# decimal(10)   NULL,
 EXTNUM decimal(8)   NULL,
 AWLOB varchar(1234) CHARACTER SET UNICODE  NULL,
 OBJNAME varchar(256) CHARACTER SET UNICODE  NULL,
 PARTNAME varchar(256) CHARACTER SET UNICODE  NULL,
 DW_OPER_FLAG SMALLINT  TITLE '操作标识' DEFAULT 1 ,
 DW_ETL_DT    TITLE '翻牌日期' DATE FORMAT 'YYYY-MM-DD' NULL ,
 DW_UPD_TM TIMESTAMP(0) TITLE '更新时间' DEFAULT CURRENT_TIMESTAMP(0) )
 PRIMARY INDEX (
PARTNAME
)
--------------------------CREATE TABLE APEX_030200.WWV_FLOW_TEMP_TABLE-------------------------------------
DROP TABLE ODS_DDL.O_CBI_WWV_FLOW_TEMP_TABLE_H;
CREATE MULTISET TABLE ODS_DDL.O_CBI_WWV_FLOW_TEMP_TABLE_H 
 (R decimal(22)   NULL,
 C001 varchar(4000) CHARACTER SET UNICODE  NULL,
 C002 varchar(4000) CHARACTER SET UNICODE  NULL,
 C003 varchar(4000) CHARACTER SET UNICODE  NULL,
 C004 varchar(4000) CHARACTER SET UNICODE  NULL,
 C005 varchar(4000) CHARACTER SET UNICODE  NULL,
 C006 varchar(4000) CHARACTER SET UNICODE  NULL,
 C007 varchar(4000) CHARACTER SET UNICODE  NULL,
 C008 varchar(4000) CHARACTER SET UNICODE  NULL,
 C009 varchar(4000) CHARACTER SET UNICODE  NULL,
 C010 varchar(4000) CHARACTER SET UNICODE  NULL,
 C011 varchar(4000) CHARACTER SET UNICODE  NULL,
 C012 varchar(4000) CHARACTER SET UNICODE  NULL,
 C013 varchar(4000) CHARACTER SET UNICODE  NULL,
 C014 varchar(4000) CHARACTER SET UNICODE  NULL,
 C015 varchar(4000) CHARACTER SET UNICODE  NULL,
 C016 varchar(4000) CHARACTER SET UNICODE  NULL,
 C017 varchar(4000) CHARACTER SET UNICODE  NULL,
 C018 varchar(4000) CHARACTER SET UNICODE  NULL,
 C019 varchar(4000) CHARACTER SET UNICODE  NULL,
 C020 varchar(4000) CHARACTER SET UNICODE  NULL,
 C021 varchar(4000) CHARACTER SET UNICODE  NULL,
 C022 varchar(4000) CHARACTER SET UNICODE  NULL,
 C023 varchar(4000) CHARACTER SET UNICODE  NULL,
 C024 varchar(4000) CHARACTER SET UNICODE  NULL,
 C025 varchar(4000) CHARACTER SET UNICODE  NULL,
 C026 varchar(4000) CHARACTER SET UNICODE  NULL,
 C027 varchar(4000) CHARACTER SET UNICODE  NULL,
 C028 varchar(4000) CHARACTER SET UNICODE  NULL,
 C029 varchar(4000) CHARACTER SET UNICODE  NULL,
 C030 varchar(4000) CHARACTER SET UNICODE  NULL,
 C031 varchar(4000) CHARACTER SET UNICODE  NULL,
 C032 varchar(4000) CHARACTER SET UNICODE  NULL,
 C033 varchar(4000) CHARACTER SET UNICODE  NULL,
 C034 varchar(4000) CHARACTER SET UNICODE  NULL,
 C035 varchar(4000) CHARACTER SET UNICODE  NULL,
 C036 varchar(4000) CHARACTER SET UNICODE  NULL,
 C037 varchar(4000) CHARACTER SET UNICODE  NULL,
 C038 varchar(4000) CHARACTER SET UNICODE  NULL,
 C039 varchar(4000) CHARACTER SET UNICODE  NULL,
 C040 varchar(4000) CHARACTER SET UNICODE  NULL,
 C041 varchar(4000) CHARACTER SET UNICODE  NULL,
 C042 varchar(4000) CHARACTER SET UNICODE  NULL,
 C043 varchar(4000) CHARACTER SET UNICODE  NULL,
 C044 varchar(4000) CHARACTER SET UNICODE  NULL,
 C045 varchar(4000) CHARACTER SET UNICODE  NULL,
 C046 varchar(4000) CHARACTER SET UNICODE  NULL,
 C047 varchar(4000) CHARACTER SET UNICODE  NULL,
 C048 varchar(4000) CHARACTER SET UNICODE  NULL,
 C049 varchar(4000) CHARACTER SET UNICODE  NULL,
 C050 varchar(4000) CHARACTER SET UNICODE  NULL,
 C051 varchar(4000) CHARACTER SET UNICODE  NULL,
 C052 varchar(4000) CHARACTER SET UNICODE  NULL,
 C053 varchar(4000) CHARACTER SET UNICODE  NULL,
 C054 varchar(4000) CHARACTER SET UNICODE  NULL,
 C055 varchar(4000) CHARACTER SET UNICODE  NULL,
 C056 varchar(4000) CHARACTER SET UNICODE  NULL,
 C057 varchar(4000) CHARACTER SET UNICODE  NULL,
 C058 varchar(4000) CHARACTER SET UNICODE  NULL,
 C059 varchar(4000) CHARACTER SET UNICODE  NULL,
 C060 varchar(4000) CHARACTER SET UNICODE  NULL,
 C061 varchar(4000) CHARACTER SET UNICODE  NULL,
 C062 varchar(4000) CHARACTER SET UNICODE  NULL,
 C063 varchar(4000) CHARACTER SET UNICODE  NULL,
 C064 varchar(4000) CHARACTER SET UNICODE  NULL,
 C065 varchar(4000) CHARACTER SET UNICODE  NULL,
 DW_START_DT   TITLE '开始日期' DATE FORMAT 'YYYY-MM-DD' NULL ,
 DW_END_DT   TITLE '结束日期' DATE FORMAT 'YYYY-MM-DD' NULL ,
 DW_ETL_DT   TITLE '翻牌日期' DATE FORMAT 'YYYY-MM-DD' NULL ,
 DW_UPD_TM TIMESTAMP(0)  TITLE '更新时间'  DEFAULT CURRENT_TIMESTAMP(0) )
 PRIMARY INDEX (
C054,C061
)
--------------------------CREATE TABLE MDSYS.SDO_ELLIPSOIDS_OLD_SNAPSHOT-------------------------------------
DROP TABLE ODS_DDL.O_CBI_SDO_ELLIPSOIDS_OLD_SNAPSHOT;
CREATE MULTISET TABLE ODS_DDL.O_CBI_SDO_ELLIPSOIDS_OLD_SNAPSHOT 
 (NAME varchar(64) CHARACTER SET UNICODE  NULL,
 SEMI_MAJOR_AXIS decimal(22)   NULL,
 INVERSE_FLATTENING decimal(22)   NULL,
 DW_OPER_FLAG SMALLINT  TITLE '操作标识' DEFAULT 1 ,
 DW_ETL_DT    TITLE '翻牌日期' DATE FORMAT 'YYYY-MM-DD' NULL ,
 DW_UPD_TM TIMESTAMP(0) TITLE '更新时间' DEFAULT CURRENT_TIMESTAMP(0) )
 PRIMARY INDEX (
SEMI_MAJOR_AXIS
)
--------------------------CREATE TABLE SYS.TABLE_PRIVILEGE_MAP-------------------------------------
DROP TABLE ODS_DDL.O_CBI_TABLE_PRIVILEGE_MAP;
CREATE MULTISET TABLE ODS_DDL.O_CBI_TABLE_PRIVILEGE_MAP 
 (PRIVILEGE decimal(22)  TITLE 'Numeric privilege (auditing option) type code' NOT NULL,
 NAME varchar(40) CHARACTER SET UNICODE TITLE 'Name of the type of privilege (auditing option)' NOT NULL,
 DW_OPER_FLAG SMALLINT  TITLE '操作标识' DEFAULT 1 ,
 DW_ETL_DT    TITLE '翻牌日期' DATE FORMAT 'YYYY-MM-DD' NULL ,
 DW_UPD_TM TIMESTAMP(0) TITLE '更新时间' DEFAULT CURRENT_TIMESTAMP(0) )
 PRIMARY INDEX (
NAME
)
