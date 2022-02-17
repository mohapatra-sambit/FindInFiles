package com.sam.textsearch.model;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import javax.swing.table.AbstractTableModel;

import com.sam.textsearch.bean.DataBean;
import com.sam.textsearch.utils.Constants;

public class ResultTableModel extends AbstractTableModel {

	private static final long serialVersionUID = 6478947128273559806L;

	private static List<DataBean> dataSet;

	private static String[] cols;

	private static ResultTableModel model;

	private ResultTableModel() {
		dataSet = new ArrayList<DataBean>();
		cols = new String[] { Constants.RESULT_HEADER_SERIAL_NO,
				Constants.RESULT_HEADER_FILENAME,
				Constants.RESULT_HEADER_LINENO };
	}

	public static ResultTableModel getInstance() {
		if (model == null) {
			model = new ResultTableModel();
		}
		return model;
	}

	@Override
	public int getRowCount() {
		return dataSet.size();
	}

	@Override
	public int getColumnCount() {
		return cols.length;
	}

	@Override
	public String getColumnName(int index) {
		return cols[index];
	}

	@Override
	public Object getValueAt(int rowIndex, int columnIndex) {
		if (dataSet.isEmpty()) {
			return "";
		}
		Iterator<DataBean> iter = dataSet.iterator();
		for (int i = 0; iter.hasNext(); i++) {
			DataBean bean = iter.next();
			if (i == rowIndex) {
				switch (columnIndex) {
				case 0:
					return i+1;
				case 1:
					return bean.getFileName();
				case 2:
					return bean.getLineNumber();
				default:
					return "";
				}
			}
		}
		return "";
	}

	@Override
	public Class<?> getColumnClass(int c) {
		return getValueAt(0, c).getClass();
	}

	public void removeData(DataBean bean) {
		dataSet.remove(bean);
	}

	public void addData(DataBean bean) {
		dataSet.add(bean);
	}

	public void reset() {
		dataSet.removeAll(dataSet);
		refresh();
	}

	public void refresh() {
		fireTableDataChanged();
	}

}