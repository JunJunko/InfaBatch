package org;



public interface Parameter {
	
	
	//Դ�ļ���
	public String SourceFolder = org.tools.GetProperties.getKeyValue("SourceFolder");
	//Ŀ���ļ���
	public String TagFolder = org.tools.GetProperties.getKeyValue("TDFolder");
	//Դ���ݿ�
	public String DBType = "Oracle";
	//Ŀ�����ݿ�
	public String TagDBType = "TD";
	//ϵͳ����
	public String Platfrom = org.tools.GetProperties.getKeyValue("System");
	//Դ����owner
	public String Owner = org.tools.GetProperties.getKeyValue("Owner");
	//������Ŀ�������
	public String ZipTagTableNm = "O_" + Platfrom + "_" + org.tools.GetProperties.getKeyValue("TableNm").toUpperCase()+"_H";
	//ȫɾȫ��Ŀ�������
	public String InTagTableNm = "O_" + Platfrom + "_" + org.tools.GetProperties.getKeyValue("TableNm").toUpperCase();
//	UpsertĿ�������
	public String UpTagTableNm = "O_" + Platfrom + "_" + org.tools.GetProperties.getKeyValue("TableNm").toUpperCase();
//	Integration Server Name
	public String Integration = org.tools.GetProperties.getKeyValue("Integration");
//	Domain Name
	public String Domain = org.tools.GetProperties.getKeyValue("Domain");

	



}
