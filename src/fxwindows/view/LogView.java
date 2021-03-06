package fxwindows.view;

import fxwindows.animation.*;
import fxwindows.core.LayoutBehavior;
import fxwindows.core.ShapeBase;
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
import java.util.Collection;
import java.util.function.Predicate;

/**
 * @author Coen Boelhouwers
 */
public class LogView extends VerticalContainer implements ListChangeListener<LogView.Log> {

	public static final Color INFO = Color.BLUE;
	public static final Color WARNING = Color.ORANGE;
	public static final Color ERROR = Color.RED;
	public static final Color SUCCESS = Color.GREEN;

	private static final ObservableList<Log> LOGS = FXCollections.observableArrayList(
			new ArrayList<Log>());

	private Predicate<Log> filter;
	private double textSize = 12;
	private Font font;
	private Color childBackground = Color.WHITE;
	private Color childBorder = Color.LIGHTGRAY;
	private AnimationType animtype;

	public LogView(double textSize, AnimationType type, Predicate<Log> filtr) {
		this.filter = filtr;
		this.animtype = type;
		this.textSize = textSize;
		this.font = Font.font("Roboto", textSize);

		//Bindings.bindContent(getChildren(), LOGS);
		clipChildren(true);
		setMaxWidth(200);
		setMaxHeight(158);
		setWidthBehavior(LayoutBehavior.FILL_SPACE);
	}

	private Animation createEnterAnim(ShapeBase log) {
		switch (animtype) {
			default:
			case ANIM_1:
				return new ScaleAnimation(log, Duration.ofMillis(400), 1, 0, 1, 1)
						.setInterpolator(new SmoothInterpolator(SmoothInterpolator.AnimType.DECELERATE));
			case ANIM_2:
				return new ScaleAnimation(log, Duration.ofMillis(400), 0, 0, 0, 1)
						.setInterpolator(new SmoothInterpolator(SmoothInterpolator.AnimType.DECELERATE))
						.then(new ScaleAnimation(log, Duration.ofMillis(400), 0, 1, 1, 1)
								.setInterpolator(new SmoothInterpolator(SmoothInterpolator.AnimType.DECELERATE))
						);
		}
	}

	private Animation createExitAnim(ShapeBase log) {
		switch (animtype) {
			default:
			case ANIM_1:
				return new ScaleAnimation(log, Duration.ofMillis(400), 1, 1, 1, 0)
						.setInterpolator(new SmoothInterpolator(SmoothInterpolator.AnimType.DECELERATE))
								.then(() -> getChildren().remove(log));
			case ANIM_2:
				return new ScaleAnimation(log, Duration.ofMillis(400), 1, 1, 0, 1)
						.setInterpolator(new SmoothInterpolator(SmoothInterpolator.AnimType.DECELERATE))
						.then(new ScaleAnimation(log, Duration.ofMillis(400), 0, 1, 0, 0)
								.setInterpolator(new SmoothInterpolator(SmoothInterpolator.AnimType.DECELERATE))
								.then(() -> getChildren().remove(log))
						);
		}
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
		Text log = new Text("", font);
		log.textProperty().bind(msg.messageProperty());
		log.textColorProperty().bind(msg.colorProperty());
		log.setBackgroundColor(childBackground);
		log.setBorderColor(childBorder);
		log.setWidthBehavior(LayoutBehavior.FILL_SPACE);
		log.setScaleY(0);

		Animation anim1 = createEnterAnim(log);
		Animation anim2 = createExitAnim(log);

		anim1.then(anim2, 3000);
		anim2.pause(!log.getNode().isVisible(), 5000);
		log.setPadding(2);
		log.setWrapText(true);
		log.getNode().visibleProperty().addListener((v1, v2, v3) -> anim2.pause(!v3, 5000));
		Platform.runLater(() -> {
			LOGS.remove(msg);
			getChildren().add(log);
			anim1.start();
		});
	}

	private void logProgress(ProgressLog msg) {
		if (msg.progressProperty().get() >= 1) return;
		HorizontalContainer hor = new HorizontalContainer();
		hor.setLayoutBehavior(LayoutBehavior.WRAP_CONTENT);
		hor.setBackgroundColor(childBackground);
		hor.setBorderColor(childBorder);
		hor.setWidthBehavior(LayoutBehavior.FILL_SPACE);
		hor.setChildAlignment(HorizontalContainer.ChildAlignment.CENTER);

		Text logText = new Text("", font);
		logText.textProperty().bind(msg.messageProperty());
		logText.textColorProperty().bind(msg.colorProperty());
		ProgressCircle progressCircle = ProgressCircle.small((Color) msg.getColor());
		progressCircle.setRadiusXY(textSize / 2.5, textSize / 2.5);
		progressCircle.setBorderWidth(textSize / 5);
		progressCircle.setPadding(textSize / 5);
		Animation fade = new FadeAnimation(progressCircle, Duration.ofMillis(1000))
				.setFrom(1).setTo(0);

		createEnterAnim(hor).start();

		logText.setPadding(5);
		msg.progressProperty().addListener((v1, v2, v3) -> {
			if (v3.doubleValue() >= 1) {
				Platform.runLater(() -> {
					LOGS.remove(msg);
					fade.start();
					createExitAnim(hor).startAt(5000);
				});
			}
		});
		hor.getChildren().addAll(logText, progressCircle);
		Platform.runLater(() -> getChildren().add(hor));
	}

	public void setChildBackgroundColor(Color value) {
		childBackground = value;
	}

	public void setTextSize(double value) {
		textSize = value;
	}

	@Override
	public void onStart() {
		super.onStart();
		logChanges(LOGS);
		LOGS.addListener(this);
	}

	@Override
	public void onStop() {
		super.onStop();
		LOGS.removeListener(this);
	}

	@Override
	public void onChanged(Change<? extends Log> c) {
		while (c.next()) {
			logChanges(c.getAddedSubList());
		}
	}

	private void logChanges(Collection<? extends Log> newLogs) {
		for (Log l : newLogs) {
			if (filter == null || filter.test(l)) {
				if (l instanceof ProgressLog) logProgress((ProgressLog) l);
				else log(l);
			}
		}
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

	public enum AnimationType {
		ANIM_1, ANIM_2
	}
}
