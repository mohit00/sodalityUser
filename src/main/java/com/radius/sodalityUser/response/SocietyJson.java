package com.radius.sodalityUser.response;

import java.util.Date;

public class SocietyJson {
	private String uuid;

	private String societyName;
	private String email;
	private String societyDisplayName;
	private Date createdDate;
	private Date lastModifiedDate;
	private String contactNumber;
	public String getSocietyName() {
		return societyName;
	}
	public void setSocietyName(String societyName) {
		this.societyName = societyName;
	}
	public String getEmail() {
		return email;
	}
	public void setEmail(String email) {
		this.email = email;
	}
	public String getSocietyDisplayName() {
		return societyDisplayName;
	}
	public void setSocietyDisplayName(String societyDisplayName) {
		this.societyDisplayName = societyDisplayName;
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
	public String getContactNumber() {
		return contactNumber;
	}
	public void setContactNumber(String contactNumber) {
		this.contactNumber = contactNumber;
	}
	public String getUuid() {
		return uuid;
	}
	public void setUuid(String uuid) {
		this.uuid = uuid;
	}
	 
 
}
