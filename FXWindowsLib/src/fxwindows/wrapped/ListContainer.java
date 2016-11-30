package fxwindows.wrapped;

import javafx.beans.binding.Bindings;
import javafx.beans.binding.DoubleExpression;

public class ListContainer extends VerticalContainer {
	
//	private DoubleExpression heightBinding;
	
	public ListContainer() {
		super();
//		heightBinding = Bindings.createDoubleBinding(() -> {
//			switch(getLayoutBehavior()) {
//			case FILL_SPACE:
//				return getMaxHeight();
//			case WRAP_CONTENT:
//				return Math.min(getListHeigth(), getMaxHeight());
//			default:
//				return 0.0;
//			}
//		}, listHeightProperty(), maxHeightProperty(), layoutBehaviorProperty());
//		bindHeight(heightBinding);
	}
}
