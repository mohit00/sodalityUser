package com.radius.sodalityUser.service;
import java.util.ArrayList;
import java.util.Date;

import javax.json.JsonObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.radius.sodalityUser.common.Commonfunction;
import com.radius.sodalityUser.model.Tower;
import com.radius.sodalityUser.model.User;
import com.radius.sodalityUser.repository.TowerRepository;
import com.radius.sodalityUser.repository.UserRepositoryImpl;
import com.radius.sodalityUser.response.TowerResponseList;
import com.radius.sodalityUser.response.TowerResponsejson;
@Service
public class TowerServiceImpl implements TowerService{
	@Autowired
	UserRepositoryImpl userRepo;
	
	@Autowired
	Commonfunction comfun;
	
	@Autowired
	TowerRepository towerRepo;
	
	public Tower saveTower(JsonObject requestBody) {
		// TODO Auto-generated method stub
		Tower t = new Tower();
		if (requestBody.containsKey("name")) {
			t.setName(requestBody.getJsonString("name").getString());
		}
		if (requestBody.containsKey("no_of_tower")) {
			t.setNo_of_tower((requestBody.getInt("no_of_tower")));
		}
		if (requestBody.containsKey("description")) {
			t.setDescription(requestBody.getJsonString("description").getString());
		}
		
		User byId = userRepo.getById(Long.parseLong(requestBody.getJsonNumber("parent_id").toString()));
		t.setUuid(comfun.uuIDSend());
		t.setParrentAccount(byId);
		Date d = new Date();
		t.setCreateDate(d);
		t.setUpdateDate(d);
		towerRepo.save(t);
 		return t;
	}

	@Override
	public TowerResponseList getTowerList(JsonObject requestBody) {
		// TODO Auto-generated method stub
		TowerResponseList response = new TowerResponseList();
		response.setStatus(false);
		ArrayList<Tower> allTower = towerRepo.getAllTower(requestBody.getJsonString("uuid").getString());
		ArrayList<TowerResponsejson> list = new ArrayList<TowerResponsejson>();
		if(allTower.size() > 0) {
 
			for(int i =0;i<allTower.size();i++) {
				TowerResponsejson arrayJson = new TowerResponsejson();

				arrayJson.setUuid(allTower.get(i).getUuid());
				arrayJson.setTowerName(allTower.get(i).getName());
				arrayJson.setTowerDescription(allTower.get(i).getDescription());
				arrayJson.setCreatedDate(allTower.get(i).getCreateDate());
				arrayJson.setLastModifiedDate(allTower.get(i).getUpdateDate());
				arrayJson.setNo_of_tower( allTower.get(i).getNo_of_tower());
				list.add(arrayJson);
			}
			response.setStatus(true);
			response.setMessage("Tower get Successfully");
		}
		
			response.setData(list);
			
		return response;
	}

	@Override
	public Tower getTower(String uuid) {
		// TODO Auto-generated method stub
		return towerRepo.gettower(uuid);
	}

	@Override
	public Tower updateTower(JsonObject requestBody) {
		// TODO Auto-generated method stub
		Tower t = new Tower();
		if (requestBody.containsKey("name")) {
			t.setName(requestBody.getJsonString("name").getString());
		}
		if (requestBody.containsKey("no_of_tower")) {
			t.setNo_of_tower((requestBody.getInt("no_of_tower")));
		}
		if (requestBody.containsKey("description")) {
			t.setDescription(requestBody.getJsonString("description").getString());
		}
		if (requestBody.containsKey("id")) {
			t.setId(Long.parseLong(requestBody.getJsonNumber("id").toString()));
		}
		User byId = userRepo.getById(Long.parseLong(requestBody.getJsonNumber("parent_id").toString()));
		if (requestBody.containsKey("uuId")) {
 			t.setUuid(requestBody.getJsonString("uuId").getString());

 		}
		t.setParrentAccount(byId);
		Date d = new Date();
		t.setCreateDate(d);
		t.setUpdateDate(d);
		towerRepo.save(t);
 		return t;
	}
	
}
