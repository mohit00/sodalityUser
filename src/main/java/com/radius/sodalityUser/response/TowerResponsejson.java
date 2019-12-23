package com.radius.sodalityUser.response;

import java.util.Date;

public class TowerResponsejson {


	private String uuid;

	private String towerName;
	private String towerDescription;
	private Date createdDate;
	private Date lastModifiedDate;
	private int no_of_tower;

	public int getNo_of_tower() {
		return no_of_tower;
	}
	public void setNo_of_tower(int no_of_tower) {
		this.no_of_tower = no_of_tower;
	}
	public String getUuid() {
		return uuid;
	}
	public void setUuid(String uuid) {
		this.uuid = uuid;
	}
	public String getTowerName() {
		return towerName;
	}
	public void setTowerName(String towerName) {
		this.towerName = towerName;
	}
	public String getTowerDescription() {
		return towerDescription;
	}
	public void setTowerDescription(String towerDescription) {
		this.towerDescription = towerDescription;
	}
	public Date getCreatedDate() {
		return createdDate;
	}
	public void setCreatedDate(Date createdDate) {
		this.createdDate = createdDate;
	}
	public Date getLastModifiedDate() {
		return lastModifiedDate;
	}
	public void setLastModifiedDate(Date lastModifiedDate) {
		this.lastModifiedDate = lastModifiedDate;
	}
	
}
