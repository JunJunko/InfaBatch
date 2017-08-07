package org;

import java.io.ByteArrayOutputStream;
import java.io.PrintStream;

/*
 * Joiner.java Created on Nov 4, 2005.
 *
 * Copyright 2004 Informatica Corporation. All rights reserved.
 * INFORMATICA PROPRIETARY/CONFIDENTIAL. Use is subject to license terms.
 */

import java.util.ArrayList;

import java.util.List;

import com.informatica.metadata.common.datarecord.Field;
import com.informatica.powercenter.sdk.mapfwk.connection.ConnectionInfo;
import com.informatica.powercenter.sdk.mapfwk.connection.ConnectionProperties;
import com.informatica.powercenter.sdk.mapfwk.connection.ConnectionPropsConstants;
import com.informatica.powercenter.sdk.mapfwk.connection.SourceTargetType;
import com.informatica.powercenter.sdk.mapfwk.core.DSQTransformation;
import com.informatica.powercenter.sdk.mapfwk.core.InputSet;
import com.informatica.powercenter.sdk.mapfwk.core.Mapping;
import com.informatica.powercenter.sdk.mapfwk.core.MappingVariable;
import com.informatica.powercenter.sdk.mapfwk.core.MappingVariableDataTypes;
import com.informatica.powercenter.sdk.mapfwk.core.RowSet;
import com.informatica.powercenter.sdk.mapfwk.core.Session;
import com.informatica.powercenter.sdk.mapfwk.core.SessionPropsConstants;
import com.informatica.powercenter.sdk.mapfwk.core.Source;
import com.informatica.powercenter.sdk.mapfwk.core.Target;
import com.informatica.powercenter.sdk.mapfwk.core.TaskProperties;
import com.informatica.powercenter.sdk.mapfwk.core.TransformField;
import com.informatica.powercenter.sdk.mapfwk.core.TransformHelper;
import com.informatica.powercenter.sdk.mapfwk.core.TransformationProperties;
import com.informatica.powercenter.sdk.mapfwk.core.Workflow;
import com.informatica.powercenter.sdk.mapfwk.exception.InvalidInputException;
import com.informatica.powercenter.sdk.mapfwk.portpropagation.PortPropagationContext;
import com.informatica.powercenter.sdk.mapfwk.portpropagation.PortPropagationContextFactory;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.tools.ExcelUtil;

/**
 * =============================================
 * 
 * @Copyright 2017上海新炬网络技术有限公司 @version：1.0.1
 * @author：Junko
 * @date：2017年7月11日上午11:39:10
 * @Description: 根据Excel配置表生成校验逻辑的XML文件
 *               =============================================
 */
public class Check extends Base implements Parameter {
	protected Target outputTarget;

	protected Source ordersSource;
	protected final String TablePrefix = org.tools.GetProperties.getKeyValue("prefix");
	protected Source orderDetailsSource;
	protected final static List<String> Keyword = org.tools.RePlaceOG.OG();
	protected static ArrayList<ArrayList<String>> TableConf = ExcelUtil
			.readExecl(org.tools.GetProperties.getKeyValue("ExcelPath"));
	protected static Log log = LogFactory.getLog(Check.class);

	// protected String System = Platfrom;

	/**
	 * @version: 1.0.1
	 * @author: Junko
	 * @see org.Base#createSources()
	 * @date: 2017年7月11日上午11:39:57
	 * @Description: 根据Excel配置信息生成一个PWC的Source
	 */
	protected void createSources() {
		ordersSource = this.CheckSouce(TablePrefix + org.tools.GetProperties.getKeyValue("TableNm"),
				org.tools.GetProperties.getKeyValue("TDFolder"), TagDBType,
				org.tools.GetProperties.getKeyValue("TableNm"));
		folder.addSource(ordersSource);

		orderDetailsSource = this.CheckSouce(org.tools.GetProperties.getKeyValue("TableNm"),
				org.tools.GetProperties.getKeyValue("SourceFolder"), DBType,
				org.tools.GetProperties.getKeyValue("TableNm"));
		folder.addSource(orderDetailsSource);

	}

