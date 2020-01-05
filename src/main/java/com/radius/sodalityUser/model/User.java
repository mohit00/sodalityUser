package com.radius.sodalityUser.model;

import java.util.Date;
import java.util.Set;

import org.neo4j.ogm.annotation.GeneratedValue;
import org.neo4j.ogm.annotation.Id;
import org.neo4j.ogm.annotation.Index;
import org.neo4j.ogm.annotation.NodeEntity;
import org.neo4j.ogm.annotation.Property;
import org.neo4j.ogm.annotation.Relationship;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedDate;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonProperty.Access;

@NodeEntity(label = "User")
public class User {
	@Id()
	@GeneratedValue()
	private Long id;

	@Index(unique = true)
	private String email;
	
	private String password;
	private String user_type;
	private String Status;
	@Property(name = "uuid")
	private String uuid;

	private String createdBy;

	@CreatedDate

	private Date createdDate;

	private String lastModifiedBy;

	@LastModifiedDate
	private Date lastModifiedDate;

	@Relationship(type = "USER_DETAIL", direction = Relationship.OUTGOING)
	public UserDetail userDetail;

	@Relationship(type = "USER_DETAIL", direction = Relationship.OUTGOING)
	public SocietyDetail societyDetail;

	@Relationship(type = "USER_DETAIL", direction = Relationship.OUTGOING)
	public ResidentDetail residentDetail;

	@Relationship(type = "USER_DETAIL", direction = Relationship.OUTGOING)
	public FamilyResident familyDetail;

	@Relationship(type = "USER_DETAIL", direction = Relationship.OUTGOING)
	public StaffDetals staffDetals;
	@JsonProperty(access = Access.WRITE_ONLY)
	@Relationship(type = "UNDER", direction = Relationship.UNDIRECTED)
	public User parrentAccount;

	@Relationship(type = "FLAT_OWNED", direction = Relationship.UNDIRECTED)
	public Set<Unit> flatOwned;

	public FamilyResident getFamilyDetail() {
		return familyDetail;
	}

	public void setFamilyDetail(FamilyResident familyDetail) {
		this.familyDetail = familyDetail;
	}

	public Set<Unit> getFlatOwned() {
		return flatOwned;
	}

	public void setFlatOwned(Set<Unit> flatOwned) {
		this.flatOwned = flatOwned;
	}

	public String getStatus() {
		return Status;
	}

	public void setStatus(String status) {
		Status = status;
	}

	public String getCreatedBy() {
		return createdBy;
	}

	public void setCreatedBy(String createdBy) {
		this.createdBy = createdBy;
	}

	public Date getCreatedDate() {
		return createdDate;
	}

	public void setCreatedDate(Date createdDate) {
		this.createdDate = createdDate;
	}

	public String getLastModifiedBy() {
		return lastModifiedBy;
	}

	public void setLastModifiedBy(String lastModifiedBy) {
		this.lastModifiedBy = lastModifiedBy;
	}

	public Date getLastModifiedDate() {
		return lastModifiedDate;
	}

	public void setLastModifiedDate(Date lastModifiedDate) {
		this.lastModifiedDate = lastModifiedDate;
	}

	public User getParrentAccount() {
		return parrentAccount;
	}

	public void setParrentAccount(User parrentAccount) {
		this.parrentAccount = parrentAccount;
	}

	public StaffDetals getStaffDetals() {
		return staffDetals;
	}

	public void setStaffDetals(StaffDetals staffDetals) {
		this.staffDetals = staffDetals;
	}

	public UserDetail getUserDetail() {
		return userDetail;
	}

	public void setUserDetail(UserDetail userDetail) {
		this.userDetail = userDetail;
	}

	public SocietyDetail getSocietyDetail() {
		return societyDetail;
	}

	public void setSocietyDetail(SocietyDetail societyDetail) {
		this.societyDetail = societyDetail;
	}

	public ResidentDetail getResidentDetail() {
		return residentDetail;
	}

	public void setResidentDetail(ResidentDetail residentDetail) {
		this.residentDetail = residentDetail;
	}

	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
	}

	public String getUser_type() {
		return user_type;
	}

	public void setUser_type(String user_type) {
		this.user_type = user_type;
	}

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public String getUuid() {
		return uuid;
	}

	public void setUuid(String uuid) {
		this.uuid = uuid;
	}

	public String getPassword() {
		return password;
	}

	public void setPassword(String password) {
		this.password = password;
	}
}
