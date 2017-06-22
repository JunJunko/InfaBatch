package com.exprotmeteexcel.main;

import java.util.List;
import java.util.Map;
import java.util.Properties;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.exprotmeteexcel.bean.MateBean;
import com.exprotmeteexcel.bean.MateColumnsBean;
import com.exprotmeteexcel.service.ExprotMeteExcelService;
import com.exprotmeteexcel.service.imp.ExprotMeteExcelServiceImpl;
import com.exprotmeteexcel.utl.Utl;

public class Testdbconn {

	private static final Logger log = LoggerFactory.getLogger(Testdbconn.class);

	public static void main(String[] args) {
		// TODO Auto-generated method stub

		getTableMateBeanByTest();

	}

	/**
	 * 测试原数据导出
	 * 
	 * @return MateBean
	 */

	public static void getTableMateBeanByTest() {
		Boolean bn = false;
		Properties p = Utl.getProperties("properties\\EXPORTMETA.properties");
		List<Map<String, Object>> alllist = getTableColAll(p.getProperty("projectpropertiespath"));
		ExprotMeteExcelService ex = new ExprotMeteExcelServiceImpl();
		MateBean tt = new MateBean();
		tt.setMatedate(alllist);
		List<MateColumnsBean> lb = ex.getTableColumn(p.getProperty("projectpropertiespath"), tt);
		long currentTime = System.currentTimeMillis();
		String path = "xls\\out\\out_" + currentTime + ".xls";
		bn = ex.ExprotExcel(lb, path);
		if (bn) {
			log.info("运行成功！");
		} else {
			log.info("运行失败！");
		}

	}

	/**
	 * 原数据导出
	 * 
	 * @return MateBean
	 */
	public static void getTableMateBean() {
		Boolean bn = false;
		Properties p = Utl.getProperties("properties\\EXPORTMETA.properties");

		ExprotMeteExcelService ex = new ExprotMeteExcelServiceImpl();
		MateBean tt = new MateBean();
		tt = ex.getTdMate(p.getProperty("teradatajdbcpath"));

		if (!Utl.isEmpty(tt.getMatedate())) {
			List<MateColumnsBean> lb = ex.getTableColumn(p.getProperty("projectpropertiespath"), tt);
			long currentTime = System.currentTimeMillis();
			String path = "xls\\out\\out_" + currentTime + ".xls";
			bn = ex.ExprotExcel(lb, path);
			ex.updateTdMate(p.getProperty("teradatajdbcpath"), bn);
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
