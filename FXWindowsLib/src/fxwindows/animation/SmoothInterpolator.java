package fxwindows.animation;

import javafx.animation.Interpolator;

public class SmoothInterpolator extends Interpolator {

	private AnimType type;
	
	public enum AnimType {ACCELERATE, DECELERATE, ACCELDECEL};

	public SmoothInterpolator(AnimType t) {
		type = t;
	}

	private double accel(double prog, double f) {
		return Math.pow(prog/f*4, 2)/16*f;
	}

	private double decel(double prog, double f) {
		return 1-(Math.pow((prog-1)/f*4, 2)/16*f);
	}

	@Override
	protected double curve(double progress) {
		switch(type) {
		case ACCELERATE:
			return accel(progress, 1);
		case DECELERATE:
			return decel(progress, 1);
		case ACCELDECEL:
			if (progress < 0.5)
				return accel(progress, 0.5f);
			else if (progress >= 0.5)
				return decel(progress, 0.5f);
			else return progress;
		default: return progress;
		}
	}

}
