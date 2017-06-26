package com.exprotmeteexcel.service.imp;

import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.OutputStream;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.Properties;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.exprotmeteexcel.bean.MateBean;
import com.exprotmeteexcel.bean.MateColumnsBean;
import com.exprotmeteexcel.dao.impl.BaseDbDaoI;
import com.exprotmeteexcel.service.ExprotMeteExcelService;
import com.exprotmeteexcel.utl.DateTran.DataTypeTrans;
import com.exprotmeteexcel.utl.ExcelUtility;
import com.exprotmeteexcel.utl.Getjdbcconfig;
import com.exprotmeteexcel.utl.Utl;
import com.exprotmeteexcel.utl.global.BiaotiBean;
import com.exprotmeteexcel.utl.global.SQLGlobal;

/**
 * 原数据导出Service类
 * @author admin
 *
 */
public class ExprotMeteExcelServiceImpl implements ExprotMeteExcelService {

	private static final Logger log = LoggerFactory.getLogger(ExprotMeteExcelServiceImpl.class);

	@SuppressWarnings("null")
	@Override
	public List<MateColumnsBean> getTableColumn(String path, MateBean mb) {
		// TODO Auto-generated method stub
		Getjdbcconfig dbcof = new Getjdbcconfig(path);
		Properties yp = Utl.getProperties(path);
		BaseDbDaoI db = FactoryBaseDbDaoServiceImp.getBaseDbDaoI(path);
		List<MateColumnsBean> listmc = new ArrayList<MateColumnsBean>();
		List<Map<String, Object>> su = new ArrayList<Map<String, Object>>();
		su = db.getTableColumnByMeta(mb);
		List<Object[]> lt = ExcelUtility.getReadExcelContent("xls\\config\\config.xlsx", 1);
		/*
		 * String dbinfo; String owner; String tableName; String tranTableName;
		 * String tableRemark; String columnsName; String tranColumnName; String
		 * columnRemark; String columnDataType; String tranColumnDataType;
		 * String isNull; String defaultValue;
		 */
		/*
		 * String primaryKey; String piValue; String remark; String pl; String
		 * synchronousType; String dateCount; String dateSize; String
		 * updateTime; String synchronousLogic; String valid; String
		 * isGigDateCol;
		 */
		//log.info("插入行数：" + su.size());
		if (su != null) {
			for (int i = 0; i < su.size(); i++) {
				MateColumnsBean col = new MateColumnsBean();
				//原库配置信息
				col.setDbinfo(dbcof.getDbtype() + ":(" + dbcof.getIp() + ":" + dbcof.getPort() + ";sid:"
						+ dbcof.getDatabasename() + ")");
				//表的所属者
				col.setOwner(su.get(i).get("OWNER").toString());
				//表名
				col.setTableName(su.get(i).get("TABLE_NAME").toString());
				
				//字段名称
				col.setColumnsName(su.get(i).get("COLUMN_NAME").toString());
				//转换表名
				//表名加前缀O_XXX（参数化平台简称）、如果拉链表逻辑表后缀加_H存入“转换表名”列
				col.setTranTableName("O_"+yp.getProperty("SourceFolder") +"_"+ su.get(i).get("TABLE_NAME").toString().toUpperCase());
				col.setTableRemark("");
		
				
			    //转换字段
				//字段名包含关键字的加_OG存入“转换字段名”列	
				
				List<String> trnsCloumn = new ArrayList<String>();
				Collections.addAll(trnsCloumn, yp.get("OGCloumn").toString().toUpperCase().split(","));
				String trnsCloumnName=trnsCloumn.contains(su.get(i).get("COLUMN_NAME").toString().toUpperCase()) ? su.get(i).get("COLUMN_NAME").toString()+"_OG" : su.get(i).get("COLUMN_NAME").toString();		
				col.setTranColumnName(trnsCloumnName.toUpperCase());
				
				col.setColumnRemark("");
				//是否大字段
				Properties p = Utl.getProperties("properties/Pub.properties");
				List<String> BigCloumn = new ArrayList<String>();
				Collections.addAll(BigCloumn, p.get("BigCloumn").toString().split(","));
				String typename = su.get(i).get("TYPE_NAME") == null || "".equals(su.get(i).get("TYPE_NAME").toString())
						? "" : su.get(i).get("TYPE_NAME").toString();
				String isBigCol=BigCloumn.contains(typename.toUpperCase()) ? "是" : "否";				
				//字段长度
				
				String datasize="(" + su.get(i).get("COLUMN_SIZE").toString() + ")";
				String transdatasize ="是".equals(isBigCol)?"(1234)":"(" + su.get(i).get("COLUMN_SIZE").toString() + ")";
				
				//字段类型转换
				String datatype = su.get(i).get("TYPE_NAME").toString();				
				String trandatatype = DataTypeTrans.TransByTd(datatype.toUpperCase(), "teradata",lt);
				col.setColumnDataType(datatype + datasize);
				col.setTranColumnDataType(trandatatype + transdatasize);
				
				//字段能否为空: NOT NULL:不为空 ; NULL:可为null
				col.setIsNull("NO".equals(su.get(i).get("ISNULL").toString())?"NOT NULL":"NULL");
				
				col.setDefaultValue(
						su.get(i).get("COLUMN_DEF") == null || "".equals(su.get(i).get("COLUMN_DEF").toString()) ? ""
								: su.get(i).get("COLUMN_DEF").toString());
				col.setPrimaryKey(
						su.get(i).get("PRIMARYKEY") == null || "".equals(su.get(i).get("PRIMARYKEY").toString()) ? null
								: su.get(i).get("PRIMARYKEY").toString());
				col.setPiValue("");
				col.setRemark("");
				col.setPl("");
				col.setSynchronousType("");
				col.setDateCount("");
				col.setDateSize("");
				Long date =System.currentTimeMillis();
				col.setUpdateTime(date);
				col.setSynchronousLogic("");
				col.setValid("");
				

				col.setIsGigDateCol(isBigCol);
				listmc.add(col);

			}
		}
		return listmc;
	}

