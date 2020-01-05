package com.radius.sodalityUser.service;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashSet;
import java.util.Optional;
import java.util.Set;

import javax.json.JsonObject;
import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import com.radius.sodalityUser.common.Commonfunction;
import com.radius.sodalityUser.model.Unit;
import com.radius.sodalityUser.model.User;
import com.radius.sodalityUser.model.UserType;
import com.radius.sodalityUser.repository.TowerRepository;
import com.radius.sodalityUser.repository.UnitRepository;
import com.radius.sodalityUser.repository.UserRepositoryImpl;
import com.radius.sodalityUser.response.FamilyListResponse;
import com.radius.sodalityUser.response.FamilyResponseJson;
import com.radius.sodalityUser.response.ParentGetJson;
import com.radius.sodalityUser.response.ResidentListResponse;
import com.radius.sodalityUser.response.ResidentResponseJson;
import com.radius.sodalityUser.response.SocietyJson;
import com.radius.sodalityUser.response.SocietyListResponse;
import com.radius.sodalityUser.response.StaffListResponse;
import com.radius.sodalityUser.response.UserResponse;
import com.radius.sodalityUser.response.staffResponse;

@Service
public class UserServiceImpl implements UserService {
	@Autowired
	UserRepositoryImpl userRepo;
	@Autowired
	Commonfunction fun;
	@Autowired
	UnitRepository unitRepo;

	@Autowired
	TowerRepository towerrepo;

	@Transactional
	@Override
	public User saveUser(JsonObject requestBody) {
		User user = new User();

		if (requestBody.containsKey("email")) {
			user.setEmail(requestBody.getJsonString("email").getString());
		}
		if (requestBody.containsKey("password")) {
			user.setPassword(requestBody.getJsonString("password").getString());
		}
		Date date = new Date();

		user.setCreatedDate(date);
		user.setLastModifiedDate(date);
		user.setStatus(UserType.userStatus.Active.toString());

		user.setUuid((fun.uuIDSend()));

		if (requestBody.containsKey("user_type")) {
			user.setUser_type(requestBody.getJsonString("user_type").getString());
			if (requestBody.getJsonString("user_type").getString().equals(UserType.userTypes.Admin.toString())) {
				user = fun.AdminAdd(requestBody, user);
			}
			if (requestBody.getJsonString("user_type").getString().equals(UserType.userTypes.Society.toString())) {
				User byId = userRepo.getById(Long.parseLong(requestBody.getJsonNumber("parent_id").toString()));
				user.setParrentAccount(byId);
			}
			if (requestBody.getJsonString("user_type").getString().equals(UserType.userTypes.Staff.toString())) {
				User byId = userRepo.getById(Long.parseLong(requestBody.getJsonNumber("parent_id").toString()));
				user.setParrentAccount(byId);
			}
			if (requestBody.getJsonString("user_type").getString().equals(UserType.userTypes.Resident.toString())) {
				User byId = userRepo.getById(Long.parseLong(requestBody.getJsonNumber("parent_id").toString()));
				user.setParrentAccount(byId);
			}
		}
		userRepo.save(user);
		return user;
	}

	@Override
	public ArrayList<User> getuser() {
		return userRepo.getAllUserWithDetailAdmin();
	}

	@Override
	public User getuserById(JsonObject requestBody, long id) {
		// TODO Auto-generated method stub
		User u = new User();
		if (requestBody.containsKey("user_type")) {

			if (requestBody.getJsonString("user_type").getString().equals(UserType.userTypes.Admin.toString())) {
				u = userRepo.getAdminDetailWithId(id);
			} else if (requestBody.getJsonString("user_type").getString()
					.equals(UserType.userTypes.Society.toString())) {
				u = userRepo.getSocietyDetailWithId(id);
			} else if (requestBody.getJsonString("user_type").getString()
					.equals(UserType.userTypes.Resident.toString())) {
				u = userRepo.getResidentDetailWithId(id);
			} else if (requestBody.getJsonString("user_type").getString().equals(UserType.userTypes.Staff.toString())) {
				u = userRepo.getStaffDetailWithId(id);
			}
		}
		return u;
	}

