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
 * 利用开源组件POI3.0.2动态导出EXCEL文档
 * 
 * 转载时请保留以下信息，注明出处！
 * 
 * @author leno
 * 
 * @version v1.0
 * 
 * @param <T>
 *            应用泛型，代表任意一个符合javabean风格的类
 * 
 *            注意这里为了简单起见，boolean型的属性xxx的get器方式为getXxx(),而不是isXxx()
 * 
 *            byte[]表jpg格式的图片数据
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
	 * 这是一个通用的方法，利用了JAVA的反射机制，可以将放置在JAVA集合中并且符号一定条件的数据以EXCEL 的形式输出到指定IO设备上
	 * 
	 * 
	 * 
	 * @param title
	 * 
	 *            表格标题名
	 * 
	 * @param headers
	 * 
	 *            表格属性列名数组
	 * 
	 * @param dataset
	 * 
	 *            需要显示的数据集合,集合中一定要放置符合javabean风格的类的对象。此方法支持的
	 * 
	 *            javabean属性的数据类型有基本数据类型及String,Date,byte[](图片数据)
	 * 
	 * @param out
	 * 
	 *            与输出设备关联的流对象，可以将EXCEL文档导出到本地文件或者网络中
	 * 
	 * @param pattern
	 * 
	 *            如果有时间数据，设定输出格式。默认为"yyy-MM-dd"
	 */

	@SuppressWarnings("unchecked")
	public void exportExcel(String title, String[] headers, Collection<T> dataset, OutputStream out, String pattern) {
		// 声明一个工作薄
		HSSFWorkbook workbook = new HSSFWorkbook();
		// 生成一个表格
		HSSFSheet sheet = workbook.createSheet(title);
		// 设置表格默认列宽度为15个字节
		sheet.setDefaultColumnWidth((short) 15);
		// 生成一个样式
		HSSFCellStyle style = workbook.createCellStyle();
		// 设置这些样式
		style.setFillForegroundColor(HSSFColor.SKY_BLUE.index);
		style.setFillPattern(HSSFCellStyle.SOLID_FOREGROUND);
		style.setBorderBottom(HSSFCellStyle.BORDER_THIN);
		style.setBorderLeft(HSSFCellStyle.BORDER_THIN);
		style.setBorderRight(HSSFCellStyle.BORDER_THIN);
		style.setBorderTop(HSSFCellStyle.BORDER_THIN);
		style.setAlignment(HSSFCellStyle.ALIGN_CENTER);
		// 生成一个字体
		HSSFFont font = workbook.createFont();
		font.setColor(HSSFColor.VIOLET.index);
		font.setFontHeightInPoints((short) 12);
		font.setBoldweight(HSSFFont.BOLDWEIGHT_BOLD);
		// 把字体应用到当前的样式
		style.setFont(font);
		// 生成并设置另一个样式
		HSSFCellStyle style2 = workbook.createCellStyle();
		style2.setFillForegroundColor(HSSFColor.WHITE.index);
		style2.setFillPattern(HSSFCellStyle.SOLID_FOREGROUND);
		style2.setBorderBottom(HSSFCellStyle.BORDER_THIN);
		style2.setBorderLeft(HSSFCellStyle.BORDER_THIN);
		style2.setBorderRight(HSSFCellStyle.BORDER_THIN);
		style2.setBorderTop(HSSFCellStyle.BORDER_THIN);
		style2.setAlignment(HSSFCellStyle.ALIGN_CENTER);
		style2.setVerticalAlignment(HSSFCellStyle.VERTICAL_CENTER);
		// 生成另一个字体
		HSSFFont font2 = workbook.createFont();
		// font2.setColor(HSSFColor.BLACK.index);
		font2.setBoldweight(HSSFFont.BOLDWEIGHT_NORMAL);
		// 把字体应用到当前的样式
		style2.setFont(font2);
		// 声明一个画图的顶级管理器
		HSSFPatriarch patriarch = sheet.createDrawingPatriarch();
		// 定义注释的大小和位置,详见文档
		HSSFComment comment = patriarch.createComment(new HSSFClientAnchor(0, 0, 0, 0, (short) 4, 2, (short) 6, 5));
		// 设置注释内容
		comment.setString(new HSSFRichTextString("脱敏策略配置"));
		// 设置注释作者，当鼠标移动到单元格上是可以在状态栏中看到该内容.
		comment.setAuthor("leno");
		// 产生表格标题行
		HSSFRow row = sheet.createRow(0);
		for (short i = 0; i < headers.length; i++) {
			HSSFCell cell = row.createCell(i);
			cell.setCellStyle(style);
			HSSFRichTextString text = new HSSFRichTextString(headers[i]);
			cell.setCellValue(text);
		}
		// 遍历集合数据，产生数据行
		Iterator<T> it = dataset.iterator();
		int index = 0;
		while (it.hasNext()) {
			index++;
			row = sheet.createRow(index);
			T t = (T) it.next();
			// 利用反射，根据javabean属性的先后顺序，动态调用getXxx()方法得到属性值
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
					// 判断值的类型后进行强制类型转换
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
						// 其它数据类型都当作字符串简单处理

						if (value != null) {
							textValue = value.toString();
						} else {
							textValue = "";
						}
					}
					// 如果不是图片数据，就利用正则表达式判断textValue是否全部由数字组成

					Pattern p = Pattern.compile("^//d+(//.//d+)?$");
					Matcher matcher = p.matcher(textValue);
					if (matcher.matches()) {
						// 是数字当作double处理
						cell.setCellValue(Double.parseDouble(textValue));
					} else {
						// System.out.println("textValue:" + textValue + "第几行："
						// + index + "第几个：" + i);
						HSSFRichTextString richString = new HSSFRichTextString(textValue);
						/*
						 * HSSFFont font3 = workbook.createFont();
						 * font3.setColor(HSSFColor.BLACK.index);
						 * richString.applyFont(font3);
						 */
						cell.setCellValue(richString);
					}

				} catch (Exception e) {
					log.error("错误信息", e);
				} finally {
					// 清理资源
				}
			}
		}
		try {
			workbook.write(out);
		} catch (IOException e) {
			log.error("错误信息", e);
		}
	}

	/**
	 * 解析excel
	 * 
	 * @param is
	 *            输入流
	 * @param row_start
	 *            起始行
	 * @return 对象数组类型的集合
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
			// 总行数
			int rowNum = sheet.getLastRowNum();
			row = sheet.getRow(0);
			// 总列数
			int colNum = row.getPhysicalNumberOfCells();
			int row_start_init = 0;
			/*
			 * 指定解析开始行、结束行、开始列、结束列
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
			log.error("错误信息", e);
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
			// 总行数
			int rowNum = sheet.getLastRowNum();
			row = sheet.getRow(0);
			// 总列数
			int colNum = row.getPhysicalNumberOfCells();
			int row_start_init = 0;
			/*
			 * 指定解析开始行、结束行、开始列、结束列
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
			log.error("错误信息", e);
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
	 * 解析excel
	 * 
	 * @param is
	 *            输入流
	 * @param row_start
	 *            起始行
	 * @return 对象数组类型的集合
	 */
	public static Map<String, Object> readExcel(InputStream is, Integer row_start) {
		POIFSFileSystem fs = null;
		HSSFWorkbook wb = null;
		HSSFSheet sheet = null;
		HSSFRow row = null;
		Map<String, Object> mp = new HashMap<String, Object>();
		List<Object[]> data = new ArrayList<Object[]>();
		// 拉链表MAP
		Map<String, String> tablemap = new HashMap<String, String>();
		try {
			fs = new POIFSFileSystem(is);
			wb = new HSSFWorkbook(fs);

			sheet = wb.getSheetAt(0);
			// 总行数
			int rowNum = sheet.getLastRowNum();
			row = sheet.getRow(0);
			// 总列数
			int colNum = row.getPhysicalNumberOfCells();
			int row_start_init = 0;
			/*
			 * 指定解析开始行、结束行、开始列、结束列
			 */
			if (row_start != null) {
				row_start_init = row_start;
			}
			Object[] objs;
			if (rowNum > 0) {
				for (int i = row_start_init; i <= rowNum; i++) {
					row = sheet.getRow(i);
					objs = new Object[colNum];
					// 是否拉链表逻辑
					Cell ce = row.getCell(20);
					if (ce.getCellType() == Cell.CELL_TYPE_STRING) {
						if (!Utl.isEmpty(ce.getStringCellValue())&&"拉链表".equals(ce.getStringCellValue())) {
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
			log.error("错误信息", e);
		}
		mp.put("LISTDATE", data);
		// 拉链表MAP
		mp.put("MAPDATE", tablemap);
		return mp;
	}
}
