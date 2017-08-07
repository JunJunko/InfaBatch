package org.tools;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.util.ArrayList;
import java.util.List;
import java.util.Properties;





import org.FactoryMapping;

import com.exprotmeteexcel.utl.Utl;
import com.informatica.metadata.common.datarecord.Key;;

public class GetProperties /*extends FactoryMapping*/{
	
	static String profilepath= Utl.getProperties( "properties\\Pub.properties").get("PROJECTPATH").toString(); 
    private static Properties props = new Properties();   

    static {   
        try {   
            props.load(new FileInputStream(profilepath));   
        } catch (FileNotFoundException e) {   
            e.printStackTrace();   
            System.exit(-1);   
        } catch (IOException e) {          
            System.exit(-1);   
        }   
    }   
    
    public static String getKeyValue(String key) {   
    	List<String> interactive = new ArrayList<String>();
    	interactive.add("TableNm");
    	interactive.add("ExcelPath");
    	interactive.add("PROJECTPATH");
    	String interactivePath = "properties\\interactiveconfig\\interactive.properties";
    	if(interactive.contains(key)){
    		try {
				props.load(new FileInputStream(interactivePath));
			} catch (FileNotFoundException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} 
    	}
        return props.getProperty(key);   
    }
    
    
    static String Pubprofilepath="properties\\Pub.properties"; 
    private static Properties Pubprops = new Properties();   
    static {   
        try {   
        	Pubprops.load(new FileInputStream(Pubprofilepath));   
        } catch (FileNotFoundException e) {   
            e.printStackTrace();   
            System.exit(-1);   
        } catch (IOException e) {          
            System.exit(-1);   
        }   
    }   
    
    public static String getPubKeyValue(String key) {   
        return Pubprops.getProperty(key);   
    }
    
  
    
    
    public static void writeProperties(String keyname,String keyvalue) {          
        try {   
            // 调用 Hashtable 的方法 put，使用 getProperty 方法提供并行性。   
            // 强制要求为属性的键和值使用字符串。返回值是 Hashtable 调用 put 的结果。   
        	String tmppath = "properties\\interactiveconfig\\interactive.properties";
            OutputStream fos = new FileOutputStream(tmppath);   
            props.setProperty(keyname, keyvalue);   
            // 以适合使用 load 方法加载到 Properties 表中的格式，   
            // 将此 Properties 表中的属性列表（键和元素对）写入输出流   
            props.store(fos, "Update '" + keyname + "' value");   
        } catch (IOException e) {   
            System.err.println("属性文件更新错误");   
        }   
    }  

}
