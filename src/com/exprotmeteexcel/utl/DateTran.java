package com.exprotmeteexcel.utl;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.InputStream;
import java.util.List;

import com.informatica.powercenter.sdk.mapfwk.core.NativeDataTypes;
/**
 * 数据类型转换工具类
 * 
 * @author wujunqing
 * @date 2017-06-26 
 */
public class DateTran {
	public static class DataTypeTrans {
		/**
		 * 解析excel配置，返回数据转换类型
		 * 
		 * @param DataType
		 *            字段数据类型
		 * @param DbType
		 *            数据库类型
		 * @return 返回数据转换类型
		 */
		public static String Trans(String DataType, String DbType) {
			String sb = null;
			String datetype = DataType.indexOf("(") > 0
					? DataType.toString().substring(0, DataType.toString().indexOf("(")) : DataType;
			if ("oracle".equals(DbType)) {
				switch (datetype) {
				case "VARCHAR2":
					sb = NativeDataTypes.Oracle.VARCHAR2;
					break;
				case "NUMBER":
					sb = NativeDataTypes.Oracle.NUMBER_PS;
					break;
				case "DATE":
					sb = NativeDataTypes.Oracle.DATE;
					break;
				case "BLOB":
					sb = NativeDataTypes.Oracle.BLOB;
					break;
				case "CHAR":
					sb = NativeDataTypes.Oracle.CHAR;
					break;
				case "CLOB":
					sb = NativeDataTypes.Oracle.CLOB;
					break;
				case "LONG":
					sb = NativeDataTypes.Oracle.LONG;
					break;
				case "LONGRAW":
					sb = NativeDataTypes.Oracle.LONGRAW;
					break;
				case "NCHAR":
					sb = NativeDataTypes.Oracle.NCHAR;
					break;
				case "NCLOB":
					sb = NativeDataTypes.Oracle.NCLOB;
					break;
				case "TIMESTAMP":
					sb = NativeDataTypes.Oracle.TIMESTAMP;
					break;
				case "VARCHAR":
					sb = NativeDataTypes.Oracle.VARCHAR;
					break;
				default:
					sb = NativeDataTypes.Oracle.VARCHAR2;
					break;

				}
				;
			} else if ("teradata".equals(DbType)) {
				System.out.println(DataType.toString());
				switch (datetype) {
				case "VARCHAR2":
					sb = NativeDataTypes.Teradata.VARCHAR;
					break;
				case "NUMBER":
					sb = NativeDataTypes.Teradata.DECIMAL;
					break;
				case "DATE":
					sb = NativeDataTypes.Teradata.TIMESTAMP;
					break;
				case "CHAR":
					sb = NativeDataTypes.Teradata.CHAR;
					break;
				case "NCHAR":
					sb = NativeDataTypes.Teradata.CHAR;
					break;
				case "TIMESTAMP":
					sb = NativeDataTypes.Teradata.TIMESTAMP;
					break;
				case "VARCHAR":
					sb = NativeDataTypes.Teradata.VARCHAR;
					break;
				case "BIGINT":
					sb = NativeDataTypes.Teradata.BIGINT;
					break;
				case "DECIMAL":
					sb = NativeDataTypes.Teradata.DECIMAL;
					break;
				case "DATETIME":
					sb = NativeDataTypes.Teradata.TIMESTAMP;
					break;
				case "INT":
					sb = NativeDataTypes.Teradata.INTEGER;
					break;
				case "TINYINT":
					sb = NativeDataTypes.Teradata.SMALLINT;
					break;
				case "BIT":
					sb = NativeDataTypes.Teradata.INTEGER;
					break;
				case "DOUBLE":
					sb = NativeDataTypes.Teradata.DECIMAL;
					break;
				case "FLOAT":
					sb = NativeDataTypes.Teradata.DECIMAL;
					break;
				case "TEXT":
					sb = NativeDataTypes.Teradata.VARCHAR;
					break;
				case "SMALLINT":
					sb = NativeDataTypes.Teradata.SMALLINT;
					break;
				default:
					sb = NativeDataTypes.Teradata.VARCHAR;
					break;
				}
				;

			} else if ("mysql".equals(DbType)) {
				switch (datetype) {
				case "BIGINT":
					sb = NativeDataTypes.ODBC.BIGINT;
					break;
				case "INT":
					sb = NativeDataTypes.ODBC.INTEGER;
					break;
				case "SMALLINT":
					sb = NativeDataTypes.ODBC.SMALLINT;
					break;
				case "DATETIME":
					sb = NativeDataTypes.ODBC.TIMESTAMP;
					break;
				case "TIMESTAMP":
					sb = NativeDataTypes.ODBC.TIMESTAMP;
					break;
				case "CHAR":
					sb = NativeDataTypes.ODBC.CHAR;
					break;
				case "DATE":
					sb = NativeDataTypes.ODBC.DATE;
					break;
				case "DECIMAL":
					sb = NativeDataTypes.ODBC.DECIMAL;
					break;
				case "DOUBLE":
					sb = NativeDataTypes.ODBC.DOUBLE;
					break;
				case "FLOAT":
					sb = NativeDataTypes.ODBC.FLOAT;
					break;
				case "VARCHAR":
					sb = NativeDataTypes.ODBC.VARCHAR;
					break;
				case "TINYINT":
					sb = NativeDataTypes.ODBC.INTEGER;
					break;
				case "TEXT":
					sb = NativeDataTypes.ODBC.LONG_VARBINARY;
					break;
				default:
					sb = NativeDataTypes.Teradata.VARCHAR;
					break;
				}
				;

			} else if ("mssql".equals(DbType)) {

				switch (DataType.toString().substring(0, DataType.toString().indexOf("(")).toUpperCase()) {
				case "BIGINT":
					sb = NativeDataTypes.SqlServer.BIGINT;
					break;
				case "INT":
					sb = NativeDataTypes.SqlServer.INT;
					break;
				case "SMALLINT":
					sb = NativeDataTypes.SqlServer.SMALLINT;
					break;
				case "DATETIME":
					sb = NativeDataTypes.SqlServer.DATETIME;
					break;
				case "TIMESTAMP":
					sb = NativeDataTypes.SqlServer.TIMESTAMP;
					break;
				case "CHAR":
					sb = NativeDataTypes.SqlServer.CHAR;
					break;
				case "DATE":
					sb = NativeDataTypes.SqlServer.TIMESTAMP;
					break;
				case "DECIMAL":
					sb = NativeDataTypes.SqlServer.DECIMAL;
					break;
				case "VARCHAR":
					sb = NativeDataTypes.SqlServer.VARCHAR;
					break;
				case "NTEXT":
					sb = NativeDataTypes.SqlServer.NTEXT;
					break;
				case "TINYINT":
					sb = NativeDataTypes.SqlServer.TINYINT;
					break;
				case "BIT":
					sb = NativeDataTypes.SqlServer.BIT;
					break;
				default:
					sb = NativeDataTypes.Teradata.VARCHAR;
					break;
				}
				;

			}
			return sb;
		}

