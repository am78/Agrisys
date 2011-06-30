package com.anteboth.agrisys.server;

import java.io.IOException;
import java.net.MalformedURLException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.List;

import javax.servlet.GenericServlet;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletResponse;

import com.anteboth.agrisys.client.model.Account;
import com.anteboth.agrisys.client.model.Aussaat;
import com.anteboth.agrisys.client.model.Betrieb;
import com.anteboth.agrisys.client.model.Bodenbearbeitung;
import com.anteboth.agrisys.client.model.Duengung;
import com.anteboth.agrisys.client.model.Ernte;
import com.anteboth.agrisys.client.model.Erntejahr;
import com.anteboth.agrisys.client.model.Pflanzenschutz;
import com.anteboth.agrisys.client.model.Schlag;
import com.anteboth.agrisys.client.model.stammdaten.BodenbearbeitungTyp;
import com.anteboth.agrisys.client.model.stammdaten.Kultur;
import com.anteboth.agrisys.client.model.stammdaten.Sorte;
import com.googlecode.objectify.Key;
import com.lowagie.text.Anchor;
import com.lowagie.text.Cell;
import com.lowagie.text.Chunk;
import com.lowagie.text.Document;
import com.lowagie.text.DocumentException;
import com.lowagie.text.Element;
import com.lowagie.text.Font;
import com.lowagie.text.FontFactory;
import com.lowagie.text.Image;
import com.lowagie.text.PageSize;
import com.lowagie.text.Paragraph;
import com.lowagie.text.Phrase;
import com.lowagie.text.Rectangle;
import com.lowagie.text.Table;
import com.lowagie.text.awt.Color;
import com.lowagie.text.pdf.PdfPCell;
import com.lowagie.text.pdf.PdfPTable;
import com.lowagie.text.pdf.PdfTable;
import com.lowagie.text.pdf.PdfWriter;
import com.lowagie.text.pdf.draw.LineSeparator;

public class ReportServlet extends GenericServlet {

	private static final long serialVersionUID = 1L;
	private HashMap<Long, String> typMap;
	private HashMap<Long, String> kulturMap;
	private HashMap<Long, String> sorteMap;

	@Override
	public void service(ServletRequest request, ServletResponse response)
	throws ServletException, IOException {
		response.setContentType("application/pdf");
		((HttpServletResponse) response).setHeader( 
				"Content-disposition", "inline");
		
		try {
			//create new document
			Document document = new Document();

			//create pdf writer
			PdfWriter pdfWriter = PdfWriter.getInstance(document, response.getOutputStream());
			
			//open document and set some meta data
			document.open();
			document.addTitle("Agrisys Report");
			document.addAuthor("Agrisys");
			document.addCreationDate();
			document.addProducer();
			
			//creates the document contents
			createContents(document, pdfWriter);
			
			//finally close the document
			document.close();
		} catch (DocumentException e) {
			e.printStackTrace();
		}
	}


