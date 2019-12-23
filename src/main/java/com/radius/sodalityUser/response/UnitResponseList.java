package com.radius.sodalityUser.response;

import java.util.ArrayList;

public class UnitResponseList {

	private Boolean status;
 	private ArrayList<UnitResponsejson> data;
	private String message;
	public Boolean getStatus() {
		return status;
	}
	public void setStatus(Boolean status) {
		this.status = status;
	}
	public ArrayList<UnitResponsejson> getData() {
		return data;
	}
	public void setData(ArrayList<UnitResponsejson> data) {
		this.data = data;
	}
	public String getMessage() {
		return message;
	}
	public void setMessage(String message) {
		this.message = message;
	}
	
}
