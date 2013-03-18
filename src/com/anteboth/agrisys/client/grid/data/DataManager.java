package com.anteboth.agrisys.client.grid.data;

import java.util.List;

import com.anteboth.agrisys.client.Agrisys;
import com.anteboth.agrisys.client.AgrisysServiceAsync;
import com.anteboth.agrisys.client.grid.AbstractListGrid;
import com.anteboth.agrisys.client.grid.AussaatGrid;
import com.anteboth.agrisys.client.grid.BodenbearbeitungGrid;
import com.anteboth.agrisys.client.grid.DuengungGrid;
import com.anteboth.agrisys.client.grid.ErnteGrid;
import com.anteboth.agrisys.client.grid.PflanzenschutzGrid;
import com.anteboth.agrisys.client.grid.SchlaglisteGrid;
import com.anteboth.agrisys.client.model.Aussaat;
import com.anteboth.agrisys.client.model.Bodenbearbeitung;
import com.anteboth.agrisys.client.model.Duengung;
import com.anteboth.agrisys.client.model.Ernte;
import com.anteboth.agrisys.client.model.Erntejahr;
import com.anteboth.agrisys.client.model.Pflanzenschutz;
import com.anteboth.agrisys.client.model.Schlag;
import com.anteboth.agrisys.client.model.SchlagErntejahr;
import com.anteboth.agrisys.client.model.UserDataTO;
import com.anteboth.agrisys.client.model.stammdaten.Kultur;
import com.anteboth.agrisys.client.model.stammdaten.Sorte;
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.smartgwt.client.data.RecordList;
import com.smartgwt.client.util.SC;

/**
 * Provides operations for data access/manipulation operations. 
 * @author michael
 *
 */
public class DataManager {
	
	private AgrisysServiceAsync service;
	private UserDataTO userData;

	public DataManager() {
		this.userData = Agrisys.getUserData();
		this.service = Agrisys.getAgrisysservice();
	}
	
	/**
	 * Handles exceptions. 
	 * @param caught exception to handle
	 */
	protected void handleException(Throwable caught) {
		caught.printStackTrace();
		SC.warn(caught.getLocalizedMessage());
	}

	/**
	 * Loads the list of {@link Schlag} items.
	 * @param grid
	 */
	public void loadSchlagData(final Erntejahr erntejahr, final SchlaglisteGrid grid) {
		//load Schlag items via service
		service.loadSchlagData(erntejahr, this.userData.getBetrieb(), 
		new AsyncCallback<List<Schlag>>() 
		{
			@Override
			public void onSuccess(List<Schlag> data) {
				//create record list for the grid view
				RecordList result = new RecordList();
				
				for (Schlag s : data) {
					SchlagRecord r = new SchlagRecord(s);
					result.add(r);
				}
				
				//set the grid data
				grid.setData(result);
			}
			
			@Override
			public void onFailure(Throwable caught) {
				handleException(caught);
			}
		});
	}

	/**
	 * Load {@link Schlag} items for current {@link Erntejahr}.
	 * @param grid
	 */
	public void loadSchlagData(SchlaglisteGrid grid) {
		loadSchlagData(this.userData.getErntejahr(), grid);
	}

	/**
	 * Saves a new {@link Schlag} item with the obtained values
	 * and item add to the grid view.
	 * @param name
	 * @param schlagNr TODO
	 * @param flaeche
	 * @param bemerkung
	 * @param erntejahr
	 * @param anbau
	 * @param vorfrucht
	 * @param grid
	 */
	public void saveSchlag(String name, int schlagNr, double flaeche, String bemerkung, 
			int erntejahr, Sorte anbau, Kultur vorfrucht, final AbstractListGrid<SchlagRecord> grid) 
	{
		service.saveNewSchlag(
			this.userData.getBetrieb(), name, schlagNr, flaeche, bemerkung, erntejahr, anbau, 
		vorfrucht, new AsyncCallback<Schlag>() {
			@Override
			public void onSuccess(Schlag result) {
				if (result != null) {
					SchlagRecord r = new SchlagRecord(result);
					grid.addData(r);
				}
			}
			@Override
			public void onFailure(Throwable caught) {
				handleException(caught);
			}
		});
	}
	
