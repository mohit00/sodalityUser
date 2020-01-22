package com.radius.sodalityUser.model;

public class UserType {
	 public enum userTypes {
		 SuperAdmin,Admin,Resident,Staff, Society,FamilyMember;
	    }
	 public enum userStatus{
		 Active,NotActive
	 }
	 public enum complainStatus{
		 New,Assigned,Inprogress,Pending,Resolved,Reopen,Close
	 }
	 public enum noticeType{
		 notice,discussion,notification
	 }
}
