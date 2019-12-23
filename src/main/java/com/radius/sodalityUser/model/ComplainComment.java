package com.radius.sodalityUser.model;

import org.neo4j.ogm.annotation.GeneratedValue;
import org.neo4j.ogm.annotation.Id;
import org.neo4j.ogm.annotation.Relationship;


public class ComplainComment {
	@Id()
	@GeneratedValue()
    private Long id;

private String comment;
 
public Long getId() {
	return id;
}
public void setId(Long id) {
	this.id = id;
}
public String getComment() {
	return comment;
}
public void setComment(String comment) {
	this.comment = comment;
}
 

}
