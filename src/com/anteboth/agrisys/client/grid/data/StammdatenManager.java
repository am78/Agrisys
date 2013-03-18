package com.anteboth.agrisys.client.grid.data;

import java.util.List;

import com.anteboth.agrisys.client.Agrisys;
import com.anteboth.agrisys.client.AgrisysServiceAsync;
import com.anteboth.agrisys.client.grid.BodenbeabeitungStammdatenGrid;
import com.anteboth.agrisys.client.grid.DuengerartStammdatenGrid;
import com.anteboth.agrisys.client.grid.KulturGrid;
import com.anteboth.agrisys.client.grid.PSMittelStammdatenGrid;
import com.anteboth.agrisys.client.grid.SorteGrid;
import com.anteboth.agrisys.client.model.Erntejahr;
import com.anteboth.agrisys.client.model.UserDataTO;
import com.anteboth.agrisys.client.model.stammdaten.BodenbearbeitungTyp;
import com.anteboth.agrisys.client.model.stammdaten.Duengerart;
import com.anteboth.agrisys.client.model.stammdaten.Kultur;
import com.anteboth.agrisys.client.model.stammdaten.PSMittel;
import com.anteboth.agrisys.client.model.stammdaten.Sorte;
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.googlecode.objectify.Key;
import com.smartgwt.client.data.RecordList;
import com.smartgwt.client.util.SC;
import com.smartgwt.client.widgets.form.fields.SelectItem;
import com.smartgwt.client.widgets.grid.ListGrid;

public class StammdatenManager {
	
	private AgrisysServiceAsync service;
	private UserDataTO userData;

