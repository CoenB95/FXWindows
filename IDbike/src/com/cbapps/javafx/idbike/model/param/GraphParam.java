package com.cbapps.javafx.idbike.model.param;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

public class GraphParam extends Param {

	private ObservableList<Integer> points;
	
	public GraphParam(String name, int... items) {
		super(name);
		points = FXCollections.observableArrayList();
		for (int i : items) points.add(Integer.valueOf(i));
	}
	
	public ObservableList<Integer> getPoints() {
		return points;
	}
	
	@Deprecated
	@Override
	public boolean isValidStringValue(String v) {
		return false;
	}

	@Deprecated
	@Override
	public boolean isValidIntValue(int v) {
		return false;
	}
}
