package fxwindows;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import fxwindows.drawable.Container;
import fxwindows.drawable.Drawable;
import javafx.animation.AnimationTimer;
import javafx.application.Application;
import javafx.scene.CacheHint;
import javafx.scene.Scene;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.Pane;
import javafx.scene.paint.Color;
import javafx.scene.paint.Paint;
import javafx.scene.shape.Shape;
import javafx.scene.text.Font;
import javafx.stage.Stage;


public abstract class Manager extends Application {

	private List<Drawable> drawables;
	private AnimationTimer timer;
	private Pane pane;
	private Container container;
	private Paint backgroundColor;
	private Drawable selectedDrawable;
	
	private Canvas canvas;
	private long frameStart;
	private long frameCount;
	private long fps;
	
	private double mouseX;
	private double mouseY;

	@Override
	public void start(Stage primaryStage) {
		//shapeVersion(primaryStage);
		canvasVersion(primaryStage);
	}
	
	public void shapeVersion(Stage primaryStage) {
		pane = new Pane();
		setupShape(pane);
		primaryStage.setScene(new Scene(new BorderPane(pane),100,100));
		primaryStage.show();
	}
	
	public void canvasVersion(Stage primaryStage) {
		try {
			pane = new Pane();			
			drawables = new ArrayList<>();
			canvas = new Canvas(400,400);
			canvas.widthProperty().bind(pane.widthProperty());
			canvas.heightProperty().bind(pane.heightProperty());
			canvas.setOnMouseMoved((e) -> {
				mouseX = e.getSceneX();
				mouseY = e.getSceneY();
			});
			canvas.setOnMouseClicked((e) -> {
				if (selectedDrawable != null) selectedDrawable.click();
			});
			GraphicsContext g = canvas.getGraphicsContext2D();

			timer = new AnimationTimer() {

				@Override
				public void handle(long now) {
					frameCount++;
					if (System.currentTimeMillis() > frameStart + 250) {
						fps = frameCount*4;
						frameStart = System.currentTimeMillis();
						frameCount = 0;
					}
					
					// Update position (Animations)
					container.update();
					
					// Update selections (Mouse)
					Drawable prev = selectedDrawable;
					selectedDrawable = container.mouse(mouseX, mouseY);
					if (prev != selectedDrawable)
						System.out.println("Hovered: " + selectedDrawable);
					
					// Actually draw
					g.setFill(backgroundColor);
					g.fillRect(0, 0, canvas.getWidth(), canvas.getHeight());
					container.draw(g);
					g.setFill(Color.WHITE);
					g.setFont(Font.getDefault());
					g.fillText("FPS: " + fps,10,10);
				}
			};
			timer.start();
			
			pane.getChildren().add(canvas);
			
			Scene scene = new Scene(new BorderPane(pane),400,400);
			primaryStage.setScene(scene);
			primaryStage.show();
			
			container = new DummyContainer(canvas);
			setup(container);
			
		} catch(Exception e) {
			e.printStackTrace();
		}
	}
	
	private static class DummyContainer extends Container {
		public DummyContainer(Canvas canv) {
			super();
			setCalculatedHeight(canv.getHeight());
			setCalculatedWidth(canv.getWidth());
			canv.widthProperty().addListener((a,b,c) -> {
				setCalculatedWidth(c.doubleValue());
			});
			canv.heightProperty().addListener((a,b,c) -> {
				setCalculatedHeight(c.doubleValue());
			});
		}
		@Override
		public void draw(GraphicsContext graphics) {
			for (Drawable child : getChildren()) {
				child.draw(graphics);
			}
		}
	}
	
	public void setBackgroundColor(Paint color) {
		backgroundColor = color;
	}



	public abstract void setup(Container canvas);
	public abstract void setupShape(Pane p);

	public final void registerShape(Shape s) {
		pane.getChildren().add(s);
	}

	public final void registerShapes(Shape... s) {
		pane.getChildren().addAll(s);
	}

	public final void registerShapes(Collection<? extends Shape> s) {
		pane.getChildren().addAll(s);
	}
}
