package com.sqs.lib;

import java.util.Date;
import java.util.Enumeration;
import java.util.Hashtable;

import org.bson.Document;
import com.mongodb.BasicDBObject;
import com.mongodb.MongoClient;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoCursor;
import com.mongodb.client.MongoDatabase;

public class MongoDB {
	private MongoClient mongoClient;
	private MongoDatabase mongodatabase;
	private Hashtable<String, MongoCollection<Document>> collection;
	private Hashtable<String, MongoCursor<Document>> cursor;
	private Hashtable<String, Document> document;
	//private final String dbHost = "192.168.1.100";
	//private final String dbHost = "192.168.22.135";
	private final String dbHost = "localhost";
	private final int dbPort = 27017;
	private final String dbName = "artemisdb";
	//private final String dbUser = "admin";
	//private final String dbPassword = "password";

	public MongoDB(){
		mongodatabase = getDB();
		collection = new Hashtable<String, MongoCollection<Document>>(); 
		cursor = new Hashtable<String, MongoCursor<Document>>(); 
		document = new Hashtable<String, Document>(); 
	}
 
	public MongoDatabase getDB(){
		if(mongoClient == null){
			try {
				mongoClient = new MongoClient(dbHost , dbPort);
			} catch (Exception ex) {
				throw ex;
			}
		}
		if(mongodatabase == null)
			mongodatabase = mongoClient.getDatabase(dbName);
		return mongodatabase;
	}
	
	/*public MongoCollection<Document> getCollection(String collectionname){
		try{
			collection.put(collectionname, mongodatabase.getCollection(collectionname));
			return collection.get(collectionname);
		}
        catch (Exception ex) {throw ex; }
	}*/
	
	public long find(String collectionname){
		try{
			collection.put(collectionname, mongodatabase.getCollection(collectionname));
			cursor.put(collectionname, collection.get(collectionname).find().iterator());
			return collection.get(collectionname).count();
		}
        catch (Exception ex) {throw ex; }
	}
	
	public long find(String collectionname, SearchCriteria criteria){
		try{
			collection.put(collectionname, mongodatabase.getCollection(collectionname));
			cursor.put(collectionname, collection.get(collectionname).find(criteria.getCriteria()).iterator());
			return collection.get(collectionname).count(criteria.getCriteria());
		}
        catch (Exception ex) {throw ex; }
	}
	
	public Boolean cursorHasNext(String collectionname){
		try{
			return cursor.get(collectionname).hasNext();
		}
        catch (Exception ex) {throw ex; }
	}
	
	public Document cursorNext(String collectionname){
		try{
			document.put(collectionname, cursor.get(collectionname).next());
			return document.get(collectionname);
		}
        catch (Exception ex) {throw ex; }
	}
	
	public int getInt(String collectionname, String key){
		try{
			return (int) document.get(collectionname).get(key);
		}
        catch (Exception ex) {throw ex; }	
	}
	
	public long getLong(String collectionname, String key){
		try{
			return (long) document.get(collectionname).get(key);
		}
        catch (Exception ex) {throw ex; }
	}
	
	public String getString(String collectionname, String key){
		try{
			return (String) document.get(collectionname).get(key).toString();
		}
        catch (Exception ex) {throw ex; }
	}
	
	public Boolean getBoolean(String collectionname, String key){
		try{		
			return (Boolean) document.get(collectionname).get(key);
		}
        catch (Exception ex) {throw ex; }
	}
	
	public Date getDate(String collectionname, String key){
		try{
			return (Date) document.get(collectionname).get(key);
		}
        catch (Exception ex) {throw ex; }
	}
	
	private void createCountersCollection(MongoCollection<Document> countersCollection, String name) {
		try{
	        Document document = new Document();
	        document.append("_id", name);
	        document.append("seq", 1);
	        countersCollection.insertOne(document);
        }
        catch (Exception ex) {throw ex; }
    }
 
    public long getNextSequence(String name) {
    	MongoCollection<Document> countercollection = null;
    	try{
	        countercollection = mongodatabase.getCollection(name + "_COUNTER");
	        if (countercollection.count() == 0) {
	            createCountersCollection(countercollection, name);
	            countercollection = mongodatabase.getCollection(name + "_COUNTER");
	        }
	        Document searchQuery = new Document("_id", name);
	        Document increase = new Document("seq", 1);
	        Document updateQuery = new Document("$inc", increase);
	        Document result = countercollection.findOneAndUpdate(searchQuery, updateQuery);
	        return Long.parseLong(result.get("seq").toString());
    	}
        catch (Exception ex) {throw ex; }
		finally{
			countercollection = null;
    	}
    }
    
