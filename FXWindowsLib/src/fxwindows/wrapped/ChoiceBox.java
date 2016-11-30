package fxwindows.wrapped;

import java.time.Duration;

import fxwindows.animation.SmoothInterpolator;
import fxwindows.animation.SmoothInterpolator.AnimType;
import fxwindows.animation.ValueAnimation;
import javafx.collections.ListChangeListener.Change;

public class ChoiceBox extends VerticalContainer {
	
	private ShapeBase itemSelected;
	private boolean itemUpdate = true;
	private boolean expanding = true;
	private ValueAnimation scrollAnim;
	private ValueAnimation heightAnim;
	
	public ChoiceBox() {
		super();
		// When the amount of children changes, a vertical re-layout is needed.
		getChildren().addListener((Change<? extends ShapeBase> c) -> {
			while (c.next()) {
				for (ShapeBase w : c.getAddedSubList()) {
					w.setOnMouseClicked(() -> {
						itemSelected = w;
						itemUpdate = true;
						expanding = !expanding;
					});
				}
				for (ShapeBase w : c.getRemoved()) {
					w.setOnMouseClicked(null);
				}
			}
		});
	}
	
	@Override
	public void update(long time) {
		super.update(time);
		if (itemUpdate) {
			itemUpdate = false;
			blockScroll(!expanding);
			double fromScroll = 0;
			double fromHeight = 0;
			double toScroll = 0;
			double toHeight = 0;
			int position = getChildren().indexOf(itemSelected);
			fromScroll = getScroll();
			if (!expanding) {
				for (int i = 0;i < position;i++) {
					toScroll -= getChildren().get(i).getHeight();
				}
			} else {
				// Prevent going back to the top on re-expand.
				toScroll = fromScroll;
			}
			fromHeight = getHeight();
			if (expanding) toHeight = Math.min(getContentHeight(), getMaxHeight());
			else toHeight = getChildren().get(position).getHeight();
			//for (int i = position;i < getChildren().size()-1;i++) {
			//	toHeight += getChildren().get(i).getHeight();
			//}
			
			if (expanding) {
				if (getContentHeight() > getMaxHeight()
						&& toScroll < -(getContentHeight() - getMaxHeight())) {
					toScroll = -(getContentHeight() - getMaxHeight());
				} else {
					toScroll = 0;
				}
			}
			
			scrollAnim = new ValueAnimation(this,
					Duration.ofMillis(400)).setFrom(fromScroll).setTo(toScroll);
			heightAnim = new ValueAnimation(this, 
					Duration.ofMillis(400)).setFrom(fromHeight).setTo(toHeight);
			scrollAnim.setInterpolator(new SmoothInterpolator(AnimType.DECELERATE));
			heightAnim.setInterpolator(new SmoothInterpolator(AnimType.DECELERATE));
			scrollAnim.start();
			heightAnim.start();
			unbindHeight();
			bindHeight(heightAnim.valueProperty());
			System.out.println("Changing scroll: from " + fromScroll + ", to "
					+ toScroll + "; height from " + fromHeight + ", to " + 
					toHeight);
		}
		if (scrollAnim != null && (!scrollAnim.hasEnded() || !expanding))
			setScroll(scrollAnim.getValue());
		//super.update(time);
	}
}
