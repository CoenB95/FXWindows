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
		background.setBackgroundColor(Color.DARKSLATEBLUE);
		
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
		
		
		
		canvas.getChildren().addAll(background, texie, list1, list2);
		
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
		
		private WrappedLine line1;
		private WrappedArc arc1;
		private Texie debug1;
		private double startAngle;
		private double newAngle;
		private RootContainer canvas;
		
		public Road(RootContainer root, double x, double y, double angle) {
			canvas = root;
			startAngle = angle;
			line1 = new WrappedLine();
			line1.setX(x);
			line1.setY(y);
			line1.bindEndXY(root.mouseXProperty().subtract(10),
					root.mouseYProperty().subtract(10));
			line1.setBorderWidth(3);
			line1.setBorderColor(Color.ALICEBLUE);
			
			debug1 = new Texie();
			debug1.setXY(50, 50);
			debug1.setBackgroundColor(Color.WHITE);
			
			arc1 = new WrappedArc();
			//arc1.bindX(line1.xProperty());//.add(line1.lengthXProperty()));//.divide(2)));
			//arc1.bindY(line1.yProperty().add(line1.lengthYProperty()));//.divide(2)));
			//arc1.bindRadiusX(line1.lengthXProperty());//.divide(2));
			//arc1.bindRadiusY(line1.lengthYProperty());//.divide(2));
			//arc1.bindX(line1.xProperty());//.subtract(10));
			arc1.setRadiusX(20);
			arc1.setBorderWidth(5);
			arc1.setBorderColor(Color.AQUA);
			//arc1.setBackgroundColor(Color.RED);
			root.getChildren().addAll(arc1, line1, debug1);
		}
		
		public void click() {
			//newAngle = newAngle - 180;
			WrappedArc placedArc = new WrappedArc();
			placedArc.setBorderWidth(5);
			placedArc.setBorderColor(Color.RED);
			update(placedArc);
			canvas.getChildren().add(placedArc);
			line1.setX(line1.endXProperty().get());
			line1.setY(line1.endYProperty().get());
			if (newAngle < 0) newAngle = 360+newAngle;
			startAngle = newAngle;
			while (startAngle > 180) startAngle -= 360;
		}
		
		public void update() {
			update(arc1);
		}
		
		public void update(WrappedArc arcie) {
			double linelength = Math.sqrt( 
					line1.lengthXProperty().get()*line1.lengthXProperty().get()
					+ line1.lengthYProperty().get()*line1.lengthYProperty().get());
			
			boolean positive = line1.lengthYProperty().get()/linelength >= 0;
			double lineAngleRad = Math.acos(line1.lengthXProperty().get()/linelength);
			if (positive) lineAngleRad = -lineAngleRad;
			if (lineAngleRad < 0) lineAngleRad += Math.toRadians(360);
			
			double circleAngleRad = Math.toRadians(startAngle + 90);
			
			double radius = (linelength/2)/Math.cos(Math.toRadians(90) -
					(lineAngleRad - Math.toRadians(startAngle)));
			
			arcie.setX(line1.getX() + Math.cos(circleAngleRad) * radius);
			arcie.setY(line1.getY() - Math.sin(circleAngleRad) * radius);
			arcie.setRadiusY(radius >= 0 ? radius : -radius);
			arcie.setRadiusX(radius >= 0 ? radius : -radius);
			arcie.setStartAngle(radius >= 0 ? startAngle-90 : startAngle+90);
			double angleLength = 
					(Math.toDegrees(lineAngleRad) - startAngle - 
					(Math.toDegrees(lineAngleRad)- startAngle <= 180 ? 0 : 360)) *2;
			arcie.setAngleLength(angleLength);
			newAngle = startAngle + angleLength;
		}
	}
}
