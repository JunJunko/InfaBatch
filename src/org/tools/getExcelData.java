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
public class getExcelData {

	public static void main(String[] args) {
		System.out.println(readExecl("D:\\EXCEL\\OUT2_OUT_CBI_20170704155607660.xls"));
//		System.out.println(readExecl("D:\\EXCEL\\eln.xlsx"));


	}
	/**
	 * describe:从Excel配置文件读取表名、字段名、数据类型、是否PI、入仓逻辑<p>
	 * @author Junko
	 * @param Excel路径
	 * @return Excel数据
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
			String cellValue;
			String ColumnNM;
			String DataType;
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
				
                //取字段名
				row = sheet.getRow(i); 
				cell_b = row.getCell(5); 
				ColumnNM = cell_b.getStringCellValue().trim();
				ColumnList.add(ColumnNM);

				//取字段类型
				row = sheet.getRow(i); 
				cell_c = row.getCell(9); 
				DataType = cell_c.getStringCellValue().trim().toUpperCase();
				TypeList.add(DataType);

				//取pi值
				row = sheet.getRow(i); // 取得第i行
				cell_d = row.getCell(13); // 取得i行的第一列
				if (!(cell_d == null)) {
					IsPi = cell_d.getStringCellValue().trim();
				} else {
					IsPi = "";
				}
				
                //取入仓逻辑
				row = sheet.getRow(i); // 取得第i行
				cell_e = row.getCell(20); // 取得i行的第一列
				InputLogic = cell_e.getStringCellValue().trim();
				
				//取入仓逻辑
				row = sheet.getRow(i); // 取得第i行
				cell_m = row.getCell(12); // 取得i行的第一列
				PrimaryKey = cell_m.getStringCellValue().trim();
				
				
				TableList.add(InputLogic);
				TypeList.add(IsPi);

				HashList.add(cellValue);
				HashList.add(ColumnNM);
				HashList.add(DataType);
				HashList.add(IsPi);
				HashList.add(InputLogic);
				HashList.add(PrimaryKey);

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