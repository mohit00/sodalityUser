package com.radius.sodalityUser.model;

import org.neo4j.ogm.annotation.GeneratedValue;
import org.neo4j.ogm.annotation.Id;
import org.neo4j.ogm.annotation.NodeEntity;

@NodeEntity(label = "Family_Resident")
public class FamilyResident {
	@Id()
	@GeneratedValue()
	private Long id;
	private String profileImage;
	private String name;
	private String mobileNumber;
	
 	private String relationShip;
	private String Descirption;
	 
	
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
	public String getRelationShip() {
		return relationShip;
	}
	public void setRelationShip(String relationShip) {
		this.relationShip = relationShip;
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
	 
	public String getDescirption() {
		return Descirption;
	}
	public void setDescirption(String descirption) {
		Descirption = descirption;
	}
	 
	
}
