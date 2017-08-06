package fxwindows.wrapped;

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
	private boolean calculateSize = true;
	private boolean calculateClip = false;
	

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

		textNode.fontProperty().addListener((a,b,c) -> calculateSize = true);
		textNode.textProperty().bind(textProperty());
		textNode.textProperty().addListener((a,b,c) -> calculateSize = true);
		textNode.fillProperty().bind(textColorProperty());

		maxWidthProperty().addListener((a,b,c) -> calculateSize = true);
		widthProperty().addListener((a,b,c) -> calculateClip = true);
		heightProperty().addListener((a,b,c) -> calculateClip = true);
		textNode.fontProperty().bind(fontProperty());
		wrapTextProperty().addListener((a,b,c) -> calculateSize = true);
		calculateSize = true;
	}

	private void calculateSize() {
		textNode.setClip(null);
		textNode.setWrappingWidth(0);
		setContentWidth(textNode.getBoundsInLocal().getWidth());
		if (getWrapText()) textNode.setWrappingWidth(getInnerWidth());
		setContentHeight(textNode.getBoundsInLocal().getHeight());
	}

	private void calculateClip() {
		if (getContentWidth() <= getInnerWidth() && getContentHeight() <= getInnerHeight()) {
			textNode.setClip(null);
		} else {
			Rectangle textClip = new Rectangle();
			textClip.setLayoutX(0);
			textClip.setLayoutY(0);
			textClip.setHeight(getInnerHeight());
			textClip.setWidth(getInnerWidth());
			textNode.setClip(textClip);
		}
	}

	@Override
	public void update() {
		if (calculateSize) calculateSize();
		if (calculateSize || calculateClip) calculateClip();
		calculateSize = false;
		calculateClip = false;
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
