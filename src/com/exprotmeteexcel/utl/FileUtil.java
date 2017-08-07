package com.exprotmeteexcel.utl;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Properties;

/**
 * �ļ�������
 * 
 * @author wujunqing
 * @date 2017-06-26
 */
public class FileUtil {

	// /**
	// * ��ȡ��ȡϵͳ��Ŀ¼��xml�ļ���·���ķ�����
	// */
	// @SuppressWarnings("unused")
	// private String projectFilePath = new File("").getAbsolutePath()
	// + "\\config.xml";
	//
	// /**
	// * ��ȡWeb��Ŀ������(Դ����src��)classesĿ¼��ĳ��xml·���ķ�����
	// */
	// @SuppressWarnings("unused")
	// private String srcFilePath = getClass().getClassLoader().getResource(
	// "config.xml").getPath();

	static File file = null;
	static boolean flag = false;

	/**
	 * д��txt�ļ���������ԭ�ļ����ݵĻ�����׷������(���ж�Ŀ¼�Ƿ���ڣ�������������Ŀ¼)
	 * 
	 * @param value
	 *            д���ļ�����
	 * @param fileCatage
	 *            �ļ���Ŀ¼��
	 * @param fileName
	 *            �ļ����֣�
	 * @param code
	 *            �ļ��ı��룻
	 * @throws IOException
	 */
	public static boolean WriteFile(String value, String fileCatage, String fileName, String code) {
		boolean successful = false;
		File file = null;
		FileOutputStream out = null;
		try {
			file = new File(fileCatage);
			if (!file.isDirectory()) {
				file.mkdir();
			}
			file = new File(fileCatage + fileName);
			if (!file.exists()) {
				file.createNewFile();
			}
			out = new FileOutputStream(file);
			out.write(value.getBytes(code));
			out.close();

			successful = true;
		} catch (IOException e) {
			successful = false;
			e.printStackTrace();
		} finally {
			if (out != null) {
				try {
					out.close();
				} catch (IOException e) {
				}
			}
		}
		return successful;
	}

	/***
	 * ����ԭ�������ݣ�
	 * 
	 * @param filePath
	 *            �ļ���·��
	 * @param content
	 *            ��������ݣ�
	 * @return
	 */
	public static boolean saveFile(String filePath, String content) {
		boolean successful = true;
		FileOutputStream fout = null;
		try {
			fout = new FileOutputStream(new File(filePath), false);
			fout.write(content.getBytes());
		} catch (FileNotFoundException e1) {
			successful = false;
		} catch (IOException e) {
			successful = false;
		} finally {
			if (fout != null) {
				try {
					fout.close();
				} catch (IOException e) {
				}
			}
		}
		return successful;
	}

	/**
	 * ɾ���ļ����ۺϲ���( ����·��ɾ��ָ����Ŀ¼���ļ������۴������)
	 * 
	 * @param sPath
	 *            Ҫɾ����Ŀ¼���ļ�
	 * @return ɾ���ɹ����� true�����򷵻� false��
	 */
	public static boolean DeleteFolder(String sPath) {
		flag = false;
		file = new File(sPath);
		// �ж�Ŀ¼���ļ��Ƿ����
		if (!file.exists()) { // �����ڷ��� false
			return flag;
		} else {
			// �ж��Ƿ�Ϊ�ļ�
			if (file.isFile()) { // Ϊ�ļ�ʱ����ɾ���ļ�����
				return deleteFile(sPath);
			} else { // ΪĿ¼ʱ����ɾ��Ŀ¼����
				return deleteDirectory(sPath);
			}
		}
	}

	/**
	 * ɾ�������ļ�
	 * 
	 * @param sPath
	 *            ��ɾ���ļ����ļ���
	 * @return �����ļ�ɾ���ɹ�����true�����򷵻�false
	 */
	public static boolean deleteFile(String sPath) {
		flag = false;
		file = new File(sPath);
		// ·��Ϊ�ļ��Ҳ�Ϊ�������ɾ��
		if (file.isFile() && file.exists()) {
			file.delete();
			flag = true;
		}
		return flag;
	}

