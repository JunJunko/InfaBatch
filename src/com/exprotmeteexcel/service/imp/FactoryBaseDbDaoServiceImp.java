package com.exprotmeteexcel.service.imp;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import com.exprotmeteexcel.dao.BaseDbDao;
import com.exprotmeteexcel.dao.impl.BaseDbDaoI;
import com.exprotmeteexcel.dao.impl.MysqlDbDaoI;
import com.exprotmeteexcel.dao.impl.OracleDbDaoI;
import com.exprotmeteexcel.dao.impl.SqlServerDbDaoI;
import com.exprotmeteexcel.dao.impl.TeradataDbDaoI;
import com.exprotmeteexcel.utl.Getjdbcconfig;
import com.exprotmeteexcel.utl.Utl;

/**
 * JDBC操作工厂类
 * 
 * @author admin
 *
 */

public class FactoryBaseDbDaoServiceImp {
	private static final Log log = LogFactory.getLog(FactoryBaseDbDaoServiceImp.class);

	/**
	 * 获得jdbc的DAO类
	 * 
	 * @param path
	 *            配置路径
	 * @return db ：
	 */
	public static BaseDbDaoI getBaseDbDaoI(String path) {

		Getjdbcconfig dbcof = new Getjdbcconfig(path);
		BaseDbDaoI db = null;
		if (!Utl.isEmpty(dbcof.getDbtype())) {

			if ("oracle".equals(dbcof.getDbtype().toLowerCase())) {

				db = new OracleDbDaoI(dbcof.getDatabasename(), dbcof.getIp(), dbcof.getPort(), dbcof.getUsername(),
						dbcof.getPassword());
				db.setDb_type(dbcof.getDbtype());
				return db;

			} else if ("mysql".equals(dbcof.getDbtype().toLowerCase())) {

				db = new MysqlDbDaoI(dbcof.getDatabasename(), dbcof.getIp(), dbcof.getPort(), dbcof.getUsername(),
						dbcof.getPassword());
				db.setDb_type(dbcof.getDbtype());
				return db;
			} else if ("mssql".equals(dbcof.getDbtype().toLowerCase())) {

				db = new SqlServerDbDaoI(dbcof.getDatabasename(), dbcof.getIp(), dbcof.getPort(), dbcof.getUsername(),
						dbcof.getPassword());
				db.setDb_type(dbcof.getDbtype());
				return db;

			} else if ("teradata".equals(dbcof.getDbtype().toLowerCase())) {

				db = new TeradataDbDaoI(dbcof.getDatabasename(), dbcof.getIp(), dbcof.getPort(), dbcof.getUsername(),
						dbcof.getPassword());
				db.setDb_type(dbcof.getDbtype());
				return db;

			}
		}
		return db;
	}

	/**
	 * 获得jdbc的DAO方法类
	 * 
	 * @param path
	 *            配置路径
	 * @return db ：
	 */
	public static BaseDbDao getBaseDbDao(String path) {

		Getjdbcconfig dbcof = new Getjdbcconfig(path);
		BaseDbDao db = null;
		if ("oracle".equals(dbcof.getDbtype())) {

			db = new OracleDbDaoI(dbcof.getDatabasename(), dbcof.getIp(), dbcof.getPort(), dbcof.getUsername(),
					dbcof.getPassword());
			return db;

		} else if ("mysql".equals(dbcof.getDbtype())) {

			db = new MysqlDbDaoI(dbcof.getDatabasename(), dbcof.getIp(), dbcof.getPort(), dbcof.getUsername(),
					dbcof.getPassword());
			return db;
		} else if ("mssql".equals(dbcof.getDbtype())) {

			db = new SqlServerDbDaoI(dbcof.getDatabasename(), dbcof.getIp(), dbcof.getPort(), dbcof.getUsername(),
					dbcof.getPassword());
			return db;

		} else if ("teradata".equals(dbcof.getDbtype())) {

			db = new TeradataDbDaoI(dbcof.getDatabasename(), dbcof.getIp(), dbcof.getPort(), dbcof.getUsername(),
					dbcof.getPassword());

			return db;

		}
		return db;
	}
}
