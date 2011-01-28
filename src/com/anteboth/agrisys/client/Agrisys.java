package com.anteboth.agrisys.client;

import java.util.ArrayList;
import java.util.List;

import com.anteboth.agrisys.client.grid.AbstractListGrid;
import com.anteboth.agrisys.client.grid.AussaatGrid;
import com.anteboth.agrisys.client.grid.BodenbearbeitungGrid;
import com.anteboth.agrisys.client.grid.ErnteGrid;
import com.anteboth.agrisys.client.grid.SchlaglisteGrid;
import com.anteboth.agrisys.client.grid.data.SchlagRecord;
import com.anteboth.agrisys.client.model.SchlagErntejahr;
import com.anteboth.agrisys.client.model.UserDataTO;
import com.anteboth.agrisys.client.ui.LoginScreen;
import com.google.gwt.core.client.EntryPoint;
import com.google.gwt.core.client.GWT;
import com.google.gwt.core.client.GWT.UncaughtExceptionHandler;
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.google.gwt.user.client.ui.RootPanel;
import com.smartgwt.client.types.Alignment;
import com.smartgwt.client.types.SelectionType;
import com.smartgwt.client.types.Side;
import com.smartgwt.client.types.VerticalAlignment;
import com.smartgwt.client.util.SC;
import com.smartgwt.client.widgets.Canvas;
import com.smartgwt.client.widgets.ImgButton;
import com.smartgwt.client.widgets.Label;
import com.smartgwt.client.widgets.events.ClickEvent;
import com.smartgwt.client.widgets.events.ClickHandler;
import com.smartgwt.client.widgets.grid.ListGrid;
import com.smartgwt.client.widgets.grid.events.RecordClickEvent;
import com.smartgwt.client.widgets.grid.events.RecordClickHandler;
import com.smartgwt.client.widgets.layout.HLayout;
import com.smartgwt.client.widgets.layout.VLayout;
import com.smartgwt.client.widgets.tab.Tab;
import com.smartgwt.client.widgets.tab.TabSet;
import com.smartgwt.client.widgets.tab.events.TabSelectedEvent;
import com.smartgwt.client.widgets.tab.events.TabSelectedHandler;
import com.smartgwt.client.widgets.toolbar.ToolStrip;
import com.smartgwt.client.widgets.toolbar.ToolStripButton;

/**
 * Entry point classes define <code>onModuleLoad()</code>.
 */
public class Agrisys implements EntryPoint, UncaughtExceptionHandler {

	/** Create a remote service proxy to talk to the server-side Agrisys service. */
	private static final AgrisysServiceAsync agrisysService = GWT.create(AgrisysService.class);

	/** Holds the user data of the current user. */
	private static UserDataTO userData;

	/** The list of ISchlagErntejahrSelectionListener. */
	private List<ISchlagErntejahrSelectionListener> schlagErntejahrListeners = 
		new ArrayList<ISchlagErntejahrSelectionListener>();

	private AbstractListGrid<?> currentRightGrid;
	private VLayout rightLayout;
	private ToolStrip rightToolStrip;

	private BodenbearbeitungGrid bodenbearbeitungGrid;
	private AussaatGrid aussaatGrid;
	private ErnteGrid ernteGrid;
	
	@Override
	public void onUncaughtException(Throwable e) {
		e.printStackTrace();
		SC.warn(e.getLocalizedMessage());
	}

	/**
	 * This is the entry point method.
	 */
	public void onModuleLoad() {
		GWT.setUncaughtExceptionHandler(this); 
		
		//ensure that the user is authenticated
		agrisysService.isUserAuthenticated(new AsyncCallback<UserDataTO>() {
			@Override
			public void onSuccess(UserDataTO user) {
				if (user != null) {
					setUserData(user);
					//user is logged -> proceed loading the module
					loadModule();
				} else {
					//if the user isn't logged in, go to the login page
					LoginScreen loginDlg = new LoginScreen() {
						@Override
						public void performLogin(String name, String pass) {
							login(name, pass);
							this.removeFromParent();
						}
					};
					RootPanel.get("mainPanel").add(loginDlg);
				}
			}
			
			@Override
			public void onFailure(Throwable caught) {
				System.err.println(caught.getLocalizedMessage());
			}
		});
	}
	
