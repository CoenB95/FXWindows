package com.cbapps.javafx.idbike;

import javafx.animation.Interpolator;
import javafx.animation.Transition;
import javafx.scene.shape.Arc;
import javafx.util.Duration;

public class ArcTransition extends Transition {

	private Arc arc;
	private boolean reversed = false;
	private boolean was_reversed = false;
	private int offset = 0;

	public ArcTransition(Duration d, Arc a) {
		setCycleDuration(d);
		setInterpolator(Interpolator.LINEAR);
		arc = a;
	}

	@Override
	protected void interpolate(double frac) {
		reversed = getCurrentRate() < 0;
		if (reversed) {
			if (!was_reversed) {
				was_reversed = true;
				offset += 270;
				if (offset > 360) offset -= 360;
				arc.setStartAngle(offset);
			}
			arc.setLength(-20-(frac*250));
		} else {
			if (was_reversed) {
				was_reversed = false;
				offset -= 20;
				if (offset > 360) offset -= 360;
				arc.setStartAngle(offset);
			}
			arc.setLength(20+frac*250);
		}
	}
}