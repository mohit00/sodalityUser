package com.radius.sodalityUser.service;

import java.io.IOException;
import java.util.Arrays;
import java.util.ArrayList;

import java.util.Date;
import java.util.HashSet;
import java.util.Set;
import java.util.stream.Collectors;

import javax.json.JsonObject;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import com.radius.sodalityUser.common.Commonfunction;
import com.radius.sodalityUser.model.Complain;
import com.radius.sodalityUser.model.User;
import com.radius.sodalityUser.model.UserType;
import com.radius.sodalityUser.repository.ComplainRepository;
import com.radius.sodalityUser.repository.UnitRepository;
import com.radius.sodalityUser.repository.UserRepositoryImpl;
import com.radius.sodalityUser.response.ComplainResponseJson;
import com.radius.sodalityUser.response.ComplainResponseList;
import com.radius.sodalityUser.response.CustomResponse;

import io.micrometer.core.instrument.util.StringUtils;

@Service
public class ComplainImp implements ComplainService {
	@Autowired
	UserRepositoryImpl userRepo;
	@Autowired
	Commonfunction fun;
	@Autowired
	UnitRepository unitRepo;
	@Autowired
	ComplainRepository complainRepo;

	@Override
	public Complain saveComplain(MultipartFile[] uploadfiles, JsonObject requestBody) {
		// TODO Auto-generated method stub
		Complain c = new Complain();
		if (requestBody.containsKey("title")) {
			c.setTitle(requestBody.getString("title"));
		}
		if (requestBody.containsKey("description")) {
			c.setDescription(requestBody.getString("description"));
		}
		if (requestBody.containsKey("category")) {
			c.setCategory(requestBody.getString("category"));
		}
		Date date = new Date();

		c.setCreatedDate(date);
		c.setUpdatedDate(date);

		c.setUuid(fun.uuIDSend());
		c.setComplainStatus(UserType.complainStatus.New.toString());
		String ImagesListfileName = Arrays.stream(uploadfiles).map(x -> {
			return x.getOriginalFilename();
		}).filter(x -> !StringUtils.isEmpty(x)).collect(Collectors.joining(" , "));
		if (StringUtils.isEmpty(ImagesListfileName)) {
		} else {
			try {
				Set<String> list = new HashSet<String>();
				for (int i = 0; i < fun.saveUploadedFiles(Arrays.asList(uploadfiles)).size(); i++) {
					list.add(fun.saveUploadedFiles(Arrays.asList(uploadfiles)).get(i));
				}
				c.setImages(list);
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			if (requestBody.containsKey("useruuId")) {
				User u = userRepo.getResidentDetail(requestBody.getString("useruuId"));
				c.setResident(u);
			}
			if (requestBody.containsKey("parentuuId")) {
				User u = userRepo.getByuuid(requestBody.getString("parentuuId"));
				c.setAssignedBy(u);
			}
			complainRepo.save(c);
		}
		return c;
	}
	@Override
	public Complain assginTo(JsonObject requestBody) {
		// TODO Auto-generated method stub
		Complain c = new Complain();
		if (requestBody.containsKey("complainId")) {
			c = complainRepo.getComplainByUUId(requestBody.getString("complainId"));
		}
		c.setOTP(fun.setotp(c.getId()));
		Date date = new Date();
		c.setAssignedDate(date);
		c.setUpdatedDate(date);
		c.setComplainStatus(UserType.complainStatus.Assigned.toString());

		if (requestBody.containsKey("assignToId")) {
			Set<User> s = new HashSet<User>();
			for (int i = 0; i < requestBody.getJsonArray("assignToId").size(); i++) {
				s.add(userRepo.getStaffDetail(requestBody.getJsonArray("assignToId").getString(i)));
			}
 			c.setAssignedTo(s);
		}
		return complainRepo.save(c);
	}
	@Override
	public CustomResponse statusChange(JsonObject requestBody) {
		// TODO Auto-generated method stub
		Complain c = new Complain();

		if (requestBody.containsKey("complainId")) {
			c = complainRepo.getComplainByUUId(requestBody.getString("complainId"));
			if (requestBody.containsKey("Status")) {

			c.setComplainStatus( requestBody.getString("Status"));
			}
		}
		complainRepo.save(c);
		CustomResponse response = new CustomResponse();
		response.setStatus(true);
		response.setMessage("Status successfully changed");

 		return response;
	}
	@Override
	public ComplainResponseList getComplainByCategory(JsonObject requestBody) {
		// TODO Auto-generated method stub
		ComplainResponseList response = new ComplainResponseList();
		ArrayList<Complain> list = new ArrayList<Complain>();
		if(requestBody.containsKey("parentId") && requestBody.containsKey("category")) {
			list=   complainRepo.getComplainByCategory(requestBody.getString("parentId"),requestBody.getString("category"));

		}
		response.setStatus(false);
		response.setMessage("No data found");

		if(list.size() > 0) {
			response.setStatus(true);
			response.setMessage("data found");

			ArrayList<ComplainResponseJson> listJson = new ArrayList<ComplainResponseJson>();
			for(int i=0;i<list.size();i++) {
			
				ComplainResponseJson json = new ComplainResponseJson();
				json.setAssignedDate(list.get(i).getAssignedDate());
				json.setAssignedTo (list.get(i).getAssignedTo());
				json.setComplainTitle(list.get(i).getTitle());
				json.setComplainUuid (list.get(i).getUuid());
				json.setCreateDate(list.get(i).getCreatedDate());
				json.setUpdatedDate (list.get(i).getUpdatedDate());
				listJson.add(json);
			}
			response.setData(listJson);

		}
		return response;
	}
	@Override
	public ComplainResponseList getComplainList(JsonObject requestBody) { 
 		// TODO Auto-generated method stub
		ComplainResponseList response = new ComplainResponseList();
		ArrayList<Complain> list = new ArrayList<Complain>();
		if(requestBody.containsKey("parentId") ) {
			list=   complainRepo.getComplainList(requestBody.getString("parentId"));

		}
		response.setStatus(false);
		response.setMessage("No data found");

		if(list.size() > 0) {
			response.setStatus(true);
			response.setMessage("data found");

			ArrayList<ComplainResponseJson> listJson = new ArrayList<ComplainResponseJson>();
			for(int i=0;i<list.size();i++) {
			
				ComplainResponseJson json = new ComplainResponseJson();
				json.setAssignedDate(list.get(i).getAssignedDate());
				json.setAssignedTo (list.get(i).getAssignedTo());
				json.setComplainTitle(list.get(i).getTitle());
				json.setComplainUuid (list.get(i).getUuid());
				json.setCreateDate(list.get(i).getCreatedDate());
				json.setUpdatedDate (list.get(i).getUpdatedDate());
				listJson.add(json);
			}
			response.setData(listJson);

		}
		return response;
	} 
	
}