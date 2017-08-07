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

import com.informatica.powercenter.sdk.mapfwk.connection.ConnectionInfo;
import com.informatica.powercenter.sdk.mapfwk.connection.ConnectionProperties;
import com.informatica.powercenter.sdk.mapfwk.connection.ConnectionPropsConstants;
import com.informatica.powercenter.sdk.mapfwk.connection.SourceTargetType;
import com.informatica.powercenter.sdk.mapfwk.core.DSQTransformation;
import com.informatica.powercenter.sdk.mapfwk.core.InputSet;
import com.informatica.powercenter.sdk.mapfwk.core.Mapping;
import com.informatica.powercenter.sdk.mapfwk.core.MappingVariable;
import com.informatica.powercenter.sdk.mapfwk.core.MappingVariableDataTypes;
import com.informatica.powercenter.sdk.mapfwk.core.OutputSet;
import com.informatica.powercenter.sdk.mapfwk.core.RowSet;
import com.informatica.powercenter.sdk.mapfwk.core.Session;
import com.informatica.powercenter.sdk.mapfwk.core.SessionPropsConstants;
import com.informatica.powercenter.sdk.mapfwk.core.Source;
import com.informatica.powercenter.sdk.mapfwk.core.Target;
import com.informatica.powercenter.sdk.mapfwk.core.TaskProperties;
import com.informatica.powercenter.sdk.mapfwk.core.TransformField;
import com.informatica.powercenter.sdk.mapfwk.core.TransformGroup;
import com.informatica.powercenter.sdk.mapfwk.core.TransformHelper;
import com.informatica.powercenter.sdk.mapfwk.core.TransformationProperties;
import com.informatica.powercenter.sdk.mapfwk.core.Workflow;
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
 * @date：2017年7月11日上午11:58:46
 * @Description: 根据Excel配置表生成Upsert逻辑的XML文件
 *               =============================================
 */
public class Upsert extends Base implements Parameter {
	protected Target outputTarget;

	protected Source ordersSource;

	protected Source orderDetailsSource;

	protected static ArrayList<ArrayList<String>> TableConf = ExcelUtil
			.readExecl(org.tools.GetProperties.getKeyValue("ExcelPath"));

	protected final String TablePrefix = org.tools.GetProperties.getKeyValue("prefix");
	protected static Log log = LogFactory.getLog(Upsert.class);

	/**
	 * @version: 1.0.1
	 * @author: Junko
	 * @see org.Base#createSources()
	 * @date: 2017年7月11日上午11:58:58
	 * @Description: 生成PWC的sources
	 */
	protected void createSources() {
		ordersSource = this.CreateUpserAppendSource(TablePrefix + org.tools.GetProperties.getKeyValue("TableNm"),
				org.tools.GetProperties.getKeyValue("TDFolder"), TagDBType,
				org.tools.GetProperties.getKeyValue("TableNm"));
		folder.addSource(ordersSource);
		orderDetailsSource = this.CreateCrm(org.tools.GetProperties.getKeyValue("TableNm"),
				org.tools.GetProperties.getKeyValue("SourceFolder"), DBType);
		folder.addSource(orderDetailsSource);
	}

	/**
	 * @version: 1.0.1
	 * @author: Junko
	 * @see org.Base#createTargets()
	 * @date: 2017年7月11日上午11:59:25
	 * @Description: 生成PWC的targets
	 */
	protected void createTargets() {
		outputTarget = this.createRelationalTarget(SourceTargetType.Teradata,
				TablePrefix + org.tools.GetProperties.getKeyValue("TableNm").toUpperCase());
	}

