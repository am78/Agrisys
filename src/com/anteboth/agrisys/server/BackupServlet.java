package com.anteboth.agrisys.server;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.io.Writer;
import java.text.DateFormat;
import java.util.Date;
import java.util.List;
import java.util.Properties;
import java.util.zip.ZipEntry;
import java.util.zip.ZipOutputStream;

import javax.mail.Message;
import javax.mail.MessagingException;
import javax.mail.Multipart;
import javax.mail.Session;
import javax.mail.Transport;
import javax.mail.internet.AddressException;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeBodyPart;
import javax.mail.internet.MimeMessage;
import javax.mail.internet.MimeMultipart;
import javax.servlet.GenericServlet;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletResponse;
import javax.xml.stream.FactoryConfigurationError;
import javax.xml.stream.XMLOutputFactory;
import javax.xml.stream.XMLStreamException;
import javax.xml.stream.XMLStreamWriter;

import com.anteboth.agrisys.client.model.Account;
import com.anteboth.agrisys.client.model.Aktivitaet;
import com.anteboth.agrisys.client.model.Aussaat;
import com.anteboth.agrisys.client.model.Betrieb;
import com.anteboth.agrisys.client.model.Bodenbearbeitung;
import com.anteboth.agrisys.client.model.Duengung;
import com.anteboth.agrisys.client.model.Ernte;
import com.anteboth.agrisys.client.model.Erntejahr;
import com.anteboth.agrisys.client.model.Flurstueck;
import com.anteboth.agrisys.client.model.Pflanzenschutz;
import com.anteboth.agrisys.client.model.Schlag;
import com.anteboth.agrisys.client.model.SchlagErntejahr;
import com.anteboth.agrisys.client.model.stammdaten.BodenbearbeitungTyp;
import com.anteboth.agrisys.client.model.stammdaten.Duengerart;
import com.anteboth.agrisys.client.model.stammdaten.Kultur;
import com.anteboth.agrisys.client.model.stammdaten.PSMittel;
import com.anteboth.agrisys.client.model.stammdaten.Sorte;
import com.anteboth.agrisys.client.model.stammdaten.Stammdaten;
import com.googlecode.objectify.Objectify;
import com.googlecode.objectify.ObjectifyService;
import com.thoughtworks.xstream.XStream;
import com.thoughtworks.xstream.io.xml.PrettyPrintWriter;

@SuppressWarnings("serial")
public class BackupServlet extends GenericServlet {
	
	private boolean plainXml = false;
	
	@Override
	public void service(ServletRequest request, ServletResponse response) 
	throws ServletException, IOException {
		
		//create XML or ZIP file?
		String media = request.getParameter("media");
		if (media != null && media.equals("xml")) {
			plainXml = true;
		} else {
			plainXml = false; 
		}
		
		Writer writer = null;
		ZipOutputStream zip = null;
		ByteArrayOutputStream baos = null;
		
		if (plainXml) {	
			//set header properties
			response.setContentType("text/xml");
			response.setCharacterEncoding("UTF-8");

			//use the servlet response writer to write xml data 
			writer = response.getWriter();
		} else {
			//set header properties
			response.setContentType("application/zip");
			((HttpServletResponse) response).setHeader( "Content-disposition", "attachment; filename=AgrisysBackup.zip");
//			zip = new ZipOutputStream(response.getOutputStream());
			
			//create zip file output stream which redirect into the baos
			baos = new ByteArrayOutputStream(2048);
			zip = new ZipOutputStream(baos);
			
			//create zip file entry "AgrisysBackup.xml"
			zip.putNextEntry(new ZipEntry("AgrisysBackup.xml"));
			//wrap zip output stream with writer (xml will be written to the writer)
			writer = new OutputStreamWriter(zip);
		}
		
		//write the xml data to the writer
		writeXmlData(writer);
		
		
		//close stream
		if (plainXml) {
			writer.close();
		} else {
			writer.flush();
			writer.close();
//			zip.close();
			baos.close();
			
			byte[] data = baos.toByteArray();
			sendMail(data);
		}
		
	}

