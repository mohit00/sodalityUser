package com.radius.sodalityUser.response;

import java.util.Date;

public class UnitResponsejson {


	private String uuid;

	private String unitNo;
	private String unitDescription;
	private String unitType;
	private Date createdDate;
	private Date lastModifiedDate;
	private String soldStatus;
	public String getUuid() {
		return uuid;
	}
	public void setUuid(String uuid) {
		this.uuid = uuid;
	}
	public String getUnitNo() {
		return unitNo;
	}
	public void setUnitNo(String unitNo) {
		this.unitNo = unitNo;
	}
 
	public String getUnitDescription() {
		return unitDescription;
	}
	public void setUnitDescription(String unitDescription) {
		this.unitDescription = unitDescription;
	}
	public String getUnitType() {
		return unitType;
	}
	public void setUnitType(String unitType) {
		this.unitType = unitType;
	}
	public Date getCreatedDate() {
		return createdDate;
	}
	public void setCreatedDate(Date createdDate) {
		this.createdDate = createdDate;
	}
	public Date getLastModifiedDate() {
		return lastModifiedDate;
	}
	public void setLastModifiedDate(Date lastModifiedDate) {
		this.lastModifiedDate = lastModifiedDate;
	}
	public String getSoldStatus() {
		return soldStatus;
	}
	public void setSoldStatus(String soldStatus) {
		this.soldStatus = soldStatus;
	}
	
}
