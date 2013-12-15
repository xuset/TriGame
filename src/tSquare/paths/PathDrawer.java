package tSquare.paths;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Point;
import java.awt.image.BufferedImage;
import java.util.ArrayList;
import java.util.Collection;

import tSquare.game.GameBoard.ViewRect;
import tSquare.imaging.ImageProcess;

public class PathDrawer implements PathDrawerI{
	private static boolean imagesCreated = false;
	private static BufferedImage pathNode;
	private static BufferedImage openNode;
	private static BufferedImage closedNode;
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
	public void draw(Graphics2D g, ViewRect rect) {
		if (!imagesCreated)
			createImages();
		
		if (openNodes != null) {
			for (Node n : openNodes) {
				Point p = new Point(n.relative.x - 20, n.relative.y -20);
				rect.translateToView(p);
				g.drawImage(openNode, p.x, p.y, null);
			}
		}
		
		if (closedNodes != null) {
			for (Node n : closedNodes) {
				Point p = new Point(n.relative.x - 20, n.relative.y -20);
				rect.translateToView(p);
				g.drawImage(closedNode, p.x, p.y, null);
			}
		}
		
		if (path != null) {
			for (Node.Point n : path.steps) {
				Point p = new Point(n.x - 15, n.y -15);
				rect.translateToView(p);
				g.drawImage(pathNode, p.x, p.y, null);
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
	
	private static BufferedImage createCircle(int diameter, Color c) {
		BufferedImage img = ImageProcess.createCompatiableImage(diameter, diameter);
		Graphics g = img.getGraphics();
		g.setColor(c);
		g.fillOval(0, 0, diameter, diameter);
		return img;
	}
	
	public static void createImages() {
		imagesCreated = true;
		pathNode = createCircle(30, Color.red);
		openNode = createCircle(40, Color.yellow);
		closedNode = createCircle(40, Color.magenta);
	}

	@Override
	public void performLogic(int frameDelta) {
		
	}
}
