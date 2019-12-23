package com.radius.sodalityUser.response;

import java.util.Date;
import java.util.Set;

import com.radius.sodalityUser.model.User;

public class ComplainResponseJson {
private String complainTitle;
private String complainUuid;
private Date createDate;
private Date updatedDate;
private Date assignedDate;
private Set<User> AssignedTo;
public String getComplainTitle() {
	return complainTitle;
}
public void setComplainTitle(String complainTitle) {
	this.complainTitle = complainTitle;
}
public String getComplainUuid() {
	return complainUuid;
}
public void setComplainUuid(String complainUuid) {
	this.complainUuid = complainUuid;
}
public Date getCreateDate() {
	return createDate;
}
public void setCreateDate(Date createDate) {
	this.createDate = createDate;
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
public Set<User> getAssignedTo() {
	return AssignedTo;
}
public void setAssignedTo(Set<User> assignedTo) {
	AssignedTo = assignedTo;
}
 

}
