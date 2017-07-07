package com.exprotmeteexcel.utl;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.apache.poi.hssf.usermodel.HSSFCell;
import org.apache.poi.hssf.usermodel.HSSFCellStyle;
import org.apache.poi.hssf.usermodel.HSSFClientAnchor;
import org.apache.poi.hssf.usermodel.HSSFComment;
import org.apache.poi.hssf.usermodel.HSSFFont;
import org.apache.poi.hssf.usermodel.HSSFPatriarch;
import org.apache.poi.hssf.usermodel.HSSFRichTextString;
import org.apache.poi.hssf.usermodel.HSSFRow;
import org.apache.poi.hssf.usermodel.HSSFSheet;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.hssf.util.HSSFColor;
import org.apache.poi.poifs.filesystem.POIFSFileSystem;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.xssf.usermodel.XSSFRow;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.exprotmeteexcel.bean.MateColumnsBean;

/**
 * 
 * ���ÿ�Դ���POI3.0.2��̬����EXCEL�ĵ�
 * 
 * ת��ʱ�뱣��������Ϣ��ע��������
 * 
 * @author leno
 * 
 * @version v1.0
 * 
 * @param <T>
 *            Ӧ�÷��ͣ���������һ������javabean������
 * 
 *            ע������Ϊ�˼������boolean�͵�����xxx��get����ʽΪgetXxx(),������isXxx()
 * 
 *            byte[]��jpg��ʽ��ͼƬ����
 */

public class ExcelUtility<T> {

	private static final Logger log = LoggerFactory.getLogger(ExcelUtility.class);

	public void exportExcel(Collection<T> dataset, OutputStream out) {
		exportExcel("Sheet1", null, dataset, out, "yyyy-MM-dd");
	}

	public void exportExcel(String[] headers, Collection<T> dataset, OutputStream out) {
		exportExcel("Sheet1", headers, dataset, out, "yyyy-MM-dd");
	}

	public void exportExcel(String[] headers, Collection<T> dataset, OutputStream out, String pattern) {
		exportExcel("Sheet1", headers, dataset, out, pattern);
	}

	/**
	 * 
	 * ����һ��ͨ�õķ�����������JAVA�ķ�����ƣ����Խ�������JAVA�����в��ҷ���һ��������������EXCEL ����ʽ�����ָ��IO�豸��
	 * 
	 * 
	 * 
	 * @param title
	 * 
	 *            ��������
	 * 
	 * @param headers
	 * 
	 *            ���������������
	 * 
	 * @param dataset
	 * 
	 *            ��Ҫ��ʾ�����ݼ���,������һ��Ҫ���÷���javabean������Ķ��󡣴˷���֧�ֵ�
	 * 
	 *            javabean���Ե����������л����������ͼ�String,Date,byte[](ͼƬ����)
	 * 
	 * @param out
	 * 
	 *            ������豸�����������󣬿��Խ�EXCEL�ĵ������������ļ�����������
	 * 
	 * @param pattern
	 * 
	 *            �����ʱ�����ݣ��趨�����ʽ��Ĭ��Ϊ"yyy-MM-dd"
	 */

