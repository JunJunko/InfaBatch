package org.RepImport;

import java.io.File;
import java.io.IOException;
import java.io.Writer;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.apache.log4j.Priority;

import com.informatica.powercenter.sdk.MessageCatalog;
import com.informatica.powercenter.sdk.mapfwk.connection.ConnectionObject;
import com.informatica.powercenter.sdk.mapfwk.core.ABAPProgram;
import com.informatica.powercenter.sdk.mapfwk.core.Folder;
import com.informatica.powercenter.sdk.mapfwk.core.INameFilter;
import com.informatica.powercenter.sdk.mapfwk.exception.ConnectionFailedException;
import com.informatica.powercenter.sdk.mapfwk.exception.MapFwkOutputException;
import com.informatica.powercenter.sdk.mapfwk.exception.MapFwkReaderException;
import com.informatica.powercenter.sdk.mapfwk.exception.RepoConnectionObjectOperationException;
import com.informatica.powercenter.sdk.mapfwk.exception.RepoObjectValidationFailedException;
import com.informatica.powercenter.sdk.mapfwk.exception.RepoOperationException;
import com.informatica.powercenter.sdk.mapfwk.repository.CommandExecutorFactory;
import com.informatica.powercenter.sdk.mapfwk.repository.PmrepRepositoryConnectionManager;
import com.informatica.powercenter.sdk.mapfwk.repository.PmrepRepositoryConnectionManager.PrintWriterTask;
import com.informatica.powercenter.sdk.mapfwk.repository.PmrepRepositoryConnectionManager.TaskObject;
import com.informatica.powercenter.sdk.mapfwk.repository.RepositoryConnectionManager;
import com.informatica.powercenter.sdk.mapfwk.repository.util.PMREP;
import com.informatica.powercenter.sdk.mapfwk.util.JMFMessageCatalog;
import com.informatica.powercenter.sdk.mapfwk.util.Logger;

public abstract class RepositoryConnectionManagerEx extends RepositoryConnectionManager {
	private Logger log = Logger.getLogger(PmrepRepositoryConnectionManager.class);
	private static String POWERMART_DTD = "powrmart.dtd";
	private static String IMPCNTL_DTD = "impcntl.dtd";
	public static String JMF_PROPERTIES = "JMFProperties.properties";
	public static String NO_OF_PMREP_PROCESS = "NO_OF_PMREP_PROCESS";
	public static String USE_BULK_EXPORT = "USE_BULK_EXPORT";
	private String workingDir = ".";
	private PMREPEXP pmrep;
	public final String ILM_APP_HOME = "app.home";
	private static List<TaskObject> list = null;
	private static String OBJECTIMPORT = "ObjectImport -i %s -c %s -s ";
	private static String PMREP_CONNECT_CMD = "Connect -r %s -n %s -x %s -h %s -o %s -s Native";
	private static List<PrintWriterTask> listPrintWriterTask = new ArrayList();
	private static MessageCatalog msgCat = JMFMessageCatalog.getMsgCatalog();

