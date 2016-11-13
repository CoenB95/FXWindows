package fxwindows.drawable;

import javafx.scene.canvas.GraphicsContext;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;

public class ListView extends Container {
	private Font font;
	
	private double listHeight;
	private double listWidth;
	public double scroll;
	private int firstVisible;
	
	public ListView() {
		font = Font.getDefault();
	}
	
	public void addTextItem(String text) {
		Texie t = new Texie(text);
		t.setFont(font);
		t.setTextColor(Color.WHITE);
		getChildren().add(t);
	}
	
	public void setGeneralFont(Font f) {
		font = f;
	}
	
	public double getListHeight() {
		return listHeight;
	}
	
	@Override
	public Drawable mouse(double x, double y) {
		if (getHoveredChild() != null) {
			Drawable same = getHoveredChild().mouse(x, y);
			if (same != null) return same;
		}
		if (super.mouse(x, y) != null) {
			boolean found = false;
			int i = firstVisible;
			while (!found && i < getChildren().size()) {
				Drawable child = getChildren().get(i);
				if (!found && child != null) {
					Drawable innerChild = child.mouse(x, y);
					if (innerChild != null) {
						found = true;
						innerChild.setHovered(true);
						setHoveredChild(innerChild);
					} else child.setHovered(false);
				} else child.setHovered(false);
				i++;
			}
			if (found) return getHoveredChild();
			else return this;
		}
		// The mouse has left the surrounding container.
		// Don't forget to reset the previous hovered child, if any. 
		if (getHoveredChild() != null) {
			getHoveredChild().setHovered(false);
			setHoveredChild(null);
		}
		return null;
	}
	
	@Override
	public void draw(GraphicsContext g) {
		double maxH = 300;
		listHeight = 0;
		listWidth = 0;
		for (int i = 0;i < getChildren().size();i++) {
			Drawable d = getChildren().get(i);
			d.setX(getX());
			d.setY(getY() + listHeight + scroll);
			if (listHeight + scroll > 0) firstVisible = i-1;
			if (d.getWidth() > listWidth) listWidth = d.getWidth();
			listHeight += d.getHeight();
		}
		setHeight(Math.min(listHeight,maxH));
		setWidth(listWidth);
		g.setGlobalAlpha(getAlpha());
		g.setFill(Color.RED);
		g.fillRect(getX(), getY(), getWidth(), getHeight());
		//g.save();
		//g.beginPath();
		//g.rect(getX(), getY(), getWidth(), getHeight());
		//g.clip();
		Drawable top_clip;
		Drawable bottom_clip;
		for (Drawable d : getChildren()) {
			if (d.getY() + d.getHeight() >= getY() && d.getY() < getY()+maxH) {
				if (d.getY() + d.getHeight() > getY() + maxH) {
					g.save();
					g.beginPath();
					g.rect(getX(), getY(), getWidth(), getHeight());
					g.clip();
					d.draw(g);
					g.closePath();
					g.restore();
				} else d.draw(g);
			}
		}
		g.closePath();
		g.restore();
		g.setGlobalAlpha(1);
	}
}