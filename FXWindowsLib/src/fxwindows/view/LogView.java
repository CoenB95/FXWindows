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
import java.util.function.Predicate;

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

	private Predicate<Log> filter;
	private double textSize = 12;

	public LogView(double textSize, Predicate<Log> filtr) {
		this.filter = filtr;
		this.textSize = textSize;

		LOGS.stream().filter(l -> filter == null || filter.test(l)).forEach(l -> {
			if (l instanceof ProgressLog) logProgress((ProgressLog) l);
			else log(l);
		});

		LOGS.addListener((ListChangeListener<Log>) c -> {
			while (c.next()) {
				for (Log l : c.getAddedSubList()) {
					if (filter == null || filter.test(l)) {
						if (l instanceof ProgressLog) logProgress((ProgressLog) l);
						else log(l);
					}
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
		return log("", message, color);
	}

	public static Log log(String tag, String message, Color color) {
		Log log = new Log(tag, message, color);
		Platform.runLater(() -> LOGS.add(log));
		return log;
	}

	public static ProgressLog logProgress(String message, Color color) {
		return logProgress("", message, color);
	}

	public static ProgressLog logProgress(String tag, String message, Color color) {
		ProgressLog log = new ProgressLog(tag, message, color);
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
			LOGS.remove(msg);
			getChildren().add(log);
			anim1.start();
		});
	}

	private void logProgress(ProgressLog msg) {
		HorizontalContainer hor = new HorizontalContainer();
		hor.setBorderColor(Color.LIGHTGRAY);
		hor.setWidthBehavior(LayoutBehavior.FILL_SPACE);
		hor.setChildAlignment(HorizontalContainer.ChildAlignment.CENTER);

		Text logText = new Text("", Font.loadFont(RobotoFont.regular(), textSize));
		logText.textProperty().bind(msg.messageProperty());
		logText.textColorProperty().bind(msg.colorProperty());
		ProgressCircle progressCircle = ProgressCircle.small((Color) msg.getColor());
		progressCircle.setRadiusXY(textSize / 2.5, textSize / 2.5);
		progressCircle.setBorderWidth(textSize / 5);
		progressCircle.setPadding(textSize / 5);
		Animation fade = new FadeAnimation(progressCircle, Duration.ofMillis(1000))
				.setFrom(1).setTo(0);
		new ValueAnimation(hor.scaleYProperty(), Duration.ofMillis(400))
				.setFrom(0)
				.setTo(1)
				.setInterpolator(new SmoothInterpolator(SmoothInterpolator.AnimType.DECELERATE))
				.start();

		logText.setPadding(5);
		msg.progressProperty().addListener((v1, v2, v3) -> {
			if (v3.doubleValue() >= 1) {
				Platform.runLater(() -> {
					LOGS.remove(msg);
					fade.start();
					new ValueAnimation(hor.scaleYProperty(), Duration.ofMillis(400))
							.setFrom(1)
							.setTo(0)
							.then(() -> Platform.runLater(() -> getChildren().remove(hor)))
							.startAt(5000);
				});
			}
		});
		hor.getChildren().addAll(logText, progressCircle);
		Platform.runLater(() -> getChildren().add(hor));
	}

	public void setTextSize(double value) {
		textSize = value;
	}

	public static class Log {
		private String tag;
		private StringProperty message;
		private ObjectProperty<Paint> color;

		private Log(String message, Paint color) {
			this.message = new SimpleStringProperty(message);
			this.color = new SimpleObjectProperty<>(color);
		}

		private Log(String tag, String message, Paint color) {
			this(message, color);
			this.tag = tag;
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

		public String getTag() {
			return tag;
		}

		public void setColor(Paint value) {
			color.set(value);
		}

		public void setMessage(String value) {
			message.set(value);
		}

		public void setTag(String tagName) {
			tag = tagName;
		}
	}

	public static class ProgressLog extends Log {
		private DoubleProperty progress;

		private ProgressLog(String message, Color color) {
			super(message, color);
			progress = new SimpleDoubleProperty();
		}

		private ProgressLog(String tag, String message, Color color) {
			super(tag, message, color);
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
