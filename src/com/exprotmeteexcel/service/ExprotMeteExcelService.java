package com.exprotmeteexcel.service;

import java.util.List;
import java.util.Map;

import com.exprotmeteexcel.bean.MateBean;
import com.exprotmeteexcel.bean.MateColumnsBean;
/**
 * 原数据导出Service接口
 * @author admin
 *
 */
public interface ExprotMeteExcelService {
	
	/**
	 * 得到配置导出原数据的类型、字段、长度等
	 * 
	 * @param path ：路径
	 *            
	 * @param mb  ：要导的原数据
	 *            ：数据库语句发送对象
	 * @return List
	 *            ：结果集
	 */
	public List<MateColumnsBean> getTableColumn(String path, MateBean mb);
   
	
	/**
	 * 得到配置导出的各库的原数据表
	 * 
	 * @param path ：路径
	 * @return  list ：结果集
	 */
	public List<Map<String, Object>> getTableColumn(String path);
	//得到配置导出的原数据表
	
	/**
	 * 导出的原数据表
	 * 
	 * @param path ：路径
	 * @return Boolean
	 *            ：成功 失败
	 */
	public Boolean ExprotExcel(List<MateColumnsBean> meta, String outputprth);
    
	
	/**
	 * 得到配置导出的原数据表
	 * 
	 * @param path ：路径
	 * @return MateBean
	 *            ：配置表
	 */
	public MateBean getTdMate(String path,String plaform);
	
	//更新处理完成的配置数据
	/**
	 * 得到配置导出的原数据表
	 * 
	 * @param path ：路径
	 * @param status ：导出成功或失败
	 * @return update
	 *            ：update配置表状态
	 */
	public Boolean updateTdMate(String path, String status,String businessName) ;
	
	
	
	
	//更新处理完成的配置数据
	/**
	 * 得到配置导出的原数据表
	 * 
	 * @param path ：路径
	 * @return Boolean
	 *            ：Boolean
	 */
	//public Boolean ReadExcelExprot(String path);
	
	//更新处理完成的配置数据
	/**
	 * 读EXCEL生成tableDDl和转换TRANLIST
	 * 
	 * @param path ：路径
	 * @return map
	 *            ：tableDDl，TRANLIST的集合
	 */
	
	public Map<String,Object> ReadExcel(String path);

	/**
	 * 得到配置导出的原数据表
	 * 
	 * @param path ：路径
	 * @param listmc:转换的列表
	 * @return Boolean
	 *            ：Boolean
	 */
	public Boolean ExcelExprot(String path,List<MateColumnsBean> listmc);
	/**
	 * 得到配置导出的各库的原数据表，导出excal
	 * 
	 * @param path
	 *            ：路径
	 * @return Boolean ：是否执行成功
	 */

	public  Boolean ExprotTableMateBean(String path);
}
