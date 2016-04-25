package com.cbapps.javafx.idbike.model.param;

public class StringParam extends Param {

	public StringParam() {
		super();
	}
	
	public StringParam(String n) {
		super(n);
	}
	
	public StringParam(String n, int...v) {
		super(n, v);
	}
	
	@Override
	public boolean isValidStringValue(String v) {
		return v.length() <= 32;
	}

	@Override
	public boolean isValidIntValue(int v) {
		return v <= Character.MAX_VALUE;
	}
	
	@Override
	public String getStringValue() {
		String g = "";
		for (Integer p:getValues()) {
			int[] chs = ParamManager.convertToBytes(p.intValue());
			if (chs[0] != 255) g += (char) chs[0];//p.byteValue();
			if (chs[1] != 255) g += (char) chs[1];
		}
		return g;
	}
	
	@Override
	public boolean setStringValue(String v, int position) {
		if (isValidStringValue(v)) {
			char[] chars = v.toCharArray();
			boolean end = false;
			for (int x = 0;x < 16;x++) {
				int b1;
				int b2;
				if (x*2 < chars.length) b1 = chars[x*2];
				else if (!end) {
					end = true;
					b1 = 0;
				} else if (x*2 == 15) b1 = 0;
				else b1 = 255;
				
				if (x*2+1 < chars.length) b2 = chars[x*2+1];
				else if (!end) {
					end = true;
					b2 = 0;
				} else if (x*2+1 == 15) b2 = 0;
				else b2 = 255;
				
				if (!super.setIntValue(ParamManager.convertFromBytes(b1, b2)
						[0],x))
						return false;
				
//				if (!super.setIntValue(ParamManager.convertFromBytes(
//						(x*2 < chars.length ? chars[x*2] : 0),
//						(x*2+1 < chars.length ? chars[x*2+1] : 0))[0], x)) 
//					return false;
			}
		}
		return true;
	}
}
