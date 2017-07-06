package com.exprotmeteexcel.service;

import java.util.List;

import com.exprotmeteexcel.bean.MateColumnsBean;
/**
 * ����DDl��Service�ӿ�
 * @author admin
 *
 */
public interface DdlToolService {
	
	/**
	 * �õ����õ���ԭ���ݵ����͡��ֶΡ����ȵ�
	 * 
	 * @param ownTable ����
	 *            
	 * @param cols  ���ֶ���Ϣ
	 *            �����ݿ���䷢�Ͷ���
	 * @return String
	 *            �����DDL
	 */
	public  String getDdlStr(String ownTable, List<MateColumnsBean> cols);
	/**
	 * �õ�ods�����������ݵ���ԭ���ݵ����͡��ֶΡ����ȵ�
	 * 
	 * @param ownTable
	 *            ����
	 * 
	 * @param cols
	 *            ���ֶ���Ϣ �����ݿ���䷢�Ͷ���
	 * @return String ��ods���DDL
	 */
	public String getOdsDdlStr(String ownTable, List<MateColumnsBean> cols);
	
	/**
	 * �õ�CK�����õ���ԭ���ݵ����͡��ֶΡ����ȵ�
	 * 
	 * @param ownTable
	 *            ����
	 * 
	 * @param cols
	 *            ���ֶ���Ϣ �����ݿ���䷢�Ͷ���
	 * @return String �����DDL
	 */
	public String getCkDdlStr(String ownTable, List<MateColumnsBean> cols);
	/**
	 * �õ����õ���ԭ���ݵ����͡��ֶΡ����ȵ�
	 * 
	 * @param path ��excel·��
	 *            
	 * @return Boolean
	 *            ������DDL
	 */
	public  Boolean exportDdl(String path) ;

}
