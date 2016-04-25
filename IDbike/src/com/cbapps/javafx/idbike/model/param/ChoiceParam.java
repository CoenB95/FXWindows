package com.cbapps.javafx.idbike.model.param;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

public class ChoiceParam extends Param {
	
	private ObservableList<String> choice_options;
	
	public ChoiceParam(String name) {
		super(name, 0);
	}
	
	public ChoiceParam(String name, int selected, String... options) {
		super(name);
		choice_options = FXCollections.observableArrayList(options);
		if (options != null) {
			if (options.length > 0) {
				if(options.length > selected) {
					super.setStringValue(choice_options.get(selected));
				} else {
					super.setStringValue(choice_options.get(0));
				}
			}
		}
	}
	
	/*@Override
	public int getIntValue() {
		String g = getStringValue();
		return choice_options.indexOf(g);
		//for (int x = 0;x < choice_options.length;x++) {
		//	if (choice_options[x].equals(g)) return x;
		//}
		//return 0;
	}*/
	
	public ObservableList<String> getOptions() {
		return choice_options;
	}
	
	@Override
	public boolean isValidIntValue(int value) {
		return value >= 0 && value < choice_options.size();//.length;
	}
	
	@Override
	public boolean isValidStringValue(String value) {
		return choice_options.contains(value);
	}
	
	@Override
	public boolean setStringValue(String v, int pos) {
		if (isValidStringValue(v)) {
			return super.setIntValue(choice_options.indexOf(v), pos);
		}
		return false;
	}
}