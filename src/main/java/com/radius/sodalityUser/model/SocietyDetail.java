package com.radius.sodalityUser.model;

import org.neo4j.ogm.annotation.GeneratedValue;
import org.neo4j.ogm.annotation.Id;
import org.neo4j.ogm.annotation.NodeEntity;
import org.neo4j.ogm.annotation.Relationship;


@NodeEntity(label="society_detail") 
public class SocietyDetail {
	@Id()
	@GeneratedValue()
	private Long id;
	private String societyName;
	private String societyDisplayName;
	private String registeredAddress;
	private String BillAddress;
	private String BillName;
	private String Registration;
	private String GSTINT;
	private String PAN;
	private String TAN;
	private String serviceTax;
	private String contactNumber;
	private String contactEmail;
	private String aboutSociety;
	private Boolean amrAccess;
	private Boolean ibmsAccess;
	private String amrUserName;
	private String amrpassword;
	
	private String ibmsUserName;
	private String ibmspassword;
	
	public Boolean getAmrAccess() {
		return amrAccess;
	}

	public void setAmrAccess(Boolean amrAccess) {
		this.amrAccess = amrAccess;
	}

	public Boolean getIbmsAccess() {
		return ibmsAccess;
	}

	public void setIbmsAccess(Boolean ibmsAccess) {
		this.ibmsAccess = ibmsAccess;
	}

	public String getAmrUserName() {
		return amrUserName;
	}

	public void setAmrUserName(String amrUserName) {
		this.amrUserName = amrUserName;
	}

	public String getAmrpassword() {
		return amrpassword;
	}

	public void setAmrpassword(String amrpassword) {
		this.amrpassword = amrpassword;
	}

	public String getIbmsUserName() {
		return ibmsUserName;
	}

	public void setIbmsUserName(String ibmsUserName) {
		this.ibmsUserName = ibmsUserName;
	}

	public String getIbmspassword() {
		return ibmspassword;
	}

	public void setIbmspassword(String ibmspassword) {
		this.ibmspassword = ibmspassword;
	}

	@Relationship(type = "IMAGE_LIST", direction = Relationship.OUTGOING)
	public ImageList imageList;

	@Relationship(type = "AD_LIST", direction = Relationship.OUTGOING)
	public AdImage adImage;
 	private String billLogo;
	private String societyLogo;
	 
	 
	public String getSocietyName() {
		return societyName;
	}

	public void setSocietyName(String societyName) {
		this.societyName = societyName;
	}

	public Long getId() {
		return id;
	}
	
	public void setId(Long id) {
		this.id = id;
	}

 

	public String getSocietyDisplayName() {
		return societyDisplayName;
	}

	public void setSocietyDisplayName(String societyDisplayName) {
		this.societyDisplayName = societyDisplayName;
	}

	public String getRegisteredAddress() {
		return registeredAddress;
	}

	public void setRegisteredAddress(String registeredAddress) {
		this.registeredAddress = registeredAddress;
	}

	public String getBillAddress() {
		return BillAddress;
	}

	public void setBillAddress(String billAddress) {
		BillAddress = billAddress;
	}

	public String getBillName() {
		return BillName;
	}

	public void setBillName(String billName) {
		BillName = billName;
	}

	public String getRegistration() {
		return Registration;
	}

	public void setRegistration(String registration) {
		Registration = registration;
	}

	public String getGSTINT() {
		return GSTINT;
	}

	public void setGSTINT(String gSTINT) {
		GSTINT = gSTINT;
	}

	public String getPAN() {
		return PAN;
	}

	public void setPAN(String pAN) {
		PAN = pAN;
	}

	public String getTAN() {
		return TAN;
	}

	public void setTAN(String tAN) {
		TAN = tAN;
	}

	public String getServiceTax() {
		return serviceTax;
	}

	public void setServiceTax(String serviceTax) {
		this.serviceTax = serviceTax;
	}

	public String getContactNumber() {
		return contactNumber;
	}

	public void setContactNumber(String contactNumber) {
		this.contactNumber = contactNumber;
	}

	public String getContactEmail() {
		return contactEmail;
	}

	public void setContactEmail(String contactEmail) {
		this.contactEmail = contactEmail;
	}

	public String getAboutSociety() {
		return aboutSociety;
	}

	public void setAboutSociety(String aboutSociety) {
		this.aboutSociety = aboutSociety;
	}

	 
	public ImageList getImageList() {
		return imageList;
	}

	public void setImageList(ImageList imageList) {
		this.imageList = imageList;
	}

	 

	 

	public AdImage getAdImage() {
		return adImage;
	}

	public void setAdImage(AdImage adImage) {
		this.adImage = adImage;
	}

	public String getBillLogo() {
		return billLogo;
	}

	public void setBillLogo(String billLogo) {
		this.billLogo = billLogo;
	}

	public String getSocietyLogo() {
		return societyLogo;
	}

	public void setSocietyLogo(String societyLogo) {
		this.societyLogo = societyLogo;
	}
	 
	 
	
}
