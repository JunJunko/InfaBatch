package org;

/*
 * Joiner.java Created on Nov 4, 2005.
 *
 * Copyright 2004 Informatica Corporation. All rights reserved.
 * INFORMATICA PROPRIETARY/CONFIDENTIAL. Use is subject to license terms.
 */

import java.util.ArrayList;

import java.util.List;

import org.tools.ExcelUtil;

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
import com.informatica.powercenter.sdk.mapfwk.portpropagation.PortPropagationContext;
import com.informatica.powercenter.sdk.mapfwk.portpropagation.PortPropagationContextFactory;

/**
 * @author Junko
 *         <p>
 *         Description: ����Excel���ñ������������У����߼���XML�ļ�
 *
 */
public class ZipCheck extends Base implements Parameter {
	protected Target outputTarget;

	protected Source ordersSource;

	protected Source orderDetailsSource;

	protected static ArrayList<ArrayList<String>> TableConf = ExcelUtil
			.readExecl(org.tools.GetProperties.getKeyValue("ExcelPath"));

	// protected String System = Platfrom;

	/**
	 * @author Junko
	 * <p> describe: ����Excel������Ϣ����һ��PWC��Source
	 */
	protected void createSources() {
		ordersSource = this.CheckSouce("O_" + Platfrom + "_" + org.tools.GetProperties.getKeyValue("TableNm")+"_H" ,
				org.tools.GetProperties.getKeyValue("TDFolder"), TagDBType);
		
		folder.addSource(ordersSource);

		orderDetailsSource = this.CheckSouce(org.tools.GetProperties.getKeyValue("TableNm"),
				org.tools.GetProperties.getKeyValue("SourceFolder"), DBType);
		folder.addSource(orderDetailsSource);
	}

	/**
	 * @author Junko
	 * <p> describe: ����Excel������Ϣ����һ��PWC��Target
	 */
	protected void createTargets() {
		outputTarget = this.createRelationalTarget(SourceTargetType.Teradata,
				"O_" + Platfrom + "_" + org.tools.GetProperties.getKeyValue("TableNm").toUpperCase() + "_H_CK");
	}

	protected void createMappings() throws Exception {
		mapping = new Mapping(
				"M_CHECK_" + Platfrom + "_" + org.tools.GetProperties.getKeyValue("TableNm").toUpperCase() + "_CK",
				"mapping", "");

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

		for (int i = 0; i < TableConf.size(); i++) {
			if (TableConf.get(i).get(0).equals(org.tools.GetProperties.getKeyValue("TableNm"))) {
				if (TableConf.get(i).get(2).substring(0, TableConf.get(i).get(2).toString().indexOf("(")).toUpperCase()
						.equals("CHAR")) {
					AllPort.add("rtrim(" + TableConf.get(i).get(1) + ")");
				} else {
					AllPort.add(TableConf.get(i).get(1));
				}
			}

		}
		List<TransformField> transFieldsMD5 = new ArrayList<TransformField>();
		String expMD5 = "string(50, 0) MD5ALL = md5("
				+ AllPort.toString().replace("[", "").replace("]", "").replace(",", "||") + ")";
		TransformField outFieldMD5 = new TransformField(expMD5);
		transFieldsMD5.add(outFieldMD5);

		RowSet expRowSetMD5_S = (RowSet) helper
				.expression(SouSQ, transFieldsMD5, "EXP_" + org.tools.GetProperties.getKeyValue("TableNm") + "md5_S")
				.getRowSets().get(0);

		RowSet expRowSetMD5_T = (RowSet) helper
				.expression(TagSQ, transFieldsMD5, "EXP_" + org.tools.GetProperties.getKeyValue("TableNm") + "md5_T")
				.getRowSets().get(0);

		// ��md5֮����������ݽ�������
		String IDColunmNM = org.tools.GetProperties.getKeyValue("IDColunmNM");

		RowSet TagSort = helper.sorter(expRowSetMD5_T, new String[] { IDColunmNM }, new boolean[] { false },
				"SRT_" + org.tools.GetProperties.getKeyValue("TableNm")).getRowSets().get(0);

		RowSet SouSort = helper
				.sorter(expRowSetMD5_S, new String[] { IDColunmNM }, new boolean[] { false },
						"SRT_" + "O_" + Platfrom + "_" + org.tools.GetProperties.getKeyValue("TableNm")+"_H")
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

				if (a.get(1) != null && !a.get(1).equals("ROW_ID")) {
					if (Column == "") {
						Column = /* a.get(1) + "," + */ "IN_" + a.get(1);
					} else {
						Column = Column/* + "," + a.get(1) */ + "," + "IN_" + a.get(1);
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

		PortPropagationContext exclOrderID2 = PortPropagationContextFactory.getContextForExcludeColsFromAll(strArray); // exclude
																														// OrderCost
																														// while
																														// writing
																														// to
																														// target

		InputSet joinInputSet2 = new InputSet(expRowSet, exclOrderID2);

		RowSet expRowSet2 = (RowSet) helper.expression(joinInputSet2, totalOrderCost,
				"EXP_" + org.tools.GetProperties.getKeyValue("TableNm") + "1").getRowSets().get(0);

		RowSet filterRS = (RowSet) helper
				.filter(expRowSet2, "IIF(ISNULL(MD5ALL),'0',MD5ALL) != IIF(ISNULL(IN_MD5ALL),'0',IN_MD5ALL)",
						"FIT_" + org.tools.GetProperties.getKeyValue("TableNm"))
				.getRowSets().get(0);

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
	 * @author Junko
	 * <p> describe: ����Excel������Ϣ����һ��PWC��Workflow
	 */
	protected void createWorkflow() throws Exception {

		workflow = new Workflow(
				"WF_CHECK_" + Platfrom + "_" + org.tools.GetProperties.getKeyValue("TableNm").toUpperCase(),
				"WF_" + org.tools.GetProperties.getKeyValue("TableNm").toUpperCase() + "_CK", "");
		workflow.addSession(session);
		workflow.assignIntegrationService(org.tools.GetProperties.getKeyValue("Integration"),
				org.tools.GetProperties.getKeyValue("Domain"));

		folder.addWorkFlow(workflow);

	}

	public static void main(String args[]) {
		try {
			ZipCheck joinerTrans = new ZipCheck();
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
			e.printStackTrace();
			System.err.println("Exception is: " + e.getMessage());
		}

	}

	/**
	 * @author Junko
	 * <p> describe: ���������ļ�����Ϣ����Դ��owner
	 */
	private void setSourceTargetProperties() {

		this.orderDetailsSource.setSessionTransformInstanceProperty("Owner Name",
				org.tools.GetProperties.getKeyValue("Owner"));

	}

	/**
	 * @author Junko
	 * <p> describe: ����Excel������Ϣ����һ��PWC��Session
	 */
	protected void createSession() throws Exception {
		// TODO Auto-generated method stub
		session = new Session(
				"S_" + Platfrom + "_" + org.tools.GetProperties.getKeyValue("TableNm").toUpperCase() + "_CK",
				"S_" + org.tools.GetProperties.getKeyValue("TableNm").toUpperCase() + "_CK",
				"");
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
		DSQTransformation Tdsq = (DSQTransformation) mapping.getTransformation(
				"SQ_" + "O_" + Platfrom + "_" + org.tools.GetProperties.getKeyValue("TableNm")+"_H");

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
