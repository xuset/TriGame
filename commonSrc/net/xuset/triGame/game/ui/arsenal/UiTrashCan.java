package net.xuset.triGame.game.ui.arsenal;

import net.xuset.tSquare.imaging.IGraphics;
import net.xuset.tSquare.imaging.IImage;
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
		
		image = Sprite.get(SPRITE_ID).createCopy();
	}
	
	@Override
	public void draw(IGraphics g) {
		setSize(image.getWidth(g) + 10, image.getHeight(g) + 10);
		setVisibile(attacher.isAttached());
		super.draw(g);
		if (!isVisible())
			return;

		g.drawImage(image, getX() + 5, getY() + 5);
	}

	@Override
	protected void recieveMouseEvent(TsMouseEvent e, float x, float y) {
		super.recieveMouseEvent(e, x, y);
		
		if (isVisible() && e.action == MouseAction.RELEASE) {
			attacher.clearAttached();
		}
	}

}
