package fxwindows.wrapped;

import fxwindows.animation.ValueAnimation;
import javafx.beans.binding.Bindings;
import javafx.beans.binding.DoubleExpression;
import javafx.beans.property.BooleanProperty;
import javafx.beans.property.DoubleProperty;
import javafx.beans.property.SimpleBooleanProperty;
import javafx.beans.property.SimpleDoubleProperty;
import javafx.beans.value.ChangeListener;
import javafx.collections.ListChangeListener.Change;
import javafx.scene.layout.Pane;
import javafx.scene.shape.Rectangle;

/**
 * Base class for a vertical-list-like layout.
 * @author CoenB95
 */
public abstract class VerticalContainer extends Container {

	private Rectangle background;
	private Pane pane;
	private BooleanProperty scrollBlocked = new SimpleBooleanProperty();
	private final DoubleProperty scroll = new SimpleDoubleProperty();
	private final DoubleProperty listHeight = new SimpleDoubleProperty();
	private final DoubleProperty maxHeight = new SimpleDoubleProperty(250);
	private ValueAnimation userScrollAnim;

	/**Flag notifying one or more things have changed that require a
	 * horizontal re-layout.*/
	private boolean updateRequested = true;

	public BooleanProperty scrollBlockedProperty() {
		return scrollBlocked;
	}
	
	public boolean isScrollBlocked() { return scrollBlockedProperty().get(); }
	public void blockScroll(boolean value) { scrollBlockedProperty().set(value); }
	
	// Scroll of the list.
	public DoubleProperty scrollProperty() {
		return scroll;
	}

	public double getScroll() { return scrollProperty().get(); }
	public void setScroll(double value) { scrollProperty().set(value); }
	
	// 
	public DoubleProperty listHeightProperty() {
		return listHeight;
	}

	public double getListHeigth() { return listHeightProperty().get(); }
	public void setListHeigth(double value) { listHeightProperty().set(value); }
	
	// 
	public DoubleProperty maxHeightProperty() {
		return maxHeight;
	}

	public double getMaxHeigth() { return maxHeightProperty().get(); }
	public void setMaxHeigth(double value) { maxHeightProperty().set(value); }

	public VerticalContainer() {
		super();

		ChangeListener<Number> change = (a,b,c) -> {
			updateRequested = true;
		};
		scroll.addListener(change);
		listHeight.addListener(change);
		
		// Important note!
		// The layoutXProperty of a Node is relative to the parent node.
		// So when we changed this code to move the local Pane,
		// we effectively doubled the position of all contents.

		// Setup all things needed to clip the children to the max height;
		pane = new Pane();
		pane.layoutXProperty().bind(xProperty());
		pane.layoutYProperty().bind(yProperty());
		pane.prefWidthProperty().bind(widthProperty());
		pane.prefHeightProperty().bind(heightProperty());
		pane.setOnScroll((e) -> {
			if (isScrollBlocked()) return;
			double oldScroll = getScroll();
			if (userScrollAnim != null) {
				oldScroll = userScrollAnim.getTo();
				userScrollAnim.stop();
			}
			double newScroll = oldScroll + e.getTextDeltaY()*e.getMultiplierY();
			if (newScroll > -(getListHeigth() - getMaxHeigth())) {
				if (newScroll < 0) {
					//userScrollAnim = new ValueAnimation(this, Duration.ofMillis(100))
					//		.setFrom(getScroll()).setTo(newScroll);
					setScroll(newScroll);
					e.consume();
				} else {
					//userScrollAnim = new ValueAnimation(this, Duration.ofMillis(100))
					//		.setFrom(getScroll()).setTo(0);
					if (oldScroll != 0) e.consume();
					setScroll(0);
				}
			} else {
				//userScrollAnim = new ValueAnimation(this, Duration.ofMillis(100)).
				//		setFrom(getScroll()).setTo(-(getListHeigth() - 
				//				getMaxHeigth()));
				if (oldScroll != -(getListHeigth() - getMaxHeigth())) e.consume();
				setScroll(-(getListHeigth() - getMaxHeigth()));
			}
			if (userScrollAnim != null) userScrollAnim.start();		
		});

		Rectangle rect = new Rectangle();
		// Don't bind position of rect, background and children. See note.
		rect.widthProperty().bind(widthProperty());
		rect.heightProperty().bind(heightProperty());
		pane.setClip(rect);

		// The background of the list.
		background = new Rectangle();
		background.opacityProperty().bind(alphaProperty());
		background.fillProperty().bind(backgroundColorProperty());
		background.strokeProperty().bind(borderColorProperty());
		background.strokeWidthProperty().bind(borderWidthProperty());
		background.layoutXProperty().bind(xProperty());
		background.layoutYProperty().bind(yProperty());
		background.widthProperty().bind(widthProperty());
		background.heightProperty().bind(heightProperty());

		// When the amount of children changes, a vertical re-layout is needed.
		getChildren().addListener((Change<? extends ShapeBase> c) -> {
			while (c.next()) {
				for (ShapeBase w : c.getAddedSubList()) {
					w.heightProperty().addListener((a,b,c1) -> {
						// Also when a single item changes its size;
						updateRequested = true;
					});
					updateRequested = true;
				}
				if (!c.getRemoved().isEmpty()) updateRequested = true;
			}
		});
		bindHeight((DoubleExpression) Bindings.min(listHeightProperty(),
				maxHeightProperty()));
	}

	@Override
	public void update(long time) {
		super.update(time);
		if (userScrollAnim != null && !userScrollAnim.hasEnded())
			setScroll(userScrollAnim.getValue());
		if (updateRequested) updateY();
	}

	/**Called to relocate all children.*/
	private void updateY() {
		updateRequested = false;
		double height = 0;
		double width = 0;
		for (ShapeBase w : getChildren()) {
			w.setY(height + getScroll());
			height += w.getHeight();
			if ((height + getScroll() <= 0) ||
					(height - w.getHeight() + getScroll() > 300)) {
				w.setAlpha(0);
			} else w.setAlpha(1);

			if (w.getWidth() > width) width = w.getWidth();
		}
		setListHeigth(height);
		//setHeight(Math.min(height, getMaxHeigth()));
		setWidth(width);
	}

	@Override
	public void addToPane(Pane p) {
		p.getChildren().add(background);
		super.addToPane(pane);
		p.getChildren().add(pane);
	}

	@Override
	public void removeFromPane(Pane p) {
		p.getChildren().remove(background);
		super.removeFromPane(p);
	}
}
