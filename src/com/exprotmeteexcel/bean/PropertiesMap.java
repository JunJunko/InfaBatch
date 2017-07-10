package com.exprotmeteexcel.bean;

public class PropertiesMap {
	String system;
	String path;
	String DDLSchema;
	String DATASchema;
	
	/*DDLSchema=ODS_DDL
	//DATAµƒÀ˘ Ù≈‰÷√
	DATASchema=ODS_DATA*/
	
	public String getSystem() {
		return system;
	}
	public String getDDLSchema() {
		return DDLSchema;
	}
	public void setDDLSchema(String dDLSchema) {
		DDLSchema = dDLSchema;
	}
	public String getDATASchema() {
		return DATASchema;
	}
	public void setDATASchema(String dATASchema) {
		DATASchema = dATASchema;
	}
	public void setSystem(String system) {
		this.system = system;
	}
	public String getPath() {
		return path;
	}
	public void setPath(String path) {
		this.path = path;
	}

}