	/**
	 * ɾ��Ŀ¼���ļ��У��Լ�Ŀ¼�µ��ļ�
	 * 
	 * @param sPath
	 *            ��ɾ��Ŀ¼���ļ�·��
	 * @return Ŀ¼ɾ���ɹ�����true�����򷵻�false
	 */
	public static boolean deleteDirectory(String sPath) {
		// ���sPath�����ļ��ָ�����β���Զ�����ļ��ָ���
		if (!sPath.endsWith(File.separator)) {
			sPath = sPath + File.separator;
		}
		File dirFile = new File(sPath);
		// ���dir��Ӧ���ļ������ڣ����߲���һ��Ŀ¼�����˳�
		if (!dirFile.exists() || !dirFile.isDirectory()) {
			return false;
		}
		flag = true;
		// ɾ���ļ����µ������ļ�(������Ŀ¼)
		File[] files = dirFile.listFiles();
		for (int i = 0; i < files.length; i++) {
			// ɾ�����ļ�
			if (files[i].isFile()) {
				flag = deleteFile(files[i].getAbsolutePath());
				if (!flag)
					break;
			} // ɾ����Ŀ¼
			else {
				flag = deleteDirectory(files[i].getAbsolutePath());
				if (!flag)
					break;
			}
		}
		if (!flag)
			return false;
		// ɾ����ǰĿ¼
		if (dirFile.delete()) {
			return true;
		} else {
			return false;
		}
	}

	/**
	 * ���ֽڡ�����ȡ�ļ������ݣ�
	 * 
	 * @param Offset
	 *            ��ȡ���ݵĿ�ʼ��
	 * @param length
	 *            ���ݵĳ��ȣ�
	 * @param filePath
	 *            �ļ���·����
	 * @param code
	 *            ���룻
	 * @return ������Ӧ�����ݣ�
	 * @throws Exception
	 */
	public static String readFileByByte(int Offset, int length, String filePath, String code) {
		File file = new File(filePath);
		FileInputStream fis = null;
		try {
			fis = new FileInputStream(file);
		} catch (FileNotFoundException e) {
			e.printStackTrace();
			return null;
		}
		try {
			fis.skip(Offset);
		} catch (IOException e) {
			e.printStackTrace();
			return null;
		}
		byte[] bytes = new byte[length];
		try {
			fis.read(bytes);
		} catch (IOException e) {
			e.printStackTrace();
			return null;
		}
		try {
			fis.close();
		} catch (IOException e) {
			e.printStackTrace();
			return null;
		}
		try {
			return new String(bytes, code);
		} catch (UnsupportedEncodingException e) {
			e.printStackTrace();
			return null;
		}
	}

	/**
	 * �����е��ı�����һ�� BufferedReader ��
	 * 
	 * @param filePath
	 *            �ļ�·��
	 * @param code
	 *            �����ʽ
	 * @return
	 * @throws IOException
	 */

	public static BufferedReader readToBufferedReader(String filePath, String code) throws IOException {
		BufferedReader bufferedReader = null;
		File file = new File(filePath);
		if (file.isFile() && file.exists()) { // �ж��ļ��Ƿ����
			InputStreamReader read = new InputStreamReader(new FileInputStream(file), code);// ���ǵ������ʽ
			bufferedReader = new BufferedReader(read);
		}
		return bufferedReader;
	}

