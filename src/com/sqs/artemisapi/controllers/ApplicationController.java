package com.sqs.artemisapi.controllers;

import java.util.*;
import javax.ws.rs.*;
import javax.ws.rs.core.*;
import javax.ws.rs.core.Response.Status;
import org.bson.Document;
import com.sqs.artemisapi.common.Authentication;
import com.sqs.artemisapi.common.CommonLibrary;
import com.sqs.artemisapi.common.DBSchema;
import com.sqs.artemisapi.models.Application;
import com.sqs.artemisapi.models.ResponseData;
import com.sqs.lib.*;
import com.sqs.lib.MongoDB.*;

@Path("projects/{projectid}/applications")
public class ApplicationController {
	@GET
	@Path("/")
	@Produces(MediaType.APPLICATION_JSON)  
	public Response getAllApplications(@HeaderParam("authorization") String authdata, 
			@PathParam("projectid") long projectid) {
		ResponseData responsedata = new ResponseData();
		MongoDB mongodb = null;
		SearchCriteria criteria = null;
		try {
			if(Authentication.isAuthorized(authdata)){
				mongodb = new MongoDB();
				criteria = mongodb.searchCriteria();
				criteria.append("projectid", projectid);
				mongodb.find(DBSchema.APP_COLLECTION, criteria);
				List<Application> list = new ArrayList<Application>();
			    while (mongodb.cursorHasNext(DBSchema.APP_COLLECTION)) {
			    	mongodb.cursorNext(DBSchema.APP_COLLECTION);
			    	Application application = new Application();
			    	application.setId(mongodb.getLong(DBSchema.APP_COLLECTION, "_id"));
			    	application.setName(mongodb.getString(DBSchema.APP_COLLECTION, "name"));
			    	application.setType(mongodb.getString(DBSchema.APP_COLLECTION, "type"));
			    	application.setDescription(mongodb.getString(DBSchema.APP_COLLECTION, "description"));
			    	application.setTechnology(mongodb.getString(DBSchema.APP_COLLECTION, "technology"));
			    	application.setProjectid(mongodb.getLong(DBSchema.APP_COLLECTION, "projectid"));
			    	application.setCreatedon(mongodb.getDate(DBSchema.APP_COLLECTION, "createdon"));
			    	application.setCreatedby(mongodb.getString(DBSchema.APP_COLLECTION, "createdby"));
			    	application.setCreatedbyfullname(CommonLibrary.getUserFullName(application.getCreatedby()));
			    	application.setIsactive(mongodb.getBoolean(DBSchema.APP_COLLECTION, "isactive"));
			    	list.add(application);
			    }
			    responsedata.setCode(Status.OK.getStatusCode());
			    responsedata.setBody(list);
			}
			else{
				responsedata.setCode(Status.UNAUTHORIZED.getStatusCode());
			    responsedata.setBody("Unauthorized access");
			}
		}
		catch(Exception ex){
			responsedata.setCode(Status.INTERNAL_SERVER_ERROR.getStatusCode());
		    responsedata.setBody("Error while getting all applications. " + ex.getMessage());
		} finally {
			if(criteria!=null)
				criteria.clear();
			if(mongodb!=null)
				mongodb.close();
		}
		return Response.status(responsedata.getCode()).entity(responsedata).build();
	}
	
	@GET
	@Path("types")
	@Produces(MediaType.APPLICATION_JSON)  
	public Response getTypes(@HeaderParam("authorization") String authdata) {
		List<String> types = new ArrayList<String> ();
		ResponseData responsedata = new ResponseData();
		try {
			types.add("Web-Desktop");
			types.add("Web-Responsive");
			types.add("Mobile-Native-Android");
			types.add("Mobile-Native-iOS");
			types.add("Mobile-Native-Windows");
			types.add("Mobile-Hybrid-Android");
			types.add("Mobile-Hybrid-iOS");
			types.add("Mobile-Hybrid-Windows");
			types.add("RESTful API");
			types.add("SOAP");
			types.add("Java");
			types.add(".Net WinForms");
			types.add("WPF.NET");
			types.add("IBM Mainframe");
			responsedata.setCode(Status.OK.getStatusCode());
		    responsedata.setBody(types);
		}
		catch(Exception ex){
			responsedata.setCode(Status.INTERNAL_SERVER_ERROR.getStatusCode());
		    responsedata.setBody("Error while getting application types. " + ex.getMessage());
		} finally {
			types = null;
		}
		return Response.status(responsedata.getCode()).entity(responsedata).build();
	}
	
