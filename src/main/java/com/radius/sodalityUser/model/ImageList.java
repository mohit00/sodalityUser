package com.radius.sodalityUser.model;

import java.util.ArrayList;

import org.neo4j.ogm.annotation.GeneratedValue;
import org.neo4j.ogm.annotation.Id;
import org.neo4j.ogm.annotation.NodeEntity;

@NodeEntity(label = "image_List")

public class ImageList {
    @Id()
    @GeneratedValue()
    private Long id;

private ArrayList<String> image;

public ArrayList<String> getImage() {
	return image;
}

public void setImage(ArrayList<String> image) {
	this.image = image;
}

public Long getId() {
	return id;
}

public void setId(Long id) {
	this.id = id;
}

}
