package org.tools;

import com.informatica.powercenter.sdk.mapfwk.core.NativeDataTypes;

public class DataTypeTrans {
	
	public static String Trans(String DataType, String DbType){
		String sb = null;
		
		
		if(DbType == "Oracle"){
			switch(DataType.toString().substring(0, DataType.toString().indexOf("(")))
	        {
	        case "VARCHAR2": sb = NativeDataTypes.Oracle.VARCHAR2; break;
	        case "NUMBER": sb = NativeDataTypes.Oracle.NUMBER_PS; break;
	        case "DATE": sb = NativeDataTypes.Oracle.DATE; break;
	        case "BLOB": sb = NativeDataTypes.Oracle.BLOB; break;
	        case "CHAR": sb = NativeDataTypes.Oracle.CHAR; break;
	        case "CLOB": sb = NativeDataTypes.Oracle.CLOB; break;
	        case "LONG": sb = NativeDataTypes.Oracle.LONG; break;
	        case "LONGRAW": sb = NativeDataTypes.Oracle.LONGRAW; break;
	        case "NCHAR": sb = NativeDataTypes.Oracle.NCHAR; break;
	        case "NCLOB": sb = NativeDataTypes.Oracle.NCLOB; break;
	        case "TIMESTAMP": sb = NativeDataTypes.Oracle.TIMESTAMP; break;
	        case "VARCHAR": sb = NativeDataTypes.Oracle.VARCHAR; break;
	        default: sb = NativeDataTypes.Oracle.VARCHAR2; break; 
		
        }; 
		}else if(DbType == "TD"){
//			System.out.println(DataType.toString());
			switch(DataType.toString().substring(0, DataType.toString().indexOf("(")))
	        {
	        case "VARCHAR2": sb = NativeDataTypes.Teradata.VARCHAR; break;
	        case "NUMBER": sb = NativeDataTypes.Teradata.DECIMAL; break;
	        case "DATE": sb = NativeDataTypes.Teradata.TIMESTAMP; break;
	        case "CHAR": sb = NativeDataTypes.Teradata.CHAR; break;
	        case "NCHAR": sb = NativeDataTypes.Teradata.CHAR; break;
	        case "TIMESTAMP": sb = NativeDataTypes.Teradata.TIMESTAMP; break;
	        case "VARCHAR": sb = NativeDataTypes.Teradata.VARCHAR; break;
	        case "BIGINT": sb = NativeDataTypes.Teradata.BIGINT; break;
	        case "DECIMAL": sb = NativeDataTypes.Teradata.DECIMAL; break;
	        case "DATETIME": sb = NativeDataTypes.Teradata.TIMESTAMP; break;
	        case "INT": sb = NativeDataTypes.Teradata.INTEGER; break;
	        case "TINYINT": sb = NativeDataTypes.Teradata.SMALLINT; break;
	        case "BIT": sb = NativeDataTypes.Teradata.SMALLINT; break;
	        case "DOUBLE": sb = NativeDataTypes.Teradata.DECIMAL; break;
	        case "FLOAT": sb = NativeDataTypes.Teradata.DECIMAL; break;
	        case "TEXT": sb = NativeDataTypes.Teradata.VARCHAR; break;
	        case "SMALLINT": sb = NativeDataTypes.Teradata.SMALLINT; break;
	        default: sb = NativeDataTypes.Teradata.VARCHAR; break; 
	        };
		}else if(DbType == "Mysql"){
			switch(DataType.toString().substring(0, DataType.toString().indexOf("(")))
	        {
	        case "BIGINT": sb = NativeDataTypes.ODBC.BIGINT; break;
	        case "INT": sb = NativeDataTypes.ODBC.INTEGER; break;
	        case "SMALLINT": sb = NativeDataTypes.ODBC.SMALLINT; break;
	        case "DATETIME": sb = NativeDataTypes.ODBC.TIMESTAMP; break;
	        case "TIMESTAMP": sb = NativeDataTypes.ODBC.TIMESTAMP; break;
	        case "CHAR": sb = NativeDataTypes.ODBC.CHAR; break;
	        case "DATE": sb = NativeDataTypes.ODBC.DATE; break;
	        case "DECIMAL": sb = NativeDataTypes.ODBC.DECIMAL; break;
	        case "DOUBLE": sb = NativeDataTypes.ODBC.DECIMAL; break;
	        case "FLOAT": sb = NativeDataTypes.ODBC.DECIMAL; break;
	        case "VARCHAR": sb = NativeDataTypes.ODBC.VARCHAR; break;
	        case "TINYINT": sb = NativeDataTypes.ODBC.INTEGER; break;
	        case "BIT": sb = NativeDataTypes.ODBC.SMALLINT; break;
	        case "TEXT": sb = NativeDataTypes.ODBC.LONG_VARBINARY; break;
	        default: sb = NativeDataTypes.Teradata.VARCHAR; break; 
	        };
			
		}else if(DbType == "MSSQL"){
//			System.out.println(DataType.toString()+"++++++++++++++++");
			switch(DataType.toString().substring(0, DataType.toString().indexOf("(")).toUpperCase())
	        {
	        case "BIGINT": sb = NativeDataTypes.SqlServer.BIGINT; break;
	        case "INT": sb = NativeDataTypes.SqlServer.INT; break;
	        case "SMALLINT": sb = NativeDataTypes.SqlServer.SMALLINT; break;
	        case "DATETIME": sb = NativeDataTypes.SqlServer.DATETIME; break;
	        case "TIMESTAMP": sb = NativeDataTypes.SqlServer.TIMESTAMP; break;
	        case "CHAR": sb = NativeDataTypes.SqlServer.CHAR; break;
	        case "DATE": sb = NativeDataTypes.SqlServer.TIMESTAMP; break;
	        case "DECIMAL": sb = NativeDataTypes.SqlServer.DECIMAL; break;
	        case "VARCHAR": sb = NativeDataTypes.SqlServer.VARCHAR; break;
	        case "NTEXT": sb = NativeDataTypes.SqlServer.NTEXT; break;
	        case "TINYINT": sb = NativeDataTypes.SqlServer.TINYINT; break;
	        case "BIT": sb = NativeDataTypes.SqlServer.BIT; break;
	        default: sb = NativeDataTypes.Teradata.VARCHAR; break; 
	        };
			
		}
		return sb;
	}

	public static void main(String[] args) {
		// TODO Auto-generated method stub
//		System.out.println("Mysql".toUpperCase());

	}

}
