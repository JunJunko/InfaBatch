package org.RepImport;

import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

import com.informatica.powercenter.sdk.mapfwk.repository.PmrepRepositoryConnectionManager;
import com.informatica.powercenter.sdk.mapfwk.repository.RepoPropsConstants;
import com.informatica.powercenter.sdk.mapfwk.repository.Repository;
import com.informatica.powercenter.sdk.mapfwk.repository.RepositoryConnectionManager;


/**
 * This class if for reading a mapplet from repository
 * @author rjain
 *
 */
public class createFolder {
	protected Repository rep;	
	
	RepositoryConnectionManager repmgr;

	public createFolder() throws IOException {
		init();
		rep.setRepositoryConnectionManager(repmgr);
	
	}

	protected void init() throws IOException {
		createRepository();
		Properties properties = new Properties();
    	String filename = "pcconfig.properties";
    	InputStream propStream = getClass().getClassLoader().getResourceAsStream( filename);

    	if ( propStream != null ) {
    		properties.load( propStream );
	        rep.getRepoConnectionInfo().setPcClientInstallPath(properties.getProperty(RepoPropsConstants.PC_CLIENT_INSTALL_PATH));
	       // rep.getRepoConnectionInfo().setTargetFolderName(properties.getProperty(RepoPropsConstants.TARGET_FOLDER_NAME));
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

	protected void createRepository() {
		rep = new Repository("PowerCenter", "PowerCenter",
				"This repository contains API test samples");
	}

synchronized public void setRepositoryConnectionManager(
				RepositoryConnectionManager repMgr) {
			if (repMgr == null)
				this.repmgr = new PmrepRepositoryConnectionManager();
			else {
				this.repmgr = repMgr;
			}
			rep.setRepositoryConnectionManager(repmgr);
		}

	/**
	 * @param args
	 */
	public static void main(String[] args) {
		// TODO Auto-generated method stub
		try {
//			createFolder repo = new createFolder();
//			RepositoryConnectionManager repMgr = new PmrepRepositoryConnectionManager();			
//			repo.setRepositoryConnectionManager(repMgr);
//			repMgr.createFolder("tdm_design5", "test", false);
		
			//repMgr.deleteFolder("tdm_design1");
			createFolder repo = new createFolder();
			RepositoryConnectionManagerEx repMgr = new PmrepRepositoryConnectionManagerEx();			
			repo.setRepositoryConnectionManager(repMgr);
			repMgr.doDeleteObject("source", "tdm_design", "TDM_SOUR.DBA_SEGMENTS");
//			

			
			
		} catch (Exception e) {
			e.printStackTrace();
			System.err.println("Exception is: " + e.getMessage());
		}

	}

}
