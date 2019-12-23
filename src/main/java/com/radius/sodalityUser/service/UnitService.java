package com.radius.sodalityUser.service;

import javax.json.JsonObject;

import com.radius.sodalityUser.model.Unit;
import com.radius.sodalityUser.response.UnitResponseList;

public interface UnitService {
	public Unit unitSave(JsonObject requestBody);
	public UnitResponseList getUnitTower(JsonObject requestBody);
	public Unit getUnit(String uuid);
	public Unit unitUpdate(JsonObject requestBody);

}
