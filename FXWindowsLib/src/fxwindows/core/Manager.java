package fxwindows.core;

import com.sun.istack.internal.NotNull;
import fxwindows.animation.Animation;
import fxwindows.wrapped.container.Container;
import javafx.animation.AnimationTimer;
import javafx.application.Application;
import javafx.beans.property.DoubleProperty;
import javafx.beans.property.SimpleDoubleProperty;
import javafx.beans.value.ChangeListener;
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
	private Text fpsText;
    private RootContainer oldContainer;
	private RootContainer shapeContainer;
	private long frameStart;
	private long frameCount;
	private long fps;

	public static void main(String[] args) {
	    start(Test.class, args);
    }

    public static class Test extends Manager {

	    public Test() {

        }

        @Override
        public void setup(RootContainer canvas) {

        }

        @Override
        public void frame() {

        }

        @Override
        public void shutdown() {

        }
    }

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
		fpsText = new Text();
		fpsText.setX(10);
		fpsText.setY(10);
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
						fpsText.setText(fps + " FPS");
					}
					canv.getGraphicsContext2D().fillRect(0, 0, 10, 10);
                    Updatable.updateAll(now/1000000);
                    if (shapeContainer != null) shapeContainer.update();
                    oldContainerHandler();
					frame();
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		};
		timer.start();
		pane = new Pane();
		pane.getChildren().addAll(canv, fpsText);
		RootContainer startContainer = new RootContainer();
		setup(startContainer);
        setRoot(startContainer, true);
		primaryStage.setScene(new Scene(new BorderPane(pane),500,500));
		primaryStage.setOnCloseRequest((e) -> shutdown());
		primaryStage.show();
	}

	private void oldContainerHandler() {
	    if (shapeContainer == null || oldContainer == null) return;
	    boolean oldDone = false;
	    boolean newDone = false;
        if (shapeContainer.enterAnimation != null) {
            newDone = shapeContainer.enterAnimation.hasFinished();
        } else newDone = true;
        if (oldContainer.exitAnimation != null) {
            oldDone = oldContainer.exitAnimation.hasFinished();
        } else oldDone = true;

        if (oldDone && newDone) {
            // All animations have finished so we can remove the old container
            // (it was kept because it could still be visible).
            oldContainer.removeFromPane(pane);
            oldContainer = null;
            System.out.println("Removed old root");
        }
    }

	public void setRoot(@NotNull RootContainer root, boolean newInFront) {
	    if (oldContainer != null) {
	        System.err.println("Root switch canceled: old was still going. " +
                    "Try again later.");
	        return;
        }
	    if (shapeContainer != null) {
            oldContainer = shapeContainer;
	        if (shapeContainer.exitAnimation != null) {
                System.out.println("Start exit animation old root");
                oldContainer.exitAnimation.start();
            }
        }
        shapeContainer = root;
        System.out.println("Set new root");
        pane.setOnMouseMoved((e) -> {
            shapeContainer.mouseX.set(e.getSceneX());
            shapeContainer.mouseY.set(e.getSceneY());
        });
        shapeContainer.bindHeight(pane.heightProperty());
        shapeContainer.bindWidth(pane.widthProperty());
	    shapeContainer.addToPane(pane);
	    if (!newInFront && oldContainer != null) oldContainer.getPane().toFront();
	    // Make sure the fps stays visible.
        fpsText.toFront();
        if (shapeContainer.enterAnimation != null) {
            System.out.println("Start enter animation new root");
            shapeContainer.enterAnimation.start();
        }
    }

	public static class RootContainer extends Container {

		private final DoubleProperty mouseX = new SimpleDoubleProperty();
		private final DoubleProperty mouseY = new SimpleDoubleProperty();

		private Animation enterAnimation;
		private Animation exitAnimation;

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
