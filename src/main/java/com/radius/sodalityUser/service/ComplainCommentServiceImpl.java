package com.radius.sodalityUser.service;

import java.util.ArrayList;
import java.util.Date;

import javax.json.JsonObject;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.radius.sodalityUser.common.Commonfunction;
import com.radius.sodalityUser.model.ComplainComment;
import com.radius.sodalityUser.model.Tower;
import com.radius.sodalityUser.repository.ComplainCommentRepository;
import com.radius.sodalityUser.repository.ComplainRepository;
import com.radius.sodalityUser.repository.UserRepositoryImpl;
import com.radius.sodalityUser.response.ComplainCommentResponseJson;
import com.radius.sodalityUser.response.ComplainCommentResponseList;
import com.radius.sodalityUser.response.TowerResponseList;
import com.radius.sodalityUser.response.TowerResponsejson;

@Service
public class ComplainCommentServiceImpl implements ComplainCommentService {
	@Autowired
	Commonfunction Commonfunctionl;
	@Autowired
	ComplainCommentRepository commentRepo;
	@Autowired
	ComplainRepository complainRepo;
	@Autowired
	UserRepositoryImpl userRepo;

	@Override
	public ComplainComment addComplainComment(JsonObject requestBody) {
		// TODO Auto-generated method stub
		ComplainComment comComent = new ComplainComment();
		if (requestBody.containsKey("comment")) {
			comComent.setComment(requestBody.getString("comment"));
		}
	        

		if (requestBody.containsKey("commentByUuid")) {
 			comComent.setCommentBy(userRepo.getUser(requestBody.getString("commentByUuid")));
		}
		Date date = new Date();
		comComent.setCommentDate(date);

		comComent.setUuid((Commonfunctionl.uuIDSend()));
		if (requestBody.containsKey("status")) {
			if (requestBody.containsKey("complainUuid")) {
				complainRepo.changeStatus(requestBody.getString("complainUuid"), requestBody.getString("status"));
				comComent.setComplain(complainRepo.getComplainByUUId(requestBody.getString("complainUuid")));	
			}
		}  
		return commentRepo.save(comComent);
	}

	@Override
	public ComplainCommentResponseList getCommentList(JsonObject requestBody) {
		// TODO Auto-generated method stub
		ComplainCommentResponseList response = new ComplainCommentResponseList();
		response.setStatus(false);
		ArrayList<ComplainComment> getAllComment = commentRepo.getAllComment(requestBody.getJsonString("uuid").getString());
		ArrayList<ComplainCommentResponseJson> list = new ArrayList<ComplainCommentResponseJson>();
		if(getAllComment.size() > 0) {
 
			for(int i =0;i<getAllComment.size();i++) {
				ComplainCommentResponseJson arrayJson = new ComplainCommentResponseJson();
				arrayJson.setComment(getAllComment.get(i).getComment());
				arrayJson.setCommentBy(getAllComment.get(i).getCommentBy());
				arrayJson.setCommentUuid(getAllComment.get(i).getUuid());
				arrayJson.setComplainDate(getAllComment.get(i).getCommentDate());
				arrayJson.setComplainStatus(getAllComment.get(i).getComplain().getComplainStatus());
				if(getAllComment.get(i).getCommentBy().getUser_type().equalsIgnoreCase("Staff")) {
					arrayJson.setProfileImage(userRepo.findById(getAllComment.get(i).getCommentBy().getId()).get().getStaffDetals().getPic());	 
				}
				if(getAllComment.get(i).getCommentBy().getUser_type().equalsIgnoreCase("Society")) {
					arrayJson.setProfileImage(userRepo.findById(getAllComment.get(i).getCommentBy().getId()).get().getSocietyDetail().getSocietyLogo());	 
				}
				list.add(arrayJson);
			}
			response.setStatus(true);
			response.setMessage("Complain get Successfully");
		}
		
			response.setData(list);
			
		return response;
	}

}
