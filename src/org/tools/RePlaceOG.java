package org.tools;

import java.util.Arrays;
import java.util.List;

import org.Parameter;

public class RePlaceOG implements Parameter {

	public static List<String> OG() {

		String[] strArray = null;
		strArray = org.tools.GetProperties.getKeyValue("OGCloumn").toUpperCase().split(",");
		List<String> OGCloumn = Arrays.asList(strArray);
		return OGCloumn;

	}

	public static List<String> DT() {

		String[] strArray = null;
		strArray = "DW_START_DT,DW_END_DT,DW_ETL_DT".toUpperCase().split(",");
		List<String> OGCloumn = Arrays.asList(strArray);
		return OGCloumn;

	}

	public static void main(String[] args) {

		// updateAttributeValue("D:\\workspace\\Uoo-master\\xml\\M_TLK_ONSITE_SERVICE_1_H.xml",
		// "À­Á´±í");
		System.out.println(DT().get(1));

	}
}
