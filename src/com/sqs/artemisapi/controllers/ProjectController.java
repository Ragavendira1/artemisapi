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

@Path("projects")
public class ProjectController {
	
	@GET
	@Path("/")
	@Produces(MediaType.APPLICATION_JSON)  
	public Response getAllProjects(@HeaderParam("authorization") String authdata) {
		ResponseData responsedata = new ResponseData();
		MongoDB mongodb = null;
		try {
			if(Authentication.isAuthorized(authdata)){
				mongodb = new MongoDB();
				mongodb.find(DBSchema.PROJECT_COLLECTION);
				List<Project> list = new ArrayList<Project>();
			    while (mongodb.cursorHasNext(DBSchema.PROJECT_COLLECTION)) {
			    	mongodb.cursorNext(DBSchema.PROJECT_COLLECTION);
			    	Project project = new Project();
			    	project.setId(mongodb.getLong(DBSchema.PROJECT_COLLECTION, "_id"));
			    	project.setName(mongodb.getString(DBSchema.PROJECT_COLLECTION, "name"));
			    	project.setDescription(mongodb.getString(DBSchema.PROJECT_COLLECTION, "description"));
			    	project.setCreatedon(mongodb.getDate(DBSchema.PROJECT_COLLECTION, "createdon"));
			    	project.setCreatedby(mongodb.getString(DBSchema.PROJECT_COLLECTION, "createdby"));
			    	project.setCreatedbyfullname(CommonLibrary.getUserFullName(project.getCreatedby()));
			    	project.setIspublic(mongodb.getBoolean(DBSchema.PROJECT_COLLECTION, "ispublic"));
			    	project.setIsactive(mongodb.getBoolean(DBSchema.PROJECT_COLLECTION, "isactive"));
			    	list.add(project);
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
		    responsedata.setBody("Error while getting all projects. " + ex.getMessage());
		} finally {
			if(mongodb!=null)
				mongodb.close();
		}
		return Response.status(responsedata.getCode()).entity(responsedata).build();
	}
	
	@GET
	@Path("public")
	@Produces(MediaType.APPLICATION_JSON)  
	public Response getPublicProjects(@HeaderParam("authorization") String authdata) {
		ResponseData responsedata = new ResponseData();
		MongoDB mongodb = null;
		SearchCriteria criteria = null;
		try {
			if(Authentication.isAuthorized(authdata)){
				mongodb = new MongoDB();
				criteria = mongodb.searchCriteria();
				criteria.append("ispublic", true);
				criteria.append("isactive", true);
				mongodb.find(DBSchema.PROJECT_COLLECTION, criteria);
				List<Project> list = new ArrayList<Project>();
				criteria.clear();
			    while (mongodb.cursorHasNext(DBSchema.PROJECT_COLLECTION)) {
			    	mongodb.cursorNext(DBSchema.PROJECT_COLLECTION);
			    	criteria.append("projectid", mongodb.getLong(DBSchema.PROJECT_COLLECTION, "_id"));
			    	criteria.append("username", CommonLibrary.getUserName(authdata));
					mongodb.find(DBSchema.PROJECT_ACCESS_COLLECTION, criteria);
					if (mongodb.cursorHasNext(DBSchema.PROJECT_ACCESS_COLLECTION)){
						Project project = new Project();
				    	project.setId(mongodb.getLong(DBSchema.PROJECT_COLLECTION, "_id"));
				    	project.setName(mongodb.getString(DBSchema.PROJECT_COLLECTION, "name"));
				    	project.setDescription(mongodb.getString(DBSchema.PROJECT_COLLECTION, "description"));
				    	project.setCreatedon(mongodb.getDate(DBSchema.PROJECT_COLLECTION, "createdon"));
				    	project.setCreatedby(mongodb.getString(DBSchema.PROJECT_COLLECTION, "createdby"));
				    	project.setCreatedbyfullname(CommonLibrary.getUserFullName(project.getCreatedby()));
				    	project.setIspublic(mongodb.getBoolean(DBSchema.PROJECT_COLLECTION, "ispublic"));
				    	project.setIsactive(mongodb.getBoolean(DBSchema.PROJECT_COLLECTION, "isactive"));
				    	list.add(project);
					}		    
					criteria.clear();
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
		    responsedata.setBody("Error while getting public projects. " + ex.getMessage());
		}finally {
			if(criteria!=null)
				criteria.clear();
			if(mongodb!=null)
				mongodb.close();
		}
		return Response.status(responsedata.getCode()).entity(responsedata).build();
	}
	
	@GET
	@Path("private")
	@Produces(MediaType.APPLICATION_JSON)  
	public Response getPrivateProjects(@HeaderParam("authorization") String authdata) {
		ResponseData responsedata = new ResponseData();
		MongoDB mongodb = null;
		SearchCriteria criteria = null;
		try {
			if(Authentication.isAuthorized(authdata)){
				mongodb = new MongoDB();
				criteria = mongodb.searchCriteria();
				criteria.append("ispublic", false);
				criteria.append("createdby", CommonLibrary.getUserName(authdata));
				mongodb.find(DBSchema.PROJECT_COLLECTION, criteria);
				List<Project> list = new ArrayList<Project>();
			    while (mongodb.cursorHasNext(DBSchema.PROJECT_COLLECTION)) {
			    	mongodb.cursorNext(DBSchema.PROJECT_COLLECTION);
			    	Project project = new Project();
			    	project.setId(mongodb.getLong(DBSchema.PROJECT_COLLECTION, "_id"));
			    	project.setName(mongodb.getString(DBSchema.PROJECT_COLLECTION, "name"));
			    	project.setDescription(mongodb.getString(DBSchema.PROJECT_COLLECTION, "description"));
			    	project.setCreatedon(mongodb.getDate(DBSchema.PROJECT_COLLECTION, "createdon"));
			    	project.setCreatedby(mongodb.getString(DBSchema.PROJECT_COLLECTION, "createdby"));
			    	project.setCreatedbyfullname(CommonLibrary.getUserFullName(project.getCreatedby()));
			    	project.setIspublic(mongodb.getBoolean(DBSchema.PROJECT_COLLECTION, "ispublic"));
			    	project.setIsactive(mongodb.getBoolean(DBSchema.PROJECT_COLLECTION, "isactive"));
			    	list.add(project);
			    }
			    criteria.clear();
			    responsedata.setCode(Status.OK.getStatusCode());
			    responsedata.setBody(list);
			}
			else{
				responsedata.setCode(Status.UNAUTHORIZED.getStatusCode());
			    responsedata.setBody("Unauthorized access");
			}
		} catch(Exception ex){
			responsedata.setCode(Status.INTERNAL_SERVER_ERROR.getStatusCode());
		    responsedata.setBody("Error while getting private projects. " + ex.getMessage());
		}finally {
			if(criteria!=null)
				criteria.clear();
			if(mongodb!=null)
				mongodb.close();
		}
		return Response.status(responsedata.getCode()).entity(responsedata).build();
	}
	
	@GET
	@Path("inactive")
	@Produces(MediaType.APPLICATION_JSON)  
	public Response getInactiveProjects(@HeaderParam("authorization") String authdata) {
		ResponseData responsedata = new ResponseData();
		MongoDB mongodb = null;
		SearchCriteria criteria = null;
		try {
			if(Authentication.isAuthorized(authdata)){
				mongodb = new MongoDB();
				criteria = mongodb.searchCriteria();
				criteria.append("isactive", false);
				mongodb.find(DBSchema.PROJECT_COLLECTION, criteria);
				List<Project> list = new ArrayList<Project>();
			    while (mongodb.cursorHasNext(DBSchema.PROJECT_COLLECTION)) {
			    	mongodb.cursorNext(DBSchema.PROJECT_COLLECTION);
			    	Project project = new Project();
			    	project.setId(mongodb.getLong(DBSchema.PROJECT_COLLECTION, "_id"));
			    	project.setName(mongodb.getString(DBSchema.PROJECT_COLLECTION, "name"));
			    	project.setDescription(mongodb.getString(DBSchema.PROJECT_COLLECTION, "description"));
			    	project.setCreatedon(mongodb.getDate(DBSchema.PROJECT_COLLECTION, "createdon"));
			    	project.setCreatedby(mongodb.getString(DBSchema.PROJECT_COLLECTION, "createdby"));
			    	project.setCreatedbyfullname(CommonLibrary.getUserFullName(project.getCreatedby()));
			    	project.setIspublic(mongodb.getBoolean(DBSchema.PROJECT_COLLECTION, "ispublic"));
			    	project.setIsactive(mongodb.getBoolean(DBSchema.PROJECT_COLLECTION, "isactive"));
			    	list.add(project);
			    }
			    criteria.clear();
			    responsedata.setCode(Status.OK.getStatusCode());
			    responsedata.setBody(list);
			}
			else{
				responsedata.setCode(Status.UNAUTHORIZED.getStatusCode());
			    responsedata.setBody("Unauthorized access");
			}
		} catch(Exception ex){
			responsedata.setCode(Status.INTERNAL_SERVER_ERROR.getStatusCode());
		    responsedata.setBody("Error while getting inactive projects. " + ex.getMessage());
		}finally {
			if(criteria!=null)
				criteria.clear();
			if(mongodb!=null)
				mongodb.close();
		}
		return Response.status(responsedata.getCode()).entity(responsedata).build();
	}
	
	@GET
	@Path("{param}")
	@Produces(MediaType.APPLICATION_JSON)  
	public Response getProject(@PathParam("param") long id,
			@HeaderParam("authorization") String authdata) {
		ResponseData responsedata = new ResponseData();
		MongoDB mongodb = null;
		SearchCriteria criteria = null;
		try {
			if(Authentication.isAuthorized(authdata)){
				mongodb = new MongoDB();
				Project project = null;
				criteria = mongodb.searchCriteria();
				criteria.append("_id", id);
				mongodb.find(DBSchema.PROJECT_COLLECTION, criteria);
				if(mongodb.cursorHasNext(DBSchema.PROJECT_COLLECTION)){
					mongodb.cursorNext(DBSchema.PROJECT_COLLECTION);
					project = new Project();
					project.setId(mongodb.getLong(DBSchema.PROJECT_COLLECTION, "_id"));
			    	project.setName(mongodb.getString(DBSchema.PROJECT_COLLECTION, "name"));
			    	project.setDescription(mongodb.getString(DBSchema.PROJECT_COLLECTION, "description"));
			    	project.setCreatedon(mongodb.getDate(DBSchema.PROJECT_COLLECTION, "createdon"));
			    	project.setCreatedby(mongodb.getString(DBSchema.PROJECT_COLLECTION, "createdby"));
			    	project.setCreatedbyfullname(CommonLibrary.getUserFullName(project.getCreatedby()));
			    	project.setIspublic(mongodb.getBoolean(DBSchema.PROJECT_COLLECTION, "ispublic"));
			    	project.setIsactive(mongodb.getBoolean(DBSchema.PROJECT_COLLECTION, "isactive"));
			    	responsedata.setCode(Status.OK.getStatusCode());
				    responsedata.setBody(project);
				}
				else
				{
					responsedata.setCode(Status.BAD_REQUEST.getStatusCode());
				    responsedata.setBody("Unable to find any projects for the ID '"+id+"'");
				}
			}
			else{
				responsedata.setCode(Status.UNAUTHORIZED.getStatusCode());
			    responsedata.setBody("Unauthorized access");
			}
		} 
		catch(Exception ex){
			responsedata.setCode(Status.INTERNAL_SERVER_ERROR.getStatusCode());
		    responsedata.setBody("Error while getting project. " + ex.getMessage());
		}finally {
			if(criteria!=null)
				criteria.clear();
			if(mongodb!=null)
				mongodb.close();
		}
		return Response.status(responsedata.getCode()).entity(responsedata).build();
	}
	
	@GET
	@Path("{param}/isexists")
	@Produces(MediaType.APPLICATION_JSON)  
	public Response isExists(@PathParam("param") long id,
			@HeaderParam("authorization") String authdata) {
		ResponseData responsedata = new ResponseData();
		MongoDB mongodb = null;
		SearchCriteria criteria = null;
		try {
			if(Authentication.isAuthorized(authdata)){
				mongodb = new MongoDB();
				criteria = mongodb.searchCriteria();
				criteria.append("_id", id);
				criteria.append("isactive", true);
				mongodb.find(DBSchema.PROJECT_COLLECTION, criteria);
				if(mongodb.cursorHasNext(DBSchema.PROJECT_COLLECTION)) {
			    	responsedata.setCode(Status.OK.getStatusCode());
				    responsedata.setBody("The project exists.");
				}
				else {
					responsedata.setCode(Status.BAD_REQUEST.getStatusCode());
				    responsedata.setBody("Unable to find any projects for the ID '"+id+"'");
				}
			}
			else{
				responsedata.setCode(Status.UNAUTHORIZED.getStatusCode());
			    responsedata.setBody("Unauthorized access");
			}
		} 
		catch(Exception ex){
			responsedata.setCode(Status.INTERNAL_SERVER_ERROR.getStatusCode());
		    responsedata.setBody("Error while verifying project. " + ex.getMessage());
		}finally {
			if(criteria!=null)
				criteria.clear();
			if(mongodb!=null)
				mongodb.close();
		}
		return Response.status(responsedata.getCode()).entity(responsedata).build();
	}
	
	@GET
	@Path("{param}/members")
	@Produces(MediaType.APPLICATION_JSON)  
	public Response getProjectMembers(@PathParam("param") long id,
			@HeaderParam("authorization") String authdata) {
		ResponseData responsedata = new ResponseData();
		MongoDB mongodb = null;
		SearchCriteria criteria = null;
		try {
			if(Authentication.isAuthorized(authdata)){
				mongodb = new MongoDB();
				List<ProjectAccess> list = new ArrayList<ProjectAccess>();
				criteria = mongodb.searchCriteria();
				criteria.append("projectid", id);
				mongodb.find(DBSchema.PROJECT_ACCESS_COLLECTION, criteria);
				criteria.clear();
				while(mongodb.cursorHasNext(DBSchema.PROJECT_ACCESS_COLLECTION)){
					mongodb.cursorNext(DBSchema.PROJECT_ACCESS_COLLECTION);
					ProjectAccess projectaccess = new ProjectAccess();
					projectaccess.setId(mongodb.getLong(DBSchema.PROJECT_ACCESS_COLLECTION, "_id"));
					projectaccess.setProjectid(mongodb.getLong(DBSchema.PROJECT_ACCESS_COLLECTION, "projectid"));
					projectaccess.setUsername(mongodb.getString(DBSchema.PROJECT_ACCESS_COLLECTION, "username"));
					projectaccess.setUserfullname(CommonLibrary.getUserFullName(mongodb.getString(DBSchema.PROJECT_ACCESS_COLLECTION, "username")));
					projectaccess.setMappeddate(mongodb.getDate(DBSchema.PROJECT_ACCESS_COLLECTION, "mappeddate"));
					projectaccess.setIsadmin(mongodb.getBoolean(DBSchema.PROJECT_ACCESS_COLLECTION, "isadmin"));
					list.add(projectaccess);
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
		    responsedata.setBody("Error while getting project. " + ex.getMessage());
		}finally {
			if(criteria!=null)
				criteria.clear();
			if(mongodb!=null)
				mongodb.close();
		}
		return Response.status(responsedata.getCode()).entity(responsedata).build();
	}
	
	@GET
	@Path("{param}/access")
	@Produces(MediaType.APPLICATION_JSON)  
	public Response getAccessInformation(@PathParam("param") long id,
			@HeaderParam("authorization") String authdata) {
		ResponseData responsedata = new ResponseData();
		MongoDB mongodb = null;
		SearchCriteria criteria = null;
		try {
			if(Authentication.isAuthorized(authdata)){
				mongodb = new MongoDB();
				criteria = mongodb.searchCriteria();
				criteria.append("projectid", id);
				criteria.append("username", CommonLibrary.getUserName(authdata));
				mongodb.find(DBSchema.PROJECT_ACCESS_COLLECTION, criteria);
				criteria.clear();
				ProjectAccess projectaccess = null;
				if(mongodb.cursorHasNext(DBSchema.PROJECT_ACCESS_COLLECTION)){
					mongodb.cursorNext(DBSchema.PROJECT_ACCESS_COLLECTION);
					projectaccess = new ProjectAccess();
					projectaccess.setId(mongodb.getLong(DBSchema.PROJECT_ACCESS_COLLECTION, "_id"));
					projectaccess.setProjectid(mongodb.getLong(DBSchema.PROJECT_ACCESS_COLLECTION, "projectid"));
					projectaccess.setUsername(mongodb.getString(DBSchema.PROJECT_ACCESS_COLLECTION, "username"));
					projectaccess.setUserfullname(CommonLibrary.getUserFullName(mongodb.getString(DBSchema.PROJECT_ACCESS_COLLECTION, "username")));
					projectaccess.setMappeddate(mongodb.getDate(DBSchema.PROJECT_ACCESS_COLLECTION, "mappeddate"));
					projectaccess.setIsadmin(mongodb.getBoolean(DBSchema.PROJECT_ACCESS_COLLECTION, "isadmin"));
				}
				responsedata.setCode(Status.OK.getStatusCode());
			    responsedata.setBody(projectaccess);
			}
			else{
				responsedata.setCode(Status.UNAUTHORIZED.getStatusCode());
			    responsedata.setBody("Unauthorized access");
			}
		} 
		catch(Exception ex){
			responsedata.setCode(Status.INTERNAL_SERVER_ERROR.getStatusCode());
		    responsedata.setBody("Error while getting project. " + ex.getMessage());
		}finally {
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
	public Response addProject(Project project,
			@HeaderParam("authorization") String authdata) {
		ResponseData responsedata = new ResponseData();
		MongoDB mongodb = null;
		SearchCriteria criteria = null;
		try {
			if(Authentication.isAuthorized(authdata)){
				mongodb = new MongoDB();
				criteria = mongodb.searchCriteria();
				criteria.append("name", project.getName());
				mongodb.find(DBSchema.PROJECT_COLLECTION, criteria);
				if(mongodb.cursorHasNext(DBSchema.PROJECT_COLLECTION)){
					responsedata.setCode(Status.BAD_REQUEST.getStatusCode());
				    responsedata.setBody("The project '"+project.getName()+"' already exists.");
				}
				else{
					project.setId(mongodb.getNextSequence(DBSchema.PROJECT_COLLECTION));
					project.setCreatedby(CommonLibrary.getUserName(authdata));
					Document document = new Document("_id", project.getId());
					document.append("name", project.getName().trim());
					document.append("description", project.getDescription());
					document.append("createdon", new Date());
					document.append("createdby", project.getCreatedby());
					document.append("ispublic", project.getIspublic());
					document.append("isactive", true);
					mongodb.insertOne(DBSchema.PROJECT_COLLECTION, document);
					document = new Document("_id",mongodb.getNextSequence(DBSchema.PROJECT_ACCESS_COLLECTION));
					document.append("username", project.getCreatedby());
					document.append("projectid", project.getId());
					document.append("mappeddate", new Date());
					document.append("isadmin", true);
					mongodb.insertOne(DBSchema.PROJECT_ACCESS_COLLECTION, document);
					responsedata.setCode(Status.CREATED.getStatusCode());
				    responsedata.setBody("Project created successfully.");
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
	public Response updateProject(Project project,
			@HeaderParam("authorization") String authdata) {
		ResponseData responsedata = new ResponseData();
		MongoDB mongodb = null;
		SearchCriteria criteria = null;
		try {
			if(Authentication.isAuthorized(authdata)){
				mongodb = new MongoDB();
				criteria = mongodb.searchCriteria();
				criteria.append("_id", FilterOperators.NOTEQUALS, project.getId());
				criteria.append("name", project.getName().trim());
				mongodb.find(DBSchema.PROJECT_COLLECTION, criteria);
				if(mongodb.cursorHasNext(DBSchema.PROJECT_COLLECTION)){
					responsedata.setCode(Status.BAD_REQUEST.getStatusCode());
				    responsedata.setBody("The project '"+project.getName()+"' already exists.");
				}
				else{
					criteria.clear();
					criteria.append("_id", project.getId());
					Document document = new Document("name", project.getName().trim());
					document.append("description", project.getDescription());
					mongodb.updateOne(DBSchema.PROJECT_COLLECTION, criteria, document);
					responsedata.setCode(Status.OK.getStatusCode());
				    responsedata.setBody("The project '"+project.getName()+"' updated successfully.");
				} 
			}
			else{
				responsedata.setCode(Status.UNAUTHORIZED.getStatusCode());
			    responsedata.setBody("Unauthorized access");
			}
		}catch(Exception ex) {
			responsedata.setCode(Status.INTERNAL_SERVER_ERROR.getStatusCode());
		    responsedata.setBody("Error while updating project. " + ex.getMessage());
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
	public Response deleteProject(@PathParam("param") long id,
			@HeaderParam("authorization") String authdata) {
		ResponseData responsedata = new ResponseData();
		MongoDB mongodb = null;
		SearchCriteria criteria = null;
		try {
			if(CommonLibrary.isProjectAdmin(authdata, id)){
				mongodb = new MongoDB();
				criteria = mongodb.searchCriteria();
				criteria.append("_id", id);
				criteria.append("ispublic", true);
				mongodb.find(DBSchema.PROJECT_COLLECTION, criteria);
				if(mongodb.cursorHasNext(DBSchema.PROJECT_COLLECTION)){
					mongodb.updateOne(DBSchema.PROJECT_COLLECTION, criteria, new Document("isactive", false));
					responsedata.setCode(Status.OK.getStatusCode());
				    responsedata.setBody("Project deactivated successfully.");
				}
				else
				{
					criteria.clear();
					criteria.append("_id", id);
					mongodb.find(DBSchema.PROJECT_COLLECTION, criteria);
					if(mongodb.cursorHasNext(DBSchema.PROJECT_COLLECTION)){
						if(mongodb.remove(DBSchema.PROJECT_COLLECTION, criteria)){
							if(mongodb.removeAll(DBSchema.PROJECT_ACCESS_COLLECTION, criteria)) {
								criteria.clear();
								criteria.append("projectid", id);
								mongodb.removeAll(DBSchema.APP_COLLECTION, criteria);
						    	responsedata.setCode(Status.OK.getStatusCode());
							    responsedata.setBody("Project and all its resources have been deleted successfully.");
							}
							else {
								responsedata.setCode(Status.EXPECTATION_FAILED.getStatusCode());
							    responsedata.setBody("Error while deleting project resources.");
							}
						}
					}
					else {
						responsedata.setCode(Status.BAD_REQUEST.getStatusCode());
					    responsedata.setBody("Unable to find any projects for the ID '" + id + "'");
					}
					criteria.clear();
				}
			}
			else{
				responsedata.setCode(Status.UNAUTHORIZED.getStatusCode());
			    responsedata.setBody(id + " Unauthorized access "+ authdata );
			}
		} 
		catch(Exception ex){
			responsedata.setCode(Status.INTERNAL_SERVER_ERROR.getStatusCode());
		    responsedata.setBody("Error while deleting project. " + ex.getMessage());
		}finally {
			if(criteria!=null)
				criteria.clear();
			if(mongodb!=null)
				mongodb.close();
		}
		return Response.status(responsedata.getCode()).entity(responsedata).build();
	}
	/*
	@GET
	@Path("{param}/isadmin")
	@Produces(MediaType.TEXT_PLAIN)  
	public Response isProjectAdmin(@HeaderParam("authorization") String authdata, 
			@PathParam("param") long id){
		try {
			if (CommonLibrary.isProjectAdmin(authdata, id))
				return Response.status(200).entity("admin").build();
			else
				return Response.status(200).entity("user").build();
		}
		catch(Exception ex){
			return Response.status(200).entity(ex.getMessage()).build();
		}finally {
			
		}
	}*/
}
