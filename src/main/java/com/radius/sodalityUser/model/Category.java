package com.radius.sodalityUser.model;

import org.neo4j.ogm.annotation.GeneratedValue;
import org.neo4j.ogm.annotation.Id;
import org.neo4j.ogm.annotation.NodeEntity;
import org.neo4j.ogm.annotation.Property;
import org.neo4j.ogm.annotation.Relationship;

@NodeEntity(label = "Category")
public class Category {
	   @Id()
	    @GeneratedValue()
	    private Long id;
	    @Property(name = "uuid")
	    private String uuid;
	    private String title;
	    @Relationship(type = "UNDER", direction = Relationship.UNDIRECTED)
	    public User parrentAccount;
	    
		public User getParrentAccount() {
			return parrentAccount;
		}
		public void setParrentAccount(User parrentAccount) {
			this.parrentAccount = parrentAccount;
		}
		public Long getId() {
			return id;
		}
		public void setId(Long id) {
			this.id = id;
		}
		public String getUuid() {
			return uuid;
		}
		public void setUuid(String uuid) {
			this.uuid = uuid;
		}
		public String getTitle() {
			return title;
		}
		public void setTitle(String title) {
			this.title = title;
		}
	    
}
