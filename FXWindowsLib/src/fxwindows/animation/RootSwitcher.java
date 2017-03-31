package fxwindows.animation;

import fxwindows.core.Manager;
import javafx.animation.SequentialTransition;

import java.time.Duration;
import java.util.List;

/**
 * @author Coen Boelhouwers
 */
public class RootSwitcher extends Animation {

	private Manager manager;
	private List<Frame> frames;
	private int currentFrame = -1;

	public RootSwitcher(Manager manager) {
		super(Duration.ofMillis(0));
		this.manager = manager;
	}

	public RootSwitcher add(Manager.RootContainer container, Duration duration) {
		frames.add(new Frame(container, getDuration().toMillis()));
		setDuration(getDuration().plus(duration));
		return this;
	}


	@Override
	public void update(double progress) {
		if (currentFrame < 0) {
			currentFrame = 0;
			//frames.get(0).
		}
	}

	private static class Frame {
		Manager.RootContainer container;
		long millis;

		Frame(Manager.RootContainer container, long millis) {
			this.container = container;
			this.millis = millis;
		}
	}
}