	public static UserDataTO getUserData() {
		return userData;
	}
	
	public static AgrisysServiceAsync getAgrisysservice() {
		return agrisysService;
	}

	/**
	 * Called when the login dialog submits.
	 * @param name
	 * @param pass
	 */
	protected void login(String name, String pass) {
		//authenticate the user
		agrisysService.authenticate(name, pass, new AsyncCallback<UserDataTO>() {
			@Override
			public void onSuccess(UserDataTO userData) {
				setUserData(userData);
				if (userData != null) {
					loadModule();
				} else {
					//user not logged in
					//start another try
					onModuleLoad();
				}
			}
			
			@Override
			public void onFailure(Throwable caught) {
				System.out.println("Fehler:");
				System.err.println(caught.getLocalizedMessage());
			}
		});		
	}


	/**
	 * Sets the user data.
	 * @param userData
	 */
	protected void setUserData(UserDataTO userData) {
		this.userData = userData;
	}

	/**
	 * Loads the application module if the user is authenticated.
	 */
	private void loadModule() {
		/* layout */

		//create the left table for the left side
		VLayout leftLayout = createLeftPanel();        	

		//main layout
		HLayout mainLayout = new HLayout();  
		mainLayout.setWidth("90%");
		mainLayout.setHeight("75%");
		mainLayout.setAlign(Alignment.CENTER);
		mainLayout.setBorder("1px solid lightgray");
		mainLayout.setBackgroundColor("#DDDDDD");

		//add left panel
		mainLayout.addMember(leftLayout);

		//create right panel
		VLayout rightLayout = createRightPanel();

		mainLayout.addMember(rightLayout);

		//add contents to the HTML container name "mainPanel"
		RootPanel.get("mainPanel").add(mainLayout);	
		
		//create the user label
		String user = 	 userData.getAccount().getUsername();
		String betrieb = userData.getBetrieb().getName();
		int erntejahr =  userData.getErntejahr().getErntejahr();
		Label userLabel = new Label(
			"Benutzer: " + user + " | Betrieb: " + betrieb + " | Erntejahr: " + erntejahr);
		userLabel.setWidth("300");
		
		RootPanel.get("userInfoPanel").add(userLabel);
		
		//create the stammdaten button
		ImgButton btnStammdaten = new ImgButton();
		btnStammdaten.setSrc("/img/stammdaten_s.png");  
		btnStammdaten.setWidth(25);  
		btnStammdaten.setHeight(25);  
		btnStammdaten.setShowRollOver(false);  
		btnStammdaten.setShowDown(false);  
		btnStammdaten.setActionType(SelectionType.BUTTON); 
		btnStammdaten.addClickHandler(new ClickHandler() {
			@Override
			public void onClick(ClickEvent event) {
				loadStammdaten();
			}
		});
		btnStammdaten.setValign(VerticalAlignment.BOTTOM);
		
		RootPanel.get("buttonStammdatenPanel").add(btnStammdaten);
	}
	
	

