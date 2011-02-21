package com.anteboth.agrisys.client.grid;

import java.util.Date;
import java.util.List;
import java.util.Map;

import com.anteboth.agrisys.client.ISchlagErntejahrSelectionListener;
import com.anteboth.agrisys.client.grid.data.DataManager;
import com.anteboth.agrisys.client.grid.data.PflanzenschutzRecord;
import com.anteboth.agrisys.client.model.Pflanzenschutz;
import com.anteboth.agrisys.client.model.SchlagErntejahr;
import com.anteboth.agrisys.client.ui.PflanzenschutzDetailsWindow;
import com.google.gwt.i18n.client.DateTimeFormat;
import com.googlecode.objectify.Key;
import com.smartgwt.client.types.ListGridEditEvent;
import com.smartgwt.client.widgets.grid.CellFormatter;
import com.smartgwt.client.widgets.grid.ListGridField;
import com.smartgwt.client.widgets.grid.ListGridRecord;
import com.smartgwt.client.widgets.grid.events.EditCompleteEvent;
import com.smartgwt.client.widgets.grid.events.RecordDoubleClickEvent;
import com.smartgwt.client.widgets.grid.events.RecordDoubleClickHandler;

public class PflanzenschutzGrid 
extends AbstractListGrid<PflanzenschutzRecord> implements ISchlagErntejahrSelectionListener {
	
	private DataManager dataManager;
	/** The current SchlagErntejahr item. */
	private SchlagErntejahr schlagErntejahr;

	public PflanzenschutzGrid() {
		super();
		
		//disable default editing in table cells
		setEditEvent(ListGridEditEvent.NONE);

		//open editing dialog on 2xclick
		addRecordDoubleClickHandler(new RecordDoubleClickHandler() {
			@Override
			public void onRecordDoubleClick(RecordDoubleClickEvent event) {
				startEditing();
			}
		});
	}
	
	@Override
	protected void initialize() {
		this.dataManager = new DataManager();
	}
	
	@Override
	public void onSchlagErntejahrSelectionChanged(SchlagErntejahr se) {
		setSchlagErntejahr(se);
		reloadData();
	}
	
	public void setSchlagErntejahr(SchlagErntejahr schlagErntejahr) {
		this.schlagErntejahr = schlagErntejahr;
	}
	
	public SchlagErntejahr getSchlagErntejahr() {
		return schlagErntejahr;
	}
	
	@Override
	protected void initGridFields() {
		final DateTimeFormat dateFormatter = DateTimeFormat.getFormat("dd.MM.yyyyy");  
		ListGridField datumField = new ListGridField(PflanzenschutzRecord.DATUM, "Datum");
		datumField.setCellFormatter(new CellFormatter() {
            public String format(Object value, ListGridRecord record, int rowNum, int colNum) {  
                if (value != null) {  
                    try {  
                        Date dateValue = (Date) value;  
                        return dateFormatter.format(dateValue);  
                    } catch (Exception e) {  
                        return value.toString();  
                    }  
                } else {  
                    return "";  
                }  
            }  
        }); 
		
		ListGridField flaecheField = new ListGridField(
				PflanzenschutzRecord.FLAECHE, "Fl&auml;che");
		ListGridField bemField = new ListGridField(
				PflanzenschutzRecord.BEMERKUNG, "Bemerkung");
		ListGridField psMittelField = new ListGridField(
				PflanzenschutzRecord.PS_MITTEL, "Pflanzenschutzmittel");
		ListGridField ecField = new ListGridField(
				PflanzenschutzRecord.EC, "EC");
		ListGridField kgProHaField = new ListGridField(
				PflanzenschutzRecord.KG_PRO_HA, "kg/ha");
		ListGridField indikationField = new ListGridField(
				PflanzenschutzRecord.INDIKATION, "Indikation");
		
		setFields(datumField, psMittelField, flaecheField, kgProHaField, ecField, indikationField, bemField);
	}

	@Override
	protected void reloadData() {
		//load the data
		this.dataManager.loadPflanzenschutzData(this.schlagErntejahr, this);
	}

	@Override
	protected void deleteRecords(List<PflanzenschutzRecord> records) {
		//delete records
		this.dataManager.delete(records, this);
	}
	
	@Override
	public Boolean startEditing() {
		PflanzenschutzRecord r = (PflanzenschutzRecord) getSelectedRecord();
		if (r != null) {
			PflanzenschutzDetailsWindow window = new PflanzenschutzDetailsWindow(r, false, this);
			window.draw();
		}
		return false;
	}

	@Override
	public void startEditingNew() {
		if (schlagErntejahr == null) {
			return;
		}
		
		//create the data entry
		Pflanzenschutz b = new Pflanzenschutz();
		b.setSchlagErntejahr(new Key<SchlagErntejahr>(SchlagErntejahr.class, schlagErntejahr.getId()));

		//create new record
		PflanzenschutzRecord r = new PflanzenschutzRecord(b);
		
		PflanzenschutzDetailsWindow window = new PflanzenschutzDetailsWindow(r, true, this);
		window.draw();

	}

	@Override
	protected void udpateAndSaveRecord(PflanzenschutzRecord record, EditCompleteEvent event) {
		if (record != null) {
			Map<String, Object> vals = event.getNewValues();
			record.updateDTO(vals);
			
			//update data
			this.dataManager.update(record, this);
			
			super.saveAllEdits();
		}
	}
	
}
