/**
 * ListWorkFlows.java Created on Jan, 2008.
 * Copyright (c) 2008 Informatica Corporation.  All rights reserved.
 * INFORMATICA PROPRIETARY/CONFIDENTIAL. Use is subject to license terms.
 */



import java.io.IOException;
import java.io.InputStream;
import java.util.List;
import java.util.Properties;

import com.informatica.powercenter.sdk.mapfwk.core.Folder;
import com.informatica.powercenter.sdk.mapfwk.core.INameFilter;
import com.informatica.powercenter.sdk.mapfwk.core.Transformation;
import com.informatica.powercenter.sdk.mapfwk.core.Workflow;
import com.informatica.powercenter.sdk.mapfwk.repository.PmrepRepositoryConnectionManager;
import com.informatica.powercenter.sdk.mapfwk.repository.RepoPropsConstants;
import com.informatica.powercenter.sdk.mapfwk.repository.Repository;
import com.informatica.powercenter.sdk.mapfwk.repository.RepositoryConnectionManager;

/**
 * This example is to list all workflow names in a given folder of a repository 
 * @author kbhat
 */

public class TestWork {
	// Instance variables
    protected Repository rep;
    protected String mapFileName;
    protected Transformation rank;
    protected Workflow workflow;

    protected void createRepository() {
        rep = new Repository( "PowerCenter", "PowerCenter", "This repository contains API test samples" );
    }

    /**
     * Initialise the repository configurations.
     */   
    protected void init() throws IOException {
        createRepository();
        Properties properties = new Properties();
    	String filename = "pcconfig.properties";
    	InputStream propStream = getClass().getClassLoader().getResourceAsStream( filename);

    	if ( propStream != null ) {
    		properties.load( propStream );
	        rep.getRepoConnectionInfo().setPcClientInstallPath(properties.getProperty(RepoPropsConstants.PC_CLIENT_INSTALL_PATH));
	        rep.getRepoConnectionInfo().setTargetFolderName(properties.getProperty(RepoPropsConstants.TARGET_FOLDER_NAME));
	        rep.getRepoConnectionInfo().setTargetRepoName(properties.getProperty(RepoPropsConstants.TARGET_REPO_NAME));
	        rep.getRepoConnectionInfo().setRepoServerHost(properties.getProperty(RepoPropsConstants.REPO_SERVER_HOST));
	        rep.getRepoConnectionInfo().setAdminPassword(properties.getProperty(RepoPropsConstants.ADMIN_PASSWORD));
	        rep.getRepoConnectionInfo().setAdminUsername(properties.getProperty(RepoPropsConstants.ADMIN_USERNAME));
	        rep.getRepoConnectionInfo().setRepoServerPort(properties.getProperty(RepoPropsConstants.REPO_SERVER_PORT));
	        rep.getRepoConnectionInfo().setServerPort(properties.getProperty(RepoPropsConstants.SERVER_PORT));
	        rep.getRepoConnectionInfo().setDatabaseType(properties.getProperty(RepoPropsConstants.DATABASETYPE));
	        
	        if(properties.getProperty(RepoPropsConstants.PMREP_CACHE_FOLDER) != null)
	        	rep.getRepoConnectionInfo().setPmrepCacheFolder(properties.getProperty(RepoPropsConstants.PMREP_CACHE_FOLDER));	        	
        }
        else {
            throw new IOException( "pcconfig.properties file not found.");
        }
    }

    /**
     * Initialize the method
     */    
    public void execute() throws Exception {
    	// initialise the repository configurations.
        init();
        RepositoryConnectionManager repmgr = new PmrepRepositoryConnectionManager();
        rep.setRepositoryConnectionManager(repmgr);

        // get the list of folder names which satisfies filter condition
        List<Folder> folders = rep.getFolders(new INameFilter() {
            public boolean accept(String name) {
            	return name.equals("Junko_test");
            }
        });
        
        //folder count - in this case it is always 1        
        int folderSize = folders.size();
        
        for(int i=0 ; i < folderSize; i++){
        	List<Workflow> listOfWorkFlows = ((Folder)folders.get(i)).fetchWorkflowsFromRepository(); //get the list of workflows
        	int listSize = listOfWorkFlows.size();
			System.out.println(" ***** List of workflows ******");
        	for(int j=0; j < listSize; j++){
        		System.out.println((listOfWorkFlows.get(j)).getName());
        		
        	}
        }
    }


    /**
     * Not expecting any arguments
     */
    public static void main(String[] args) {
        try {
        	TestWork repo = new TestWork();
            repo.execute();
        } catch (Exception e) {
            e.printStackTrace();
            System.err.println( "Exception is: " + e.getMessage() );
        }
    }
}