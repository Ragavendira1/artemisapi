package com.sqs.artemisapi.models;

import java.util.Date;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;

@JsonIgnoreProperties( ignoreUnknown = true )
@JsonInclude(Include.NON_NULL)
public class ElementStore {
	public ElementStore(){
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
	@JsonProperty("description")
	public String getDescription() {
		return description;
	}
	@JsonProperty("description")
	public void setDescription(String description) {
		this.description = description;
	}
	@JsonProperty("applicationid")
	public long getApplicationid() {
		return applicationid;
	}
	@JsonProperty("applicationid")
	public void setApplicationid(long applicationid) {
		this.applicationid = applicationid;
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
	private long id;
	private String name;
	private String description;
	private long applicationid;
	private Date createdon;
	private String createdby;
	private String createdbyfullname;
}
