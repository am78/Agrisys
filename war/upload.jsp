<%@ page import="com.google.appengine.api.blobstore.BlobstoreServiceFactory" %>
<%@ page import="com.google.appengine.api.blobstore.BlobstoreService" %>

<%
    BlobstoreService blobstoreService = BlobstoreServiceFactory.getBlobstoreService();
	String refId = request.getParameter("refId");
	String success = request.getParameter("success");
	String msg = "";
	boolean needsRefresh = false;
	if (success != null) {
		if (success.equals("true")) {
			msg = "Upload erfolgreich!";
			needsRefresh = true;
		} else {
			msg = "Fehler beim Upload!";
		}
	}
	
%>


<html>
    <head>
        <title>Datei-Upload</title>
        <style type="text/css">
       	 .gwt-Frame {  border: 0; } 
        </style>
    </head>
    <body>
       	<%= msg %><br/>
       	<%
       	if (needsRefresh) {
       	%> 
     		<script type="text/javascript">
     			//window.hello("Manolo");
       		</script>
		<% 
       	}
		%>       		
        <form action="<%= blobstoreService.createUploadUrl("/upload") %>" method="post" enctype="multipart/form-data">
           	Bild-Datei auswählen
           	<input type="file" name="myFile"><input type="submit" value="Upload">
           	<input type="hidden" name="refId" value="<%=refId%>">
        </form>
    </body>
</html>