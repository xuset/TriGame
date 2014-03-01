package net.xuset.triGame.game.ui.arsenal;

import net.xuset.tSquare.imaging.IGraphics;
import net.xuset.tSquare.imaging.IImage;
import net.xuset.tSquare.imaging.ImageFactory;
import net.xuset.tSquare.imaging.Sprite;
import net.xuset.tSquare.imaging.TsColor;
import net.xuset.tSquare.system.input.mouse.MouseAction;
import net.xuset.tSquare.system.input.mouse.TsMouseEvent;
import net.xuset.tSquare.ui.UiComponent;

public class UiTrashCan extends UiComponent {
	public static final String SPRITE_ID = "media/trashCan.png";
	private final BuildingAttacher attacher;
	private final IImage image;

	public UiTrashCan(BuildingAttacher attacher) {
		super(0, 0, 0, 0);
		this.attacher = attacher;
		getBorder().setColor(TsColor.red);
		getBorder().setVisibility(true);
		
		IImage rawImg = Sprite.get(SPRITE_ID).createCopy();
		double sx = 80.0 / rawImg.getWidth(), sy = 80.0 / rawImg.getHeight();
		image = ImageFactory.instance.createScaled(rawImg, sx, sy);
	}
	
	@Override
	public void draw(IGraphics g) {
		setSize(image.getWidth(g), image.getHeight(g));
		setVisibile(attacher.isAttached());
		super.draw(g);
		if (!isVisible())
			return;

		g.drawImage(image, getX(), getY());
	}

	@Override
	protected void recieveMouseEvent(TsMouseEvent e, float x, float y) {
		super.recieveMouseEvent(e, x, y);
		
		if (isVisible() && e.action == MouseAction.RELEASE) {
			attacher.clearAttached();
		}
	}

}
