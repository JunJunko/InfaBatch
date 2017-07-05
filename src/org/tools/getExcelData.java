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
 * describe:��Excel�����ļ���ȡ�������ֶ������������͡��Ƿ�PI������߼�<p>
 * @author Junko
 *
 */
public class getExcelData {

	public static void main(String[] args) {
		System.out.println(readExecl("D:\\EXCEL\\OUT2_OUT_CBI_20170704155607660.xls"));
//		System.out.println(readExecl("D:\\EXCEL\\eln.xlsx"));


	}
	/**
	 * describe:��Excel�����ļ���ȡ�������ֶ������������͡��Ƿ�PI������߼�<p>
	 * @author Junko
	 * @param Excel·��
	 * @return Excel����
	 */
	public static ArrayList<ArrayList<String>> readExecl(String fileName) {
		boolean isE2007 = false; // �ж��Ƿ���excel2007��ʽ
		List<String> TableList = new ArrayList<String>();
		List<String> ColumnList = new ArrayList<String>();
		List<String> TypeList = new ArrayList<String>();
		ArrayList<String> HashList = new ArrayList<String>();
		ArrayList<ArrayList<String>> ReList = new ArrayList<ArrayList<String>>();
		if (fileName.endsWith("xlsx"))
			isE2007 = true;
		try {
			InputStream input = new FileInputStream(fileName); // ����������
			Workbook wb = null;
			// �����ļ���ʽ(2003����2007)����ʼ��
			if (isE2007)
				wb = new XSSFWorkbook(input);
			else
				wb = new HSSFWorkbook(input);
			org.apache.poi.ss.usermodel.Sheet sheet = wb.getSheetAt(0); // ��õ�һ����
			Iterator<Row> rows = ((org.apache.poi.ss.usermodel.Sheet) sheet).rowIterator(); // ��õ�һ�����ĵ�����
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
				//ȡ����
				row = sheet.getRow(i); 
				cell_a = row.getCell(2); 

				cellValue = cell_a.getStringCellValue().trim();

				TableList.add(cellValue);
				
                //ȡ�ֶ���
				row = sheet.getRow(i); 
				cell_b = row.getCell(5); 
				ColumnNM = cell_b.getStringCellValue().trim();
				ColumnList.add(ColumnNM);

				//ȡ�ֶ�����
				row = sheet.getRow(i); 
				cell_c = row.getCell(9); 
				DataType = cell_c.getStringCellValue().trim().toUpperCase();
				TypeList.add(DataType);

				//ȡpiֵ
				row = sheet.getRow(i); // ȡ�õ�i��
				cell_d = row.getCell(13); // ȡ��i�еĵ�һ��
				if (!(cell_d == null)) {
					IsPi = cell_d.getStringCellValue().trim();
				} else {
					IsPi = "";
				}
				
                //ȡ����߼�
				row = sheet.getRow(i); // ȡ�õ�i��
				cell_e = row.getCell(20); // ȡ��i�еĵ�һ��
				InputLogic = cell_e.getStringCellValue().trim();
				
				//ȡ����߼�
				row = sheet.getRow(i); // ȡ�õ�i��
				cell_m = row.getCell(12); // ȡ��i�еĵ�һ��
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