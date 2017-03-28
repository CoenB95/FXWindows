package fxwindows.animation;


import fxwindows.core.Updatable;
import javafx.animation.Interpolator;

import java.time.Duration;

public abstract class Animation extends Updatable {

	private long startTime;
	private Duration duration;
	private boolean started;
	private boolean afterEnd;
	private boolean paused;
	private long pauseTime = -1;
	private Interpolator interpolator = Interpolator.EASE_BOTH;

	public Animation(Duration duration) {
		setDuration(duration);
		// Prevent auto-start of Updatable.
		unregister();
	}
	
	public boolean hasFinished() {
		return isUnregistered();
	}
	public final void update(long time) {
		if (paused) {
			if (pauseTime < 0) pauseTime = time;
			return;
		} else if (pauseTime > 0) {
			startTime += (time - pauseTime);
			pauseTime = -1;
		}
		if (hasFinished()) return;
		if (!started && time >= startTime) {
			started = true;
		}
		if (started) {
			if (time >= startTime + duration.toMillis()) {
				update(1.0);
				if (!afterEnd) unregister();
			} else {
				update(interpolator.interpolate(0.0, 1.0, (time - startTime) /
						(double) duration.toMillis()));
			}
			
		}
	}

	public void pause(boolean value) {
		paused = value;
	}

	public final void start() {
		startAt(0);
	}

	public final void startAndStick() {
		startAt(0);
		afterEnd = true;
	}

	public final void startAt(long milisDelay) {
		started = false;
		if (isUnregistered()) register();
		startTime = System.nanoTime()/1000000 + milisDelay;
		afterEnd = false;
		update(startTime - milisDelay);
	}

	public final void stop() {
		afterEnd = false;
		unregister();
	}

	public Animation then(Runnable r) {
		unregisteredProperty().addListener((v1, v2, v3) -> {
			if (started && v3) r.run();
		});
		return this;
	}

	public Animation then(Animation other) {
		return then(other, 0);
	}

	public Animation then(Animation other, long millisDelay) {
		unregisteredProperty().addListener((v1, v2, v3) -> {
			if (v3) other.startAt(millisDelay);
		});
		return this;
	}

	public final void setDuration(Duration duration) {
		this.duration = duration;
	}

	public Animation setInterpolator(Interpolator value) {
		interpolator = value;
		return this;
	}

	public final Duration getDuration() {
		return this.duration;
	}

	public abstract void update(double progress);
}