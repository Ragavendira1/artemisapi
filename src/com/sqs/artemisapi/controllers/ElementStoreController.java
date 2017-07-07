package com.sqs.artemisapi.controllers;

import java.util.*;
import javax.ws.rs.*;
import javax.ws.rs.core.*;
import javax.ws.rs.core.Response.Status;
import org.bson.Document;
import com.sqs.artemisapi.common.Authentication;
import com.sqs.artemisapi.common.CommonLibrary;
import com.sqs.artemisapi.common.DBSchema;
import com.sqs.artemisapi.models.ElementStore;
import com.sqs.artemisapi.models.ResponseData;
import com.sqs.lib.*;
import com.sqs.lib.MongoDB.*;

@Path("projects/{projectid}/applications/{applicationid}/elementstores")
public class ElementStoreController {
	@GET
	@Path("/")
	@Produces(MediaType.APPLICATION_JSON)  
	public Response getAllApplications(@HeaderParam("authorization") String authdata, 
			@PathParam("applicationid") long applicationid) {
		ResponseData responsedata = new ResponseData();
		MongoDB mongodb = null;
		SearchCriteria criteria = null;
		try {
			if(Authentication.isAuthorized(authdata)){
				mongodb = new MongoDB();
				criteria = mongodb.searchCriteria();
				criteria.append("applicationid", applicationid);
				mongodb.find(DBSchema.ELEMENT_STORE_COLLECTION, criteria);
				List<ElementStore> list = new ArrayList<ElementStore>();
			    while (mongodb.cursorHasNext(DBSchema.ELEMENT_STORE_COLLECTION)) {
			    	mongodb.cursorNext(DBSchema.ELEMENT_STORE_COLLECTION);
			    	ElementStore elementstore = new ElementStore();
			    	elementstore.setId(mongodb.getLong(DBSchema.ELEMENT_STORE_COLLECTION, "_id"));
			    	elementstore.setName(mongodb.getString(DBSchema.ELEMENT_STORE_COLLECTION, "name"));
			    	elementstore.setDescription(mongodb.getString(DBSchema.ELEMENT_STORE_COLLECTION, "description"));
			    	elementstore.setApplicationid(mongodb.getLong(DBSchema.ELEMENT_STORE_COLLECTION, "applicationid"));
			    	elementstore.setCreatedon(mongodb.getDate(DBSchema.ELEMENT_STORE_COLLECTION, "createdon"));
			    	elementstore.setCreatedby(mongodb.getString(DBSchema.ELEMENT_STORE_COLLECTION, "createdby"));
			    	elementstore.setCreatedbyfullname(CommonLibrary.getUserFullName(elementstore.getCreatedby()));
			    	list.add(elementstore);
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
		    responsedata.setBody("Error while getting element stores. " + ex.getMessage());
		} finally {
			if(criteria!=null)
				criteria.clear();
			if(mongodb!=null)
				mongodb.close();
		}
		return Response.status(responsedata.getCode()).entity(responsedata).build();
	}
	
	@POST
	@Path("add")
	@Consumes(MediaType.APPLICATION_JSON)  
	@Produces(MediaType.APPLICATION_JSON)  
	public Response addElementStore(ElementStore elementstore,
			@HeaderParam("authorization") String authdata,
			@PathParam("applicationid") long applicationid) {
		ResponseData responsedata = new ResponseData();
		MongoDB mongodb = null;
		SearchCriteria criteria = null;
		try {
			if(Authentication.isAuthorized(authdata)){
				mongodb = new MongoDB();
				criteria = mongodb.searchCriteria();
				criteria.append("name", elementstore.getName());
				criteria.append("applicationid", applicationid);
				mongodb.find(DBSchema.ELEMENT_STORE_COLLECTION, criteria);
				if(mongodb.cursorHasNext(DBSchema.ELEMENT_STORE_COLLECTION)){
					responsedata.setCode(Status.BAD_REQUEST.getStatusCode());
				    responsedata.setBody("The element store '"+elementstore.getName()+"' already exists.");
				}
				else{
					elementstore.setApplicationid(applicationid);
					elementstore.setId(mongodb.getNextSequence(DBSchema.ELEMENT_STORE_COLLECTION));
					elementstore.setCreatedby(CommonLibrary.getUserName(authdata));
					Document document = new Document("_id", elementstore.getId());
					document.append("name", elementstore.getName().trim());
					document.append("description", elementstore.getDescription());
					document.append("applicationid", elementstore.getApplicationid());
					document.append("createdon", new Date());
					document.append("createdby", elementstore.getCreatedby());
					mongodb.insertOne(DBSchema.ELEMENT_STORE_COLLECTION, document);
					responsedata.setCode(Status.CREATED.getStatusCode());
				    responsedata.setBody("The element store '"+elementstore.getName().trim()+"' added successfully.");
				} 
			}
			else{
				responsedata.setCode(Status.UNAUTHORIZED.getStatusCode());
			    responsedata.setBody("Unauthorized access");
			}
		}catch(Exception ex) {
			responsedata.setCode(Status.INTERNAL_SERVER_ERROR.getStatusCode());
		    responsedata.setBody("Error while adding new element store. " + ex.getMessage());
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
	public Response updateElementStore(ElementStore elementstore,
			@HeaderParam("authorization") String authdata,
			@PathParam("applicationid") long applicationid) {
		ResponseData responsedata = new ResponseData();
		MongoDB mongodb = null;
		SearchCriteria criteria = null;
		try {
			if(Authentication.isAuthorized(authdata)) {
				mongodb = new MongoDB();
				criteria = mongodb.searchCriteria();
				criteria.append("_id", FilterOperators.NOTEQUALS, elementstore.getId());
				criteria.append("name", elementstore.getName().trim());
				criteria.append("applicationid", applicationid);
				mongodb.find(DBSchema.ELEMENT_STORE_COLLECTION, criteria);
				if(mongodb.cursorHasNext(DBSchema.ELEMENT_STORE_COLLECTION)){
					responsedata.setCode(Status.BAD_REQUEST.getStatusCode());
				    responsedata.setBody("The element store '"+elementstore.getName()+"' already exists.");
				}
				else{
					criteria.clear();
					criteria.append("_id", elementstore.getId());
					Document document = new Document("name", elementstore.getName().trim());
					document.append("description", elementstore.getDescription());
					mongodb.updateOne(DBSchema.ELEMENT_STORE_COLLECTION, criteria, document);
					responsedata.setCode(Status.OK.getStatusCode());
				    responsedata.setBody("The element store '"+elementstore.getName()+"' updated successfully.");
				} 
			}
		}catch(Exception ex) {
			responsedata.setCode(Status.INTERNAL_SERVER_ERROR.getStatusCode());
		    responsedata.setBody("Error while updating element store. " + ex.getMessage());
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
	public Response deleteElementStore(@PathParam("param") long id,
			@HeaderParam("authorization") String authdata) {
		ResponseData responsedata = new ResponseData();
		MongoDB mongodb = null;
		SearchCriteria criteria = null;
		try {
			if(Authentication.isAuthorized(authdata)) {
			mongodb = new MongoDB();
			criteria = mongodb.searchCriteria();
			criteria.append("_id", id);
			mongodb.find(DBSchema.ELEMENT_STORE_COLLECTION, criteria);
				if(mongodb.cursorHasNext(DBSchema.ELEMENT_STORE_COLLECTION)){
					if(mongodb.remove(DBSchema.ELEMENT_STORE_COLLECTION, criteria)){
						responsedata.setCode(Status.OK.getStatusCode());
					    responsedata.setBody("The element store have been deleted successfully.");
					}
				}else {
					responsedata.setCode(Status.BAD_REQUEST.getStatusCode());
				    responsedata.setBody("Unable to find the element store for the ID '" + id + "'");
				}
			}
			else{
				responsedata.setCode(Status.UNAUTHORIZED.getStatusCode());
			    responsedata.setBody("Unauthorized access");
			}
		}catch(Exception ex) {
			responsedata.setCode(Status.INTERNAL_SERVER_ERROR.getStatusCode());
		    responsedata.setBody("Error while deleting element store. " + ex.getMessage());
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
