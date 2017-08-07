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
/**
* =============================================
* @Copyright 2017�Ϻ��¾����缼�����޹�˾
* @version��1.0.1
* @author��Junko
* @date��2017��7��11������3:34:24
* @Description: ��ȡExcel��������Ϣ
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
	 * @date: 2017��7��11������3:33:53 
	 * @Description: ��ȡExcel��������Ϣ
	 * @param fileName
	 * @return ����Excel��������Ϣ
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
				//ȡ����
				row = sheet.getRow(i); 
				cell_a = row.getCell(2); 

				cellValue = cell_a.getStringCellValue().trim();

				TableList.add(cellValue);
				
                //ȡԴ�ֶ���
				cell_b = row.getCell(5); 
				ColumnNM = cell_b.getStringCellValue().trim().toUpperCase();
				ColumnList.add(ColumnNM);
				
//				ȡĿ���ֶ���
				cell_g = row.getCell(6); 
				ColumnTNM = cell_g.getStringCellValue().trim().toUpperCase();

				//ȡԴ�ֶ�����
				cell_c = row.getCell(8); 	
				SourceDataType = cell_c.getStringCellValue().trim();
				
				TypeList.add(SourceDataType);

				//ȡpiֵ
				cell_d = row.getCell(13); // ȡ��i�еĵ�һ��
				
				
				if (!(cell_d == null)) {
					IsPi = cell_d.getStringCellValue().trim().toUpperCase();
				} else {
					IsPi = "";
				}
				
                //ȡ����߼�
				cell_e = row.getCell(20); // ȡ��i�еĵ�һ��
				InputLogic = cell_e == null? "": cell_e.getStringCellValue().trim();
				
				//ȡ����߼�
				cell_m = row.getCell(12); // ȡ��i�еĵ�һ��
				PrimaryKey = cell_m.getStringCellValue().trim();
				
				//ȡĿ���ֶ�����
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