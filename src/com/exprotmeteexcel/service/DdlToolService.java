package com.exprotmeteexcel.service;

import java.util.List;

import com.exprotmeteexcel.bean.MateColumnsBean;
/**
 * 导出DDl的Service接口
 * @author admin
 *
 */
public interface DdlToolService {
	
	/**
	 * 得到配置导出原数据的类型、字段、长度等
	 * 
	 * @param ownTable ：表
	 *            
	 * @param cols  ：字段信息
	 *            ：数据库语句发送对象
	 * @return String
	 *            ：表的DDL
	 */
	public  String getDdlStr(String ownTable, List<MateColumnsBean> cols);
	/**
	 * 得到ods数据正常数据导出原数据的类型、字段、长度等
	 * 
	 * @param ownTable
	 *            ：表
	 * 
	 * @param cols
	 *            ：字段信息 ：数据库语句发送对象
	 * @return String ：ods表的DDL
	 */
	public String getOdsDdlStr(String ownTable, List<MateColumnsBean> cols);
	
	/**
	 * 得到CK表配置导出原数据的类型、字段、长度等
	 * 
	 * @param ownTable
	 *            ：表
	 * 
	 * @param cols
	 *            ：字段信息 ：数据库语句发送对象
	 * @return String ：表的DDL
	 */
	public String getCkDdlStr(String ownTable, List<MateColumnsBean> cols);
	/**
	 * 得到配置导出原数据的类型、字段、长度等
	 * 
	 * @param path ：excel路径
	 *            
	 * @return Boolean
	 *            ：导出DDL
	 */
	public  Boolean exportDdl(String path) ;

}
