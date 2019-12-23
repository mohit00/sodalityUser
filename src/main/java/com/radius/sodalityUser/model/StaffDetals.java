package com.radius.sodalityUser.model;

import java.util.Date;

import org.neo4j.ogm.annotation.GeneratedValue;
import org.neo4j.ogm.annotation.Id;
import org.neo4j.ogm.annotation.NodeEntity;
import org.neo4j.ogm.annotation.Relationship;
@NodeEntity(label="staff_detail") 

public class StaffDetals {
	@Id()
    @GeneratedValue()
    private Long id;
	 	private String employeeId ; 
		private String name;
		private String mobileNumber;
		private String designation;
		private String vendor;
		private Date from;
		private Date to;
		private Date dateOfCardIssue;
		private Date validUpto;
 		private Date dateOfBirth;
		private String aadharNo;
		private String address;
		private String chooseStaffWorkArea;
		private Boolean policeVerification;
		private String pic;
		@Relationship(type="CATEGORY",direction = Relationship.UNDIRECTED)
		public Category category;
		
		public Category getCategory() {
			return category;
		}
		public void setCategory(Category category) {
			this.category = category;
		}
		public String getPic() {
			return pic;
		}
		public Date getFrom() {
			return from;
		}
		public void setFrom(Date from) {
			this.from = from;
		}
		public Date getTo() {
			return to;
		}
		public void setTo(Date to) {
			this.to = to;
		}
		public Date getDateOfCardIssue() {
			return dateOfCardIssue;
		}
		public void setDateOfCardIssue(Date dateOfCardIssue) {
			this.dateOfCardIssue = dateOfCardIssue;
		}
		public Date getValidUpto() {
			return validUpto;
		}
		public void setValidUpto(Date validUpto) {
			this.validUpto = validUpto;
		}
		public Date getDateOfBirth() {
			return dateOfBirth;
		}
		public void setDateOfBirth(Date dateOfBirth) {
			this.dateOfBirth = dateOfBirth;
		}
		public void setPic(String pic) {
			this.pic = pic;
		}
		public Long getId() {
			return id;
		}
		public void setId(Long id) {
			this.id = id;
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
		public String getMobileNumber() {
			return mobileNumber;
		}
		public void setMobileNumber(String mobileNumber) {
			this.mobileNumber = mobileNumber;
		}
		public String getDesignation() {
			return designation;
		}
		public void setDesignation(String designation) {
			this.designation = designation;
		}
		public String getVendor() {
			return vendor;
		}
		public void setVendor(String vendor) {
			this.vendor = vendor;
		}
	     
		 
		  
		public String getAadharNo() {
			return aadharNo;
		}
		public void setAadharNo(String aadharNo) {
			this.aadharNo = aadharNo;
		}
		public String getAddress() {
			return address;
		}
		public void setAddress(String address) {
			this.address = address;
		}
		public String getChooseStaffWorkArea() {
			return chooseStaffWorkArea;
		}
		public void setChooseStaffWorkArea(String chooseStaffWorkArea) {
			this.chooseStaffWorkArea = chooseStaffWorkArea;
		}
		public Boolean getPoliceVerification() {
			return policeVerification;
		}
		public void setPoliceVerification(Boolean policeVerification) {
			this.policeVerification = policeVerification;
		}

  
	 


}
