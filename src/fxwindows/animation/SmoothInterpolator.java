package fxwindows.animation;

import javafx.animation.Interpolator;

public class SmoothInterpolator extends Interpolator {

	private AnimType type;
	
	public enum AnimType {ACCELERATE, DECELERATE, ACCELDECEL};

	public SmoothInterpolator(AnimType t) {
		type = t;
	}

	private double accel(double prog) {
		return (1.0 - Math.cos(prog*Math.PI/2));
		//return Math.pow(prog/f*4, 2)/16*f;
	}

	private double decel(double prog) {
		return Math.sin(prog*Math.PI/2);
		//return 1-(Math.pow((prog-1)/f*4, 2)/16*f);
	}

	@Override
	protected double curve(double progress) {
		switch(type) {
		case ACCELERATE:
			return accel(progress);
		case DECELERATE:
			return decel(progress);
		case ACCELDECEL:
			//if (progress < 0.5)
				return accel(progress * 2) / 2;
			//else if (progress >= 0.5)
			//	return decel(progress, 0.5f);
			//else return progress;
		default: return progress;
		}
	}

}
