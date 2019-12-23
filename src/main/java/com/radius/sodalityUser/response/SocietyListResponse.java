package com.radius.sodalityUser.response;

import java.util.ArrayList;

public class SocietyListResponse {
	private Boolean status;
 	private String message;
 	private ArrayList<SocietyJson> data;
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
	public ArrayList<SocietyJson> getData() {
		return data;
	}
	public void setData(ArrayList<SocietyJson> data) {
		this.data = data;
	}
	 
 	
 }
