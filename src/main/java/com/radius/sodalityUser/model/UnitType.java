package com.radius.sodalityUser.model;

import java.util.Date;

import org.neo4j.ogm.annotation.GeneratedValue;
import org.neo4j.ogm.annotation.Id;
import org.neo4j.ogm.annotation.NodeEntity;
import org.neo4j.ogm.annotation.Relationship;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonProperty.Access;

@NodeEntity(label = "UnitType")
public class UnitType {

    @Id()
    @GeneratedValue()
    private Long id;
    private String title;
    private Integer carpetArea;
    private Integer builtUpArea;
    private Integer superBuiltUpArea;
     private Date createdDate;
    private Date lastModifiedDate;
    private String uuid;

    @Relationship(type = "UNDER", direction = Relationship.UNDIRECTED)
    public  User User;
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
	
    
	public User getUser() {
		return User;
	}
	public void setUser(User user) {
		User = user;
	}
	public String getUuid() {
		return uuid;
	}
	public void setUuid(String uuid) {
		this.uuid = uuid;
	}
	public Long getId() {
		return id;
	}
	public void setId(Long id) {
		this.id = id;
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

 
 }
