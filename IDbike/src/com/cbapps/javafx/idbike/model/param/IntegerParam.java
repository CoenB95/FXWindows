package com.cbapps.javafx.idbike.model.param;

public class IntegerParam extends Param {

	private int min_value;
	private int max_value;
	private boolean min_max = false;
	
	public IntegerParam(String name) {
		super(name, 0);
	}
	
	public IntegerParam(String name, int... default_value) {
		super(name, default_value);
	}
	
	public IntegerParam disableLimits() {
		min_max = false;
		return this;
	}
	public IntegerParam enableLimits(int min, int max) {
		min_max = true;
		min_value = min;
		max_value = max;
		return this;
	}
	
	public boolean isValidIntValue(int value) {
		return min_max ? (value >= min_value && value <= max_value): 
			value <= Short.MAX_VALUE*2;
	}
	
	@Override
	public boolean isValidStringValue(String v) {
		int i;
		try {
			i = Integer.parseInt(v);
		} catch (NumberFormatException e) {
			return false;
		}
		return isValidIntValue(i);
	}
}