	/**
	 * �����е��ı�����һ�� StringBuffer ��
	 * 
	 * @param filePath
	 *            �ļ�·��
	 * @throws IOException
	 */
	public static StringBuffer readToBuffer(String filePath, String code) {
		StringBuffer buffer = new StringBuffer();
		InputStream is;
		try {
			File file = new File(filePath);
			if (!file.exists())
				return null;
			is = new FileInputStream(filePath);
			String line; // ��������ÿ�ж�ȡ������
			BufferedReader reader = new BufferedReader(new InputStreamReader(new FileInputStream(file), code));
			line = reader.readLine(); // ��ȡ��һ��
			while (line != null) { // ��� line Ϊ��˵��������
				buffer.append(line); // ��������������ӵ� buffer ��
				buffer.append("\n"); // ��ӻ��з�
				line = reader.readLine(); // ��ȡ��һ��
			}
			reader.close();
			is.close();
		} catch (Exception e) {
			e.printStackTrace();
		}
		return buffer;
	}

	public static String loadFile(String filePath, String charset) {
		FileInputStream fin = null;
		StringBuffer sb = new StringBuffer();
		try {
			fin = new FileInputStream(new File(filePath));
			byte[] buffer = new byte[Integer.MAX_VALUE];
			int start = -1;
			while ((start = fin.read(buffer)) != -1) {
				sb.append(new String(buffer, 0, start, charset));
			}
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			if (fin != null) {
				try {
					fin.close();
				} catch (IOException e) {
				}
			}
		}
		return sb.toString();
	}

	/**
	 * ��ȡĳ��Ŀ¼�������ļ����߻�ȡĳ���ļ��Ĵ�С�� ��λ��MB
	 * 
	 * @param file
	 * @return
	 */
	public static double getDirSize(File file) {
		// �ж��ļ��Ƿ����
		if (file.exists()) {
			// �����Ŀ¼��ݹ���������ݵ��ܴ�С
			if (file.isDirectory()) {
				File[] children = file.listFiles();
				double size = 0;
				for (File f : children)
					size += getDirSize(f);
				return size;
			} else {// ������ļ���ֱ�ӷ������С,�ԡ��ס�Ϊ��λ
				double size = (double) file.length() / 1024 / 1024;
				return size;
			}
		} else {
			System.out.println("��ȡ�ļ���С���󣡣��ļ������ļ��в����ڣ�����·���Ƿ���ȷ��");
			return 0.0;
		}
	}

	/**
	 * ��ȡĳ��Ŀ¼�������ļ�·��
	 * 
	 * @param file
	 * @return List<String>
	 */
	public static List<String> getDirfile(File file) {
		List<String> filelist = new ArrayList<String>();
		// �ж��ļ��Ƿ����
		if (file.exists()) {
			// �����Ŀ¼��ݹ���������ݵ��ܴ�С
			if (file.isDirectory()) {
				File[] children = file.listFiles();
				for (File f : children) {
					filelist.add(f.getAbsolutePath());
				}
				return filelist;
			}
		} else {

			return filelist;
		}
		return filelist;
	}

	/**
	 * ��ȡĳ��Ŀ¼�������ļ�·��
	 * 
	 * @param file
	 * @return List<String>
	 */
	public static List<String> getDirfile(File[] files) {
		List<String> filelist = new ArrayList<String>();
		for (File fe : files) {
			filelist.addAll(getDirfile(fe));
		}
		return filelist;
	}

	/**
	 * ��ȡĳ��Ŀ¼�������ļ�·��
	 * 
	 * @param file
	 * @return List<String>
	 */
	public static List<String> getDirfileBySuffix(File file, String Suffix) {
		List<String> filelist = new ArrayList<String>();
		// �ж��ļ��Ƿ����
		if (file.exists()) {
			// �����Ŀ¼��ݹ���������ݵ��ܴ�С
			if (file.isDirectory()) {
				File[] children = file.listFiles();
				String fileName = "";
				for (File f : children) {
					fileName = f.getAbsolutePath();
					String suf = fileName.substring(fileName.lastIndexOf(".") + 1);
					if (Suffix.equals(suf)) {
						filelist.add(f.getAbsolutePath());
					}
				}
				return filelist;
			}
		} else {

			return filelist;
		}
		return filelist;
	}