	/**
	 * @version: 1.0.1
	 * @author: Junko
	 * @see org.Base#createTargets()
	 * @date: 2017年7月11日上午11:40:06
	 * @Description: 根据Excel配置信息生成一个PWC的Target
	 */
	protected void createTargets() {
		outputTarget = this.createRelationalTarget(SourceTargetType.Teradata,
				TablePrefix + org.tools.GetProperties.getKeyValue("TableNm").toUpperCase() + "_CK");
	}

	/**
	 * @version: 1.0.1
	 * @author: Junko
	 * @see org.Base#createMappings()
	 * @date: 2017年7月11日上午11:49:10
	 * @Description: 根据Excel配置信息生成一个PWC的Mappings
	 * @throws Exception
	 */
	protected void createMappings() throws Exception {
		mapping = new Mapping(
				"M_CHECK_" + Platfrom + "_" + org.tools.GetProperties.getKeyValue("TableNm").toUpperCase() + "_CK",
				"mapping", "");

		setMapFileName(mapping);
		TransformHelper helper = new TransformHelper(mapping);
		for (int i = 0; i < ordersSource.getFields().size(); i++) {
			String tmp = ordersSource.getFields().get(i).getName();
			if (tmp.length() > 3)
				if (tmp.substring(tmp.length() - 3, tmp.length()).equals("_OG"))
					//
					ordersSource.getField(tmp).setName("IN_" + tmp);
		}

		// Pipeline - 1
		// 导入目标的sourceQualifier
		RowSet TagSQ = (RowSet) helper.sourceQualifier(orderDetailsSource).getRowSets().get(0);

		// Pipeline - 2
		// // 导入源的sourceQualifier

		RowSet SouSQ = (RowSet) helper.sourceQualifier(ordersSource).getRowSets().get(0);

		//

		// 增加MD5
		ArrayList<String> AllPort = new ArrayList<String>();

		for (int i = 0; i < SouSQ.getFields().size(); i++) {
			if (SouSQ.getFields().get(i).getDataType().equals("char")) {

					AllPort.add("rtrim(" + SouSQ.getFields().get(i).getName() + ")");
				
				} else {
					AllPort.add(SouSQ.getFields().get(i).getName());
				
			}

		}
		List<TransformField> transFieldsMD5_S = new ArrayList<TransformField>();
		String expMD5_S = "string(50, 0) MD5ALL = md5("
				+ AllPort.toString().replace("[", "").replace("]", "").replace(",", "||") + ")";
		TransformField outFieldMD5_S = new TransformField(expMD5_S);
		
		transFieldsMD5_S.add(outFieldMD5_S);

		RowSet expRowSetMD5_S = (RowSet) helper
				.expression(SouSQ, transFieldsMD5_S, "EXP_" + org.tools.GetProperties.getKeyValue("TableNm") + "md5_S")
				.getRowSets().get(0);
		
		AllPort.clear();
		for (int i = 0; i < SouSQ.getFields().size(); i++) {
//			if (TableConf.get(i).get(0).equals(org.tools.GetProperties.getKeyValue("TableNm"))) {
				if (TagSQ.getFields().get(i).getDataType().equals("char")) {
					AllPort.add("rtrim(" + TagSQ.getFields().get(i).getName() + ")");
					
				} else {
					AllPort.add(TagSQ.getFields().get(i).getName());
					
//				}
			}
		}
		
		List<TransformField> transFieldsMD5_T = new ArrayList<TransformField>();
		String expMD5_T = "string(50, 0) MD5ALL = md5("
				+ AllPort.toString().replace("[", "").replace("]", "").replace(",", "||") + ")";
		TransformField outFieldMD5_T = new TransformField(expMD5_T);
		
		transFieldsMD5_T.add(outFieldMD5_T);
		

		RowSet expRowSetMD5_T = (RowSet) helper
				.expression(TagSQ, transFieldsMD5_T, "EXP_" + org.tools.GetProperties.getKeyValue("TableNm") + "md5_T")
				.getRowSets().get(0);

		// 将md5之后进来的数据进行排序
		String IDColunmNM = org.tools.GetProperties.getKeyValue("IDColunmNM");

		RowSet TagSort = helper.sorter(expRowSetMD5_T, new String[] { IDColunmNM }, new boolean[] { false },
				"SRT_" + org.tools.GetProperties.getKeyValue("TableNm")).getRowSets().get(0);

		RowSet SouSort = helper
				.sorter(expRowSetMD5_S, new String[] { IDColunmNM }, new boolean[] { false },
						"SRT_" + TablePrefix + org.tools.GetProperties.getKeyValue("TableNm") + "1")
				.getRowSets().get(0);

		InputSet SouInputSet = new InputSet(SouSort);

		// Join Pipeline-1 to Pipeline-2
		List<InputSet> inputSets = new ArrayList<InputSet>();
		inputSets.add(SouInputSet); // collection includes only the detail

		TransformationProperties JoinProperty = new TransformationProperties();
		JoinProperty.setProperty("Join Type", "Full Outer Join");
		JoinProperty.setProperty("Joiner Data Cache Size", "auto");
		JoinProperty.setProperty("Joiner Index Cache Size", "auto");
		// 将SQ连到Join组件
		RowSet joinRowSet = (RowSet) helper.join(inputSets, new InputSet(TagSort), "MD5ALL = IN_MD5ALL", JoinProperty,
				"JNR_" + org.tools.GetProperties.getKeyValue("TableNm")).getRowSets().get(0);

		List<TransformField> transFields = new ArrayList<TransformField>();
		String Column = "";
		for (int i = 0; i < TableConf.size(); i++) {
			List<String> a = TableConf.get(i);
			if (a.get(0).equals(org.tools.GetProperties.getKeyValue("TableNm")) && !a.get(1).equals("ROW_ID")) {

				String sb = "";
				switch (a.get(2).toString().substring(0, a.get(2).toString().indexOf("("))) {
				case "VARCHAR2":
					sb = a.get(2).replace("VARCHAR2", "String").replace(")", ",0)");
					break;
				case "NUMBER":
					sb = a.get(2).replace("NUMBER", "decimal").replace(")", ",0)");
					break;
				case "DATE":
					sb = "date/time(29,9)";
					break;
				case "DATETIME":
					sb = "date/time(29,9)";
					break;
				case "TIME":
					sb = "date/time(29,9)";
					break;
				case "BLOB":
					sb = a.get(2).replace("BLOB", "binary").replace(")", ",0)");
					break;
				case "CHAR":
					sb = a.get(2).replace("CHAR", "String").replace(")", ",0)");
					break;
				case "CLOB":
					sb = a.get(2).replace("CLOB", "binary").replace(")", ",0)");
					break;
				case "LONG":
					sb = a.get(2).replace("LONG", "binary").replace(")", ",0)");
					break;
				case "LONGRAW":
					sb = a.get(2).replace("LONGRAW", "text").replace(")", ",0)");
					break;
				case "NCHAR":
					sb = a.get(2).replace("NCHAR", "String").replace(")", ",0)");
					break;
				case "NCLOB":
					sb = a.get(2).replace("NCLOB", "binary").replace(")", ",0)");
					break;
				case "TIMESTAMP":
					sb = "date/time(29,9)";
					break;
				case "VARCHAR":
					sb = a.get(2).replace("VARCHAR", "String").replace(")", ",0)");
					break;
				default:
					sb = "String(50,0)";
					break;
				}
String tmp = "";
				if (!a.get(5).equals("pri")) {
					tmp = Keyword.contains(a.get(1))? "IN_" + a.get(1)+"_OG": "IN_" + a.get(1);
					if (Column == "") {
//						System.out.println(arg0);
						Column = /* a.get(1) + "," + */ tmp;
					} else {
						Column = Column/* + "," + a.get(1) */ + "," + tmp;
					}
				}

			}
		}
		Column = Column + ", DW_START_DT";
		TransformField totalOrderCost = null;

		RowSet expRowSet = (RowSet) helper
				.expression(joinRowSet, transFields, "EXP_" + org.tools.GetProperties.getKeyValue("TableNm"))
				.getRowSets().get(0);

		String[] strArray = null;
		strArray = Column.split(",");

		PortPropagationContext exclOrderID2 = PortPropagationContextFactory.getContextForExcludeColsFromAll(strArray);

		InputSet joinInputSet2 = new InputSet(expRowSet, exclOrderID2);

		RowSet expRowSet2 = (RowSet) helper.expression(joinInputSet2, totalOrderCost,
				"EXP_" + org.tools.GetProperties.getKeyValue("TableNm") + "1").getRowSets().get(0);

		RowSet filterRS = (RowSet) helper
				.filter(expRowSet2, "IIF(ISNULL(MD5ALL),'0',MD5ALL) != IIF(ISNULL(IN_MD5ALL),'0',IN_MD5ALL)",
						"FIT_" + org.tools.GetProperties.getKeyValue("TableNm"))
				.getRowSets().get(0);

		// write to target
		mapping.writeTarget(new InputSet(filterRS, exclOrderID2), outputTarget);
		//
		// ordersSource.getField("IN_ACOSH_OG").setName("ACOSH_OG");
		for (int i = 0; i < ordersSource.getFields().size(); i++) {
			String tmp = ordersSource.getFields().get(i).getName();
			if (tmp.length() > 3)
				if (tmp.substring(0, 3).equals("IN_"))
					ordersSource.getField(tmp).setName(tmp.substring(3, tmp.length()));
		}
		// SouSQ = (RowSet)
		// helper.sourceQualifier(ordersSource).getRowSets().get(0);

		// 增加参数
		MappingVariable mappingVar = new MappingVariable(MappingVariableDataTypes.STRING, "0",
				"mapping variable example", true, "$$PRVS1D_CUR_DATE", "20", "0", true);
		mapping.addMappingVariable(mappingVar);
		// add mapping to folder
		folder.addMapping(mapping);

	}

