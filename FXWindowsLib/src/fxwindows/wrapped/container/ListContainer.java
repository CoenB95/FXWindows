package fxwindows.wrapped.container;

import java.time.Duration;

import fxwindows.animation.MoveAnimation;
import fxwindows.animation.SmoothInterpolator;
import fxwindows.animation.SmoothInterpolator.AnimType;
import fxwindows.animation.ValueAnimation;
import fxwindows.core.ShapeBase;
import fxwindows.wrapped.Rectangle;
import javafx.beans.binding.Bindings;
import javafx.beans.property.BooleanProperty;
import javafx.beans.property.IntegerProperty;
import javafx.beans.property.SimpleBooleanProperty;
import javafx.beans.property.SimpleIntegerProperty;
import javafx.collections.ListChangeListener;
import javafx.scene.input.KeyCode;
import javafx.scene.layout.Pane;
import javafx.scene.paint.Color;

/**
 * The ListContainer is a VerticalContainer with added support for
 * walking thru and selecting one of its children.
 * 
 * @author CoenB95
 */
public class ListContainer extends VerticalContainer {
	
	/**
	 * Whether or not the user is allowed to select an item from the list.
	 * <p/>
	 * When true, the list will display visual feedback of selected and hovered
	 * items and allow the user to navigate through the options.
	 * <p/><b>Note:</b><p/>
	 * This property does not prevent scrolling. See {@link #blockScroll(boolean)}.
	 */
	private final BooleanProperty selectionAllowed = new SimpleBooleanProperty();
	
	private final IntegerProperty selectedPosition = new SimpleIntegerProperty();
	
	private int hoveredPosition;
	private Rectangle hoverRect;
	private MoveAnimation hoverPositionAnim;
	private ValueAnimation hoverHeightAnim;
	
	private Rectangle selectionRect;
	private MoveAnimation selectionPositionAnim;
	private ValueAnimation selectionHeightAnim;
	
	public ListContainer() {
		super();
		
		getNode().setFocusTraversable(true);
		getNode().setOnKeyPressed(event -> {
			if (!isSelectionAllowed()) {
				if (getNode().isFocused() && event.getCode() == KeyCode.ENTER) {
					// Temp solution to make ChoiceBox work once closed.
					getChildren().get(selectedPosition.get()).getOnMouseClicked()
					.handle(null);
					event.consume();
				}
				return;
			} else {
				if (event.getCode() == KeyCode.UP && hoveredPosition > 0) {
					hoveredPosition--;
					moveHoverRect(getChildren().get(hoveredPosition));
					event.consume();
				} else if (event.getCode() == KeyCode.DOWN && hoveredPosition <
						getChildren().size() - 1) {
					hoveredPosition++;
					System.out.println("naar item nr " + hoveredPosition);
					moveHoverRect(getChildren().get(hoveredPosition));
					event.consume();
				} else if (event.getCode() == KeyCode.ENTER) {
					setSelectedPosition(hoveredPosition);
					event.consume();
				}
			}
		});
		
		getChildren().addListener((ListChangeListener<ShapeBase>) c -> {
            while (c.next()) {
            	hoveredPosition = 0;
                for (ShapeBase w : c.getAddedSubList()) {
                    w.hoveredProperty().addListener((v1, v2, v3) -> {
                    	if (v3 && isSelectionAllowed()) {
                    		hoveredPosition = getChildren().indexOf(w);
                    		moveHoverRect(w);
                    	}
                    });
                }
            }
        });
		
		hoverPositionAnim = new MoveAnimation(Duration.ofMillis(100));
		hoverPositionAnim.setInterpolator(new SmoothInterpolator(AnimType.DECELERATE));
		hoverHeightAnim = new ValueAnimation(Duration.ofMillis(100));
		selectionPositionAnim = new MoveAnimation(Duration.ofMillis(100));
		selectionPositionAnim.setInterpolator(new SmoothInterpolator(AnimType.DECELERATE));
		selectionHeightAnim = new ValueAnimation(Duration.ofMillis(100));
		
		hoverRect = new Rectangle(widthProperty(), hoverHeightAnim
				.valueProperty());
		hoverRect.alphaProperty().bind(Bindings.when(hoveredProperty().or(getNode()
				.focusedProperty())).then(0.2).otherwise(0.0));
		hoverRect.setBackgroundColor(Color.WHITE);
		
		selectionRect = new Rectangle(widthProperty(), selectionHeightAnim
				.valueProperty());
		selectionRect.alphaProperty().bind(Bindings.when(getNode().focusedProperty()
				.and(selectionAllowed)).then(1.0).otherwise(0.0));
		selectionRect.setBackgroundColor(Color.TRANSPARENT);
		selectionRect.setBorderColor(Color.DEEPSKYBLUE);
		selectionRect.setBorderWidth(2);
		
		hoverPositionAnim.setShapeBase(hoverRect);
		selectionPositionAnim.setShapeBase(selectionRect);
		
		selectedPositionProperty().addListener((v1,v2,v3) -> {
			moveSelectionRect(getChildren().get(v3.intValue()));
			getChildren().get(v3.intValue()).getOnMouseClicked().handle(null);
		});
		
		((Pane) getNode()).getChildren().add(1, hoverRect.getNode());
		((Pane) getNode()).getChildren().add(2, selectionRect.getNode());
		getNode().parentProperty().addListener((v1,v2,v3) -> {
			if (v3 == null) {
				System.out.println("Detected loss of parent for ListContainer. Stop animations.");
				hoverPositionAnim.stop();
				selectionPositionAnim.stop();
			}
		});
	}
	
	private void moveHoverRect(ShapeBase item) {
		hoverPositionAnim.setFrom(0, hoverRect.getY())
		.setTo(item.xProperty(), item.yProperty().add(paddingYProperty()))
		.startAndStick();
		hoverHeightAnim.setFrom(hoverRect.getHeight())
		.setTo(item.getHeight()).start();
		if (item.getY() + item.getHeight() > getInnerHeight()) {
			setScrollY(getScrollY() + getInnerHeight() - 
					(item.getY() + item.getHeight()));
		}
		if (item.getY() < 0) {
			setScrollY(getScrollY() - item.getY());
		}
	}
	
	private void moveSelectionRect(ShapeBase item) {
		selectionPositionAnim.setFrom(0, selectionRect.getY())
		.setTo(item.xProperty(), item.yProperty().add(paddingYProperty()))
		.startAndStick();
		selectionHeightAnim.setFrom(selectionRect.getHeight())
		.setTo(item.getHeight()).start();
	}
	
	public IntegerProperty selectedPositionProperty() {
		return selectedPosition;
	}
	
	public int getSelectedPosition() {
		return selectedPositionProperty().get();
	}
	
	public BooleanProperty selectionAllowedProperty() {
		return selectionAllowed;
	}
	
	public boolean isSelectionAllowed() {
		return selectionAllowed.get();
	}
	
	private void setSelectedPosition(int value) {
		selectedPositionProperty().set(value);
	}
}
