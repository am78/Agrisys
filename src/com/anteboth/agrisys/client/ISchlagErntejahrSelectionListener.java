package com.anteboth.agrisys.client;

import com.anteboth.agrisys.client.model.SchlagErntejahr;

public interface ISchlagErntejahrSelectionListener {
	
	/**
	 * Called when the {@link SchlagErntejahr} selection has been changed.
	 * @param se the new selection
	 */
	void onSchlagErntejahrSelectionChanged(SchlagErntejahr se);

}
