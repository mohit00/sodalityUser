package com.radius.sodalityUser.model;

import java.util.ArrayList;
import java.util.Date;
import java.util.Set;
import org.neo4j.ogm.annotation.GeneratedValue;
import org.neo4j.ogm.annotation.Id;
import org.neo4j.ogm.annotation.NodeEntity;
import org.neo4j.ogm.annotation.Relationship;
import com.radius.sodalityUser.model.UserType.noticeType;

@NodeEntity(label = "Notice")
public class Notice {
	@Id()
	@GeneratedValue()
	private Long id;
	private String title;
	private String Description;
	private ArrayList<String> image;
	private String noticeTo;
	private Date createdDate;
	private Date updatedDate;
	private String uuid;
	private noticeType type;

	@Relationship(type = "UNDER", direction = Relationship.OUTGOING)
	public User parrentAccount;
	@Relationship(type = "DISCCUSION_BY", direction = Relationship.OUTGOING)
	public User disccusionBy;
	
	public User getDisccusionBy() {
		return disccusionBy;
	}

	public void setDisccusionBy(User disccusionBy) {
		this.disccusionBy = disccusionBy;
	}

	public Date getCreatedDate() {
		return createdDate;
	}

	public void setCreatedDate(Date createdDate) {
		this.createdDate = createdDate;
	}

	public Date getUpdatedDate() {
		return updatedDate;
	}

	public void setUpdatedDate(Date updatedDate) {
		this.updatedDate = updatedDate;
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

	public noticeType getType() {
		return type;
	}

	public void setType(noticeType type) {
		this.type = type;
	}

	public void setTitle(String title) {
		this.title = title;
	}

	public String getDescription() {
		return Description;
	}

	public void setDescription(String description) {
		Description = description;
	}

	public ArrayList<String> getImage() {
		return image;
	}

	 
	public String getNoticeTo() {
		return noticeTo;
	}

	public void setImage(ArrayList<String> image) {
		this.image = image;
	}

	public void setNoticeTo(String noticeTo) {
		this.noticeTo = noticeTo;
	}
 

	public String getUuid() {
		return uuid;
	}

	public void setUuid(String uuid) {
		this.uuid = uuid;
	}

	public User getParrentAccount() {
		return parrentAccount;
	}

	public void setParrentAccount(User parrentAccount) {
		this.parrentAccount = parrentAccount;
	}

}