	/**
	 * @version: 1.0.1
	 * @author: Junko
	 * @see org.Base#createWorkflow()
	 * @date: 2017年7月11日上午11:49:36
	 * @Description: 根据Excel配置信息生成一个PWC的Workflow
	 * @throws Exception
	 */
	protected void createWorkflow() throws Exception {

		workflow = new Workflow(
				"WF_CHECK_" + Platfrom + "_" + org.tools.GetProperties.getKeyValue("TableNm").toUpperCase(),
				"WF_" + org.tools.GetProperties.getKeyValue("TableNm").toUpperCase() + "_CK",
				"");
		workflow.addSession(session);
		workflow.assignIntegrationService(org.tools.GetProperties.getPubKeyValue("Integration"),
				org.tools.GetProperties.getPubKeyValue("Domain"));

		folder.addWorkFlow(workflow);

	}

	public static void main(String args[]) {
		try {
			Check joinerTrans = new Check();
			if (args.length > 0) {
				if (joinerTrans.validateRunMode(args[0])) {
					org.tools.GetProperties.writeProperties("TableNm", args[1]);
					org.tools.GetProperties.writeProperties("LoadType", args[2]);
					joinerTrans.execute();
				}
			} else {
				joinerTrans.printUsage();
			}
		} catch (Exception e) {
			ByteArrayOutputStream baos = new ByteArrayOutputStream();  
			e.printStackTrace(new PrintStream(baos));  
			String exception = baos.toString();  
			log.error( exception);	
		}

	}

