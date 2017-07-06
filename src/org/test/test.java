package org.test;

import java.sql.Array;

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
		char [] b = {'a', 's', '1'};
 		StringTest a = new StringTest(b);
 		String c = new String(b);
		System.out.println(a);
		System.out.println(c);
		stringTes1t();
		
		String j = "a";
	    System.out.println(j.compareTo("bbbbuuuuuuuuuuuuuuuuuuuub"));
	}
            
}
