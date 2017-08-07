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
 * @Copyright 2017�Ϻ��¾����缼�����޹�˾ @version��1.0.1
 * @author��Junko
 * @date��2017��7��11������3:07:12
 * @Description: ����Excel���ñ������������߼���XML�ļ�
 *               =============================================
 */
public class ZipperTable extends Base implements Parameter {
	protected Target outputTarget;

	protected Source ordersSource;

	protected Source orderDetailsSource;

	protected static ArrayList<ArrayList<String>> TableConf = ExcelUtil
			.readExecl(org.tools.GetProperties.getKeyValue("ExcelPath"));
	protected final String TablePrefix = org.tools.GetProperties.getKeyValue("prefix");
	protected static Log log = LogFactory.getLog(ZipperTable.class);

	// protected String System = Platfrom;

	/**
	 * @version: 1.0.1
	 * @author: Junko
	 * @see org.Base#createSources()
	 * @date: 2017��7��11������3:07:23
	 * @Description: ����PWC��Source
	 */
	protected void createSources() {
		ordersSource = this.CreateZipper(TablePrefix + org.tools.GetProperties.getKeyValue("TableNm") + "_H",
				org.tools.GetProperties.getKeyValue("TDFolder"), TagDBType,
				org.tools.GetProperties.getKeyValue("TableNm"));
		folder.addSource(ordersSource);
		orderDetailsSource = this.CreateZipper(org.tools.GetProperties.getKeyValue("TableNm"),
				org.tools.GetProperties.getKeyValue("SourceFolder"), DBType,
				org.tools.GetProperties.getKeyValue("TableNm"));
		folder.addSource(orderDetailsSource);
	}

	/**
	 * @version: 1.0.1
	 * @author: Junko
	 * @see org.Base#createTargets()
	 * @date: 2017��7��11������3:07:37
	 * @Description: ����PWC��Target
	 */
	protected void createTargets() {
		outputTarget = this.createRelationalTarget(SourceTargetType.Teradata,
				TablePrefix + org.tools.GetProperties.getKeyValue("TableNm").toUpperCase() + "_H");
	}

