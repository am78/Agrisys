package com.anteboth.agrisys.client.ui;

import com.anteboth.agrisys.client.grid.AbstractListGrid;
import com.anteboth.agrisys.client.grid.data.DataManager;
import com.anteboth.agrisys.client.grid.data.SchlagRecord;
import com.anteboth.agrisys.client.grid.data.StammdatenManager;
import com.anteboth.agrisys.client.model.KulturDataSource;
import com.anteboth.agrisys.client.model.Schlag;
import com.anteboth.agrisys.client.model.SorteDataSource;
import com.anteboth.agrisys.client.model.stammdaten.Kultur;
import com.anteboth.agrisys.client.model.stammdaten.Sorte;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.user.client.ui.Button;
import com.googlecode.objectify.Key;
import com.smartgwt.client.widgets.Window;
import com.smartgwt.client.widgets.form.DynamicForm;
import com.smartgwt.client.widgets.form.fields.FloatItem;
import com.smartgwt.client.widgets.form.fields.SelectItem;
import com.smartgwt.client.widgets.form.fields.TextAreaItem;
import com.smartgwt.client.widgets.form.fields.TextItem;
import com.smartgwt.client.widgets.form.fields.events.ChangedEvent;
import com.smartgwt.client.widgets.form.fields.events.ChangedHandler;
import com.smartgwt.client.widgets.layout.HLayout;
import com.smartgwt.client.widgets.layout.VLayout;

public class SchlagdetailsWindow extends Window {

	private AbstractListGrid grid;
	private StammdatenManager stammdatenManager;
	private DataManager dataManager;
	private KulturDataSource kulturDataSource;
	private SorteDataSource sorteDataSource;
	
