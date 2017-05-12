package org.tools;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class CallPmrepCom {
	
	public static void writeLog(String str)
    {
        try
        {
        String path="Pmrep.bat";
        File file=new File(path);
        if(file.exists()){
            file.createNewFile();
        }
        FileOutputStream out=new FileOutputStream(file,true); //���׷�ӷ�ʽ��true        
        StringBuffer sb=new StringBuffer();
        sb.append(str+"\n");
        out.write(sb.toString().getBytes("GBK"));//ע����Ҫת����Ӧ���ַ���
        out.close();
        }
        catch(IOException ex)
        {
            System.out.println(ex.getStackTrace());
        }
    } 
	
	public static List<String> getFileName() {
        String path = "D:\\workspace\\Uoo\\xml"; // ·��
        File f = new File(path);
        if (!f.exists()) {
            System.out.println(path + " not exists");
        }

        File fa[] = f.listFiles();
        List<String> a = new ArrayList<String>();
        for (int i = 0; i < fa.length; i++) {
            File fs = fa[i];
            if (!fs.isDirectory()) {
            	a.add(fs.getName());
            }
        }
        return a;
    }

	public static void main(String[] args) {
		
		
		// TODO Auto-generated method stub
		
//		pmrep objectImport -i D:\workspace\Uoo\m_CRM_CX_FXH_LOGIN.xml -c D:\workspace\Uoo\sample_import_controlfile.xml


            
//			Runtime runtime=Runtime.getRuntime();

//			try{
////				String commandStr = "cmd /k D:\\software\\Informatica\\9.6.1\\clients\\PowerCenterClient\\client\\binpmrep connect -r dev_store_edw  -h etldsvr01 -o 6005 -n NC_ZJK -x 499099784";
////				Runtime.getRuntime().exec(commandStr);
//
//
//			}catch(Exception e){

			List<String> FN = getFileName();
			writeLog("D:\\software\\Informatica\\9.6.1\\clients\\PowerCenterClient\\client\\bin\\pmrep connect -r dev_store_edw  -h etldsvr01 -o 6005 -n NC_ZJK -x 499099784\r");
			for (int i = 0; i < FN.size(); i++){
//				writeLog("D:\\software\\Informatica\\9.6.1\\clients\\PowerCenterClient\\client\\bin\\pmrep objectImport -i F:\\shsnc\\��Ŀ\\���޼�\\import_xml\\0302\\ȫɾȫ��\\"+FN.get(i)+" -c F:\\shsnc\\��Ŀ\\���޼�\\import_xml\\0302\\ȫɾȫ��\\sample_import_controlfile.xml\r");
				writeLog("D:\\software\\Informatica\\9.6.1\\clients\\PowerCenterClient\\client\\bin\\pmrep objectImport -i D:\\workspace\\Uoo\\xml\\"+FN.get(i)+" -c D:\\workspace\\Uoo\\sample_import_controlfile.xml\r");
           
			}
			System.out.println("Done!");
	
	}

			

			
	
	}


