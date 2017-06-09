package org.tools;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.util.Properties;





import org.FactoryMapping;;

public class GetProperties /*extends FactoryMapping*/{
	
	static String profilepath="ELN.properties"; 
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
        return props.getProperty(key);   
    }
    
    
    static String Pubprofilepath="Pub.properties"; 
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
            // ���� Hashtable �ķ��� put��ʹ�� getProperty �����ṩ�����ԡ�   
            // ǿ��Ҫ��Ϊ���Եļ���ֵʹ���ַ���������ֵ�� Hashtable ���� put �Ľ����   
            OutputStream fos = new FileOutputStream(profilepath);   
            props.setProperty(keyname, keyvalue);   
            // ���ʺ�ʹ�� load �������ص� Properties ���еĸ�ʽ��   
            // ���� Properties ���е������б�����Ԫ�ضԣ�д�������   
            props.store(fos, "Update '" + keyname + "' value");   
        } catch (IOException e) {   
            System.err.println("�����ļ����´���");   
        }   
    }  

}
