package org.MegerWorklet;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.dom4j.Document;
import org.dom4j.DocumentException;
import org.dom4j.io.SAXReader;
import org.dom4j.Element;

import org.tools.GetProperties;
import org.MegerWorklet.readWorkletConfigInfo;
import org.MegerWorklet.GetSeesionName;

public class CreateWorkltXml extends GetAllXmlList implements getXmlElement {
	// private String filePath;
	Log log = LogFactory.getLog(CreateWorkltXml.class);
	static String sys = new String(GetProperties.getKeyValue("System"));
	readWorkletConfigInfo readWorkletHead = new readWorkletConfigInfo();
	String WorkletHead = readWorkletHead.readWorkletConfigHead();

	public CreateWorkltXml(String type) {
		super(type);
		// TODO Auto-generated constructor stub
	}

	/**
	 * @version: 1.0.1
	 * @author: Junko
	 * @date: 2017年7月24日上午11:30:13
	 * @Description: 省当前时间"MM/dd/yyyy HH:mm:ss"
	 * @return 时间
	 */
	public static String getStringDate() {
		Date currentTime = new Date();
		SimpleDateFormat formatter = new SimpleDateFormat("MM/dd/yyyy HH:mm:ss");
		String dateString = formatter.format(currentTime);
		return dateString;
	}

	/**
	 * @version: 1.0.1
	 * @author: Junko
	 * @date: 2017年7月24日下午1:52:17
	 * @Description: 将合并好的内容写到XML文件里面
	 * @param str
	 */
	public void WriteXml(String str) {
		String outPath = new String();
		
		File Workle = new File("xml\\WorkletXML");
		if(!Workle.exists())
			Workle.mkdirs();
		
		String WorkletNm = new String();
		switch (type) {
		case "init":
			outPath = "xml\\WorkletXML\\ALL_" + sys + "_INITIALIZATION.xml";
			WorkletNm = "ALL_" + sys + "_INITIALIZATION";
			break;
		case "increment":
			outPath = "xml\\WorkletXML\\ALL_" + sys + "_INCREMENT.xml";
			WorkletNm = "ALL_" + sys + "_INCREMENT";
			break;
		case "check":
			outPath = "xml\\WorkletXML\\ALL_" + sys + "_CK.xml";
			WorkletNm = "ALL_" + sys + "_CK";
			break;

		}

		File file = new File(outPath);
		try {
			file.createNewFile();

			FileOutputStream out = new FileOutputStream(file, false); // 如果追加方式用true
			StringBuffer sb = new StringBuffer();

			sb.append(WorkletHead);
			sb.append(str + "\n");

			out.write(sb.toString().getBytes("UTF-8"));// 注意需要转换对应的字符集
			out.close();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			log.error(e.getStackTrace());
		}
		log.info("成功合并Worklet: " + outPath);

	}