	@SuppressWarnings("unchecked")
	public void exportExcel(String title, String[] headers, Collection<T> dataset, OutputStream out, String pattern) {
		// ����һ��������
		HSSFWorkbook workbook = new HSSFWorkbook();
		// ����һ�����
		HSSFSheet sheet = workbook.createSheet(title);
		// ���ñ��Ĭ���п��Ϊ15���ֽ�
		sheet.setDefaultColumnWidth((short) 15);
		// ����һ����ʽ
		HSSFCellStyle style = workbook.createCellStyle();
		// ������Щ��ʽ
		style.setFillForegroundColor(HSSFColor.SKY_BLUE.index);
		style.setFillPattern(HSSFCellStyle.SOLID_FOREGROUND);
		style.setBorderBottom(HSSFCellStyle.BORDER_THIN);
		style.setBorderLeft(HSSFCellStyle.BORDER_THIN);
		style.setBorderRight(HSSFCellStyle.BORDER_THIN);
		style.setBorderTop(HSSFCellStyle.BORDER_THIN);
		style.setAlignment(HSSFCellStyle.ALIGN_CENTER);
		// ����һ������
		HSSFFont font = workbook.createFont();
		font.setColor(HSSFColor.VIOLET.index);
		font.setFontHeightInPoints((short) 12);
		font.setBoldweight(HSSFFont.BOLDWEIGHT_BOLD);
		// ������Ӧ�õ���ǰ����ʽ
		style.setFont(font);
		// ���ɲ�������һ����ʽ
		HSSFCellStyle style2 = workbook.createCellStyle();
		style2.setFillForegroundColor(HSSFColor.WHITE.index);
		style2.setFillPattern(HSSFCellStyle.SOLID_FOREGROUND);
		style2.setBorderBottom(HSSFCellStyle.BORDER_THIN);
		style2.setBorderLeft(HSSFCellStyle.BORDER_THIN);
		style2.setBorderRight(HSSFCellStyle.BORDER_THIN);
		style2.setBorderTop(HSSFCellStyle.BORDER_THIN);
		style2.setAlignment(HSSFCellStyle.ALIGN_CENTER);
		style2.setVerticalAlignment(HSSFCellStyle.VERTICAL_CENTER);
		// ������һ������
		HSSFFont font2 = workbook.createFont();
		// font2.setColor(HSSFColor.BLACK.index);
		font2.setBoldweight(HSSFFont.BOLDWEIGHT_NORMAL);
		// ������Ӧ�õ���ǰ����ʽ
		style2.setFont(font2);
		// ����һ����ͼ�Ķ���������
		HSSFPatriarch patriarch = sheet.createDrawingPatriarch();
		// ����ע�͵Ĵ�С��λ��,����ĵ�
		HSSFComment comment = patriarch.createComment(new HSSFClientAnchor(0, 0, 0, 0, (short) 4, 2, (short) 6, 5));
		// ����ע������
		comment.setString(new HSSFRichTextString("������������"));
		// ����ע�����ߣ�������ƶ�����Ԫ�����ǿ�����״̬���п���������.
		comment.setAuthor("leno");
		// ������������
		HSSFRow row = sheet.createRow(0);
		for (short i = 0; i < headers.length; i++) {
			HSSFCell cell = row.createCell(i);
			cell.setCellStyle(style);
			HSSFRichTextString text = new HSSFRichTextString(headers[i]);
			cell.setCellValue(text);
		}
		// �����������ݣ�����������
		Iterator<T> it = dataset.iterator();
		int index = 0;
		while (it.hasNext()) {
			index++;
			row = sheet.createRow(index);
			T t = (T) it.next();
			// ���÷��䣬����javabean���Ե��Ⱥ�˳�򣬶�̬����getXxx()�����õ�����ֵ
			Field[] fields = t.getClass().getDeclaredFields();

			for (int i = 0; i < fields.length; i++) {
				HSSFCell cell = row.createCell(i);
				cell.setCellStyle(style2);
				Field field = fields[i];
				String fieldName = field.getName();

				String getMethodName = "get" + fieldName.substring(0, 1).toUpperCase() + fieldName.substring(1);
				try {
					Class tCls = t.getClass();
					Method getMethod = tCls.getMethod(getMethodName, new Class[] {});
					Object value = getMethod.invoke(t, new Object[] {});
					// �ж�ֵ�����ͺ����ǿ������ת��
					String textValue = null;

					// if (value instanceof Integer) {
					// int intValue = (Integer) value;
					// cell.setCellValue(intValue);
					// } else if (value instanceof Float) {
					// float fValue = (Float) value;
					// textValue = new HSSFRichTextString(
					// String.valueOf(fValue));
					// cell.setCellValue(textValue);
					// } else if (value instanceof Double) {
					// double dValue = (Double) value;
					// textValue = new HSSFRichTextString(
					// String.valueOf(dValue));
					// cell.setCellValue(textValue);
					// } else if (value instanceof Long) {
					// long longValue = (Long) value;
					// cell.setCellValue(longValue);
					// }
					if (value instanceof Date) {
						Date date = (Date) value;
						SimpleDateFormat sdf = new SimpleDateFormat(pattern);
						textValue = sdf.format(date);
					} else {
						// �����������Ͷ������ַ����򵥴���

						if (value != null) {
							textValue = value.toString();
						} else {
							textValue = "";
						}
					}
					// �������ͼƬ���ݣ�������������ʽ�ж�textValue�Ƿ�ȫ�����������

					Pattern p = Pattern.compile("^//d+(//.//d+)?$");
					Matcher matcher = p.matcher(textValue);
					if (matcher.matches()) {
						// �����ֵ���double����
						cell.setCellValue(Double.parseDouble(textValue));
					} else {
						// System.out.println("textValue:" + textValue + "�ڼ��У�"
						// + index + "�ڼ�����" + i);
						HSSFRichTextString richString = new HSSFRichTextString(textValue);
						/*
						 * HSSFFont font3 = workbook.createFont();
						 * font3.setColor(HSSFColor.BLACK.index);
						 * richString.applyFont(font3);
						 */
						cell.setCellValue(richString);
					}

				} catch (Exception e) {
					log.error("������Ϣ", e);
				} finally {
					// ������Դ
				}
			}
		}
		try {
			workbook.write(out);
		} catch (IOException e) {
			log.error("������Ϣ", e);
		}
	}

