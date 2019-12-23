package com.radius.sodalityUser.response;

 

import com.radius.sodalityUser.model.User;

public class UserResponse {
private Boolean status;
private User data;
private String message;

public String getMessage() {
	return message;
}
public void setMessage(String message) {
	this.message = message;
}
public Boolean getStatus() {
	return status;
}
public void setStatus(Boolean status) {
	this.status = status;
}
public User getData() {
	return data;
}
public void setData(User user) {
	this.data = user;
}

}
