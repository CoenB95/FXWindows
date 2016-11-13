package fxwindows;

import java.time.Duration;

import com.cbapps.javafx.utilities.animation.SmoothInterpolator;
import com.cbapps.javafx.utilities.animation.SmoothInterpolator.AnimType;
import com.cbapps.javafx.utilities.resources.RobotoFont;

import fxwindows.animation.Animation;
import fxwindows.animation.MoveAnimation;
import fxwindows.wrapped.Container;
import fxwindows.wrapped.ListContainer;
import fxwindows.wrapped.Texie;
import fxwindows.wrapped.WrappedRectangle;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;

public class Main extends Manager {

	public static void main(String[] args) {
		launch(args);
	}
	
	@Override
	public void setup(Container canvas) {
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
		list1.setBackgroundColor(Color.RED);
		Font f = Font.loadFont(RobotoFont.medium(), 24);
		for (int i = 0;i < 20;i++) {
			Texie t = new Texie("Test "+i,f);
			t.setTextColor(Color.WHITE);
			list1.getChildren().add(t);
		}
		
		ListContainer list2 = new ListContainer();
		list2.setBackgroundColor(Color.PURPLE);
		list2.bindY(list1.transformedYProperty());
		list2.bindX(list1.transformedXProperty().add(list1.widthProperty()));
		for (int i = 0;i < 20;i++) {
			list2.getChildren().add(new Texie("Test item "+i, f, Color.WHITE));
		}
		
		canvas.getChildren().addAll(background, texie, list1, list2);

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
			new Animation(list1, Duration.ofMillis(4500)) {	
				@Override
				public void update(double progress) {
					list1.setScroll( -(list1.getListHeight()-list1.getHeight()) *
							progress);
				}
			}.startAt(500);
			new Animation(list1, Duration.ofMillis(500)) {	
				@Override
				public void update(double progress) {
					list1.setScroll( -(list1.getListHeight()-list1.getHeight()) *
							(1.0-progress));
				}
			}.start();
		});
	}
}
