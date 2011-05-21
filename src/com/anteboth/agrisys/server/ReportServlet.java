package com.anteboth.agrisys.server;

import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

import javax.servlet.GenericServlet;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletResponse;

import com.anteboth.agrisys.client.model.Account;
import com.anteboth.agrisys.client.model.Aktivitaet;
import com.anteboth.agrisys.client.model.Betrieb;
import com.anteboth.agrisys.client.model.Erntejahr;
import com.anteboth.agrisys.client.model.Schlag;
import com.anteboth.agrisys.client.model.stammdaten.Kultur;
import com.anteboth.agrisys.client.model.stammdaten.Sorte;
import com.lowagie.text.Anchor;
import com.lowagie.text.Chunk;
import com.lowagie.text.Document;
import com.lowagie.text.DocumentException;
import com.lowagie.text.Element;
import com.lowagie.text.Font;
import com.lowagie.text.Paragraph;
import com.lowagie.text.Phrase;
import com.lowagie.text.awt.Color;
import com.lowagie.text.pdf.PdfPCell;
import com.lowagie.text.pdf.PdfPTable;
import com.lowagie.text.pdf.PdfTable;
import com.lowagie.text.pdf.PdfWriter;
import com.lowagie.text.pdf.draw.LineSeparator;

public class ReportServlet extends GenericServlet {

	private static final long serialVersionUID = 1L;

	@Override
	public void service(ServletRequest request, ServletResponse response)
	throws ServletException, IOException {
		response.setContentType("application/pdf");
		((HttpServletResponse) response).setHeader( 
				"Content-disposition", "inline");
		
		try {
			Document document = new Document();
			PdfWriter.getInstance(document, response.getOutputStream());
			document.open();
			
			document.addTitle("Schlagliste");
			document.addCreationDate();
			
			ServiceManager mgr = ServiceManager.getInstance();
			Erntejahr erntejahr = mgr.getErntejahr(mgr.getCurrentErntejahr());
			Betrieb betrieb = mgr.getBetriebForAccount(mgr.getCurrentUserAccount());
			Account manager = mgr.getCurrentUserAccount(); 
			String betriebName = betrieb.getName();
			String email = manager.getEmail();
			
			//create page header
			Paragraph p = new Paragraph();
			p.add(new Phrase("Betrieb: " + betriebName));
			p.add(new Chunk("\r\n"));
			p.add(new Phrase("Email:   " + email));
			document.add(p);
			
			p = new Paragraph();			
			LineSeparator line = new LineSeparator(1, 100, null, Element.ALIGN_CENTER, 0);
			p.add(line);
			document.add(p);

			p = new Paragraph();
			p.setSpacingBefore(20);
			p.add(new Phrase("Schlagliste"));

			//create Schlagliste table
			PdfPTable table = new PdfPTable(4);
			table.setHeaderRows(1);
			table.setHorizontalAlignment(PdfTable.ALIGN_LEFT);
			
			table.addCell(createHeaderCell(table, "Schlag"));			
			table.addCell(createHeaderCell(table, "Fläche"));
			table.addCell(createHeaderCell(table, "Kultur"));
			table.addCell(createHeaderCell(table, "Sorte"));

			List<Schlag> schlagList = mgr.loadSchlagData(erntejahr, betrieb);
			for (Schlag schlag : schlagList) {
				String name = schlag.getFlurstueck().getName();
				Long id = schlag.getFlurstueck().getID();
				double flaeche = schlag.getSchlagErntejahr().getFlaeche();
				Kultur k = mgr.getKulur(schlag.getSchlagErntejahr().getAnbauKultur());
				Sorte  s = mgr.getSorte(schlag.getSchlagErntejahr().getAnbauSorte());
				
				Anchor nameAnchor = new Anchor(name);
				nameAnchor.setReference("#" + id);
				Phrase ph = new Phrase();
				ph.add(nameAnchor);
				Font font = new Font(Font.HELVETICA, Font.DEFAULTSIZE, Font.UNDERLINE);
				font.setColor(new Color(0, 0, 200));
				ph.setFont(font);
				
				table.addCell(ph);
				table.addCell(flaeche + " ha");
				table.addCell(k.getName());
				table.addCell(s.getName());
			}
			p.add(table);
			document.add(p);
			
			for (Schlag schlag : schlagList) {
				document.newPage();

				String name = schlag.getFlurstueck().getName();
				Long id = schlag.getFlurstueck().getID();

				p = new Paragraph();
				Anchor schlagAnchor = new Anchor(name);
				schlagAnchor.setName(id + "");
				p.add(schlagAnchor);
				document.add(p);
				
				p = new Paragraph();
				line = new LineSeparator(1, 100, null, Element.ALIGN_CENTER, 0);
				p.add(line);
				document.add(p);
				
				List<Aktivitaet> aktivitaetList = 
					mgr.loadAktivitaetData(schlag.getSchlagErntejahr().getId());
				addAktivitaetTable(aktivitaetList, document);
				
				
			}
			
			document.close();
		} catch (DocumentException e) {
			e.printStackTrace();
		}
	}

	private void addAktivitaetTable(List<Aktivitaet> data, Document document) 
	throws DocumentException {
		PdfPTable table = new PdfPTable(4);
		table.setHeaderRows(1);
		table.setHorizontalAlignment(PdfTable.ALIGN_LEFT);
		
		table.addCell(createHeaderCell(table, "Datum"));			
		table.addCell(createHeaderCell(table, "Fläche"));
		table.addCell(createHeaderCell(table, "Bemerkung"));
		table.addCell(createHeaderCell(table, "Typ"));
		
		for (Aktivitaet akt : data) {
			Date d = akt.getDatum();
			String bem = akt.getBemerkung();
			double flaeche = akt.getFlaeche();
			String date = SimpleDateFormat.getDateInstance().format(d);
			
			table.addCell(date);
			table.addCell(flaeche + " ha");
			table.addCell(bem);
			table.addCell("tbd");
		}
		
		Paragraph p = new Paragraph();
		p.setSpacingBefore(25);
		p.add(table);
		document.add(p);
	}

	private PdfPCell createHeaderCell(PdfPTable table, String txt) {		
		Font bold = new Font(Font.HELVETICA, 14, Font.BOLD);
		PdfPCell headerCell = new PdfPCell(new Phrase(txt, bold));
		headerCell.setBackgroundColor(new Color(128, 200, 128));
		headerCell.setPadding (5.0f);					
		return headerCell;
	}

}
