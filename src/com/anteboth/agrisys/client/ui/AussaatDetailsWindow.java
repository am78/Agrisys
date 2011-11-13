package com.anteboth.agrisys.client.ui;

import java.util.Date;

import com.anteboth.agrisys.client.grid.AbstractListGrid;
import com.anteboth.agrisys.client.grid.data.AussaatRecord;
import com.anteboth.agrisys.client.grid.data.DataManager;
import com.anteboth.agrisys.client.grid.data.StammdatenManager;
import com.anteboth.agrisys.client.model.Aussaat;
import com.anteboth.agrisys.client.model.SorteDataSource;
import com.smartgwt.client.widgets.Button;
import com.smartgwt.client.widgets.Window;
import com.smartgwt.client.widgets.events.ClickEvent;
import com.smartgwt.client.widgets.events.ClickHandler;
import com.smartgwt.client.widgets.form.DynamicForm;
import com.smartgwt.client.widgets.form.fields.DateItem;
import com.smartgwt.client.widgets.form.fields.FloatItem;
import com.smartgwt.client.widgets.form.fields.SelectItem;
import com.smartgwt.client.widgets.form.fields.TextAreaItem;
import com.smartgwt.client.widgets.form.fields.TextItem;
import com.smartgwt.client.widgets.layout.HLayout;
import com.smartgwt.client.widgets.layout.VLayout;

/**
 * Details panel for a {@link Aussaat} entry.
 * @author michael
 */
public class AussaatDetailsWindow extends Window {

	private AbstractListGrid grid;
	private StammdatenManager stammdatenManager;
	private DataManager dataManager;
	private SorteDataSource sorteDataSource;
	
	public AussaatDetailsWindow(final AussaatRecord record, final boolean addNewRecord, AbstractListGrid grid) {
		super();

		this.stammdatenManager = new StammdatenManager();
		this.dataManager = new DataManager();
		
		this.grid = grid;
		setTitle("Aussaat");
		setAutoSize(true);
		setCanDragResize(true);
		setIsModal(true);  
        setShowModalMask(true);
        setAutoCenter(true);
                
        SelectItem sorteItem = new SelectItem(AussaatRecord.SORTE, "Sorte");  
		sorteItem.setRequired(true);
		this.sorteDataSource = new SorteDataSource(sorteItem);
        
		/* create the form */
		FloatItem flaecheItem = FormItemFactory.createFloatItem(
				AussaatRecord.FLAECHE, "Fl&auml;che", true);
		FloatItem kgProHaItem = FormItemFactory.createFloatItem(
				AussaatRecord.KG_PRO_HA, "kg/ha", true);
		DateItem datumItem = FormItemFactory.createDateItem(
				AussaatRecord.DATUM, "Datum", true);
		TextItem beizeItem = FormItemFactory.createTextItem(
				AussaatRecord.BEIZE, "Beize", false);
        TextAreaItem bemItem = FormItemFactory.createTextAreaItem(
        		AussaatRecord.BEMERKUNG, "Bemerkung", false);

        final DynamicForm form = new DynamicForm();
        form.setItems(datumItem, flaecheItem, kgProHaItem, sorteItem, beizeItem, bemItem);
        
        //display values form an existing entry
        if (!addNewRecord && record != null) {
        	Aussaat a = record.getDTO();
        	
        	bemItem.setValue(a.getBemerkung() != null ? a.getBemerkung() : "");
        	flaecheItem.setValue(a.getFlaeche());
        	datumItem.setValue(a.getDatum() != null ? a.getDatum() : new Date());
        	kgProHaItem.setValue(a.getKgProHa() != null ? a.getKgProHa() : Double.valueOf(0));
        	beizeItem.setValue(a.getBeize() != null ? a.getBeize() : "");
        	
        	this.sorteDataSource.kulturValueChanged(a.getKultur().getKey());
        	this.sorteDataSource.setSelectedValue(a.getSorte().getId());
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
					AussaatDetailsWindow.this.destroy();
				}  
			}
		});
        
		Button cancelBtn = new Button("Abbrechen");
		cancelBtn.addClickHandler(new ClickHandler() {
			@Override
			public void onClick(ClickEvent event) {
				//close dialog window
				AussaatDetailsWindow.this.destroy();
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
	
	private void onSavePressed(AussaatRecord record, DynamicForm form, boolean addNewRecord) {
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