package fxwindows.core;

import java.util.List;
import fxwindows.animation.Animation;

/**
 * Interface for objects that can be animated.
 * @author CoenB95
 *
 */
public interface Animatable {

	List<Animation> getAnimations();
	void update(long time);
	void addAnimation(Animation anim);
}
