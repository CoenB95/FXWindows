package fxwindows.wrapped.container;

import fxwindows.animation.SmoothInterpolator;
import fxwindows.animation.ValueAnimation;
import javafx.beans.property.BooleanProperty;
import javafx.beans.property.SimpleBooleanProperty;
import java.time.Duration;

/**
 *
 * @author Coen Boelhouwers
 * @version 1.0
 */
public abstract class ScrollContainer extends Container {

    private BooleanProperty scrollBlocked = new SimpleBooleanProperty();
    private ValueAnimation scrollXAnim;
    private ValueAnimation scrollYAnim;

    public BooleanProperty scrollBlockedProperty() {
        return scrollBlocked;
    }

    public boolean isScrollBlocked() { return scrollBlockedProperty().get(); }
    public void blockScroll(boolean value) { scrollBlockedProperty().set(value); }

    public double getScrollX() { return scrollXAnim.getValue(); }
    public void setScrollX(double value) {
        scrollXAnim.setFrom(scrollXAnim.getValue());
        scrollXAnim.setTo(value);
        scrollXAnim.start();
    }

    public double getScrollY() { return scrollYAnim.getValue(); }
    public void setScrollY(double value) {
        scrollYAnim.setFrom(scrollYAnim.getValue());
        scrollYAnim.setTo(value);
        scrollYAnim.start();
    }

    public ScrollContainer() {
        super();
        setMaxHeight(250);
        setMaxWidth(100);

        scrollXAnim = new ValueAnimation(Duration.ofMillis(400));
        scrollYAnim = new ValueAnimation(Duration.ofMillis(400));
        scrollYAnim.setInterpolator(new SmoothInterpolator(SmoothInterpolator.AnimType.DECELERATE));
        scrollXAnim.setInterpolator(new SmoothInterpolator(SmoothInterpolator.AnimType.DECELERATE));

        getPane().setOnScroll((e) -> {
            if (isScrollBlocked()) return;

            double oldScrollY = scrollYAnim.getTo();
            double newScrollY = oldScrollY + e.getTextDeltaY()*e.getMultiplierY();
            double oldScrollX = scrollXAnim.getTo();
            double newScrollX = oldScrollX + e.getTextDeltaX()*e.getMultiplierX();

            if (updateVerticalScroll(oldScrollY, newScrollY) ||
                    updateHorizontalScroll(oldScrollX, newScrollX)) e.consume();
        });
    }

    private boolean updateVerticalScroll(double oldValue, double newValue) {
        boolean consumed = false;
        if (isScrollBlocked()) return false;
        if (getContentHeight() <= getMaxHeight()) {
            setScrollY(0);
            return false;
        }
        if (newValue > -(getContentHeight() - getMaxHeight())) {
            if (newValue < 0) {
                setScrollY(newValue);
                consumed = true;
            } else {
                if (oldValue != 0) consumed = true;
                setScrollY(0);
            }
        } else {
            if (oldValue != -(getContentHeight() - getMaxHeight())) consumed = true;
            setScrollY(-(getContentHeight() - getMaxHeight()));
        }
        return consumed;
    }

    private boolean updateHorizontalScroll(double oldValue, double newValue) {
        boolean consumed = false;
        if (isScrollBlocked()) return false;
        if (getContentWidth() <= getMaxWidth()) {
            setScrollX(0);
            return false;
        }
        if (newValue > -(getContentWidth() - getMaxWidth())) {
            if (newValue < 0) {
                setScrollX(newValue);
                consumed = true;
            } else {
                if (oldValue != 0) consumed = true;
                setScrollX(0);
            }
        } else {
            if (oldValue != -(getContentWidth() - getMaxWidth())) consumed = true;
            setScrollX(-(getContentWidth() - getMaxWidth()));
        }
        return consumed;
    }
}