	/**
	 * Creates the right panel and it's subpanels.
	 * @return
	 */
	private VLayout createRightPanel() {
		// create button panel
//		int w = 120, h = 25; //int w = 138, h = 25;
//		String[] btns = new String[]{
//				"[APP]/img/button-boden.png", "/img/button-aussaat.png", "/img/button-duenger.png",
//				"/img/button-pflanzenschutz.png", "/img/button-ernte.png"
//		};
//
//		HLayout btnlayout = new HLayout(2);
//		btnlayout.setAlign(Alignment.CENTER);
//		for (String s : btns) {
//			final ImgButton imgButton = new ImgButton();
//			//        	if (i == 0) imgButton.setSelected(true);
//			imgButton.setSrc(s);  
//			imgButton.setWidth(w);  
//			imgButton.setHeight(h);  
//			imgButton.setShowRollOver(true);  
//			imgButton.setShowDown(true);  
//			imgButton.setActionType(SelectionType.RADIO); 
//			imgButton.setRadioGroup("schlagnavigationGroup");
//			btnlayout.addMember(imgButton);
//		}
//
//
//		ImgButton imgButton = new ImgButton();
//		imgButton.setSrc("/img/stammdaten_s.png");  
//		imgButton.setWidth(25);  
//		imgButton.setHeight(25);  
//		imgButton.setShowRollOver(false);  
//		imgButton.setShowDown(false);  
//		imgButton.setActionType(SelectionType.BUTTON); 
//		btnlayout.addMember(imgButton);
//		
//		imgButton.addClickHandler(new ClickHandler() {
//			@Override
//			public void onClick(ClickEvent event) {
//				loadStammdaten();
//			}
//		});
		
		
		this.bodenbearbeitungGrid = new BodenbearbeitungGrid();
		this.bodenbearbeitungGrid.setHeight("100%");
		
		this.currentRightGrid = this.bodenbearbeitungGrid;
		
		this.aussaatGrid = new AussaatGrid();
		this.aussaatGrid.setHeight("100%");
		
		this.ernteGrid = new ErnteGrid();
		this.ernteGrid.setHeight("100%");

		final TabSet tabSet = new TabSet();  
        tabSet.setTabBarPosition(Side.TOP);  
        tabSet.setTabBarAlign(Side.LEFT);
        tabSet.setBackgroundColor("#996633");
  
        Tab tabBoden = new Tab("Bodenbearbeitung");  
        tabBoden.setPane(this.bodenbearbeitungGrid);
        tabBoden.setIcon("[APP]/img/boden_16x16.png");
        
        Tab tabAussaat = new Tab("Aussaat");
        tabAussaat.setPane(this.aussaatGrid);
        tabAussaat.setIcon("[APP]/img/aussaat_16x16.png");
        	
        Tab tabDuengung = new Tab("D&uuml;ngung");
        tabDuengung.setIcon("[APP]/img/duengung_16x16.png");
        
        Tab tabPflanzenschutz = new Tab("Pflanzenschutz");
        tabPflanzenschutz.setIcon("[APP]/img/pflanzenschutz_16x16.png");
        
        Tab tabErnte = new Tab("Ernte");
        tabErnte.setPane(this.ernteGrid);
        tabErnte.setIcon("[APP]/img/ernte_16x16.png");
        
  
        tabSet.addTab(tabBoden);
        tabSet.addTab(tabAussaat);
        tabSet.addTab(tabDuengung);
        tabSet.addTab(tabPflanzenschutz);
        tabSet.addTab(tabErnte);
        
        tabSet.setSelectedTab(0);

        tabSet.addTabSelectedHandler(new TabSelectedHandler() {
			@Override
			public void onTabSelected(TabSelectedEvent event) {
				Canvas pane = event.getTab().getPane();
				System.out.println("selected tab: " + event.getTab().getTitle());
				if (pane instanceof AbstractListGrid<?>) {
					currentRightGrid = (AbstractListGrid<?>) pane;
				}
				int selTab = event.getTabNum();
				if (selTab == 0) {
					tabSet.setBackgroundColor("#996633");
				} else if (selTab == 1) {
					tabSet.setBackgroundColor("#99cc00");
				} else if (selTab == 2) {
					tabSet.setBackgroundColor("#3399cc");
				} else if (selTab == 3) {
					tabSet.setBackgroundColor("#993399");
				} else if (selTab == 4) {
					tabSet.setBackgroundColor("#ffcc00");
				}
				
			}
		});
		
		//add ISchlagErntejahrSelectionListener
		this.addSchlagErntejahrSelectionListener(bodenbearbeitungGrid);
		this.addSchlagErntejahrSelectionListener(aussaatGrid);
		this.addSchlagErntejahrSelectionListener(ernteGrid);

		//create add, remove, edit buttons
		ToolStripButton addButton = new ToolStripButton();  
		addButton.setIcon("[SKIN]actions/add.png");
		addButton.addClickHandler(new ClickHandler() {  
			public void onClick(ClickEvent event) {  
				addNewRightEntry();
			}  
		});  

		ToolStripButton removeButton = new ToolStripButton();  
		removeButton.setIcon("[SKIN]actions/remove.png");  
		removeButton.addClickHandler(new ClickHandler() {  
			public void onClick(ClickEvent event) {  
				removeSelectedRightEntries();
			}  
		});

		ToolStripButton editButton = new ToolStripButton();  
		editButton.setIcon("[SKIN]actions/edit.png");  
		editButton.addClickHandler(new ClickHandler() {  
			public void onClick(ClickEvent event) {  
				editRightEntry();
			}  
		});

		//create button panel
		ToolStrip toolstrip = new ToolStrip();
		toolstrip.setWidth("100%");
		toolstrip.addFill();
		toolstrip.addButton(addButton);
		toolstrip.addButton(removeButton);
		toolstrip.addButton(editButton);
		this.rightToolStrip = toolstrip;

		//create layout holding the grid and the toolstrip
		this.rightLayout = new VLayout();
		this.rightLayout.addMember(tabSet);
		
//		this.rightLayout.addMember(btnlayout);
//		this.rightLayout.addMember(this.currentRightGrid);
		this.rightLayout.addMember(this.rightToolStrip);

		return this.rightLayout;
	}
	


