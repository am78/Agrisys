package com.anteboth.agrisys.client.ui;

import com.anteboth.agrisys.client.grid.AbstractListGrid;
import com.anteboth.agrisys.client.grid.data.DataManager;
import com.anteboth.agrisys.client.grid.data.ErnteRecord;
import com.anteboth.agrisys.client.grid.data.StammdatenManager;
import com.anteboth.agrisys.client.model.Ernte;
import com.smartgwt.client.widgets.Button;
import com.smartgwt.client.widgets.Window;
import com.smartgwt.client.widgets.events.ClickEvent;
import com.smartgwt.client.widgets.events.ClickHandler;
import com.smartgwt.client.widgets.form.DynamicForm;
import com.smartgwt.client.widgets.form.fields.DateItem;
import com.smartgwt.client.widgets.form.fields.FloatItem;
import com.smartgwt.client.widgets.form.fields.TextAreaItem;
import com.smartgwt.client.widgets.form.fields.TextItem;
import com.smartgwt.client.widgets.layout.HLayout;
import com.smartgwt.client.widgets.layout.VLayout;

/**
 * Details panel for a {@link Ernte} entry.
 * @author michael
 */
public class ErnteDetailsWindow extends Window {

	private AbstractListGrid grid;
	private StammdatenManager stammdatenManager;
	private DataManager dataManager;
	
	public ErnteDetailsWindow(final ErnteRecord record, final boolean addNewRecord, AbstractListGrid grid) {
		super();

		this.stammdatenManager = new StammdatenManager();
		this.dataManager = new DataManager();
		
		this.grid = grid;
		setTitle("Ernte");
		setAutoSize(true);
		setCanDragResize(true);
		setIsModal(true);  
        setShowModalMask(true);
        setAutoCenter(true);
        
		/* create the form */
		FloatItem flaecheItem = new FloatItem();
		flaecheItem.setName(ErnteRecord.FLAECHE);
		flaecheItem.setTitle("Fl&auml;che");
		flaecheItem.setRequired(true);
		
		FloatItem dtProHaItem = new FloatItem();
		dtProHaItem.setName(ErnteRecord.DT_PRO_HA);
		dtProHaItem.setTitle("dt/ha");
		dtProHaItem.setRequired(true);
		
		FloatItem gesamtmengeItem = new FloatItem();
		gesamtmengeItem.setName(ErnteRecord.GESAMTMENGE);
		gesamtmengeItem.setTitle("Gesamtmenge");
		gesamtmengeItem.setRequired(true);
		
		DateItem datumItem = new DateItem(ErnteRecord.DATUM, "Datum");
		datumItem.setRequired(true);
		
		TextItem anlieferungItem = new TextItem(ErnteRecord.ANLIEFERUNG, "Anlieferung");
        TextAreaItem bemItem = new TextAreaItem(ErnteRecord.BEMERKUNG, "Bemerkung");

        final DynamicForm form = new DynamicForm();
        form.setItems(datumItem, flaecheItem, dtProHaItem, gesamtmengeItem, anlieferungItem, bemItem);
        
        //display values form an existing entry
        if (!addNewRecord && record != null) {
        	Ernte e = record.getDTO();
        	
        	bemItem.setValue(e.getBemerkung() != null ? e.getBemerkung() : "");
    		flaecheItem.setValue(e.getFlaeche());
        	if (e.getDatum() != null) {
        		datumItem.setValue(e.getDatum());
        	}
        	if (e.getDtProHa() != null) {
        		dtProHaItem.setValue(e.getDtProHa());
        	}
        	if (e.getAnlieferung() != null) {
        		anlieferungItem.setValue(e.getAnlieferung());
        	}
        	if (e.getGesamtMenge() != null) {
        		gesamtmengeItem.setValue(e.getGesamtMenge());
        	}
        }

		/* create the save & cancel buttons */
        Button saveBtn = new Button("Speichern");
        saveBtn.addClickHandler(new ClickHandler() {
			@Override
			public void onClick(ClickEvent event) {
				if(form.validate()) {
					//save changes
					onSavePressed(record, form, addNewRecord);
					//close dialog window
					ErnteDetailsWindow.this.destroy();
				}  
			}
		});
        
		Button cancelBtn = new Button("Abbrechen");
		cancelBtn.addClickHandler(new ClickHandler() {
			@Override
			public void onClick(ClickEvent event) {
				//close dialog window
				ErnteDetailsWindow.this.destroy();
			}
		});


		/* create the layout */

		//panel for the buttons
		HLayout btnLayout = new HLayout();
		btnLayout.setMargin(10);
		//add the buttons
		btnLayout.addMember(saveBtn);
		btnLayout.addMember(cancelBtn);

		VLayout verticalPanel = new VLayout();
		verticalPanel.setMargin(10);
		verticalPanel.setPadding(5);
		
		//add the form 
		verticalPanel.addMember(form);
		//and the buttons
		verticalPanel.addMember(btnLayout);

		//add panel to component
		addItem(verticalPanel);
	}
	
	private void onSavePressed(ErnteRecord record, DynamicForm form, boolean addNewRecord) {
		//update the internal DTO values with the values stored in the form 
		record.updateDTO(form.getValues());

		if (addNewRecord) {
			//save data and add to table
			this.dataManager.save(record, this.grid);
		}
		else {
			//save data and update table
			this.dataManager.update(record, this.grid);
		}
		
		//redraw the grid
		this.grid.redraw();
	}
}