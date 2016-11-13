package fxwindows;

import java.util.ArrayList;
import java.util.List;
import java.util.ListIterator;

import fxwindows.animation.Animation;

public abstract class Animatable extends Positionable {

	private List<Animation> animations;
	
	public Animatable() {
		animations = new ArrayList<>();
	}
	
	public List<Animation> getAnimations() {
		return animations;
	}
	
	public void update(long time) {
		ListIterator<Animation> it = getAnimations().listIterator(0);
		while (it.hasNext()) {
			Animation a = it.next();
			a.update(time);
			if (a.hasEnded()) it.remove();
		}
	}
	
	public void addAnimation(Animation anim) {
		getAnimations().add(anim);
	}
}
