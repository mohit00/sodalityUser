package com.radius.sodalityUser.service;

import java.util.ArrayList;
import java.util.Date;

import javax.json.JsonObject;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.radius.sodalityUser.common.Commonfunction;
import com.radius.sodalityUser.model.Unit;
import com.radius.sodalityUser.repository.TowerRepository;
import com.radius.sodalityUser.repository.UnitRepository;
import com.radius.sodalityUser.repository.UnitTypeRepository;
import com.radius.sodalityUser.response.UnitResponseList;
import com.radius.sodalityUser.response.UnitResponsejson;

@Service
public class UnitServiceImpl implements UnitService {
	@Autowired
	UnitRepository unitRepo;
	@Autowired
	UnitTypeRepository unitTypeRepo;
	@Autowired
	TowerRepository towerRepo;
	@Autowired
	Commonfunction Commonfunctionl;

	@Override
	public Unit unitSave(JsonObject requestBody) {
		// TODO Auto-generated method stub
		Unit u = new Unit();
		if (requestBody.containsKey("unit_no")) {
			u.setUnit_no(requestBody.getJsonString("unit_no").getString());
		}
		if (requestBody.containsKey("unit_remark")) {
			u.setUnit_remark(unitTypeRepo.getUnitType(requestBody.getString("unit_remark")));
		}
		if (requestBody.containsKey("UOM")) {
			u.setUOM(requestBody.getJsonString("UOM").getString());
		}
		if (requestBody.containsKey("Description")) {
			u.setDescription(requestBody.getJsonString("Description").getString());
		}
		if (requestBody.containsKey("unit_type")) {
			u.setUnit_type(requestBody.getJsonString("unit_type").getString());
		}
		if (requestBody.containsKey("pipeGas")) {
			u.setPipeGas(requestBody.getBoolean("pipeGas"));
		}

		
		if (requestBody.containsKey("sanctionedLoadGrid")) {
			u.setSanctionedLoadGrid(requestBody.getJsonNumber("sanctionedLoadGrid").intValue());
		}
		if (requestBody.containsKey("sanctionedLoadBackUp")) {
			u.setSanctionedLoadBackUp(requestBody.getJsonNumber("sanctionedLoadBackUp").intValue());
		}
		if (requestBody.containsKey("soldStatus")) {
			u.setSoldStatus(requestBody.getJsonString("soldStatus").getString());
		}
		if (requestBody.containsKey("parentId")) {

			u.setParentAccount(towerRepo.gettower(requestBody.getJsonString("parentId").getString()));
		}
		u.setUuid(Commonfunctionl.uuIDSend());
		Date d = new Date();
		u.setCreateDate(d);
		u.setUpdateDate(d);
		unitRepo.save(u);
		return u;
	}

	@Override
	public UnitResponseList getUnitTower(JsonObject requestBody) {
		UnitResponseList responseList = new UnitResponseList();
		responseList.setStatus(false);

		if (requestBody.containsKey("parentId")) {
			ArrayList<Unit> allUnit = unitRepo.getAllUnit(requestBody.getJsonString("parentId").getString());
			ArrayList<UnitResponsejson> list = new ArrayList<UnitResponsejson>();

			for (int i = 0; i < allUnit.size(); i++) {
				UnitResponsejson response = new UnitResponsejson();

				response.setUnitDescription(allUnit.get(i).getDescription());
				response.setCreatedDate(allUnit.get(i).getCreateDate());
				response.setLastModifiedDate(allUnit.get(i).getUpdateDate());
				response.setSoldStatus(allUnit.get(i).getSoldStatus());
				response.setUnitNo(allUnit.get(i).getUnit_no());
				response.setUnitType(allUnit.get(i).getUnit_type());
				response.setUuid(allUnit.get(i).getUuid());
				list.add(response);
			}
			responseList.setMessage("get Data Successfully");
			responseList.setData(list);
		}
		// TODO Auto-generated method stub
		return responseList;

	}

	@Override
	public Unit getUnit(String uuid) {
		// TODO Auto-generated method stub

		return unitRepo.getUnit(uuid);
	}

	@Override
	public Unit unitUpdate(JsonObject requestBody) {
		// TODO Auto-generated method stub
		Unit u = new Unit();
		if (requestBody.containsKey("unit_no")) {
			u.setUnit_no(requestBody.getJsonString("unit_no").getString());
		}
		if (requestBody.containsKey("unit_remark")) {
			u.setUnit_remark(unitTypeRepo.getUnitType(requestBody.getString("unit_remark")));
		}
		if (requestBody.containsKey("UOM")) {
			u.setUOM(requestBody.getJsonString("UOM").getString());
		}
		if (requestBody.containsKey("Description")) {
			u.setDescription(requestBody.getJsonString("Description").getString());
		}
		if (requestBody.containsKey("unit_type")) {
			u.setUnit_type(requestBody.getJsonString("unit_type").getString());
		}
		
		if (requestBody.containsKey("sanctionedLoadGrid")) {
			u.setSanctionedLoadGrid(requestBody.getJsonNumber("sanctionedLoadGrid").intValue());
		}
		if (requestBody.containsKey("sanctionedLoadBackUp")) {
			u.setSanctionedLoadBackUp(requestBody.getJsonNumber("sanctionedLoadBackUp").intValue());
		}
		if (requestBody.containsKey("soldStatus")) {
			u.setSoldStatus(requestBody.getJsonString("soldStatus").getString());
		}
		if (requestBody.containsKey("parentId")) {

			u.setParentAccount(towerRepo.gettower(requestBody.getJsonString("parentId").getString()));
		}
		if (requestBody.containsKey("uuId")) {

			u.setUuid((requestBody.getJsonString("uuId").getString()));
		}
		if (requestBody.containsKey("id")) {
			u.setId(Long.parseLong(requestBody.getJsonNumber("id").toString()));
		}
		Date d = new Date();
		u.setCreateDate(d);
		u.setUpdateDate(d);
		unitRepo.save(u);
		return u;
	}

}
