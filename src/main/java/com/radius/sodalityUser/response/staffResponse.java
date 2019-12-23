package com.radius.sodalityUser.response;

import java.util.Date;

public class staffResponse {
	private String uuid;
	private String email;
	private Date createdDate;
	private Date lastModifiedDate;
	private String employeeId;
	private String name;
	private Date validUpto;
	private String designation;
	private String category;
	private String pic;
	private Boolean policeVerification;
	private String chooseStaffWorkArea;
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
	public String getEmployeeId() {
		return employeeId;
	}
	public void setEmployeeId(String employeeId) {
		this.employeeId = employeeId;
	}
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public Date getValidUpto() {
		return validUpto;
	}
	public void setValidUpto(Date validUpto) {
		this.validUpto = validUpto;
	}
	public String getDesignation() {
		return designation;
	}
	public void setDesignation(String designation) {
		this.designation = designation;
	}
	public String getCategory() {
		return category;
	}
	public void setCategory(String category) {
		this.category = category;
	}
	public String getPic() {
		return pic;
	}
	public void setPic(String pic) {
		this.pic = pic;
	}
	public Boolean getPoliceVerification() {
		return policeVerification;
	}
	public void setPoliceVerification(Boolean policeVerification) {
		this.policeVerification = policeVerification;
	}
	public String getChooseStaffWorkArea() {
		return chooseStaffWorkArea;
	}
	public void setChooseStaffWorkArea(String chooseStaffWorkArea) {
		this.chooseStaffWorkArea = chooseStaffWorkArea;
	}



}