	/**
	 * ����excel
	 * 
	 * @param is
	 *            ������
	 * @param row_start
	 *            ��ʼ��
	 * @return �����������͵ļ���
	 */
	public static List<Object[]> readExcelContent(InputStream is, Integer row_start) {
		POIFSFileSystem fs = null;
		HSSFWorkbook wb = null;
		HSSFSheet sheet = null;
		HSSFRow row = null;

		List<Object[]> data = new ArrayList<Object[]>();
		try {
			fs = new POIFSFileSystem(is);
			wb = new HSSFWorkbook(fs);

			sheet = wb.getSheetAt(0);
			// ������
			int rowNum = sheet.getLastRowNum();
			row = sheet.getRow(0);
			// ������
			int colNum = row.getPhysicalNumberOfCells();
			int row_start_init = 0;
			/*
			 * ָ��������ʼ�С������С���ʼ�С�������
			 */
			if (row_start != null) {
				row_start_init = row_start;
			}
			Object[] objs;
			if (rowNum > 0) {
				for (int i = row_start_init; i <= rowNum; i++) {
					row = sheet.getRow(i);
					objs = new Object[colNum];
					for (int j = 0; j < colNum; j++) {
						Cell cell = row.getCell(j);
						if (cell != null) {
							if (cell.getCellType() == Cell.CELL_TYPE_STRING) {
								objs[j] = cell.getStringCellValue() == null ? "" : cell.getStringCellValue();
							} else {
								DecimalFormat df = new DecimalFormat("#");
								int value = (int) cell.getNumericCellValue();
								objs[j] = value == 0 ? "" : df.format(cell.getNumericCellValue());
							}
						}
					}
					data.add(objs);
				}
			}
		} catch (IOException e) {
			log.error("������Ϣ", e);
		}
		return data;
	}

