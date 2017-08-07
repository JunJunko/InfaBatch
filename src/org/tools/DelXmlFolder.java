package org.tools;

import java.io.File;

/**
* =============================================
* @Copyright 2017�Ϻ��¾����缼�����޹�˾
* @version��1.0.1
* @author��Junko
* @date��2017��7��11������3:32:54
* @Description: �������XML�ļ���Ŀ¼
* =============================================
 */
public class DelXmlFolder {
	public static void delFolder(String folderPath) {
	     try {
	        delAllFile(folderPath); //ɾ����������������
	        String filePath = folderPath;
	        filePath = filePath.toString();
	        java.io.File myFilePath = new java.io.File(filePath);
	        myFilePath.delete(); //ɾ�����ļ���
	     } catch (Exception e) {
	       e.printStackTrace(); 
	     }
	}

	
	
	/**
	 * @version: 1.0.1
	 * @author: Junko
	 * @date: 2017��7��11������3:33:24 
	 * @Description: ɾ��ָ��Ŀ¼�µ������ļ�
	 * @param path
	 * @return ������ʶ
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
	             delAllFile(path + "/" + tempList[i]);//��ɾ���ļ���������ļ�
	             delFolder(path + "/" + tempList[i]);//��ɾ�����ļ���
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