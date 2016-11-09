package fxwindows.wrapped;

import java.util.ArrayList;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

public class Container extends WrappedRectangle {

	private final ObservableList<WrappedNode> children =
			FXCollections.observableArrayList(new ArrayList<>());

	public ObservableList<WrappedNode> getChildren() {
		return children;
	}
	
	public Container() {
		super();
	}
}
