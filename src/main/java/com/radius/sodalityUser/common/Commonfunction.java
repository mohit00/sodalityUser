package com.radius.sodalityUser.common;

import java.io.File;
import java.io.IOException;
import java.io.StringReader;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.List;
import java.util.Random;
import java.util.UUID;
import java.util.stream.Collectors;

import javax.json.Json;
import javax.json.JsonObject;
import javax.json.JsonReader;
import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.web.multipart.MultipartFile;

import com.radius.sodalityUser.model.AdImage;
import com.radius.sodalityUser.model.FamilyResident;
import com.radius.sodalityUser.model.ImageList;
import com.radius.sodalityUser.model.ResidentDetail;
import com.radius.sodalityUser.model.SocietyDetail;
import com.radius.sodalityUser.model.StaffDetals;
import com.radius.sodalityUser.model.User;
import com.radius.sodalityUser.model.UserDetail;
import com.radius.sodalityUser.repository.CategoryRepository;

import io.micrometer.core.instrument.util.StringUtils;

@Component
public class Commonfunction {
	@Autowired
	CategoryRepository catRepo;

	public String setotp(Long complainId) {
		Random random = new Random();

		String id = complainId + String.format("%04d", random.nextInt(10000));

		return id;
	}

	public User AdminAdd(JsonObject requestBody, User user) {
		final UserDetail userDetail = new UserDetail();

		if (requestBody.containsKey("userDetail")) {
			System.out.print(requestBody.getJsonObject("userDetail"));
			JsonReader jsonReader2 = Json
					.createReader(new StringReader(requestBody.getJsonObject("userDetail").toString()));
			JsonObject requestBody2 = jsonReader2.readObject();
			if (requestBody2.containsKey("id")) {

				userDetail.setId(Long.parseLong(requestBody.getJsonNumber("id").toString()));

			}
			userDetail.setAddress(requestBody2.getJsonString("address").getString());
			userDetail.setPhoneNumber(requestBody2.getString("phoneNumber"));
			userDetail.setCity(requestBody2.getString("city"));
			user.setUserDetail(userDetail);

		}

		return user;
	}