	/**
	 * @version: 1.0.1
	 * @author: Junko
	 * @see org.Base#createMappings()
	 * @date: 2017��7��11������3:07:54
	 * @Description: ����PWC��Mapping
	 * @throws Exception
	 */
	protected void createMappings() throws Exception {

		mapping = new Mapping("H_M_" + org.tools.GetProperties.getKeyValue("TableNm").toUpperCase(), "mapping", "");
		setMapFileName(mapping);
		TransformHelper helper = new TransformHelper(mapping);

		// Pipeline - 1
		// ����Ŀ���sourceQualifier
		RowSet TagSQ = (RowSet) helper.sourceQualifier(orderDetailsSource).getRowSets().get(0);

		// Pipeline - 2
		// // ����Դ��sourceQualifier

		RowSet SouSQ = (RowSet) helper.sourceQualifier(ordersSource).getRowSets().get(0);
		//
		// ����MD5
		ArrayList<String> AllPort = new ArrayList<String>();
		String tmp = "";

		for (int i = 0; i < TagSQ.getFields().size(); i++) {
			
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
		for (int i = 0; i < TagSQ.getFields().size(); i++) {
			// if
			// (TableConf.get(i).get(0).equals(org.tools.GetProperties.getKeyValue("TableNm")))
			// {
			if (TagSQ.getFields().get(i).getDataType().equals("char")) {
				AllPort.add("rtrim(" + TagSQ.getFields().get(i).getName() + ")");

			} else {
				AllPort.add(TagSQ.getFields().get(i).getName());

				// }
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

		// ��md5֮����������ݽ�������
		String IDColunmNM = org.tools.GetProperties.getKeyValue("IDColunmNM");

		RowSet TagSort = helper.sorter(expRowSetMD5_T, new String[] { IDColunmNM }, new boolean[] { false },
				"SRT_" + org.tools.GetProperties.getKeyValue("TableNm")).getRowSets().get(0);

		RowSet SouSort = helper
				.sorter(expRowSetMD5_S, new String[] { IDColunmNM }, new boolean[] { false },
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
		// ��SQ����Join���
		RowSet joinRowSet = (RowSet) helper.join(inputSets, new InputSet(TagSort), "MD5ALL = IN_MD5ALL", JoinProperty,
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
		// String[] toBeStored = Column.toArray(new String[Column.size()]);

		TransformField totalOrderCost = null;
		// new TransformField(
		// "decimal(24,0) TotalOrderCost = OrderCost + Freight");

		String[] strArray = null;
		Column = Column + ",MD5ALL,IN_MD5ALL,falg";
		strArray = Column.split(",");

		PortPropagationContext exclOrderID2 = PortPropagationContextFactory.getContextForExcludeColsFromAll(strArray); // exclude
																														// OrderCost
																														// while
																														// writing
																														// to
																														// target

		// ����router���
		List<TransformGroup> transformGrps = new ArrayList<TransformGroup>();
		TransformGroup transGrp = new TransformGroup("Data_UDs", org.tools.getPrimaryKeyTran2Expresion
				.getPrimayKeyList(SourcePrimaryKeyList, TargetPrimaryKeyList, "Datsa_UDs"));
		transformGrps.add(transGrp);
		transGrp = new TransformGroup("Data_Inserts", org.tools.getPrimaryKeyTran2Expresion
				.getPrimayKeyList(SourcePrimaryKeyList, TargetPrimaryKeyList, "Data_Inserts"));
		transformGrps.add(transGrp);
		OutputSet routerOutputSet = helper.router(joinRowSet, transformGrps,
				"RTR_" + org.tools.GetProperties.getKeyValue("TableNm"));

		// ��router���������������ʽ���

		RowSet outRS = routerOutputSet.getRowSet("Data_UDs");
		RowSet expData_Ins = null;
		RowSet expData_Ups = null;
		ArrayList<String> TagCloumn = new ArrayList<String>();
		ArrayList<String> SouCloumn = new ArrayList<String>();
		if (outRS != null) {

			for (int i = outRS.size() / 2 - 1; i < outRS.size(); i++) {

				TagCloumn.add(outRS.getFields().get(i).getName().toString());

			}
			TagCloumn.add("IN_MD5ALL1");
			TagCloumn.remove(0);
			String Port[] = TagCloumn.toArray(new String[TagCloumn.size()]);
			;

			PortPropagationContext INPort = PortPropagationContextFactory.getContextForExcludeColsFromAll(Port);
			TransformField a = null;

			expData_Ups = (RowSet) helper.expression(new InputSet(outRS, INPort), a,
					"EXP_" + org.tools.GetProperties.getKeyValue("TableNm") + "_Ups").getRowSets().get(0);

		}
		outRS = routerOutputSet.getRowSet("Data_Inserts");

		if (outRS != null) {

			for (int i = 0; i <= outRS.size() / 2; i++) {
				System.out.println(outRS.getFields().get(i).getName().toString());
				SouCloumn.add(outRS.getFields().get(i).getName().toString());

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

		// ΪInsert��Express����DW�ֶ�

		List<TransformField> Exp = new ArrayList<TransformField>();
		String exp1 = "date/time(10, 0) DW_START_DT = SYSDATE";
		String exp2 = "date/time(10, 0) DW_END_DT = to_date('2999-12-31','YYYY-MM-DD')";
		String exp3 = "date/time(10, 0) DW_ETL_DT = to_date($$PRVS1D_CUR_DATE,'yyyymmdd')";
		String exp4 = "date/time(19, 0) DW_UPD_TM = SESSSTARTTIME";
		String exp5 = "integer(10, 0) falg = 0";
		TransformField outField1 = new TransformField(exp1);
		TransformField outField2 = new TransformField(exp2);
		TransformField outField3 = new TransformField(exp3);
		TransformField outField4 = new TransformField(exp4);
		TransformField outField5 = new TransformField(exp5);
		Exp.add(outField1);
		Exp.add(outField2);
		Exp.add(outField3);
		Exp.add(outField4);
		Exp.add(outField5);

		RowSet expData_Ins_Dw = (RowSet) helper
				.expression(expData_Ins, Exp, "EXP_" + org.tools.GetProperties.getKeyValue("TableNm") + "_Ins2")
				.getRowSets().get(0);

		List<TransformField> Exp2 = new ArrayList<TransformField>();
		exp1 = "date/time(10, 0) DW_END_DT = ADD_TO_DATE ( sysdate, 'DD', -1 )";
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

		RowSet SortFlag = (RowSet) helper
				.sorter(UnionSet, "falg", false, "SOR_" + org.tools.GetProperties.getKeyValue("TableNm") + "_2")
				.getRowSets().get(0);

		RowSet filterRS = (RowSet) helper.updateStrategy(SortFlag, "iif(falg=0,DD_INSERT,DD_UPDATE)",
				"UPD_" + org.tools.GetProperties.getKeyValue("TableNm")).getRowSets().get(0);

		// write to target
		mapping.writeTarget(new InputSet(filterRS, exclOrderID2), outputTarget);

		// ���Ӳ���
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
	 * @date: 2017��7��11������3:08:24
	 * @Description: ����PWC��Workflow
	 * @throws Exception
	 */
	protected void createWorkflow() throws Exception {

		workflow = new Workflow("H_WF_" + org.tools.GetProperties.getKeyValue("TableNm").toUpperCase(),
				"H_WF_" + org.tools.GetProperties.getKeyValue("TableNm").toUpperCase(), "");
		workflow.addSession(session);
		workflow.assignIntegrationService(org.tools.GetProperties.getPubKeyValue("Integration"),
				org.tools.GetProperties.getPubKeyValue("Domain"));
		folder.addWorkFlow(workflow);

	}

	public static void main(String args[]) {
		try {
			ZipperTable joinerTrans = new ZipperTable();
			if (args.length > 0) {
				if (joinerTrans.validateRunMode(args[0])) {
					ArrayList<String> a = GetTableList();
					// for (int i = 0; i < a.size(); i++) {
					org.tools.GetProperties.writeProperties("TableNm", args[1]);
					joinerTrans.execute();
					// }
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
	 * @date: 2017��7��11������3:08:46
	 * @Description: ��Excel�����ļ�ȡ������������߼�
	 * @return ��Excel�����ļ�ȡ������������߼���ArrayList<String>����ʽ����
	 */
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
	 * @date: 2017��7��11������3:09:29
	 * @Description: ����Session��������Ϣ
	 */
	private void setSourceTargetProperties() {

		this.orderDetailsSource.setSessionTransformInstanceProperty("Owner Name",
				org.tools.GetProperties.getKeyValue("Owner"));

		this.ordersSource.setSessionTransformInstanceProperty("Source Filter",
				"DW_END_DT=to_date('2999-12-31','YYYY-MM-DD')");

	}

	private void TransformationProperties() {

		//
		// this.ordersSource.setProps(arg0);
		// setSessionTransformInstanceProperty("Source Filter",
		// "DW_END_DT=to_date('2999-12-31','YYYY-MM-DD')");

	}

	/**
	 * @version: 1.0.1
	 * @author: Junko
	 * @see com.informatica.powercenter.sdk.mapfwk.samples.Base#createSession()
	 * @date: 2017��7��11������3:09:37
	 * @Description: ����PWC��Session
	 * @throws Exception
	 */
	protected void createSession() throws Exception {
		// TODO Auto-generated method stub
		session = new Session("H_S_" + org.tools.GetProperties.getKeyValue("TableNm").toUpperCase(),
				"H_S_" + org.tools.GetProperties.getKeyValue("TableNm").toUpperCase(), "");
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

		DSQTransformation dsq = (DSQTransformation) mapping
				.getTransformation("SQ_" + org.tools.GetProperties.getKeyValue("TableNm"));
		session.addConnectionInfoObject(dsq, SrcConOra);

		ConnectionInfo SrcConTD = new ConnectionInfo(SourceTargetType.Teradata_PT_Connection);
		SrcConTD.setConnectionVariable(TDConnExport);
		DSQTransformation Tdsq = (DSQTransformation) mapping
				.getTransformation("SQ_" + TablePrefix + org.tools.GetProperties.getKeyValue("TableNm") + "_H");
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
		TransformationProperties();
	}
}