	/**
	 * Update the {@link Schlag} item with the obtained values
	 * and item add to the grid view.
	 * @param name
	 * @param schlagNr TODO
	 * @param flaeche
	 * @param bemerkung
	 * @param erntejahr
	 * @param anbau
	 * @param vorfrucht
	 * @param grid
	 */
	public void updateSchlag(Schlag s, String name, int schlagNr, double flaeche, String bemerkung, 
			int erntejahr, Sorte anbau, Kultur vorfrucht, final AbstractListGrid<SchlagRecord> grid) 
	{
		service.updateSchlag(s, this.userData.getBetrieb(), name, schlagNr, flaeche, bemerkung, erntejahr, anbau, vorfrucht, 
			new AsyncCallback<Schlag>() {
				@Override
				public void onSuccess(Schlag result) {
					if (result != null) {
						SchlagRecord r = new SchlagRecord(result);
						grid.redraw();
					}
				}
				@Override
				public void onFailure(Throwable caught) {
					handleException(caught);
				}
			});
	}
	
	public void delete(List<SchlagRecord> records, final SchlaglisteGrid grid) {
		if (records == null || records.isEmpty()) {
			return;
		}
		
		for (final SchlagRecord r : records) {
			if (r != null && r.getDTO() != null){
				Schlag s = r.getDTO();
				AsyncCallback<Void> callback = new AsyncCallback<Void>() {
					@Override
					public void onSuccess(Void result) {
						//if deleted, remove the record from the grid
						grid.removeData(r);
					}
					@Override
					public void onFailure(Throwable caught) {
						handleException(caught);
					}
				};
				//delete the entries
				service.delete(s, callback);
			}
		}
	}

	/* Bodenbearbeitung */
	
	/**
	 * @param record
	 * @param grid
	 */
	public void update(final BodenbearbeitungRecord record, final AbstractListGrid<BodenbearbeitungRecord> grid) {
		Bodenbearbeitung b = record.getDTO();
		service.save(b, new AsyncCallback<Bodenbearbeitung>() {
			@Override
			public void onSuccess(Bodenbearbeitung result) {
				if (result != null) {					
					record.setDTO(result);
				}
			}
			@Override
			public void onFailure(Throwable caught) {
				handleException(caught);
			}
		});
	}
	
	/**
	 * @param record
	 * @param grid
	 */
	public void save(final BodenbearbeitungRecord record, final AbstractListGrid<BodenbearbeitungRecord> grid) {
		Bodenbearbeitung b = record.getDTO();
		service.save(b, new AsyncCallback<Bodenbearbeitung>() {
			@Override
			public void onSuccess(Bodenbearbeitung result) {
				if (result != null) {					
					record.setDTO(result);
					grid.addData(record);
					grid.redraw();
				}
			}
			@Override
			public void onFailure(Throwable caught) {
				handleException(caught);
			}
		});
	}
	
	/**
	 * Delets the obtained records and removes them from the grid.
	 * @param records the records to delete
	 * @param grid the grid
	 */
	public void delete(List<BodenbearbeitungRecord> records, final BodenbearbeitungGrid grid) {
		if (records == null || records.isEmpty()) {
			return;
		}
		
		for (final BodenbearbeitungRecord r : records) {
			if (r != null && r.getDTO() != null){
				Bodenbearbeitung b = r.getDTO();
				AsyncCallback<Void> callback = new AsyncCallback<Void>() {
					@Override
					public void onSuccess(Void result) {
						//if deleted, remove the record from the grid
						grid.removeData(r);
					}
					@Override
					public void onFailure(Throwable caught) {
						handleException(caught);
					}
				};
				//delete the entries
				service.delete(b, callback);
			}
		}
	}

