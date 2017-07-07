package com.sqs.artemisapi.models;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonInclude.Include;

@JsonIgnoreProperties( ignoreUnknown = true )
@JsonInclude(Include.NON_NULL)
public class ResponseData {
	public ResponseData(){
		super();
	}
	@JsonProperty("code")
	public int getCode() {
		return code;
	}
	@JsonProperty("code")
	public void setCode(int code) {
		this.code = code;
	}
	@JsonProperty("body")
	public Object getBody() {
		return body;
	}
	@JsonProperty("body")
	public void setBody(Object body) {
		this.body = body;
	}

	private int code;
	private Object body;
}
