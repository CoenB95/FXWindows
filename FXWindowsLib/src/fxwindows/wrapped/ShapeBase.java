package fxwindows.wrapped;

import java.util.ArrayList;
import java.util.List;
import java.util.ListIterator;

import fxwindows.animation.Animation;
import fxwindows.core.Animatable;
import fxwindows.core.ColoredPositionable;
import javafx.scene.Node;
import javafx.scene.layout.Pane;

public abstract class ShapeBase extends ColoredPositionable
		implements Animatable {

	private List<Animation> animations;
	
	public ShapeBase() {
		animations = new ArrayList<>();
	}
	
	public abstract void addToPane(Pane p);
	public abstract void clip(Node n);
	public abstract void removeFromPane(Pane p);
	
	@Override
		public List<Animation> getAnimations() {
			return animations;
		}
	
	@Override
	public void update(long time) {
		ListIterator<Animation> it = getAnimations().listIterator(0);
		while (it.hasNext()) {
			Animation a = it.next();
			a.update(time);
			if (a.hasEnded()) it.remove();
		}
	}
	
	@Override
	public void addAnimation(Animation anim) {
		getAnimations().add(anim);
	}
}
