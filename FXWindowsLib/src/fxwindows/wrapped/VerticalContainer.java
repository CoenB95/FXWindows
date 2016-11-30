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
	private final DoubleProperty contentHeight = new SimpleDoubleProperty();
	private final DoubleProperty contentWidth = new SimpleDoubleProperty();
	private DoubleExpression heightBinding;
	private DoubleExpression widthBinding;
	//private final DoubleProperty maxHeight = new SimpleDoubleProperty(250);
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
	protected DoubleProperty contentHeightProperty() {
		return contentHeight;
	}

	protected double getContentHeight() { return contentHeightProperty().get(); }
	protected void setContentHeight(double value) {
		contentHeightProperty().set(value);
	}
	
	// 
	protected DoubleProperty contentWidthProperty() {
		return contentWidth;
	}

	protected double getContentWidth() { return contentWidthProperty().get(); }
	protected void setContentWidth(double value) { contentWidthProperty().set(value); }
	
	// 
	/*public DoubleProperty maxHeightProperty() {
		return maxHeight;
	}

	public double getMaxHeigth() { return maxHeightProperty().get(); }
	public void setMaxHeigth(double value) { maxHeightProperty().set(value); }
	 */
	public VerticalContainer() {
		super();
		setMaxHeight(250);
		setMaxWidth(100);
		
		heightBinding = Bindings.createDoubleBinding(() -> {
			switch(getHeightBehavior()) {
			case FILL_SPACE:
				return getMaxHeight();
			case WRAP_CONTENT:
				return getContentHeight();
			default:
				return 0.0;
			}
		}, contentHeightProperty(), maxHeightProperty(), heightBehaviorProperty());
		bindHeight(heightBinding);
		
		widthBinding = Bindings.createDoubleBinding(() -> {
			switch(getWidthBehavior()) {
			case FILL_SPACE:
				return getMaxWidth();
			case WRAP_CONTENT:
				return getContentWidth();
			default:
				return 0.0;
			}
		}, contentWidthProperty(), maxWidthProperty(), widthBehaviorProperty());
		bindWidth(widthBinding);

		ChangeListener<Number> change = (a,b,c) -> {
			updateRequested = true;
		};
		scroll.addListener(change);
		contentHeight.addListener(change);
		contentWidth.addListener(change);
		
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
			if (userScrollAnim != null) {
				oldScroll = userScrollAnim.getTo();
				userScrollAnim.stop();
			}
			double newScroll = oldScroll + e.getTextDeltaY()*e.getMultiplierY();
			if (newScroll > -(getContentHeight() - getMaxHeight())) {
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
				if (oldScroll != -(getContentHeight() - getMaxHeight())) e.consume();
				setScroll(-(getContentHeight() - getMaxHeight()));
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
		
		//maxWidthProperty().addListener((a1,a2,a3) -> {
		//	for (ShapeBase sh : getChildren()) {
		//		sh.setMaxWidth(a3.doubleValue());
		//	}
		//});

		// When the amount of children changes, a vertical re-layout is needed.
		getChildren().addListener((Change<? extends ShapeBase> c) -> {
			while (c.next()) {
				for (ShapeBase w : c.getAddedSubList()) {
					w.heightProperty().addListener((a,b,c1) -> {
						// Also when a single item changes its size;
						updateRequested = true;
					});
					//w.setMaxWidth(getMaxWidth());
					w.maxWidthProperty().bind(maxWidthProperty());
					//maxWidthProperty().addListener((a1,a2,a3) -> {
					//	if (a3.doubleValue() < a2.doubleValue()) {
					//		System.out.println("maxWidth went down! " + a2+" "+ a3);
					//	}
					//	w.setMaxWidth(a3.doubleValue());
					//});//.bind(this.maxWidthProperty());
					updateRequested = true;
				}
				if (!c.getRemoved().isEmpty()) updateRequested = true;
			}
		});
		//bindHeight(contentHeightProperty());
		//bindWidth(contentWidthProperty());
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
		setContentHeight(height);
		setContentWidth(width);
		//setHeight(Math.min(height, getMaxHeigth()));
		//setWidth(width);
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
