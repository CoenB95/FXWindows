package com.cbapps.javafx.idbike.model.param;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

public class ParamManager {

	private ObservableList<Param> params;
	
	public static class Menu {
		public String name;
		public int[] items;
		
		public Menu(String n, Bike... bikes) {
			name = n;
			items = new int[bikes.length];
			for (int i = 0;i < bikes.length;i++) 
				items[i] = bikes[i].ordinal();
		}
	}
	
	public static final Menu GRAPH_MENU = new Menu("Parameters",
			Bike.AMP_HIGH_PLUS,
			Bike.AMP_HIGH,
			Bike.AMP_ID,
			Bike.AMP_LOW);
	public static final Menu MENU_0 = new Menu("Overig",
//			Bike.LOGO,
//			Bike.VERSION_MC,
//			Bike.VERSION_MCBC,
//			Bike.BATTERY_CAPACITY,
//			Bike.BACKLIGHT,
//			Bike.MOTOR_CONNECTION,
//			Bike.BACKLIGHT_LEVEL,
//			Bike.HEADLIGHT,
//			Bike.HEADLIGHT_ON,
//			Bike.HEADLIGHT_OFF,
//			Bike.HEADLIGHT_TIME,
			Bike.SWITCH_OFF_TIME,
			Bike.INFO_SELECTOR,
			Bike.BUTTON_REVERSE,
//			Bike.LCD_CONTRAST,
//			Bike.WHEEL_SIZE,
//			Bike.WHEEL_SIZE_CUST,
			Bike.PULSREV,
			Bike.MAX_CURRENT,
//			Bike.SINE_BLDC_DRIVE_MODE,
			Bike.PHASE_ZERO,
			Bike.PHASE_SHIFT_SPEED,
//			Bike.MOTOR_REDUCTION,
//			Bike.MOTOR_POLES,
//			Bike.MOTOR_TYPE,
			Bike.BIKE_TYPE,
			Bike.USER_PROGRAMS,
			Bike.SWITCH_OFF_VOLTAGE,
//			Bike.BATTERY_CELLS,
//			Bike.BATTERY_CHEM,
			Bike.SENSORPLATE,
			Bike.REVERSE_TMM,
			Bike.REVERSE_BRAKE,
			Bike.TRESHOLD_0,
			Bike.TRESHOLD_5,
			Bike.TRESHOLD_8,
//			Bike.AMP_LOW,
//			Bike.AMP_ID,
//			Bike.AMP_HIGH,
//			Bike.AMP_HIGH_PLUS,
			Bike.CONFIG_NAME,
			Bike.CONFIG_DATE,
			Bike.CUSTOMER,
			Bike.BIKE_TYPENAME,
			Bike.PID_P,
			Bike.PID_I,
			Bike.PID_D,
			Bike.SPEED_TIMEOUT,
			Bike.TEST_MODE,
			Bike.CRC
			);
	public static final Menu MENU_1 = new Menu("Verlichting",
			Bike.BACKLIGHT,
			Bike.BACKLIGHT_LEVEL,
			Bike.HEADLIGHT,
			Bike.HEADLIGHT_ON,
			Bike.HEADLIGHT_OFF,
			Bike.HEADLIGHT_TIME,
			Bike.LCD_CONTRAST);
	public static final Menu MENU_2 = new Menu("Logo", 
			Bike.LOGO);
	public static final Menu MENU_3 = new Menu("Versie", 
			Bike.VERSION_MC,
			Bike.VERSION_MCBC);
	public static final Menu MENU_4 = new Menu("Batterij", 
			Bike.BATTERY_CAPACITY,
			Bike.BATTERY_CELLS,
			Bike.BATTERY_CHEM);
	public static final Menu MENU_5 = new Menu("Wiel", 
			Bike.WHEEL_SIZE,
			Bike.WHEEL_SIZE_CUST);
	public static final Menu MENU_6 = new Menu("Motor",
			Bike.MOTOR_CONNECTION,
			Bike.MOTOR_POLES,
			Bike.MOTOR_REDUCTION,
			Bike.MOTOR_TYPE,
			Bike.SINE_BLDC_DRIVE_MODE);
	
	public static final Menu[] ALL_MENUS = {MENU_1, MENU_2, MENU_3,
			MENU_4, MENU_5, MENU_6, MENU_0};
	
	public ParamManager() {
		params = FXCollections.observableArrayList();
		for (Bike b:Bike.values()) {
			params.add(b.getParam());
		}
		// Only dependencies need to be bound here.
		params.get(Bike.VERSION_MC.ordinal()).depend(Param.ALWAYS_FALSE);
		params.get(Bike.VERSION_MCBC.ordinal()).depend(Param.ALWAYS_FALSE);
		params.get(Bike.WHEEL_SIZE_CUST.ordinal()).depend(params.get(
				Bike.WHEEL_SIZE.ordinal()).getValues().valueAt(0)
				.isEqualTo(2));
	}
	
	public void clearParams() {
		for (Param p : params) {
			p.getValues().clear();
		}
	}
	
	public static int[] convertFromBytes(int... bytes) {
		int[] result = new int[bytes.length/2];
		for (int step = 0;step*2 < bytes.length-1;step++) {
			result[step] = ((bytes[step*2+1] << 8)&0xFF00)|
					(bytes[step*2]&0x00FF);
		}
		return result;
	}
	
