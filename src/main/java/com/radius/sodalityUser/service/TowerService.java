package com.radius.sodalityUser.service;

import javax.json.JsonObject;

import com.radius.sodalityUser.model.Tower;
import com.radius.sodalityUser.response.TowerResponseList;

public interface TowerService {
	public Tower saveTower(JsonObject requestBody);
public TowerResponseList getTowerList(JsonObject requestBody);
public Tower getTower(String uuid);
public Tower updateTower(JsonObject requestBody);

}