	@POST
	@Path("add")
	@Consumes(MediaType.APPLICATION_JSON)  
	@Produces(MediaType.APPLICATION_JSON)  
	public Response addApplication(Application application,
			@HeaderParam("authorization") String authdata,
			@PathParam("projectid") long projectid) {
		ResponseData responsedata = new ResponseData();
		MongoDB mongodb = null;
		SearchCriteria criteria = null;
		//application.setProjectid(projectid);
		try {
			if(Authentication.isAuthorized(authdata)){
				mongodb = new MongoDB();
				criteria = mongodb.searchCriteria();
				criteria.append("name", application.getName());
				criteria.append("projectid", projectid);
				mongodb.find(DBSchema.APP_COLLECTION, criteria);
				if(mongodb.cursorHasNext(DBSchema.APP_COLLECTION)){
					responsedata.setCode(Status.BAD_REQUEST.getStatusCode());
				    responsedata.setBody("The application '"+application.getName()+"' already exists.");
				}
				else{
					application.setProjectid(projectid);
					application.setId(mongodb.getNextSequence(DBSchema.APP_COLLECTION));
					application.setCreatedby(CommonLibrary.getUserName(authdata));
					Document document = new Document("_id", application.getId());
					document.append("name", application.getName().trim());
					document.append("type", application.getType().trim());
					document.append("description", application.getDescription());
					document.append("technology", application.getTechnology());
					document.append("projectid", application.getProjectid());
					document.append("createdon", new Date());
					document.append("createdby", application.getCreatedby());
					document.append("isactive", true);
					mongodb.insertOne(DBSchema.APP_COLLECTION, document);
					responsedata.setCode(Status.CREATED.getStatusCode());
				    responsedata.setBody("The application '"+application.getName().trim()+"' added successfully.");
				} 
			}
			else{
				responsedata.setCode(Status.UNAUTHORIZED.getStatusCode());
			    responsedata.setBody("Unauthorized access");
			}
		}catch(Exception ex) {
			responsedata.setCode(Status.INTERNAL_SERVER_ERROR.getStatusCode());
		    responsedata.setBody("Error while adding new project. " + ex.getMessage());
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
	@Path("update")
	@Consumes(MediaType.APPLICATION_JSON)  
	@Produces(MediaType.APPLICATION_JSON)  
	public Response updateProject(Application application,
			@HeaderParam("authorization") String authdata,
			@PathParam("projectid") long projectid) {
		ResponseData responsedata = new ResponseData();
		MongoDB mongodb = null;
		SearchCriteria criteria = null;
		try {
			if(Authentication.isAuthorized(authdata)) {
				mongodb = new MongoDB();
				criteria = mongodb.searchCriteria();
				criteria.append("_id", FilterOperators.NOTEQUALS, application.getId());
				criteria.append("name", application.getName().trim());
				criteria.append("projectid", projectid);
				mongodb.find(DBSchema.APP_COLLECTION, criteria);
				if(mongodb.cursorHasNext(DBSchema.APP_COLLECTION)){
					responsedata.setCode(Status.BAD_REQUEST.getStatusCode());
				    responsedata.setBody("The application '"+application.getName()+"' already exists.");
				}
				else{
					criteria.clear();
					criteria.append("_id", application.getId());
					Document document = new Document("name", application.getName().trim());
					document.append("type", application.getType().trim());
					document.append("description", application.getDescription());
					document.append("technology", application.getTechnology());
					mongodb.updateOne(DBSchema.APP_COLLECTION, criteria, document);
					responsedata.setCode(Status.OK.getStatusCode());
				    responsedata.setBody("The application '"+application.getName()+"' updated successfully.");
				} 
			}
		}catch(Exception ex) {
			responsedata.setCode(Status.INTERNAL_SERVER_ERROR.getStatusCode());
		    responsedata.setBody("Error while updating application. " + ex.getMessage());
		}
		finally {
			if(criteria!=null)
				criteria.clear();
			if(mongodb!=null)
				mongodb.close();
		}
		return Response.status(responsedata.getCode()).entity(responsedata).build();
	}
	
	@DELETE
	@Path("{param}/delete")
	@Produces(MediaType.APPLICATION_JSON)
	public Response deleteApplication(@PathParam("param") long id,
			@HeaderParam("authorization") String authdata) {
		ResponseData responsedata = new ResponseData();
		MongoDB mongodb = null;
		SearchCriteria criteria = null;
		try {
			if(Authentication.isAuthorized(authdata)) {
			mongodb = new MongoDB();
			criteria = mongodb.searchCriteria();
			criteria.append("_id", id);
			mongodb.find(DBSchema.APP_COLLECTION, criteria);
				if(mongodb.cursorHasNext(DBSchema.APP_COLLECTION)){
					if(mongodb.remove(DBSchema.APP_COLLECTION, criteria)){
						criteria.clear();
						criteria.append("applicationid", id);
						mongodb.removeAll(DBSchema.ELEMENT_STORE_COLLECTION, criteria);
						responsedata.setCode(Status.OK.getStatusCode());
					    responsedata.setBody("The application have been deleted successfully.");
					}
				}else {
					responsedata.setCode(Status.BAD_REQUEST.getStatusCode());
				    responsedata.setBody("Unable to find the application for the ID '" + id + "'");
				}
			}
			else{
				responsedata.setCode(Status.UNAUTHORIZED.getStatusCode());
			    responsedata.setBody("Unauthorized access");
			}
		}catch(Exception ex) {
			responsedata.setCode(Status.INTERNAL_SERVER_ERROR.getStatusCode());
		    responsedata.setBody("Error while deleting application. " + ex.getMessage());
		}
		finally {
			if(criteria!=null)
				criteria.clear();
			if(mongodb!=null)
				mongodb.close();
		}
		return Response.status(responsedata.getCode()).entity(responsedata).build();
	}
}