	/**
	 * Loads the {@link Bodenbearbeitung} entries for the obtained {@link SchlagErntejahr} item.
	 * @param schlagErntejahr the {@link SchlagErntejahr} item
	 * @param grid the grid to update with the loaded values
	 */
	public void loadBodenbearbeitungData(final SchlagErntejahr schlagErntejahr, 
			final AbstractListGrid<BodenbearbeitungRecord> grid) 
	{
		//load Schlag items via service
		service.loadBodenbearbeitungData(schlagErntejahr,  
		new AsyncCallback<List<Bodenbearbeitung>>() 
		{
			@Override
			public void onSuccess(List<Bodenbearbeitung> data) {
				//create record list for the grid view
				RecordList result = new RecordList();
				
				for (Bodenbearbeitung b : data) {
					BodenbearbeitungRecord r = new BodenbearbeitungRecord(b);
					result.add(r);
				}
				
				//set the grid data
				grid.setData(result);
			}
			
			@Override
			public void onFailure(Throwable caught) {
				handleException(caught);
			}
		});
	}
	
	
	/* Duengung */
	
	/**
	 * @param record
	 * @param grid
	 */
	public void update(final DuengungRecord record, final AbstractListGrid<DuengungRecord> grid) {
		Duengung b = record.getDTO();
		service.save(b, new AsyncCallback<Duengung>() {
			@Override
			public void onSuccess(Duengung result) {
				if (result != null) {					
					record.setDTO(result);
				}
			}
			@Override
			public void onFailure(Throwable caught) {
				handleException(caught);
			}
		});
	}
	
	/**
	 * @param record
	 * @param grid
	 */
	public void save(final DuengungRecord record, final AbstractListGrid<DuengungRecord> grid) {
		Duengung b = record.getDTO();
		service.save(b, new AsyncCallback<Duengung>() {
			@Override
			public void onSuccess(Duengung result) {
				if (result != null) {					
					record.setDTO(result);
					grid.addData(record);
					grid.redraw();
				}
			}
			@Override
			public void onFailure(Throwable caught) {
				handleException(caught);
			}
		});
	}
	
	/**
	 * Delets the obtained records and removes them from the grid.
	 * @param records the records to delete
	 * @param grid the grid
	 */
	public void delete(List<DuengungRecord> records, final DuengungGrid grid) {
		if (records == null || records.isEmpty()) {
			return;
		}
		
		for (final DuengungRecord r : records) {
			if (r != null && r.getDTO() != null){
				Duengung b = r.getDTO();
				AsyncCallback<Void> callback = new AsyncCallback<Void>() {
					@Override
					public void onSuccess(Void result) {
						//if deleted, remove the record from the grid
						grid.removeData(r);
					}
					@Override
					public void onFailure(Throwable caught) {
						handleException(caught);
					}
				};
				//delete the entries
				service.delete(b, callback);
			}
		}
	}

	/**
	 * Loads the {@link Duengung} entries for the obtained {@link SchlagErntejahr} item.
	 * @param schlagErntejahr the {@link SchlagErntejahr} item
	 * @param grid the grid to update with the loaded values
	 */
	public void loadDuengungData(final SchlagErntejahr schlagErntejahr, 
			final AbstractListGrid<DuengungRecord> grid) 
	{
		//load Schlag items via service
		service.loadDuengungData(schlagErntejahr,  
		new AsyncCallback<List<Duengung>>() 
		{
			@Override
			public void onSuccess(List<Duengung> data) {
				//create record list for the grid view
				RecordList result = new RecordList();
				
				for (Duengung b : data) {
					DuengungRecord r = new DuengungRecord(b);
					result.add(r);
				}
				
				//set the grid data
				grid.setData(result);
			}
			
			@Override
			public void onFailure(Throwable caught) {
				handleException(caught);
			}
		});
	}
	
	
	/* Pflanzenschutz */
	
	/**
	 * @param record
	 * @param grid
	 */
	public void update(final PflanzenschutzRecord record, final AbstractListGrid<PflanzenschutzRecord> grid) {
		Pflanzenschutz ps = record.getDTO();
		service.save(ps, new AsyncCallback<Pflanzenschutz>() {
			@Override
			public void onSuccess(Pflanzenschutz result) {
				if (result != null) {					
					record.setDTO(result);
				}
			}
			@Override
			public void onFailure(Throwable caught) {
				handleException(caught);
			}
		});
	}
	