	/**
	 * @version: 1.0.1
	 * @author: Junko
	 * @date: 2017年7月11日上午11:50:10
	 * @Description: 根据属性文件的信息配置源的owner
	 */
	private void setSourceTargetProperties() {

		this.orderDetailsSource.setSessionTransformInstanceProperty("Owner Name",
				org.tools.GetProperties.getKeyValue("Owner"));

	}

	/**
	 * @version: 1.0.1
	 * @author: Junko
	 * @see org.Base#createSession()
	 * @date: 2017年7月11日上午11:50:19
	 * @Description: 根据Excel配置信息生成一个PWC的Session
	 * @throws Exception
	 */
	protected void createSession() throws Exception {
		// TODO Auto-generated method stub
		session = new Session(
				"S_" + Platfrom + "_" + org.tools.GetProperties.getKeyValue("TableNm").toUpperCase() + "_CK",
				"S_" + org.tools.GetProperties.getKeyValue("TableNm").toUpperCase() + "_CK",
				"This is session for Expression DMO Tx");
		session.setMapping(this.mapping);

		// Adding Connection Objects for substitution mask option
		ConnectionInfo info = new ConnectionInfo(SourceTargetType.Oracle);
		ConnectionProperties cprops = info.getConnProps();
		cprops.setProperty(ConnectionPropsConstants.CONNECTIONNAME, "Oracle");
		cprops.setProperty(ConnectionPropsConstants.CONNECTIONNUMBER, "1");

		ConnectionInfo info2 = new ConnectionInfo(SourceTargetType.Oracle);
		ConnectionProperties cprops2 = info2.getConnProps();
		cprops2.setProperty(ConnectionPropsConstants.CONNECTIONNAME, "Oracle");
		cprops2.setProperty(ConnectionPropsConstants.CONNECTIONNUMBER, "2");
		List<ConnectionInfo> cons = new ArrayList<ConnectionInfo>();
		cons.add(info);
		cons.add(info2);
		// session.addConnectionInfosObject(dmo, cons);

		// Overriding source connection in Seesion level
		ConnectionInfo SrcConOra = new ConnectionInfo(SourceTargetType.Oracle);
		SrcConOra.setConnectionVariable(org.tools.GetProperties.getKeyValue("Connection"));
		String SourceTransformation = TablePrefix.length() == 0
				? "SQ_" + org.tools.GetProperties.getKeyValue("TableNm") + 1
				: "SQ_" + org.tools.GetProperties.getKeyValue("TableNm");
		DSQTransformation dsq = (DSQTransformation) mapping.getTransformation(SourceTransformation);

		try {
			session.addConnectionInfoObject(dsq, SrcConOra);
		} catch (InvalidInputException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		ConnectionInfo SrcConTD = new ConnectionInfo(SourceTargetType.Teradata_PT_Connection);
		SrcConTD.setConnectionVariable(TDConnExport);
		DSQTransformation Tdsq = (DSQTransformation) mapping
				.getTransformation("SQ_" + TablePrefix + org.tools.GetProperties.getKeyValue("TableNm"));

		session.addConnectionInfoObject(Tdsq, SrcConTD);
		// session.addConnectionInfoObject(jobSourceObj, newSrcCon);
		session.setTaskInstanceProperty("REUSABLE", "YES");
		// Overriding target connection in Seesion level
		ConnectionInfo newTgtCon = new ConnectionInfo(SourceTargetType.Teradata_PT_Connection);

		ConnectionProperties newTgtConprops = newTgtCon.getConnProps();

		TaskProperties SP = session.getProperties();
		SP.setProperty(SessionPropsConstants.CFG_OVERRIDE_TRACING, "terse");
		SP.setProperty("Parameter Filename", "$PMRootDir/EDWParam/edw.param");
		newTgtConprops.setProperty(ConnectionPropsConstants.TRUNCATE_TABLE, "YES");
		newTgtCon.setConnectionVariable(TDConnUpdate);
		session.addConnectionInfoObject(outputTarget, newTgtCon);
		setSourceTargetProperties();
	}
}
