package com.radius.sodalityUser.service;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.HashSet;
import java.util.Set;
import java.util.stream.Collectors;

import javax.json.JsonObject;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import com.radius.sodalityUser.common.Commonfunction;
import com.radius.sodalityUser.model.Notice;
import com.radius.sodalityUser.model.User;
import com.radius.sodalityUser.model.UserType.noticeType;
import com.radius.sodalityUser.repository.NoticeRepository;
import com.radius.sodalityUser.repository.UserRepositoryImpl;
import com.radius.sodalityUser.response.NoticeResponseJson;
import com.radius.sodalityUser.response.NoticeResponseList;
import com.radius.sodalityUser.response.SocietyJson;

import io.micrometer.core.instrument.util.StringUtils;

@Service
public class NoticeServiceImpl implements NoticeService {
	@Autowired
	Commonfunction fun;
	@Autowired
	NoticeRepository noticeRepo;
	@Autowired
	UserRepositoryImpl userRepo;

	@Override
	public Notice addNotice(MultipartFile[] uploadfiles, JsonObject requestBody) {
		// TODO Auto-generated method stub
		Notice notice = new Notice();
		if (requestBody.containsKey("title")) {
			notice.setTitle(requestBody.getString("title"));
		}
		if (requestBody.containsKey("description")) {
			notice.setDescription(requestBody.getString("description"));
		}
		if (requestBody.containsKey("noticeTo")) {
			notice.setNoticeTo(requestBody.getString("noticeTo"));
		}
		Date d = new Date();
		notice.setCreatedDate(d);
		notice.setUpdatedDate(d);
		notice.setUuid(fun.uuIDSend());
		notice.setType(noticeType.notice);

		notice.setParrentAccount(userRepo.findById(Long.parseLong(requestBody.getJsonNumber("id").toString())).get());

		String ImagesListfileName = Arrays.stream(uploadfiles).map(x -> {
			return x.getOriginalFilename();
		}).filter(x -> !StringUtils.isEmpty(x)).collect(Collectors.joining(" , "));

		ArrayList<String> listimage = new ArrayList<String>();
		ArrayList setData = null;
		if (StringUtils.isEmpty(ImagesListfileName)) {
		} else {
			try {
				listimage = fun.saveUploadedFiles(Arrays.asList(uploadfiles));
				setData = (listimage);

			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}

		notice.setImage(setData);
		noticeRepo.save(notice);
		return notice;

	}

	@Override
	public Notice updateNotice(MultipartFile[] uploadfiles, JsonObject requestBody) {
		// TODO Auto-generated method stub
		String path = "/images/";

		Notice notice = new Notice();
		notice = noticeRepo.findById(Long.parseLong(requestBody.getJsonNumber("noticeId").toString())).get();
		Date d = new Date();
		notice.setUpdatedDate(d);
		if (requestBody.containsKey("title")) {
			notice.setTitle(requestBody.getString("title"));
		}
		if (requestBody.containsKey("description")) {
			notice.setDescription(requestBody.getString("description"));
		}
		if (requestBody.containsKey("noticeTo")) {
			notice.setNoticeTo(requestBody.getString("noticeTo"));
		}

		String ImagesListfileName = Arrays.stream(uploadfiles).map(x -> {
			return x.getOriginalFilename();
		}).filter(x -> !StringUtils.isEmpty(x)).collect(Collectors.joining(" , "));

		ArrayList<String> listimage = new ArrayList<String>();
		if (StringUtils.isEmpty(ImagesListfileName)) {
		} else {
			try {
				listimage = fun.saveUploadedFiles(Arrays.asList(uploadfiles));

			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		if (requestBody.containsKey("images")) {
			for (int i = 0; i < requestBody.getJsonArray("images").size(); i++) {
				System.out.println(path + requestBody.getJsonArray("images").getString(i));
				String Newpath = path + requestBody.getJsonArray("images").getString(i);
				System.out.println(Newpath);
				listimage.add(Newpath);

			}

		}

		notice.setImage(listimage);
		noticeRepo.save(notice);
		return notice;
	}

	@Override
	public NoticeResponseList getAllNotice(JsonObject requestBody) {
		// TODO Auto-generated method stub
		String type = "";
		if (requestBody.containsKey("type")) {
			type = requestBody.getString("type");
		} else {
			type = "";
		}
		ArrayList<Notice> NoticeList;
		NoticeResponseList jsonResponse = new NoticeResponseList();
		if (type.isEmpty()) {

			NoticeList = noticeRepo.getAllNotice(requestBody.getString("parentUUid"));

		} else {
			NoticeList = noticeRepo.getAllNotice(requestBody.getString("parentUUid"));

		}
		ArrayList<NoticeResponseJson> list = new ArrayList();
		jsonResponse.setStatus(false);

		for (int counter = 0; counter < NoticeList.size(); counter++) {
			NoticeResponseJson json = new NoticeResponseJson();

			json.setTitle(NoticeList.get(counter).getTitle());
			json.setType(NoticeList.get(counter).getType());
			json.setCreatedDate(NoticeList.get(counter).getCreatedDate());
			json.setDescription(NoticeList.get(counter).getDescription());
			json.setUpdatedDate(NoticeList.get(counter).getUpdatedDate());
			json.setUuid(NoticeList.get(counter).getUuid());
			list.add(json);
			jsonResponse.setStatus(true);
		}
		jsonResponse.setData(list);

		return jsonResponse;
	}

	@Override
	public Notice getNotice(String uuid) {
		// TODO Auto-generated method stub
		return noticeRepo.getNoticeDetail(uuid);
	}

	@Override
	public NoticeResponseList getAllNotification(JsonObject returnJsonObject) {
		// TODO Auto-generated method stub
		ArrayList<Notice> NoticeList;
		NoticeResponseList jsonResponse = new NoticeResponseList();
		NoticeList = noticeRepo.getAllNotification(returnJsonObject.getString("parentUUid"));

		ArrayList<NoticeResponseJson> list = new ArrayList<NoticeResponseJson>();
		jsonResponse.setStatus(false);

		for (int counter = 0; counter < NoticeList.size(); counter++) {
			NoticeResponseJson json = new NoticeResponseJson();

			json.setTitle(NoticeList.get(counter).getTitle());
			json.setType(NoticeList.get(counter).getType());
			json.setCreatedDate(NoticeList.get(counter).getCreatedDate());
			json.setDescription(NoticeList.get(counter).getDescription());
			json.setUpdatedDate(NoticeList.get(counter).getUpdatedDate());
			json.setUuid(NoticeList.get(counter).getUuid());
			json.setType(NoticeList.get(counter).getType());
			json.setImagelist(NoticeList.get(counter).getImage());
			json.setUserImage(NoticeList.get(counter).getParrentAccount().getSocietyDetail().getSocietyLogo());
			json.setUserName(NoticeList.get(counter).getParrentAccount().getSocietyDetail().getSocietyDisplayName());
			json.setNoticeTo(NoticeList.get(counter).getNoticeTo());
			if (NoticeList.get(counter).getDisccusionBy() != null) {
				json.setDiscussBy(NoticeList.get(counter).getDisccusionBy().getUuid());
				json.setUserImage(NoticeList.get(counter).getDisccusionBy().getResidentDetail().getProfileImage());
				json.setUserName(NoticeList.get(counter).getDisccusionBy().getResidentDetail().getFirstName());
			}
			list.add(json);
			jsonResponse.setStatus(true);
		}
		jsonResponse.setData(list);

		return jsonResponse;
	}

	@Override
	public Notice addDisccusion(MultipartFile[] uploadfiles, JsonObject requestBody) {
		// TODO Auto-generated method stub
		Notice notice = new Notice();
		if (requestBody.containsKey("title")) {
			notice.setTitle(requestBody.getString("title"));
		}
		if (requestBody.containsKey("description")) {
			notice.setDescription(requestBody.getString("description"));
		}
		if (requestBody.containsKey("noticeTo")) {
			notice.setNoticeTo(requestBody.getString("noticeTo"));
		}
		Date d = new Date();
		notice.setCreatedDate(d);
		notice.setUpdatedDate(d);
		notice.setUuid(fun.uuIDSend());
		notice.setType(noticeType.discussion);

		notice.setParrentAccount(userRepo.getParentUuid(requestBody.getString("uuid")));
		notice.setDisccusionBy(userRepo.getUser(requestBody.getString("uuid")));
		String ImagesListfileName = Arrays.stream(uploadfiles).map(x -> {
			return x.getOriginalFilename();
		}).filter(x -> !StringUtils.isEmpty(x)).collect(Collectors.joining(" , "));

		ArrayList<String> listimage = new ArrayList<String>();
		ArrayList setData = null;
		if (StringUtils.isEmpty(ImagesListfileName)) {
		} else {
			try {
				listimage = fun.saveUploadedFiles(Arrays.asList(uploadfiles));
				setData = (listimage);

			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}

		notice.setImage(setData);
		noticeRepo.save(notice);
		return notice;

	}

	@Override
	public Notice updateDisccusion(MultipartFile[] uploadfiles, JsonObject requestBody) {
		// TODO Auto-generated method stub
		String path = "/images/";

		Notice notice = null;
		notice = noticeRepo.findById(Long.parseLong(requestBody.getJsonNumber("noticeId").toString())).get();
		Date d = new Date();
		notice.setUpdatedDate(d);
		if (requestBody.containsKey("title")) {
			notice.setTitle(requestBody.getString("title"));
		}
		if (requestBody.containsKey("description")) {
			notice.setDescription(requestBody.getString("description"));
		}
		if (requestBody.containsKey("noticeTo")) {
			notice.setNoticeTo(requestBody.getString("noticeTo"));
		}

		String ImagesListfileName = Arrays.stream(uploadfiles).map(x -> {
			return x.getOriginalFilename();
		}).filter(x -> !StringUtils.isEmpty(x)).collect(Collectors.joining(" , "));

		ArrayList<String> listimage = new ArrayList<String>();
		if (StringUtils.isEmpty(ImagesListfileName)) {
		} else {
			try {
				listimage = fun.saveUploadedFiles(Arrays.asList(uploadfiles));

			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		if (requestBody.containsKey("images")) {
			for (int i = 0; i < requestBody.getJsonArray("images").size(); i++) {
				System.out.println(path + requestBody.getJsonArray("images").getString(i));
				String Newpath = path + requestBody.getJsonArray("images").getString(i);
				System.out.println(Newpath);
				listimage.add(Newpath);

			}

		}

		notice.setImage(listimage);
		noticeRepo.save(notice);
		return notice;
	}

	@Override
	public NoticeResponseList getAllDisccusion(JsonObject requestBody) {
		// TODO Auto-generated method stub
		String type = "";
		if (requestBody.containsKey("type")) {
			type = requestBody.getString("type");
		} else {
			type = "";
		}
		ArrayList<Notice> NoticeList;
		NoticeResponseList jsonResponse = new NoticeResponseList();
		if (type.isEmpty()) {

			NoticeList = noticeRepo.getAllDisccusion(requestBody.getString("parentUUid"));

		} else {
			NoticeList = noticeRepo.getAllDisccusion(requestBody.getString("parentUUid"));

		}
		ArrayList<NoticeResponseJson> list = new ArrayList();
		jsonResponse.setStatus(false);

		for (int counter = 0; counter < NoticeList.size(); counter++) {
			NoticeResponseJson json = new NoticeResponseJson();

			json.setTitle(NoticeList.get(counter).getTitle());
			json.setType(NoticeList.get(counter).getType());
			json.setCreatedDate(NoticeList.get(counter).getCreatedDate());
			json.setDescription(NoticeList.get(counter).getDescription());
			json.setUpdatedDate(NoticeList.get(counter).getUpdatedDate());
			json.setUuid(NoticeList.get(counter).getUuid());
			list.add(json);
			jsonResponse.setStatus(true);
		}
		jsonResponse.setData(list);

		return jsonResponse;
	}

	@Override
	public Notice getDisccusionDetail(String uuid) {
		// TODO Auto-generated method stub
		return noticeRepo.getNoticeDetail(uuid);
	}

}