	protected void editRightEntry() {
		currentRightGrid.startEditing();
	}

	protected void removeSelectedRightEntries() {
		currentRightGrid.removeSelectedData();
	}

	protected void addNewRightEntry() {
		currentRightGrid.startEditingNew();
	}

	protected void loadStammdaten() {
		StammdatenDialog d = new StammdatenDialog();
		d.setIsModal(true);
		d.setShowModalMask(true);
		d.setSize("600", "400");
		d.setCanDragResize(true);
		d.setCanDrag(true);
		d.centerInPage();
		d.show();
	}

	/**
	 * Create the layout and the table (Schlagliste) for the left side of the main panel.
	 * 
	 * @return VLayout
	 */
	private VLayout createLeftPanel() {
		//create the gridview
		final ListGrid schlaglisteGrid = new SchlaglisteGrid();
		schlaglisteGrid.setWidth(180);
		
		schlaglisteGrid.addRecordClickHandler(new RecordClickHandler() {
			@Override
			public void onRecordClick(RecordClickEvent event) {
				SchlagRecord r = (SchlagRecord) event.getRecord();
				if (r != null) {
					SchlagErntejahr se = r.getDTO().getSchlagErntejahr();
					onSchlagErntejahrSelectionChanged(se);
				}
			}
		});

		//create add, remove, edit buttons
		ToolStripButton addButton = new ToolStripButton();  
		addButton.setIcon("[SKIN]actions/add.png");
		addButton.addClickHandler(new ClickHandler() {  
			public void onClick(ClickEvent event) {  
				schlaglisteGrid.startEditingNew();  
			}  
		});  

		ToolStripButton removeButton = new ToolStripButton();  
		removeButton.setIcon("[SKIN]actions/remove.png");  
		removeButton.addClickHandler(new ClickHandler() {  
			public void onClick(ClickEvent event) {  
				schlaglisteGrid.removeSelectedData();  
			}  
		});

		ToolStripButton editButton = new ToolStripButton();  
		editButton.setIcon("[SKIN]actions/edit.png");  
		editButton.addClickHandler(new ClickHandler() {  
			public void onClick(ClickEvent event) {  
				schlaglisteGrid.startEditing();
			}  
		});

		//create button panel
		ToolStrip toolstrip = new ToolStrip();
		toolstrip.setWidth(schlaglisteGrid.getWidthAsString());
		toolstrip.addFill();
		toolstrip.addButton(addButton);
		toolstrip.addButton(removeButton);
		toolstrip.addButton(editButton);

		//create layout holding the grid and the toolstrip
		VLayout leftLayout = new VLayout();
		leftLayout.setAutoWidth();       
		leftLayout.addMember(schlaglisteGrid);
		leftLayout.addMember(toolstrip);
		leftLayout.setShowResizeBar(false);

		return leftLayout;
	}

	/**
	 * Called when the {@link SchlagErntejahr} selection has been changed.
	 * @param se the new selection
	 */
	protected void onSchlagErntejahrSelectionChanged(SchlagErntejahr se) {
		for (ISchlagErntejahrSelectionListener l : this.schlagErntejahrListeners) {
			l.onSchlagErntejahrSelectionChanged(se);
		}
	}
	
	public void addSchlagErntejahrSelectionListener(ISchlagErntejahrSelectionListener l) {
		this.schlagErntejahrListeners.add(l);
	}
	
	public void removeSchlagErntejahrSelectionListener(ISchlagErntejahrSelectionListener l) {
		this.schlagErntejahrListeners.remove(l);
	}
}
