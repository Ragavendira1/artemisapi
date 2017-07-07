package com.sqs.artemisapi.common;

import java.util.Base64;
import java.util.StringTokenizer;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.Response.Status;

import com.sqs.artemisapi.models.*;
import com.sqs.lib.MongoDB;
import com.sqs.lib.MongoDB.*;

public class CommonLibrary {
	/// USER
	
	public static String getUserName(String authdata){
		authdata = authdata.replace("Basic", "").trim();
		try {
			String usernameAndPassword = null;
			byte[] decodedBytes = Base64.getDecoder().decode(authdata);
			usernameAndPassword = new String(decodedBytes, "UTF-8");
			StringTokenizer tokenizer = new StringTokenizer(usernameAndPassword, ":");
			String username = tokenizer.nextToken();
			return username;
		} catch (Exception ex) {
			return "";
		}
	}
	
	public static Boolean isSystemAdmin(String authdata) {
		MongoDB mongodb = null;
		SearchCriteria criteria = null;
		Boolean isadmin = false;
		try {
			String username = getUserName(authdata);
			mongodb = new MongoDB();
			criteria = mongodb.searchCriteria();
			criteria.append("username", username);
			criteria.append("isadmin", true);
			mongodb.find(DBSchema.USER_COLLECTION, criteria);
			isadmin = mongodb.cursorHasNext(DBSchema.USER_COLLECTION);
			criteria.clear();
		}catch(Exception ex){ }finally {
			if(criteria!=null)
				criteria.clear();
			if(mongodb!=null)
				mongodb.close();
		}
		return isadmin;
	}

	public static String getUserFullName(String username) {
		String fullname = username;
		MongoDB mongodb = null;
		SearchCriteria criteria = null;
		try {
			mongodb = new MongoDB();
			criteria = mongodb.searchCriteria();
			criteria.append("username", username);
			mongodb.find(DBSchema.USER_COLLECTION, criteria);
			if (mongodb.cursorHasNext(DBSchema.USER_COLLECTION)) {
				mongodb.cursorNext(DBSchema.USER_COLLECTION);
				User user = new User();
		    	user.setFirstName(mongodb.getString(DBSchema.USER_COLLECTION, "firstname"));
		    	user.setMiddleName(mongodb.getString(DBSchema.USER_COLLECTION, "middlename"));
		    	user.setLastName(mongodb.getString(DBSchema.USER_COLLECTION, "lastname"));
				fullname = user.getFullname();
			}
			criteria.clear();
		}  catch(Exception ex){ }finally {
			if(criteria!=null)
				criteria.clear();
			if(mongodb!=null)
				mongodb.close();
		}
		return fullname;
	}
	
	public static Response getUser(String username, String password) {
		return CommonLibrary.getUser(username, Authentication.getToken(username, password), true); 
	}
	
	public static Response getUser(String username, String authdata, boolean includetoken ) {
		ResponseData responsedata = new ResponseData();
		MongoDB mongodb = null;
		User user = null;
		SearchCriteria criteria = null;
		try {
			if(Authentication.isAuthorized(authdata)){
				mongodb = new MongoDB();
				criteria = mongodb.searchCriteria();
				criteria.append("username", username);
				mongodb.find(DBSchema.USER_COLLECTION, criteria);
				if(mongodb.cursorHasNext(DBSchema.USER_COLLECTION)){
					mongodb.cursorNext(DBSchema.USER_COLLECTION);
			    	user = new User();
			    	user.setId(mongodb.getLong(DBSchema.USER_COLLECTION, "_id"));
			    	user.setUserName(mongodb.getString(DBSchema.USER_COLLECTION, "username"));
			    	if (includetoken)
			    		user.setToken(mongodb.getString(DBSchema.USER_COLLECTION, "token"));
			    	user.setFirstName(mongodb.getString(DBSchema.USER_COLLECTION, "firstname"));
			    	user.setMiddleName(mongodb.getString(DBSchema.USER_COLLECTION, "middlename"));
			    	user.setLastName(mongodb.getString(DBSchema.USER_COLLECTION, "lastname"));
			    	user.setEmployeeID(mongodb.getString(DBSchema.USER_COLLECTION, "employeeid"));
			    	user.setEmailID(mongodb.getString(DBSchema.USER_COLLECTION, "emailid"));
			    	user.setMobilePhone(mongodb.getString(DBSchema.USER_COLLECTION, "mobilephone"));
			    	user.setPhoneExtension(mongodb.getString(DBSchema.USER_COLLECTION, "phoneextension"));
			    	user.setIsactive(mongodb.getBoolean(DBSchema.USER_COLLECTION, "isactive"));
			    	user.setIsadmin(mongodb.getBoolean(DBSchema.USER_COLLECTION, "isadmin"));
			    	user.setCreatedon(mongodb.getDate(DBSchema.USER_COLLECTION, "createdon"));
			    	user.setCreatedby(mongodb.getString(DBSchema.USER_COLLECTION, "createdby"));
			    	user.setTheme(mongodb.getString(DBSchema.USER_COLLECTION, "theme"));
			    	responsedata.setCode(Status.OK.getStatusCode());
				    responsedata.setBody(user);
				}
				else {
					responsedata.setCode(Status.BAD_REQUEST.getStatusCode());
				    responsedata.setBody("The user name '"+username+"' does not exist.");
				}
			}
			else{
				responsedata.setCode(Status.UNAUTHORIZED.getStatusCode());
			    responsedata.setBody("Unauthorized access");
			}
		}catch(Exception ex){
			responsedata.setCode(Status.INTERNAL_SERVER_ERROR.getStatusCode());
		    responsedata.setBody("Error while getting user. " + ex.getMessage());
		}finally {
			if(criteria!=null)
				criteria.clear();
			if(mongodb!=null)
				mongodb.close();
		}
		return Response.status(responsedata.getCode()).entity(responsedata).build();
	}
	
	//PROJECT
	
	public static Boolean isProjectAdmin(String authdata, long id){
		Boolean isprojectadmin = false;
		//Boolean isadmin = false;
		SearchCriteria criteria = null;
		MongoDB mongodb = null;
		try {
			if(Authentication.isAuthorized(authdata)){
				mongodb = new MongoDB();
				criteria = mongodb.searchCriteria();
				criteria.append("projectid", id);
				criteria.append("username", getUserName(authdata));
				criteria.append("isadmin", true);
				mongodb.find(DBSchema.PROJECT_ACCESS_COLLECTION, criteria);
				isprojectadmin = mongodb.cursorHasNext(DBSchema.PROJECT_ACCESS_COLLECTION);
			}
		}
		catch(Exception ex){
			throw ex;
		}finally {
			if(criteria!=null)
				criteria.clear();
			if(mongodb!=null)
				mongodb.close();
		}
		return (isprojectadmin);
	}
	
	//APPLICATION
}
