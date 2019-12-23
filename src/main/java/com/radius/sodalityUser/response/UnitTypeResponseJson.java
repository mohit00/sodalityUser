package com.radius.sodalityUser.response;

import java.util.Date;

public class UnitTypeResponseJson {

	   private String title;
	    private Integer carpetArea;
	    private Integer builtUpArea;
	    private Integer superBuiltUpArea;
	    private String uuid;
	    public Date createdDate;
	    
		public Date getCreatedDate() {
			return createdDate;
		}
		public void setCreatedDate(Date createdDate) {
			this.createdDate = createdDate;
		}
		public String getTitle() {
			return title;
		}
		public void setTitle(String title) {
			this.title = title;
		}
		public Integer getCarpetArea() {
			return carpetArea;
		}
		public void setCarpetArea(Integer carpetArea) {
			this.carpetArea = carpetArea;
		}
		public Integer getBuiltUpArea() {
			return builtUpArea;
		}
		public void setBuiltUpArea(Integer builtUpArea) {
			this.builtUpArea = builtUpArea;
		}
		public Integer getSuperBuiltUpArea() {
			return superBuiltUpArea;
		}
		public void setSuperBuiltUpArea(Integer superBuiltUpArea) {
			this.superBuiltUpArea = superBuiltUpArea;
		}
		public String getUuid() {
			return uuid;
		}
		public void setUuid(String uuid) {
			this.uuid = uuid;
		}
	    
	
 }