	@Override
	protected void _connect() throws RepoConnectionObjectOperationException {

		if (this.rep == null)
			return;
		String str1 = "";
		this.workingDir = this.rep.getRepoConnectionInfo().getPcClientInstallPath();
		String str2 = this.rep.getRepoConnectionInfo().getRepoServerDomainName();
		if ((str2 == null) || (str2.length() <= 0))
			this.pmrep = new PMREPEXP(this.workingDir, new File(this.workingDir),
					this.rep.getRepoConnectionInfo().getTargetRepoName(),
					this.rep.getRepoConnectionInfo().getRepoServerHost(),
					this.rep.getRepoConnectionInfo().getRepoServerPort(),
					this.rep.getRepoConnectionInfo().getAdminUsername(),
					this.rep.getRepoConnectionInfo().getAdminPassword());
		// else
		// this.pmrep = new PMREPEXP(this.workingDir, new File(this.workingDir),
		// this.rep.getRepoConnectionInfo().getTargetRepoName(), str2,
		// this.rep.getRepoConnectionInfo().getAdminUsername(),
		// this.rep.getRepoConnectionInfo().getAdminPassword());
		CommandExecutorFactory localCommandExecutorFactory = getCommandExecutorFactory();
		if (null != localCommandExecutorFactory)
			this.pmrep.setCommandExecutorFactory(localCommandExecutorFactory);
		String str3 = this.rep.getRepoConnectionInfo().getPmrepCacheFolder();
		if ((str3 == null) || (str3.length() <= 0))
			str3 = new File(this.workingDir, "temp_cache").getAbsolutePath();
		this.pmrep.setRepoConnectionInfo(this.rep.getRepoConnectionInfo());
		this.pmrep.setPmrepCacheFolder(str3);
		// super.setCacheDirectory();
		str1 = this.pmrep.doGetVersion();
		if (str1.equalsIgnoreCase("8.1.1"))
			this.rep.getRepoConnectionInfo().setVersion("177");
		try {
			if (this.pmrep.doConnect() != 0)
				throw new RepoConnectionObjectOperationException(
						msgCat.getMessage("1024_PMREP_REPOCON_CONNECT_REPOSITORY_FAILED_ERROR_message").toString());
		} catch (ConnectionFailedException localConnectionFailedException) {
			throw new RepoConnectionObjectOperationException(msgCat
					.getMessage("1057_CONNECTING_REPOSITORY_ERROR_message", localConnectionFailedException.getMessage())
					.toString());
		}

	}

