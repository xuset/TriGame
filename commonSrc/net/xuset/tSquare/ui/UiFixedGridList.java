package net.xuset.tSquare.ui;

public class UiFixedGridList extends UiGridList {
	private final int minRows;
	private final float minWidth;
	private final String[] emptyRow;
	private int rowCount = 0;

	public UiFixedGridList(int columnCount, int minRows, float minWidth) {
		super(columnCount);
		this.minRows = minRows;
		this.minWidth = minWidth;
		emptyRow = new String[columnCount];
		for (int i = 0; i < emptyRow.length; i++)
			emptyRow[i] = "";
		for (int i = 0; i < minRows; i++) {
			super.addRow(emptyRow);
		}
		
	}
	
	@Override
	public int getRowCount() {
		return rowCount;
	}

	@Override
	public void addRow(String... item) {
		if (rowCount <= minRows) {
			setRow(rowCount, item);
		} else {
			super.addRow(item);
		}
		rowCount++;
	}

	@Override
	public void removeRow(String[] item) {
		int firstCount = super.getRowCount();
		super.removeRow(item);
		if (firstCount != super.getRowCount())
			rowCount--;
		
	}

	@Override
	public void removeRow(int row) {
		if (row >= rowCount)
			throw new ArrayIndexOutOfBoundsException();
		else {
			rowCount--;
			super.removeRow(row);
		}
	}

	@Override
	public String[] getRow(int row) {
		if (row >= rowCount)
			throw new ArrayIndexOutOfBoundsException();
		else
			return super.getRow(row);
	}

	@Override
	public void setRow(int row, String[] item) {
		if (row > rowCount)
			throw new ArrayIndexOutOfBoundsException();
		else
			super.setRow(row, item);
	}
	
	@Override
	public void clearRows() {
		super.clearRows();
		rowCount = 0;
		
		for (int i = 0; i < minRows; i++)
			super.addRow(emptyRow);
	}

	@Override
	public void setSize(float w, float h) {
		super.setSize(Math.max(minWidth, w), h);
	}

}
