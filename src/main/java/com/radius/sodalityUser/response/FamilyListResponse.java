package com.radius.sodalityUser.response;

import java.util.ArrayList;

public class FamilyListResponse {
	private Boolean status;
 	private String message;
 	private ArrayList<FamilyResponseJson> data;
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
	public ArrayList<FamilyResponseJson> getData() {
		return data;
	}
	public void setData(ArrayList<FamilyResponseJson> data) {
		this.data = data;
	}
 
	 
 	
 }
