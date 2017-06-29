package com.exprotmeteexcel.utl;

import java.util.List;
import java.util.Properties;

import com.exprotmeteexcel.bean.MateColumnsBean;

public class DdlTool {
	public static String getDdlStr(String ownTable, List<MateColumnsBean> cols) {
		StringBuffer sb = new StringBuffer();
		String PRIMARY = "";
		String TableRemark = "";
		sb.append("--------------------------CREATE TABLE " + ownTable + "-------------------------------------/n");
		sb.append("DROP TABLE " + ownTable + " FEEDBACK;/n");
		sb.append("CREATE MULTISET TABLE " + ownTable + " /n (");
		for (int i = 0; i < cols.size(); i++) {
			if (Utl.isEmpty(cols.get(i).getTableRemark())) {
				TableRemark = cols.get(i).getTableRemark();
			}
			String ColumnName = cols.get(i).getTranColumnName();
			String ColumnDataType = cols.get(i).getTranColumnDataType();
			String ColumnRemark = cols.get(i).getColumnRemark();
			String IsNull = cols.get(i).getIsNull();
			String colTitle = Utl.isEmpty(ColumnRemark) ? "TITLE '" + ColumnRemark + "'" : "";
			String UNICODE = "VARCHAR".equals(ColumnDataType.toUpperCase()) ? "CHARACTER SET UNICODE" : "";
			if (!Utl.isEmpty(cols.get(i).getPrimaryKey())) {
				PRIMARY = "pri".equals(cols.get(i).getPrimaryKey().toString())
						? cols.get(i).getPrimaryKey().toString() + "," : "";
			}
			if (i == 0) {
				sb.append(ColumnName + " " + ColumnDataType + " " + UNICODE + " " + colTitle + " " + IsNull);
			} else {
				sb.append(",/n " + ColumnName + " " + ColumnDataType + " " + UNICODE + " " + colTitle + " " + IsNull
						+ "  /n");
			}

		}
		sb.append(")/n ");

		if (!Utl.isEmpty(PRIMARY)) {

			sb.append("PRIMARY INDEX /n(" + PRIMARY.substring(0, PRIMARY.length() - 1) + "/n)");
		}

		if (!Utl.isEmpty(TableRemark)) {
			sb.append("COMMENT ON TABLE "+ownTable+" IS '"+TableRemark+"';");
		}
		return sb.toString();

	}
}
