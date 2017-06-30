package org;

import java.util.ArrayList;
import java.util.Enumeration;
import java.util.List;
import java.util.Properties;

import com.informatica.powercenter.sdk.mapfwk.connection.ConnectionInfo;
import com.informatica.powercenter.sdk.mapfwk.connection.SourceTargetType;
import com.informatica.powercenter.sdk.mapfwk.core.Command;
import com.informatica.powercenter.sdk.mapfwk.core.EMail;
import com.informatica.powercenter.sdk.mapfwk.core.Field;
import com.informatica.powercenter.sdk.mapfwk.core.FieldKeyType;
import com.informatica.powercenter.sdk.mapfwk.core.FieldType;
import com.informatica.powercenter.sdk.mapfwk.core.InputSet;
import com.informatica.powercenter.sdk.mapfwk.core.Mapping;
import com.informatica.powercenter.sdk.mapfwk.core.NativeDataTypes;
import com.informatica.powercenter.sdk.mapfwk.core.RowSet;
import com.informatica.powercenter.sdk.mapfwk.core.Session;
import com.informatica.powercenter.sdk.mapfwk.core.SessionComponent;
import com.informatica.powercenter.sdk.mapfwk.core.Source;
import com.informatica.powercenter.sdk.mapfwk.core.Target;
import com.informatica.powercenter.sdk.mapfwk.core.Task;
import com.informatica.powercenter.sdk.mapfwk.core.Transformation;
import com.informatica.powercenter.sdk.mapfwk.core.TransformationConstants;
import com.informatica.powercenter.sdk.mapfwk.core.TransformationContext;
import com.informatica.powercenter.sdk.mapfwk.core.Workflow;

public class SessionComponentSample extends Base{

	protected Target target;
	protected Source source;
	protected Transformation dsqTransform;
	protected SessionComponent sessionComponent; 
	
	public SessionComponentSample() {
		target = null;
		source = null;
		dsqTransform = null;
	}
	@Override
	protected void createSources() {
		this.source = this.createOracleJobSource("SessionComponentSampleSource");
		this.folder.addSource(this.source);
	}

	@Override
	protected void createTargets() {
		ConnectionInfo info = getRelationalConnInfo( SourceTargetType.Oracle , "SessionComponentSampleTarget");
		target = new Target( "SCS_JOBS", "SCS_JOBS", "This is JOBS table", "SCS_JOBS", info );
		this.folder.addTarget(this.target);
		
	}

	@Override
	protected void createMappings() throws Exception {
		mapping = new Mapping("SessionComponent_Mapping", "SessionComponent_Mapping", "This is SessionComponent Mapping");
		setMapFileName(mapping);
		
		List<InputSet> inputSets = new ArrayList<InputSet>();
		InputSet tptSource = new InputSet(this.source);
		inputSets.add(tptSource);
		
		TransformationContext tc = new TransformationContext( inputSets );
		dsqTransform = tc.createTransform( TransformationConstants.DSQ, "TPT_DSQ" );
       
		RowSet dsqRS = (RowSet) dsqTransform.apply().getRowSets().get( 0 );
		
        mapping.addTransformation( dsqTransform );      
		mapping.writeTarget(dsqRS, this.target);
		folder.addMapping(mapping);
		
	}

	@Override
	protected void createSession() throws Exception {
		 session = new Session( "session_For_SessionComponent_Mapping", "Session_For_SessionComponent_Mapping",
			"This is session for SessionComponent Mapping" );
		 
		 
		 
		 SessionComponent sessionComponent = new SessionComponent("on_success_mail", false, SessionComponent.Type.Success_Email);
		 Task emailtask = new EMail("on_success_mail","","");
		 sessionComponent.addTask(emailtask);
		 
	 
		 SessionComponent sessionComponent1 = new SessionComponent("Post-session_failure_variable_assignment", false, SessionComponent.Type.Post_session_failure_variable_assignment);
		 Command cmdtask = new Command("postsession_failure_variable_assignment","","");
		 sessionComponent1.addTask(cmdtask);
		 
		 SessionComponent sessionComponent2 = new SessionComponent("presession_variable_assignment", false, SessionComponent.Type.Pre_session_variable_assignment);
		 cmdtask = new Command("presession_variable_assignment","","");
		 sessionComponent2.addTask(cmdtask);
		 
		 SessionComponent sessionComponent3 = new SessionComponent("postsession_success_variable_assignment", false, SessionComponent.Type.Post_session_success_variable_assignment);
		 cmdtask = new Command("postsession_success_variable_assignment","","");
		 sessionComponent3.addTask(cmdtask);
		 
		 session.addSessionComponent(sessionComponent1);
		 session.addSessionComponent(sessionComponent);
		 session.addSessionComponent(sessionComponent2);
		 session.addSessionComponent(sessionComponent3);
		 session.setMapping( this.mapping );
		
	}

	@Override
	protected void createWorkflow() throws Exception {
		workflow = new Workflow( "Workflow_for_SessionComponent_Mapping", "Workflow_for_SessionComponent_Mapping",
		"This workflow for SessionComponent Mapping" );
		workflow.addSession( session );
		workflow.setSuspendOnError(true);
		folder.addWorkFlow( workflow );
	}

	public static void main(String[] args) {
		try {
			SessionComponentSample sessionComp = new SessionComponentSample();
            if (args.length > 0) {
                if (sessionComp.validateRunMode( args[0] )) {
                	sessionComp.execute();
                }
            } else {
            	sessionComp.printUsage();
            }
        } catch (Exception e) {
            e.printStackTrace();
            System.err.println( "Exception is: " + e.getMessage() );
        }
	}
}
