package fxwindows.core;

import fxwindows.animation.Animation;
import fxwindows.wrapped.container.Container;
import javafx.animation.AnimationTimer;
import javafx.application.Application;
import javafx.beans.property.DoubleProperty;
import javafx.beans.property.SimpleDoubleProperty;
import javafx.scene.Scene;
import javafx.scene.canvas.Canvas;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.Pane;
import javafx.scene.text.Text;
import javafx.stage.Stage;

/**
 * Base class for using FXWindows. Subclasses need to call launch in the
 * main method in order for the application to start.
 * The setup method passes a root container for the nodes to be added to.
 *
 * @author CoenB95
 */
public abstract class Manager extends Application {

	private AnimationTimer timer;
	private Pane pane;
	private RootContainer shapeContainer;
	private long frameStart;
	private long frameCount;
	private long fps;

	public static void start(Class<? extends Manager> theClass, String[] args) {
		launch(theClass, args);
	}
	
	@Override
	public void start(Stage primaryStage) {
		try {
			shapeVersion(primaryStage);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	public void shapeVersion(Stage primaryStage) {
		Text t = new Text();
		t.setX(10);
		t.setY(10);
		Canvas canv = new Canvas(10,10);
		timer = new AnimationTimer() {
			@Override
			public void handle(long now) {
				try {
					frameCount++;
					if (System.currentTimeMillis() > frameStart + 250) {
						fps = frameCount*4;
						frameStart = System.currentTimeMillis();
						frameCount = 0;
						t.setText(fps + " FPS");
					}
					canv.getGraphicsContext2D().fillRect(0, 0, 10, 10);
                    Updatable.updateAll(now/1000000);
					shapeContainer.update();
					frame();
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		};
		timer.start();
		pane = new Pane();
		pane.getChildren().add(canv);
		shapeContainer = new RootContainer(pane);
		setup(shapeContainer);
		pane.getChildren().add(t);
		primaryStage.setScene(new Scene(new BorderPane(pane),500,500));
		primaryStage.setOnCloseRequest((e) -> shutdown());
		primaryStage.show();
	}

	public void setRoot(RootContainer root) {
	    if (shapeContainer.exitAnimation != null) {
	        final RootContainer prev = shapeContainer;
	        prev.exitAnimation.start();
	        prev.exitAnimation.unregisteredProperty().addListener((v1, v2, v3) -> {
	            if (v3) prev.removeFromPane(pane);
            });
        }
        shapeContainer = root;
	    shapeContainer.addToPane(pane);
        if (shapeContainer.enterAnimation != null) {
            shapeContainer.enterAnimation.start();
        }
    }

	public static class RootContainer extends Container {

		private final DoubleProperty mouseX = new SimpleDoubleProperty();
		private final DoubleProperty mouseY = new SimpleDoubleProperty();

		private Animation enterAnimation;
		private Animation exitAnimation;

		public RootContainer(Pane canv) {
			super();
			canv.setOnMouseMoved((e) -> {
				mouseX.set(e.getSceneX());
				mouseY.set(e.getSceneY());
			});
			bindHeight(canv.heightProperty());
			bindWidth(canv.widthProperty());
			canv.setOnMouseClicked((e) -> {
				for (Runnable r : getOnMouseClickedListeners()) r.run();
			});
			addToPane(canv);
		}

		public void setEnterAnimation(Animation value) {
		    enterAnimation = value;
        }

        public void setExitAnimation(Animation value) {
		    exitAnimation = value;
        }

		public DoubleProperty mouseXProperty() {
			return mouseX;
		}

		public DoubleProperty mouseYProperty() {
			return mouseY;
		}
	}

	public abstract void setup(RootContainer canvas);
	public abstract void frame();
	public abstract void shutdown();
}
