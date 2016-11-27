package fxwindows.resources;

import java.io.InputStream;

/**Util class for easy using Roboto Fonts.
 * Example usage:<p>
 * {@code Font.loadFont(RobotoFont.regular(), 14);}*/
public class RobotoFont {

	public static final InputStream bold() { 
		return RobotoFont.class.getResourceAsStream("Roboto-Bold.ttf");
	}

	
	public static final InputStream medium() {
		return RobotoFont.class.getResourceAsStream("Roboto-Medium.ttf");
	}
	
	public static final InputStream regular() { 
		return RobotoFont.class.getResourceAsStream("Roboto-Regular.ttf");
	}
	
	public static final InputStream thin() {
		return RobotoFont.class.getResourceAsStream("Roboto-Thin.ttf");
	}
}
