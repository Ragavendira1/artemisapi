package com.sqs.artemisapi.controllers;

import java.util.*;
import javax.ws.rs.*;
import javax.ws.rs.core.*;
import javax.ws.rs.core.Response.Status;
import org.bson.Document;

import com.sqs.artemisapi.common.Authentication;
import com.sqs.artemisapi.common.CommonLibrary;
import com.sqs.artemisapi.common.DBSchema;
import com.sqs.artemisapi.models.*;
import com.sqs.lib.*;
import com.sqs.lib.MongoDB.*;

@Path("users")
public class UserController {	
	
	@GET
	@Path("/")
	@Produces(MediaType.APPLICATION_JSON)  
	public Response getAllUsers(@HeaderParam("authorization") String authdata) {
		ResponseData responsedata = new ResponseData();
		MongoDB mongodb = null;
		try {
			if(Authentication.isAuthorized(authdata)){
				mongodb = new MongoDB();
				mongodb.find(DBSchema.USER_COLLECTION);
				List<User> list = new ArrayList<User>();
			    while (mongodb.cursorHasNext(DBSchema.USER_COLLECTION)) {
			    	mongodb.cursorNext(DBSchema.USER_COLLECTION);
			    	User user = new User();
			    	user.setId(mongodb.getLong(DBSchema.USER_COLLECTION, "_id"));
			    	user.setUserName(mongodb.getString(DBSchema.USER_COLLECTION, "username"));
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
			    	user.setTheme(mongodb.getString(DBSchema.USER_COLLECTION, "theme"));
			    	list.add(user);
			    }
			    responsedata.setCode(Status.OK.getStatusCode());
			    responsedata.setBody(list);
			}
			else{
				responsedata.setCode(Status.UNAUTHORIZED.getStatusCode());
			    responsedata.setBody("Unauthorized access");
			}
		} catch(Exception ex){
			responsedata.setCode(Status.INTERNAL_SERVER_ERROR.getStatusCode());
		    responsedata.setBody("Error while getting all users. " + ex.getMessage());
		} finally {
			if(mongodb!=null)
				mongodb.close();
		}
		return Response.status(responsedata.getCode()).entity(responsedata).build();
	}
	
	@GET
	@Path("{param}")
	@Produces(MediaType.APPLICATION_JSON)  
	public Response getUser(@PathParam("param") String username,
			@HeaderParam("authorization") String authdata) {
		return CommonLibrary.getUser(username, authdata, false);
	}
	
	@POST
	@Path("add")
	@Consumes(MediaType.APPLICATION_JSON)  
	@Produces(MediaType.APPLICATION_JSON)  
	public Response addUser(User user,
			@HeaderParam("authorization") String authdata) {
		ResponseData responsedata = new ResponseData();
		MongoDB mongodb = null;		
		SearchCriteria criteria = null;
		try {
			if(Authentication.isAuthorized(authdata)){			
				mongodb = new MongoDB();
				criteria = mongodb.searchCriteria();
				criteria.append("username", user.getUserName());
				mongodb.find(DBSchema.USER_COLLECTION, criteria);
				if(mongodb.cursorHasNext(DBSchema.USER_COLLECTION)){
					responsedata.setCode(Status.BAD_REQUEST.getStatusCode());
				    responsedata.setBody("User with user name '"+user.getUserName()+"' already exists.");
				}
				else{
					user.setId(mongodb.getNextSequence(DBSchema.USER_COLLECTION));
					Document document = new Document("_id", user.getId());
					document.append("username", user.getUserName().trim());
					document.append("password", user.getPassword());
					document.append("token", Authentication.getToken(user.getUserName().trim(), user.getPassword()));
					document.append("firstname", user.getFirstName().trim());
					document.append("middlename", user.getMiddleName().trim());
					document.append("lastname", user.getLastName().trim());
					document.append("employeeid", user.getEmployeeID().trim());
					document.append("emailid", user.getEmailID().trim());
					document.append("mobilephone", user.getMobilePhone().trim());
					document.append("phoneextension", user.getPhoneExtension().trim());
					document.append("isadmin", user.getIsadmin());
					document.append("isactive", user.getIsactive());
					document.append("createdon", new Date());
					document.append("createdby", CommonLibrary.getUserName(authdata));
					document.append("theme", "Default");
					mongodb.insertOne(DBSchema.USER_COLLECTION, document);
					responsedata.setCode(Status.CREATED.getStatusCode());
				    responsedata.setBody("User created successfully.");
				} 
			}
			else{
				responsedata.setCode(Status.UNAUTHORIZED.getStatusCode());
			    responsedata.setBody("Unauthorized access");
			}
		}catch(Exception ex) {
			responsedata.setCode(Status.INTERNAL_SERVER_ERROR.getStatusCode());
		    responsedata.setBody("Error while adding new user. " + ex.getMessage());
		}
		finally {
			if(criteria!=null)
				criteria.clear();
			if(mongodb!=null)
				mongodb.close();
		}
		return Response.status(responsedata.getCode()).entity(responsedata).build();
	}
	
	@POST
	@Path("authenticate")
	@Consumes(MediaType.APPLICATION_JSON)  
	@Produces(MediaType.APPLICATION_JSON)  
	public Response authenticate(User user) {
		return CommonLibrary.getUser(user.getUserName(), user.getPassword());
	}
}
