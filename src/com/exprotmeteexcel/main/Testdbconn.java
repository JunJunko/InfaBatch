package com.exprotmeteexcel.main;

import java.io.File;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.Properties;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.exprotmeteexcel.bean.MateBean;
import com.exprotmeteexcel.bean.MateColumnsBean;
import com.exprotmeteexcel.service.ExprotMeteExcelService;
import com.exprotmeteexcel.service.imp.ExprotMeteExcelServiceImpl;
import com.exprotmeteexcel.utl.FileUtil;
import com.exprotmeteexcel.utl.Utl;

public class Testdbconn {

	private static final Logger log = LoggerFactory.getLogger(Testdbconn.class);

	public static void main(String[] args) {
		// TODO Auto-generated method stub

		//com.exprotmeteexcel.main.ExportExcel.main(null);
		ExprotMeteExcelService t =new ExprotMeteExcelServiceImpl();
		t.getPropertiesMapList("properties\\businessconfig");
	}

	/**
	 * 测试原数据导出
	 * 
	 * @return MateBean
	 */

	public static void getTableMateBeanByTest(String path) {
		Boolean bn = false;
		Properties p = Utl.getProperties(path);
		//Properties yp = Utl.getProperties(p.getProperty("businesspropertiespath"));
		String businessName =p.getProperty("System");
		List<Map<String, Object>> alllist = getTableColAll(path);
		ExprotMeteExcelService ex = new ExprotMeteExcelServiceImpl();
		MateBean tt = new MateBean();
		tt.setMatedate(alllist);
		List<MateColumnsBean> lb = ex.getTableColumn(path, tt);
		SimpleDateFormat formatter = new SimpleDateFormat("yyyyMMddHHmmssSSS");
		String formatStr = formatter.format(new Date());
		String opath = "xls\\out\\metaout\\OUT_" + businessName + "_" + formatStr + ".xls";
		bn = ex.ExprotExcel(lb, opath);
		if (bn) {
			log.info("运行成功！");
			log.info("表个数：" + alllist.size());
		} else {
			log.info("运行失败！");
		}

	}

	public static void ReadExcelExprot(String path) {
		Boolean bn = false;
		ExprotMeteExcelService ex = new ExprotMeteExcelServiceImpl();
		Map<String, Object> readmap = ex.ReadExcel(path);
		@SuppressWarnings({ "unchecked", "unused" })
		List<MateColumnsBean> listmc = (List<MateColumnsBean>) readmap.get("TRANLIST");
		File file = new File(path);
		String metapath = file.getName();
		String str = metapath.substring(0, metapath.lastIndexOf("."));
		bn = ex.ExcelExprot(str, listmc);

		if (bn) {
			log.info("运行成功！");
		} else {
			log.info("运行失败！");
		}
	}

	/**
	 * 原数据导出excel
	 * 
	 * @return MateBean
	 */
	public static void getTableMateBean(String path) {
		Boolean bn = false;
		MateBean tt = new MateBean();
		Properties p = Utl.getProperties(path);
		Properties py = Utl.getProperties(p.getProperty("businesspropertiespath"));
		String businessName = py.getProperty("System");
		// 1、得到业务配置表
		ExprotMeteExcelService ex = new ExprotMeteExcelServiceImpl();
		tt = ex.getTdMate(p.getProperty("teradatajdbcpath"), businessName);
		if (!Utl.isEmpty(tt.getMatedate())) {
			// 锁定对应的表
			ex.updateTdMate(p.getProperty("teradatajdbcpath"), "running", businessName);
			// 得到每个配置表的对原数据信息
			List<MateColumnsBean> lb = ex.getTableColumn(p.getProperty("businesspropertiespath"), tt);
			SimpleDateFormat formatter = new SimpleDateFormat("yyyyMMddHHmmssSSS");
			String formatStr = formatter.format(new Date());
			String outpath = "xls\\out\\OUT_" + businessName + "_" + formatStr + ".xls";
			bn = ex.ExprotExcel(lb, outpath);
			// 完成后更新表
			ex.updateTdMate(p.getProperty("teradatajdbcpath"), bn ? "ok" : "", businessName);
		}

		if (bn) {
			log.info("运行成功！");
		} else {
			log.info("运行失败！");
		}

	}

	public static List<Map<String, Object>> getTableColAll(String path) {
		ExprotMeteExcelService ex = new ExprotMeteExcelServiceImpl();
		return ex.getTableColumn(path);

	}

}
