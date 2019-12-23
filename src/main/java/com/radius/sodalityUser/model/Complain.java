package com.radius.sodalityUser.model;
 
import java.util.Date;
import java.util.Set;

import org.neo4j.ogm.annotation.GeneratedValue;
import org.neo4j.ogm.annotation.Id;
import org.neo4j.ogm.annotation.NodeEntity;
import org.neo4j.ogm.annotation.Property;
import org.neo4j.ogm.annotation.Relationship;

@NodeEntity(label = "Complain")

public class Complain {
    @Id()
    @GeneratedValue()
    private Long id;
    @Property(name = "uuid")
    private String uuid;
    private String title;
    private String description;
    private String category;
    private String complainStatus;
    private Date createdDate;
    private Date updatedDate;
    private Date assignedDate;
    private String OTP;

    public String getOTP() {
		return OTP;
	}
	public void setOTP(String oTP) {
		OTP = oTP;
	}
	private Set<String> images;
    @Relationship(type = "UNDER", direction = Relationship.UNDIRECTED)
    public User resident;
    @Relationship(type = "ASSIGNED_BY", direction = Relationship.UNDIRECTED)
    public User assignedBy;
    @Relationship(type = "ASSIGNED_TO", direction = Relationship.UNDIRECTED)
    public Set<User> assignedTo;
    @Relationship(type = "COMPLAIN_COMENTS", direction = Relationship.UNDIRECTED)
    public ComplainComment Comment;
   
	public String getComplainStatus() {
		return complainStatus;
	}
	public void setComplainStatus(String complainStatus) {
		this.complainStatus = complainStatus;
	}
	public Date getCreatedDate() {
		return createdDate;
	}
	public void setCreatedDate(Date createdDate) {
		this.createdDate = createdDate;
	}
	public Date getUpdatedDate() {
		return updatedDate;
	}
	public void setUpdatedDate(Date updatedDate) {
		this.updatedDate = updatedDate;
	}
	public Date getAssignedDate() {
		return assignedDate;
	}
	public void setAssignedDate(Date assignedDate) {
		this.assignedDate = assignedDate;
	}
	public String getCategory() {
		return category;
	}
	public void setCategory(String category) {
		this.category = category;
	}
	public ComplainComment getComment() {
		return Comment;
	}
	public void setComment(ComplainComment comment) {
		Comment = comment;
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
	public String getTitle() {
		return title;
	}
	public void setTitle(String title) {
		this.title = title;
	}
	public String getDescription() {
		return description;
	}
	public void setDescription(String description) {
		this.description = description;
	}
	public Set<String> getImages() {
		return images;
	}
	public void setImages(Set<String> images) {
		this.images = images;
	}
	public User getResident() {
		return resident;
	}
	public void setResident(User resident) {
		this.resident = resident;
	}
	 
	public User getAssignedBy() {
		return assignedBy;
	}
	public void setAssignedBy(User assignedBy) {
		this.assignedBy = assignedBy;
	}
	public Set<User> getAssignedTo() {
		return assignedTo;
	}
	public void setAssignedTo(Set<User> assignedTo) {
		this.assignedTo = assignedTo;
	}
	 
	


}
