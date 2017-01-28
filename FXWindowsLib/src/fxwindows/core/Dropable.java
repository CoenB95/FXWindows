package fxwindows.core;

import javafx.scene.input.Dragboard;

/**
 * @author Coen Boelhouwers
 * @version 1.0
 */
public interface Dropable {

	boolean onValidateDrag(Dragboard board);
	void onDragEnter(Dragboard board, boolean valid);
	void onDragExit();
	boolean onDragDropped(Dragboard dragboard);
}
