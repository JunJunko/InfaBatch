package org.test;

import java.io.File;
import java.sql.Array;
import java.util.ArrayList;
import java.util.List;

import org.tools.ExcelUtil;

import com.exprotmeteexcel.utl.DateTran.DataTypeTrans;

public class test {
	
	public void test(){
		boolean a = 12356788 >>> 16 == 0;
		String g = "";
		System.out.println(a);
		
	}
	public static void stringTes1t(){
	    String a = "a"+"b"+1;
	    String b = "ab1";
	    System.out.println(a == b);
	}

	public static void main(String[] args) {
//		ArrayList<ArrayList<String>> TableConf = ExcelUtil
//				.readExecl(org.tools.GetProperties.getKeyValue("ExcelPath"));
//	    System.out.println(TableConf);
        File a = new File("xml\\InitXml");		
        System.out.println(a.listFiles());		
        File fa[] = a.listFiles();
        for (int i = 0; i < fa.length; i++) {
        	File l = fa[i];
        	System.out.println(l.getName());
        }
	}
            
}
