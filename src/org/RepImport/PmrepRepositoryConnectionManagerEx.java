package org.RepImport;

import com.informatica.powercenter.sdk.mapfwk.exception.MapFwkOutputException;
import com.informatica.powercenter.sdk.mapfwk.exception.RepoConnectionObjectOperationException;
import com.informatica.powercenter.sdk.mapfwk.exception.RepoOperationException;

public class PmrepRepositoryConnectionManagerEx extends RepositoryConnectionManagerEx {

	@Override
	protected void _doDeleteObject(String object_typ, String folder_nam, String object_name)
			throws RepoOperationException {
		// TODO Auto-generated method stub

	}
	
	@Override
	protected int _importObjectIntoRepository(String paramString1, String paramString2)
			throws RepoConnectionObjectOperationException, MapFwkOutputException {
		// TODO Auto-generated method stub
		return 0;
	}

}
