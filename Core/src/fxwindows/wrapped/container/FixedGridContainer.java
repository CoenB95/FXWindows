package fxwindows.wrapped.container;

import fxwindows.core.ShapeBase;
import javafx.collections.ListChangeListener;

/**
 * @author Coen Boelhouwers
 */
public class FixedGridContainer extends Container {

	private int columns;
	private int rows;
	private boolean relayout;

	public FixedGridContainer(int columns, int rows) {
		this.columns = columns;
		this.rows = rows;

		heightProperty().addListener((v1, v2, v3) -> relayout = true);
		widthProperty().addListener((v1, v2, v3) -> relayout = true);
		getChildren().addListener((ListChangeListener<ShapeBase>) c -> relayout = true);
	}

	@Override
	public void update() {
		if (relayout) {
			if (columns * rows < getChildren().size())
				throw new IllegalStateException("More items than allocated space (grid = " +
						(columns * rows) + " [" + columns + "*" + rows + "], children = " +
						getChildren().size() + ")");
			double width_step = getInnerWidth() / columns;
			double height_step = getInnerHeight() / rows;

			int col = 0, row = 0;

			for (ShapeBase b : getChildren()) {
				if (b != null) {
					b.setXY(width_step * col,
							height_step * row);
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
