package fxwindows.animation;


import fxwindows.core.Updatable;
import javafx.animation.Interpolator;

import java.time.Duration;

public abstract class Animation extends Updatable {

	private long startTime;
	private Duration duration;
	private boolean started;
	private Interpolator interpolator = Interpolator.EASE_BOTH;

	public Animation(Duration duration) {
		setDuration(duration);
	}
	
	public boolean hasFinished() {
		return isUnregistered();
	}
	public final void update(long time) {
		if (hasFinished()) return;
		if (!started && time >= startTime) {
			started = true;
		}
		if (started) {
			update(interpolator.interpolate(0.0, 1.0, (time - startTime) /
					(double) duration.toMillis()));
			if (time >= startTime + duration.toMillis()) {
				unregister();
			}
		}
	}
	public final void start() {
		started = false;
		if (isUnregistered()) register();
		startTime = System.nanoTime()/1000000;
	}
	public final void startAt(long milisDelay) {
		start();
		startTime = System.nanoTime()/1000000 + milisDelay;
	}
	public final void stop() {
		unregister();
	}

	public final void setDuration(Duration duration) {
		this.duration = duration;
	}
	public final void setInterpolator(Interpolator value) {
		interpolator = value;
	}
	public final Duration getDuration() {
		return this.duration;
	}

	public abstract void update(double progress);
}