	/**
	 * @version: 1.0.1
	 * @author: Junko
	 * @date: 2017年7月24日下午1:52:48
	 * @Description: 合并初始化和check逻辑的XML
	 * @return XML文件的内容
	 */
	public String MegerInitCheckSessionElement() {
		String WorkletNm = "";
		switch (type) {
		case "init":
			WorkletNm = "ALL_" + sys + "_INITIALIZATION";
			break;

		case "check":
			WorkletNm = "ALL_" + sys + "_CK";
			break;
		}
		List<String> FilList = new ArrayList<String>(super.getInitCheckFileName());
		// System.out.println(FilList);
		StringBuffer SessionElementSb = new StringBuffer();
		int i;
		GetSeesionName a = new GetSeesionName();
		for (i = 0; i < FilList.size(); i++) {
			SessionElementSb.append(GetXmlSeesionElement(FilList.get(i)) + "\n");
		}

		SessionElementSb.append("<WORKLET DESCRIPTION =\"\" ISVALID =\"YES\" NAME =\"" + WorkletNm
				+ "\" REUSABLE =\"YES\" VERSIONNUMBER =\"1\">\n"
				+ "<TASK DESCRIPTION =\"\" NAME =\"Start\" REUSABLE =\"NO\" TYPE =\"Start\" VERSIONNUMBER =\"1\"/>\n"
				+ "<TASKINSTANCE DESCRIPTION =\"\" ISENABLED =\"YES\" NAME =\"Start\" REUSABLE =\"NO\" TASKNAME =\"Start\" TASKTYPE =\"Start\"/>\n");

		for (i = 0; i < FilList.size(); i++) {
			SessionElementSb
					.append("<TASKINSTANCE DESCRIPTION =\"\" FAIL_PARENT_IF_INSTANCE_DID_NOT_RUN =\"NO\" FAIL_PARENT_IF_INSTANCE_FAILS =\"NO\" ISENABLED =\"YES\" NAME =\""
							+ a.getSeesionNm(FilList.get(i), "SESSION") + "\" REUSABLE =\"YES\" TASKNAME =\""
							+ a.getSeesionNm(FilList.get(i), "SESSION")
							+ "\" TASKTYPE =\"Session\" TREAT_INPUTLINK_AS_AND =\"YES\"/>" + "\n");
		}

		SessionElementSb.append("<WORKFLOWLINK CONDITION =\"\" FROMTASK =\"Start\" TOTASK =\""
				+ a.getSeesionNm(FilList.get(0), "SESSION") + "\"/>\n");

		for (i = 0; i < FilList.size() - 1; i++) {
			SessionElementSb
					.append("<WORKFLOWLINK CONDITION =\"\" FROMTASK =\"" + a.getSeesionNm(FilList.get(i), "SESSION")
							+ "\" TOTASK =\"" + a.getSeesionNm(FilList.get(i + 1), "SESSION") + "\"/>\n");
		}

		SessionElementSb
				.append("        <WORKFLOWVARIABLE DATATYPE =\"date/time\" DEFAULTVALUE =\"\" DESCRIPTION =\"The time this task started\" ISNULL =\"NO\" ISPERSISTENT =\"NO\" NAME =\"$Start.StartTime\" USERDEFINED =\"NO\"/>"
						+ "        <WORKFLOWVARIABLE DATATYPE =\"date/time\" DEFAULTVALUE =\"\" DESCRIPTION =\"The time this task completed\" ISNULL =\"NO\" ISPERSISTENT =\"NO\" NAME =\"$Start.EndTime\" USERDEFINED =\"NO\"/>"
						+ "        <WORKFLOWVARIABLE DATATYPE =\"integer\" DEFAULTVALUE =\"\" DESCRIPTION =\"Status of this task&apos;s execution\" ISNULL =\"NO\" ISPERSISTENT =\"NO\" NAME =\"$Start.Status\" USERDEFINED =\"NO\"/>"
						+ "        <WORKFLOWVARIABLE DATATYPE =\"integer\" DEFAULTVALUE =\"\" DESCRIPTION =\"Status of the previous task that is not disabled\" ISNULL =\"NO\" ISPERSISTENT =\"NO\" NAME =\"$Start.PrevTaskStatus\" USERDEFINED =\"NO\"/>"
						+ "        <WORKFLOWVARIABLE DATATYPE =\"integer\" DEFAULTVALUE =\"\" DESCRIPTION =\"Error code for this task&apos;s execution\" ISNULL =\"NO\" ISPERSISTENT =\"NO\" NAME =\"$Start.ErrorCode\" USERDEFINED =\"NO\"/>"
						+ "        <WORKFLOWVARIABLE DATATYPE =\"string\" DEFAULTVALUE =\"\" DESCRIPTION =\"Error message for this task&apos;s execution\" ISNULL =\"NO\" ISPERSISTENT =\"NO\" NAME =\"$Start.ErrorMsg\" USERDEFINED =\"NO\"/>");

		for (i = 0; i < FilList.size(); i++) {
			SessionElementSb
					.append(readWorkletHead.readWorkletConfigVariable(a.getSeesionNm(FilList.get(i), "SESSION")));
		}

		SessionElementSb.append("        <ATTRIBUTE NAME =\"Allow Concurrent Run\" VALUE =\"NO\"/>\n"
				+ "    </WORKLET>\n" + "</FOLDER>\n" + "</REPOSITORY>\n" + "</POWERMART>\n");

		log.info("共合并： " + i + "个XML到"+WorkletNm+".xml");
		log.info("XML列表: " + FilList);
		return SessionElementSb.toString();

	}

