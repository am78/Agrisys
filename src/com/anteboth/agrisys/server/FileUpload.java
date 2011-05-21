package com.anteboth.agrisys.server;

import java.io.IOException;
import java.util.Map;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.google.appengine.api.blobstore.BlobKey;
import com.google.appengine.api.blobstore.BlobstoreService;
import com.google.appengine.api.blobstore.BlobstoreServiceFactory;

/**
 * FileUpload servlet.
 * 
 * @author michael
 */
public class FileUpload extends HttpServlet {

	private BlobstoreService blobstoreService = BlobstoreServiceFactory.getBlobstoreService();

	public void doPost(HttpServletRequest req, HttpServletResponse res)
	throws ServletException, IOException {
		
		//this is the reference id to which the uploaded resources should be assigned
		String refId = req.getParameter("refId");

		//get the uploaded data
		Map<String, BlobKey> blobs = blobstoreService.getUploadedBlobs(req);
		BlobKey blobKey = blobs.get("image");
		
		try {
			
			//if something went wrong set returns message to error msg
			if (blobKey == null) {
				//res.sendRedirect("/upload.jsp?refId=" + refId + "&success=false");
			} 
			//uploaded successfully, set return message to upload successfull
			else {
				//now assign the blobKey to the referenceID
				ServiceManager.getInstance().assignBlob(refId, blobKey.getKeyString());

				//res.sendRedirect("/upload.jsp?refId=" + refId + "&success=true");
				//res.sendRedirect("/upload/serve?blob-key=" + blobKey.getKeyString());
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
}