	@Override
	protected List<Folder> _listFolders(INameFilter paramINameFilter, boolean paramBoolean)
			throws RepoOperationException, MapFwkReaderException {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	protected Map<Folder, List> _listSources(List<Folder> paramList, INameFilter paramINameFilter,
			boolean paramBoolean1, boolean paramBoolean2) throws RepoOperationException, MapFwkReaderException {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	protected Map<Folder, List> _listSources(List<Folder> paramList, INameFilter paramINameFilter,
			boolean paramBoolean1, boolean paramBoolean2, String paramString)
			throws RepoOperationException, MapFwkReaderException {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	protected Map<Folder, List> _listSourceNames(List<Folder> paramList, INameFilter paramINameFilter,
			boolean paramBoolean) throws RepoOperationException, MapFwkReaderException {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	protected Map<Folder, List> _listSourceNames(List<Folder> paramList, INameFilter paramINameFilter,
			boolean paramBoolean, String paramString) throws RepoOperationException, MapFwkReaderException {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	protected Map<Folder, List> _listTargets(List<Folder> paramList, INameFilter paramINameFilter,
			boolean paramBoolean1, boolean paramBoolean2) throws RepoOperationException, MapFwkReaderException {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	protected Map<Folder, List> _listTargetNames(List<Folder> paramList, INameFilter paramINameFilter,
			boolean paramBoolean) throws RepoOperationException, MapFwkReaderException {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	protected Map<Folder, List> _listTransformations(List<Folder> paramList, int paramInt, INameFilter paramINameFilter,
			boolean paramBoolean) throws RepoOperationException, MapFwkReaderException {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	protected Map<Folder, List> _listTransformationNames(List<Folder> paramList, int paramInt,
			INameFilter paramINameFilter, boolean paramBoolean) throws RepoOperationException, MapFwkReaderException {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	protected Map<Folder, List> _listMapplets(List<Folder> paramList, INameFilter paramINameFilter,
			boolean paramBoolean) throws RepoOperationException, MapFwkReaderException {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	protected Map<Folder, List> _listMappletNames(List<Folder> paramList, INameFilter paramINameFilter,
			boolean paramBoolean) throws RepoOperationException, MapFwkReaderException {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	protected Map<Folder, List> _listMappings(List<Folder> paramList, INameFilter paramINameFilter,
			boolean paramBoolean) throws RepoOperationException, MapFwkReaderException {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	protected Map<Folder, List> _listMappingNames(List<Folder> paramList, INameFilter paramINameFilter,
			boolean paramBoolean) throws RepoOperationException, MapFwkReaderException {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	protected Map<Folder, List> _listWorkflows(List<Folder> paramList, INameFilter paramINameFilter,
			boolean paramBoolean) throws RepoOperationException, MapFwkReaderException {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	protected Map<Folder, List> _listWorkflowNames(List<Folder> paramList, INameFilter paramINameFilter,
			boolean paramBoolean) throws RepoOperationException, MapFwkReaderException {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	protected Map<Folder, List> _listSessionNames(List<Folder> paramList, INameFilter paramINameFilter,
			boolean paramBoolean) throws RepoOperationException, MapFwkReaderException {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	protected Map<Folder, List> _listSessions(List<Folder> paramList, INameFilter paramINameFilter,
			boolean paramBoolean) throws RepoOperationException, MapFwkReaderException {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	protected List _listAllConnections(String paramString, INameFilter paramINameFilter)
			throws RepoConnectionObjectOperationException {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	protected List _listConnections(String paramString, INameFilter paramINameFilter)
			throws RepoConnectionObjectOperationException {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	protected void _createConnection(ConnectionObject paramConnectionObject)
			throws RepoConnectionObjectOperationException {
		// TODO Auto-generated method stub

	}

	@Override
	protected void _deleteConnection(String paramString1, String paramString2)
			throws RepoConnectionObjectOperationException {
		// TODO Auto-generated method stub

	}

	@Override
	protected void _updateConnection(ConnectionObject paramConnectionObject)
			throws RepoConnectionObjectOperationException {
		// TODO Auto-generated method stub

	}

	@Override
	protected void _createFolder(String paramString1, String paramString2, boolean paramBoolean)
			throws RepoOperationException {
		// TODO Auto-generated method stub

	}

	@Override
	protected void _deleteFolder(String paramString) throws RepoOperationException {
		// TODO Auto-generated method stub

	}

	@Override
	protected boolean _validate(String paramString1, String paramString2, String paramString3, boolean paramBoolean)
			throws RepoConnectionObjectOperationException, RepoObjectValidationFailedException {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	protected boolean _validate(String paramString1, String paramString2, String paramString3, boolean paramBoolean,
			Writer paramWriter)
			throws RepoConnectionObjectOperationException, RepoObjectValidationFailedException, IOException {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	protected int _importObjectIntoRepository(String paramString1, String paramString2)
			throws RepoConnectionObjectOperationException, MapFwkOutputException {
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	protected int _generateABAPProgram(ABAPProgram paramABAPProgram)
			throws RepoConnectionObjectOperationException, MapFwkOutputException {
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	protected int _installABAPProgram(ABAPProgram paramABAPProgram)
			throws RepoConnectionObjectOperationException, MapFwkOutputException {
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	protected int _installABAPProgram(ABAPProgram paramABAPProgram, String paramString)
			throws RepoConnectionObjectOperationException, MapFwkOutputException {
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	protected int _encryptABAPPassword(ABAPProgram paramABAPProgram)
			throws RepoConnectionObjectOperationException, MapFwkOutputException {
		// TODO Auto-generated method stub
		return 0;
	}

	protected abstract void _doDeleteObject(String object_typ, String folder_nam, String object_name)
			throws RepoOperationException;

	public synchronized void doDeleteObject(String object_typ, String folder_nam, String object_name)
			throws RepoOperationException {
		connect();
		_doDeleteObject(object_typ, folder_nam, object_name);
	}

	public void doImportObject(String paramString1, String paramString2)
			throws RepoConnectionObjectOperationException, MapFwkOutputException {
		// TODO Auto-generated method stub
		connect();
		_importObjectIntoRepository(paramString1, paramString2);
	}

}
