package org;



public interface Parameter {
	
	
	//源文件夹
	public String SourceFolder = org.tools.GetProperties.getKeyValue("SourceFolder");
	//目标文件夹
	public String TagFolder = org.tools.GetProperties.getKeyValue("TDFolder");
	//源数据库
	public String DBType = "Oracle";
	//目标数据库
	public String TagDBType = "TD";
	//系统名称
	public String Platfrom = org.tools.GetProperties.getKeyValue("System");
	//源所属owner
	public String Owner = org.tools.GetProperties.getKeyValue("Owner");
	//拉链表目标表名称
	public String ZipTagTableNm = "O_" + Platfrom + "_" + org.tools.GetProperties.getKeyValue("TableNm").toUpperCase()+"_H";
	//全删全插目标表名称
	public String InTagTableNm = "O_" + Platfrom + "_" + org.tools.GetProperties.getKeyValue("TableNm").toUpperCase();
//	Upsert目标表名称
	public String UpTagTableNm = "O_" + Platfrom + "_" + org.tools.GetProperties.getKeyValue("TableNm").toUpperCase();
//	Integration Server Name
	public String Integration = org.tools.GetProperties.getKeyValue("Integration");
//	Domain Name
	public String Domain = org.tools.GetProperties.getKeyValue("Domain");

	



}
