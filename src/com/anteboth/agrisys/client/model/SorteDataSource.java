/**
 * 
 */
package com.anteboth.agrisys.client.model;

import java.util.LinkedHashMap;
import java.util.List;

import com.anteboth.agrisys.client.Agrisys;
import com.anteboth.agrisys.client.model.stammdaten.Kultur;
import com.anteboth.agrisys.client.model.stammdaten.Sorte;
import com.google.gwt.core.client.GWT;
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.googlecode.objectify.Key;
import com.smartgwt.client.widgets.form.fields.SelectItem;

/**
 * @author michael
 *
 */
public class SorteDataSource  {
	
	private List<Sorte> data;
	private SelectItem selectItem;
	private Key<Kultur> kultur;

	public SorteDataSource(SelectItem selectItem) {
		this.selectItem = selectItem;
	}

	private void loadData() {
		if (this.kultur == null) {
			return;
		}
		
		//clear existing slected value
		this.selectItem.setValue((String) null);
		
		//load new values
		Agrisys.getAgrisysservice().loadSorteData(this.kultur, new AsyncCallback<List<Sorte>>() {
			@Override
			public void onSuccess(List<Sorte> result) {
				setData(result);
			}

			@Override
			public void onFailure(Throwable caught) {
				caught.printStackTrace();
				GWT.getUncaughtExceptionHandler().onUncaughtException(caught);
			}
		});
	}

	protected void setData(List<Sorte> result) {
		this.data = result;
		
		LinkedHashMap<String, String> valueMap = new LinkedHashMap<String, String>();
		for (Sorte s : result) {
			valueMap.put(s.getId().toString(), s.getName());
		}
		selectItem.setValueMap(valueMap);
	}

	public void kulturValueChanged(Key<Kultur> kultur) {
		this.kultur = kultur;
		loadData();
	}
	
	public Sorte getSorteForId(String id) {
		if (id != null && this.data != null) {
			for (Sorte s : this.data) {
				if (s.getId().equals(Long.valueOf(id))) {
					return s;
				}
			}
			
		}
		return null;
	}

	public void setSelectedValue(long id) {
		this.selectItem.setValue(Long.valueOf(id).toString());
	}
}
