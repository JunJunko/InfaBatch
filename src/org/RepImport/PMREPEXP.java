package org.RepImport;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

import org.apache.log4j.Priority;

import com.informatica.powercenter.sdk.MessageCatalog;
import com.informatica.powercenter.sdk.mapfwk.exception.MapFwkOutputException;
import com.informatica.powercenter.sdk.mapfwk.exception.RepoConnectionObjectOperationException;
import com.informatica.powercenter.sdk.mapfwk.exception.RepoOperationException;
import com.informatica.powercenter.sdk.mapfwk.repository.util.PMREP;
import com.informatica.powercenter.sdk.mapfwk.util.JMFMessageCatalog;
import com.informatica.powercenter.sdk.mapfwk.util.Logger;

import com.informatica.powercenter.sdk.mapfwk.repository.RepoPropsConstants;
import com.informatica.powercenter.sdk.mapfwk.repository.Repository;
import com.informatica.powercenter.sdk.mapfwk.repository.RepositoryConnectionManager;

public class PMREPEXP extends PMREP {
	
protected Repository rep;	
	
	RepositoryConnectionManager repmgr;


		

	private Logger log = Logger.getLogger(PMREP.class);
	private static MessageCatalog msgCat = JMFMessageCatalog.getMsgCatalog();

	public PMREPEXP(String paramString1, File paramFile, String paramString2, String paramString3, String paramString4,
			String paramString5, String paramString6) {
		super(paramString1, paramFile, paramString2, paramString3, paramString4, paramString5, paramString6);
	}

	public PMREPEXP(String paramString1, File paramFile, String paramString2, String paramString3, String paramString4,
			String paramString5) {
		super(paramString1, paramFile, paramString2, paramString3, paramString4, paramString5);
	}
	
	protected void init() throws IOException {
//		createRepository();
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

	public int doDeleteObject(String object_typ, String folder_nam, String object_name)
			throws RepoConnectionObjectOperationException {
		
		int i = 0;
		try {
			reset();
			addParameter("deleteobject");
			addParameter("o", object_typ);
			addParameter("f", folder_nam);
			addParameter("n", object_name);
			i = run();
		} catch (Exception localException) {
			this.log.logMessage(msgCat
					.getMessage("1020_PMREP_DELETE_CONNECTION_ERROR_message", localException.toString()).toString(),
					Priority.DEBUG);
			throw new RepoConnectionObjectOperationException(msgCat
					.getMessage("1020_PMREP_DELETE_CONNECTION_ERROR_message", localException.toString()).toString());
		}
		return i;
	}

	public  int doImportObject(String Location, String CtrlFile)
			throws RepoConnectionObjectOperationException {

//		rep.setRepositoryConnectionManager(repmgr);
		int i = 0;
		try {
			reset();
			addParameter("objectImport");
			addParameter("i", Location);
			addParameter("c", CtrlFile);
			i = run();
		} catch (Exception localException) {
			this.log.logMessage(msgCat
					.getMessage("1020_PMREP_IMPORT_CONNECTION_ERROR_message", localException.toString()).toString(),
					Priority.DEBUG);
			throw new RepoConnectionObjectOperationException(msgCat
					.getMessage("1020_PMREP_IMPORT_CONNECTION_ERROR_message", localException.toString()).toString());
		}
		return i;
	}

	public static void main(String args[]) throws MapFwkOutputException, IOException {
		RepositoryConnectionManagerEx repMgr = new PmrepRepositoryConnectionManagerEx();			
//		PMREPEXP repMgr = new PMREPEXP(DELIMITER,   null, "dev_store_edw", "Dev_Domain_etldstsvr01", "NC_ZJK", "499099784");
		
		RepoInit repo = new RepoInit();
		repo.setRepositoryConnectionManager(repMgr);
		
		try {
			repMgr.doImportObject("xml\\CheckXml\\M_CHECK_KMS_KMS_ASK_CATEGORY_CK.xml", "CtrlFile\\check.xml");
//			repMgr._doDeleteObject("source", "a", "a");

		} catch (RepoConnectionObjectOperationException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (RepoOperationException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

}