	@Override
	public UserResponse login(JsonObject requestBody) {
		// TODO Auto-generated method stub
		if (requestBody.containsKey("email")) {
		}
		if (requestBody.containsKey("password")) {
		}
		User loginData = null;
		loginData = userRepo.loginData(requestBody.getJsonString("email").getString(),
				requestBody.getJsonString("password").getString());
		UserResponse response = new UserResponse();
		if (loginData != null) {
			response.setStatus(true);
			if (loginData.getUser_type().equals(UserType.userTypes.Admin.toString())) {
				response.setData(userRepo.getAdminDetailWithId(loginData.getId()));
			} else if (loginData.getUser_type().equals(UserType.userTypes.Society.toString())) {
				response.setData(userRepo.getSocietyDetailWithId(loginData.getId()));
			} else if (loginData.getUser_type().equals(UserType.userTypes.Resident.toString())) {
				User u =new User();
				u = userRepo.getResidentDetailWithId(loginData.getId());
				u.setParrentAccount(userRepo.getParentUuid(u.getUuid()));
				response.setData(u);
			} else if (loginData.getUser_type().equals(UserType.userTypes.Staff.toString())) {
				response.setData(userRepo.getStaffDetailWithId(loginData.getId()));
			}
		} else {
			response.setMessage("Invalid user Email or Password");
			response.setStatus(false);
		}
		return response;
	}

