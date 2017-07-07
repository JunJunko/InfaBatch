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
    	String path= new String();
    	String FileInitPath = "xml\\ALL_"+org.tools.GetProperties.getKeyValue("System")+"_DELETE_ALL_INSERT";
    	String FileIncrePath = "xml\\ALL_"+org.tools.GetProperties.getKeyValue("System")+"_UPSERT_CHAIN_INCREMENT";
    	String FileCheckPath = "xml\\ALL_"+org.tools.GetProperties.getKeyValue("System")+"_INCREMENT";
 
        
        File FileInit=new File(path);
        File FileIncre=new File(path);
        File FileCheck=new File(path);
      
        if(FileInit.exists()){
        	try {
				FileInit.createNewFile();
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
        }
        
        if(FileIncre.exists()){
        	try {
				FileIncre.createNewFile();
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
        }
        
        if(FileCheck.exists()){
        	try {
				FileCheck.createNewFile();
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
        }
        FileOutputStream out=new FileOutputStream(file,true); 
        File file=new File(path);
        if(file.exists()){
            try {
				file.createNewFile();
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
        }
    	for(int i = 0; i< FileList.size(); i++){
    		
    		SessionMessage = GetXmlSeesionElement.GetSession("xml\\"+FileList.get(i));
    		System.out.println(SessionMessage.toString());
    		out.write(sb.toString().getBytes("GBK"));//注意需要转换对应的字符集
          out.close();
    	}
//    	System.out.println(SessionMessage.toString());
//    	return SessionMessage.toString();
    	


//        FileOutputStream out=new FileOutputStream(file,true); //如果追加方式用true        
//        StringBuffer sb=new StringBuffer();
//        sb.append(SessionMessage+"\n");
//        out.write(sb.toString().getBytes("GBK"));//注意需要转换对应的字符集
//        out.close();
//        }
//        catch(IOException ex)
//        {
//            System.out.println(ex.getStackTrace());
//        }
    }
	
	public static void main(String[] args) {
		// TODO Auto-generated method stub
		
		MegerElement();
//		GetXmlSeesionElement.GetSession("xml\\CheckXml\\M_CHECK_KMS_COMP_DBCP_CK.xml");
	}

}
