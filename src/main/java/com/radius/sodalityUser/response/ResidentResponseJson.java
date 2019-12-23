package com.radius.sodalityUser.response;

import java.util.Date;

public class ResidentResponseJson {
	private String uuid;
	private String email;
	private String firstName;
	private String middleName;
	private String lastName;
	private Boolean clubMemberShip;
	private String mobileNumber;
	private Date possesionDate;
	private Date serviceStartDate;
	private Date createdDate;
	private Date lastModifiedDate;
	
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
	public String getUuid() {
		return uuid;
	}
	public void setUuid(String uuid) {
		this.uuid = uuid;
	}
	public String getEmail() {
		return email;
	}
	public void setEmail(String email) {
		this.email = email;
	}
	public String getFirstName() {
		return firstName;
	}
	public void setFirstName(String firstName) {
		this.firstName = firstName;
	}
	public String getMiddleName() {
		return middleName;
	}
	public void setMiddleName(String middleName) {
		this.middleName = middleName;
	}
	public String getLastName() {
		return lastName;
	}
	public void setLastName(String lastName) {
		this.lastName = lastName;
	}
	public Boolean getClubMemberShip() {
		return clubMemberShip;
	}
	public void setClubMemberShip(Boolean clubMemberShip) {
		this.clubMemberShip = clubMemberShip;
	}
	public String getMobileNumber() {
		return mobileNumber;
	}
	public void setMobileNumber(String mobileNumber) {
		this.mobileNumber = mobileNumber;
	}
	public Date getPossesionDate() {
		return possesionDate;
	}
	public void setPossesionDate(Date possesionDate) {
		this.possesionDate = possesionDate;
	}
	public Date getServiceStartDate() {
		return serviceStartDate;
	}
	public void setServiceStartDate(Date serviceStartDate) {
		this.serviceStartDate = serviceStartDate;
	}
	
}
