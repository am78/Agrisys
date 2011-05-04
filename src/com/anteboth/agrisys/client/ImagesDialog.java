package com.anteboth.agrisys.client;

import java.util.Date;
import java.util.List;

import com.anteboth.agrisys.client.grid.data.ImageTileRecord;
import com.anteboth.agrisys.client.model.Aktivitaet;
import com.anteboth.agrisys.client.model.ImageResource;
import com.google.gwt.core.client.GWT;
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.smartgwt.client.types.Alignment;
import com.smartgwt.client.util.SC;
import com.smartgwt.client.widgets.Button;
import com.smartgwt.client.widgets.Window;
import com.smartgwt.client.widgets.events.ClickEvent;
import com.smartgwt.client.widgets.events.ClickHandler;
import com.smartgwt.client.widgets.events.DoubleClickEvent;
import com.smartgwt.client.widgets.events.DoubleClickHandler;
import com.smartgwt.client.widgets.layout.HLayout;
import com.smartgwt.client.widgets.layout.VLayout;
import com.smartgwt.client.widgets.tile.TileGrid;
import com.smartgwt.client.widgets.tile.TileRecord;
import com.smartgwt.client.widgets.viewer.DetailViewerField;

/**
 * The images dialog.
 * @author michael
 */
public class ImagesDialog extends Window {

	private static final String IMG_PREVIEW_URL_PREFIX = "/upload/serve?qual=preview&blob-key=";
	private static final String IMG_URL_PREFIX = "/upload/serve?qual=full&blob-key=";
	private static final String IMG_UPLOAD_URL = "/upload.jsp";

	private ImageTileRecord[] data;
	private Aktivitaet aktivitaet;
	private TileGrid tileGrid;

	public ImagesDialog(Aktivitaet akt) {
		super();
		this.aktivitaet = akt;

		//create the records
		this.data = createRecords(akt);

		//initialize the UI
		initialize();
	}

	private ImageTileRecord[] createRecords(Aktivitaet a) {
		ImageTileRecord[] d = null;

		if (a != null) {		
			List<ImageResource> images = a.getAttachments();

			d = new ImageTileRecord[images.size()];
			int i=0;
			for (ImageResource ir : images) {
				Date date = ir.getDate();
				String pict = ir.getBlobKey();
				String desc = ir.getDescription();
				d[i] = new ImageTileRecord(date, pict, desc);
				i++;
			}
		} else {
			d = new ImageTileRecord[0];
		}
		return d;
	}

	/**
	 * Reloads and displays the data.
	 */
	public void reloadData() {
		GWT.log("reload images");
		Agrisys.getAgrisysservice().loadAktivitaet(
				this.aktivitaet.getId(), 
				new AsyncCallback<Aktivitaet>() {
					@Override
					public void onSuccess(Aktivitaet result) {
						resetData(result);
					}

					@Override
					public void onFailure(Throwable caught) {
					}
				});
	}

	/**
	 * Resets the aktivitaet entry and relaods and redisplays the data.
	 * 
	 * @param akt the new aktivitaet item
	 */
	protected void resetData(Aktivitaet akt) {
		this.aktivitaet = akt;
		this.data = createRecords(this.aktivitaet);
		this.tileGrid.setData(this.data);
	}

	/**
	 * Inits the UI.
	 */
	private void initialize() {
		setTitle("Bilder");

		this.tileGrid = new TileGrid();  
		tileGrid.setTileWidth(200);  
		tileGrid.setTileHeight(240);  
		tileGrid.setHeight100();  
		tileGrid.setWidth100();  
		//tileGrid.setCanReorderTiles(true);  
		tileGrid.setShowAllRecords(true);  
		tileGrid.setData(this.data);  
		tileGrid.addDoubleClickHandler(new DoubleClickHandler() {
			@Override
			public void onDoubleClick(DoubleClickEvent event) {		
				onTileDoubleClicked(tileGrid.getSelectedRecord());
			}
		});

		DetailViewerField pictureField = new DetailViewerField("picture");  
		pictureField.setType("image");  
		pictureField.setImageURLPrefix(IMG_PREVIEW_URL_PREFIX);  
		pictureField.setImageWidth(200);  
		//pictureField.setImageHeight(200);  

		DetailViewerField dateField = new DetailViewerField("date");
		DetailViewerField descField = new DetailViewerField("description");

		tileGrid.setFields(pictureField, dateField, descField);  

		VLayout vLayout = new VLayout();  
		vLayout.setMembersMargin(0);  
		vLayout.setHeight("100%");
		vLayout.addMember(tileGrid);

		//close dialog button
		Button closeBtn = new Button("Schlie&szlig;en");
		closeBtn.addClickHandler(new ClickHandler() {
			@Override
			public void onClick(ClickEvent event) {
				destroy();
			}
		});

		//delete image button
		Button deleteBtn = new Button("Foto l&ouml;schen");
		deleteBtn.addClickHandler(new ClickHandler() {
			@Override
			public void onClick(ClickEvent event) {
				onDeleteRecord(tileGrid.getSelectedRecord());
			}
		});

		//upload image button
		String refId = this.aktivitaet.getId().toString();
		UploadPhoto upload = new UploadPhoto(this.aktivitaet.getId()) {
			@Override
			protected void onImageUploaded() {
				//reload images after upload finished
				reloadData();
			}
		};

		HLayout btnLayout = new HLayout();
		btnLayout.setAlign(Alignment.RIGHT);
		btnLayout.addMember(deleteBtn);
		btnLayout.addMember(closeBtn);
		btnLayout.setMargin(10);
		btnLayout.setHeight(closeBtn.getHeight());

		vLayout.addMember(upload);
		vLayout.addMember(btnLayout);

		addItem(vLayout);		
		
		reloadData();
	}
	
	

	protected void onDeleteRecord(TileRecord record) {		
		final String picKey = record.getAttribute("picture");
		Agrisys.getAgrisysservice().deleteResource(
				aktivitaet.getId(), picKey, 
				new AsyncCallback<Void>() {
					@Override
					public void onSuccess(Void result) {
						//now reload data
						reloadData();
						SC.say("Das Bild wurde erfolgreich gel&ouml;scht.");
					}

					@Override
					public void onFailure(Throwable caught) {
						SC.say("Fehler beim L&ouml;schen des Bildes.");
					}
				});
	}

	/**
	 * On 2xClick we want to load the selected image in original size in a separate 
	 * browser window.
	 * @param record the selected record
	 */
	protected void onTileDoubleClicked(TileRecord record) {
		String pic = record.getAttribute("picture");
		String url = IMG_URL_PREFIX + pic;
		String title = "Foto vom " + getAttribute("date");
		String features = "menubar=no,location=no,resizable=yes,scrollbars=yes,status=yes";

		//open the image in new window
		com.google.gwt.user.client.Window.open(url, title, features);
	}


	/**
	 * Displays the image upload page and closes image dialog.
	 */
	protected void onUploadFileClicked() {
		String url = IMG_UPLOAD_URL + "?refId=" + aktivitaet.getId();
		String title = "Foto upload...";
		String features = "menubar=no,location=no,resizable=yes,scrollbars=yes,status=yes";

		//open the image in new window
		com.google.gwt.user.client.Window.open(url, title, features);

		destroy();
	}
}
