package com.sqs.artemisapi.models;

import java.util.Date;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;

@JsonIgnoreProperties( ignoreUnknown = true )
@JsonInclude(Include.NON_NULL)
public class ProjectAccess {
	public ProjectAccess(){
		super();
	}
	@JsonProperty("id")
	public long getId() {
		return id;
	}
	@JsonProperty("id")
	public void setId(long id) {
		this.id = id;
	}
	@JsonProperty("username")
	public String getUsername() {
		return username;
	}
	@JsonProperty("username")
	public void setUsername(String username) {
		this.username = username;
	}
	@JsonProperty("userfullname")
	public String getUserfullname() {
		return userfullname;
	}
	@JsonProperty("userfullname")
	public void setUserfullname(String userfullname) {
		this.userfullname = userfullname;
	}
	@JsonProperty("projectid")
	public long getProjectid() {
		return projectid;
	}
	@JsonProperty("projectid")
	public void setProjectid(long projectid) {
		this.projectid = projectid;
	}
	@JsonProperty("mappeddate")
	public Date getMappeddate() {
		return mappeddate;
	}
	@JsonProperty("mappeddate")
	public void setMappeddate(Date mappeddate) {
		this.mappeddate = mappeddate;
	}
	@JsonProperty("isadmin")
	public Boolean getIsadmin() {
		return isadmin;
	}
	@JsonProperty("isadmin")
	public void setIsadmin(Boolean isadmin) {
		this.isadmin = isadmin;
	}
	@JsonProperty("role")
	public String getRole() {
		if(isadmin)
			role = "Project Administrator";
		else
			role = "User";
		return role;
	}
	
	private long id;
	private String username;
	private String userfullname;
	private long projectid;
	private Date mappeddate;
	private Boolean isadmin;
	private String role;
}
