package com.radius.sodalityUser.service;

import java.util.ArrayList;
import java.util.Date;

import javax.json.JsonObject;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.radius.sodalityUser.common.Commonfunction;
import com.radius.sodalityUser.model.User;
import com.radius.sodalityUser.model.UserType;
import com.radius.sodalityUser.model.notificationComment;
import com.radius.sodalityUser.repository.NoticeRepository;
import com.radius.sodalityUser.repository.NotificationCommentRepository;
import com.radius.sodalityUser.repository.UserRepositoryImpl;

@Service
public class NotificationCommentServiceImpl implements NotificationCommentService {
	@Autowired
	Commonfunction fun;
	@Autowired
	NotificationCommentRepository notiRepo;
	@Autowired
	UserRepositoryImpl userRepo;
	@Autowired
	NoticeRepository noticeRepo;

	@Override
	public notificationComment addComment(JsonObject requestBody) {
		// TODO Auto-generated method stub
		notificationComment comment = new notificationComment();
		if (requestBody.containsKey("description")) {
			comment.setDescription(requestBody.getString("description"));
		}
		User user = null;

		if (requestBody.containsKey("uuid")) {
			user = userRepo.getUser(requestBody.getString("uuid"));
			comment.setCommentBy(user);
			comment.setCommentUnder(noticeRepo.getNoticeDetail(requestBody.getString("noticeUuid")));

		}
		comment.setUuid(fun.uuIDSend());
		Date d = new Date();
		comment.setCreatedDate(d);
		comment.setUpdatedDate(d);
		return notiRepo.save(comment);

	}

	@Override
	public notificationComment UpdateComment(JsonObject requestBody) {
		notificationComment notificationComment = notiRepo
				.findById(Long.parseLong(requestBody.getJsonNumber("id").toString())).get();
		if (requestBody.containsKey("description")) {
			notificationComment.setDescription(requestBody.getString("description"));
		}
		Date d = new Date();
		notificationComment.setUpdatedDate(d);

		// TODO Auto-generated method stub
		return notiRepo.save(notificationComment);
	}

	@Override
	public ArrayList<notificationComment> getAllComment(JsonObject requestBody) {
		// TODO Auto-generated method stub

		return notiRepo.getNoticeCommentList(requestBody.getString("notificationuuid"));

	}

	@Override
	public notificationComment getComment(String uuid) {
		// TODO Auto-generated method stub
		return null;
	}

}