	public static int[] convertToBytes(int... value) {
		int[] result = new int[value.length*2];
		for (int step = 0;step*2 < value.length;step++) {
			result[step*2] = (value[step]&0x00FF);
			result[step*2+1] = (value[step] >> 8);
		}
		return result;
	}
	
	public Param getParam(int id) {
		if (id < params.size()) {
			return params.get(id);
		}
		return null;
	}
	
	public ObservableList<Param> getParams() {
		return params;
	}
	
	public boolean setParam(int id, int... values) {
		if (id < params.size()) {
			return setParam(params.get(id), values);
		}
		return false;
	}
	
	public boolean setParam(Param p, int...values) {
		boolean r = false;
		r = p.setIntValues(values);
		//System.out.println("Set param: "+(r?"(success)":"(failed)")
		//		+ "\n"+p.toString()+
		//		"\nreturned bytes: " + Arrays.toString(
		//				convertToBytes(p.getIntValues())));
		return r;
	}
	
	public boolean setParam(Param p, String value) {
		boolean r = false;
		r = p.setStringValue(value);
		//System.out.println("Set param: "+(r?"(success)":"(failed)")
		//		+ "\n"+p.toString()+
		//		"\nreturned bytes: " + Arrays.toString(
		//				convertToBytes(p.getIntValues())));
		return r;
	}
	
	public enum Bike {
		LOGO("Logo", "Off" , "IDbike", "OXF",
				"TREK", "GIANT", "RAP", "QWIC", "STELLA", "FLEVO", 
				"EBIKEZ", "TDCM", "IRIDING", "SPIKED"),
		UNIT("Units", "Metric", "Imoerial"),
		VERSION_MC(),
		VERSION_MCBC(),
		BATTERY_CAPACITY(),
		BACKLIGHT(),
		MOTOR_CONNECTION("Motor wiring", "Forw A", "Forw B", "Forw C", 
				"Forw D", "Forw E", "Forw F", "Rev A", "Rev B", "Rev C",
				"Rev D", "Rev E", "Rev F"),
		BACKLIGHT_LEVEL(),
		HEADLIGHT("Headlight", "Auto", "Manual", "Auto/Manual", "Manual/Auto"),
		HEADLIGHT_ON(),
		HEADLIGHT_OFF(),
		HEADLIGHT_TIME(),
		SWITCH_OFF_TIME(),
		INFO_SELECTOR(),
		BUTTON_REVERSE(),
		LCD_CONTRAST(),
		WHEEL_SIZE("Wheelsize", "26 inch", "28 inch", "Custom"),
		WHEEL_SIZE_CUST(),
		PULSREV(),
		MAX_CURRENT(),
		SINE_BLDC_DRIVE_MODE("Drive mode", "Sinus", "BLDC", "Auto"),
		PHASE_ZERO(),
		PHASE_SHIFT_SPEED(),
		MOTOR_REDUCTION(),
		MOTOR_POLES(),
		MOTOR_TYPE("Motor type", "Direct Drive", "Geared Drive", "Geared Drive"),
		BIKE_TYPE(),
		USER_PROGRAMS(),
		SWITCH_OFF_VOLTAGE(),
		BATTERY_CELLS(),
		BATTERY_CHEM("Battery chemistry", "NiMH", "Li-Ion", "LiFePo", "Pb"),
		SENSORPLATE(),
		REVERSE_TMM(),
		REVERSE_BRAKE(),
		TRESHOLD_0(),
		TRESHOLD_5(),
		TRESHOLD_8(),
		AMP_LOW(Type.GRAPH, "AMP_LOW"),
		AMP_ID(Type.GRAPH, "AMP_ID"),
		AMP_HIGH(Type.GRAPH, "AMP_HIGH"),
		AMP_HIGH_PLUS(Type.GRAPH, "AMP_HIGH_PLUS"),
		CONFIG_NAME(Type.STRING, "Config name"),
		CONFIG_DATE(Type.STRING, "Config date"),
		CUSTOMER(Type.STRING, "Customer"),
		BIKE_TYPENAME(Type.STRING, "Bike typename"),
		PID_P(),
		PID_I(),
		PID_D(),
		SPEED_TIMEOUT(),
		TEST_MODE(),
		CRC();
		
		public String name;
		private Param param;
		
		public enum Type {
			INT, STRING, GRAPH
		}
		
		/**IntegerParam*/
		private Bike() {
			name = toString();
			param = new IntegerParam(name, 0);
		}
		/**IntegerParam*/
		private Bike(String n) {
			this(Type.INT, n, 0);
		}
		/**IntegerParam*/
		private Bike(String n, int...val) {
			this(Type.INT, n, val);
		}
		/**ChoiceParam*/
		private Bike(String n, String...val) {
			name = n;
			param = new ChoiceParam(n, 0, val);
		}
		/**IntegerParam or StringParam*/
		private Bike(Type type, String n) {
			this(type, n, 0);
		}
		/**IntegerParam or StringParam*/
		private Bike(Type type, String n, int...val) {
			name = n;
			switch (type) {
			case INT: param = new IntegerParam(name, val); break;
			case STRING: param = new StringParam(name, val); break;
			case GRAPH: param = new IntegerParam(name, 0,0,0,0,0,0,0,0);
			break;
			}
		}
		
		public Param getParam() {
			return param;
		}
	}
}
