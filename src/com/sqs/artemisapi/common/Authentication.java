package com.sqs.artemisapi.common;

import java.util.Base64;
import java.util.StringTokenizer;

import com.sqs.lib.MongoDB;
import com.sqs.lib.MongoDB.SearchCriteria;

public class Authentication {
	
	public static String getToken(String username, String password) {
		try {
			byte[] encodedBytes = Base64.getEncoder().encode((username+":"+password).getBytes());
			String token = new String(encodedBytes, "UTF-8");
			return token;
		} catch (Exception ex) {
			return "";
		}
	}
	
	public static Boolean isAuthorized(String authdata){
		Boolean authorized = false;
		try {
			authdata = authdata.replace("Basic", "").trim();
			byte[] decodedBytes = Base64.getDecoder().decode(authdata);
			String usernameAndPassword = new String(decodedBytes, "UTF-8");
			final StringTokenizer tokenizer = new StringTokenizer(
					usernameAndPassword, ":");
			final String username = tokenizer.nextToken();
			final String password = tokenizer.nextToken();
			authorized = isAuthenticated(username, password);
		} catch (Exception ex) {
			//throw ex;
		}
		return authorized;
	}
	
	public static boolean isAuthenticated(String username, String password){
		MongoDB mongodb = null;		
		boolean authorized = false;
		SearchCriteria criteria = null;
		try {
			mongodb = new MongoDB();
			criteria = mongodb.searchCriteria();
			criteria.append("username", username);
			criteria.append("password", password);
			mongodb.find(DBSchema.USER_COLLECTION, criteria);
			authorized = mongodb.cursorHasNext(DBSchema.USER_COLLECTION);
		}catch(Exception ex) { }
		finally {
			if(mongodb!=null)
				mongodb.close();
			if(criteria!=null)
				criteria.clear();
		}
		return authorized;
	}
}
