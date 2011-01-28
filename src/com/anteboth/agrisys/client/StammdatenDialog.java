package com.anteboth.agrisys.client;

import com.anteboth.agrisys.client.grid.AbstractListGrid;
import com.anteboth.agrisys.client.grid.BodenbeabeitungStammdatenGrid;
import com.anteboth.agrisys.client.grid.DuengerartStammdatenGrid;
import com.anteboth.agrisys.client.grid.KulturGrid;
import com.anteboth.agrisys.client.grid.PSMittelStammdatenGrid;
import com.anteboth.agrisys.client.grid.SorteGrid;
import com.anteboth.agrisys.client.grid.data.KulturRecord;
import com.anteboth.agrisys.client.model.stammdaten.BodenbearbeitungTyp;
import com.anteboth.agrisys.client.model.stammdaten.Kultur;
import com.smartgwt.client.data.Record;
import com.smartgwt.client.types.Alignment;
import com.smartgwt.client.types.Side;
import com.smartgwt.client.widgets.Button;
import com.smartgwt.client.widgets.Canvas;
import com.smartgwt.client.widgets.Label;
import com.smartgwt.client.widgets.Window;
import com.smartgwt.client.widgets.events.ClickEvent;
import com.smartgwt.client.widgets.events.ClickHandler;
import com.smartgwt.client.widgets.grid.events.RecordClickEvent;
import com.smartgwt.client.widgets.grid.events.RecordClickHandler;
import com.smartgwt.client.widgets.layout.HLayout;
import com.smartgwt.client.widgets.layout.Layout;
import com.smartgwt.client.widgets.layout.VLayout;
import com.smartgwt.client.widgets.tab.Tab;
import com.smartgwt.client.widgets.tab.TabSet;
import com.smartgwt.client.widgets.toolbar.ToolStrip;
import com.smartgwt.client.widgets.toolbar.ToolStripButton;

/**
 * Implements the stammdaten dialog holding several stammdaten panels in tabs.
 */
public class StammdatenDialog extends Window {

	public StammdatenDialog() {
		super();
		initialize();
	}

	/**
	 * Create the main layout.
	 */
	private void initialize() {
		setTitle("Stammdaten");

		TabSet tabSet = new TabSet();  
        tabSet.setTabBarPosition(Side.TOP);  
        tabSet.setTabBarAlign(Side.LEFT);  
  
        Tab tabBoden = new Tab("Bodenbearbeitung");  
        tabBoden.setPane(createBodenbearbeitungPane());
        	
        Tab tabDuengung = new Tab("D&uuml;ngemittel");
        tabDuengung.setPane(createDuengungPane());
        	
        Tab tabPflanzenschutz = new Tab("Pflanzenschutz");
        tabPflanzenschutz.setPane(createPflanzenschutzPane());
        	
        Tab tabKultur = new Tab("Kultur/Sorte");
        tabKultur.setPane(createKulturPane());
  
        tabSet.addTab(tabBoden);  
        tabSet.addTab(tabDuengung);
        tabSet.addTab(tabPflanzenschutz);
        tabSet.addTab(tabKultur);
  
        Button closeBtn = new Button("OK");
        closeBtn.addClickHandler(new ClickHandler() {
			@Override
			public void onClick(ClickEvent event) {
				StammdatenDialog.this.destroy();
			}
		});
        
        HLayout btnLayout = new HLayout();
        btnLayout.setAlign(Alignment.RIGHT);
        btnLayout.addMember(closeBtn);
        btnLayout.setMargin(5);
        btnLayout.setHeight(closeBtn.getHeight());

        VLayout vLayout = new VLayout();  
        vLayout.setMembersMargin(0);  
        vLayout.setHeight("100%");
        vLayout.addMember(tabSet);  
        vLayout.addMember(btnLayout);
          
        addItem(vLayout);
	}

	/**
	 * Create pane for Kultur/Sorte-Stammdaten pane.
	 * @return
	 */
	private Canvas createKulturPane() {		
		/* create KulturGrid */
		KulturGrid kulturGrid = new KulturGrid();
		Layout kulturLayout = createStammdatenGridLayout(kulturGrid);
		
		/* Sorte grid */
		final SorteGrid sorteGrid = new SorteGrid();
		Layout sorteLayout = createStammdatenGridLayout(sorteGrid);
		
		Label delimeter = new Label("==>");
		delimeter.setAlign(Alignment.CENTER);
		
		//create layout holding sorte and kultur grids
		HLayout layout = new HLayout();
		layout.addMember(kulturLayout);
		layout.addMember(delimeter);
		layout.addMember(sorteLayout);
		
		kulturGrid.addRecordClickHandler(new RecordClickHandler() {
			@Override
			public void onRecordClick(RecordClickEvent event) {
				Record r = event.getRecord();
				if (r instanceof KulturRecord) {
					Kultur selected = ((KulturRecord) r).getDTO();
					sorteGrid.setSelectedKultur(selected);
				}
			}
		});
		
		return layout;
	}

	/**
	 * Create pane for Pflanzenschutz-Stammdaten pane.
	 * @return
	 */
	private Canvas createPflanzenschutzPane() {
		final PSMittelStammdatenGrid grid = new PSMittelStammdatenGrid();
		grid.setHeight("100%");
		Layout layout = createStammdatenGridLayout(grid);
		return layout;
	}

	/**
	 * Create pane for Dunegung-Stammdate pane.
	 * @return
	 */
	private Canvas createDuengungPane() {
		final DuengerartStammdatenGrid grid = new DuengerartStammdatenGrid();
		grid.setHeight("100%");
		Layout layout = createStammdatenGridLayout(grid);
		return layout;
	}

	/**
	 * Creates the {@link BodenbearbeitungTyp} stammdaten panel.
	 * 
	 * @return
	 */
	private Canvas createBodenbearbeitungPane() {
		final BodenbeabeitungStammdatenGrid grid = new BodenbeabeitungStammdatenGrid();
		grid.setHeight("100%");
		Layout layout = createStammdatenGridLayout(grid);
		return layout;
	}
	
	/**
	 * Creates the Stammdaten layout panel holding the grid
	 * and the ADD, EDIT, DELETE buttons beneath the grid.
	 * @param grid the grid
	 * @return the created layout
	 */
	private Layout createStammdatenGridLayout(final AbstractListGrid<?> grid) {
		//create add, remove, edit buttons
		ToolStripButton addButton = new ToolStripButton();  
		addButton.setIcon("[SKIN]actions/add.png");
		addButton.addClickHandler(new ClickHandler() {  
			public void onClick(ClickEvent event) {  
				grid.startEditingNew();  
			}  
		});  

		ToolStripButton removeButton = new ToolStripButton();  
		removeButton.setIcon("[SKIN]actions/remove.png");  
		removeButton.addClickHandler(new ClickHandler() {  
			public void onClick(ClickEvent event) {  
				grid.removeSelectedData();  
			}  
		});

		ToolStripButton editButton = new ToolStripButton();  
		editButton.setIcon("[SKIN]actions/edit.png");  
		editButton.addClickHandler(new ClickHandler() {  
			public void onClick(ClickEvent event) {  
				grid.startEditing();
			}  
		});
		
		//create button panel
		ToolStrip toolstrip = new ToolStrip();
		toolstrip.setWidth(grid.getWidthAsString());
		toolstrip.addFill();
		toolstrip.addButton(addButton);
		toolstrip.addButton(removeButton);
		toolstrip.addButton(editButton);
		
		VLayout layout = new VLayout();
		layout.addMember(grid);
		layout.addMember(toolstrip);
		
		return layout;
	}
	
}
