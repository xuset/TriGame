package net.xuset.tSquare.imaging;

import net.xuset.tSquare.math.rect.Rectangle;
import net.xuset.tSquare.math.rect.IRectangleR;

import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Matrix;
import android.graphics.Paint;
import android.graphics.Path;
import android.graphics.Rect;
import android.graphics.RectF;
import android.graphics.Typeface;

public class AndroidGraphics implements IGraphics {
	private final Canvas canvas;
	private final Paint paint;
	private final IRectangleR view;
	
	public AndroidGraphics(Bitmap bitmap) {
		this(new Canvas(bitmap));
	}
	
	public AndroidGraphics(Canvas canvas) {
		this.canvas = canvas;
		paint = new Paint();
		view = new Rectangle(0, 0, canvas.getWidth(), canvas.getHeight());
	}

	@Override
	public void drawImage(IImage image, float dx, float dy, float dw, float dh, float sx,
			float sy, float sw, float sh) {
		Rect dst = new Rect((int) dx, (int) dy, (int) (dx + dw), (int) (dy + dh));
		Rect src = new Rect((int) sx, (int) sy, (int) (sx + sw), (int) (sy + sh));
		canvas.drawBitmap(((AndroidImage) image).bitmap, src, dst, paint);
	}

	@Override
	public void drawImage(IImage image, float x, float y) {
		//TODO maybe the paint style should be swtiched back to Paint.Sytle.FILL?
		canvas.drawBitmap(((AndroidImage) image).bitmap, x, y, paint);
	}

	@Override
	public void drawImageRotate(IImage image, float x, float y, double radians) {
		Matrix matrix = new Matrix();
		matrix.setRotate((float) Math.toDegrees(-radians + Math.PI/2), image.getWidth() / 2, image.getHeight() / 2);
		matrix.postTranslate(x, y);
		canvas.drawBitmap(((AndroidImage) image).bitmap, matrix, null);
	}

	@Override
	public void drawRect(float x, float y, float w, float h) {
		paint.setStyle(Paint.Style.STROKE);
		canvas.drawRect(x, y, x + w, y + h, paint);
	}

	@Override
	public void drawRoundedRect(float x, float y, float w, float h, int rx, int ry) {
		paint.setStyle(Paint.Style.STROKE);
		RectF rect = new RectF(x, y, x + w, x + h);
		canvas.drawRoundRect(rect, rx, ry, paint);
	}

	@Override
	public void drawOval(float x, float y, float w, float h) {
		paint.setStyle(Paint.Style.STROKE);
		RectF r = new RectF(x, y, x + w, y + h);
		canvas.drawOval(r, paint);
	}

	@Override
	public void fillRect(float x, float y, float w, float h) {
		paint.setStyle(Paint.Style.FILL);
		canvas.drawRect(x, y, x + w, y + h, paint);
	}

	@Override
	public void fillRoundedRect(float x, float y, float w, float h, int rx, int ry) {
		paint.setStyle(Paint.Style.FILL);
		RectF rect = new RectF(x, y, x + w, y + h);
		canvas.drawRoundRect(rect, rx, ry, paint);
	}
	
	@Override
	public void fillOval(float x, float y, float w, float h) {
		paint.setStyle(Paint.Style.FILL);
		RectF r = new RectF(x, y, x + w, y + h);
		canvas.drawOval(r, paint);
	}
	
	/*public void fillInvertedCircle(float x, float y, float w, float h) {
		paint.setStyle(Paint.Style.FILL);
		Path p = new Path();
		RectF r = new RectF(x, y, x + w, y + h);
		p.addOval(r, Direction.CCW);
		canvas.clipPath(p);
		canvas.drawRect(r, paint);
	}*/

	@Override
	public void setColor(TsColor color) {
		paint.setColor(color.getRGBA());
	}

	@Override
	public void setColor(int color) {
		paint.setColor(color);
	}
	
	@Override
	public void setColor(int r, int g, int b) {
		paint.setColor(Color.rgb(r, g, b));
	}

	@Override
	public void drawLine(float x1, float y1, float x2, float y2) {
		canvas.drawLine(x1, y1, x2, y2, paint);
	}

	@Override
	public void setFont(IFont font) {
		Typeface tf = translateToTypeface(font);
		paint.setTextSize(font.getSize());
		paint.setTypeface(tf);
	}

	@Override
	public void drawText(float x, float y, String text) {
		paint.setStyle(Paint.Style.FILL);
		canvas.drawText(text, x, y, paint);
	}

	@Override
	public float getTextWidth(String text) {
		return paint.measureText(text);
	}
	
	@Override
	public float getTextHeight() {
		return paint.getTextSize();
	}

	@Override
	public void clear() {
		canvas.drawColor(Color.BLACK);
	}

	@Override
	public IRectangleR getView() {
		return view;
	}

	@Override
	public void dispose() {
		//no need to dispose of canvas
	}

	@Override
	public void setAntiAlias(boolean antiAlias) {
		paint.setAntiAlias(antiAlias);
	}
	
	@Override
	public boolean isAntiAliasOn() {
		return paint.isAntiAlias();
	}
	
	@Override
	public void fillTriangle(float x, float y, float w, float h) {
		paint.setStyle(Paint.Style.FILL);

		float aX = x + w / 2, aY = y;
		float bX = x, bY = y + h;
		float cX = x + w, cY = y + h;

	    Path path = new Path();
	    path.moveTo(aX, aY);
	    path.lineTo(bX, bY);
	    path.lineTo(cX, cY);
	    path.lineTo(aX, aY);
	    path.close();

	    canvas.drawPath(path, paint);
	}
	

	public Typeface translateToTypeface(IFont font) {
		int style = Typeface.NORMAL;
		
		switch(font.getTypeFace()) {
		case BOLD:
			style = Typeface.BOLD;
			break;
		case ITALICS:
			style = Typeface.ITALIC;
			break;
		default:
			style = Typeface.NORMAL;
			break;
		}
		
		return Typeface.create(font.getName(), style);
	}

	@Override
	public float getWidthUnits(IImage image) {
		return image.getWidth();
	}

	@Override
	public float getHeightUnits(IImage image) {
		return image.getHeight();
	}

}
