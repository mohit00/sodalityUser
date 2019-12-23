package com.radius.sodalityUser.response;

import java.util.ArrayList;

public class ResidentListResponse {
	private Boolean status;
 	private String message;
 	private ArrayList<ResidentResponseJson> data;
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
	public ArrayList<ResidentResponseJson> getData() {
		return data;
	}
	public void setData(ArrayList<ResidentResponseJson> data) {
		this.data = data;
	}
	 
	 
 	
 }
