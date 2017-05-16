package org;

import java.util.ArrayList;
import java.util.List;

import org.tools.ExcelUtil;

import com.informatica.powercenter.sdk.mapfwk.connection.ConnectionInfo;
import com.informatica.powercenter.sdk.mapfwk.connection.ConnectionProperties;
import com.informatica.powercenter.sdk.mapfwk.connection.ConnectionPropsConstants;
import com.informatica.powercenter.sdk.mapfwk.connection.SourceTargetType;
import com.informatica.powercenter.sdk.mapfwk.core.DSQTransformation;
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
import com.informatica.powercenter.sdk.mapfwk.core.TransformHelper;
import com.informatica.powercenter.sdk.mapfwk.core.Workflow;
import com.informatica.powercenter.sdk.mapfwk.exception.InvalidInputException;
import com.informatica.powercenter.sdk.mapfwk.exception.InvalidTransformationException;

public class Expression extends Base implements Parameter {

	protected Source employeeSrc;
	protected Target TdTarget;

	protected static ArrayList<ArrayList<String>> TableConf = ExcelUtil
			.readExecl(org.tools.GetProperties.getKeyValue("ExcelPath"));
	protected String SourceFolder = org.tools.GetProperties.getKeyValue("SourceFolder");
	

	public static void main(String[] args) {
		// TODO Auto-generated method stub
		 try {
	            Expression expressionTrans = new Expression();
	            if (args.length > 0) {
	                if (expressionTrans.validateRunMode( args[0] )) {
//	                	ArrayList<String> a = GetTableList();
//	                	org.tools.DelXmlFolder.delAllFile("D:\\workspace\\Uoo\\xml\\");
//	                	for(int i = 0; i < a.size(); i++){
	                		
	                		org.tools.GetProperties.writeProperties("TableNm", args[1]);
//	                		System.out.println(org.tools.GetProperties.getKeyValue("org.tools.GetProperties.getKeyValue("TableNm")"));
	                        expressionTrans.execute();
//	                	}
	                }
	            } else {
	                expressionTrans.printUsage();
	            }
	        } catch (Exception e) {
	            e.printStackTrace();
	            System.err.println( "Exception is: " + e.getMessage() );
	        }
	    	
	    	System.out.println(GetTableList());
	    

	}

	@Override
	public void createSession() {
		// TODO Auto-generated method stub
		session = new Session("S_" + org.tools.GetProperties.getKeyValue("TableNm").toUpperCase(),
				"S_" + org.tools.GetProperties.getKeyValue("TableNm").toUpperCase(),
				"This is session for Expression DMO Tx");
		session.setMapping(this.mapping);

		// Adding Connection Objects for substitution mask option
		session.setTaskInstanceProperty("REUSABLE", "YES");
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

		ConnectionInfo newSrcCon = new ConnectionInfo(SourceTargetType.Oracle);
		newSrcCon.setConnectionVariable(org.tools.GetProperties.getKeyValue("Connection"));
		DSQTransformation dsq = (DSQTransformation) mapping
				.getTransformation("SQ_" + org.tools.GetProperties.getKeyValue("TableNm"));
		try {
			session.addConnectionInfoObject(dsq, newSrcCon);
		} catch (InvalidInputException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		ConnectionInfo newTgtCon = new ConnectionInfo(SourceTargetType.Teradata_PT_Connection);

		ConnectionProperties newTgtConprops = newTgtCon.getConnProps();

		TaskProperties SP = session.getProperties();
		SP.setProperty(SessionPropsConstants.CFG_OVERRIDE_TRACING, "terse");
		SP.setProperty("Parameter Filename", "$PMRootDir/EDWParam/edw.param");
		newTgtConprops.setProperty(ConnectionPropsConstants.TRUNCATE_TABLE, "YES");

		

		newTgtCon.setConnectionVariable("$DBConnection_TD_U");

		try {
			session.addConnectionInfoObject(TdTarget, newTgtCon);
		} catch (InvalidInputException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		setSourceTargetProperties();

	}

	@Override
	protected void createMappings() throws Exception {
		// TODO Auto-generated method stub
		// TODO Auto-generated method stub
		mapping = new Mapping("M_" + Platfrom + "_" + org.tools.GetProperties.getKeyValue("TableNm").toUpperCase()+"_I",
				"mapping", "");
		setMapFileName(mapping);
		TransformHelper helper = new TransformHelper(mapping);
		// creating DSQ Transformation
		OutputSet outSet = null;
		try {
			outSet = helper.sourceQualifier(employeeSrc);
		} catch (InvalidTransformationException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}
		RowSet dsqRS = (RowSet) outSet.getRowSets().get(0);

      String expr = "integer(1,0) DW_OPER_FLAG = 1";
      TransformField outField = new TransformField( expr );
      
      String expr2 = "date/time(10, 0) DW_ETL_DT= to_date($$PRVS1D_CUR_DATE, 'yyyymmdd')";
      TransformField outField2 = new TransformField( expr2 );
      
      String expr3 = "date/time(19, 0) DW_UPD_TM= SESSSTARTTIME";
      TransformField outField3 = new TransformField( expr3 );
		
		
		List<TransformField> transFields = new ArrayList<TransformField>();
		transFields.add(outField);
		transFields.add(outField2);
		transFields.add(outField3);
		RowSet expRS = null;
		try {
			expRS = (RowSet) helper
					.expression(dsqRS, transFields, "EXP_" + org.tools.GetProperties.getKeyValue("TableNm"))
					.getRowSets().get(0);
		} catch (InvalidTransformationException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		// write to target
		mapping.writeTarget(expRS, TdTarget);
		MappingVariable mappingVar = new MappingVariable(MappingVariableDataTypes.STRING, "0",
				"mapping variable example", true, "$$PRVS1D_CUR_DATE", "20", "0", true);
		mapping.addMappingVariable(mappingVar);
		folder.addMapping(mapping);

	}

	public void setSourceTargetProperties() {
		// TODO Auto-generated method stub

		// set Source properties
		this.employeeSrc.setSessionTransformInstanceProperty("Owner Name", Owner);

	}

	@Override
	protected void createSources() {
		// TODO Auto-generated method stub
		String TableNm = new String(org.tools.GetProperties.getKeyValue("TableNm"));
		employeeSrc = this.CreateCrm(TableNm, SourceFolder, DBType);
		folder.addSource(employeeSrc);
	}

	@Override
	protected void createTargets() {
		// TODO Auto-generated method stub
		TdTarget = this.createRelationalTarget(SourceTargetType.Teradata, "O_" + Platfrom + "_" + org.tools.GetProperties.getKeyValue("TableNm").toUpperCase()+"_I");
	}

	@Override
	protected void createWorkflow() throws Exception {
		// TODO Auto-generated method stub
		workflow = new Workflow("WF_" + org.tools.GetProperties.getKeyValue("TableNm").toUpperCase(),
				"WF_" + org.tools.GetProperties.getKeyValue("TableNm").toUpperCase(), "This workflow for expression");
		workflow.addSession(session);
		workflow.assignIntegrationService(Integration, Domain);
		folder.addWorkFlow(workflow);
	}

	public static ArrayList<String> GetTableList() {
		// TODO Auto-generated method stub
		ArrayList<String> TL = new ArrayList<String> ();
	      
        for (int i = 0; i < TableConf.size(); i++){
        	ArrayList<String> a = (ArrayList<String>) TableConf.get(i);
        	if(!TL.contains(a.get(0))){
        		TL.add(a.get(0));
        		
        	}
        }  
        
        return TL;
    }
	

}
