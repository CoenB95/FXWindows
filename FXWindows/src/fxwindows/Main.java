package fxwindows;

import java.time.Duration;

import com.cbapps.javafx.utilities.animation.SmoothInterpolator;
import com.cbapps.javafx.utilities.animation.SmoothInterpolator.AnimType;
import com.cbapps.javafx.utilities.resources.RobotoFont;

import fxwindows.animation.Animation;
import fxwindows.animation.MoveAnimation;
import fxwindows.drawable.Container;
import fxwindows.drawable.ListView;
import fxwindows.drawable.Texie;
import fxwindows.wrapped.ListContainer;
import fxwindows.wrapped.WrappedRectangle;
import javafx.geometry.VPos;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.layout.Pane;
import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;
import javafx.scene.text.Font;
import javafx.scene.text.Text;
import javafx.scene.text.TextAlignment;

public class Main extends Manager {

	public static void main(String[] args) {
		launch(args);
	}
	
	@Override
	public void setup(Container canvas) {
		Texie texie = new Texie("Welkom op Avans Hogeschool Breda");
		texie.wrappingWidthProperty().bind(canvas.transformedWidthProperty()
				.multiply(0.8));
		texie.setFont(Font.loadFont(RobotoFont.thin(),120));
		texie.setTextColor(Color.WHITE);
		//text.setTextAlignment(TextAlignment.CENTER);
		texie.bindX(canvas.transformedWidthProperty().divide(2)
				.subtract(texie.transformedWidthProperty().divide(2)));
		texie.bindY(canvas.transformedHeightProperty().divide(2)
				.subtract(texie.transformedHeightProperty().divide(2)));
		
		ListView texie2 = new ListView();
		texie2.setXY(50, 50);
		texie2.setGeneralFont(Font.loadFont(RobotoFont.medium(), 24));
		for (int i = 0;i < 20;i++) {
			texie2.addTextItem("Test item "+i);
		}
		
		/*ListView list2 = new ListView();
		list2.setY(50);
		list2.bindX(texie2.transformedXProperty().add(texie2.transformedWidthProperty()).add(10));
		list2.setGeneralFont(Font.loadFont(RobotoFont.medium(), 24));
		for (int i = 0;i < 20;i++) {
			list2.addTextItem("Test item "+i);
		}
		
		ListView list3 = new ListView();
		list3.setY(50);
		list3.bindX(list2.transformedXProperty().add(texie2.transformedWidthProperty()).add(10));
		list3.setGeneralFont(Font.loadFont(RobotoFont.medium(), 24));
		for (int i = 0;i < 20;i++) {
			list3.addTextItem("Test item "+i);
		}*/

		canvas.setOnMouseClicked(() -> {
			texie.setAlpha(1);
			MoveAnimation b = new MoveAnimation(texie, Duration.ofSeconds(1));
			b.setFromY(canvas.getHeight()/2 + texie.getHeight()/2);
			b.setInterpolator(new SmoothInterpolator(AnimType.DECELERATE));
			b.start();
			MoveAnimation c = new MoveAnimation(texie, Duration.ofSeconds(1));
			c.setToX(texie.getWidth()*-1.2);
			c.setInterpolator(new SmoothInterpolator(AnimType.ACCELERATE));
			c.startAt(3000);
			new Animation(texie2, Duration.ofMillis(4500)) {	
				@Override
				public void update(double progress) {
					texie2.scroll = -(texie2.getListHeight()-texie2.getHeight()) *
							progress;
				}
			}.startAt(500);
			new Animation(texie2, Duration.ofMillis(500)) {	
				@Override
				public void update(double progress) {
					texie2.scroll = -(texie2.getListHeight()-texie2.getHeight()) *
							(1.0-progress);
				}
			}.start();
		});
		setBackgroundColor(Color.DARKSLATEBLUE);
		canvas.getChildren().addAll(texie, texie2);//, list2, list3);
	}
	
	@Override
	public void setupShape(fxwindows.wrapped.Container canvas) {
		WrappedRectangle background = new WrappedRectangle(
				canvas.widthProperty(), canvas.heightProperty());
		background.setBackgroundColor(Color.DARKSLATEBLUE);
		//background.widthProperty().bind(canvas.widthProperty());
		//background.heightProperty().bind(canvas.heightProperty());
		
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
		
		
		//texie.addToStage(canvas);
		
		ListContainer texie2 = new ListContainer();
		texie2.setXY(50, 50);
		texie2.setBackgroundColor(Color.RED);
		Font f = Font.loadFont(RobotoFont.medium(), 24);
		//texie2.setGeneralFont(Font.loadFont(RobotoFont.medium(), 24));
		for (int i = 0;i < 20;i++) {
			//texie2.addTextItem("Test item "+i);
			fxwindows.wrapped.Texie t = new fxwindows.wrapped.Texie("Test "+i,f);
			t.setTextColor(Color.WHITE);
			texie2.getChildren().add(t);
		}
		
		ListContainer list2 = new ListContainer();
		list2.setY(50);
		list2.bindX(texie2.transformedXProperty().add(texie2.transformedWidthProperty()).add(10));
		//list2.setGeneralFont(Font.loadFont(RobotoFont.medium(), 24));
		for (int i = 0;i < 20;i++) {
			list2.getChildren().add(new fxwindows.wrapped.Texie("Test item "+i, f,
					Color.WHITE));
		}
		
		canvas.getChildren().addAll(background, texie, texie2, list2);

		canvas.setOnMouseClicked(() -> {
			texie.setAlpha(1);
			MoveAnimation b = new MoveAnimation(texie, Duration.ofSeconds(1));
			b.setFromY(canvas.getHeight()/2 + texie.getHeight()/2);
			b.setInterpolator(new SmoothInterpolator(AnimType.DECELERATE));
			b.start();
			MoveAnimation c = new MoveAnimation(texie, Duration.ofSeconds(1));
			c.setToX(texie.getWidth()*-1.2);
			c.setInterpolator(new SmoothInterpolator(AnimType.ACCELERATE));
			c.startAt(3000);
			new Animation(texie2, Duration.ofMillis(4500)) {	
				@Override
				public void update(double progress) {
					texie2.setScroll( -(texie2.getListHeight()-texie2.getHeight()) *
							progress);
				}
			}.startAt(500);
			new Animation(texie2, Duration.ofMillis(500)) {	
				@Override
				public void update(double progress) {
					texie2.setScroll( -(texie2.getListHeight()-texie2.getHeight()) *
							(1.0-progress));
				}
			}.start();
		});
//		setBackgroundColor(Color.DARKSLATEBLUE);
//		canvas.getChildren().addAll(texie, texie2);
	}
}
