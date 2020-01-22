package com.radius.sodalityUser.response;

import java.util.ArrayList;
import java.util.Date;

import com.radius.sodalityUser.model.UserType.noticeType;

public class NoticeResponseJson {
private String title;
private String description;
private String uuid;
private noticeType type;
private ArrayList<String> imagelist;
private String discussBy;
private Date createdDate;
private Date updatedDate;
private String userImage;
private String userName;

private String noticeTo;

public String getNoticeTo() {
	return noticeTo;
}
public void setNoticeTo(String noticeTo) {
	this.noticeTo = noticeTo;
}
public String getUserName() {
	return userName;
}
public void setUserName(String userName) {
	this.userName = userName;
}
public String getUserImage() {
	return userImage;
}
public void setUserImage(String userImage) {
	this.userImage = userImage;
}
public ArrayList<String> getImagelist() {
	return imagelist;
}
public void setImagelist(ArrayList<String> imagelist) {
	this.imagelist = imagelist;
}
public String getDiscussBy() {
	return discussBy;
}
public void setDiscussBy(String discussBy) {
	this.discussBy = discussBy;
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
public noticeType getType() {
	return type;
}
public void setType(noticeType type) {
	this.type = type;
}
public String getUuid() {
	return uuid;
}
public void setUuid(String uuid) {
	this.uuid = uuid;
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

}
