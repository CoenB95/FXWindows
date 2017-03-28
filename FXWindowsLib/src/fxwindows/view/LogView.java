package fxwindows.view;

import fxwindows.animation.Animation;
import fxwindows.animation.FadeAnimation;
import fxwindows.animation.SmoothInterpolator;
import fxwindows.animation.ValueAnimation;
import fxwindows.core.LayoutBehavior;
import fxwindows.core.ShapeBase;
import fxwindows.resources.RobotoFont;
import fxwindows.wrapped.Text;
import fxwindows.wrapped.container.HorizontalContainer;
import fxwindows.wrapped.container.VerticalContainer;
import javafx.application.Platform;
import javafx.beans.binding.Bindings;
import javafx.beans.property.DoubleProperty;
import javafx.beans.property.SimpleDoubleProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;

import java.time.Duration;
import java.util.ArrayList;

/**
 * @author Coen Boelhouwers
 */
public class LogView extends VerticalContainer {

	public static final Color INFO = Color.BLUE;
	public static final Color WARNING = Color.ORANGE;
	public static final Color ERROR = Color.RED;
	public static final Color SUCCESS = Color.GREEN;

	private static final ObservableList<ShapeBase> LOGS = FXCollections.observableArrayList(
			new ArrayList<ShapeBase>());

	public LogView() {
		Bindings.bindContent(getChildren(), LOGS);
		clipChildren(true);
		setMaxWidth(200);
		setMaxHeight(158);
		setWidthBehavior(LayoutBehavior.FILL_SPACE);
	}

	public static void log(String message, Color color) {
		Text log = new Text(message, Font.loadFont(RobotoFont.regular(), 12), color);
		log.setBorderColor(Color.LIGHTGRAY);
		log.setWidthBehavior(LayoutBehavior.FILL_SPACE);
		log.setScaleY(0);
		ValueAnimation anim1 = new ValueAnimation(log.scaleYProperty(), Duration.ofMillis(400))
				.setFrom(0)
				.setTo(1)
				.setInterpolator(new SmoothInterpolator(SmoothInterpolator.AnimType.DECELERATE));

		Animation anim2 = new ValueAnimation(log.scaleYProperty(), Duration.ofMillis(400))
						.setFrom(1)
						.setTo(0)
						.setInterpolator(new SmoothInterpolator(SmoothInterpolator.AnimType.DECELERATE))
								.then(() -> LOGS.remove(log));
		anim1.then(anim2, 3000);
		anim2.pause(!log.getNode().isVisible());
		log.setPadding(2);
		log.setWrapText(true);
		log.getNode().visibleProperty().addListener((v1, v2, v3) -> anim2.pause(!v3));
		Platform.runLater(() -> {
			LOGS.add(log);
			anim1.start();
		});
	}

	public static ProgressLog logProgress(String message, Color color) {
		return new ProgressLog(message, color);
	}

	public static class ProgressLog {

		private HorizontalContainer hor;
		private DoubleProperty progress;
		private Text log;
		private ProgressCircle prog;

		private ProgressLog(String message, Color color) {
			hor = new HorizontalContainer();
			hor.setBorderColor(Color.LIGHTGRAY);
			hor.setWidthBehavior(LayoutBehavior.FILL_SPACE);
			progress = new SimpleDoubleProperty();
			log = new Text(message, Font.loadFont(RobotoFont.regular(), 14), color);
			prog = ProgressCircle.small(Color.BLUE);
			Animation fade = new FadeAnimation(prog, Duration.ofMillis(1000))
					.setFrom(1).setTo(0);
			ValueAnimation scale = new ValueAnimation(hor.scaleYProperty(), Duration.ofMillis(400))
					.setFrom(0).setTo(1).setInterpolator(new SmoothInterpolator(SmoothInterpolator.AnimType.DECELERATE));
			log.setPadding(5);
			progress.addListener((v1, v2, v3) -> {
				if (v3.doubleValue() >= 1) {
					Platform.runLater(() -> {
						fade.start();
						scale.setFrom(1).setTo(0).then(() -> Platform.runLater(() -> LOGS.remove(hor)))
								.startAt(5000);
					});
				}
			});
			hor.getChildren().addAll(log, prog);
			Platform.runLater(() -> {
				LOGS.add(hor);
				scale.start();
			});
		}

		public DoubleProperty progressProperty() {
			return progress;
		}

		public void setColor(Color c) {
			Platform.runLater(() -> {
				log.setTextColor(c);
				prog.setBorderColor(c);
			});
		}
		public void setProgress(double value) {
			progress.set(value);
		}

		public void setText(String value) {
			Platform.runLater(() -> {
				log.setText(value);
			});
		}
	}
}
