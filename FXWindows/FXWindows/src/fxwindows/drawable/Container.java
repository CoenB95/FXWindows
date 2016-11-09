package fxwindows.drawable;

import java.util.ArrayList;

import javafx.beans.property.ObjectProperty;
import javafx.beans.property.SimpleObjectProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

public abstract class Container extends Drawable {

	private ObjectProperty<Drawable> hoveredChild;
	public ObjectProperty<Drawable> hoveredChildProperty() {
		if (hoveredChild == null) hoveredChild = new SimpleObjectProperty<Drawable>();
		return hoveredChild;
	}
	public void setHoveredChild(Drawable value) { hoveredChildProperty().set(value); }
	public Drawable getHoveredChild() { return hoveredChildProperty().get(); }
	
	private final ObservableList<Drawable> children =
			FXCollections.observableArrayList(new ArrayList<>());

	public ObservableList<Drawable> getChildren() {
		return children;
	}
	
	@Override
	public Drawable mouse(double x, double y) {
		if (super.mouse(x, y) != null) {
			boolean found = false;
			for (int i = getChildren().size()-1;i >= 0;i--) {
				Drawable child = getChildren().get(i);
				if (!found && child != null) {
					Drawable innerChild = child.mouse(x, y);
					if (innerChild != null) {
						found = true;
						innerChild.setHovered(true);
						hoveredChildProperty().set(innerChild);
					} else child.setHovered(false);
				} else child.setHovered(false);
			}
			if (found) return hoveredChildProperty().get();
			else return this;
		}
		// The mouse has left the surrounding container.
		// Don't forget to reset the previous hovered child, if any. 
		if (hoveredChildProperty().get() != null) {
			hoveredChildProperty().get().setHovered(false);
			hoveredChildProperty().set(null);
		}
		return null;
	}
	
	@Override
	public void update() {
		super.update();
		for (Drawable drawable : children) drawable.update();
	}
}
