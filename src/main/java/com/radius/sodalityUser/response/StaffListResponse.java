package com.radius.sodalityUser.response;

import java.util.ArrayList;

public class StaffListResponse {

	private Boolean status;
 	private String message;
 	private ArrayList<staffResponse> data;
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
	public ArrayList<staffResponse> getData() {
		return data;
	}
	public void setData(ArrayList<staffResponse> data) {
		this.data = data;
	}
	 
 	
 
}
