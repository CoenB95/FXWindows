package fxwindows.wrapped;

import java.util.ArrayList;

import javafx.collections.FXCollections;
import javafx.collections.ListChangeListener;
import javafx.collections.ObservableList;
import javafx.scene.Node;
import javafx.scene.layout.Pane;

public abstract class Container extends WrappedNode {

	private Pane pane;
	
	private final ObservableList<WrappedNode> children =
			FXCollections.observableArrayList(new ArrayList<>());

	public ObservableList<WrappedNode> getChildren() {
		return children;
	}
	
	public Container() {
		super();
		getChildren().addListener(new ListChangeListener<WrappedNode>() {

			@Override
			public void onChanged(Change<? extends WrappedNode> c) {
				while (c.next()) {
					for (WrappedNode w : c.getAddedSubList()) {
						if (pane != null) w.addToPane(pane);
					}
				}
			}
			
		});
	}
	
	@Override
	public void update(long time) {
		super.update(time);
		for (WrappedNode w : getChildren()) {
			w.update(time);
		}
	}

	@Override
	public void addToPane(Pane p) {
		pane = p;
		for (WrappedNode w : getChildren()) {
			w.addToPane(p);
		}
	}
	
	@Override
	public void removeFromPane(Pane p) {
		for (WrappedNode w : getChildren()) {
			w.removeFromPane(p);
		}
	}
	
	@Override
	public void clip(Node n) {
		// TODO Implement clip for lists?
	}
}
