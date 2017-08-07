package org.tools;

import java.util.Arrays;
import java.util.List;

import org.Parameter;

/**
* =============================================
* @Copyright 2017上海新炬网络技术有限公司
* @version：1.0.1
* @author：Junko
* @date：2017年7月11日下午4:09:55
* @Description: 将TD的关键字识别出来，并在末尾添加"_OG"
* =============================================
 */
public class RePlaceOG implements Parameter{

	public static List<String> OG(){
		
		String[] strArray = null;  
        strArray = org.tools.GetProperties.getPubKeyValue("OGCloumn").toUpperCase().split(",");
        List<String> OGCloumn =  Arrays.asList(strArray);
		return OGCloumn;
		
	}
	
public static List<String> DT(){
		
		String[] strArray = null;  
        strArray = "DW_START_DT,DW_END_DT,DW_ETL_DT".toUpperCase().split(",");
        List<String> OGCloumn =  Arrays.asList(strArray);
		return OGCloumn;
		
	}

public static void main(String[] args) {

//	updateAttributeValue("D:\\workspace\\Uoo-master\\xml\\M_TLK_ONSITE_SERVICE_1_H.xml", "拉链表");
	 System.out.println(DT().get(1));

}
}
