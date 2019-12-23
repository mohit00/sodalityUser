package com.radius.sodalityUser.model;

import java.util.Date;

import org.neo4j.ogm.annotation.GeneratedValue;
import org.neo4j.ogm.annotation.Id;
import org.neo4j.ogm.annotation.NodeEntity;
import org.neo4j.ogm.annotation.Relationship;

@NodeEntity(label = "Tower")

public class Tower {
    @Id()
    @GeneratedValue()
    private Long id;

 private String name;
 private String Description;
 private int no_of_tower;
 private Date  createDate;
 private Date  updateDate;
private String uuid;
 @Relationship(type = "UNDER", direction = Relationship.UNDIRECTED)
 public User parrentAccount;

public User getParrentAccount() {
	return parrentAccount;
}

public void setParrentAccount(User parrentAccount) {
	this.parrentAccount = parrentAccount;
}

public String getName() {
	return name;
}

public void setName(String name) {
	this.name = name;
}

public String getDescription() {
	return Description;
}

public void setDescription(String description) {
	Description = description;
}

public int getNo_of_tower() {
	return no_of_tower;
}

public void setNo_of_tower(int no_of_tower) {
	this.no_of_tower = no_of_tower;
}

public Long getId() {
	return id;
}

public void setId(Long id) {
	this.id = id;
}

public Date getCreateDate() {
	return createDate;
}

public void setCreateDate(Date createDate) {
	this.createDate = createDate;
}

public Date getUpdateDate() {
	return updateDate;
}

public void setUpdateDate(Date updateDate) {
	this.updateDate = updateDate;
}

public String getUuid() {
	return uuid;
}

public void setUuid(String uuid) {
	this.uuid = uuid;
}

}
