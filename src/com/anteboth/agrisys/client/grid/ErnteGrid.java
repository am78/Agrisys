package com.anteboth.agrisys.client.grid;

import java.util.Date;
import java.util.List;
import java.util.Map;

import com.anteboth.agrisys.client.ISchlagErntejahrSelectionListener;
import com.anteboth.agrisys.client.grid.data.DataManager;
import com.anteboth.agrisys.client.grid.data.ErnteRecord;
import com.anteboth.agrisys.client.model.Ernte;
import com.anteboth.agrisys.client.model.SchlagErntejahr;
import com.anteboth.agrisys.client.ui.ErnteDetailsWindow;
import com.google.gwt.i18n.client.DateTimeFormat;
import com.googlecode.objectify.Key;
import com.smartgwt.client.types.ListGridEditEvent;
import com.smartgwt.client.widgets.grid.CellFormatter;
import com.smartgwt.client.widgets.grid.ListGridField;
import com.smartgwt.client.widgets.grid.ListGridRecord;
import com.smartgwt.client.widgets.grid.events.EditCompleteEvent;
import com.smartgwt.client.widgets.grid.events.RecordDoubleClickEvent;
import com.smartgwt.client.widgets.grid.events.RecordDoubleClickHandler;

public class ErnteGrid 
extends AbstractListGrid<ErnteRecord> implements ISchlagErntejahrSelectionListener {
	
	private DataManager dataManager;
	
	/** The current SchlagErntejahr item. */
	private SchlagErntejahr schlagErntejahr;

	public ErnteGrid() {
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
		ListGridField datumField = new ListGridField(ErnteRecord.DATUM, "Datum");
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
		
		ListGridField flaecheField = new ListGridField(ErnteRecord.FLAECHE, "Fl&auml;che");
		ListGridField bemField = new ListGridField(ErnteRecord.BEMERKUNG, "Bemerkung");
		ListGridField sorteField = new ListGridField(ErnteRecord.SORTE, "Sorte");
		ListGridField kulturField = new ListGridField(ErnteRecord.KULTUR, "Kultur");
		ListGridField dtProHaField = new ListGridField(ErnteRecord.DT_PRO_HA, "kg/ha");
		ListGridField anlieferungField = new ListGridField(ErnteRecord.ANLIEFERUNG, "Anlieferung");
		ListGridField gesamtmengeField = new ListGridField(ErnteRecord.GESAMTMENGE, "Gesamtmenge");
		
		setFields(datumField, kulturField, sorteField, flaecheField, dtProHaField, anlieferungField, gesamtmengeField, bemField);
	}

	@Override
	protected void reloadData() {
		//load the data
		this.dataManager.loadErnteData(this.schlagErntejahr, this);
	}

	@Override
	protected void deleteRecords(List<ErnteRecord> records) {
		//delete records
		this.dataManager.delete(records, this);
	}
	
	@Override
	public Boolean startEditing() {
		ErnteRecord r = (ErnteRecord) getSelectedRecord();
		if (r != null) {
			ErnteDetailsWindow window = new ErnteDetailsWindow(r, false, this);
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
		Ernte e = new Ernte();
		e.setSchlagErntejahr(new Key<SchlagErntejahr>(SchlagErntejahr.class, schlagErntejahr.getId()));

		//create new record
		ErnteRecord r = new ErnteRecord(e);
		
		ErnteDetailsWindow window = new ErnteDetailsWindow(r, true, this);
		window.draw();

	}

	@Override
	protected void udpateAndSaveRecord(ErnteRecord record, EditCompleteEvent event) {
		if (record != null) {
			Map<String, String> vals = event.getNewValues();
			record.updateDTO(vals);
			
			//update data
			this.dataManager.update(record, this);
			
			super.saveAllEdits();
		}
	}
}