	/**
	 * @version: 1.0.1
	 * @author: Junko
	 * @date: 2017年7月24日下午1:50:51
	 * @Description: 合并Upsert逻辑XML
	 * @return XML文件的内容
	 */
	public String MegerUpsertSessionElement() {
		List<String> FilList = new ArrayList<String>(super.getUpsertFileName());
		// System.out.println(FilList);
		StringBuffer SessionElementSb = new StringBuffer();
		int i;
		for (i = 0; i < FilList.size(); i++) {
			SessionElementSb.append(GetXmlSeesionElement(FilList.get(i)) + "\n");
		}
		GetSeesionName a = new GetSeesionName();
		for (i = 0; i < FilList.size(); i++) {
			SessionElementSb.append(GetXmlSeesionElement(FilList.get(i)) + "\n");
		}

		SessionElementSb.append("<WORKLET DESCRIPTION =\"\" ISVALID =\"YES\" NAME =\"" + "ALL_" + sys + "_INCREMENT"
				+ "\" REUSABLE =\"YES\" VERSIONNUMBER =\"1\">\n"
				+ "<TASK DESCRIPTION =\"\" NAME =\"Start\" REUSABLE =\"NO\" TYPE =\"Start\" VERSIONNUMBER =\"1\"/>\n"
				+ "<TASKINSTANCE DESCRIPTION =\"\" ISENABLED =\"YES\" NAME =\"Start\" REUSABLE =\"NO\" TASKNAME =\"Start\" TASKTYPE =\"Start\"/>\n");

		for (i = 0; i < FilList.size(); i++) {
			SessionElementSb
					.append("<TASKINSTANCE DESCRIPTION =\"\" FAIL_PARENT_IF_INSTANCE_DID_NOT_RUN =\"NO\" FAIL_PARENT_IF_INSTANCE_FAILS =\"NO\" ISENABLED =\"YES\" NAME =\""
							+ a.getSeesionNm(FilList.get(i), "SESSION") + "\" REUSABLE =\"YES\" TASKNAME =\""
							+ a.getSeesionNm(FilList.get(i), "SESSION")
							+ "\" TASKTYPE =\"Session\" TREAT_INPUTLINK_AS_AND =\"YES\"/>" + "\n");
		}

		SessionElementSb.append("<WORKFLOWLINK CONDITION =\"\" FROMTASK =\"Start\" TOTASK =\""
				+ a.getSeesionNm(FilList.get(0), "SESSION") + "\"/>\n");

		for (i = 0; i < FilList.size() - 1; i++) {
			SessionElementSb
					.append("<WORKFLOWLINK CONDITION =\"\" FROMTASK =\"" + a.getSeesionNm(FilList.get(i), "SESSION")
							+ "\" TOTASK =\"" + a.getSeesionNm(FilList.get(i + 1), "SESSION") + "\"/>\n");
		}

		SessionElementSb
				.append("        <WORKFLOWVARIABLE DATATYPE =\"date/time\" DEFAULTVALUE =\"\" DESCRIPTION =\"The time this task started\" ISNULL =\"NO\" ISPERSISTENT =\"NO\" NAME =\"$Start.StartTime\" USERDEFINED =\"NO\"/>"
						+ "        <WORKFLOWVARIABLE DATATYPE =\"date/time\" DEFAULTVALUE =\"\" DESCRIPTION =\"The time this task completed\" ISNULL =\"NO\" ISPERSISTENT =\"NO\" NAME =\"$Start.EndTime\" USERDEFINED =\"NO\"/>"
						+ "        <WORKFLOWVARIABLE DATATYPE =\"integer\" DEFAULTVALUE =\"\" DESCRIPTION =\"Status of this task&apos;s execution\" ISNULL =\"NO\" ISPERSISTENT =\"NO\" NAME =\"$Start.Status\" USERDEFINED =\"NO\"/>"
						+ "        <WORKFLOWVARIABLE DATATYPE =\"integer\" DEFAULTVALUE =\"\" DESCRIPTION =\"Status of the previous task that is not disabled\" ISNULL =\"NO\" ISPERSISTENT =\"NO\" NAME =\"$Start.PrevTaskStatus\" USERDEFINED =\"NO\"/>"
						+ "        <WORKFLOWVARIABLE DATATYPE =\"integer\" DEFAULTVALUE =\"\" DESCRIPTION =\"Error code for this task&apos;s execution\" ISNULL =\"NO\" ISPERSISTENT =\"NO\" NAME =\"$Start.ErrorCode\" USERDEFINED =\"NO\"/>"
						+ "        <WORKFLOWVARIABLE DATATYPE =\"string\" DEFAULTVALUE =\"\" DESCRIPTION =\"Error message for this task&apos;s execution\" ISNULL =\"NO\" ISPERSISTENT =\"NO\" NAME =\"$Start.ErrorMsg\" USERDEFINED =\"NO\"/>");

		for (i = 0; i < FilList.size(); i++) {
			SessionElementSb
					.append(readWorkletHead.readWorkletConfigVariable(a.getSeesionNm(FilList.get(i), "SESSION")));
		}

		SessionElementSb.append("        <ATTRIBUTE NAME =\"Allow Concurrent Run\" VALUE =\"NO\"/>\n"
				+ "    </WORKLET>\n" + "</FOLDER>\n" + "</REPOSITORY>\n" + "</POWERMART>\n");
		log.info("共合并： " + i + "个XML到"+"ALL_"+sys+"_INCREMENT.xml");
		log.info("XML列表: " + FilList);
		return SessionElementSb.toString();

	}

