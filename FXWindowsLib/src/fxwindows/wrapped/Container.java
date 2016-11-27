package fxwindows.wrapped;

import java.util.ArrayList;

import javafx.collections.FXCollections;
import javafx.collections.ListChangeListener;
import javafx.collections.ObservableList;
import javafx.scene.Node;
import javafx.scene.layout.Pane;

/**
 * 
 * @author PC1
 *
 */
public abstract class Container extends ShapeBase {

	private Pane pane;
	
	private final ObservableList<ShapeBase> children =
			FXCollections.observableArrayList(new ArrayList<>());

	public ObservableList<ShapeBase> getChildren() {
		return children;
	}
	
	public Container() {
		super();
		getChildren().addListener(new ListChangeListener<ShapeBase>() {

			@Override
			public void onChanged(Change<? extends ShapeBase> c) {
				while (c.next()) {
					for (ShapeBase w : c.getAddedSubList()) {
						if (pane != null) w.addToPane(pane);
					}
				}
			}
			
		});
	}
	
	@Override
	public void update(long time) {
		super.update(time);
		for (ShapeBase w : getChildren()) {
			w.update(time);
		}
	}

	@Override
	public void addToPane(Pane p) {
		pane = p;
		for (ShapeBase w : getChildren()) {
			w.addToPane(p);
		}
	}
	
	@Override
	public void removeFromPane(Pane p) {
		for (ShapeBase w : getChildren()) {
			w.removeFromPane(p);
		}
	}
	
	@Override
	public void clip(Node n) {
		// TODO Implement clip for lists?
	}
}
