package com.radius.sodalityUser.service;
import java.util.ArrayList;

import javax.json.JsonObject;

import com.radius.sodalityUser.model.Category;
public interface CategoryService {
public Category saveCategory(JsonObject requestBody);
public ArrayList<Category> getCategory(JsonObject requestBody);
public Category getCategoryDetail(String uuid);
public Category categoryUpdate(JsonObject requestBody);



}
