package fxwindows.core;

import javafx.beans.property.DoubleProperty;
import javafx.beans.property.ReadOnlyDoubleProperty;
import javafx.beans.property.ReadOnlyDoubleWrapper;
import javafx.beans.property.SimpleDoubleProperty;

/**
 * @author Coen Boelhouwers
 */
public class PaddedArea extends Area {

    private final DoubleProperty paddingX = new SimpleDoubleProperty();
    private final DoubleProperty paddingY = new SimpleDoubleProperty();

    public DoubleProperty paddingXProperty() {
        return paddingX;
    }

    public double getPaddingX() {
        return paddingX.get();
    }

    public void setPaddingX(double value) {
        paddingX.set(value);
    }

    public DoubleProperty paddingYProperty() {
        return paddingY;
    }

    public double getPaddingY() {
        return paddingY.get();
    }

    public void setPaddingY(double value) {
        paddingY.set(value);
    }

    private final ReadOnlyDoubleWrapper innerX = new ReadOnlyDoubleWrapper();
    private final ReadOnlyDoubleWrapper innerY = new ReadOnlyDoubleWrapper();
    private final ReadOnlyDoubleWrapper innerHeight = new ReadOnlyDoubleWrapper();
    private final ReadOnlyDoubleWrapper innerWidth = new ReadOnlyDoubleWrapper();

    public ReadOnlyDoubleProperty innerXProperty() {
        return innerX.getReadOnlyProperty();
    }

    public ReadOnlyDoubleProperty innerYProperty() {
        return innerY.getReadOnlyProperty();
    }

    public ReadOnlyDoubleProperty innerHeightProperty() {
        return innerHeight.getReadOnlyProperty();
    }

    public ReadOnlyDoubleProperty innerWidthProperty() {
        return innerWidth.getReadOnlyProperty();
    }

    public double getInnerX() {
        return innerX.get();
    }

    public double getInnerY() {
        return innerY.get();
    }

    public double getInnerHeight() {
        return innerHeight.get();
    }

    public double getInnerWidth() {
        return innerWidth.get();
    }

    public PaddedArea() {
        innerHeight.bind(heightProperty().subtract(
                paddingYProperty().multiply(2)));
        innerWidth.bind(heightProperty().subtract(
                paddingXProperty().multiply(2)));
    }
}
