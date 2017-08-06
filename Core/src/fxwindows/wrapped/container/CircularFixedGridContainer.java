package fxwindows.wrapped.container;

import fxwindows.core.ShapeBase;
import javafx.collections.ListChangeListener;

/**
 * @author Coen Boelhouwers
 */
public class CircularFixedGridContainer extends Container {

	private int columns;
	private int rows;
	private boolean relayout;

	public CircularFixedGridContainer(int columns, int rows) {
		this.columns = columns;
		this.rows = rows;

		heightProperty().addListener((v1, v2, v3) -> relayout = true);
		widthProperty().addListener((v1, v2, v3) -> relayout = true);
		getChildren().addListener((ListChangeListener<ShapeBase>) c -> relayout = true);
	}

	@Override
	public void update() {
		if (relayout) {
			if (rows + columns + rows + columns < getChildren().size())
				throw new IllegalStateException("More items than allocated space (grid = " +
						(columns * rows) + " [" + columns + "*" + rows + "], children = " +
						getChildren().size() + ")");
			double width_step = getInnerWidth() / columns;
			double height_step = getInnerHeight() / rows;

			int col = 0, row = 0;

			for (int i = 0; i < rows + columns + rows + columns; i++) {
				if (i >= getChildren().size()) continue;
				ShapeBase b = getChildren().get(i);
				if (b != null) {
					if (i < columns - 1) {
						row = 0;
						col = i;
					} else if (i < columns + rows - 2) {
						col = columns - 1;
						row = i + 1 - columns;
					} else if (i < columns + rows + columns - 3) {
						row = rows - 1;
						col = columns + rows + columns - 3 - i;
					} else {
						col = 0;
						row = columns + rows + columns + rows - 4 - i;
					}
					b.setXY(width_step * col,height_step * row);
					b.setMaxWidth(width_step);
					b.setMaxHeight(height_step);
				}
				col = (col + 1) % columns;
				if (col == 0) row = (row + 1) % rows;
			}
			relayout = false;
		}
		super.update();
	}
}
