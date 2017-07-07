package com.sqs.artemisapi.models;

import java.util.Date;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;

@JsonIgnoreProperties( ignoreUnknown = true )
@JsonInclude(Include.NON_NULL)
public class Application {
	public Application(){
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
	@JsonProperty("name")
	public String getName() {
		return name;
	}
	@JsonProperty("name")
	public void setName(String name) {
		this.name = name;
	}
	@JsonProperty("type")
	public String getType() {
		return type;
	}
	@JsonProperty("type")
	public void setType(String type) {
		this.type = type;
	}
	@JsonProperty("description")
	public String getDescription() {
		return description;
	}
	@JsonProperty("description")
	public void setDescription(String description) {
		this.description = description;
	}
	@JsonProperty("technology")
	public String getTechnology() {
		return technology;
	}
	@JsonProperty("technology")
	public void setTechnology(String technology) {
		this.technology = technology;
	}
	@JsonProperty("projectid")
	public long getProjectid() {
		return projectid;
	}
	@JsonProperty("projectid")
	public void setProjectid(long projectid) {
		this.projectid = projectid;
	}
	@JsonProperty("createdon")
	public Date getCreatedon() {
		return createdon;
	}
	@JsonProperty("createdon")
	public void setCreatedon(Date createdon) {
		this.createdon = createdon;
	}
	@JsonProperty("createdby")
	public String getCreatedby() {
		return createdby;
	}
	@JsonProperty("createdby")
	public void setCreatedby(String createdby) {
		this.createdby = createdby;
	}
	@JsonProperty("createdbyfullname")
	public String getCreatedbyfullname() {
		return createdbyfullname;
	}
	@JsonProperty("createdbyfullname")
	public void setCreatedbyfullname(String createdbyfullname) {
		this.createdbyfullname = createdbyfullname;
	}
	@JsonProperty("isactive")
	public boolean isIsactive() {
		return isactive;
	}
	@JsonProperty("isactive")
	public void setIsactive(boolean isactive) {
		this.isactive = isactive;
	}
	private long id;
	private String name;
	private String type;
	private String description;
	private String technology;
	private long projectid;
	private Date createdon;
	private String createdby;
	private String createdbyfullname;
	private boolean isactive;
}
