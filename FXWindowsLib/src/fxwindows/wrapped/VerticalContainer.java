package fxwindows.wrapped;

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

	public VerticalContainer() {
		super();
		setMaxHeight(250);
		setMaxWidth(100);

		ChangeListener<Number> change = (a,b,c) -> updateRequested = true;
		scrollProperty().addListener(change);
		contentHeightProperty().addListener(change);
		contentWidthProperty().addListener(change);
		
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
			if (getContentHeight() <= getMaxHeight()) {
				setScroll(0);
				return;
			}
			double oldScroll = getScroll();
			double newScroll = oldScroll + e.getTextDeltaY()*e.getMultiplierY();
			if (updateScroll(oldScroll, newScroll)) e.consume();
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
					w.maxWidthProperty().bind(maxWidthProperty());
					updateRequested = true;
				}
				if (!c.getRemoved().isEmpty()) updateRequested = true;
			}
		});
	}

	@Override
	public void update(long time) {
		super.update(time);
		if (updateRequested) {
			updateY();
			updateScroll(getScroll(), getScroll());
		}
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
		setContentHeight(height);
		setContentWidth(width);
	}
	
	private boolean updateScroll(double oldValue, double newValue) {
		boolean consumed = false;
		if (isScrollBlocked()) return false;
		if (getContentHeight() <= getMaxHeight()) {
			setScroll(0);
			return false;
		}
		if (newValue > -(getContentHeight() - getMaxHeight())) {
			if (newValue < 0) {
				setScroll(newValue);
				consumed = true;
			} else {
				if (oldValue != 0) consumed = true;
				setScroll(0);
			}
		} else {
			if (oldValue != -(getContentHeight() - getMaxHeight())) consumed = true;
			setScroll(-(getContentHeight() - getMaxHeight()));
		}
		return consumed;
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