	/**
	 * @param record
	 * @param grid
	 */
	public void save(final PflanzenschutzRecord record, final AbstractListGrid<PflanzenschutzRecord> grid) {
		Pflanzenschutz ps = record.getDTO();
		service.save(ps, new AsyncCallback<Pflanzenschutz>() {
			@Override
			public void onSuccess(Pflanzenschutz result) {
				if (result != null) {					
					record.setDTO(result);
					grid.addData(record);
					grid.redraw();
				}
			}
			@Override
			public void onFailure(Throwable caught) {
				handleException(caught);
			}
		});
	}
	
	/**
	 * Delets the obtained records and removes them from the grid.
	 * @param records the records to delete
	 * @param grid the grid
	 */
	public void delete(List<PflanzenschutzRecord> records, final PflanzenschutzGrid grid) {
		if (records == null || records.isEmpty()) {
			return;
		}
		
		for (final PflanzenschutzRecord r : records) {
			if (r != null && r.getDTO() != null){
				Pflanzenschutz ps = r.getDTO();
				AsyncCallback<Void> callback = new AsyncCallback<Void>() {
					@Override
					public void onSuccess(Void result) {
						//if deleted, remove the record from the grid
						grid.removeData(r);
					}
					@Override
					public void onFailure(Throwable caught) {
						handleException(caught);
					}
				};
				//delete the entries
				service.delete(ps, callback);
			}
		}
	}

	/**
	 * Loads the {@link Pflanzenschutz} entries for the obtained {@link SchlagErntejahr} item.
	 * @param schlagErntejahr the {@link SchlagErntejahr} item
	 * @param grid the grid to update with the loaded values
	 */
	public void loadPflanzenschutzData(final SchlagErntejahr schlagErntejahr, 
			final AbstractListGrid<PflanzenschutzRecord> grid) 
	{
		//load Schlag items via service
		service.loadPflanzenschutzData(schlagErntejahr,  
		new AsyncCallback<List<Pflanzenschutz>>() 
		{
			@Override
			public void onSuccess(List<Pflanzenschutz> data) {
				//create record list for the grid view
				RecordList result = new RecordList();
				
				for (Pflanzenschutz ps : data) {
					PflanzenschutzRecord r = new PflanzenschutzRecord(ps);
					result.add(r);
				}
				
				//set the grid data
				grid.setData(result);
			}
			
			@Override
			public void onFailure(Throwable caught) {
				handleException(caught);
			}
		});
	}
	
	
	/* Aussaat */
	
	/**
	 * @param record
	 * @param grid
	 */
	public void update(final AussaatRecord record, final AbstractListGrid<AussaatRecord> grid) {
		Aussaat dto = record.getDTO();
		service.save(dto, new AsyncCallback<Aussaat>() {
			@Override
			public void onSuccess(Aussaat result) {
				if (result != null) {					
					record.setDTO(result);
					grid.redraw();
				}
			}
			@Override
			public void onFailure(Throwable caught) {
				handleException(caught);
			}
		});
	}
	
	/**
	 * @param record
	 * @param grid
	 */
	public void save(final AussaatRecord record, final AbstractListGrid<AussaatRecord> grid) {
		Aussaat dto = record.getDTO();
		service.save(dto, new AsyncCallback<Aussaat>() {
			@Override
			public void onSuccess(Aussaat result) {
				if (result != null) {					
					record.setDTO(result);
					grid.addData(record);
				}
			}
			@Override
			public void onFailure(Throwable caught) {
				handleException(caught);
			}
		});
	}
	
	/**
	 * Delets the obtained records and removes them from the grid.
	 * @param records the records to delete
	 * @param grid the grid
	 */
	public void delete(List<AussaatRecord> records, final AussaatGrid grid) {
		if (records == null || records.isEmpty()) {
			return;
		}
		
		for (final AussaatRecord r : records) {
			if (r != null && r.getDTO() != null){
				Aussaat dto = r.getDTO();
				AsyncCallback<Void> callback = new AsyncCallback<Void>() {
					@Override
					public void onSuccess(Void result) {
						//if deleted, remove the record from the grid
						grid.removeData(r);
					}
					@Override
					public void onFailure(Throwable caught) {
						handleException(caught);
					}
				};
				//delete the entries
				service.delete(dto, callback);
			}
		}
	}

