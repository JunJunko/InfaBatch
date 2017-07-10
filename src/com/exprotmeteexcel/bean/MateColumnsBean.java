package com.exprotmeteexcel.bean;

public class MateColumnsBean implements Cloneable {
	String dbinfo;
	String owner;
	String tableName;
	String tranTableName;
	String tableRemark;
	String columnsName;
	String tranColumnName;
	String columnRemark;
	String columnDataType;
	String tranColumnDataType;
	String isNull;
	String defaultValue;

	// 如果是pri
	String primaryKey;
	String piValue;
	String remark;
	String pl;
	String synchronousType;
	String dateCount;
	String dateSize;
	String updateTime;
	String synchronousLogic;
	String valid;
	String isGigDateCol;
	String system;

	public Object clone() {
		MateColumnsBean o = null;
		try {
			o = (MateColumnsBean) super.clone();
		} catch (CloneNotSupportedException e) {
			e.printStackTrace();
		}
		return o;
	}

	public String getDbinfo() {
		return dbinfo;
	}

	public void setDbinfo(String dbinfo) {
		this.dbinfo = dbinfo;
	}

	public String getOwner() {
		return owner;
	}

	public void setOwner(String owner) {
		this.owner = owner;
	}

	public String getTableName() {
		return tableName;
	}

	public void setTableName(String tableName) {
		this.tableName = tableName;
	}

	public String getTranTableName() {
		return tranTableName;
	}

	public void setTranTableName(String tranTableName) {
		this.tranTableName = tranTableName;
	}

	public String getTableRemark() {
		return tableRemark;
	}

	public void setTableRemark(String tableRemark) {
		this.tableRemark = tableRemark;
	}

	public String getColumnsName() {
		return columnsName;
	}

	public void setColumnsName(String columnsName) {
		this.columnsName = columnsName;
	}

	public String getTranColumnName() {
		return tranColumnName;
	}

	public void setTranColumnName(String tranColumnName) {
		this.tranColumnName = tranColumnName;
	}

	public String getColumnRemark() {
		return columnRemark;
	}

	public void setColumnRemark(String columnRemark) {
		this.columnRemark = columnRemark;
	}

	public String getColumnDataType() {
		return columnDataType;
	}

	public void setColumnDataType(String columnDataType) {
		this.columnDataType = columnDataType;
	}

	public String getTranColumnDataType() {
		return tranColumnDataType;
	}

	public void setTranColumnDataType(String tranColumnDataType) {
		this.tranColumnDataType = tranColumnDataType;
	}

	public String getIsNull() {
		return isNull;
	}

	public void setIsNull(String isNull) {
		this.isNull = isNull;
	}

	public String getDefaultValue() {
		return defaultValue;
	}

	public void setDefaultValue(String defaultValue) {
		this.defaultValue = defaultValue;
	}

	public String getPrimaryKey() {
		return primaryKey;
	}

	public void setPrimaryKey(String primaryKey) {
		this.primaryKey = primaryKey;
	}

	public String getPiValue() {
		return piValue;
	}

	public void setPiValue(String piValue) {
		this.piValue = piValue;
	}

	public String getRemark() {
		return remark;
	}

	public void setRemark(String remark) {
		this.remark = remark;
	}

	public String getPl() {
		return pl;
	}

	public void setPl(String pl) {
		this.pl = pl;
	}

	public String getSynchronousType() {
		return synchronousType;
	}

	public void setSynchronousType(String synchronousType) {
		this.synchronousType = synchronousType;
	}

	public String getDateCount() {
		return dateCount;
	}

	public void setDateCount(String dateCount) {
		this.dateCount = dateCount;
	}

	public String getDateSize() {
		return dateSize;
	}

	public void setDateSize(String dateSize) {
		this.dateSize = dateSize;
	}

	public String getUpdateTime() {
		return updateTime;
	}

	public void setUpdateTime(String updateTime) {
		this.updateTime = updateTime;
	}

	public String getSynchronousLogic() {
		return synchronousLogic;
	}

	public void setSynchronousLogic(String synchronousLogic) {
		this.synchronousLogic = synchronousLogic;
	}

	public String getValid() {
		return valid;
	}

	public void setValid(String valid) {
		this.valid = valid;
	}

	public String getIsGigDateCol() {
		return isGigDateCol;
	}

	public void setIsGigDateCol(String isGigDateCol) {
		this.isGigDateCol = isGigDateCol;
	}

	public String getSystem() {
		return system;
	}

