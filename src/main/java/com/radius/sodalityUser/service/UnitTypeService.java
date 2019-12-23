package com.radius.sodalityUser.service;

import javax.json.JsonObject;

import com.radius.sodalityUser.model.UnitType;
import com.radius.sodalityUser.response.UnitTypeResponseList;

public interface UnitTypeService {
	public UnitType unitSave(JsonObject requestBody);
	public UnitTypeResponseList getUnitTypeList(JsonObject requestBody);
	public UnitType getUnitType(String uuid);

}