	/**
	 * Create the report payload.
	 * @param document the document to add the data to
	 * @param pdfWriter 
	 * @throws DocumentException
	 * @throws IOException 
	 * @throws MalformedURLException 
	 */
	private void createContents(Document document, PdfWriter pdfWriter) throws DocumentException, MalformedURLException, IOException {
		ServiceManager mgr = ServiceManager.getInstance();
		Erntejahr erntejahr = mgr.getErntejahr(mgr.getCurrentErntejahr());
		Betrieb betrieb = mgr.getBetriebForAccount(mgr.getCurrentUserAccount());
		Account manager = mgr.getCurrentUserAccount(); 
		String betriebName = betrieb.getName();
		String email = manager.getEmail();
		String adresse = "Winkel 37\n38835 Berßel"; //TODO
		String tel = "+49 39421-73154";
		
		/* create the title page */
		Paragraph p = new Paragraph();
		p.setLeading(200);

		//create logo
		Image img = Image.getInstance("img/agrisys_logo_bw.png");
		//centered
		img.setAlignment(Element.ALIGN_CENTER);
		p.add(img);
		document.add(p);
		
		//create page header
		p = new Paragraph();
		p.add(new Chunk("\n\n"));
		p.setIndentationLeft(180);
		Chunk c = new Chunk("Report - " + erntejahr.getErntejahr(), 
				FontFactory.getFont(FontFactory.TIMES_BOLDITALIC, 19, Font.NORMAL));
		p.add(c);
		p.add(new Chunk("\n\n"));
		document.add(p);

		//print some meta data
		Table tab = new Table(2);
		tab.setBorder(Rectangle.NO_BORDER);
		tab.setSpacing(5);
		
		Cell dcell = new Cell();
		dcell.setBorderWidth(0);
		tab.setDefaultCell(dcell);
		
		Cell leftCell = new Cell("Erntejahr:");
		leftCell.setBorderWidth(0);
		leftCell.setHorizontalAlignment(Cell.ALIGN_RIGHT);
		tab.addCell(leftCell);
		tab.addCell(erntejahr.getErntejahr() + "");
		
		leftCell = new Cell("Betrieb:");
		leftCell.setBorderWidth(0);
		leftCell.setHorizontalAlignment(Cell.ALIGN_RIGHT);
		tab.addCell(leftCell);
		tab.addCell(betriebName);
		
		leftCell = new Cell("Adresse:");
		leftCell.setBorderWidth(0);
		leftCell.setHorizontalAlignment(Cell.ALIGN_RIGHT);
		tab.addCell(leftCell);
		tab.addCell(adresse);
		
		leftCell = new Cell("Tel.:");
		leftCell.setBorderWidth(0);
		leftCell.setHorizontalAlignment(Cell.ALIGN_RIGHT);
		tab.addCell(leftCell);
		tab.addCell(tel);
	
		document.add(tab);
		
		//new page
		document.newPage();
		
		//for page numbering
		pdfWriter.setPageEvent(new HeaderFooter());
		
		p = new Paragraph();			
		LineSeparator line = new LineSeparator(1, 100, null, Element.ALIGN_CENTER, 0);
		p.add(line);
		document.add(p);

		p = new Paragraph();
		p.setSpacingBefore(20);
		
		Phrase title = new Phrase("Schlagliste", FontFactory.getFont(FontFactory.HELVETICA, 17, Font.BOLD));
		p.add(title);

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
			Kultur k = mgr.getKultur(schlag.getSchlagErntejahr().getAnbauKultur());
			Sorte  s = mgr.getSorte(schlag.getSchlagErntejahr().getAnbauSorte());
			
			Anchor nameAnchor = new Anchor(name, 
				FontFactory.getFont(FontFactory.HELVETICA, Font.DEFAULTSIZE, Font.NORMAL, new Color(0, 0, 200)));
			nameAnchor.setReference("#" + id);
			Phrase phr = new Phrase();
			phr.add(nameAnchor);
			
			table.addCell(phr);
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
			Anchor schlagAnchor = new Anchor(name,
					FontFactory.getFont(FontFactory.HELVETICA, 17, Font.BOLD));
			schlagAnchor.setName(id + "");
			p.add(schlagAnchor);
			document.add(p);
			
			
			String kultur = getKultur(schlag.getSchlagErntejahr().getAnbauKultur());
			String sorte =  getSorte(schlag.getSchlagErntejahr().getAnbauSorte());
			double flaeche = schlag.getSchlagErntejahr().getFlaeche();
			String vorfrucht = getKultur(schlag.getSchlagErntejahr().getVorfrucht());
			
			p = new Paragraph();
			
			//Flaeche
			Phrase pr = new Phrase("Fläche: ", FontFactory.getFont(FontFactory.HELVETICA, 11, Font.BOLD));
			p.add(pr);
			pr = new Phrase(flaeche + " h    ", FontFactory.getFont(FontFactory.HELVETICA, 11, Font.NORMAL));
			p.add(pr);
			
			//Kultur
			pr = new Phrase("Kultur: ", FontFactory.getFont(FontFactory.HELVETICA, 11, Font.BOLD));
			p.add(pr);
			pr = new Phrase(kultur + "    ", FontFactory.getFont(FontFactory.HELVETICA, 11, Font.NORMAL));
			p.add(pr);
			
			//Sorte
			pr = new Phrase("Sorte: ", FontFactory.getFont(FontFactory.HELVETICA, 11, Font.BOLD));
			p.add(pr);
			pr = new Phrase(sorte + "    ", FontFactory.getFont(FontFactory.HELVETICA, 11, Font.NORMAL));
			p.add(pr);
			
			//Vorfrucht
			pr = new Phrase("Vorfrucht: ", FontFactory.getFont(FontFactory.HELVETICA, 11, Font.BOLD));
			p.add(pr);
			pr = new Phrase(vorfrucht, FontFactory.getFont(FontFactory.HELVETICA, 11, Font.NORMAL));
			p.add(pr);
			
			document.add(p);
			
			p = new Paragraph();
			line = new LineSeparator(1, 100, null, Element.ALIGN_CENTER, 0);
			p.add(line);
			document.add(p);
			
			//Bodebearbeitung
			List<Bodenbearbeitung> bodenList = mgr.loadBodenbearbeitungData(schlag.getSchlagErntejahr());
			addBodenbearbeitungTable(bodenList, document);
			
			//Aussaat
			List<Aussaat> aussaatList = mgr.loadAussaatData(schlag.getSchlagErntejahr());
			addAussaatTable(aussaatList, document);
			
			//Duengung
			List<Duengung> duengungList = mgr.loadDuengungData(schlag.getSchlagErntejahr());
			addDuengungTable(duengungList, document);
			
			//Pflanzenschutz
			List<Pflanzenschutz> psList = mgr.loadPflanzenschutzData(schlag.getSchlagErntejahr());
			addPflanzenschutzTable(psList, document);
			
			//Ernte
			List<Ernte> ernteList = mgr.loadErnteData(schlag.getSchlagErntejahr());
			addErnteTable(ernteList, document);
		}
	}
	
	
	/**
	 * Creates and adds the Ernte table to the pdf document.
	 * @param data the ernte entries
	 * @param document the pdf document
	 * @throws DocumentException
	 */
	private void addErnteTable(List<Ernte> data, Document document) 
	throws DocumentException {
		PdfPTable table = new PdfPTable(7);
		table.setHeaderRows(1);
		table.setWidthPercentage(100);
		table.setHorizontalAlignment(PdfTable.ALIGN_LEFT);
		
		table.addCell(createHeaderCell(table, "Datum"));			
		table.addCell(createHeaderCell(table, "Sorte"));
		table.addCell(createHeaderCell(table, "Fläche"));
		table.addCell(createHeaderCell(table, "dt/ha"));
		table.addCell(createHeaderCell(table, "Anlieferung"));
		table.addCell(createHeaderCell(table, "Gesamtmenge"));
		table.addCell(createHeaderCell(table, "Bemerkung"));
		
		for (Ernte e : data) {
			Date d = e.getDatum();
			String bem = e.getBemerkung();
			double flaeche = e.getFlaeche();
			String date = SimpleDateFormat.getDateInstance().format(d);
			String sorte = e.getSorte().getName();
			String dtProHa = e.getDtProHa() + "";
			String anlieferung = e.getAnlieferung();
			String ges = e.getGesamtMenge() + "";
			
			table.addCell(date);
			table.addCell(sorte);
			table.addCell(flaeche + " ha");
			table.addCell(dtProHa);
			table.addCell(anlieferung);
			table.addCell(ges);
			table.addCell(bem);
		}
		
		Paragraph p = new Paragraph();
		p.setSpacingBefore(25);
		p.add(new Phrase("Ernte",
				FontFactory.getFont(FontFactory.HELVETICA, 14, Font.BOLD)));
		p.add(table);
		document.add(p);
	}
	
	/**
	 * Creates and adds the Pflanzenschutz table to the pdf document.
	 * @param data the pflanzenschutz entries
	 * @param document the pdf document
	 * @throws DocumentException
	 */
	private void addPflanzenschutzTable(List<Pflanzenschutz> data, Document document) 
	throws DocumentException {
		PdfPTable table = new PdfPTable(7);
		table.setHeaderRows(1);
		table.setWidthPercentage(100);
		table.setHorizontalAlignment(PdfTable.ALIGN_LEFT);
		
		table.addCell(createHeaderCell(table, "Datum"));			
		table.addCell(createHeaderCell(table, "PS-Mittel"));
		table.addCell(createHeaderCell(table, "Fläche"));
		table.addCell(createHeaderCell(table, "kg/ha"));
		table.addCell(createHeaderCell(table, "EC"));
		table.addCell(createHeaderCell(table, "Indikation"));
		table.addCell(createHeaderCell(table, "Bemerkung"));
		
		for (Pflanzenschutz ps : data) {
			Date d = ps.getDatum();
			String bem = ps.getBemerkung();
			double flaeche = ps.getFlaeche();
			String date = SimpleDateFormat.getDateInstance().format(d);
			String psMittel = ps.getPsMittel().getName();
			String kgProHa = ps.getKgProHa() + "";
			String ec = ps.getEc() + "";
			String ind = ps.getIndikation();
			
			table.addCell(date);
			table.addCell(psMittel);
			table.addCell(flaeche + " ha");
			table.addCell(kgProHa);
			table.addCell(ec);
			table.addCell(ind);
			table.addCell(bem);
		}
		
		Paragraph p = new Paragraph();
		p.setSpacingBefore(25);
		p.add(new Phrase("Pflanzenschutz",
				FontFactory.getFont(FontFactory.HELVETICA, 14, Font.BOLD)));
		p.add(table);
		document.add(p);
	}
	
	/**Creates and adds the Duengung table to the pdf document.
	 * @param data the Duengung entries
	 * @param document the document
	 * @throws DocumentException
	 */
	private void addDuengungTable(List<Duengung> data, Document document) 
	throws DocumentException {
		PdfPTable table = new PdfPTable(6);
		table.setHeaderRows(1);
		table.setWidthPercentage(100);
		table.setHorizontalAlignment(PdfTable.ALIGN_LEFT);
		
		table.addCell(createHeaderCell(table, "Datum"));
		table.addCell(createHeaderCell(table, "Düngerart"));
		table.addCell(createHeaderCell(table, "Fläche"));
		table.addCell(createHeaderCell(table, "kg/ha"));
		table.addCell(createHeaderCell(table, "EC"));			
		table.addCell(createHeaderCell(table, "Bemerkung"));
		
		for (Duengung due : data) {
			Date d = due.getDatum();
			String bem = due.getBemerkung();
			double flaeche = due.getFlaeche();
			String date = SimpleDateFormat.getDateInstance().format(d);
			String duengerart = due.getDuengerart().getName();
			String kgProHa = due.getKgProHa() + "";
			String ec = due.getEc() + "";
			
			table.addCell(date);
			table.addCell(duengerart);
			table.addCell(flaeche + " ha");
			table.addCell(kgProHa);
			table.addCell(ec);
			table.addCell(bem);
		}
		
		Paragraph p = new Paragraph();
		p.setSpacingBefore(25);
		p.add(new Phrase("Düngung",
				FontFactory.getFont(FontFactory.HELVETICA, 14, Font.BOLD)));
		p.add(table);
		document.add(p);
	}
	
	/**
	 * Creates and adds the aussaat table to the pdf document.
	 * @param data the  aussaat entries to add
	 * @param document the document
	 * @throws DocumentException
	 */
	private void addAussaatTable(List<Aussaat> data, Document document) 
	throws DocumentException {
		PdfPTable table = new PdfPTable(6);
		table.setHeaderRows(1);
		table.setHorizontalAlignment(PdfTable.ALIGN_LEFT);
		table.setWidthPercentage(100);
		
		table.addCell(createHeaderCell(table, "Datum"));			
		table.addCell(createHeaderCell(table, "Sorte"));
		table.addCell(createHeaderCell(table, "Fläche"));
		table.addCell(createHeaderCell(table, "kg/ha"));
		table.addCell(createHeaderCell(table, "Beize"));
		table.addCell(createHeaderCell(table, "Bemerkung"));
		
		for (Aussaat a : data) {
			Date d = a.getDatum();
			String bem = a.getBemerkung();
			double flaeche = a.getFlaeche();
			String date = SimpleDateFormat.getDateInstance().format(d);
			String sorte = a.getSorte().getName();
			String kgProHa = a.getKgProHa() + "";
			String beize = a.getBeize();
			
			table.addCell(date);
			table.addCell(sorte);
			table.addCell(flaeche + " ha");
			table.addCell(kgProHa);
			table.addCell(beize);
			table.addCell(bem);
		}
		
		Paragraph p = new Paragraph();
		p.setSpacingBefore(25);
		p.add(new Phrase("Aussaat",
				FontFactory.getFont(FontFactory.HELVETICA, 14, Font.BOLD)));
		p.add(table);
		document.add(p);
	}
	
	/**
	 * Creates and adds the Bodnbearbeitung entries table.
	 * @param data the bodenbearbeitung entries to display in the table
	 * @param document the pdf document to add the table
	 * @throws DocumentException
	 */
	private void addBodenbearbeitungTable(List<Bodenbearbeitung> data, Document document) 
	throws DocumentException {
		//create the table
		PdfPTable table = new PdfPTable(4);
		table.setHeaderRows(1);
		table.setHorizontalAlignment(PdfTable.ALIGN_LEFT);
		table.setWidthPercentage(100);
		
		table.addCell(createHeaderCell(table, "Datum"));			
		table.addCell(createHeaderCell(table, "Fläche"));
		table.addCell(createHeaderCell(table, "Bemerkung"));
		table.addCell(createHeaderCell(table, "Typ"));
		
		for (Bodenbearbeitung b : data) {
			Date d = b.getDatum();
			String bem = b.getBemerkung();
			double flaeche = b.getFlaeche();
			String date = SimpleDateFormat.getDateInstance().format(d);
			String typ = getTypeString(b.getTypKey());
			
			table.addCell(date);
			table.addCell(flaeche + " ha");
			table.addCell(bem);
			table.addCell(typ);
		}
		
		Paragraph p = new Paragraph();
		p.setSpacingBefore(25);
		//add title label
		p.add(new Phrase("Bodenbearbeitung", 
			FontFactory.getFont(FontFactory.HELVETICA, 14, Font.BOLD)));
		p.add(table);
		//add the table
		document.add(p);
	}

	/**
	 * Returns the name of the Bodenbearbeitungtyp key.
	 * @param typKey the key
	 * @return the name or empty string if not found
	 */
	private String getTypeString(Key<BodenbearbeitungTyp> typKey) {
		if (typKey == null) {
			return "";
		}
		
		if (this.typMap == null) {
			this.typMap = new HashMap<Long, String>();
		}
		
		if (typMap.containsKey(typKey.getId())) {
			return typMap.get(typKey.getId());
		} else {
			BodenbearbeitungTyp typ = ServiceManager.getInstance().getBodenbearbeitungTyp(typKey.getId());
			String typName = typ != null ? typ.getName() : "";
			typMap.put(typKey.getId(), typName);
			return typName;
		}
	}
	
	
	/**
	 * Get the Kultur name of the requeted key
	 * @param key the kultur key
	 * @return the name of the found Kultur entry or "" if not found.
	 */
	private String getKultur(Key<Kultur> key) {
		if (key == null) {
			return "";
		}
		
		if (this.kulturMap == null) {
			this.kulturMap = new HashMap<Long, String>();
		}
		
		if (kulturMap.containsKey(key.getId())) {
			return kulturMap.get(key.getId());
		} else {
			Kultur k = ServiceManager.getInstance().getKultur(key);
			String name = k != null ? k.getName() : "";
			kulturMap.put(key.getId(), name);
			return name;
		}
	}
	

	/**
	 * Get the Sorte name of the requeted key
	 * @param key the sorte key
	 * @return the name of the found Sorte entry or "" if not found.
	 */
	private String getSorte(Key<Sorte> key) {
		if (key == null) {
			return "";
		}
		
		if (this.sorteMap == null) {
			this.sorteMap = new HashMap<Long, String>();
		}
		
		if (sorteMap.containsKey(key.getId())) {
			return sorteMap.get(key.getId());
		} else {
			Sorte s = ServiceManager.getInstance().getSorte(key);
			String name = s != null ? s.getName() : "";
			sorteMap.put(key.getId(), name);
			return name;
		}
	}


	/**
	 * Creates a table header cell for the specified values.
	 * 
	 * @param table the table
	 * @param txt the header cell text
	 * @return the header cell
	 */
	private PdfPCell createHeaderCell(PdfPTable table, String txt) {		
		Font bold = new Font(Font.HELVETICA, 14, Font.BOLD);
		PdfPCell headerCell = new PdfPCell(new Phrase(txt, bold));
		headerCell.setBackgroundColor(new Color(128, 200, 128));
		headerCell.setPadding (5.0f);					
		return headerCell;
	}

}
