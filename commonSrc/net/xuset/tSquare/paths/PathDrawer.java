package net.xuset.tSquare.paths;

import java.util.ArrayList;
import java.util.Collection;

import net.xuset.tSquare.imaging.IGraphics;
import net.xuset.tSquare.imaging.IImage;
import net.xuset.tSquare.imaging.ImageFactory;
import net.xuset.tSquare.imaging.TsColor;
import net.xuset.tSquare.math.point.IPointW;
import net.xuset.tSquare.math.point.Point;
import net.xuset.tSquare.math.rect.IRectangleR;
import net.xuset.tSquare.paths.Node;
import net.xuset.tSquare.paths.Path;
import net.xuset.tSquare.paths.PathDrawerI;



public class PathDrawer implements PathDrawerI{
	private static boolean imagesCreated = false;
	private static IImage pathNode;
	private static IImage openNode;
	private static IImage closedNode;
	private Path path;
	private Collection<Node> openNodes;
	private Collection<Node> closedNodes;
	
	public PathDrawer() {
		openNodes = new ArrayList<Node>();
		closedNodes = new ArrayList<Node>();
	}

	@Override
	public void clearAll() {
		path = null;
		openNodes.clear();
		closedNodes.clear();
	}

	@Override
	public void draw(IGraphics g) {
		if (!imagesCreated)
			createImages();
		
		IRectangleR rect = g.getView();
		
		if (openNodes != null) {
			for (Node n : openNodes) {
				IPointW p = new Point(n.relative.x - 20, n.relative.y -20);
				p.setTo(p.getX() - rect.getX(), p.getY() - rect.getY());
				g.drawImage(openNode, (int) p.getX(), (int) p.getY());
			}
		}
		
		if (closedNodes != null) {
			for (Node n : closedNodes) {
				IPointW p = new Point(n.relative.x - 20, n.relative.y -20);
				p.setTo(p.getX() - rect.getX(), p.getY() - rect.getY());
				g.drawImage(closedNode, (int) p.getX(), (int) p.getY());
			}
		}
		
		if (path != null) {
			for (Node.Point n : path.steps) {
				IPointW p = new Point(n.x - 15, n.y -15);
				p.setTo(p.getX() - rect.getX(), p.getY() - rect.getY());
				g.drawImage(pathNode, (int) p.getX(), (int) p.getY());
			}
		}
	}

	@Override
	public void addToOpenNodes(Node n) {
		openNodes.add(n);
	}

	@Override
	public void addToClosedNodes(Node n) {
		closedNodes.add(n);
	}
	
	@Override
	public void setPath(Path p) {
		path = p;
	}
	
	private static IImage createCircle(int diameter, TsColor c) {
		IImage img = ImageFactory.instance.createEmpty(diameter, diameter);
		IGraphics g = img.getGraphics();
		g.setColor(c);
		g.fillOval(0, 0, diameter, diameter);
		return img;
	}
	
	public static void createImages() {
		imagesCreated = true;
		pathNode = createCircle(30, TsColor.red);
		openNode = createCircle(40, TsColor.yellow);
		closedNode = createCircle(40, TsColor.magenta);
	}

	@Override
	public void update(int frameDelta) {
		
	}
}
