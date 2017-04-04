package fxwindows.wrapped;

import fxwindows.core.ShapeBase;
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
		view.layoutXProperty().bind(xProperty());
		view.layoutYProperty().bind(yProperty());
		//view.fitWidthProperty().bind(maxWidthProperty());
		//view.fitHeightProperty().bind(maxHeightProperty());
		view.imageProperty().addListener((v1,v2,v3) -> {
			recalculate = true;
			if (v3 != null) v3.progressProperty().addListener((v11, v12, v13) -> {
				if (v13.doubleValue() >= 1.0) recalculate = true;
			});
		});
		setupTopLevelBindings(view);
		setupScaleBindings(view);
		setupMouseBindings(view);
	}

	public void setImage(Image image) {
		view.setImage(image);
	}

	public void setImage(String url) {
		view.setImage(new Image(url));
	}
	
	@Override
	public void update() {
		if (recalculate) {
			recalculate = false;
			Bounds b = view.getBoundsInLocal();
			setContentHeight(b.getHeight());
			setContentWidth(b.getWidth());
		}
	}
	
	@Override
	public Node getNode() {
		return view;
	}

	@Override
	public void clip(Node n) {
		view.setClip(n);
	}
}
