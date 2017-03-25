package fxwindows.util;

import javafx.geometry.Point2D;

/**
 * @author Coen Boelhouwers
 * @version 1.0
 */
public class MathUtil {

	public static Point2D randomDistanceForce(double angle, double minDistance, double maxDistance) {
		double diffDistance = maxDistance - minDistance;
		return force(angle, Math.random() * diffDistance + minDistance);
	}

	public static Point2D randomAngleForce(double minAngle, double maxAngle, double distance) {
		double diffAngle = maxAngle - minAngle;
		return force(Math.random() * diffAngle + minAngle, distance);
	}

	public static Point2D randomForce(double minAngle, double maxAngle, double minDistance, double maxDistance) {
		double diffAngle = maxAngle - minAngle;
		double diffDistance = maxDistance - minDistance;
		return force(Math.random() * diffAngle + minAngle, Math.random() * diffDistance + minDistance);
	}

	public static Point2D force(double angle, double distance) {
		return new Point2D(Math.cos(angle) * distance,
				Math.sin(angle) * distance);
	}
}
