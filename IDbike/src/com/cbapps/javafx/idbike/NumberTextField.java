package com.cbapps.javafx.idbike;

import javafx.scene.control.TextField;

public class NumberTextField extends TextField {

	private int min_value;
	private int max_value;
	
	public NumberTextField() {
		textProperty().addListener((c, o, n) -> {
			if (valid(n)) 
				setText(n);
			else setText(o);
		});
	}
	
	/*@Override
	public void replaceSelection(String replacement) {
		if (valid(replacement))
			super.replaceSelection(replacement);
	}
	
	@Override
	public void replaceText(int start, int end, String text) {
		if (valid(text))
			super.replaceText(start, end, text);
	}*/
	
	public int getValue() {
		String t = getText();
		if (t.equals("")) return 0;
		else return Integer.parseInt(t);
	}
	
	public NumberTextField setAllowedValues(int min, int max) {
		min_value = min;
		max_value = max;
		return this;
	}
	
	public NumberTextField setMin(int min) {
		min_value = min;
		return this;
	}
	
	public NumberTextField setMax(int max) {
		max_value = max;
		return this;
	}
	
	public boolean valid(String text) {
		int i = 0;
		if (text.equals("")) return true;
		try {
			i = Integer.parseInt(text);
		} catch (NumberFormatException e) {
			return false;
		}
		return i >= min_value && i <= max_value;
	}
}