	public void setSystem(String system) {
		this.system = system;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((columnDataType == null) ? 0 : columnDataType.hashCode());
		result = prime * result + ((columnRemark == null) ? 0 : columnRemark.hashCode());
		result = prime * result + ((columnsName == null) ? 0 : columnsName.hashCode());
		result = prime * result + ((dateCount == null) ? 0 : dateCount.hashCode());
		result = prime * result + ((dateSize == null) ? 0 : dateSize.hashCode());
		result = prime * result + ((dbinfo == null) ? 0 : dbinfo.hashCode());
		result = prime * result + ((defaultValue == null) ? 0 : defaultValue.hashCode());
		result = prime * result + ((isGigDateCol == null) ? 0 : isGigDateCol.hashCode());
		result = prime * result + ((isNull == null) ? 0 : isNull.hashCode());
		result = prime * result + ((owner == null) ? 0 : owner.hashCode());
		result = prime * result + ((piValue == null) ? 0 : piValue.hashCode());
		result = prime * result + ((pl == null) ? 0 : pl.hashCode());
		result = prime * result + ((primaryKey == null) ? 0 : primaryKey.hashCode());
		result = prime * result + ((remark == null) ? 0 : remark.hashCode());
		result = prime * result + ((synchronousLogic == null) ? 0 : synchronousLogic.hashCode());
		result = prime * result + ((synchronousType == null) ? 0 : synchronousType.hashCode());
		result = prime * result + ((tableName == null) ? 0 : tableName.hashCode());
		result = prime * result + ((tableRemark == null) ? 0 : tableRemark.hashCode());
		result = prime * result + ((tranColumnDataType == null) ? 0 : tranColumnDataType.hashCode());
		result = prime * result + ((tranColumnName == null) ? 0 : tranColumnName.hashCode());
		result = prime * result + ((tranTableName == null) ? 0 : tranTableName.hashCode());
		result = prime * result + ((updateTime == null) ? 0 : updateTime.hashCode());
		result = prime * result + ((valid == null) ? 0 : valid.hashCode());
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
		MateColumnsBean other = (MateColumnsBean) obj;
		if (columnDataType == null) {
			if (other.columnDataType != null)
				return false;
		} else if (!columnDataType.equals(other.columnDataType))
			return false;
		if (columnRemark == null) {
			if (other.columnRemark != null)
				return false;
		} else if (!columnRemark.equals(other.columnRemark))
			return false;
		if (columnsName == null) {
			if (other.columnsName != null)
				return false;
		} else if (!columnsName.equals(other.columnsName))
			return false;
		if (dateCount == null) {
			if (other.dateCount != null)
				return false;
		} else if (!dateCount.equals(other.dateCount))
			return false;
		if (dateSize == null) {
			if (other.dateSize != null)
				return false;
		} else if (!dateSize.equals(other.dateSize))
			return false;
		if (dbinfo == null) {
			if (other.dbinfo != null)
				return false;
		} else if (!dbinfo.equals(other.dbinfo))
			return false;
		if (defaultValue == null) {
			if (other.defaultValue != null)
				return false;
		} else if (!defaultValue.equals(other.defaultValue))
			return false;
		if (isGigDateCol == null) {
			if (other.isGigDateCol != null)
				return false;
		} else if (!isGigDateCol.equals(other.isGigDateCol))
			return false;
		if (isNull == null) {
			if (other.isNull != null)
				return false;
		} else if (!isNull.equals(other.isNull))
			return false;
		if (owner == null) {
			if (other.owner != null)
				return false;
		} else if (!owner.equals(other.owner))
			return false;
		if (piValue == null) {
			if (other.piValue != null)
				return false;
		} else if (!piValue.equals(other.piValue))
			return false;
		if (pl == null) {
			if (other.pl != null)
				return false;
		} else if (!pl.equals(other.pl))
			return false;
		if (primaryKey == null) {
			if (other.primaryKey != null)
				return false;
		} else if (!primaryKey.equals(other.primaryKey))
			return false;
		if (remark == null) {
			if (other.remark != null)
				return false;
		} else if (!remark.equals(other.remark))
			return false;
		if (synchronousLogic == null) {
			if (other.synchronousLogic != null)
				return false;
		} else if (!synchronousLogic.equals(other.synchronousLogic))
			return false;
		if (synchronousType == null) {
			if (other.synchronousType != null)
				return false;
		} else if (!synchronousType.equals(other.synchronousType))
			return false;
		if (tableName == null) {
			if (other.tableName != null)
				return false;
		} else if (!tableName.equals(other.tableName))
			return false;
		if (tableRemark == null) {
			if (other.tableRemark != null)
				return false;
		} else if (!tableRemark.equals(other.tableRemark))
			return false;
		if (tranColumnDataType == null) {
			if (other.tranColumnDataType != null)
				return false;
		} else if (!tranColumnDataType.equals(other.tranColumnDataType))
			return false;
		if (tranColumnName == null) {
			if (other.tranColumnName != null)
				return false;
		} else if (!tranColumnName.equals(other.tranColumnName))
			return false;
		if (tranTableName == null) {
			if (other.tranTableName != null)
				return false;
		} else if (!tranTableName.equals(other.tranTableName))
			return false;
		if (updateTime == null) {
			if (other.updateTime != null)
				return false;
		} else if (!updateTime.equals(other.updateTime))
			return false;
		if (valid == null) {
			if (other.valid != null)
				return false;
		} else if (!valid.equals(other.valid))
			return false;
		return true;
	}

	@Override
	public String toString() {
		return "MateColumnsBean [dbinfo=" + dbinfo + ", owner=" + owner + ", tableName=" + tableName
				+ ", tranTableName=" + tranTableName + ", tableRemark=" + tableRemark + ", columnsName=" + columnsName
				+ ", tranColumnName=" + tranColumnName + ", columnRemark=" + columnRemark + ", columnDataType="
				+ columnDataType + ", tranColumnDataType=" + tranColumnDataType + ", isNull=" + isNull
				+ ", defaultValue=" + defaultValue + ", primaryKey=" + primaryKey + ", piValue=" + piValue + ", remark="
				+ remark + ", pl=" + pl + ", synchronousType=" + synchronousType + ", dateCount=" + dateCount
				+ ", dateSize=" + dateSize + ", updateTime=" + updateTime + ", synchronousLogic=" + synchronousLogic
				+ ", valid=" + valid + ", isGigDateCol=" + isGigDateCol + "]";
	}

}
