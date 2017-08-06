package fxwindows.animation;


import fxwindows.core.Colorable;
import javafx.scene.paint.Color;

import java.time.Duration;

public class ColorAnimation extends Animation {

	private boolean background;
	private Colorable animatedColorable;
	private Color startColor;
	private Color endColor;

	private ColorAnimation(Colorable drawable, Duration duration) {
		super(duration);
		animatedColorable = drawable;
	}

	public static ColorAnimation backgroundColor(Colorable drawable, Duration duration) {
		ColorAnimation anim = new ColorAnimation(drawable, duration);
		anim.background = true;
		return anim;
	}

	public static ColorAnimation borderColor(Colorable drawable, Duration duration) {
		ColorAnimation anim = new ColorAnimation(drawable, duration);
		anim.background = false;
		return anim;
	}

	public ColorAnimation setFromColor(Color value) {
		this.startColor = value;
		return this;
	}

	public ColorAnimation setToColor(Color value) {
		this.endColor = value;
		return this;
	}

	@Override
	public ColorAnimation then(Animation other) {
		return then(other, 0);
	}

	@Override
	public ColorAnimation then(Animation other, long millisDelay) {
		return (ColorAnimation) super.then(other, millisDelay);
	}

	@Override
	public void update(double progress) {
		if (background) animatedColorable.setBackgroundColor(startColor.interpolate(endColor, progress));
		else animatedColorable.setBorderColor(startColor.interpolate(endColor, progress));
	}
}