	/**
	 * ��ȡĳ��Ŀ¼�������ļ�·��
	 * 
	 * @param file
	 * @return List<String>
	 */
	public static List<String> getDirfile(File[] files, String Suffix) {
		List<String> filelist = new ArrayList<String>();
		for (File fe : files) {
			filelist.addAll(gatAllFileBySuffix(fe, Suffix));
		}
		return filelist;
	}

	/**
	 * ��ȡĳ��Ŀ¼�����е��ļ���ȫ·�����ļ����ļ��ϣ�
	 * 
	 * @return
	 */
	public static List<List<String>> getAllFile(String mulu) {
		File file = new File(mulu);
		File[] files = file.listFiles();
		List<List<String>> ret = new ArrayList<List<String>>();
		List<String> allFilePath = new ArrayList<String>();
		List<String> allFileName = new ArrayList<String>();
		for (int i = 0; i < files.length; i++) {
			if (files[i].isDirectory()) {
				allFilePath.add(files[i].toString());
				allFileName.add(files[i].getName());
			}
		}
		ret.add(allFilePath);
		ret.add(allFileName);
		return ret;
	}

	public static List<String> gatAllFileBySuffix(File file, String Suffix) {
		List<String> lt = new ArrayList<String>();
		if (file.isDirectory())// �ж�file�Ƿ���Ŀ¼

		{
			File[] lists = file.listFiles();
			if (lists != null) {
				for (int i = 0; i < lists.length; i++) {
					if (lists[i].isFile()) {

						String fileName = lists[i].getAbsolutePath();
						String suf = fileName.substring(fileName.lastIndexOf(".") + 1);
						if (Suffix.equals(suf)) {
							lt.add(lists[i].getAbsolutePath());
						}
					} else
						lt.addAll(gatAllFileBySuffix(lists[i], Suffix));// ��Ŀ¼�͵ݹ����Ŀ¼���ٽ����ж�

				}
			}
		}
		return lt;
	}

	/**
	 * ��ȡĳ��Ŀ¼�����е��ļ����ϣ�
	 * 
	 * @return List
	 */
	public static List<String> getFile(String mulu) {
		File file = new File(mulu);
		File[] files = file.listFiles();
		List<String> ret = new ArrayList<String>();

		for (int i = 0; i < files.length; i++) {
			if (files[i].isFile()) {
				ret.add(files[i].toString());
			}
		}

		return ret;
	}

	/**
	 * ��ȡĳ��Ŀ¼�����е�XML�ļ����ϣ�
	 * 
	 * @return List
	 */
	public static List<String> getFileXml(String mulu) {
		File file = new File(mulu);
		File[] files = file.listFiles();
		List<String> ret = new ArrayList<String>();

		for (int i = 0; i < files.length; i++) {
			if (files[i].isFile()) {
				if (files[i].isFile()) {
					ret.add(files[i].toString());
				}

			}
		}

		return ret;
	}

	public static boolean wirteProperty(String path, String keyname, String keyvalue) {
		Properties prop = new Properties();// ���Լ��϶���
		FileInputStream fis;
		try {
			fis = new FileInputStream(path);
			prop.load(fis);// �������ļ���װ�ص�Properties������
			fis.close();// �ر���
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			return false;
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			return false;
		}
		prop.setProperty(keyname, keyvalue);
		// �ļ������
		try {
			FileOutputStream fos = new FileOutputStream(path);
			// ��Properties���ϱ��浽����
			prop.store(fos, "Copyright (c) Boxcode Studio");
			fos.close();// �ر���
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			return false;
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			return false;
		}
		System.out.println("��ȡ�޸ĺ������ֵ��" + keyname + "=" + prop.getProperty(keyname));
		return true;
	}
	/**
	 * ��ȡ·���ļ���
	 * 
	 * @return List
	 */
	public static String getfileName(String path) {
		File tempFile = new File(path.trim());
		String fileName = tempFile.getName();

		// System.out.println("����һ��fileName = " + fileName);
		return fileName;
	}
}