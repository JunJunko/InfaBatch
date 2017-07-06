package com.exprotmeteexcel.bean;

import java.util.List;
import java.util.Map;

public class MateBean {

	private String dbtype;
	private String dbconfpath;
	private String dbSid;
	private List<Map<String, Object>> matedate;

	public String getDbtype() {
		return dbtype;
	}

	public void setDbtype(String dbtype) {
		this.dbtype = dbtype;
	}

	public String getDbconfpath() {
		return dbconfpath;
	}

	public void setDbconfpath(String dbconfpath) {
		this.dbconfpath = dbconfpath;
	}

	public String getDbSid() {
		return dbSid;
	}

	public void setDbSid(String dbSid) {
		this.dbSid = dbSid;
	}

	public List<Map<String, Object>> getMatedate() {
		return matedate;
	}

	public void setMatedate(List<Map<String, Object>> matedate) {
		this.matedate = matedate;
	}

	@Override
	public String toString() {
		return "MateBean [dbtype=" + dbtype + ", dbconfpath=" + dbconfpath + ", dbSid=" + dbSid + ", matedate="
				+ matedate + "]";
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((dbSid == null) ? 0 : dbSid.hashCode());
		result = prime * result + ((dbconfpath == null) ? 0 : dbconfpath.hashCode());
		result = prime * result + ((dbtype == null) ? 0 : dbtype.hashCode());
		result = prime * result + ((matedate == null) ? 0 : matedate.hashCode());
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		MateBean other = (MateBean) obj;
		if (dbSid == null) {
			if (other.dbSid != null)
				return false;
		} else if (!dbSid.equals(other.dbSid))
			return false;
		if (dbconfpath == null) {
			if (other.dbconfpath != null)
				return false;
		} else if (!dbconfpath.equals(other.dbconfpath))
			return false;
		if (dbtype == null) {
			if (other.dbtype != null)
				return false;
		} else if (!dbtype.equals(other.dbtype))
			return false;
		if (matedate == null) {
			if (other.matedate != null)
				return false;
		} else if (!matedate.equals(other.matedate))
			return false;
		return true;
	}

}
