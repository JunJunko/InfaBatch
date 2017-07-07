package org.test;

import java.io.ObjectStreamField;
import java.io.Serializable;
import java.util.Arrays;

public class StringTest 
             implements Serializable, Comparable<String>, CharSequence{
	
	  private final char[] value;
	  private int hash;
	  private static final long serialVersionUID = -6849794470754667710L;
	  private static final ObjectStreamField[] serialPersistentFields = new ObjectStreamField[0];
//	  private static final int HASHING_SEED = i;
	  private transient int hash32 = 0;

	  
	  public StringTest() {
	        this.value = new char[0];
	    }
	  
	  public StringTest(StringTest paramString)
	  {
	    this.value = paramString.value;
	    this.hash = paramString.hash;
	  }
	  
	  public StringTest(char[] paramArrayOfChar)
	  {
	    this.value = Arrays.copyOf(paramArrayOfChar, paramArrayOfChar.length);
	  }
	  
	@Override
	public char charAt(int arg0) {
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	public int length() {
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	public CharSequence subSequence(int arg0, int arg1) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public int compareTo(String arg0) {
		// TODO Auto-generated method stub
		return 0;
	}

}