    public void insertOne(String collectionname, Document document){
    	try{
    		mongodatabase.getCollection(collectionname).insertOne(document);
    	}
        catch (Exception ex) {throw ex; }
    }
    
    public void updateOne(String collectionname, SearchCriteria criteria, Document document){
    	try{
    		mongodatabase.getCollection(collectionname).updateOne(criteria.getCriteria(), new Document("$set", document));
    	}
        catch (Exception ex) {throw ex; }
    }
    
    public Boolean remove(String collectionname, SearchCriteria criteria){
    	try{
    		MongoCollection<Document> collectiontoremove = mongodatabase.getCollection(collectionname);
    		collectiontoremove.deleteOne(criteria.getCriteria());
	    	return true;
    	}
    	catch(Exception ex){
    		throw ex;
    	}
    }
    
    public Boolean removeAll(String collectionname, SearchCriteria criteria){
    	try{
    		MongoCollection<Document> collectiontoremove = mongodatabase.getCollection(collectionname);
    		MongoCursor<Document> cursortoremove = collectiontoremove.find(criteria.getCriteria()).iterator();
	    	while(cursortoremove.hasNext()){
	    		collectiontoremove.deleteOne(cursortoremove.next());
	    	}
	    	return true;
    	}
    	catch(Exception ex){
    		throw ex;
    	}
    }
    
    public SearchCriteria searchCriteria(){
    	try{
    		return new SearchCriteria();
    	}
        catch (Exception ex) {throw ex; }
    }
	
    public void closeCursor(String collectionname){
    	if(cursor.get(collectionname) != null){
			try {
				cursor.get(collectionname).close();
			} catch (Exception ex) {throw ex; }
		}
    }
    
    public void closeDocument(String collectionname){
    	if(document.get(collectionname) != null){
			try {
				document.get(collectionname).clear();
			} catch (Exception ex) {throw ex; }
		}
    }
    
	public void close(){
		try {
			Enumeration<String> cursors = cursor.keys();
			while(cursors.hasMoreElements()) {
				closeCursor((String) cursors.nextElement());
			}
			cursor.clear();
		} catch (Exception ex) {}
		cursor = null;
		try {
			Enumeration<String> documents = document.keys();
			while(documents.hasMoreElements()) {
				closeDocument((String) documents.nextElement());
			}
			document.clear();
		} catch (Exception ex) {}
		document = null;
		try {
			collection.clear();
		} catch (Exception ex) {}
		collection = null;
		
		if(mongoClient != null){
			try {
				mongoClient.close();
			} catch (Exception ex) {throw ex; }
			finally{
				mongoClient = null;
				mongodatabase = null;
			}
		}
	}
	
	public class SearchCriteria{
		private BasicDBObject basicdbobject;
		
		public SearchCriteria(){
			try{
				basicdbobject = new BasicDBObject();
			}
	        catch (Exception ex) {throw ex; }
		}
		
		/*public void append(String key, Object value){
			basicdbobject.append(key, value);
		}*/
		
		public void append(String key, long value){
			try{
				basicdbobject.append(key, value);
			}
	        catch (Exception ex) {throw ex; }
		}
		
		public void append(String key, String value){
			try{
				basicdbobject.append(key, value);
			}
	        catch (Exception ex) {throw ex; }
		}
		
		public void append(String key, Boolean value){
			try{
				basicdbobject.append(key, value);
			}
	        catch (Exception ex) {throw ex; }
		}
		
		public void append(String key, String operator, Object value){
			try{
				basicdbobject.append(key, new BasicDBObject(operator, value));
			}
	        catch (Exception ex) {throw ex; }
		}
		
		public BasicDBObject getCriteria(){
			try{
				return basicdbobject;
			}
	        catch (Exception ex) {throw ex; }
		}
		
		public void clear(){
			try{
				basicdbobject.clear();
			}
	        catch (Exception ex) {throw ex; }
		}
	}
	
	public class FilterOperators{
		public static final String EQUALS = "$eq";
		public static final String NOTEQUALS = "$ne";
		public static final String GREATERTHAN = "$gt";
		public static final String GREATERTHANEQUALTO = "$gte";
		public static final String LESSTHAN = "$lt";
		public static final String LESSTHANEQUALTO = "$lte";
		public static final String IN = "$in";
		public static final String NOTIN = "$nin";
	}
}