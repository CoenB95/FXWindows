package com.cbapps.javafx.idbike;

import com.cbapps.javafx.idbike.SmoothInterpolator.AnimType;

import javafx.animation.Interpolator;
import javafx.animation.RotateTransition;
import javafx.animation.Transition;
import javafx.scene.Group;
import javafx.scene.layout.StackPane;
import javafx.scene.paint.Color;
import javafx.scene.shape.Arc;
import javafx.scene.shape.Rectangle;
import javafx.util.Duration;

public class FancyProgressBar extends StackPane {

	public FancyProgressBar() {
		Rectangle rec = new Rectangle(60, 60, Color.TRANSPARENT);
		Arc arc = new Arc(22.5,22.5,15,15,0,270);
		arc.setStrokeWidth(3);
		arc.strokeProperty().set(Color.BLUE);
		Rectangle arc_cir = new Rectangle(45, 45, Color.TRANSPARENT);
		arc.setFill(Color.TRANSPARENT);
		Group arc_grp = new Group();
		arc_grp.getChildren().addAll(arc_cir, arc);
		getChildren().addAll(rec,arc_grp);
		ArcTransition at = new ArcTransition(Duration.millis(800), arc);
		at.setCycleCount(Transition.INDEFINITE);
		at.setAutoReverse(true);
		at.setInterpolator(new SmoothInterpolator(AnimType.ACCELDECEL));
		at.play();
		RotateTransition art = new RotateTransition(Duration.millis(2000), 
				arc_grp);
		art.setFromAngle(0);
		art.setInterpolator(Interpolator.LINEAR);
		art.setToAngle(-360);
		art.setCycleCount(Transition.INDEFINITE);
		art.play();
	}
}
