package com.sam.textsearch.ui;

import java.awt.Color;
import java.awt.Component;

import javax.swing.JTable;
import javax.swing.SwingConstants;
import javax.swing.table.DefaultTableCellRenderer;

import com.sam.textsearch.utils.Constants;

public class ResultTableRenderer extends DefaultTableCellRenderer {

	private static final long serialVersionUID = -5704955149766071266L;

	@Override
	public Component getTableCellRendererComponent(JTable t, Object v, boolean s,
			boolean f, int r, int c) {
		Component comp = super.getTableCellRendererComponent(t, v, s, f, r, c);
		if (c == 0 || c == 2) {
			this.setHorizontalAlignment(SwingConstants.CENTER);
			this.setBackground(Color.WHITE);
		} else {
			this.setHorizontalAlignment(SwingConstants.LEFT);
			String val = (String) v;
			if (val.startsWith(Constants.ERROR_PREFIX)) {
				this.setBackground(Color.RED);
			}
		}
		return comp;
	}

}
