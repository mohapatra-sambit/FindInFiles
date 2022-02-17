package com.sam.textsearch.model;

import java.util.ArrayList;
import java.util.List;

import javax.swing.AbstractListModel;
import javax.swing.ComboBoxModel;

import com.sam.textsearch.init.Loader;

public class SavedSearchModel extends AbstractListModel implements
		ComboBoxModel {
	
	private static final long serialVersionUID = 8438329476245671159L;

	private static List<String> searchNames;
	
	private static String selectedItem;
	
	private static SavedSearchModel model;
	
	private SavedSearchModel() {
		searchNames = new ArrayList<String>(Loader.getAllSavedSearchNames());
	}
	
	public static SavedSearchModel getInstance() {
		if (model == null) {
			model = new SavedSearchModel();
		}
		return model;
	}

	@Override
	public int getSize() {
		return searchNames.size();
	}

	@Override
	public Object getElementAt(int index) {
		return searchNames.get(index);
	}

	@Override
	public void setSelectedItem(Object item) {
		selectedItem = (String) item;
	}

	@Override
	public Object getSelectedItem() {
		return selectedItem;
	}
	
	public void addItem(String item) {
		searchNames.add(item);
		refresh();
	}
	
	public void refresh() {
		fireIntervalAdded(this, 0, searchNames.size());
	}

}
