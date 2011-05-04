package com.anteboth.agrisys.client.grid;

import java.util.ArrayList;
import java.util.List;

import com.anteboth.agrisys.client.ImagesDialog;
import com.anteboth.agrisys.client.model.Aktivitaet;
import com.anteboth.agrisys.client.model.IDTO;
import com.anteboth.agrisys.client.model.ListRecord;
import com.smartgwt.client.data.Record;
import com.smartgwt.client.types.ListGridEditEvent;
import com.smartgwt.client.util.BooleanCallback;
import com.smartgwt.client.util.SC;
import com.smartgwt.client.widgets.Canvas;
import com.smartgwt.client.widgets.IButton;
import com.smartgwt.client.widgets.events.ClickEvent;
import com.smartgwt.client.widgets.events.ClickHandler;
import com.smartgwt.client.widgets.grid.ListGrid;
import com.smartgwt.client.widgets.grid.ListGridRecord;
import com.smartgwt.client.widgets.grid.events.EditCompleteEvent;
import com.smartgwt.client.widgets.grid.events.EditCompleteHandler;
import com.smartgwt.client.widgets.grid.events.RecordClickEvent;
import com.smartgwt.client.widgets.grid.events.RecordClickHandler;

/**
 * 
 * Provides an abstract class for grids with basic add, edit, delete support.
 * 
 * @author michael
 *
 * @param <T> the concrete IDTO type 
 */
public abstract class AbstractListGrid<RECORD extends ListRecord<? extends IDTO>> extends ListGrid {
	
	public AbstractListGrid() {		
		initialize();
		initUI();

	}


	@Override
	public abstract void startEditingNew();
	
	
	/**
	 * Called beford {@link #initUI()}. Can be used to do some pre-initialization stuff. 
	 */
	protected void initialize() {
	}
	
	@Override
	public void addData(Record record) {
		super.addData(record);
	}
	
	/**
	 * Initializes the grid.
	 */
	private void initUI() {
		setShowRecordComponents(true);          
        setShowRecordComponentsByCell(true); 
        
		//initialized the grid fields
		initGridFields();
		
		setCanEdit(true);
//		setShowAllRecords(true);  
		setEditEvent(ListGridEditEvent.DOUBLECLICK);
		setSelectOnEdit(true);

		//get initial data
		reloadData();
		
		//add edit complete handler
		addEditCompleteHandler(new EditCompleteHandler() {
			@Override
			public void onEditComplete(EditCompleteEvent event) {				
				editComplete(event);
			}
		});
		
		//add record selection handler
		addRecordClickHandler(new RecordClickHandler() {
            public void onRecordClick(RecordClickEvent event) {
            	onSelectionChanged(event);
            }  
        });

	}
	

	/**
	 * Called when a record has been selected.
	 * Yout can use <code>event.getRecord()</code> to get the affected record. 
	 * @param event
	 */
	protected void onSelectionChanged(RecordClickEvent event) {
	}


	/**
	 * Initializes the grid fields.
	 */
	protected abstract void initGridFields();

	/**
	 * Reloads the data.
	 */
	protected abstract void reloadData();
	
	
	@SuppressWarnings("unchecked")
	protected void editComplete(EditCompleteEvent event) {
		int row = event.getRowNum();
		RECORD r = (RECORD) getRecord(row);
		if (r != null) {			
			//update values
			udpateAndSaveRecord(r, event);

			super.saveAllEdits();
		}			
	}
	
	
	/**
	 * Updates the record data and saves the data to backend
	 * after the edit operation has been perfomed. 
	 * @param record the affected record
	 * @param event the edit complete event
	 */
	protected abstract void udpateAndSaveRecord(RECORD record, EditCompleteEvent event);
	

	@Override
	public void removeSelectedData() {
		if (getSelection() == null || getSelection().length < 1) {
			return;
		}
		
		//asks the user to delete the selected data 
		SC.confirm("Selektierten Eintrag wirklich l&ouml;schen?", new BooleanCallback() {  
			public void execute(Boolean value) {  
                if (value != null && value) {
                	//perform the delete operation
                	deleteSelection();
                }  
            }

        }); 
	}
	
	@SuppressWarnings("unchecked")
	private void deleteSelection() {
		ListGridRecord[] sel = getSelection();
		List<RECORD> records = new ArrayList<RECORD>(); 
		for (int i=0; i<sel.length; i++) {
			records.add((RECORD) sel[i]);
		}
		deleteRecords(records);
	}
	
	protected void displayImagesDialog(Aktivitaet akt) {
		ImagesDialog d = new ImagesDialog(akt);
		d.setIsModal(true);
		d.setShowModalMask(true);
		d.setSize("800", "600");
		d.setCanDragResize(true);
		d.setCanDrag(true);
		d.centerInPage();
		d.show();
	}
	
	@Override
	protected Canvas createRecordComponent(ListGridRecord record, Integer colNum) {
		String fieldName = this.getFieldName(colNum);
		if (fieldName.equals(ListRecord.ATTACHMENTS)) {
			//get the attachment value
			//it's not nil if the record has assigned attachments
			final Aktivitaet dto = (Aktivitaet) ((ListRecord) record).getDTO();
			
			IButton button = new IButton();  
			button.setHeight(18);  
			button.setWidth(25);                      
			button.setTitle("...");  
			button.addClickHandler(new ClickHandler() {
				public void onClick(ClickEvent event) {
					displayImagesDialog(dto);  
				}  
			});
	        return button;
		} else {
			return super.createRecordComponent(record, colNum);
		}
	}

	/**
	 * Delete the records.
	 * @param records records to delete
	 */
	protected abstract void deleteRecords(List<RECORD> records);
}