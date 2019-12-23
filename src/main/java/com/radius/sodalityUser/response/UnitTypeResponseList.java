package com.radius.sodalityUser.response;

import java.util.ArrayList;

public class UnitTypeResponseList {
	private Boolean status;
 	private String message;
 	private ArrayList<UnitTypeResponseJson> data;
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
	public ArrayList<UnitTypeResponseJson> getData() {
		return data;
	}
	public void setData(ArrayList<UnitTypeResponseJson> data) {
		this.data = data;
	}
	 
 	
 }
