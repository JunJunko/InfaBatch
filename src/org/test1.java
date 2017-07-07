package org;
import java.util.ArrayList;
import java.util.List;

import org.tools.ExcelUtil;

public class test1 {

	public static void main(String[] args) {
		// TODO Auto-generated method stub
		
		Class<?> class1 = null;
        Class<?> class2 = null;
        Class<?> class3 = null;
        // 一般采用这种形式
        try {
			class1 = Class.forName("net.xsoftlab.baike.TestReflect");
		} catch (ClassNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
//        class2 = new TestReflect().getClass();
//        class3 = TestReflect.class;
        System.out.println("类名称   " + class1.getName());
        System.out.println("类名称   " + class2.getName());
        System.out.println("类名称   " + class3.getName());

}
}