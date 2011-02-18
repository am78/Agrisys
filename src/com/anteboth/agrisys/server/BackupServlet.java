package com.anteboth.agrisys.server;

import java.io.IOException;
import java.util.Hashtable;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.Map.Entry;
import java.util.zip.ZipOutputStream;

import javax.servlet.GenericServlet;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletResponse;

import org.apache.tools.zip.ZipEntry;

import com.anteboth.agrisys.client.model.Account;
import com.anteboth.agrisys.client.model.Aktivitaet;
import com.anteboth.agrisys.client.model.Betrieb;
import com.anteboth.agrisys.client.model.Erntejahr;
import com.anteboth.agrisys.client.model.Schlag;
import com.anteboth.agrisys.client.model.stammdaten.Stammdaten;
import com.googlecode.objectify.Objectify;
import com.googlecode.objectify.ObjectifyService;
import com.thoughtworks.xstream.XStream;

@SuppressWarnings("serial")
public class BackupServlet extends GenericServlet {
	
	private boolean plainXml = false;

	@Override
	public void service(ServletRequest request, ServletResponse response) 
	throws ServletException, IOException {
		Map<String, Object> backupData = new Hashtable<String, Object>();
		XStream xstream = new XStream();

		//backup Stammdaten
		ServiceManager service = ServiceManager.getInstance();
		Stammdaten sd = service.getStammdaten();
		backupData.put("Stammdaten", sd);
		
		Objectify ofy = ObjectifyService.begin();
		
		//backup Account data
		List<Account> accounts = ofy.query(Account.class).list();
		for (Account account : accounts) {
			String key = "Account_" + account.getId();
			backupData.put(key, account);
		}
		
		//backup Betrieb and Schlagdata for each Betrieb and Erntejahr
		List<Betrieb> betriebList = ofy.query(Betrieb.class).list();
		List<Erntejahr> erntejahrList = ofy.query(Erntejahr.class).list();
		for (Betrieb betrieb : betriebList) {
			backupData.put("Betrieb_" + betrieb.getId() , betrieb);
			for (Erntejahr erntejahr : erntejahrList) {
				List<Schlag> schlagData = ServiceManager.getInstance().loadSchlagData(erntejahr, betrieb);
				if (schlagData != null && schlagData.size() > 0) {
					String key = "Erntejahr_" + erntejahr.getErntejahr() + "-Betrieb_" + betrieb.getId();
					backupData.put(key, schlagData);
					
					//backup Aktivitaet data
					for (Schlag schlag : schlagData) {
						Long seId = schlag.getSchlagErntejahr().getId();
						List<Aktivitaet> aktivitaetList = ServiceManager.getInstance().loadAktivitaetData(seId);
						for (Aktivitaet aktivitaet : aktivitaetList) {
							key = "Aktivitaet_" + aktivitaet.getId() + 
								"_Schlagerntejahr_" + aktivitaet.getSchlagErntejahr().getId();
							backupData.put(key, aktivitaet);
						}
					}
					
				}
			}
		}
		
		
		
		/* create zip or xml output of the collected data */
		
		//create XML or ZIP file?
		String media = request.getParameter("media");
		if (media != null && media.equals("xml")) {
			plainXml = true;
		} else {
			plainXml = false; 
		}

		ZipOutputStream zip = null;
		if (plainXml) {			
			response.setContentType("text/xml");
			response.setCharacterEncoding("UTF-8");
			response.getWriter().write("<?xml version='1.0' encoding='utf-8'?>");
			response.getWriter().write("<AgrisysBackup>");
		} else {
			//create zip file
			response.setContentType("application/zip");
			
			((HttpServletResponse) response).setHeader( "Content-disposition",
				"attachment; filename=AgrisysBackup.zip");
			zip = new ZipOutputStream(response.getOutputStream());
		}
		

		Set<Entry<String, Object>> entries = backupData.entrySet();
		for (Entry<String, Object> entry : entries) {
			String name = entry.getKey();
			Object data = entry.getValue();
			String xml = xstream.toXML(data);
			if (plainXml) {				
				response.getWriter().write("<entry name=\"" + name + "\">\n");
				response.getWriter().write(xml);
				response.getWriter().write("\n</entry>\n");
			} else {
				zip.putNextEntry(new ZipEntry(name + ".xml"));
				zip.write(xml.getBytes());
				zip.closeEntry();
			}
		}
		
		if (plainXml) {
			response.getWriter().write("</AgrisysBackup>");
			response.getWriter().close();
		} else {
			zip.close();
		}
	}
}