	/**
	 * @version: 1.0.1
	 * @author: Junko
	 * @see org.MegerWorklet.getXmlElement#GetXmlSeesionElement(java.lang.String)
	 * @date: 2017年7月24日下午1:53:28
	 * @Description: 遍历指定文件夹下面所有xml文件的Session标签
	 * @param filePath
	 * @return
	 */
	@Override
	public String GetXmlSeesionElement(String filePath) {
		// TODO Auto-generated method stub
		String[] T = filePath.split("\\\\");
		List<String> Type = new ArrayList<String>();
		Type.toArray(T);

		SAXReader reader = new SAXReader();
		Document document = null;

		try {
			reader.setEntityResolver(new IgnoreDTDEntityResolver());
			document = (Document) reader.read(new File(filePath));
			document.getNodeTypeName();
		} catch (DocumentException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		Element rootElm = document.getRootElement();
		String testxml = rootElm.asXML();
		String result = testxml.substring(testxml.indexOf("<SESSION"), testxml.lastIndexOf("</SESSION>") + 10);
		//
		return result;
	}

	public static void main(String[] args) {
		// TODO Auto-generated method stub
		
		CreateWorkltXml Wlinit = new CreateWorkltXml("init");
		CreateWorkltXml Wlincrement = new CreateWorkltXml("increment");
		CreateWorkltXml Wlcheck = new CreateWorkltXml("check");
		String initString = Wlinit.MegerInitCheckSessionElement();
		String incrementString = Wlincrement.MegerUpsertSessionElement();
		String checkString = Wlcheck.MegerInitCheckSessionElement();
		Wlinit.WriteXml(initString);
		Wlincrement.WriteXml(incrementString);
		Wlcheck.WriteXml(checkString);
	}

}
