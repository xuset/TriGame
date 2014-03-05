package net.xuset.tSquare.ui;

import java.util.ArrayList;

import net.xuset.tSquare.imaging.IFont;
import net.xuset.tSquare.imaging.IGraphics;
import net.xuset.tSquare.imaging.TsFont;
import net.xuset.tSquare.imaging.TsTypeFace;
import net.xuset.tSquare.system.input.mouse.MouseAction;
import net.xuset.tSquare.system.input.mouse.TsMouseEvent;

public class UiGridList extends UiComponent {
	public static interface GridListChange { void onChange(int index, String[] text); }
	
	private final ArrayList<String[]> items = new ArrayList<String[]>();
	private final int columnCount;
	private final float[] columnWidths;
	
	private IFont font = new TsFont("Arial", 12, TsTypeFace.PLAIN);
	private float gutter = 5.0f;
	private float cellHeight = 0.0f;
	private int selectedIndex;
	private GridListChange listener;
	
	public void setFont(IFont font) { this.font = font; }
	public IFont getFont() { return font; }
	
	public void setGutter(float gutter) { this.gutter = gutter; }
	public float getGutter() { return gutter; }
	
	public void setGridListListener(GridListChange listener) { this.listener = listener; }
	public GridListChange getGridListListener() { return listener; }

	public UiGridList(int columnCount) {
		super(0, 0, 0, 0);
		this.columnCount = columnCount;
		columnWidths  = new float[columnCount];
		
		setOpaque(true);
	}
	
	public int getRowCount() { return items.size(); }
	
	public void addRow(String...item) {
		if (item.length != columnCount)
			throw new IllegalArgumentException("array size not equal to column count");
		items.add(item);
	}
	
	public void removeRow(String[] item) {
		items.remove(item);
	}
	
	public void removeRow(int row) {
		items.remove(row);
	}
	
	public String[] getRow(int row) {
		return items.get(row);
	}
	
	public void setRow(int row, String[] item) {
		if (item.length != columnCount)
			throw new IllegalArgumentException("array size not equal to column count");
		items.set(row, item);
	}
	
	public int getSelectedRowIndex() {
		return selectedIndex;
		}
	
	public String[] getSelectedRow() {
		return items.get(selectedIndex);
	}
	
	public void clearRows() {
		items.clear();
	}
	
	@Override
	public void draw(IGraphics g) {
		if (!isVisible())
			return;
		g.setFont(getFont());
		setDimensions(g);
		super.draw(g);

		float textHeight = g.getTextHeight();
		float y = getY();
		for (int i = 0; i < items.size(); i++) {
			float x = getX() + gutter;
			if (i == selectedIndex) {
				g.setColor(getBackground().shade(-20));
				g.fillRect(getX(), y, getWidth(), cellHeight);
			}

			g.setColor(getForeground());
			for (int j = 0; j < columnCount; j++) {
				g.drawText(x, y + cellHeight / 2 + textHeight / 4, items.get(i)[j]);
				x += columnWidths[j];
			}
			
			
			g.setColor(getBorder().getColor());
			g.drawRect(getX(), y, getWidth(), cellHeight);
			
			y += cellHeight;
			
		}
		
		float currentW = 0.0f;
		for (float w : columnWidths) {
			g.drawRect(getX() + currentW, getY(), w, getHeight());
			currentW += w;
		}
	}
	
	private void setDimensions(IGraphics g) {
		g.setFont(getFont());
		
		for (int i = 0; i < columnCount; i++)
			columnWidths[i] = 0.0f;
		
		for (String[] row : items) {
			for (int i = 0; i < columnCount; i++) {
				String col = row[i];
				float w = g.getTextWidth(col);
				if (w > columnWidths[i])
					columnWidths[i] = w;
			}
		}
		
		float maxWidth = 0.0f;
		for (int i = 0; i < columnCount; i++) {
			columnWidths[i] += 2 * gutter;
			maxWidth += columnWidths[i];
		}
		
		float textHeight = g.getTextHeight();
		cellHeight = textHeight + gutter * 2;
		float maxHeight = cellHeight * items.size();
		
		setSize(maxWidth, maxHeight);
	}
	
	@Override
	protected void recieveMouseEvent(TsMouseEvent e, float x, float y) {
		super.recieveMouseEvent(e, x, y);
		
		if (e.action == MouseAction.PRESS) {
			selectedIndex = (int) (y / cellHeight);
			if (listener != null) {
				listener.onChange(selectedIndex, items.get(selectedIndex));
			}
		}
	}

}
