package com.cbapps.javafx.idbike.model.param;

import javafx.beans.property.BooleanProperty;
import javafx.beans.property.IntegerProperty;
import javafx.beans.property.ListProperty;
import javafx.beans.property.SimpleBooleanProperty;
import javafx.beans.property.SimpleIntegerProperty;
import javafx.beans.property.SimpleListProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;
import javafx.beans.value.ObservableValue;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

/**
 * @author PC1
 *
 */
public abstract class Param {

	private final StringProperty name;
	private final ListProperty<Integer> values;
	private final BooleanProperty activ;
	
	public static final BooleanProperty ALWAYS_FALSE = 
			new SimpleBooleanProperty(false);
	
	public static final BooleanProperty ALWAYS_TRUE = 
			new SimpleBooleanProperty(true);
	
	public Param () {
		this("");
	}
	
	public Param (String n, int... v) {
		name = new SimpleStringProperty(n);
		values = new SimpleListProperty<>(
				FXCollections.observableArrayList());
		for (int i : v) values.add(i);
		activ = new SimpleBooleanProperty(true);
	}

	public static String convertTime(int t) {
		int h = (int) Math.floor((t/60));
		int hm = h*60;
		int m = t-hm;
		return h + ":" + String.format("%02d", m);
	}
	
	public BooleanProperty activProperty() {
		return activ;
	}
	
	public Param depend(ObservableValue<? extends Boolean> other) {
		activ.bind(other);
		return this;
	}
	
	public String getName() {
		return name.get();
	}
	
	public String getStringValue() {
		if (values.size() > 0) return values.get(0).toString();
		return "";
	}
	
	public int getIntValue() {
		return getIntValue(0);
	}
	
	public int getIntValue(int position) {
		if (values.size() > position) 
			return values.get(position).intValue();
		return 0;
	}
	
	public int[] getIntValues() {
		int[] ints = new int[values.size()];
		for (int i = 0;i < values.size();i++)
			ints[i] = values.get(i).intValue();
		return ints;
	}
	
	public abstract boolean isValidStringValue(String v);
	
	public abstract boolean isValidIntValue(int v);
	
	public StringProperty nameProperty() {
		return name;
	}
	
	public ListProperty<Integer> getValues() {
		return values;
	}
	
	public boolean setName(String n) {
		name.set(n);
		return true;
	}
	
	public boolean setStringValue(String v) {
		return setStringValue(v, 0);
	}
	
	public boolean setStringValue(String v, int position) {
		if (values.size() > position) 
			//values.set(position, 
			//		new SimpleIntegerProperty(Integer.valueOf(v)));
			values.set(position, Integer.valueOf(v));
		else values.add(Integer.valueOf(v));
		return true;
	}
	
	public boolean setIntValue(int value) {
		return setIntValue(value, 0);
	}
	
	public boolean setIntValue(int value, int position) {
		if (values.size() > position) 
			//values.set(position, new SimpleIntegerProperty(value));
			values.set(position, value);
		else values.add(value);
		return true;
	}
	
	public boolean setIntValues(int... value) {
		for (int i = 0;i < value.length;i++) {
			if (!setIntValue(value[i], i)) return false;
		}
		return true;
	}
	
	@Override
	public String toString() {
		StringBuilder sb = new StringBuilder();
		sb.append(getClass().getSimpleName());
		sb.append("{name=").append(name.get()).append(", values=[");
		for (Integer val : values) {
			sb.append(val.intValue()).append(", ");
		}
		sb.delete(sb.length()-2, sb.length());
		sb.append("]");
		return sb.toString();
	}
}