	public User SocietyAdd(MultipartFile[] uploadfiles, HttpServletRequest request, MultipartFile[] adImage,
			MultipartFile billLogo, MultipartFile societyLogo, JsonObject requestBody, User user) {
		final SocietyDetail userDetail = new SocietyDetail();
		String path = "src/main/resources/images/";
		if (requestBody.containsKey("userDetail")) {

			JsonReader jsonReader2 = Json
					.createReader(new StringReader(requestBody.getJsonObject("userDetail").toString()));
			JsonObject requestBody2 = jsonReader2.readObject();

			if (requestBody2.containsKey("id")) {
				userDetail.setId(Long.parseLong(requestBody2.getJsonString("id").getString()));
			}
			String ImagesListfileName = Arrays.stream(uploadfiles).map(x -> {
				return x.getOriginalFilename();
			}).filter(x -> !StringUtils.isEmpty(x)).collect(Collectors.joining(" , "));
			String adImageFileName = Arrays.stream(adImage).map(x -> x.getOriginalFilename())
					.filter(x -> !StringUtils.isEmpty(x)).collect(Collectors.joining(" , "));
			ImageList list = new ImageList();
			AdImage list1 = new AdImage();

			ArrayList<String> listimage = new ArrayList<String>();
			ArrayList<String> adImageList = new ArrayList<String>();

			if (StringUtils.isEmpty(ImagesListfileName)) {
			} else {
				try {
					listimage = saveUploadedFiles(Arrays.asList(uploadfiles));

				} catch (IOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
				userDetail.setImageList(list);

			}
			if (StringUtils.isEmpty(adImageFileName)) {
			} else {

				try {
					adImageList = saveUploadedFiles(Arrays.asList(adImage));
				} catch (IOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
				userDetail.setAdImage(list1);
			}
			try {

				if (societyLogo != null) {
					userDetail.setSocietyLogo(saveUploadedFiles(Arrays.asList(societyLogo)).get(0));

				} else if (requestBody2.containsKey("societyLogoArray")) {
					userDetail.setSocietyLogo(path + requestBody2.getJsonString("societyLogoArray").getString());

				}
				if (billLogo != null) {
					userDetail.setBillLogo(saveUploadedFiles(Arrays.asList(billLogo)).get(0));

				} else if (requestBody2.containsKey("billLogoArray")) {
					userDetail.setBillLogo(path + requestBody2.getJsonString("billLogoArray").getString());

				}
				if (requestBody2.containsKey("imageListArray")) {
					System.out.println(requestBody2.getJsonArray("imageListArray"));
					for (int i = 0; i < requestBody2.getJsonArray("imageListArray").size(); i++) {
						listimage.add(path + requestBody2.getJsonArray("imageListArray").getString(i));
					}
				}
				if (requestBody2.containsKey("adImageArray")) {
					for (int i = 0; i < requestBody2.getJsonArray("adImageArray").size(); i++) {
						adImageList.add(path + requestBody2.getJsonArray("adImageArray").getString(i));

					}
				}
				list.setImage(listimage);
				list1.setImage(adImageList);
				if (requestBody2.containsKey("albumId")) {
					list.setId(Long.parseLong(requestBody2.getJsonString("albumId").getString()));

				}
				if (requestBody2.containsKey("adId")) {
					list1.setId(Long.parseLong(requestBody2.getJsonString("adId").getString()));

				}
				userDetail.setImageList(list);
				userDetail.setAdImage(list1);

			} catch (IOException e) {
			}

			if (requestBody2.containsKey("societyName")) {
				userDetail.setSocietyName((requestBody2.getJsonString("societyName").getString()));

			}
			if (requestBody2.containsKey("societyDisplayName")) {
				userDetail.setSocietyDisplayName((requestBody2.getJsonString("societyDisplayName").getString()));

			}
			if (requestBody2.containsKey("registeredAddress")) {
				userDetail.setRegisteredAddress((requestBody2.getJsonString("registeredAddress").getString()));

			}
			if (requestBody2.containsKey("BillAddress")) {
				userDetail.setBillAddress((requestBody2.getJsonString("BillAddress").getString()));

			}
			if (requestBody2.containsKey("BillName")) {
				userDetail.setBillName((requestBody2.getJsonString("BillName").getString()));

			}
			if (requestBody2.containsKey("Registration")) {
				userDetail.setRegistration((requestBody2.getJsonString("Registration").getString()));

			}
			if (requestBody2.containsKey("GSTINT")) {
				userDetail.setGSTINT((requestBody2.getJsonString("GSTINT").getString()));

			}
			if (requestBody2.containsKey("PAN")) {
				userDetail.setPAN((requestBody2.getJsonString("PAN").getString()));

			}
			if (requestBody2.containsKey("TAN")) {
				userDetail.setTAN((requestBody2.getJsonString("TAN").getString()));

			}
			if (requestBody2.containsKey("serviceTax")) {
				userDetail.setServiceTax((requestBody2.getJsonString("serviceTax").getString()));

			}
			if (requestBody2.containsKey("contactNumber")) {
				userDetail.setContactNumber((requestBody2.getJsonNumber("contactNumber").toString()));

			}
			if (requestBody2.containsKey("contactEmail")) {
				userDetail.setContactEmail((requestBody2.getJsonString("contactEmail").getString()));

			}
			if (requestBody2.containsKey("aboutSociety")) {
				userDetail.setAboutSociety((requestBody2.getJsonString("aboutSociety").getString()));

			}
		}
		user.setSocietyDetail(userDetail);
		return user;
	}

	public User staffAdd(MultipartFile uploadfiles, JsonObject requestBody, User user) {
		final StaffDetals userDetail = new StaffDetals();
		if (requestBody.containsKey("userDetail")) {
			System.out.print(requestBody.getJsonObject("userDetail"));
			JsonReader jsonReader2 = Json
					.createReader(new StringReader(requestBody.getJsonObject("userDetail").toString()));
			JsonObject requestBody2 = jsonReader2.readObject();
			if (requestBody2.containsKey("id")) {
				userDetail.setId(Long.parseLong(requestBody.getJsonNumber("id").toString()));
			}
			if (requestBody2.containsKey("categoryId")) {
				userDetail.setCategory(catRepo.getCategoryDetail(requestBody2.getString("categoryId")));
			}
			if (requestBody2.containsKey("employeeId")) {
				userDetail.setEmployeeId((requestBody2.getString("employeeId")));

			}
			if (requestBody2.containsKey("name")) {
				userDetail.setName((requestBody2.getString("name")));

			}
			if (requestBody2.containsKey("mobileNumber")) {
				userDetail.setMobileNumber((requestBody2.getJsonNumber("mobileNumber").toString()));

			}
			if (requestBody2.containsKey("designation")) {
				userDetail.setDesignation((requestBody2.getString("designation")));
			}
			if (requestBody2.containsKey("vendor")) {
				userDetail.setVendor((requestBody2.getString("vendor")));
			}
			if (requestBody2.containsKey("from")) {
				if (!requestBody2.getString("from").isEmpty()) {
					String dateStr = requestBody2.getString("from");
					userDetail.setFrom(DateFormate(dateStr));
				}

			}
			if (requestBody2.containsKey("to")) {
				if (!requestBody2.getString("to").isEmpty()) {
					String dateStr = requestBody2.getString("to");
					userDetail.setTo(DateFormate(dateStr));

				}
			}
			if (requestBody2.containsKey("dateOfCardIssue")) {
				if (!requestBody2.getString("dateOfCardIssue").isEmpty()) {
					String dateStr = requestBody2.getString("dateOfCardIssue");
					userDetail.setDateOfCardIssue(DateFormate(dateStr));

				}
			}
			if (requestBody2.containsKey("validUpto")) {
				if (!requestBody2.getString("validUpto").isEmpty()) {
					String dateStr = requestBody2.getString("validUpto");
					userDetail.setValidUpto(DateFormate(dateStr));

				}

			}

			if (requestBody2.containsKey("dateOfBirth")) {
				if (!requestBody2.getString("dateOfBirth").isEmpty()) {
					String dateStr = requestBody2.getString("dateOfBirth");
					userDetail.setDateOfBirth(DateFormate(dateStr));

				}

			}
			if (requestBody2.containsKey("aadharNo")) {
				userDetail.setAadharNo((requestBody2.getString("aadharNo")));

			}
			if (requestBody2.containsKey("address")) {
				userDetail.setAddress((requestBody2.getString("address")));

			}
			if (requestBody2.containsKey("chooseStaffWorkArea")) {
				userDetail.setChooseStaffWorkArea((requestBody2.getString("chooseStaffWorkArea")));

			}
			if (requestBody2.containsKey("policeVerification")) {
				userDetail.setPoliceVerification((requestBody2.getBoolean("policeVerification")));

			}
			if (requestBody2.containsKey("pic")) {
				userDetail.setPic(requestBody2.getString("pic"));
			} else {
				try {
					if (uploadfiles != null) {
						userDetail.setPic(saveUploadedFiles(Arrays.asList(uploadfiles)).get(0));

					}
				} catch (IOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}

			}
			user.setStaffDetals(userDetail);
		}

		return user;
	}

	public User familyAdd(MultipartFile uploadfiles, JsonObject requestBody, User user) {
		final FamilyResident userDetail = new FamilyResident();
		JsonReader jsonReader2 = Json
				.createReader(new StringReader(requestBody.getJsonObject("userDetail").toString()));
		JsonObject requestBody2 = jsonReader2.readObject();

		if (requestBody2.containsKey("id")) {
			userDetail.setId(Long.parseLong(requestBody.getJsonNumber("id").toString()));
		}
		if (requestBody2.containsKey("relationShip")) {
			userDetail.setRelationShip(requestBody2.getString("relationShip"));
		}
		if (requestBody2.containsKey("descirption")) {
			userDetail.setDescirption(requestBody2.getString("descirption"));

		}
		if (requestBody2.containsKey("name")) {
			userDetail.setName(requestBody2.getString("name"));

		}

		if (requestBody2.containsKey("mobileNumber")) {
			userDetail.setMobileNumber(requestBody2.getString("mobileNumber"));

		}
		try {
			if (uploadfiles != null) {
				userDetail.setProfileImage(saveUploadedFiles(Arrays.asList(uploadfiles)).get(0));
			} else {
				if (requestBody2.containsKey("profileImage")) {
					userDetail.setProfileImage(requestBody2.getString("profileImage"));
				}
			}
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		user.setFamilyDetail(userDetail);

		return user;
	}

	public User residentAdd(MultipartFile uploadfiles, JsonObject requestBody, User user) {
		final ResidentDetail userDetail = new ResidentDetail();

		if (requestBody.containsKey("userDetail")) {
			System.out.print(requestBody.getJsonObject("userDetail"));
			JsonReader jsonReader2 = Json
					.createReader(new StringReader(requestBody.getJsonObject("userDetail").toString()));
			JsonObject requestBody2 = jsonReader2.readObject();
			if (requestBody2.containsKey("id")) {
				userDetail.setId(Long.parseLong(requestBody.getJsonNumber("id").toString()));
			}

			if (requestBody2.containsKey("firstName")) {
				userDetail.setFirstName(requestBody2.getString("firstName"));

			}
			if (requestBody2.containsKey("residentType")) {
				userDetail.setResidentType(requestBody2.getString("residentType"));

			}
			if (requestBody2.containsKey("middleName")) {
				userDetail.setMiddleName(requestBody2.getString("middleName"));

			}
			if (requestBody2.containsKey("lastName")) {
				userDetail.setLastName(requestBody2.getString("lastName"));

			}
			if (requestBody2.containsKey("clubMembership")) {
				userDetail.setClubMembership(requestBody2.getBoolean("clubMembership"));
			}
			if (requestBody2.containsKey("mobileNumber")) {
				userDetail.setMobileNumber(requestBody2.getString("mobileNumber"));

			}
			if (requestBody2.containsKey("alternateMobileNumber")) {
				if (requestBody2.getString("alternateMobileNumber") != null) {
					userDetail.setAlternateMobileNumber(requestBody2.getString("alternateMobileNumber"));

				}

			}
			if (requestBody2.containsKey("alternateEmailId")) {
				userDetail.setAlternateEmailId(requestBody2.getString("alternateEmailId"));

			}
			if (requestBody2.containsKey("landLine")) {
				userDetail.setLandLine(requestBody2.getString("landLine"));

			}
			if (requestBody2.getString("intercom") != null) {
				userDetail.setIntercom(requestBody2.getString("intercom"));

			}
			if (requestBody2.containsKey("occupation")) {
				userDetail.setOccupation(requestBody2.getString("occupation"));

			}

			if (requestBody2.containsKey("possesionDate")) {

				if (requestBody2.isNull("possesionDate")) {

				} else {
					String dateStr = requestBody2.getString("possesionDate");
					userDetail.setPossesionDate(DateFormate(dateStr));

				}

			}
			if (requestBody2.containsKey("serviceStartDate")) {
				if (requestBody2.isNull("serviceStartDate")) {

				} else {
					String dateStr = requestBody2.getString("serviceStartDate");
					userDetail.setServiceStartDate(DateFormate(dateStr));

				}

			}
			if (requestBody2.containsKey("accessCardNumber")) {
				userDetail.setAccessCardNumber(requestBody2.getString("accessCardNumber"));
			}
			if (requestBody2.containsKey("residentResiding")) {
				userDetail.setResidentResiding(requestBody2.getBoolean("residentResiding"));
			}
			try {
				if (uploadfiles != null) {
					userDetail.setProfileImage(saveUploadedFiles(Arrays.asList(uploadfiles)).get(0));
				} else {
					if (requestBody2.containsKey("profileImage")) {
						userDetail.setProfileImage(requestBody2.getString("profileImage"));
					}
				}
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			user.setResidentDetail(userDetail);
		}
		return user;
	}

	public JsonObject ReturnJsonObject(String string) {
		JsonReader jsonReader = Json.createReader(new StringReader(string));
		JsonObject requestBody = jsonReader.readObject();
		return requestBody;
	}

	public String uuIDSend() {
		UUID uuid = UUID.randomUUID();
		String randomUUIDString = (uuid.toString());

		return randomUUIDString;
	}

	public ArrayList<String> saveUploadedFiles(List<MultipartFile> files) throws IOException {
//	    String UPLOADED_FOLDER = request.getRealPath("images");
		String path1 = "src/main/resources/images/";

		File file1 = new File(path1);
		String absolutePath = file1.getAbsolutePath();
		System.out.println(absolutePath);
		ArrayList<String> imagePath = new ArrayList();
		for (MultipartFile file : files) {

			if (file.isEmpty()) {
				continue; // next pls
			}
			byte[] bytes = file.getBytes();
			Path path = Paths.get(absolutePath + '/' + file.getOriginalFilename());
			Files.write(path, bytes);
			imagePath.add("resources/images/" + file.getOriginalFilename());

		}
		return imagePath;

	}

	public Date DateFormate(String data) {

		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSSX");
		Date birthDate = null;

		try {
			birthDate = sdf.parse(data);

		} catch (ParseException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return birthDate;

	}
}
