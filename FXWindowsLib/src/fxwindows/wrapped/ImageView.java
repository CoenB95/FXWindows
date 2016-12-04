package fxwindows.wrapped;

import javafx.geometry.Bounds;
import javafx.scene.Node;
import javafx.scene.image.Image;
import javafx.scene.layout.Pane;

public class ImageView extends ShapeBase {

	private javafx.scene.image.ImageView view;
	private boolean recalculate = true;

	public ImageView() {
		this(new javafx.scene.image.ImageView());
	}
	
	public ImageView(Image image) {
		this(new javafx.scene.image.ImageView(image));
	}
	
	public ImageView(String url) {
		this(new javafx.scene.image.ImageView(url));
	}
	
	private ImageView(javafx.scene.image.ImageView iView) {
		super();
		view = iView;
		view.xProperty().bind(xProperty());
		view.yProperty().bind(yProperty());
		view.fitWidthProperty().bind(maxWidthProperty());
		view.fitHeightProperty().bind(maxHeightProperty());
		view.imageProperty().addListener((v1,v2,v3) -> recalculate = true);
	}
	
	@Override
	public void update(long time) {
		super.update(time);
		if (recalculate) {
			recalculate = false;
			Bounds b = view.getBoundsInParent();
			setContentHeight(b.getHeight());
			setContentWidth(b.getWidth());
		}
	}
	
	@Override
	public void addToPane(Pane p) {
		p.getChildren().add(view);
	}

	@Override
	public void clip(Node n) {
		view.setClip(n);
	}

	@Override
	public void removeFromPane(Pane p) {
		p.getChildren().remove(view);
	}
	
}