	public static List<Object[]> readExcelContentByXlsx(InputStream is, Integer row_start) {
		POIFSFileSystem fs = null;
		XSSFWorkbook wb = null;
		XSSFSheet sheet = null;
		XSSFRow row = null;

		List<Object[]> data = new ArrayList<Object[]>();
		try {

			wb = new XSSFWorkbook(is);

			sheet = wb.getSheetAt(0);
			// ������
			int rowNum = sheet.getLastRowNum();
			row = sheet.getRow(0);
			// ������
			int colNum = row.getPhysicalNumberOfCells();
			int row_start_init = 0;
			/*
			 * ָ��������ʼ�С������С���ʼ�С�������
			 */
			if (row_start != null) {
				row_start_init = row_start;
			}
			Object[] objs;
			if (rowNum > 0) {
				for (int i = row_start_init; i <= rowNum; i++) {
					row = sheet.getRow(i);
					objs = new Object[colNum];
					for (int j = 0; j < colNum; j++) {
						Cell cell = row.getCell(j);
						if (cell != null) {
							if (cell.getCellType() == Cell.CELL_TYPE_STRING) {
								objs[j] = cell.getStringCellValue() == null ? "" : cell.getStringCellValue();
							} else {
								DecimalFormat df = new DecimalFormat("#");
								int value = (int) cell.getNumericCellValue();
								objs[j] = value == 0 ? "" : df.format(cell.getNumericCellValue());
							}
						}
					}
					data.add(objs);
				}
			}
		} catch (IOException e) {
			log.error("������Ϣ", e);
		}
		return data;
	}

	public static List<Object[]> getReadExcelContent(String path, Integer row_start) {
		InputStream is = null;
		File fl = new File(path);
		String fileName = fl.getName();
		try {
			is = new FileInputStream(path);
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		}
		if (fileName.endsWith("xls")) {
			return readExcelContent(is, row_start);
		} else if (fileName.endsWith("xlsx")) {
			return readExcelContentByXlsx(is, row_start);
		} else {
			return null;
		}

	}

	/**
	 * ����excel
	 * 
	 * @param is
	 *            ������
	 * @param row_start
	 *            ��ʼ��
	 * @return �����������͵ļ���
	 */
	public static Map<String, Object> readExcel(InputStream is, Integer row_start) {
		POIFSFileSystem fs = null;
		HSSFWorkbook wb = null;
		HSSFSheet sheet = null;
		HSSFRow row = null;
		Map<String, Object> mp = new HashMap<String, Object>();
		List<Object[]> data = new ArrayList<Object[]>();
		// ������MAP
		Map<String, String> tablemap = new HashMap<String, String>();
		try {
			fs = new POIFSFileSystem(is);
			wb = new HSSFWorkbook(fs);

			sheet = wb.getSheetAt(0);
			// ������
			int rowNum = sheet.getLastRowNum();
			row = sheet.getRow(0);
			// ������
			int colNum = row.getPhysicalNumberOfCells();
			int row_start_init = 0;
			/*
			 * ָ��������ʼ�С������С���ʼ�С�������
			 */
			if (row_start != null) {
				row_start_init = row_start;
			}
			Object[] objs;
			if (rowNum > 0) {
				for (int i = row_start_init; i <= rowNum; i++) {
					row = sheet.getRow(i);
					objs = new Object[colNum];
					// �Ƿ��������߼�
					Cell ce = row.getCell(20);
					if (ce.getCellType() == Cell.CELL_TYPE_STRING) {
						if (!Utl.isEmpty(ce.getStringCellValue())&&"������".equals(ce.getStringCellValue())) {
							Cell tablece = row.getCell(2);
							tablemap.put(tablece.getStringCellValue(), ce.getStringCellValue());
						}
					}
					for (int j = 0; j < colNum; j++) {
						Cell cell = row.getCell(j);
						if (cell != null) {
							if (cell.getCellType() == Cell.CELL_TYPE_STRING) {
								objs[j] = cell.getStringCellValue() == null ? "" : cell.getStringCellValue();
							} else {
								DecimalFormat df = new DecimalFormat("#");
								int value = (int) cell.getNumericCellValue();
								objs[j] = value == 0 ? "" : df.format(cell.getNumericCellValue());
							}
						}
					}
					data.add(objs);
				}
			}
		} catch (IOException e) {
			log.error("������Ϣ", e);
		}
		mp.put("LISTDATE", data);
		// ������MAP
		mp.put("MAPDATE", tablemap);
		return mp;
	}
}