	public StammdatenManager() {
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
	
	/* BodenbearbeitungTyp */

	/**
	 * Loads the list of {@link BodenbearbeitungTyp} items.
	 * @param grid
	 */
	public void loadBodenbearbeitungStammdatenRecords(final ListGrid grid) {
		//load BodenbearbeitungTypen via service
		service.loadBodenbearbeitungTypen(new AsyncCallback<List<BodenbearbeitungTyp>>() {
			@Override
			public void onSuccess(List<BodenbearbeitungTyp> typen) {
				//create record list for the grid view
				RecordList result = new RecordList();
				
				for (BodenbearbeitungTyp t : typen) {
					BodenbearbeitungTypRecord r = new BodenbearbeitungTypRecord(t);
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
	 * Saves a new record.
	 * @param r the record to save
	 * @param grid the new record will be added to this grid
	 */
	public void save(final BodenbearbeitungTypRecord r, final BodenbeabeitungStammdatenGrid grid) {
		if (r == null || r.getDTO() == null) {
			return;
		}
		
		BodenbearbeitungTyp typ = r.getDTO();
		AsyncCallback<BodenbearbeitungTyp> callback = new AsyncCallback<BodenbearbeitungTyp>() {
			@Override
			public void onSuccess(BodenbearbeitungTyp result) {
				//reset the dto in list recode
				r.setDTO(result);
				grid.addData(r);
			}
			@Override
			public void onFailure(Throwable caught) {
				handleException(caught);
			}
		};
		//perform the save operation
		service.save(typ, callback);
	}
	
	/**
	 * Updates a record.
	 * @param r the record to save
	 * @param grid 
	 */
	public void update(final BodenbearbeitungTypRecord r, final BodenbeabeitungStammdatenGrid grid) {
		if (r == null || r.getDTO() == null) {
			return;
		}
		
		BodenbearbeitungTyp typ = r.getDTO();
		AsyncCallback<BodenbearbeitungTyp> callback = new AsyncCallback<BodenbearbeitungTyp>() {
			@Override
			public void onSuccess(BodenbearbeitungTyp result) {
				//reset the dto in list recode
				r.setDTO(result);
				grid.redraw();
			}
			@Override
			public void onFailure(Throwable caught) {
				handleException(caught);
			}
		};
		//perform the save operation
		service.save(typ, callback);
	}
	
	/**
	 * Deletes the obtained records
	 * @param records the records to delete
	 * @param grid the grid to refresh after record deleted
	 */
	public void delete(List<BodenbearbeitungTypRecord> records, final BodenbeabeitungStammdatenGrid grid) {
		if (records == null || records.isEmpty()) {
			return;
		}
		
		for (final BodenbearbeitungTypRecord r : records) {
			if (r != null && r.getDTO() != null){
				BodenbearbeitungTyp typ = r.getDTO();
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
				//delete the entry
				service.delete(typ, callback);
			}
		}
	}
	
	/* PSMittel */
	
	/**
	 * Loads the list of {@link PSMittel} items.
	 * @param grid
	 */
	public void loadPSMittelStammdatenRecords(final ListGrid grid) {
		//load BodenbearbeitungTypen via service
		service.loadPSMittel(new AsyncCallback<List<PSMittel>>() {
			@Override
			public void onSuccess(List<PSMittel> data) {
				//create record list for the grid view
				RecordList result = new RecordList();
				
				for (PSMittel t : data) {
					PSMittelRecord r = new PSMittelRecord(t);
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
	 * Saves a new record.
	 * @param r the record to save
	 * @param grid the new record will be added to this grid
	 */
	public void save(final PSMittelRecord r, final PSMittelStammdatenGrid grid) {
		if (r == null || r.getDTO() == null) {
			return;
		}
		
		PSMittel dto = r.getDTO();
		AsyncCallback<PSMittel> callback = new AsyncCallback<PSMittel>() {
			@Override
			public void onSuccess(PSMittel result) {
				//reset the dto in list recode
				r.setDTO(result);
				grid.addData(r);
			}
			@Override
			public void onFailure(Throwable caught) {
				handleException(caught);
			}
		};
		//perform the save operation
		service.save(dto, callback);
	}
	
	/**
	 * Updates a record.
	 * @param r the record to save
	 * @param grid 
	 */
	public void update(final PSMittelRecord r, final PSMittelStammdatenGrid grid) {
		if (r == null || r.getDTO() == null) {
			return;
		}
		
		PSMittel dto = r.getDTO();
		AsyncCallback<PSMittel> callback = new AsyncCallback<PSMittel>() {
			@Override
			public void onSuccess(PSMittel result) {
				//reset the dto in list recode
				r.setDTO(result);
				grid.redraw();
			}
			@Override
			public void onFailure(Throwable caught) {
				handleException(caught);
			}
		};
		//perform the save operation
		service.save(dto, callback);
	}
	
	/**
	 * Deletes the obtained records
	 * @param records the records to delete
	 * @param grid the grid to refresh after record deleted
	 */
	public void delete(List<PSMittelRecord> records, final PSMittelStammdatenGrid grid) {
		if (records == null || records.isEmpty()) {
			return;
		}
		
		for (final PSMittelRecord r : records) {
			if (r != null && r.getDTO() != null){
				PSMittel dto = r.getDTO();
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
				//delete the entry
				service.delete(dto, callback);
			}
		}
	}
	
	/* Duengerart */
	
	/**
	 * Loads the list of {@link PSMittel} items.
	 * @param grid
	 */
	public void loadDuengerartStammdatenRecords(final ListGrid grid) {
		//load BodenbearbeitungTypen via service
		service.loadDuengerart(new AsyncCallback<List<Duengerart>>() {
			@Override
			public void onSuccess(List<Duengerart> data) {
				//create record list for the grid view
				RecordList result = new RecordList();
				
				for (Duengerart t : data) {
					DuengerartRecord r = new DuengerartRecord(t);
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
	 * Saves a new record.
	 * @param r the record to save
	 * @param grid the new record will be added to this grid
	 */
	public void save(final DuengerartRecord r, final DuengerartStammdatenGrid grid) {
		if (r == null || r.getDTO() == null) {
			return;
		}
		
		Duengerart dto = r.getDTO();
		AsyncCallback<Duengerart> callback = new AsyncCallback<Duengerart>() {
			@Override
			public void onSuccess(Duengerart result) {
				//reset the dto in list recode
				r.setDTO(result);
				grid.addData(r);
			}
			@Override
			public void onFailure(Throwable caught) {
				handleException(caught);
			}
		};
		//perform the save operation
		service.save(dto, callback);
	}
	
	/**
	 * Updates a record.
	 * @param r the record to save
	 * @param grid 
	 */
	public void update(final DuengerartRecord r, final DuengerartStammdatenGrid grid) {
		if (r == null || r.getDTO() == null) {
			return;
		}
		
		Duengerart dto = r.getDTO();
		AsyncCallback<Duengerart> callback = new AsyncCallback<Duengerart>() {
			@Override
			public void onSuccess(Duengerart result) {
				//reset the dto in list recode
				r.setDTO(result);
				grid.redraw();
			}
			@Override
			public void onFailure(Throwable caught) {
				handleException(caught);
			}
		};
		//perform the save operation
		service.save(dto, callback);
	}
	
	/**
	 * Deletes the obtained records
	 * @param records the records to delete
	 * @param grid the grid to refresh after record deleted
	 */
	public void delete(List<DuengerartRecord> records, final DuengerartStammdatenGrid grid) {
		if (records == null || records.isEmpty()) {
			return;
		}
		
		for (final DuengerartRecord r : records) {
			if (r != null && r.getDTO() != null){
				Duengerart dto = r.getDTO();
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
				//delete the entry
				service.delete(dto, callback);
			}
		}
	}

	/**
	 * Loads all {@link Erntejahr} entries. 
	 * @param item
	 */
	public void loadErntejahrData(final SelectItem item) {
		service.loadErntejahrData(new AsyncCallback<List<Erntejahr>>() {
			@Override
			public void onSuccess(List<Erntejahr> result) {				
				String[] data = new String[result.size()];
				for (int i = 0; i < result.size(); i++) {
					data[i] = result.get(i).getErntejahr() + "";
				}
				item.setValueMap(data);				
				item.setValue(userData.getErntejahr().getErntejahr() + "");
			}
			@Override
			public void onFailure(Throwable caught) {
				handleException(caught);
			}
		});
	}

	/**
	 * Loads the {@link Erntejahr} item for the specified key and
	 * sets the erntejahr value as list selection in the SelectItem.
	 * @param item select the value in this item
	 * @param key load {@link Erntejahr} for this key
	 */
	public void updateErntejahrField(final SelectItem item, Key<Erntejahr> key) {
		service.loadErntejahr(key, new AsyncCallback<Erntejahr>() {
			@Override
			public void onSuccess(Erntejahr result) {
				if (item != null && result != null) {
					item.setValue(result.getErntejahr());
				}
			}
			@Override
			public void onFailure(Throwable caught) {
				handleException(caught);
			}
		});
	}

	/**
	 * Load the Kultur entries and displays them in the grid.
	 * @param kulturGrid
	 */
	public void loadKulturStammdatenRecords(final KulturGrid grid) {
		//load BodenbearbeitungTypen via service
		service.loadKulturData(new AsyncCallback<List<Kultur>>() {
			@Override
			public void onSuccess(List<Kultur> data) {
				//create record list for the grid view
				RecordList result = new RecordList();
				
				for (Kultur t : data) {
					KulturRecord r = new KulturRecord(t);
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
	 * Saves the obtained Kultur item.
	 * @param r item to save
	 * @param grid grid to update
	 */
	public void save(final KulturRecord r, final KulturGrid grid) {
		if (r == null || r.getDTO() == null) {
			return;
		}
		
		Kultur dto = r.getDTO();
		AsyncCallback<Kultur> callback = new AsyncCallback<Kultur>() {
			@Override
			public void onSuccess(Kultur result) {
				//reset the dto in list recode
				r.setDTO(result);
				grid.addData(r);
			}
			@Override
			public void onFailure(Throwable caught) {
				handleException(caught);
			}
		};
		//perform the save operation
		service.save(dto, callback);
	}

	/**
	 * Deletes the obtained Kultur item.
	 * @param records item to delete.
	 * @param grid grid to update
	 */
	public void delete(List<KulturRecord> records, final KulturGrid grid) {
		if (records == null || records.isEmpty()) {
			return;
		}
		
		for (final KulturRecord r : records) {
			if (r != null && r.getDTO() != null){
				Kultur dto = r.getDTO();
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
				//delete the entry
				service.delete(dto, callback);
			}
		}
	}

	/**
	 * Updates the kultur record.
	 * @param record
	 * @param kulturGrid
	 */
	public void update(final KulturRecord r, final KulturGrid grid) {
		if (r == null || r.getDTO() == null) {
			return;
		}
		
		Kultur dto = r.getDTO();
		AsyncCallback<Kultur> callback = new AsyncCallback<Kultur>() {
			@Override
			public void onSuccess(Kultur result) {
				//reset the dto in list recode
				r.setDTO(result);
				grid.redraw();
			}
			@Override
			public void onFailure(Throwable caught) {
				handleException(caught);
			}
		};
		//perform the save operation
		service.save(dto, callback);
	}

	/**
	 * Loads the Sorte-Stammdaten entries.
	 * @param selectedKultur 
	 * @param grid
	 */
	public void loadSorteStammdatenRecords(Kultur kultur, final SorteGrid grid) {
		if (kultur != null) {
			//load BodenbearbeitungTypen via service
			service.loadSorteData(kultur.getKey(), new AsyncCallback<List<Sorte>>() {
				@Override
				public void onSuccess(List<Sorte> data) {
					//create record list for the grid view
					RecordList result = new RecordList();
					
					for (Sorte t : data) {
						SorteRecord r = new SorteRecord(t);
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

	/**
	 * Saves the new Sorte-Stammdaten entrie for the specified Sorte.
	 * @param r the new data to save
	 * @param kultur the Kultur item 
	 * @param grid the grid
	 */
	public void save(final SorteRecord r, final Kultur kultur, final SorteGrid grid) {
		if (r == null || r.getDTO() == null) {
			return;
		}
		
		Sorte dto = r.getDTO();
		AsyncCallback<Sorte> callback = new AsyncCallback<Sorte>() {
			@Override
			public void onSuccess(Sorte result) {
				//reset the dto in list recode
				r.setDTO(result);
				grid.addData(r);
			}
			@Override
			public void onFailure(Throwable caught) {
				handleException(caught);
			}
		};
		//perform the save operation
		service.save(dto, kultur, callback);
	}

	/**
	 * Updates an existing Sorte-Stammdaten item.
	 * @param r the data to update
	 * @param kultur the Kultur item
	 * @param grid the grid
	 */
	public void update(final SorteRecord r, final Kultur kultur, final SorteGrid grid) {
		if (r == null || r.getDTO() == null) {
			return;
		}
		
		Sorte dto = r.getDTO();
		AsyncCallback<Sorte> callback = new AsyncCallback<Sorte>() {
			@Override
			public void onSuccess(Sorte result) {
				//reset the dto in list recode
				r.setDTO(result);
				grid.redraw();
			}
			@Override
			public void onFailure(Throwable caught) {
				handleException(caught);
			}
		};
		//perform the save operation
		service.save(dto, kultur, callback);
	}

	/**
	 * Deletes the Sorte-Stammdaten item. 
	 * @param records the item to delete
	 * @param grid the grid
	 */
	public void delete(List<SorteRecord> records, final SorteGrid grid) {
		if (records == null || records.isEmpty()) {
			return;
		}
		
		for (final SorteRecord r : records) {
			if (r != null && r.getDTO() != null){
				Sorte dto = r.getDTO();
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
				//delete the entry
				service.delete(dto, callback);
			}
		}
	}
}