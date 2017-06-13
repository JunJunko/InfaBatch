package org.MegerWorklet;

import java.io.File;
import java.util.ArrayList;
import java.util.List;


public class GetAllXmlList {
	static String path = "xml\\";
	public static List<String> getFileName() {
        // Â·¾¶
       File f = new File(path);
       if (!f.exists()) {
           System.out.println(path + " not exists");
       }

       File fa[] = f.listFiles();
       List<String> a = new ArrayList<String>();
       for (int i = 0; i < fa.length; i++) {
           File fs = fa[i];
           if (fs.isDirectory()) {
           	
//           	System.out.println(fs.getName());
           	File ffs[] = fs.listFiles();
           	for (int j = 0; j < ffs.length; j++){
           		File l = ffs[j];
//           		System.out.println(l.getName());
           		if (!l.isDirectory()) {
           			a.add(fs.getName()+"\\"+l.getName());
           		}
           	}
           	
           	
           }
       }
       return a;
   }

	public static void main(String[] args) {
		// TODO Auto-generated method stub
		System.out.println(getFileName());

	}

}
