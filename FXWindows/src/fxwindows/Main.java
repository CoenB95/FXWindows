package fxwindows;

import java.time.Duration;

import com.cbapps.javafx.utilities.animation.SmoothInterpolator;
import com.cbapps.javafx.utilities.animation.SmoothInterpolator.AnimType;
import com.cbapps.javafx.utilities.resources.RobotoFont;

import fxwindows.animation.Animation;
import fxwindows.animation.MoveAnimation;
import fxwindows.wrapped.ListContainer;
import fxwindows.wrapped.Texie;
import fxwindows.wrapped.WrappedArc;
import fxwindows.wrapped.WrappedLine;
import fxwindows.wrapped.WrappedRectangle;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;

public class Main extends Manager {
	
	private Road currentRoad;
	
	public static void main(String[] args) {
		launch(args);
	}
	
	@Override
	public void setup(RootContainer canvas) {
		WrappedRectangle background = new WrappedRectangle(
				canvas.widthProperty(), canvas.heightProperty());
		background.setBackgroundColor(Color.DARKGREEN);
		
		fxwindows.wrapped.Texie texie = new fxwindows.wrapped.Texie(
				"Welkom op Avans Hogeschool Breda");
		texie.wrappingWidthProperty().bind(canvas.widthProperty()
				.multiply(0.8));
		texie.setFont(Font.loadFont(RobotoFont.thin(),120));
		texie.setTextColor(Color.WHITE);
		//texie.setTextAlignment(TextAlignment.CENTER);
		texie.bindX(canvas.widthProperty().divide(2).subtract(
				texie.widthProperty().divide(2)));
		texie.bindY(canvas.heightProperty().divide(2).subtract(
				texie.heightProperty().divide(2)));
		//texie.setTextOrigin(VPos.CENTER);
		
		ListContainer list1 = new ListContainer();
		list1.setXY(100, 10);
		list1.bindX(canvas.widthProperty().divide(4));
		list1.setBorderColor(Color.RED);
		list1.setBorderWidth(3);
		Font f = Font.loadFont(RobotoFont.medium(), 24);
		for (int i = 0;i < 20;i++) {
			Texie t = new Texie("Test "+i,f);
			t.setTextColor(Color.WHITE);
			list1.getChildren().add(t);
		}
		
		ListContainer list2 = new ListContainer();
		list2.setBorderColor(Color.ORANGE);
		list2.setBorderWidth(3);
		list2.bindY(list1.yProperty().add(list1.heightProperty()));
		//list2.bindX(list1.transformedXProperty().add(list1.widthProperty()));
		list2.bindX(canvas.widthProperty().divide(2));
		for (int i = 0;i < 100;i++) {
			list2.getChildren().add(new Texie("Test item "+i, f, Color.WHITE));
		}
		
		
		
		canvas.getChildren().addAll(background);//, texie, list1, list2);
		
		currentRoad = new Road(canvas, 200, 400, 10);

		canvas.setOnMouseClicked(() -> {
			currentRoad.click();
			/*texie.setAlpha(1);
			MoveAnimation b = new MoveAnimation(texie, Duration.ofSeconds(1));
			b.setFromY(canvas.getHeight()/2 + texie.getHeight()/2);
			b.setInterpolator(new SmoothInterpolator(AnimType.DECELERATE));
			b.start();
			MoveAnimation c = new MoveAnimation(texie, Duration.ofSeconds(1));
			c.setToX(texie.getWidth()*-1.2);
			c.setInterpolator(new SmoothInterpolator(AnimType.ACCELERATE));
			c.startAt(3000);
			new Animation(list1, Duration.ofMillis(4500)) {	
				@Override
				public void update(double progress) {
					list1.setSchrink(progress);
					//list1.setScroll( -(list1.getListHeight()-list1.getHeight()) *
					//		progress);
					list2.setScroll( -(list2.getListHeight()-list2.getHeight()) *
							progress);
				}
			}.startAt(500);
			new Animation(list1, Duration.ofMillis(500)) {	
				@Override
				public void update(double progress) {
					list1.setSchrink(1.0-progress);
					//list1.setScroll( -(list1.getListHeight()-list1.getHeight()) *
					//		(1.0-progress));
					list2.setScroll( -(list2.getListHeight()-list2.getHeight()) *
							(1.0-progress));
				}
			}.start();*/
		});
	}
	
	@Override
	public void frame() {
		currentRoad.update();
	}
	
	public static class Road {
		
		private RootContainer canvas;
		private WrappedLine line;
		private WrappedArc arc;
		private double startAngle;
		private double newAngle;
		
		private double arcX;
		private double arcY;
		private double arcRadius;
		private double arcStartAngle;
		private double arcAngleLength;
		
		public Road(RootContainer root, double x, double y, double angle) {
			canvas = root;
			startAngle = angle;
			line = new WrappedLine();
			line.setX(x);
			line.setY(y);
			line.bindEndXY(root.mouseXProperty().subtract(10),
					root.mouseYProperty().subtract(10));
			line.setBorderWidth(3);
			line.setBorderColor(Color.ALICEBLUE);
			
			arc = new WrappedArc();
			arc.setRadiusX(20);
			arc.setBorderWidth(10);
			arc.setBorderColor(Color.AQUA);
			root.getChildren().addAll(arc, line);
		}
		
		public void click() {
			WrappedArc placedArc = new WrappedArc();
			placedArc.setBorderWidth(10);
			placedArc.setBorderColor(Color.DARKGRAY);
			updateArc(placedArc);
			canvas.getChildren().add(placedArc);
			line.setX(line.endXProperty().get());
			line.setY(line.endYProperty().get());
			if (newAngle < 0) newAngle = 360+newAngle;
			startAngle = newAngle;
			while (startAngle > 180) startAngle -= 360;
		}
		
		public void update() {
			calculate();
			updateArc(arc);
		}
		
		public void updateArc(WrappedArc a) {
			a.setX(arcX);
			a.setY(arcY);
			a.setRadiusXY(arcRadius, arcRadius);
			a.setStartAngle(arcStartAngle);
			a.setAngleLength(arcAngleLength);
		}
		
		public void calculate() {
			double linelength = Math.sqrt( 
					line.lengthXProperty().get()*line.lengthXProperty().get()
					+ line.lengthYProperty().get()*line.lengthYProperty().get());
			
			boolean positive = line.lengthYProperty().get()/linelength >= 0;
			double lineAngleRad = Math.acos(line.lengthXProperty().get()/linelength);
			if (positive) lineAngleRad = -lineAngleRad;
			if (lineAngleRad < 0) lineAngleRad += Math.toRadians(360);
			
			double circleAngleRad = Math.toRadians(startAngle + 90);
			
			double radius = (linelength/2)/Math.cos(Math.toRadians(90) -
					(lineAngleRad - Math.toRadians(startAngle)));
			
			arcX = line.getX() + Math.cos(circleAngleRad) * radius;
			arcY = line.getY() - Math.sin(circleAngleRad) * radius;
			arcRadius = radius >= 0 ? radius : -radius;
			arcStartAngle = radius >= 0 ? startAngle-90 : startAngle+90;
			arcAngleLength = 
					(Math.toDegrees(lineAngleRad) - startAngle - 
					(Math.toDegrees(lineAngleRad)- startAngle <= 180 ? 0 : 360)) *2;
			
			newAngle = startAngle + arcAngleLength;
		}
	}
}
