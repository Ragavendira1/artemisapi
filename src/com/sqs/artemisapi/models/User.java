package com.sqs.artemisapi.models;

import java.util.Date;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;

@JsonIgnoreProperties( ignoreUnknown = true )
@JsonInclude(Include.NON_NULL)
public class User {
	public User(){
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
	public String getUserName() {
		return username;
	}
	@JsonProperty("username")
	public void setUserName(String username) {
		this.username = username;
	}
	@JsonProperty("password")
	public String getPassword() {
		return password;
	}
	@JsonProperty("password")
	public void setPassword(String password) {
		this.password = password;
	}
	@JsonProperty("token")
	public String getToken() {
		return token;
	}
	@JsonProperty("token")
	public void setToken(String token) {
		this.token = token;
	}
	@JsonProperty("firstname")
	public String getFirstName() {
		return firstname;
	}
	@JsonProperty("firstname")
	public void setFirstName(String firstname) {
		this.firstname = firstname;
	}
	@JsonProperty("middlename")
	public String getMiddleName() {
		if(middlename==null)
			middlename="";
		return middlename;
	}
	@JsonProperty("middlename")
	public void setMiddleName(String middlename) {
		this.middlename = middlename;
	}
	@JsonProperty("lastname")
	public String getLastName() {
		return lastname;
	}
	@JsonProperty("lastname")
	public void setLastName(String lastname) {
		this.lastname = lastname;
	}
	@JsonProperty("fullname")
	public String getFullname() {
		if (getMiddleName().trim().equals("")) 
			fullname = firstname.trim() + " " + lastname.trim();
		else
			fullname = firstname.trim() + " " + middlename.trim() + " " + lastname.trim();

		return fullname;
	}
	@JsonProperty("employeeid")
	public String getEmployeeID() {
		return employeeid;
	}
	@JsonProperty("employeeid")
	public void setEmployeeID(String employeeid) {
		this.employeeid = employeeid;
	}
	@JsonProperty("emailid")
	public String getEmailID() {
		return emailid;
	}
	@JsonProperty("emailid")
	public void setEmailID(String emailid) {
		this.emailid = emailid;
	}
	@JsonProperty("mobilephone")
	public String getMobilePhone() {
		return mobilephone;
	}
	@JsonProperty("mobilephone")
	public void setMobilePhone(String mobilephone) {
		this.mobilephone = mobilephone;
	}
	@JsonProperty("phoneextension")
	public String getPhoneExtension() {
		return phoneextension;
	}
	@JsonProperty("phoneextension")
	public void setPhoneExtension(String phoneextension) {
		this.phoneextension = phoneextension;
	}
	@JsonProperty("isactive")
	public Boolean getIsactive() {
		return isactive;
	}
	@JsonProperty("isactive")
	public void setIsactive(Boolean isactive) {
		this.isactive = isactive;
	}
	@JsonProperty("isadmin")
	public Boolean getIsadmin() {
		return isadmin;
	}
	@JsonProperty("isadmin")
	public void setIsadmin(Boolean isadmin) {
		this.isadmin = isadmin;
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
	@JsonProperty("theme")
	public String getTheme() {
		return theme;
	}
	@JsonProperty("theme")
	public void setTheme(String theme) {
		this.theme = theme;
	}

	private long id;
	private String username;
	private String password;
	private String token;
	private String firstname;
	private String middlename;
	private String lastname;
	private String fullname;
	private String employeeid;
	private String emailid;
	private String mobilephone;
	private String phoneextension;
	private Boolean isactive;
	private Boolean isadmin;
	private Date createdon;
	private String createdby;
	private String theme="";
}
