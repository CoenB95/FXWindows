package fxwindows.wrapped;

import java.time.Duration;

import fxwindows.animation.SmoothInterpolator;
import fxwindows.animation.SmoothInterpolator.AnimType;
import fxwindows.animation.ValueAnimation;
import javafx.beans.property.DoubleProperty;
import javafx.beans.property.SimpleDoubleProperty;
import javafx.beans.value.ChangeListener;
import javafx.collections.ListChangeListener.Change;

public class ChoiceBox extends VerticalContainer {
	
	private ShapeBase itemSelected;
	private int itemPosition;
	private boolean relayout = true;
	private boolean itemUpdate = true;
	private boolean expanding = true;
	private ValueAnimation scrollAnim;
	private ValueAnimation heightAnim;
	private SmoothInterpolator interpolator;
	private Duration duration;
	
	private final DoubleProperty toScroll = new SimpleDoubleProperty();
	private final DoubleProperty toHeight = new SimpleDoubleProperty();
	
	public ChoiceBox() {
		super();
		interpolator = new SmoothInterpolator(AnimType.DECELERATE);
		duration = Duration.ofMillis(400);
		// When the amount of children changes, a vertical re-layout is needed.
		getChildren().addListener((Change<? extends ShapeBase> c) -> {
			while (c.next()) {
				for (ShapeBase w : c.getAddedSubList()) {
					w.heightProperty().addListener((v1,v2,v3) -> relayout = true);
					w.addOnMouseClicked(() -> {
						itemSelected = w;
						itemUpdate = true;
						expanding = !expanding;
					});
				}
			}
		});
		ChangeListener<Number> lis = (v1,v2,v3) -> relayout = true;
		widthProperty().addListener(lis);
		heightProperty().addListener(lis);
	}
	
	@Override
	public void update(long time) {
		super.update(time);
		if (itemUpdate) itemPosition = getChildren().indexOf(itemSelected);
		if (itemUpdate || relayout) {
			relayout = false;
			if (!expanding) {
				double temp = 0;
				for (int i = 0;i < itemPosition;i++) {
					temp -= getChildren().get(i).getHeight();
				}
				toScroll.set(temp);
			} else {
				// Prevent going back to the top on re-expand.
				toScroll.set(getScroll());
			}
			if (expanding) toHeight.set(Math.min(getContentHeight(), getMaxHeight()));
			else toHeight.set(itemSelected.getHeight());
			if (expanding) {
				if (getContentHeight() > getMaxHeight()) {
					if (toScroll.get() < -(getContentHeight() - getMaxHeight())) {
						toScroll.set(-(getContentHeight() - getMaxHeight()));
					}
				} else toScroll.set(0);
			}
		}
		if (itemUpdate) {
			itemUpdate = false;
			blockScroll(!expanding);
			double fromScroll = 0;
			double fromHeight = 0;
			//double toScroll = 0;
			//double toHeight = 0;
//			int position = getChildren().indexOf(itemSelected);
			fromScroll = getScroll();
//			if (!expanding) {
//				for (int i = 0;i < position;i++) {
//					toScroll -= getChildren().get(i).getHeight();
//				}
//			} else {
//				// Prevent going back to the top on re-expand.
//				toScroll = fromScroll;
//			}
			fromHeight = getHeight();
//			if (expanding) toHeight = Math.min(getContentHeight(), getMaxHeight());
//			else toHeight = getChildren().get(position).getHeight();
			//for (int i = position;i < getChildren().size()-1;i++) {
			//	toHeight += getChildren().get(i).getHeight();
			//}
			
//			if (expanding) {
//				if (getContentHeight() > getMaxHeight()
//						&& toScroll < -(getContentHeight() - getMaxHeight())) {
//					toScroll = -(getContentHeight() - getMaxHeight());
//				} else {
//					toScroll = 0;
//				}
//			}
			
			scrollAnim = new ValueAnimation(this,
					duration).setFrom(fromScroll).setTo(toScroll);
			heightAnim = new ValueAnimation(this, 
					duration).setFrom(fromHeight).setTo(toHeight);
			scrollAnim.setInterpolator(interpolator);
			heightAnim.setInterpolator(interpolator);
			scrollAnim.start();
			heightAnim.start();
			unbindHeight();
//			bindHeight(heightAnim.valueProperty());
		}
		if (scrollAnim != null && !scrollAnim.hasFinished())
			setScroll(scrollAnim.getValue());
		else if (!expanding) setScroll(toScroll.get());
		if (heightAnim != null && !heightAnim.hasFinished())
			setHeight(heightAnim.getValue());
		else if (!expanding) setHeight(toHeight.get());
		//super.update(time);
	}
	
	public void setAnimation(SmoothInterpolator smooth, Duration duration) {
		interpolator = smooth;
		this.duration = duration;
	}
}
