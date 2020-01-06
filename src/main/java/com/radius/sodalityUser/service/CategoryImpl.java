package com.radius.sodalityUser.service;

import java.util.ArrayList;

import javax.json.JsonObject;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.radius.sodalityUser.common.Commonfunction;
import com.radius.sodalityUser.model.Category;
import com.radius.sodalityUser.repository.CategoryRepository;
import com.radius.sodalityUser.repository.UserRepositoryImpl;
@Service
public class CategoryImpl implements CategoryService {
	@Autowired
	Commonfunction Commonfunctionl;
	@Autowired
	CategoryRepository catRepo;
	@Autowired
	UserRepositoryImpl userRepo;
	@Override
	public Category saveCategory(JsonObject requestBody) {
		// TODO Auto-generated method stub

		Category c = new Category();
		if (requestBody.containsKey("title")) {
			c.setTitle(requestBody.getString("title")); 
			}
		 
		if (requestBody.containsKey("parentId")) {
			c.setParrentAccount(userRepo.getById(Long.parseLong(requestBody.getJsonNumber("parentId").toString())));
		}

		c.setUuid((Commonfunctionl.uuIDSend()));
		catRepo.save(c);
		return c;
	}
	@Override
	public ArrayList<Category> getCategory(JsonObject requestBody) {
		// TODO Auto-generated method stub
		ArrayList<Category> list = new ArrayList<Category>();
		if (requestBody.containsKey("parentId")) {
			list = catRepo.getAllCategory(Long.parseLong(requestBody.getJsonNumber("parentId").toString())); 
			}
		return list;
	}
	@Override
	public Category getCategoryDetail(String uuid) {
		// TODO Auto-generated method stub
		return catRepo.getCategoryDetail(uuid);
	}
	@Override
	public Category categoryUpdate(JsonObject requestBody) {
		// TODO Auto-generated method stub

		Category c = new Category();
		if (requestBody.containsKey("uuid")) {
			if(requestBody.getString("uuid")!=null) {
				c.setUuid(requestBody.getString("uuid")); 

			}else {
				c.setUuid(Commonfunctionl.uuIDSend());

			}
			}else {
				c.setUuid(Commonfunctionl.uuIDSend());
			}
		if (requestBody.containsKey("id")) {
			c.setId(Long.parseLong(requestBody.getJsonNumber("id").toString())); 
			}
		if (requestBody.containsKey("title")) {
			c.setTitle(requestBody.getString("title")); 
			} 
		if (requestBody.containsKey("parentId")) {
			c.setParrentAccount(userRepo.getById(Long.parseLong(requestBody.getJsonNumber("parentId").toString())));
		}

 		catRepo.save(c);
		return c;
	}
	@Override
	public ArrayList<Category> getCategoryByResidentIId(JsonObject requestBody) {
		// TODO Auto-generated method stub
		ArrayList<Category> list = new ArrayList<Category>();
		if (requestBody.containsKey("parentId")) {
			 System.out.println(userRepo.getParentUuid(requestBody.getString("parentId")).getId());
			list = catRepo.getAllCategory(userRepo.getParentUuid(requestBody.getString("parentId")).getId()); 
			}
		return list;
	}
	 
}
