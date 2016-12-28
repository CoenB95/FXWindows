package fxwindows.wrapped;

import fxwindows.wrapped.container.ScrollContainer;
import javafx.collections.ListChangeListener.Change;

/**
 * Base class for a vertical-list-like layout.
 * @author CoenB95
 */
public abstract class VerticalContainer extends ScrollContainer {

	/**Flag notifying one or more things have changed that require a
	 * horizontal re-layout.*/
	//private boolean updateRequested = true;

	public VerticalContainer() {
		super();
		setMaxHeight(250);
		setMaxWidth(100);

		//ChangeListener<Number> change = (a,b,c) -> updateRequested = true;
		//scrollYProperty().addListener(change);
		//contentHeightProperty().addListener(change);
		//contentWidthProperty().addListener(change);

		// When the amount of children changes, a vertical re-layout is needed.
		getChildren().addListener((Change<? extends ShapeBase> c) -> {
			while (c.next()) {
				for (ShapeBase w : c.getAddedSubList()) {
					//w.heightProperty().addListener((a,b,c1) -> {
						// Also when a single item changes its size;
					//	updateRequested = true;
					//});
					w.maxWidthProperty().bind(maxWidthProperty());
					//updateRequested = true;
				}
				//if (!c.getRemoved().isEmpty()) updateRequested = true;
			}
		});
	}

	@Override
	public void update() {
		super.update();
		//if (updateRequested) {
			updateY();
		//}
	}

	/**Called to relocate all children.*/
	private void updateY() {
		//updateRequested = false;
		double height = 0;
		double width = 0;
		for (ShapeBase w : getChildren()) {
			w.setY(height + getScrollY());
			height += w.getHeight();
			if ((height + getScrollY() <= 0) ||
					(height - w.getHeight() + getScrollY() > 300)) {
				w.setAlpha(0);
			} else w.setAlpha(1);

			if (w.getWidth() > width) width = w.getWidth();
		}
		setContentHeight(height);
		setContentWidth(width);
	}
}
