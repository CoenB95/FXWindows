package fxwindows.core;

import javafx.scene.input.Dragboard;

/**
 * @author Coen Boelhouwers
 * @version 1.0
 */
@FunctionalInterface
public interface Draggable {
	void setupDragboard(Dragboard board);
}
