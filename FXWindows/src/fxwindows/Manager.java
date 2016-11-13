package fxwindows;

import fxwindows.wrapped.Container;
import javafx.animation.AnimationTimer;
import javafx.application.Application;
import javafx.scene.Scene;
import javafx.scene.canvas.Canvas;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.Pane;
import javafx.scene.text.Text;
import javafx.stage.Stage;


public abstract class Manager extends Application {

	private AnimationTimer timer;
	private Pane pane;
	private Container shapeContainer;
	private long frameStart;
	private long frameCount;
	private long fps;
	
	@Override
	public void start(Stage primaryStage) {
		shapeVersion(primaryStage);
	}
	
	public void shapeVersion(Stage primaryStage) {
		Text t = new Text();
		t.setX(10);
		t.setY(10);
		Canvas canv = new Canvas(10,10);
		timer = new AnimationTimer() {
			@Override
			public void handle(long now) {
				frameCount++;
				if (System.currentTimeMillis() > frameStart + 250) {
					fps = frameCount*4;
					frameStart = System.currentTimeMillis();
					frameCount = 0;
					t.setText(fps + " FPS");
				}
				canv.getGraphicsContext2D().fillRect(0, 0, 10, 10);
				shapeContainer.update(now/1000000);
			}
		};
		timer.start();
		pane = new Pane();
		pane.getChildren().add(canv);
		shapeContainer = new DummyShapeContainer(pane);
		setup(shapeContainer);
		pane.getChildren().add(t);
		primaryStage.setScene(new Scene(new BorderPane(pane),500,500));
		primaryStage.show();
	}
	
	private static class DummyShapeContainer extends fxwindows.wrapped.Container {
		public DummyShapeContainer(Pane canv) {
			super();
			bindHeight(canv.heightProperty());
			bindWidth(canv.widthProperty());
			canv.setOnMouseClicked((e) -> {
				if (getOnMouseClicked() != null) getOnMouseClicked().run();
			});
			addToPane(canv);
		}
	}

	public abstract void setup(Container canvas);
}