	/**
	 * @version: 1.0.1
	 * @author: Junko
	 * @see org.Base#createMappings()
	 * @date: 2017年7月11日上午11:59:39
	 * @Description: 生成PWC的Mappings
	 * @throws Exception
	 */
	protected void createMappings() throws Exception {

		mapping = new Mapping("U_M_" + org.tools.GetProperties.getKeyValue("TableNm").toUpperCase(), "mapping", "");
//		List<String> SourcePrimaryKeyList = new ArrayList<String>();
//		List<String> TargetPrimaryKeyList = new ArrayList<String>();


		setMapFileName(mapping);
		TransformHelper helper = new TransformHelper(mapping);

		// Pipeline - 1
		// 导入目标的sourceQualifier
		RowSet TagSQ =	(RowSet) helper.sourceQualifier(orderDetailsSource).getRowSets().get(0);

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
		String[] TagSortKey = SourcePrimaryKeyListNotIN.toArray(new String[SourcePrimaryKeyListNotIN.size()]); 
		System.out.println(TagSortKey);
		
		String[] SouSortKey = TargetPrimaryKeyList.toArray(new String[TargetPrimaryKeyList.size()]);
		
		boolean [] sort = new boolean[TargetPrimaryKeyList.size()];
		for(int i = 0; i < TargetPrimaryKeyList.size(); i++) {    
			sort[i] = false;
		}   
		

//		System.out.println(SouSortKey); 

		RowSet TagSort = helper.sorter(expRowSetMD5_T, TagSortKey, sort,
				"SRT_" + org.tools.GetProperties.getKeyValue("TableNm")).getRowSets().get(0);

		RowSet SouSort = helper
				.sorter(expRowSetMD5_S, SouSortKey, sort,
						"SRT_" + TablePrefix + org.tools.GetProperties.getKeyValue("TableNm") + "_2")
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
		RowSet joinRowSet = (RowSet) helper.join(inputSets, new InputSet(TagSort), org.tools.getPrimaryKeyTran2Expresion
				.getPrimayKeyList(SourcePrimaryKeyList, TargetPrimaryKeyList, "Join"), JoinProperty,
				"JNR_" + org.tools.GetProperties.getKeyValue("TableNm")).getRowSets().get(0);

		// InputSet joinInputSet = new InputSet(joinRowSet);

		// Apply expression to calculate TotalOrderCost

		List<TransformField> transFields = new ArrayList<TransformField>();

		// String expr = "integer(1,0) DW_OPER_FLAG = 1";
		// TransformField outField = new TransformField( expr );
		// transFields.add( outField );

		String Column = "";
		for (int i = 0; i < TableConf.size(); i++) {
			List<String> a = TableConf.get(i);
			if (a.get(0).equals(org.tools.GetProperties.getKeyValue("TableNm"))) {

				String sb = null;
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
				;
				//
				// System.out.println(sb);
				String exp = sb + " " + a.get(1) + "_out" + " = iif(isnull(" + a.get(1) + ")," + "IN_" + a.get(1) + ","
						+ a.get(1) + ")";
				TransformField outField = new TransformField(exp);
				transFields.add(outField);
				if (a.get(1) != null) {
					if (Column == "") {
						Column = a.get(1) + "," + "IN_" + a.get(1);
					} else {
						Column = Column + "," + a.get(1) + "," + "IN_" + a.get(1);
					}
					// System.out.println(Column);
				}

			}
		}

		String exp = "date/time(29,9) DW_START_DT_out = DW_START_DT";
		TransformField outField = new TransformField(exp);
		transFields.add(outField);

		String[] strArray = null;
		Column =  "falg";
		strArray = Column.split(",");

		PortPropagationContext exclOrderID2 = PortPropagationContextFactory.getContextForExcludeColsFromAll(strArray); // exclude
																														// OrderCost
																														// while
																														// writing
																														// to
																														// target

		// 增加router组件
		List<TransformGroup> transformGrps = new ArrayList<TransformGroup>();
		TransformGroup transGrp = new TransformGroup("Data_UDs", org.tools.getPrimaryKeyTran2Expresion
				.getPrimayKeyList(SourcePrimaryKeyList, TargetPrimaryKeyList, "Datsa_UDs"));
		transformGrps.add(transGrp);
		transGrp = new TransformGroup("Data_Inserts", org.tools.getPrimaryKeyTran2Expresion.getPrimayKeyList(SourcePrimaryKeyList, TargetPrimaryKeyList, "Data_Inserts"));
		transformGrps.add(transGrp);
		
		OutputSet routerOutputSet = helper.router(joinRowSet, transformGrps,
				"RTR_" + org.tools.GetProperties.getKeyValue("TableNm"));

		// 将router组件分组输出到表达式组件

		RowSet outRS = routerOutputSet.getRowSet("Data_UDs");
		RowSet expData_Ins = null;
		RowSet expData_Ups = null;
		ArrayList<String> TagCloumn = new ArrayList<String>();
		ArrayList<String> SouCloumn = new ArrayList<String>();
		
		if (outRS != null) {

			for (int i = outRS.size()/2-1; i < outRS.size(); i++) {

//				if (!outRS.getFields().get(i).getName().toString().substring(0, 3).equals("IN_")) {

					TagCloumn.add(outRS.getFields().get(i).getName().toString());
//				}

			}
			TagCloumn.add("IN_MD5ALL1");
			// TagCloumn.remove(0);
			String Port[] = TagCloumn.toArray(new String[TagCloumn.size()]);
			;

			PortPropagationContext INPort = PortPropagationContextFactory.getContextForExcludeColsFromAll(Port);
			TransformField a = null;

			expData_Ups = (RowSet) helper.expression(new InputSet(outRS, INPort), a,
					"EXP_" + org.tools.GetProperties.getKeyValue("TableNm") + "_Ups").getRowSets().get(0);

		}
		outRS = routerOutputSet.getRowSet("Data_Inserts");
		if (outRS != null) {

			for (int i = 0; i < outRS.size()/2; i++) {
//				if (outRS.getFields().get(i).getName().toString().substring(0, 3).equals("IN_")) {

					SouCloumn.add(outRS.getFields().get(i).getName().toString());
//				}
			}
			SouCloumn.add("MD5ALL2");
			SouCloumn.add("DW_START_DT2");
			String Port[] = SouCloumn.toArray(new String[SouCloumn.size()]);
			;

			PortPropagationContext TagPort = PortPropagationContextFactory.getContextForExcludeColsFromAll(Port);
			TransformField a = null;

			expData_Ins = (RowSet) helper.expression(new InputSet(outRS, TagPort), a,
					"EXP_" + org.tools.GetProperties.getKeyValue("TableNm") + "_Ins").getRowSets().get(0);

		}

		// 为Insert的Express增加DW字段

		List<TransformField> Exp = new ArrayList<TransformField>();
		String exp1 = "integer(1, 0) DW_OPER_FLAG = 1";
		String exp2 = "date/time(10, 0) DW_ETL_DT = to_date($$PRVS1D_CUR_DATE,'yyyymmdd')";
		String exp3 = "date/time(19, 0) DW_UPD_TM = SESSSTARTTIME";
		String exp4 = "integer(10, 0) falg = 0";
		TransformField outField1 = new TransformField(exp1);
		TransformField outField2 = new TransformField(exp2);
		TransformField outField3 = new TransformField(exp3);
		TransformField outField4 = new TransformField(exp4);
		Exp.add(outField1);
		Exp.add(outField2);
		Exp.add(outField3);
		Exp.add(outField4);

		RowSet expData_Ins_Dw = (RowSet) helper
				.expression(expData_Ins, Exp, "EXP_" + org.tools.GetProperties.getKeyValue("TableNm") + "_Ins2")
				.getRowSets().get(0);

		List<TransformField> Exp2 = new ArrayList<TransformField>();
		exp1 = "integer(1, 0) DW_OPER_FLAG = 0";
		exp2 = "date/time(10, 0) DW_ETL_DT = to_date($$PRVS1D_CUR_DATE,'yyyymmdd')";
		exp3 = "date/time(19, 0) DW_UPD_TM = SESSSTARTTIME";
		exp4 = "integer(10, 0) falg = 1";
		outField1 = new TransformField(exp1);
		outField2 = new TransformField(exp2);
		outField3 = new TransformField(exp3);
		outField4 = new TransformField(exp4);
		Exp2.add(outField1);
		Exp2.add(outField2);
		Exp2.add(outField3);
		Exp2.add(outField4);

		RowSet expData_Ups_Dw = (RowSet) helper
				.expression(expData_Ups, Exp2, "EXP_" + org.tools.GetProperties.getKeyValue("TableNm") + "_Ups2")
				.getRowSets().get(0);

		List<InputSet> UninputSets = new ArrayList<InputSet>();
		UninputSets.add(new InputSet(expData_Ins_Dw));
		UninputSets.add(new InputSet(expData_Ups_Dw));
		//
		RowSet UnionSet = (RowSet) helper
				.union(UninputSets, expData_Ups_Dw, "UNI_" + org.tools.GetProperties.getKeyValue("TableNm"))
				.getRowSets().get(0);

//		RowSet SortFlag = (RowSet) helper
//				.sorter(UnionSet, "falg", false, "SOR_" + org.tools.GetProperties.getKeyValue("TableNm") + "_2")
//				.getRowSets().get(0);

		RowSet filterRS = (RowSet) helper.updateStrategy(UnionSet, "DD_UPDATE",
				"UPD_" + org.tools.GetProperties.getKeyValue("TableNm")).getRowSets().get(0);

		// write to target
		mapping.writeTarget(new InputSet(filterRS, exclOrderID2), outputTarget);

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
	 * @date: 2017年7月11日下午12:00:04
	 * @Description: 生成PWC的workflow
	 * @throws Exception
	 */
	protected void createWorkflow() throws Exception {

		workflow = new Workflow("U_WF_" + org.tools.GetProperties.getKeyValue("TableNm").toUpperCase(),
				"U_WF_" + org.tools.GetProperties.getKeyValue("TableNm").toUpperCase(), "");
		workflow.addSession(session);
		workflow.assignIntegrationService(org.tools.GetProperties.getPubKeyValue("Integration"),
				org.tools.GetProperties.getPubKeyValue("Domain"));
		folder.addWorkFlow(workflow);

	}

	public static void main(String args[]) {
		try {
			Upsert joinerTrans = new Upsert();
			if (args.length > 0) {
				if (joinerTrans.validateRunMode(args[0])) {
					org.tools.GetProperties.writeProperties("TableNm", args[1]);
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

	public static ArrayList<String> GetTableList() {
		ArrayList<String> TL = new ArrayList<String>();

		for (int i = 0; i < TableConf.size(); i++) {
			ArrayList<String> a = (ArrayList<String>) TableConf.get(i);
			if (!TL.contains(a.get(0))) {
				TL.add(a.get(0));

			}
		}

		return TL;
	}

	/**
	 * @version: 1.0.1
	 * @author: Junko
	 * @date: 2017年7月11日下午12:00:20
	 * @Description: 配置Session的属性信息
	 */
	private void setSourceTargetProperties() {

		this.orderDetailsSource.setSessionTransformInstanceProperty("Owner Name",
				org.tools.GetProperties.getKeyValue("Owner"));

		this.ordersSource.setSessionTransformInstanceProperty("Source Filter",
				"DW_END_DT=to_date('2999-12-31','YYYY-MM-DD')");

	}

	/**
	 * @version: 1.0.1
	 * @author: Junko
	 * @see org.Base#createSession()
	 * @date: 2017年7月11日下午12:00:29
	 * @Description: 生成PWC的Session
	 * @throws Exception
	 * @see com.informatica.powercenter.sdk.mapfwk.samples.Base#createSession()
	 */
	protected void createSession() throws Exception {
		// TODO Auto-generated method stub
		session = new Session("U_S_" + org.tools.GetProperties.getKeyValue("TableNm").toUpperCase(),
				"U_S_" + org.tools.GetProperties.getKeyValue("TableNm").toUpperCase(), "");
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
		String SqInstanNM = TablePrefix.length() == 0 ? "SQ_" + org.tools.GetProperties.getKeyValue("TableNm") + 1
				: "SQ_" + org.tools.GetProperties.getKeyValue("TableNm");

		DSQTransformation dsq = (DSQTransformation) mapping.getTransformation(SqInstanNM);
		session.addConnectionInfoObject(dsq, SrcConOra);

		ConnectionInfo SrcConTD = new ConnectionInfo(SourceTargetType.Teradata_PT_Connection);
		SrcConTD.setConnectionVariable(TDConnExport);
		DSQTransformation Tdsq = (DSQTransformation) mapping
				.getTransformation("SQ_" + TablePrefix + org.tools.GetProperties.getKeyValue("TableNm"));
		session.addConnectionInfoObject(Tdsq, SrcConTD);
		// session.addConnectionInfoObject(jobSourceObj, newSrcCon);
		session.setTaskInstanceProperty("REUSABLE", "YES");
		// Overriding target connection in Seesion level
		ConnectionInfo newTgtCon = new ConnectionInfo(SourceTargetType.Teradata_PT_Connection);

		// ConnectionProperties newTgtConprops = newTgtCon.getConnProps();

		TaskProperties SP = session.getProperties();

		SP.setProperty("Parameter Filename", "$PMRootDir/EDWParam/edw.param");
		SP.setProperty("Treat source rows as", "Data driven");
		SP.setProperty(SessionPropsConstants.CFG_OVERRIDE_TRACING, "terse");
		newTgtCon.setConnectionVariable(TDConnUpdate);
		// ConnectionProperties newTgtConprops = newTgtCon.getConnProps();
		// newTgtConprops.setProperty( ConnectionPropsConstants.CONNECTIONNAME,
		// "$DBConnection_TD");

		session.addConnectionInfoObject(outputTarget, newTgtCon);
		// Setting session level property.
		// session.addSessionTransformInstanceProperties(dmo, props);
		setSourceTargetProperties();
	}

}