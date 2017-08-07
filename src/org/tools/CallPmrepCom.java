package org.tools;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * Describe:
 * @author Junko
 * 
 *
 */
public class CallPmrepCom {
	static String path  = "D:\\workspace\\InfaBatch\\xml\\";

	
	public static void writeLog(String str)
    {
        try
        {
        String path="Pmrep.bat";
        File file=new File(path);
        if(file.exists()){
            file.createNewFile();
        }
        FileOutputStream out=new FileOutputStream(file,true); //如果追加方式用true        
        StringBuffer sb=new StringBuffer();
        sb.append(str+"\n");
        out.write(sb.toString().getBytes("GBK"));//注意需要转换对应的字符集
        out.close();
        }
        catch(IOException ex)
        {
            System.out.println(ex.getStackTrace());
        }
    } 
	
	public static List<String> getFileName() {
         // 路径
        File f = new File(path);
        if (!f.exists()) {
            System.out.println(path + " not exists");
        }

        File fa[] = f.listFiles();
        List<String> a = new ArrayList<String>();
        for (int i = 0; i < fa.length; i++) {
            File fs = fa[i];
            if (fs.isDirectory()) {
            	
//            	System.out.println(fs.getName());
            	File ffs[] = fs.listFiles();
            	for (int j = 0; j < ffs.length; j++){
            		File l = ffs[j];
//            		System.out.println(l.getName());
            		if (!l.isDirectory()) {
            			a.add(fs.getName()+"\\"+l.getName());
            		}
            	}
            	
            	
            }
        }
        return a;
    }
	
	public static String ReCtrlPath(String Input){
		String CPath = "";
		System.out.println(Input);
		String[] FileNM = Input.split("\\\\");
		
		List list = Arrays.asList(FileNM);
		

		System.out.println(Input);
		if(list.get(0).equals("InitXml")){
			System.out.println(list.get(0));
			CPath = "D:\\software\\Informatica\\9.6.1\\clients\\PowerCenterClient\\client\\bin\\pmrep objectImport -i "+path+Input+" -c D:\\workspace\\Uoo-master\\CtrlFile\\init.xml\r";
		}else if (list.get(0).equals("CheckXml")){
			CPath = "D:\\software\\Informatica\\9.6.1\\clients\\PowerCenterClient\\client\\bin\\pmrep objectImport -i "+path+Input+" -c D:\\workspace\\Uoo-master\\CtrlFile\\check.xml\r";
		}else {
			CPath = "D:\\software\\Informatica\\9.6.1\\clients\\PowerCenterClient\\client\\bin\\pmrep objectImport -i "+path+Input+" -c D:\\workspace\\Uoo-master\\CtrlFile\\increment.xml\r";
		}

		return CPath;

	}

	public static void main(String[] args) {
		
		
		// TODO Auto-generated method stub
		

			List<String> FN = getFileName();
			writeLog("D:\\software\\Informatica\\9.6.1\\clients\\PowerCenterClient\\client\\bin\\pmrep connect -r dev_store_edw  -h etldsvr01 -o 6005 -n NC_ZJK -x 499099784\r");
			for (int i = 0; i < FN.size(); i++){
//				writeLog("D:\\software\\Informatica\\9.6.1\\clients\\PowerCenterClient\\client\\bin\\pmrep objectImport -i F:\\shsnc\\项目\\无限极\\import_xml\\0302\\全删全插\\"+FN.get(i)+" -c F:\\shsnc\\项目\\无限极\\import_xml\\0302\\全删全插\\sample_import_controlfile.xml\r");
				writeLog(ReCtrlPath(FN.get(i)));
//				ReCtrlPath(FN.get(i));
			}
			System.out.println("Done!");
	
	}

			

			
	
	}


