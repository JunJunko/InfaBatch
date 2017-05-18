/**
 * ListWorkFlows.java Created on Jan, 2008.
 * Copyright (c) 2008 Informatica Corporation.  All rights reserved.
 * INFORMATICA PROPRIETARY/CONFIDENTIAL. Use is subject to license terms.
 */



import java.io.IOException;
import java.io.InputStream;
import java.util.List;
import java.util.Properties;

import org.Base;

import com.informatica.powercenter.sdk.mapfwk.core.Folder;
import com.informatica.powercenter.sdk.mapfwk.core.INameFilter;
import com.informatica.powercenter.sdk.mapfwk.core.MapFwkOutputContext;
import com.informatica.powercenter.sdk.mapfwk.core.Mapping;
import com.informatica.powercenter.sdk.mapfwk.core.Session;
import com.informatica.powercenter.sdk.mapfwk.core.Source;
import com.informatica.powercenter.sdk.mapfwk.core.Target;
import com.informatica.powercenter.sdk.mapfwk.core.Transformation;
import com.informatica.powercenter.sdk.mapfwk.core.Workflow;
import com.informatica.powercenter.sdk.mapfwk.exception.MapFwkReaderException;
import com.informatica.powercenter.sdk.mapfwk.exception.RepoOperationException;
import com.informatica.powercenter.sdk.mapfwk.repository.PmrepRepositoryConnectionManager;
import com.informatica.powercenter.sdk.mapfwk.repository.RepoPropsConstants;
import com.informatica.powercenter.sdk.mapfwk.repository.Repository;
import com.informatica.powercenter.sdk.mapfwk.repository.RepositoryConnectionManager;

/**
 * This example is to list all workflow names in a given folder of a repository 
 * @author kbhat
 */



public class TestWork2 extends Base{
	protected Source source;
	// Instance variables
	
   protected void getList(){
	   try {
		List<Folder> folders = rep.getFolders(new INameFilter() {
		       public boolean accept(String name) {
		       	return name.equals("Junko_test");
		       }
		   });
		int folderSize = folders.size();
		for(int i=0 ; i < folderSize; i++){
		   List<Source> listOfSource = ((Folder)folders.get(i)).fetchSourcesFromRepository();
		   List<Target> listOfTarget = ((Folder)folders.get(i)).fetchTargetsFromRepository();
		   List<Mapping> listOfMapping = ((Folder)folders.get(i)).fetchMappingsFromRepository();
		   List<Session> listOfSession = ((Folder)folders.get(i)).fetchSessionsFromRepository();
		   List<Workflow> listOfWorkFlows = ((Folder)folders.get(i)).fetchWorkflowsFromRepository();
		}
	} catch (RepoOperationException | MapFwkReaderException e) {
		// TODO Auto-generated catch block
		e.printStackTrace();
	}
	   
   }
	@Override
	protected void createSources() {
		// TODO Auto-generated method stub
		
	}
	@Override
	protected void createTargets() {
		// TODO Auto-generated method stub
		
	}
	@Override
	protected void createMappings() throws Exception {
		// TODO Auto-generated method stub
		
	}
	@Override
	protected void createSession() throws Exception {
		// TODO Auto-generated method stub
		
	}
	@Override
	protected void createWorkflow() throws Exception {
		// TODO Auto-generated method stub
		
	}

}