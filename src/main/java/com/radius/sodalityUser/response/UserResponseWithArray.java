package com.radius.sodalityUser.response;

import java.util.ArrayList;

import com.radius.sodalityUser.model.User;

public class UserResponseWithArray {

	private Boolean Status;
	private String Message;
	private ArrayList<User> Data;
	public Boolean getStatus() {
		return Status;
	}
	public void setStatus(Boolean status) {
		Status = status;
	}
	public String getMessage() {
		return Message;
	}
	public void setMessage(String message) {
		Message = message;
	}
	public ArrayList<User> getData() {
		return Data;
	}
	public void setData(ArrayList<User> data) {
		Data = data;
	}
	
}
