package com.radius.sodalityUser.service;

import java.util.ArrayList;
import java.util.Date;

import javax.json.JsonObject;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.CrossOrigin;

import com.radius.sodalityUser.common.Commonfunction;
import com.radius.sodalityUser.model.UnitType;
import com.radius.sodalityUser.model.User;
import com.radius.sodalityUser.repository.UnitTypeRepository;
import com.radius.sodalityUser.repository.UserRepositoryImpl;
import com.radius.sodalityUser.response.UnitTypeResponseJson;
import com.radius.sodalityUser.response.UnitTypeResponseList;

@Service
@CrossOrigin
public class UnitTypeServiceImpl implements UnitTypeService {
	@Autowired
	UnitTypeRepository unitTypeRepo;
	@Autowired
	Commonfunction Commonfunctionl;
	@Autowired
	UserRepositoryImpl userRepo;

	@Override
	public UnitType unitSave(JsonObject requestBody) {

		UnitType type = new UnitType();
		// TODO Auto-generated method stub
		if (requestBody.containsKey("id")) {
			type.setId(Long.parseLong(requestBody.getJsonNumber("id").toString()));
		}
		if (requestBody.containsKey("title")) {

			type.setTitle(requestBody.getString("title"));

		}
		if (requestBody.containsKey("carpetArea")) {
			if (requestBody.isNull("carpetArea")) {
				type.setCarpetArea(requestBody.getInt("carpetArea"));
			} else {
				type.setCarpetArea(requestBody.getInt("superBuiltUpArea"));
			}
		}
		User byId = userRepo.getByuuid((requestBody.getString("parent_id")));
		type.setUser(byId);
		if (requestBody.containsKey("builtUpArea")) {
			if (requestBody.isNull("builtUpArea")) {
			} else {
				type.setBuiltUpArea(requestBody.getInt("builtUpArea"));
			}
		}
		if (requestBody.containsKey("superBuiltUpArea")) {
			if (requestBody.isNull("superBuiltUpArea")) {
			} else {
				type.setSuperBuiltUpArea(requestBody.getInt("superBuiltUpArea"));
			}
		}
		if (requestBody.containsKey("uuid")) {
			type.setUuid(requestBody.getString("uuid"));
		} else {
			type.setUuid(Commonfunctionl.uuIDSend());
		}
		Date d = new Date();
		type.setCreatedDate(d);
		type.setLastModifiedDate(d);
		unitTypeRepo.save(type);
		return type;
	}

	@Override
	public UnitTypeResponseList getUnitTypeList(JsonObject requestBody) {
		// TODO Auto-generated method stub
		UnitTypeResponseList list = new UnitTypeResponseList();
		ArrayList<UnitType> type = unitTypeRepo.getAllUnitType(requestBody.getString("parentId"));
		ArrayList<UnitTypeResponseJson> list2 = new ArrayList<UnitTypeResponseJson>();
		for (int i = 0; i < type.size(); i++) {
			UnitTypeResponseJson response = new UnitTypeResponseJson();

			response.setTitle(type.get(i).getTitle());
			response.setBuiltUpArea(type.get(i).getBuiltUpArea());
			response.setCarpetArea(type.get(i).getCarpetArea());
			response.setSuperBuiltUpArea(type.get(i).getSuperBuiltUpArea());
			response.setUuid(type.get(i).getUuid());
			response.setCreatedDate(type.get(i).getCreatedDate());
			list2.add(response);
		}
		list.setData(list2);
		list.setStatus(true);
		list.setMessage("Data Get Successfully");
		return list;
	}

	@Override
	public UnitType getUnitType(String uuid) {
		// TODO Auto-generated method stub

		return unitTypeRepo.getUnitType(uuid);
	}
}