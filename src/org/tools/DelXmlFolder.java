package org.tools;

import java.io.File;

/**
* =============================================
* @Copyright 2017上海新炬网络技术有限公司
* @version：1.0.1
* @author：Junko
* @date：2017年7月11日下午3:32:54
* @Description: 情况生成XML文件的目录
* =============================================
 */
public class DelXmlFolder {
	public static void delFolder(String folderPath) {
	     try {
	        delAllFile(folderPath); //删除完里面所有内容
	        String filePath = folderPath;
	        filePath = filePath.toString();
	        java.io.File myFilePath = new java.io.File(filePath);
	        myFilePath.delete(); //删除空文件夹
	     } catch (Exception e) {
	       e.printStackTrace(); 
	     }
	}

	
	
	/**
	 * @version: 1.0.1
	 * @author: Junko
	 * @date: 2017年7月11日下午3:33:24 
	 * @Description: 删除指定目录下的所有文件
	 * @param path
	 * @return 操作标识
	 */
	public static boolean delAllFile(String path) {
	       boolean flag = false;
	       File file = new File(path);
	       if (!file.exists()) {
	         return flag;
	       }
	       if (!file.isDirectory()) {
	         return flag;
	       }
	       String[] tempList = file.list();
	       File temp = null;
	       for (int i = 0; i < tempList.length; i++) {
	          if (path.endsWith(File.separator)) {
	             temp = new File(path + tempList[i]);
	          } else {
	              temp = new File(path + File.separator + tempList[i]);
	          }
	          if (temp.isFile()) {
	             temp.delete();
	          }
	          if (temp.isDirectory()) {
	             delAllFile(path + "/" + tempList[i]);//先删除文件夹里面的文件
	             delFolder(path + "/" + tempList[i]);//再删除空文件夹
	             flag = true;
	          }
	       }
	       return flag;
	     }
	

	public static void main(String[] args) {
		// TODO Auto-generated method stub
		delAllFile("D:\\workspace\\Uoo\\xml\\");
	}

}