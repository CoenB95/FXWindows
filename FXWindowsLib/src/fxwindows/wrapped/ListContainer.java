package fxwindows.wrapped;

import javafx.beans.property.DoubleProperty;
import javafx.beans.property.SimpleDoubleProperty;
import javafx.collections.ListChangeListener.Change;
import javafx.scene.input.ScrollEvent.HorizontalTextScrollUnits;
import javafx.scene.layout.Pane;
import javafx.scene.shape.Rectangle;

public class ListContainer extends Container {

	private Rectangle background;
	private Pane pane;
	private DoubleProperty scroll;
	private DoubleProperty schrink;
	private double listHeight;

	/**Flag notifying one or more things have changed that require a
	 * horizontal re-layout.*/
	private boolean updateRequested = true;

	// Scroll of the list.
	public DoubleProperty scrollProperty() {
		if (scroll == null) {
			scroll = new SimpleDoubleProperty();
			scroll.addListener((a,b,c) -> {
				updateRequested = true;
			});
		}
		return scroll;
	}

	public double getScroll() { return scrollProperty().get(); }
	public void setScroll(double value) { scrollProperty().set(value); }
	
	// TODO: rename.
	// Schrink of the list.
	public DoubleProperty schrinkProperty() {
		if (schrink == null) {
			schrink = new SimpleDoubleProperty(1.0);
			schrink.addListener((a,b,c) -> {
				updateRequested = true;
			});
		}
		return schrink;
	}

	public double getSchrink() { return schrinkProperty().get(); }
	public void setSchrink(double value) { schrinkProperty().set(value); }

	public ListContainer() {
		super();

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
			setScroll(getScroll() + e.getTextDeltaY()*e.getMultiplierY());
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
	}

	@Override
	public void update(long time) {
		super.update(time);
		if (updateRequested) updateY();
	}

	public double getListHeight() {
		return listHeight;
	}

	/**Called to relocate all children.*/
	private void updateY() {
		updateRequested = false;
		double height = 0;
		double width = 0;
		for (ShapeBase w : getChildren()) {
			w.setY((height + getScroll()) * getSchrink());
			height += w.getHeight();
			if ((height + getScroll() <= 0) ||
					(height - w.getHeight() + getScroll() > 300)) {
				w.setAlpha(0);
			} else w.setAlpha(1);

			if (w.getWidth() > width) width = w.getWidth();
		}
		listHeight = height;
		setHeight(getSchrink() * Math.min(height,300));
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
