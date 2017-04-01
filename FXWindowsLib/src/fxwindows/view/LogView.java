package fxwindows.view;

import fxwindows.animation.Animation;
import fxwindows.animation.FadeAnimation;
import fxwindows.animation.SmoothInterpolator;
import fxwindows.animation.ValueAnimation;
import fxwindows.core.LayoutBehavior;
import fxwindows.resources.RobotoFont;
import fxwindows.wrapped.Text;
import fxwindows.wrapped.container.HorizontalContainer;
import fxwindows.wrapped.container.VerticalContainer;
import javafx.application.Platform;
import javafx.beans.property.*;
import javafx.collections.FXCollections;
import javafx.collections.ListChangeListener;
import javafx.collections.ObservableList;
import javafx.scene.paint.Color;
import javafx.scene.paint.Paint;
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

	private static final ObservableList<Log> LOGS = FXCollections.observableArrayList(
			new ArrayList<Log>());

	private double textSize = 12;

	public LogView(double textSize) {
		this.textSize = textSize;
		for (Log l : LOGS) {
			if (l instanceof ProgressLog) logProgress((ProgressLog) l);
			else log(l);
		}
		LOGS.addListener((ListChangeListener<Log>) c -> {
			while (c.next()) {
				for (Log l : c.getAddedSubList()) {
					if (l instanceof ProgressLog) logProgress((ProgressLog) l);
					else log(l);
				}
			}
		});
		//Bindings.bindContent(getChildren(), LOGS);
		clipChildren(true);
		setMaxWidth(200);
		setMaxHeight(158);
		setWidthBehavior(LayoutBehavior.FILL_SPACE);
	}

	public static Log log(String message, Color color) {
		Log log = new Log(message, color);
		Platform.runLater(() -> LOGS.add(log));
		return log;
	}

	public static ProgressLog logProgress(String message, Color color) {
		ProgressLog log = new ProgressLog(message, color);
		Platform.runLater(() -> LOGS.add(log));
		return log;
	}

	private void log(Log msg) {
		Text log = new Text("", Font.loadFont(RobotoFont.regular(), textSize));
		log.textProperty().bind(msg.messageProperty());
		log.textColorProperty().bind(msg.colorProperty());
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
								.then(() -> getChildren().remove(log));
		anim1.then(anim2, 3000);
		anim2.pause(!log.getNode().isVisible());
		log.setPadding(2);
		log.setWrapText(true);
		log.getNode().visibleProperty().addListener((v1, v2, v3) -> anim2.pause(!v3));
		Platform.runLater(() -> {
			getChildren().add(log);
			anim1.start();
		});
	}

	private void logProgress(ProgressLog msg) {
		HorizontalContainer hor = new HorizontalContainer();
		hor.setBorderColor(Color.LIGHTGRAY);
		hor.setWidthBehavior(LayoutBehavior.FILL_SPACE);

		Text logText = new Text("", Font.loadFont(RobotoFont.regular(), textSize));
		logText.textProperty().bind(msg.messageProperty());
		logText.textColorProperty().bind(msg.colorProperty());
		ProgressCircle progressCircle = ProgressCircle.small((Color) msg.getColor());
		progressCircle.setRadiusXY(textSize / 2, textSize / 2);
		Animation fade = new FadeAnimation(progressCircle, Duration.ofMillis(1000))
				.setFrom(1).setTo(0);
		ValueAnimation scale = new ValueAnimation(hor.scaleYProperty(), Duration.ofMillis(400))
				.setFrom(0).setTo(1).setInterpolator(new SmoothInterpolator(SmoothInterpolator.AnimType.DECELERATE));
		logText.setPadding(5);
		msg.progressProperty().addListener((v1, v2, v3) -> {
			if (v3.doubleValue() >= 1) {
				Platform.runLater(() -> {
					fade.start();
					scale.setFrom(1).setTo(0).then(() -> Platform.runLater(() -> getChildren().remove(hor)))
							.startAt(5000);
				});
			}
		});
		hor.getChildren().addAll(logText, progressCircle);
		Platform.runLater(() -> {
			getChildren().add(hor);
			scale.start();
		});
	}

	public void setTextSize(double value) {
		textSize = value;
	}

	public static class Log {
		private StringProperty message;
		private ObjectProperty<Paint> color;

		private Log(String message, Paint color) {
			this.message = new SimpleStringProperty(message);
			this.color = new SimpleObjectProperty<>(color);
		}

		public ObjectProperty<Paint> colorProperty() {
			return color;
		}

		public StringProperty messageProperty() {
			return message;
		}

		public Paint getColor() {
			return color.get();
		}

		public void setColor(Paint value) {
			color.set(value);
		}

		public void setMessage(String value) {
			message.set(value);
		}
	}

	public static class ProgressLog extends Log {
		private DoubleProperty progress;

		private ProgressLog(String message, Color color) {
			super(message, color);
			progress = new SimpleDoubleProperty();
		}

		public DoubleProperty progressProperty() {
			return progress;
		}

		public void setProgress(double value) {
			progress.set(value);
		}
	}

	/*public static class ProgressLog {

		private HorizontalContainer hor;
		private DoubleProperty progress;
		private Text log;
		private ProgressCircle prog;

		private ProgressLog(String message, Color color) {
			hor = new HorizontalContainer();
			hor.setBorderColor(Color.LIGHTGRAY);
			hor.setWidthBehavior(LayoutBehavior.FILL_SPACE);
			progress = new SimpleDoubleProperty();
			log = new Text(message, Font.loadFont(RobotoFont.regular(), 12), color);
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
						scale.setFrom(1).setTo(0).then(() -> Platform.runLater(() -> getChildren().remove(hor)))
								.startAt(5000);
					});
				}
			});
			hor.getChildren().addAll(log, prog);
			Platform.runLater(() -> {
				getChildren().add(hor);
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
	}*/
}
