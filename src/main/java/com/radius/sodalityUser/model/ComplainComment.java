package com.radius.sodalityUser.model;

import java.util.Date;

import org.neo4j.ogm.annotation.GeneratedValue;
import org.neo4j.ogm.annotation.Id;
import org.neo4j.ogm.annotation.Relationship;

public class ComplainComment {
	@Id()
	@GeneratedValue()
	private Long id;
	private String comment;
	private Date CommentDate;
	private String uuid;

	@Relationship(type = "COMMENT_BY", direction = Relationship.UNDIRECTED)
	private User commentBy;
	 

	public Date getCommentDate() {
		return CommentDate;
	}

	public void setCommentDate(Date commentDate) {
		CommentDate = commentDate;
	}

	public String getUuid() {
		return uuid;
	}

	public void setUuid(String uuid) {
		this.uuid = uuid;
	}

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	@Relationship(type = "UNDER", direction = Relationship.UNDIRECTED)
	private Complain Complain;

	public String getComment() {
		return comment;
	}

	public void setComment(String comment) {
		this.comment = comment;
	}

	public Complain getComplain() {
		return Complain;
	}

	public void setComplain(Complain complain) {
		Complain = complain;
	}

	public User getCommentBy() {
		return commentBy;
	}

	public void setCommentBy(User commentBy) {
		this.commentBy = commentBy;
	}
	
}
