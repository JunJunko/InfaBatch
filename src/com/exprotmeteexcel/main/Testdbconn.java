package com.exprotmeteexcel.main;

import java.awt.BorderLayout;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.Properties;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JOptionPane;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.exprotmeteexcel.bean.MateBean;
import com.exprotmeteexcel.bean.MateColumnsBean;
import com.exprotmeteexcel.bean.ShowDialogLintener;
import com.exprotmeteexcel.service.ExprotMeteExcelService;
import com.exprotmeteexcel.service.imp.ExprotMeteExcelServiceImpl;
import com.exprotmeteexcel.utl.Utl;

public class Testdbconn {

	private static final Logger log = LoggerFactory.getLogger(Testdbconn.class);

	public static void main(String[] args) {
		// TODO Auto-generated method stub

		getTableMateBeanByTest();

		com.exprotmeteexcel.main.ExportExcel.main(null);

	}

	/**
	 * 测试原数据导出
	 * 
	 * @return MateBean
	 */

	public static void getTableMateBeanByTest() {
		Boolean bn = false;
		Properties p = Utl.getProperties("properties\\EXPORTMETA.properties");
		Properties yp = Utl.getProperties(p.getProperty("businesspropertiespath"));
		String businessName = yp.getProperty("SourceFolder");
		List<Map<String, Object>> alllist = getTableColAll(p.getProperty("businesspropertiespath"));
		ExprotMeteExcelService ex = new ExprotMeteExcelServiceImpl();
		MateBean tt = new MateBean();
		tt.setMatedate(alllist);
		List<MateColumnsBean> lb = ex.getTableColumn(p.getProperty("businesspropertiespath"), tt);
		SimpleDateFormat formatter = new SimpleDateFormat("yyyyMMddHHmmssSSS");
		String formatStr = formatter.format(new Date());
		String path = "xls\\out\\metaout\\OUT_" + businessName + "_" + formatStr + ".xls";
		bn = ex.ExprotExcel(lb, path);
		if (bn) {
			log.info("运行成功！");
		} else {
			log.info("运行失败！");
		}

	}

	public static void ReadExcelExprot(String path,String metapath) {
		Boolean bn = false;
		ExprotMeteExcelService ex = new ExprotMeteExcelServiceImpl();
		Map<String, Object> readmap = ex.ReadExcel(path);
		@SuppressWarnings({ "unchecked", "unused" })
		List<MateColumnsBean> listmc = (List<MateColumnsBean>) readmap.get("TRANLIST");

		@SuppressWarnings({ "unchecked", "unused" })
		Map<String, List<MateColumnsBean>> tableDdl = (Map<String, List<MateColumnsBean>>) readmap.get("TABLEDDL");
	    ex.ExcelExprot(metapath,listmc);
		
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
		Properties yp = Utl.getProperties(p.getProperty("businesspropertiespath"));
		String businessName = yp.getProperty("SourceFolder");
		if (!Utl.isEmpty(tt.getMatedate())) {
			List<MateColumnsBean> lb = ex.getTableColumn(p.getProperty("businesspropertiespath"), tt);
			SimpleDateFormat formatter = new SimpleDateFormat("yyyyMMddHHmmssSSS");
			String formatStr = formatter.format(new Date());
			String path = "xls\\out\\OUT_" + businessName + "_" + formatStr + ".xls";
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
