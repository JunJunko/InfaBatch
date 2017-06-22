package com.exprotmeteexcel.dao;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.List;
import java.util.Map;
/**
 * JDBC²Ù×÷DAO½Ó¿Ú
 * @author admin
 *
 */
public interface BaseDbDao {

	public Connection getConnection() throws SQLException;	
	
	public Boolean execute(String sql, String[] param);	
	
	public Boolean executeByCon(String sql, String[] param,Connection conn);
	
	public Integer executeSQL(String sql, String[] param) throws SQLException;
	
	public Integer executeSQLBatch(String[] sqls) throws SQLException;	
	
	public Integer executeSQLProc(String sql, String[] param) throws SQLException;
	
	public List<Map<String, String>> getDateForMapStr(String sql);	
	
	public List getDateForMap(String sql);	
	
	public int getCount(String sql);
	
}