	public SchlagdetailsWindow(final SchlagRecord record, final boolean addNewRecord, AbstractListGrid grid) {
		super();
		this.stammdatenManager = new StammdatenManager();
		this.dataManager = new DataManager();
		
		this.grid = grid;
		setTitle("Schlag");
		setAutoSize(true);
		setCanDragResize(true);
		setIsModal(true);  
        setShowModalMask(true);
        setAutoCenter(true);
        
		/* create the form */
		TextItem nameItem = new TextItem("name", "Name");
		nameItem.setRequired(true);

		FloatItem flaecheItem = new FloatItem();
		flaecheItem.setName("flaeche");
		flaecheItem.setTitle("Fl&auml;che");
		flaecheItem.setRequired(true);
		
		SelectItem erntejahrItem = new SelectItem("erntejahr", "Erntejahr");  
        erntejahrItem.setRequired(true);
        this.stammdatenManager.loadErntejahrData(erntejahrItem);
        
        SelectItem vorfruchtItem = new SelectItem("vorfrucht", "Vorfrucht");  
        vorfruchtItem.setRequired(true);
        KulturDataSource vorfruchtDataSource = new KulturDataSource(vorfruchtItem);
        
        final SelectItem kulturItem = new SelectItem("kultur", "Kultur");  
        kulturItem.setRequired(true);
        this.kulturDataSource = new KulturDataSource(kulturItem);
        
        final SelectItem sorteItem = new SelectItem("sorte", "Sorte");  
        sorteItem.setRequired(true);
        this.sorteDataSource = new SorteDataSource(sorteItem);
        
        //add Changed handler to kultur item
        //to react on a change Kultur item
        kulturItem.addChangedHandler(new ChangedHandler() {
			@Override
			public void onChanged(ChangedEvent event) {
				//load the sorte sata for the selected kultur item
				Kultur selkultur = getSelectedKultur(kulturItem);
				sorteDataSource.kulturValueChanged(selkultur.getKey());
			}
		});
        
        TextAreaItem bemItem = new TextAreaItem("bemerkung", "Bemerkung");

        final DynamicForm form = new DynamicForm();
        form.setItems(erntejahrItem, nameItem, flaecheItem, kulturItem, sorteItem, vorfruchtItem, bemItem);
        
        //display values form an existing entry
        if (!addNewRecord && record != null) {
        	//we are editing an existing record
        	//so set the old field values
        	Schlag s = record.getDTO();
        	String name = s.getFlurstueck().getName();
        	double flaeche = s.getSchlagErntejahr().getFlaeche();
        	String bemerkung = s.getSchlagErntejahr().getBemerkung();

        	//sets the Erntejahr value
        	this.stammdatenManager.updateErntejahrField(
    			erntejahrItem, s.getSchlagErntejahr().getErntejahr());
        	
        	nameItem.setValue(name);
        	flaecheItem.setValue(flaeche);
        	bemItem.setValue(bemerkung);

        	Key<Kultur> anbauKulturKey = s.getSchlagErntejahr().getAnbauKultur();
        	if (anbauKulturKey != null && anbauKulturKey.getId() > -1) {
        		kulturDataSource.setSelectedValue(anbauKulturKey.getId());
        		sorteDataSource.kulturValueChanged(anbauKulturKey);
        	}
        	
        	Key<Sorte> anbauSorteKey = s.getSchlagErntejahr().getAnbauSorte();
        	if (anbauSorteKey != null && anbauSorteKey.getId() > -1) {
        		sorteDataSource.setSelectedValue(anbauSorteKey.getId());
        	}
        	
        	Key<Kultur> vorfruchKey = s.getSchlagErntejahr().getVorfrucht();
        	if (vorfruchKey != null && vorfruchKey.getId() > -1) {
        		vorfruchtDataSource.setSelectedValue(vorfruchKey.getId());
        	}
        }

		/* create the buttons */
        com.smartgwt.client.widgets.Button saveBtn = new com.smartgwt.client.widgets.Button("Speichern");
        saveBtn.addClickHandler(new com.smartgwt.client.widgets.events.ClickHandler() {
			@Override
			public void onClick(com.smartgwt.client.widgets.events.ClickEvent event) {
				if(form.validate()) {
					//save changes
					onSavePressed(record, form, addNewRecord);
					//close dialog window
					SchlagdetailsWindow.this.destroy();
				}  
			}
		});
        
		com.smartgwt.client.widgets.Button cancelBtn = new com.smartgwt.client.widgets.Button("Abbrechen");
		cancelBtn.addClickHandler(new com.smartgwt.client.widgets.events.ClickHandler() {
			@Override
			public void onClick(com.smartgwt.client.widgets.events.ClickEvent event) {
				//close dialog window
				SchlagdetailsWindow.this.destroy();
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
	
	protected Kultur getSelectedKultur(SelectItem item) {
		String id = (String) item.getValue();
		if (id != null) {
			return this.kulturDataSource.getKulturForId(id);
		}
		return null;
	}

	private void onSavePressed(SchlagRecord record, DynamicForm form, boolean addNewRecord) {
		//get values from from
		String name = form.getValueAsString("name");
		double flaeche = Double.valueOf(form.getValueAsString("flaeche"));
		String bemerkung = form.getValueAsString("bemerkung");
		int erntejahr = Integer.parseInt(form.getValueAsString("erntejahr"));

		//get selected Anbausorte
		String sorteId = (String) form.getValue("sorte");
		Sorte anbau = this.sorteDataSource.getSorteForId(sorteId);
		
		//get selected Vorfrucht-Kultur
		String vorfruchId = (String) form.getValue("vorfrucht");
		Kultur vorfrucht = this.kulturDataSource.getKulturForId(vorfruchId);
		
		if (addNewRecord) {
			//save data and add to table
			this.dataManager.saveSchlag(name, flaeche, bemerkung, erntejahr, anbau, vorfrucht, this.grid);
		}
		else {
			//save data and update table
			Schlag schlag = record.getDTO();
			this.dataManager.updateSchlag(schlag, name, flaeche, bemerkung, erntejahr, anbau, vorfrucht, this.grid);
			record.updateAttributes();
			this.grid.redraw();
		}
	}
}