package fxwindows.wrapped;

import javafx.geometry.Bounds;
import javafx.scene.Node;
import javafx.scene.layout.Pane;
import javafx.scene.shape.SVGPath;

public class SVGView extends ShapeBase {

	private SVGPath path;
	private boolean recalculate = true;

	public SVGView() {
		path = new SVGPath();
		setup();
	}
	
	public SVGView(String url) {
		path = new SVGPath();
		path.setContent(url);
		setup();
	}
	
	private void setup() {
		path.layoutXProperty().bind(xProperty());
		path.layoutYProperty().bind(yProperty());
		path.scaleXProperty().bind(widthProperty().divide(contentWidthProperty()));
		path.scaleYProperty().bind(heightProperty().divide(contentHeightProperty()));
		path.contentProperty().addListener((v1,v2,v3) -> recalculate = true);
		path.fillProperty().bind(super.backgroundColorProperty());
	}
	
	@Override
	public void update() {
		if (recalculate) {
			recalculate = false;
			Bounds b = path.getBoundsInParent();
			setContentHeight(b.getHeight());
			setContentWidth(b.getWidth());
		}
	}
	
	@Override
	public void addToPane(Pane p) {
		p.getChildren().add(path);
	}

	@Override
	public void clip(Node n) {
		path.setClip(n);
	}

	@Override
	public void removeFromPane(Pane p) {
		p.getChildren().remove(path);
	}
	
}
