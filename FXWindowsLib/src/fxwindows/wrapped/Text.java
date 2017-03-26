package fxwindows.wrapped;

import javafx.beans.binding.Bindings;
import javafx.beans.value.ChangeListener;
import javafx.geometry.Bounds;
import javafx.geometry.VPos;
import javafx.scene.CacheHint;
import javafx.scene.Group;
import javafx.scene.Node;
import javafx.scene.paint.Paint;
import javafx.scene.shape.Rectangle;
import javafx.scene.text.Font;

public class Text extends TextBase {

	private Group group;
	private javafx.scene.text.Text textNode;
	private Rectangle rectNode;
	private boolean recalculate = true;
	
	

	public Text() {
		this("");
	}

	public Text(String value) {
		this(new javafx.scene.text.Text());
		setText(value);
	}

	private Text(javafx.scene.text.Text text) {
		super();
		textNode = text;
		textNode.setCache(true);
		textNode.setCacheHint(CacheHint.SPEED);
		rectNode = new Rectangle();
		//textClip = new Rectangle();
		group = new Group(rectNode, textNode);
		setupBindings();
	}

	public Text(String text, Font font) {
		this(text);
		setFont(font);
	}

	public Text(String text, Font font, Paint color) {
		this(text, font);
		setTextColor(color);
	}

	private void setupBindings() {
		ChangeListener l = (a,b,c) -> recalculate = true;
		rectNode.widthProperty().bind(widthProperty());
		rectNode.heightProperty().bind(heightProperty());
		group.layoutXProperty().bind(xProperty());
		group.layoutYProperty().bind(yProperty());
		textNode.layoutXProperty().bind(paddingXProperty().multiply(scaleXProperty()));
        textNode.layoutYProperty().bind(paddingYProperty().multiply(scaleYProperty()));
		textNode.setTextOrigin(VPos.TOP);

		setupTopLevelBindings(group);
		setupScaleBindings(textNode);
		setupBackgroundBindings(rectNode);
		setupMouseBindings(group);

		textNode.fontProperty().addListener(l);
		textNode.textProperty().bind(textProperty());
		textNode.textProperty().addListener(l);
		textNode.fillProperty().bind(textColorProperty());
		textNode.wrappingWidthProperty().bind(Bindings.when(wrapTextProperty())
				.then(innerWidthProperty()).otherwise(0.0));
		maxWidthProperty().addListener(l);
		textNode.fontProperty().bind(fontProperty());
		textNode.wrappingWidthProperty().addListener(l);
		recalculate = true;
	}

	private void calculateSize() {
		textNode.setClip(null);
		Bounds b = textNode.getBoundsInLocal();
		setContentHeight(b.getHeight());
		setContentWidth(b.getWidth());
		Rectangle textClip = new Rectangle();
		textClip.setLayoutX(0);
		textClip.setLayoutY(0);
		textClip.setHeight(getHeight());
		textClip.setWidth(getWidth());
		//textClip.setHeight(100);
		textNode.setClip(textClip);
	}

	@Override
	public void update() {
		if (recalculate) {
			recalculate = false;
			calculateSize();
		}
	}
	
	@Override
	public Node getNode() {
		return group;
	}
	
	@Override
	public void clip(Node n) {
		group.setClip(n);
	}
}
