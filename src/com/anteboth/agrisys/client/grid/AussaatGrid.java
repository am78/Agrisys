package com.anteboth.agrisys.client.grid;

import java.util.Date;
import java.util.List;
import java.util.Map;

import com.anteboth.agrisys.client.ISchlagErntejahrSelectionListener;
import com.anteboth.agrisys.client.grid.data.AussaatRecord;
import com.anteboth.agrisys.client.grid.data.DataManager;
import com.anteboth.agrisys.client.grid.data.ErnteRecord;
import com.anteboth.agrisys.client.model.Aussaat;
import com.anteboth.agrisys.client.model.SchlagErntejahr;
import com.anteboth.agrisys.client.ui.AussaatDetailsWindow;
import com.google.gwt.i18n.client.DateTimeFormat;
import com.googlecode.objectify.Key;
import com.smartgwt.client.types.ListGridEditEvent;
import com.smartgwt.client.widgets.grid.CellFormatter;
import com.smartgwt.client.widgets.grid.ListGridField;
import com.smartgwt.client.widgets.grid.ListGridRecord;
import com.smartgwt.client.widgets.grid.events.EditCompleteEvent;
import com.smartgwt.client.widgets.grid.events.RecordDoubleClickEvent;
import com.smartgwt.client.widgets.grid.events.RecordDoubleClickHandler;

public class AussaatGrid 
extends AbstractListGrid<AussaatRecord> implements ISchlagErntejahrSelectionListener {
	
	private DataManager dataManager;
	
	/** The current SchlagErntejahr item. */
	private SchlagErntejahr schlagErntejahr;

	public AussaatGrid() {
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
		ListGridField datumField = new ListGridField(AussaatRecord.DATUM, "Datum");
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
		
		ListGridField flaecheField = new ListGridField(AussaatRecord.FLAECHE, "Fl&auml;che");
		ListGridField bemField = new ListGridField(AussaatRecord.BEMERKUNG, "Bemerkung");
		ListGridField sorteField = new ListGridField(AussaatRecord.SORTE, "Sorte");
		ListGridField kulturField = new ListGridField(AussaatRecord.KULTUR, "Kultur");
		ListGridField kgProHaField = new ListGridField(AussaatRecord.KG_PRO_HA, "kg/ha");
		ListGridField beizeField = new ListGridField(AussaatRecord.BEIZE, "Beize");
		
		ListGridField attachmentsField = new ListGridField(ErnteRecord.ATTACHMENTS, "IMG", 30);
		
		setFields(datumField, kulturField, sorteField, flaecheField, 
				kgProHaField, beizeField, bemField, attachmentsField);
	}

	@Override
	protected void reloadData() {
		//load the data
		this.dataManager.loadAussaatData(this.schlagErntejahr, this);
	}

	@Override
	protected void deleteRecords(List<AussaatRecord> records) {
		//delete records
		this.dataManager.delete(records, this);
	}
	
	@Override
	public Boolean startEditing() {
		AussaatRecord r = (AussaatRecord) getSelectedRecord();
		if (r != null) {
			AussaatDetailsWindow window = new AussaatDetailsWindow(r, false, this);
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
		Aussaat a = new Aussaat();
		a.setSchlagErntejahr(new Key<SchlagErntejahr>(SchlagErntejahr.class, schlagErntejahr.getId()));

		//create new record
		AussaatRecord r = new AussaatRecord(a);
		
		AussaatDetailsWindow window = new AussaatDetailsWindow(r, true, this);
		window.draw();

	}

	@Override
	protected void udpateAndSaveRecord(AussaatRecord record, EditCompleteEvent event) {
		if (record != null) {
			Map<String, Object> vals = event.getNewValues();
			record.updateDTO(vals);
			
			//update data
			this.dataManager.update(record, this);
			
			super.saveAllEdits();
		}
	}
}