	@Override
	public User updateUser(JsonObject requestBody) {
		User user = new User();
		if (requestBody.containsKey("user_status")) {
			user.setStatus(requestBody.getJsonNumber("user_status").toString());
		}
		if (requestBody.containsKey("id")) {
			user.setId(Long.parseLong(requestBody.getJsonNumber("id").toString()));
		}

		if (requestBody.containsKey("email")) {
			user.setEmail(requestBody.getJsonString("email").getString());
		}
		if (requestBody.containsKey("password")) {
			user.setPassword(requestBody.getJsonString("password").getString());
		}
		if (requestBody.containsKey("user_type")) {
			user.setUser_type(requestBody.getJsonString("user_type").getString());
			if (requestBody.getJsonString("user_type").getString().equals(UserType.userTypes.Admin.toString())) {

				user = fun.AdminAdd(requestBody, user);
			}
			if (requestBody.getJsonString("user_type").getString().equals(UserType.userTypes.Society.toString())) {
			}

			if (requestBody.getJsonString("user_type").getString().equals(UserType.userTypes.Resident.toString())) {
			}
		}
		if (requestBody.containsKey("updateDate")) {
			String dateStr = requestBody.getString("updateDate");
			SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
			Date birthDate;
			try {
				birthDate = sdf.parse(dateStr);
				user.setLastModifiedDate(birthDate);
			} catch (ParseException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		userRepo.save(user);
		return user;
	}

	@Override
	public SocietyListResponse getSociety(JsonObject requestBody) {
		// TODO Auto-generated method stub
		SocietyListResponse response = new SocietyListResponse();
		response.setStatus(false);
		ArrayList<User> adminSocietyList = userRepo
				.getAdminSocietyList(Long.parseLong(requestBody.getJsonNumber("id").toString()));
		ArrayList<SocietyJson> list = new ArrayList();
		for (int counter = 0; counter < adminSocietyList.size(); counter++) {
			SocietyJson json = new SocietyJson();

			json.setContactNumber(adminSocietyList.get(counter).getSocietyDetail().getContactNumber());
			json.setCreatedDate(adminSocietyList.get(counter).getCreatedDate());
			json.setEmail(adminSocietyList.get(counter).getEmail());
			json.setLastModifiedDate(adminSocietyList.get(counter).getLastModifiedDate());
			json.setSocietyDisplayName(adminSocietyList.get(counter).getSocietyDetail().getSocietyDisplayName());
			json.setSocietyName(adminSocietyList.get(counter).getSocietyDetail().getSocietyName());
			json.setUuid(adminSocietyList.get(counter).getUuid());

			list.add(json);

		}

		if (adminSocietyList.size() > 0) {
			response.setMessage("Sucessfully data get");
			response.setData(list);
		} else {
			response.setMessage("No Data Found");
		}
		return response;
	}

	@Override
	public User saveSociety(MultipartFile[] uploadfiles, HttpServletRequest request, MultipartFile[] adImage,
			MultipartFile billLogo, MultipartFile societyLogo, JsonObject requestBody) {
		// TODO Auto-generated method stub
		User user = new User();
		if (requestBody.containsKey("email")) {
			user.setEmail(requestBody.getJsonString("email").getString());
		}

		if (requestBody.containsKey("password")) {
			user.setPassword(requestBody.getJsonString("password").getString());
		}
		Date date = new Date();

		user.setCreatedDate(date);
		user.setLastModifiedDate(date);
		user.setStatus(UserType.userStatus.Active.toString());

		user.setUuid((fun.uuIDSend()));

		user.setUser_type(UserType.userTypes.Society.toString());
		user = fun.SocietyAdd(uploadfiles, request, adImage, billLogo, societyLogo, requestBody, user);
		User byId = userRepo.getById(Long.parseLong(requestBody.getJsonNumber("parent_id").toString()));
		user.setParrentAccount(byId);

		userRepo.save(user);
		return user;
	}

	@Override
	public User getSocietyByuuId(String uuid) {
		// TODO Auto-generated method stub
		return userRepo.getByuuid(uuid);
	}

	@Override
	public User updateSociety(MultipartFile[] uploadfiles, HttpServletRequest request, MultipartFile[] adImage,
			MultipartFile billLogo, MultipartFile societyLogo, JsonObject requestBody) {
		// TODO Auto-generated method stub
		User user = new User();
		Optional<User> userlastDetail = null;

		if (requestBody.containsKey("id")) {
			user.setId(Long.parseLong(requestBody.getJsonNumber("id").toString()));
			userlastDetail = userRepo.findById(Long.parseLong(requestBody.getJsonNumber("id").toString()));

		}
		System.out.println(userlastDetail.get().getEmail());
		user.setEmail(userlastDetail.get().getEmail());
		user.setPassword(userlastDetail.get().getPassword());
		user.setCreatedDate((userlastDetail.get().getCreatedDate()));

		if (requestBody.containsKey("uuid")) {
			user.setUuid(requestBody.getJsonString("uuid").getString());
		}
		Date date = new Date();
		user.setLastModifiedDate(date);
		user.setStatus(UserType.userStatus.Active.toString());
		if (requestBody.containsKey("user_type")) {
			user.setUser_type(requestBody.getJsonString("user_type").getString());
			if (requestBody.getJsonString("user_type").getString().equals(UserType.userTypes.Society.toString())) {
				user = fun.SocietyAdd(uploadfiles, request, adImage, billLogo, societyLogo, requestBody, user);
				User byId = userRepo.getById(Long.parseLong(requestBody.getJsonNumber("parent_id").toString()));
				user.setParrentAccount(byId);
			}
		}
		userRepo.save(user);
		return user;
	}

	@Override
	public User saveResident(MultipartFile uploadfiles, JsonObject requestBody) {
		// TODO Auto-generated method stub

		User user = new User();

		if (requestBody.containsKey("email")) {
			user.setEmail(requestBody.getJsonString("email").getString());
		}
		if (requestBody.containsKey("flatOwned")) {
			Set<Unit> set = new HashSet<Unit>();

			for (int i = 0; i < requestBody.getJsonArray("flatOwned").size(); i++) {
				set.add(unitRepo.getUnit(requestBody.getJsonArray("flatOwned").getString(i)));
			}
			user.setFlatOwned(set);
		}
		if (requestBody.containsKey("password")) {
			user.setPassword(requestBody.getJsonString("password").getString());
		}
		Date date = new Date();
		user.setCreatedDate(date);
		user.setLastModifiedDate(date);
		user.setStatus(UserType.userStatus.Active.toString());
		user.setUuid((fun.uuIDSend()));
		user.setUser_type(UserType.userTypes.Resident.toString());
		User byId = userRepo.getById(Long.parseLong(requestBody.getJsonNumber("parent_id").toString()));
		user.setParrentAccount(byId);
		user = fun.residentAdd(uploadfiles, requestBody, user);
		userRepo.save(user);
		return user;
	}

	@Override
	public User updateResident(MultipartFile uploadfiles, JsonObject requestBody) {
		// TODO Auto-generated method stub
		// TODO Auto-generated method stub
		User userLastDetail = new User();
		User user = new User();
		if (requestBody.containsKey("id")) {
			user.setId(Long.parseLong(requestBody.getJsonNumber("id").toString()));
			userLastDetail = userRepo.getResidentDetailWithId(user.getId());
		}
		if (requestBody.containsKey("flatOwned")) {
			Set<Unit> set = new HashSet<Unit>();
			for (int i = 0; i < requestBody.getJsonArray("flatOwned").size(); i++) {
				set.add(unitRepo.getUnit(requestBody.getJsonArray("flatOwned").getString(i)));
			}
			user.setFlatOwned(set);
		}
		if (requestBody.containsKey("email")) {
			user.setEmail(requestBody.getJsonString("email").getString());
		}
		if (requestBody.containsKey("password")) {
			user.setPassword(requestBody.getJsonString("password").getString());
		}
		user.setCreatedDate(userLastDetail.getCreatedDate());
		Date date = new Date();

		user.setLastModifiedDate(date);
		user.setStatus(UserType.userStatus.Active.toString());

		user.setUuid((userLastDetail.getUuid()));

		user.setUser_type(UserType.userTypes.Resident.toString());

		User byId = userRepo.getById(Long.parseLong(requestBody.getJsonNumber("parent_id").toString()));
		user.setParrentAccount(byId);
		user = fun.residentAdd(uploadfiles, requestBody, user);

		userRepo.save(user);
		return user;
	}

	@Override
	public User getResidentUuid(String uuid) {
		// TODO Auto-generated method stub

		return userRepo.getResidentDetail(uuid);
	}

	@Override
	public ResidentListResponse getResidentList(JsonObject requestBody) {
		// TODO Auto-generated method stub
		ArrayList<User> u = null;
		ArrayList<ResidentResponseJson> responseJson = new ArrayList<ResidentResponseJson>();
		if (requestBody.containsKey("uuid")) {
			u = userRepo.getResidentList(requestBody.getJsonString("uuid").getString());
			for (int i = 0; i < u.size(); i++) {
				ResidentResponseJson json = new ResidentResponseJson();
				json.setClubMemberShip(u.get(i).getResidentDetail().getClubMembership());
				json.setEmail(u.get(i).getEmail());
				json.setFirstName(u.get(i).getResidentDetail().getFirstName());
				json.setLastName(u.get(i).getResidentDetail().getLastName());
				json.setMiddleName(u.get(i).getResidentDetail().getMiddleName());
				json.setMobileNumber(u.get(i).getResidentDetail().getMobileNumber());
				json.setUuid((u.get(i).getUuid()));

				json.setPossesionDate(u.get(i).getResidentDetail().getPossesionDate());
				json.setServiceStartDate(u.get(i).getResidentDetail().getServiceStartDate());
				json.setCreatedDate(u.get(i).getCreatedDate());
				json.setLastModifiedDate(u.get(i).getLastModifiedDate());

				responseJson.add(json);
			}
		}
		ResidentListResponse response = new ResidentListResponse();

		if (u.size() > 0) {
			response.setStatus(true);
			response.setData(responseJson);
			response.setMessage("Data get Successfully");
		}
		return response;
	}

	@Override
	public User saveStaff(MultipartFile uploadfiles, JsonObject requestBody) {
		// TODO Auto-generated method stub
		User user = new User();
		if (requestBody.containsKey("id")) {
			user.setId(Long.parseLong(requestBody.getJsonNumber("id").toString()));
		}
		if (requestBody.containsKey("email")) {
			user.setEmail(requestBody.getJsonString("email").getString());
		}

		if (requestBody.containsKey("password")) {
			user.setPassword(requestBody.getJsonString("password").getString());
		}
		Date date = new Date();

		user.setCreatedDate(date);
		user.setLastModifiedDate(date);
		user.setStatus(UserType.userStatus.Active.toString());

		user.setUuid((fun.uuIDSend()));

		user.setUser_type(UserType.userTypes.Staff.toString());

		User byId = userRepo.getById(Long.parseLong(requestBody.getJsonNumber("parent_id").toString()));
		user.setParrentAccount(byId);
		user = fun.staffAdd(uploadfiles, requestBody, user);

		userRepo.save(user);
		return user;
	}

	@Override
	public User getStaffuuid(String uuid) {
		// TODO Auto-generated method stub
		return userRepo.getStaffDetail(uuid);

	}

	@Override
	public User updateStaff(MultipartFile uploadfiles, JsonObject requestBody) {
		// TODO Auto-generated method stub
		User user = new User();
		Optional<User> userlastDetail = null;
		if (requestBody.containsKey("id")) {
			user.setId(Long.parseLong(requestBody.getJsonNumber("id").toString()));
			userlastDetail = userRepo.findById(user.getId());
		}
		if (requestBody.containsKey("email")) {
			user.setEmail(requestBody.getJsonString("email").getString());
		}
		if (requestBody.containsKey("password")) {
			user.setPassword(requestBody.getJsonString("password").getString());
		}
		user.setCreatedDate(userlastDetail.get().getCreatedDate());
		user.setUuid(userlastDetail.get().getUuid());
		Date date = new Date();

		user.setLastModifiedDate(date);
		user.setStatus(UserType.userStatus.Active.toString());

		user.setUser_type(UserType.userTypes.Staff.toString());
		System.out.println(userlastDetail.get().getParrentAccount().getId());
		User byId = userRepo.getById(userlastDetail.get().getParrentAccount().getId());
		user.setParrentAccount(byId);
		user = fun.staffAdd(uploadfiles, requestBody, user);

		userRepo.save(user);
		return user;
	}

	@Override
	public StaffListResponse getStaffList(JsonObject requestBody) {
		// TODO Auto-generated method stub
		ArrayList<User> u = null;
		ArrayList<staffResponse> responseJson = new ArrayList<staffResponse>();
		if (requestBody.containsKey("uuid")) {
			u = userRepo.getStaffList(requestBody.getJsonString("uuid").getString());
			for (int i = 0; i < u.size(); i++) {
				staffResponse json = new staffResponse();
				json.setEmail(u.get(i).getEmail());
				json.setUuid(u.get(i).getUuid());
				json.setCategory(u.get(i).getStaffDetals().getCategory().getTitle());
				json.setChooseStaffWorkArea(u.get(i).getStaffDetals().getChooseStaffWorkArea());
				json.setCreatedDate(u.get(i).getCreatedDate());
				json.setLastModifiedDate(u.get(i).getLastModifiedDate());
				json.setDesignation(u.get(i).getStaffDetals().getDesignation());
				json.setEmployeeId(u.get(i).getStaffDetals().getEmployeeId());
				json.setName(u.get(i).getStaffDetals().getName());
				json.setPic(u.get(i).getStaffDetals().getPic());
				json.setPoliceVerification(u.get(i).getStaffDetals().getPoliceVerification());
				json.setValidUpto(u.get(i).getStaffDetals().getValidUpto());

				responseJson.add(json);
			}
		}
		StaffListResponse response = new StaffListResponse();

		if (u.size() > 0) {
			response.setStatus(true);
			response.setData(responseJson);
			response.setMessage("Data get Successfully");
		}
		return response;
	}

	@Override
	public User saveFamilyMember(MultipartFile uploadfiles, JsonObject requestBody) {
		// TODO Auto-generated method stub

		User user = new User();
 
		if (requestBody.containsKey("id")) {
			user.setId(Long.parseLong(requestBody.getJsonNumber("id").toString()));

 		}
		if (requestBody.containsKey("email")) {
			user.setEmail(requestBody.getJsonString("email").getString());
		}

		if (requestBody.containsKey("password")) {
			user.setPassword(requestBody.getJsonString("password").getString());
		}
		Date date = new Date();
		user.setCreatedDate(date);
		user.setLastModifiedDate(date);
		user.setStatus(UserType.userStatus.Active.toString());
		if (requestBody.containsKey("uuid")) {
			user.setUuid((requestBody.getString("uuid")));

		}else {
			user.setUuid((fun.uuIDSend()));

		}
		user.setUser_type(UserType.userTypes.FamilyMember.toString());
		User byId = userRepo.getResidentDetail((requestBody.getString("parent_id")));
		user.setParrentAccount(byId);
		user = fun.familyAdd(uploadfiles, requestBody, user);
		userRepo.save(user);
		return user;
	}
	@Override
	public User updateFamilyMember(MultipartFile uploadfiles, JsonObject requestBody) {
		// TODO Auto-generated method stub

		User user = new User();
		Optional<User> userlastDetail = null;
		if (requestBody.containsKey("id")) {
			user.setId(Long.parseLong(requestBody.getJsonNumber("id").toString()));
			userlastDetail = userRepo.findById(user.getId());
		}

		user.setPassword(userlastDetail.get().getPassword());
		user.setEmail(userlastDetail.get().getEmail());

		Date date = new Date();
		user.setCreatedDate(userlastDetail.get().getCreatedDate());
		user.setLastModifiedDate(date);
		user.setStatus(UserType.userStatus.Active.toString());
		user.setUuid(userlastDetail.get().getUuid());
		user.setUser_type(UserType.userTypes.FamilyMember.toString());
 		user.setParrentAccount(userRepo.getResidentDetail((requestBody.getString("parent_id"))));
		user = fun.familyAdd(uploadfiles, requestBody, user);
		System.out.println(user.getFamilyDetail().getName());
		userRepo.save(user);
		return user;
	}

	@Override
	public FamilyListResponse getFamilyList(JsonObject requestBody) {
		// TODO Auto-generated method stub
		ArrayList<User> familyList = userRepo.getFamilyList(requestBody.getString("residentId"));
		FamilyListResponse familyResponseList = new FamilyListResponse();
		ArrayList<FamilyResponseJson> list2 = new ArrayList<FamilyResponseJson>();
		for(int i=0;i<familyList.size();i++) {
			FamilyResponseJson response = new FamilyResponseJson();
			response.setName(familyList.get(i).getFamilyDetail().getName());
			response.setEmail(familyList.get(i).getEmail());
			response.setMobileNumber(familyList.get(i).getFamilyDetail().getMobileNumber());
			response.setUuid(familyList.get(i).getUuid());
			response.setRelationShip(familyList.get(i).getFamilyDetail().getRelationShip());
			list2.add(response);
		}
		familyResponseList.setData(list2);
		familyResponseList.setStatus(true);
		familyResponseList.setMessage("Successfully get");
		return familyResponseList;
	}

	@Override
	public User getFamilyDetail(String uuid) {
		// TODO Auto-generated method stub
		
		return userRepo.getFamilyByUuid(uuid);
		}

	@Override
	public ParentGetJson getParentDetail(String uuid) {
		// TODO Auto-generated method stub
		User parentUuid = userRepo.getParentUuid(uuid);
			ParentGetJson json = new ParentGetJson();
			json.setUuid(parentUuid.getUuid());
			
		return json
				;
	}

	@Override
	public StaffListResponse getStaffListByCategory(JsonObject requestBody) {
		// TODO Auto-generated method stub
		ArrayList<User> u = null;
		ArrayList<staffResponse> responseJson = new ArrayList<staffResponse>();
		if (requestBody.containsKey("uuid")) {
			if(requestBody.containsKey("categoryUUid")) {
				u = userRepo.getStaffListByCategory(requestBody.getJsonString("uuid").getString(),requestBody.getJsonString("categoryUUid").getString());

			}else {
				u = userRepo.getStaffList(requestBody.getJsonString("uuid").getString());

			}
			for (int i = 0; i < u.size(); i++) {
				staffResponse json = new staffResponse();
				json.setEmail(u.get(i).getEmail());
				json.setUuid(u.get(i).getUuid());
				json.setCategory(u.get(i).getStaffDetals().getCategory().getTitle());
				json.setChooseStaffWorkArea(u.get(i).getStaffDetals().getChooseStaffWorkArea());
				json.setCreatedDate(u.get(i).getCreatedDate());
				json.setLastModifiedDate(u.get(i).getLastModifiedDate());
				json.setDesignation(u.get(i).getStaffDetals().getDesignation());
				json.setEmployeeId(u.get(i).getStaffDetals().getEmployeeId());
				json.setName(u.get(i).getStaffDetals().getName());
				json.setPic(u.get(i).getStaffDetals().getPic());
				json.setPoliceVerification(u.get(i).getStaffDetals().getPoliceVerification());
				json.setValidUpto(u.get(i).getStaffDetals().getValidUpto());

				responseJson.add(json);
			}
		}
		StaffListResponse response = new StaffListResponse();

		if (u.size() > 0) {
			response.setStatus(true);
			response.setData(responseJson);
			response.setMessage("Data get Successfully");
		}
		return response;
	}

}