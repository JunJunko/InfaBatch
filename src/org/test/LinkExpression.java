/*
 * LinkExpression.java Created on Nov 4, 2005.
 *
 * Copyright 2004 Informatica Corporation. All rights reserved.
 * INFORMATICA PROPRIETARY/CONFIDENTIAL. Use is subject to license terms.
 */
package org.test;

import java.util.ArrayList;
import java.util.List;

import org.Base;

import com.informatica.powercenter.sdk.mapfwk.core.Field;
import com.informatica.powercenter.sdk.mapfwk.core.FieldKeyType;
import com.informatica.powercenter.sdk.mapfwk.core.FieldType;
import com.informatica.powercenter.sdk.mapfwk.core.InputSet;
import com.informatica.powercenter.sdk.mapfwk.core.Mapping;
import com.informatica.powercenter.sdk.mapfwk.core.OutputSet;
import com.informatica.powercenter.sdk.mapfwk.core.RowSet;
import com.informatica.powercenter.sdk.mapfwk.core.Session;
import com.informatica.powercenter.sdk.mapfwk.core.Source;
import com.informatica.powercenter.sdk.mapfwk.core.Target;
import com.informatica.powercenter.sdk.mapfwk.core.TransformField;
import com.informatica.powercenter.sdk.mapfwk.core.TransformHelper;
import com.informatica.powercenter.sdk.mapfwk.core.TransformationDataTypes;
import com.informatica.powercenter.sdk.mapfwk.core.Workflow;
import com.informatica.powercenter.sdk.mapfwk.portpropagation.PortLinkContext;
import com.informatica.powercenter.sdk.mapfwk.portpropagation.PortLinkContextFactory;

public class LinkExpression extends Base {
    // /////////////////////////////////////////////////////////////////////////////////////
    // Instance variables
    // /////////////////////////////////////////////////////////////////////////////////////
    protected Source employeeSrc;
    protected Target outputTarget;

    /**
     * Create sources
     */
    protected void createSources() {
        employeeSrc = this.createEmployeeSource();
        folder.addSource( employeeSrc );
    }

    /**
     * Create targets
     */
    protected void createTargets() {
        outputTarget = this.createFlatFileTarget( "Expression_Output" );
    }

    public void createMappings() throws Exception {
        // create a mapping
        mapping = new Mapping( "LinkExpressionMapping", "mapping", "Testing LinkExpression sample" );
        setMapFileName( mapping );
        TransformHelper helper = new TransformHelper( mapping );
        // creating DSQ Transformation
        OutputSet outSet = helper.sourceQualifier( employeeSrc );
        RowSet dsqRS = (RowSet) outSet.getRowSets().get( 0 );
        // create link fields
        List<Field> linkFields = getLinkFields();
        // create the link
        PortLinkContext portLinkContext = PortLinkContextFactory
                .getPortLinkContextByPosition( linkFields );
        InputSet linkInputSet = new InputSet( dsqRS, portLinkContext );
        // create an expression Transformation
        // the fields LastName and FirstName are concataneted to produce a new
        // field fullName
        String expr = "string(80, 0) fullName= firstName1 || lastName1";
        TransformField outField = new TransformField( expr );
        RowSet expRS = (RowSet) helper.expression( linkInputSet, outField, "link_exp_transform" )
                .getRowSets().get( 0 );
        // write to target
        mapping.writeTarget( expRS, outputTarget );
        folder.addMapping( mapping );
    }

    /**
     * This method returns the link fields
     * In this method, we are creating transformation fields, so we will use
     * transformation data types while creating fields and field type will
     * be transform
     * 
     * @return List
     * @see com.informatica.powercenter.sdk.mapfwk.core.TransformationDataTypes
     */
    public List<Field> getLinkFields() {
        List<Field> fields = new ArrayList<Field>();
        Field field1 = new Field( "EmployeeID1", "EmployeeID1", "", TransformationDataTypes.INTEGER,
                "10", "0", FieldKeyType.PRIMARY_KEY, FieldType.TRANSFORM, true );
        fields.add( field1 );
        Field field2 = new Field( "LastName1", "LastName1", "", TransformationDataTypes.STRING, "20",
                "0", FieldKeyType.NOT_A_KEY, FieldType.TRANSFORM, false );
        fields.add( field2 );
        Field field3 = new Field( "FirstName1", "FirstName1", "", TransformationDataTypes.STRING, "10",
                "0", FieldKeyType.NOT_A_KEY, FieldType.TRANSFORM, false );
        fields.add( field3 );
        return fields;
    }

    public static void main( String args[] ) {
        try {
            LinkExpression linkExpressionTrans = new LinkExpression();
            if (args.length > 0) {
                if (linkExpressionTrans.validateRunMode( args[0] )) {
                    linkExpressionTrans.execute();
                }
            } else {
                linkExpressionTrans.printUsage();
            }
        } catch (Exception e) {
            e.printStackTrace();
            System.err.println( "Exception is: " + e.getMessage() );
        }
    }

    /*
     * (non-Javadoc)
     * 
     * @see com.informatica.powercenter.sdk.mapfwk.samples.Base#createSession()
     */
    protected void createSession() throws Exception {
        session = new Session( "Session_For_Expression", "Session_For_Expression",
                "This is session for expression" );
        session.setMapping( this.mapping );
    }

    /*
     * (non-Javadoc)
     * 
     * @see com.informatica.powercenter.sdk.mapfwk.samples.Base#createWorkflow()
     */
    protected void createWorkflow() throws Exception {
        workflow = new Workflow( "Workflow_for_Expression", "Workflow_for_Expression",
                "This workflow for expression" );
        workflow.addSession( session );
        folder.addWorkFlow( workflow );
    }
}
