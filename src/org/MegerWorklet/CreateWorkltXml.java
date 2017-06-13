package org.MegerWorklet;

import org.MegerWorklet.GetXmlSeesionElement;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.ArrayList;

import org.MegerWorklet.GetAllXmlList;


public class CreateWorkltXml {
	public void WriteXml(String str){
		 
	}
	
    public  static void MegerElement(){

    	ArrayList<String> FileList = new ArrayList<String>(GetAllXmlList.getFileName());
    	String SessionMessage = new String();
    	for(int i = 0; i< FileList.size(); i++){
    		
    		SessionMessage = GetXmlSeesionElement.GetSession("xml\\"+FileList.get(i));
//    		System.out.println(SessionMessage.size());
    	}
//    	System.out.println(SessionMessage.toString());
//    	return SessionMessage.toString();
    	
    	try
        {
        String path="Pmrep.bat";
        File file=new File(path);
        if(file.exists()){
            file.createNewFile();
        }
        FileOutputStream out=new FileOutputStream(file,true); //如果追加方式用true        
        StringBuffer sb=new StringBuffer();
        sb.append(SessionMessage+"\n");
        out.write(sb.toString().getBytes("GBK"));//注意需要转换对应的字符集
        out.close();
        }
        catch(IOException ex)
        {
            System.out.println(ex.getStackTrace());
        }
    }
	
	public static void main(String[] args) {
		// TODO Auto-generated method stub
		
//		System.out.println(MegerElement());
//		GetXmlSeesionElement.GetSession("xml\\CheckXml\\M_CHECK_KMS_COMP_DBCP_CK.xml");
	}

}
