package com.anteboth.agrisys.server;
import java.io.IOException;

import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.google.appengine.api.blobstore.BlobKey;
import com.google.appengine.api.blobstore.BlobstoreService;
import com.google.appengine.api.blobstore.BlobstoreServiceFactory;
import com.google.appengine.api.images.Image;
import com.google.appengine.api.images.ImagesService;
import com.google.appengine.api.images.ImagesServiceFactory;
import com.google.appengine.api.images.Transform;

@SuppressWarnings("serial")
public class Serve extends HttpServlet {
	
	private static final String PREVIEW_KEY = "preview";
	private static final int previewSize = 200;
	
	private BlobstoreService blobstoreService = BlobstoreServiceFactory.getBlobstoreService();

	public void doGet(HttpServletRequest req, HttpServletResponse res)
	throws IOException {
		BlobKey blobKey = new BlobKey(req.getParameter("blob-key"));
		
		if (PREVIEW_KEY.equals(req.getParameter("qual"))) {
			ImagesService imagesService = ImagesServiceFactory.getImagesService();
			Image oldImage = ImagesServiceFactory.makeImageFromBlob(blobKey);
			Transform resize = ImagesServiceFactory.makeResize(previewSize, previewSize);
			Image newImage = imagesService.applyTransform(resize, oldImage);
			byte[] newImageData = newImage.getImageData();
			res.getOutputStream().write(newImageData);
			res.getOutputStream().close();
		}
		else {
			res.setContentType("image/JPEG");
			blobstoreService.serve(blobKey, res);
		}
	}
}