	@Override
	public Boolean ExprotExcel(List<MateColumnsBean> meta, String outputprth) {
		// TODO Auto-generated method stub
		Boolean bl = false;
		OutputStream out = null;
		try {
			out = new FileOutputStream(outputprth);
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			return bl;
		}
		ExcelUtility<MateColumnsBean> ex = new ExcelUtility<MateColumnsBean>();
		ex.exportExcel(BiaotiBean.HEADER, meta, out, "yyyy-MM-dd HH:mm:ss");
		bl = true;
		return bl;
	}

	@SuppressWarnings("unchecked")
	@Override
	public MateBean getTdMate(String path) {
		// TODO Auto-generated method stub
		MateBean mb = new MateBean();
		List<Map<String, Object>> lt = new ArrayList<Map<String, Object>>();
		BaseDbDaoI db = FactoryBaseDbDaoServiceImp.getBaseDbDaoI(path);
		String sql = "select * from  CONF_AUTODEV where FLAG =1";
		lt = db.getDateForMap(sql);
		mb.setDbSid(db.getDb_ssid());
		mb.setDbconfpath(path);
		mb.setDbtype(db.getDb_type());
		mb.setMatedate(lt);
		return mb;

	}

	public String getSql(String Type) {
		String sql = "";
		if ("oracle".equals(Type)) {
			sql = SQLGlobal.GET_COLUMN_ORACLESQL;
		} else if ("mssql".equals(Type)) {
			sql = SQLGlobal.GET_COLUMN_SQLSERVERSQL;
		} else if ("teradate".equals(Type)) {
			sql = SQLGlobal.GET_COLUMN_TERADATASQL;
		} else if ("mysql".equals(Type)) {
			sql = SQLGlobal.GET_COLUMN_MYSQLSQL;
		}

		return sql;

	}

	@Override
	public List<Map<String, Object>> getTableColumn(String path) {
       
		BaseDbDaoI db = FactoryBaseDbDaoServiceImp.getBaseDbDaoI(path);
		return db.getTableColumn();
	}

	@Override
	public Boolean updateTdMate(String path,Boolean status) {
		BaseDbDaoI db = FactoryBaseDbDaoServiceImp.getBaseDbDaoI(path);
		String sql="";
		if(status){
			sql="update CONF_AUTODEV  set FLAG =2 ";
		}else{
			sql="update CONF_AUTODEV  set FLAG =-2";
			
		}
		
		return db.execute(sql, null);
	}



}
