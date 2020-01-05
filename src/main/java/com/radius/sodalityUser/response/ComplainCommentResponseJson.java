package com.radius.sodalityUser.response;

import java.util.Date;

import com.radius.sodalityUser.model.User;

public class ComplainCommentResponseJson {
	private String comment;
	private Date complainDate;
	private User commentBy;
	private  String profileImage;
	private String commentUuid;
	private String complainStatus;
	public String getComment() {
		return comment;
	}
	public void setComment(String comment) {
		this.comment = comment;
	}
	 
	 
	 
	public String getComplainStatus() {
		return complainStatus;
	}
	public void setComplainStatus(String complainStatus) {
		this.complainStatus = complainStatus;
	}
	public String getCommentUuid() {
		return commentUuid;
	}
	public void setCommentUuid(String commentUuid) {
		this.commentUuid = commentUuid;
	}
	public Date getComplainDate() {
		return complainDate;
	}
	public void setComplainDate(Date complainDate) {
		this.complainDate = complainDate;
	}
	public User getCommentBy() {
		return commentBy;
	}
	public void setCommentBy(User commentBy) {
		this.commentBy = commentBy;
	}
	public String getProfileImage() {
		return profileImage;
	}
	public void setProfileImage(String profileImage) {
		this.profileImage = profileImage;
	}
	

}
