package fxwindows.animation;


import java.time.Duration;

import fxwindows.core.Animatable;
import javafx.animation.Interpolator;

public abstract class Animation {
	private long startTime;
	private Duration duration;
	private boolean started;
	private boolean done;
	private Interpolator interpolator = Interpolator.EASE_BOTH;
	private Animatable parent;
	public Animation(Duration duration) {
		setDuration(duration);
	}
	public Animation(Animatable drawable, Duration duration) {
		setDuration(duration);
		setDrawable(drawable);
	}
	public boolean hasEnded() {
		return done;
	}
	public final void update(long time) {
		if (done) return;
		if (!started && time >= startTime) {
			started = true;
			init();
		}
		if (started) {
			update(interpolator.interpolate(0.0, 1.0, (time - startTime) /
					(double) duration.toMillis()));
			if (time >= startTime + duration.toMillis()) {
				done = true;
			}
		}
	}
	public final void start() {
		if (started && !done) return;
		started = false;
		done = false;
		startTime = System.nanoTime()/1000000;//currentTimeMillis();
	}
	public final void startAt(long milisDelay) {
		start();
		startTime = System.nanoTime()/1000000 + milisDelay;//currentTimeMillis() + milisDelay;
	}
	public final void stop() {
		done = true;
	}
	public final void setDrawable(Animatable drawable) {
		parent = drawable;
		parent.addAnimation(this);
	}
	public final Animatable getDrawable() {
		return parent;
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
	public void init() {}
	public abstract void update(double progress);
}