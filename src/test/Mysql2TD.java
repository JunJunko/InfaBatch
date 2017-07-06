package test;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.io.RandomAccessFile;
import java.nio.channels.FileChannel;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class Mysql2TD {

	public static void readTxtFile(String filePath) {
		try {
			String encoding = "UTF8";
			File file = new File(filePath);
			if (file.isFile() && file.exists()) { // 判断文件是否存在
				InputStreamReader read = new InputStreamReader(new FileInputStream(file), encoding);// 考虑到编码格式
				BufferedReader bufferedReader = new BufferedReader(read);
				String lineTxt = null;
				StringBuffer sb = new StringBuffer();
				RandomAccessFile rf = new RandomAccessFile("a.sql", "rw");
				FileChannel fc = rf.getChannel();
				// 将文件大小截为0
				fc.truncate(0);

				// // 用 Pattern 类的 matcher() 方法生成一个 Matcher 对象
				// Matcher m = p.matcher("Kelvin Li and Kelvin Chan are both
				// working in " +
				// "Kelvin Chen's KelvinSoftShop company");
				while ((lineTxt = bufferedReader.readLine()) != null) {
					// System.out.println(lineTxt);
					if (lineTxt.matches(".*VARCHAR.*") && lineTxt.matches(".*,.*")) {
						sb.append(lineTxt.replace("),", ")  CHARACTER SET UNICODE CASESPECIFIC,").replace("NOT NULL,",
								"CHARACTER SET UNICODE CASESPECIFIC not null,") + "\r");
						if (!lineTxt.matches(".*,.*")) {
							sb.append(lineTxt + " CHARACTER SET UNICODE CASESPECIFIC\r");
						}
					} else if (lineTxt.matches(".*VARCHAR.*") && !lineTxt.matches(".*,.*")) {
						sb.append(lineTxt + " CHARACTER SET UNICODE CASESPECIFIC\r");
					} else if (lineTxt.matches(".*prompt.*")) {
						// sb.append(lineTxt+"\r");
					} else {
						sb.append(lineTxt + "\r");
					}
				}
				read.close();

				String[] strr = sb.toString().split(";");

				for (int i = 0; i < strr.length; i++) {
					/*
					 * oracle转换
					 */
					// System.out.println(strr[i].contains("create table"));

					// 只把建表语句输出、可以更改为只输出index
					StringBuffer a = new StringBuffer();
					if (strr[i].contains("CREATE TABLE")) {
						Pattern pattern = Pattern.compile("CREATE TABLE(.*)");

						Matcher matcher = pattern.matcher(strr[i]);

						if (matcher.find()) {
							// System.out.println(matcher.group(1));

							a.append("DROP TABLE ODS_DDL." + matcher.group(1).trim() + ";");
						}

						a.append(strr[i].replace("VARCHAR2", "VARCHAR").replace("DATE", "TIMESTAMP(6)")
								.replace("NUMBER", "DECIMAL").replace("VARCHAR(3999", "VARCHAR(4000)")
								.replace(" CHAR)", ")").replace("sysdate", "CURRENT_TIMESTAMP(0)")
								.replace("create table ", "CREATE MULTISET TABLE ODS_DDL.")
								.replace("\r(",
										",NO FALLBACK ,\r" + "     NO BEFORE JOURNAL,\r" + "     NO AFTER JOURNAL,\r"
												+ "     CHECKSUM = DEFAULT,\r" + "     DEFAULT MERGEBLOCKRATIO (")
								.replace("==", "").replace(" month ", " \"MONTH\" ").replace(" time ", " \" TIME \" ")
								.replace(" day ", " \" DAY \" ").replace(" type ", " \" TYPE \" ")
								.replace(" password ", " \" PASSWORD \" ").replace(" period ", " \" PERIOD \" ")
								.replace(" version ", " \" VERSION \" "));

						method2("a.sql", a.toString().toUpperCase() + ";");

					}

				}
			} else {
				System.out.println("找不到指定的文件");
			}
		} catch (Exception e) {
			System.out.println("读取文件内容出错");
			e.printStackTrace();
		}

	}

	public static void method2(String file, String conent) {
		BufferedWriter out = null;

		try {
			out = new BufferedWriter(new OutputStreamWriter(new FileOutputStream(file, true)));
			out.write(conent + "\r\n");
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			try {
				out.close();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
	}

	public static void main(String[] args) {
		// TODO Auto-generated method stub
		readTxtFile("F:\\g工作资料\\shsnc\\无限极\\建表语句\\售后服务系统.sql");
		// String str5 = " "+
		// " "+
		// " "+
		// "Creating table CX_FXH_BUYPV "+
		// "=========================== "+
		// " "+
		// "create table CX_FXH_BUYPV "+
		// " "+
		// " row_id VARCHAR(15 ) not null, "+
		// " created TIMESTAMP(6) default CURRENT_TIMESTAMP(0) not null, "+
		// " created_by VARCHAR(15 ) not null, "+
		// " last_upd TIMESTAMP(6) default CURRENT_TIMESTAMP(0) not null, "+
		// " last_upd_by VARCHAR(15 ) not null, "+
		// " modification_num DECIMAL(10) default 0 not null, "+
		// " conflict_id VARCHAR(15 ) default '0' not null, "+
		// " db_last_upd TIMESTAMP(6), "+
		// " pv_date TIMESTAMP(6), "+
		// " pv_money DECIMAL(10), "+
		// " pv_num DECIMAL(10), "+
		// " act_name VARCHAR(50 ), "+
		// " card_name VARCHAR(50 ), "+
		// " card_num VARCHAR(15 ), "+
		// " card_phone VARCHAR(15 ), "+
		// " db_last_upd_src VARCHAR(50 ), "+
		// " reffer_num VARCHAR(15 ), "+
		// " pv_month VARCHAR(15 ) "+
		// " "+
		// " "+
		// " ";
		//
		// Pattern pattern= Pattern.compile("create table(.*)");
		//
		// Matcher matcher = pattern.matcher(str5);
		//
		// if (matcher.find()) {
		// System.out.println(matcher.group(1));
		// }
		// System.out.println(str5.matches(".*"));
	}

}