	private void sendMail(byte[] attachmentData) {
		Properties props = new Properties();
        Session session = Session.getDefaultInstance(props, null);

        String date = DateFormat.getDateTimeInstance().format(new Date());
        String msgBody = "Agriys Backup vom:\n\t" + date;
        String subject = "Agrisys Backup - " + date;
        String from = "noreply@anteboth.com";
        String to = "admins";
        
        try {
            Message msg = new MimeMessage(session);
            msg.setFrom(new InternetAddress(from));
            msg.addRecipient(Message.RecipientType.TO,new InternetAddress(to));
            msg.setSubject(subject);
            
            //create multipart message
            Multipart mp = new MimeMultipart();

            //add message body part
            MimeBodyPart body = new MimeBodyPart();
            body.setFileName("body");
            body.setContent(msgBody, "application/text");
            mp.addBodyPart(body);

            //add message attachment part
            MimeBodyPart attachment = new MimeBodyPart();
            attachment.setFileName("agrisys_backup.zip");
            attachment.setContent(attachmentData, "application/x-zip");
            mp.addBodyPart(attachment);

            //add multipart to message
            msg.setContent(mp);
            
            //send message
            Transport.send(msg);
        } catch (AddressException e) {
        	handleException(e);
        } catch (MessagingException e) {
        	handleException(e);
		} catch (Throwable e) {
			handleException(e);
		}
	}

	private void handleException(Throwable e) {
		e.printStackTrace();
	}

	private void writeXmlData(Writer writer) throws FactoryConfigurationError {
		XStream xstream = new XStream();
		xstream.alias("stammdaten", Stammdaten.class);
		xstream.alias("kultur", Kultur.class);
		xstream.alias("sorte", Sorte.class);
		xstream.alias("duengerart", Duengerart.class);
		xstream.alias("psMittel", PSMittel.class);
		xstream.alias("bodenbearbeitungTyp", BodenbearbeitungTyp.class);
		xstream.alias("account", Account.class);
		xstream.alias("betrieb", Betrieb.class);
		xstream.alias("schlag", Schlag.class);
		xstream.alias("bodenbearbeitung", Bodenbearbeitung.class);
		xstream.alias("ernte", Ernte.class);
		xstream.alias("aussaat", Aussaat.class);
		xstream.alias("duengung", Duengung.class);
		xstream.alias("pflanzenschutz", Pflanzenschutz.class);
		xstream.alias("schlagErntejahr", SchlagErntejahr.class);
		xstream.alias("flurstueck", Flurstueck.class);
		
		
		
		try {
			XMLOutputFactory factory = XMLOutputFactory.newInstance();
			PrettyPrintWriter ppw = new PrettyPrintWriter(writer);

			XMLStreamWriter xw = factory.createXMLStreamWriter(writer);
			xw.writeStartDocument();
			//write the root node
			xw.writeStartElement("agrisysBackup");
			xw.writeCharacters("\n");
			//flush tobe able to write with xstram
			xw.flush();
			
			//use xstream to write stammdaten
			writeData(xstream, ppw);
			
			xw.writeEndElement(); //agrisysBackup
			xw.writeEndDocument(); //close xml document
		} catch (XMLStreamException e) {
			e.printStackTrace();
		}
	}

	private void writeData(XStream xstream, PrettyPrintWriter ppw) {
		Stammdaten sd = ServiceManager.getInstance().getStammdaten();
		xstream.marshal(sd, ppw);
		
		Objectify ofy = ObjectifyService.begin();
		
		//backup Account data
		List<Account> accounts = ofy.query(Account.class).list();
		ppw.startNode("accountList");
		for (Account account : accounts) {
			xstream.marshal(account, ppw);
		}
		ppw.endNode();
		
		//backup Betrieb and Schlagdata for each Betrieb and Erntejahr
		List<Betrieb> betriebList = ofy.query(Betrieb.class).list();
		List<Erntejahr> erntejahrList = ofy.query(Erntejahr.class).list();
		
		for (Betrieb betrieb : betriebList) {
			
			ppw.startNode("betriebData");
			xstream.marshal(betrieb, ppw);
			
			for (Erntejahr erntejahr : erntejahrList) {
				List<Schlag> schlagList = ServiceManager.getInstance().loadSchlagData(erntejahr, betrieb);
				if (schlagList != null && schlagList.size() > 0) {
					ppw.startNode("schlagList");
					ppw.addAttribute("erntejahr", erntejahr.getErntejahr() + "");

					for (Schlag schlag : schlagList) {						
						ppw.startNode("schlagData");
						
						SchlagErntejahr schlagErntejahr = schlag.getSchlagErntejahr();						
						xstream.marshal(schlagErntejahr, ppw);
						
						Flurstueck f = schlag.getFlurstueck();
						xstream.marshal(f, ppw);
						
						Long seId = schlag.getSchlagErntejahr().getId();						
						List<Aktivitaet> aktivitaetList = 
							ServiceManager.getInstance().loadAktivitaetData(seId);
						
						ppw.startNode("aktivitaetList");
						for (Aktivitaet aktivitaet : aktivitaetList) {
							xstream.marshal(aktivitaet, ppw);
						}
						ppw.endNode(); //aktivitaetList

						ppw.endNode(); //schlagData
					}
					ppw.endNode(); //schlagList
					
				}
			}
			ppw.endNode();
		}
		
		
		//we need a flush here
		ppw.flush();
	}
}
