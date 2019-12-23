package com.radius.sodalityUser.model;

import java.util.Date;

import org.neo4j.ogm.annotation.GeneratedValue;
import org.neo4j.ogm.annotation.Id;
import org.neo4j.ogm.annotation.NodeEntity;

@NodeEntity(label="resident_detail") 
public class ResidentDetail {
	@Id()
    @GeneratedValue()
    private Long id;
	private String firstName;
	private String residentType;
	private String middleName;
	private String lastName;
	private Boolean clubMembership;
	private String mobileNumber;
	private String alternateMobileNumber;
	private String alternateEmailId;
	private String landLine;
	private String intercom;
	private String occupation;
	
	private Date possesionDate;
	private Date serviceStartDate;
	private String accessCardNumber;
	private Boolean residentResiding;
	private String profileImage;
	
	public String getResidentType() {
		return residentType;
	}
	public void setResidentType(String residentType) {
		this.residentType = residentType;
	}
	public String getProfileImage() {
		return profileImage;
	}
	public void setProfileImage(String profileImage) {
		this.profileImage = profileImage;
	}
	public Long getId() {
		return id;
	}
	public void setId(Long id) {
		this.id = id;
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
	public Boolean getClubMembership() {
		return clubMembership;
	}
	public void setClubMembership(Boolean clubMembership) {
		this.clubMembership = clubMembership;
	}
	public String getMobileNumber() {
		return mobileNumber;
	}
	public void setMobileNumber(String mobileNumber) {
		this.mobileNumber = mobileNumber;
	}
	public String getAlternateMobileNumber() {
		return alternateMobileNumber;
	}
	public void setAlternateMobileNumber(String alternateMobileNumber) {
		this.alternateMobileNumber = alternateMobileNumber;
	}
	public String getAlternateEmailId() {
		return alternateEmailId;
	}
	public void setAlternateEmailId(String alternateEmailId) {
		this.alternateEmailId = alternateEmailId;
	}
	public String getLandLine() {
		return landLine;
	}
	public void setLandLine(String landLine) {
		this.landLine = landLine;
	}
	public String getIntercom() {
		return intercom;
	}
	public void setIntercom(String intercom) {
		this.intercom = intercom;
	}
	public String getOccupation() {
		return occupation;
	}
	public void setOccupation(String occupation) {
		this.occupation = occupation;
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
	public String getAccessCardNumber() {
		return accessCardNumber;
	}
	public void setAccessCardNumber(String accessCardNumber) {
		this.accessCardNumber = accessCardNumber;
	}
	public Boolean getResidentResiding() {
		return residentResiding;
	}
	public void setResidentResiding(Boolean residentResiding) {
		this.residentResiding = residentResiding;
	}
 



 

}
