package com.anteboth.agrisys.server;

import com.lowagie.text.Document;
import com.lowagie.text.Element;
import com.lowagie.text.Paragraph;
import com.lowagie.text.Phrase;
import com.lowagie.text.Rectangle;
import com.lowagie.text.pdf.ColumnText;
import com.lowagie.text.pdf.PdfPageEventHelper;
import com.lowagie.text.pdf.PdfWriter;

class HeaderFooter extends PdfPageEventHelper {
	
	int pagenumber = 1;

	/* (non-Javadoc)
	 * @see com.lowagie.text.pdf.PdfPageEventHelper#onOpenDocument(com.lowagie.text.pdf.PdfWriter, com.lowagie.text.Document)
	 */
	public void onOpenDocument(PdfWriter writer, Document document) {
	}

	/* (non-Javadoc)
	 * @see com.lowagie.text.pdf.PdfPageEventHelper#onChapter(com.lowagie.text.pdf.PdfWriter, com.lowagie.text.Document, float, com.lowagie.text.Paragraph)
	 */
	public void onChapter(PdfWriter writer, Document document, float paragraphPosition, Paragraph title) {
	}

	/* (non-Javadoc)
	 * @see com.lowagie.text.pdf.PdfPageEventHelper#onStartPage(com.lowagie.text.pdf.PdfWriter, com.lowagie.text.Document)
	 */
	public void onStartPage(PdfWriter writer, Document document) {
		pagenumber++;
	}

	/* (non-Javadoc)
	 * @see com.lowagie.text.pdf.PdfPageEventHelper#onEndPage(com.lowagie.text.pdf.PdfWriter, com.lowagie.text.Document)
	 */
	public void onEndPage(PdfWriter writer, Document document) {
		Rectangle rect = document.getPageSize();
		ColumnText.showTextAligned(
				writer.getDirectContent(),
				Element.ALIGN_CENTER, 
				new Phrase(String.format("Seite %d", pagenumber)), 
				(rect.getLeft() + rect.getRight()) / 2, 
				20, 0);
	}
}