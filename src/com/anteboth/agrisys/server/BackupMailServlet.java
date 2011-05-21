package com.anteboth.agrisys.server;

import java.io.IOException;
import java.io.StringWriter;
import java.text.DateFormat;
import java.util.Date;
import java.util.Properties;

import javax.mail.Message;
import javax.mail.Multipart;
import javax.mail.Session;
import javax.mail.Transport;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeBodyPart;
import javax.mail.internet.MimeMessage;
import javax.mail.internet.MimeMultipart;
import javax.servlet.GenericServlet;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;

@SuppressWarnings("serial")
public class BackupMailServlet extends GenericServlet {

	@Override
	public void service(ServletRequest request, ServletResponse response) 
	throws ServletException, IOException {

		StringWriter sw = new StringWriter();
		BackupServlet.writeXmlData(sw);
		try {
			sendMail(sw.toString());
			response.getWriter().write("Backup mail sent.");
		} catch (Exception e) {
			e.printStackTrace();
			response.getWriter().write("Error while sending backup mail: " + e.getLocalizedMessage() + "\n" + e);
		}
	}

	private void sendMail(String data) throws Exception {
		Properties props = new Properties();
		Session session = Session.getDefaultInstance(props, null);

		String date = DateFormat.getDateTimeInstance().format(new Date());
		String msgBody = "Agriys Backup vom:\n\t" + date;
		String subject = "Agrisys Backup - " + date;
		String from = "anteboth@googlemail.com";
		String to = "anteboth@googlemail.com";

		try {
			Message msg = new MimeMessage(session);
			msg.setFrom(new InternetAddress(from));
			msg.addRecipient(Message.RecipientType.TO,new InternetAddress(to));
			msg.setSubject(subject);
			msg.setText(msgBody);

			//create multipart message
			Multipart mp = new MimeMultipart();

			//add message attachment part
			MimeBodyPart attachment = new MimeBodyPart();
			attachment.setText(data);
			attachment.setFileName("agrisys_backup.xml");
			mp.addBodyPart(attachment); 
			
			//set the content of the message to be the multipart 
			msg.setContent(mp); 
			msg.saveChanges();//I think this is necessary,but, not sure.... 
	
			Transport.send(msg);
		} catch (Exception e) {
			handleException(e);
		}
	}

	private void handleException(Exception e) throws Exception {
		e.printStackTrace();
		throw e;
	}
}
