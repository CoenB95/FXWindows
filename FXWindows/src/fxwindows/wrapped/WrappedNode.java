package fxwindows.wrapped;

import fxwindows.Animatable;
import javafx.scene.Node;
import javafx.scene.layout.Pane;

public abstract class WrappedNode extends Animatable {

//	private Node node;
//	
//	public WrappedNode(Node node) {
//		this.node = node;
//		setupBindings();
//	}
//	
//	private void setupBindings() {
//		node.layoutXProperty().bind(transformedXProperty());
//		node.layoutYProperty().bind(transformedYProperty());
//		node.translateXProperty().bind(transformXProperty());
//		node.translateYProperty().bind(transformYProperty());
//	}
	
//	public Node getNode() {
//		return node;
//	}
	public abstract void addToPane(Pane p);
	public abstract void clip(Node n);
	public abstract void removeFromPane(Pane p);
}
