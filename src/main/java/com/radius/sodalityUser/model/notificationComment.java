package com.radius.sodalityUser.model;

import java.util.Date;

import org.neo4j.ogm.annotation.GeneratedValue;
import org.neo4j.ogm.annotation.Id;
import org.neo4j.ogm.annotation.NodeEntity;
import org.neo4j.ogm.annotation.Relationship;

@NodeEntity(label = "Notification_Comment")
public class notificationComment {
	@Id()
	@GeneratedValue()
	private Long id;
	private String description;
	@Relationship(type = "UNDER", direction = Relationship.OUTGOING)
	private Notice commentUnder;
	@Relationship(type = "COMMENT_BY", direction = Relationship.OUTGOING)
	private User commentBy;
 

	private Date createdDate;
	private Date updatedDate;
	private String uuid;
	
	 
	public Notice getCommentUnder() {
		return commentUnder;
	}
	public void setCommentUnder(Notice commentUnder) {
		this.commentUnder = commentUnder;
	}
	public Long getId() {
		return id;
	}
	public void setId(Long id) {
		this.id = id;
	}
	public String getDescription() {
		return description;
	}
	public void setDescription(String description) {
		this.description = description;
	}
	 
	public User getCommentBy() {
		return commentBy;
	}
	public void setCommentBy(User commentBy) {
		this.commentBy = commentBy;
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
	public String getUuid() {
		return uuid;
	}
	public void setUuid(String uuid) {
		this.uuid = uuid;
	}
	

}
