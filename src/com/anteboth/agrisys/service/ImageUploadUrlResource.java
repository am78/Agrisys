package com.anteboth.agrisys.service;

import org.restlet.resource.Get;
import org.restlet.resource.ServerResource;

import com.google.appengine.api.blobstore.BlobstoreServiceFactory;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

public class ImageUploadUrlResource extends ServerResource {
	
	
	@Get
	public String retrieve() {
		String url = BlobstoreServiceFactory.getBlobstoreService().createUploadUrl("/upload");
		return url;
	}
	
	@Get ("json") 
	public String retrieveJson() {
		GsonBuilder gb = new GsonBuilder();
		gb.setPrettyPrinting();
		Gson gson = gb.create();
		String s = gson.toJson(retrieve());
		return s;
	}

}
