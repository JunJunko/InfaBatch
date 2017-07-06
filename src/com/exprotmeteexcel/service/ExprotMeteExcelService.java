package com.exprotmeteexcel.service;

import java.util.List;
import java.util.Map;

import com.exprotmeteexcel.bean.MateBean;
import com.exprotmeteexcel.bean.MateColumnsBean;
/**
 * ԭ���ݵ���Service�ӿ�
 * @author admin
 *
 */
public interface ExprotMeteExcelService {
	
	/**
	 * �õ����õ���ԭ���ݵ����͡��ֶΡ����ȵ�
	 * 
	 * @param path ��·��
	 *            
	 * @param mb  ��Ҫ����ԭ����
	 *            �����ݿ���䷢�Ͷ���
	 * @return List
	 *            �������
	 */
	public List<MateColumnsBean> getTableColumn(String path, MateBean mb);
   
	
	/**
	 * �õ����õ����ĸ����ԭ���ݱ�
	 * 
	 * @param path ��·��
	 * @return  list �������
	 */
	public List<Map<String, Object>> getTableColumn(String path);
	//�õ����õ�����ԭ���ݱ�
	
	/**
	 * ������ԭ���ݱ�
	 * 
	 * @param path ��·��
	 * @return Boolean
	 *            ���ɹ� ʧ��
	 */
	public Boolean ExprotExcel(List<MateColumnsBean> meta, String outputprth);
    
	
	/**
	 * �õ����õ�����ԭ���ݱ�
	 * 
	 * @param path ��·��
	 * @return MateBean
	 *            �����ñ�
	 */
	public MateBean getTdMate(String path,String plaform);
	
	//���´�����ɵ���������
	/**
	 * �õ����õ�����ԭ���ݱ�
	 * 
	 * @param path ��·��
	 * @param status �������ɹ���ʧ��
	 * @return update
	 *            ��update���ñ�״̬
	 */
	public Boolean updateTdMate(String path, String status,String businessName) ;
	
	
	
	
	//���´�����ɵ���������
	/**
	 * �õ����õ�����ԭ���ݱ�
	 * 
	 * @param path ��·��
	 * @return Boolean
	 *            ��Boolean
	 */
	//public Boolean ReadExcelExprot(String path);
	
	//���´�����ɵ���������
	/**
	 * ��EXCEL����tableDDl��ת��TRANLIST
	 * 
	 * @param path ��·��
	 * @return map
	 *            ��tableDDl��TRANLIST�ļ���
	 */
	
	public Map<String,Object> ReadExcel(String path);

	/**
	 * �õ����õ�����ԭ���ݱ�
	 * 
	 * @param path ��·��
	 * @param listmc:ת�����б�
	 * @return Boolean
	 *            ��Boolean
	 */
	public Boolean ExcelExprot(String path,List<MateColumnsBean> listmc);
	/**
	 * �õ����õ����ĸ����ԭ���ݱ�����excal
	 * 
	 * @param path
	 *            ��·��
	 * @return Boolean ���Ƿ�ִ�гɹ�
	 */

	public  Boolean ExprotTableMateBean(String path);
}
