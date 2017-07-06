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

public class ExcelUtil {

	public static void main(String[] args) {
		System.out.println(readExecl("F:\\g��������\\shsnc\\���޼�\\test.xlsx"));

	}

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
			int firstRowNum = sheet.getFirstRowNum();
			int lastRowNum = sheet.getLastRowNum();

			Row row = null;
			Cell cell_a = null;
			Cell cell_b = null;
			Cell cell_c = null;
			Cell cell_d = null;
			Cell cell_e = null;
			String cellValue;
			String cellValue2;
			String cellValue3;
			String cellValue4;
			String cellValue5;
			HashMap<String, Integer> Hm = new HashMap<String, Integer>();

			for (int i = 1; i <= lastRowNum; i++) {
				row = sheet.getRow(i); // ȡ�õ�i��
				cell_a = row.getCell(0); // ȡ��i�еĵ�һ��

				cellValue = cell_a.getStringCellValue().trim();

				// System.out.println(cellValue);
				// if (!TableList.contains(cellValue)){
				TableList.add(cellValue);
				// }

				row = sheet.getRow(i); // ȡ�õ�i��
				cell_b = row.getCell(1); // ȡ��i�еĵ�һ��
				cellValue2 = cell_b.getStringCellValue().trim();
				ColumnList.add(cellValue2);

				row = sheet.getRow(i); // ȡ�õ�i��
				cell_c = row.getCell(3); // ȡ��i�еĵ�һ��
				cellValue3 = cell_c.getStringCellValue().trim().toUpperCase();
				TypeList.add(cellValue3);

				row = sheet.getRow(i); // ȡ�õ�i��
				cell_d = row.getCell(4); // ȡ��i�еĵ�һ��
				if (!(cell_d == null)) {
					cellValue4 = cell_d.getStringCellValue().trim();
				} else {
					cellValue4 = "";
				}

				row = sheet.getRow(i); // ȡ�õ�i��
				cell_e = row.getCell(5); // ȡ��i�еĵ�һ��
				cellValue5 = cell_e.getStringCellValue().trim();
				TableList.add(cellValue5);
				TypeList.add(cellValue4);

				HashList.add(cellValue);
				HashList.add(cellValue2);
				HashList.add(cellValue3);
				HashList.add(cellValue4);
				HashList.add(cellValue5);

				ReList.add(HashList);

				HashList = new ArrayList<String>();

				if (Hm.containsKey(cellValue)) {
					Hm.put(cellValue, Hm.get(cellValue) + 1);
				} else {
					Hm.put(cellValue, 1);
				}

			}
			// System.out.println(Hm);
			// List listWithoutDup = new ArrayList(new HashSet(TableList));
			// System.out.println(TableList);
			// System.out.println(ColumnList.size());
			// int o = 0;
			// for(int i = 1; i <= TableList.size(); i++){
			//
			// for(int j = 0; j < Hm.get(TableList.get(o)); j++){
			//
			// System.out.print(j);
			// System.out.println(ColumnList);
			// System.out.println(ColumnList.get(j)+"____________"+TypeList.get(j));
			//
			//
			// }
			// o++;
			//
			//// return ColumnList;
			// }

			// System.out.println(ColumnList.get(8));

		} catch (IOException ex) {
			ex.printStackTrace();
		}

		return ReList;

	}

}