	/**
	 * Loads the {@link Aussaat} entries for the obtained {@link SchlagErntejahr} item.
	 * @param schlagErntejahr the {@link SchlagErntejahr} item
	 * @param grid the grid to update with the loaded values
	 */
	public void loadAussaatData(final SchlagErntejahr schlagErntejahr, 
			final AbstractListGrid<AussaatRecord> grid) 
	{
		//load Schlag items via service
		service.loadAussaatData(schlagErntejahr,  
		new AsyncCallback<List<Aussaat>>() 
		{
			@Override
			public void onSuccess(List<Aussaat> data) {
				//create record list for the grid view
				RecordList result = new RecordList();
				
				for (Aussaat dto : data) {
					AussaatRecord r = new AussaatRecord(dto);
					result.add(r);
				}
				
				//set the grid data
				grid.setData(result);
			}
			
			@Override
			public void onFailure(Throwable caught) {
				handleException(caught);
			}
		});
	}
	
	/* Ernte */
	
	/**
	 * @param record
	 * @param grid
	 */
	public void update(final ErnteRecord record, final AbstractListGrid<ErnteRecord> grid) {
		Ernte dto = record.getDTO();
		service.save(dto, new AsyncCallback<Ernte>() {
			@Override
			public void onSuccess(Ernte result) {
				if (result != null) {					
					record.setDTO(result);
					grid.redraw();
				}
			}
			@Override
			public void onFailure(Throwable caught) {
				handleException(caught);
			}
		});
	}
	
	/**
	 * @param record
	 * @param grid
	 */
	public void save(final ErnteRecord record, final AbstractListGrid<ErnteRecord> grid) {
		Ernte dto = record.getDTO();
		service.save(dto, new AsyncCallback<Ernte>() {
			@Override
			public void onSuccess(Ernte result) {
				if (result != null) {					
					record.setDTO(result);
					grid.addData(record);
				}
			}
			@Override
			public void onFailure(Throwable caught) {
				handleException(caught);
			}
		});
	}
	
	/**
	 * Delets the obtained records and removes them from the grid.
	 * @param records the records to delete
	 * @param grid the grid
	 */
	public void delete(List<ErnteRecord> records, final ErnteGrid grid) {
		if (records == null || records.isEmpty()) {
			return;
		}
		
		for (final ErnteRecord r : records) {
			if (r != null && r.getDTO() != null){
				Ernte dto = r.getDTO();
				AsyncCallback<Void> callback = new AsyncCallback<Void>() {
					@Override
					public void onSuccess(Void result) {
						//if deleted, remove the record from the grid
						grid.removeData(r);
					}
					@Override
					public void onFailure(Throwable caught) {
						handleException(caught);
					}
				};
				//delete the entries
				service.delete(dto, callback);
			}
		}
	}

	/**
	 * Loads the {@link Ernte} entries for the obtained {@link SchlagErntejahr} item.
	 * @param schlagErntejahr the {@link SchlagErntejahr} item
	 * @param grid the grid to update with the loaded values
	 */
	public void loadErnteData(final SchlagErntejahr schlagErntejahr, 
			final AbstractListGrid<ErnteRecord> grid) 
	{
		//load Schlag items via service
		service.loadErnteData(schlagErntejahr,  
		new AsyncCallback<List<Ernte>>() 
		{
			@Override
			public void onSuccess(List<Ernte> data) {
				//create record list for the grid view
				RecordList result = new RecordList();
				
				for (Ernte dto : data) {
					ErnteRecord r = new ErnteRecord(dto);
					result.add(r);
				}
				
				//set the grid data
				grid.setData(result);
			}
			
			@Override
			public void onFailure(Throwable caught) {
				handleException(caught);
			}
		});
	}
}
