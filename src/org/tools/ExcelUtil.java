package org.tools;

import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;

import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

/**
 * describe:从Excel配置文件读取表名、字段名、数据类型、是否PI、入仓逻辑<p>
 * @author Junko
 *
 */
/**
* =============================================
* @Copyright 2017上海新炬网络技术有限公司
* @version：1.0.1
* @author：Junko
* @date：2017年7月11日下午3:34:24
* @Description: 读取Excel的配置信息
* =============================================
 */
public class ExcelUtil {

	public static void main(String[] args) {
		System.out.println(readExecl("xls\\out\\piout\\DLPM_20170727.xls"));
//		System.out.println(readExecl("D:\\EXCEL\\eln.xlsx"));


	}
	/**
	 * @version: 1.0.1
	 * @author: Junko
	 * @date: 2017年7月11日下午3:33:53 
	 * @Description: 读取Excel的配置信息
	 * @param fileName
	 * @return 返回Excel的配置信息
	 */
	public static ArrayList<ArrayList<String>> readExecl(String fileName) {
		boolean isE2007 = false; // 判断是否是excel2007格式
		List<String> TableList = new ArrayList<String>();
		List<String> ColumnList = new ArrayList<String>();
		List<String> TypeList = new ArrayList<String>();
		ArrayList<String> HashList = new ArrayList<String>();
		ArrayList<ArrayList<String>> ReList = new ArrayList<ArrayList<String>>();
		if (fileName.endsWith("xlsx"))
			isE2007 = true;
		try {
			InputStream input = new FileInputStream(fileName); // 建立输入流
			Workbook wb = null;
			// 根据文件格式(2003或者2007)来初始化
			if (isE2007)
				wb = new XSSFWorkbook(input);
			else
				wb = new HSSFWorkbook(input);
			org.apache.poi.ss.usermodel.Sheet sheet = wb.getSheetAt(0); // 获得第一个表单
			Iterator<Row> rows = ((org.apache.poi.ss.usermodel.Sheet) sheet).rowIterator(); // 获得第一个表单的迭代器
			int lastRowNum = sheet.getLastRowNum();

			Row row = null;
			Cell cell_a = null;
			Cell cell_b = null;
			Cell cell_c = null;
			Cell cell_d = null;
			Cell cell_e = null;
			Cell cell_m = null;
			Cell cell_j = null;
			Cell cell_pk = null;
			Cell cell_g = null;
			
			String cellValue;
			String ColumnNM;
			String ColumnTNM;
			String SourceDataType;
			String TargetDataType;
			String IsPi;
			String InputLogic;
			String PrimaryKey;
			HashMap<String, Integer> Hm = new HashMap<String, Integer>();

			for (int i = 1; i <= lastRowNum; i++) {
				//取表名
				row = sheet.getRow(i); 
				cell_a = row.getCell(2); 

				cellValue = cell_a.getStringCellValue().trim();

				TableList.add(cellValue);
				
                //取源字段名
				cell_b = row.getCell(5); 
				ColumnNM = cell_b.getStringCellValue().trim().toUpperCase();
				ColumnList.add(ColumnNM);
				
//				取目标字段名
				cell_g = row.getCell(6); 
				ColumnTNM = cell_g.getStringCellValue().trim().toUpperCase();

				//取源字段类型
				cell_c = row.getCell(8); 	
				SourceDataType = cell_c.getStringCellValue().trim();
				
				TypeList.add(SourceDataType);

				//取pi值
				cell_d = row.getCell(13); // 取得i行的第一列
				
				
				if (!(cell_d == null)) {
					IsPi = cell_d.getStringCellValue().trim().toUpperCase();
				} else {
					IsPi = "";
				}
				
                //取入仓逻辑
				cell_e = row.getCell(20); // 取得i行的第一列
				InputLogic = cell_e == null? "": cell_e.getStringCellValue().trim();
				
				//取入仓逻辑
				cell_m = row.getCell(12); // 取得i行的第一列
				PrimaryKey = cell_m.getStringCellValue().trim();
				
				//取目标字段类型
				cell_j = row.getCell(9); 
				TargetDataType = cell_j.getStringCellValue().trim();
				
				
				TableList.add(InputLogic);
				TypeList.add(IsPi);

				HashList.add(cellValue);
				HashList.add(ColumnNM);				
				HashList.add(SourceDataType);
				HashList.add(IsPi);
				HashList.add(InputLogic);
				HashList.add(PrimaryKey);
				HashList.add(TargetDataType);
				HashList.add(ColumnTNM);

				ReList.add(HashList);

				HashList = new ArrayList<String>();

				if (Hm.containsKey(cellValue)) {
					Hm.put(cellValue, Hm.get(cellValue) + 1);
				} else {
					Hm.put(cellValue, 1);
				}

			}

		} catch (IOException ex) {
			ex.printStackTrace();
		}

		return ReList;

	}

}