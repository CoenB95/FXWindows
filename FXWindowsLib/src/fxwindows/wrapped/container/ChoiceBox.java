package fxwindows.wrapped.container;

import java.time.Duration;

import fxwindows.animation.SmoothInterpolator;
import fxwindows.animation.SmoothInterpolator.AnimType;
import fxwindows.animation.ValueAnimation;
import fxwindows.core.ShapeBase;
import javafx.beans.property.DoubleProperty;
import javafx.beans.property.ObjectProperty;
import javafx.beans.property.SimpleDoubleProperty;
import javafx.beans.property.SimpleObjectProperty;
import javafx.collections.ListChangeListener.Change;

public class ChoiceBox extends ListContainer {
	
	private ShapeBase itemSelected;
	private int itemPosition;
	private boolean relayout = true;
	private boolean itemUpdate = true;
	private boolean expanding = true;
	//private ValueAnimation scrollAnim;
	private ValueAnimation heightAnim;
	private SmoothInterpolator interpolator;
	private Duration duration;
	
	private final DoubleProperty toScroll = new SimpleDoubleProperty();
	private final DoubleProperty toHeight = new SimpleDoubleProperty();
	private final ObjectProperty<OnItemSelectedListener> onItemSelected =
			new SimpleObjectProperty<>();
	
	public ChoiceBox() {
		super();
		selectionAllowedProperty().bind(scrollBlockedProperty().not());
		interpolator = new SmoothInterpolator(AnimType.DECELERATE);
		duration = Duration.ofMillis(400);
		clipChildren(true);
		// When the amount of children changes, a vertical re-layout is needed.
		getChildren().addListener((Change<? extends ShapeBase> c) -> {
			while (c.next()) {
				for (ShapeBase w : c.getAddedSubList()) {
					w.heightProperty().addListener((v1,v2,v3) -> relayout = true);
					w.setOnMouseClicked((e) -> {
						itemSelected = w;
						itemUpdate = true;
						expanding = !expanding;
						if (!expanding && getOnItemSelected() != null)
							getOnItemSelected().onItemSelected(w);
					});
					itemUpdate = true;
					//Items got added, so the position of the selected item
					//is invalidated.
					expanding = true;
				}
				if (!c.getRemoved().isEmpty()) {
					itemUpdate = true;
					//Items got removed, so the position of the selected item
					//is invalidated.
					expanding = true;
				}
			}
		});
	}
	
	@Override
	public void update() {
		super.update();
		if (!expanding && itemPosition >= getChildren().size())
			itemPosition = getChildren().size()-1;
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
				toScroll.set(getScrollY());
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
			setScrollY(toScroll.get());
		}
		if (itemUpdate) {
			itemUpdate = false;
			blockScroll(!expanding);
			double fromHeight;
			fromHeight = getHeight();
			
			heightAnim = new ValueAnimation(duration).setFrom(fromHeight).setTo(toHeight);
			heightAnim.setInterpolator(interpolator);
			heightAnim.start();
		}
		if (heightAnim != null && !heightAnim.hasFinished())
			setMaxHeight(heightAnim.getValue() + getPaddingY()*2);
		else if (!expanding) setMaxHeight(toHeight.get() + getPaddingY()*2);
	}

	public ObjectProperty<OnItemSelectedListener> onItemSelectedProperty() {
        return onItemSelected;
    }

    public OnItemSelectedListener getOnItemSelected() {
	    return onItemSelectedProperty().get();
    }

	public void setOnItemSelected(OnItemSelectedListener l) {
	    onItemSelectedProperty().set(l);
    }

	public void setAnimation(SmoothInterpolator smooth, Duration duration) {
		interpolator = smooth;
		this.duration = duration;
	}
	
	public interface OnItemSelectedListener {
		void onItemSelected(ShapeBase item);
	}
}
