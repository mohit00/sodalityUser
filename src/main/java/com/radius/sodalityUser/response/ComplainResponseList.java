package com.radius.sodalityUser.response;

import java.util.ArrayList;

public class ComplainResponseList {
	private Boolean status;
 	private String message;
 	private ArrayList<ComplainResponseJson> data;
	public Boolean getStatus() {
		return status;
	}
	public void setStatus(Boolean status) {
		this.status = status;
	}
	public String getMessage() {
		return message;
	}
	public void setMessage(String message) {
		this.message = message;
	}
	public ArrayList<ComplainResponseJson> getData() {
		return data;
	}
	public void setData(ArrayList<ComplainResponseJson> data) {
		this.data = data;
	}
 
	 
	 
 	
 }