		/**
		 * 解析excel配置，返回数据转换类型
		 * 
		 * @param DataType
		 *            字段数据类型
		 * @param DbType
		 *            数据库类型
		 * @param lt
		 *            配置数据转换集合
		 * @return 返回数据转换类型
		 */
		public static String TransByTd(String DataType, String DbType, List<Object[]> lt) {
			String sb = null;
			String datetype = DataType.indexOf("(") > 0
					? DataType.toString().substring(0, DataType.toString().indexOf("(")) : DataType;

			if (!Utl.isEmpty(lt)) {

				for (Object[] row : lt) {
					if (DbType.toUpperCase().equals(row[0].toString().toUpperCase()) && datetype.toUpperCase().equals(row[1].toString().toUpperCase())) {
						sb = row[2].toString();
						break;
					}
				}
			}
			if (Utl.isEmpty(sb)) {
				sb = "varchar";
			}
			return sb;
		}
		/**
		 * 解析excel配置，返回数据转换类型
		 * 
		 * @param DataType
		 *            字段数据类型
		 * @param DbType
		 *            数据库类型
		 * @return 返回数据转换类型
		 */
		public static String TransByTd(String DataType, String DbType) {
			String sb = null;
			String datetype = DataType.indexOf("(") > 0
					? DataType.toString().substring(0, DataType.toString().indexOf("(")) : DataType;
			List<Object[]> lt = ExcelUtility.getReadExcelContent("xls\\config\\SOURCE2PWC.xlsx", 1);
			
			if (!Utl.isEmpty(lt)) {

				for (Object[] row : lt) {
					if (DbType.toUpperCase().equals(row[0].toString().toUpperCase()) && datetype.toUpperCase().equals(row[1].toString().toUpperCase())) {
						sb = row[2].toString();
						break;
					}
				}
			}
			if (Utl.isEmpty(sb)) {
				sb = "varchar";
			}
			return sb;
		}

	}
}
