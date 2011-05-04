package com.anteboth.agrisys.client.grid;

import java.util.Date;
import java.util.List;
import java.util.Map;

import com.anteboth.agrisys.client.ISchlagErntejahrSelectionListener;
import com.anteboth.agrisys.client.grid.data.DataManager;
import com.anteboth.agrisys.client.grid.data.DuengungRecord;
import com.anteboth.agrisys.client.grid.data.ErnteRecord;
import com.anteboth.agrisys.client.model.Duengung;
import com.anteboth.agrisys.client.model.SchlagErntejahr;
import com.anteboth.agrisys.client.ui.DuengungDetailsWindow;
import com.google.gwt.i18n.client.DateTimeFormat;
import com.googlecode.objectify.Key;
import com.smartgwt.client.types.ListGridEditEvent;
import com.smartgwt.client.widgets.grid.CellFormatter;
import com.smartgwt.client.widgets.grid.ListGridField;
import com.smartgwt.client.widgets.grid.ListGridRecord;
import com.smartgwt.client.widgets.grid.events.EditCompleteEvent;
import com.smartgwt.client.widgets.grid.events.RecordDoubleClickEvent;
import com.smartgwt.client.widgets.grid.events.RecordDoubleClickHandler;

public class DuengungGrid 
extends AbstractListGrid<DuengungRecord> implements ISchlagErntejahrSelectionListener {
	
	private DataManager dataManager;
	/** The current SchlagErntejahr item. */
	private SchlagErntejahr schlagErntejahr;

	public DuengungGrid() {
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
		ListGridField datumField = new ListGridField(DuengungRecord.DATUM, "Datum");
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
				DuengungRecord.FLAECHE, "Fl&auml;che");
		ListGridField bemField = new ListGridField(
				DuengungRecord.BEMERKUNG, "Bemerkung");
		ListGridField duengerartField = new ListGridField(
				DuengungRecord.DUENGERART, "D&uuml;ngerart");
		ListGridField ecField = new ListGridField(
				DuengungRecord.EC, "EC");
		ListGridField kgProHaField = new ListGridField(
				DuengungRecord.KG_PRO_HA, "kg/ha");
		ListGridField attachmentsField = new ListGridField(ErnteRecord.ATTACHMENTS, "IMG", 30);
		
		setFields(datumField, duengerartField, flaecheField, kgProHaField, ecField, bemField, attachmentsField);
	}

	@Override
	protected void reloadData() {
		//load the data
		this.dataManager.loadDuengungData(this.schlagErntejahr, this);
	}

	@Override
	protected void deleteRecords(List<DuengungRecord> records) {
		//delete records
		this.dataManager.delete(records, this);
	}
	
	@Override
	public Boolean startEditing() {
		DuengungRecord r = (DuengungRecord) getSelectedRecord();
		if (r != null) {
			DuengungDetailsWindow window = new DuengungDetailsWindow(r, false, this);
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
		Duengung b = new Duengung();
		b.setSchlagErntejahr(new Key<SchlagErntejahr>(SchlagErntejahr.class, schlagErntejahr.getId()));

		//create new record
		DuengungRecord r = new DuengungRecord(b);
		
		DuengungDetailsWindow window = new DuengungDetailsWindow(r, true, this);
		window.draw();

	}

	@Override
	protected void udpateAndSaveRecord(DuengungRecord record, EditCompleteEvent event) {
		if (record != null) {
			Map<String, Object> vals = event.getNewValues();
			record.updateDTO(vals);
			
			//update data
			this.dataManager.update(record, this);
			
			super.saveAllEdits();
		}
	}
	
}
