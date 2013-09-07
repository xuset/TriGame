package tSquare.paths;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.image.BufferedImage;
import java.util.Collection;

import tSquare.game.GameBoard;
import tSquare.imaging.ImageProccess;

public class PathDrawer {
	public static GameBoard gameBoard;
	public static Path path;
	public static Collection<Node> openNodes;
	public static Collection<Node> closedNodes;
	
	private static BufferedImage pathNode;
	private static BufferedImage openNode;
	private static BufferedImage closedNode;
	
	public static void createImages() {
		pathImage();
		openImage();
		closedImage();
	}
	
	private static void pathImage() {
		pathNode = ImageProccess.createCompatiableImage(30, 30);
		Graphics g = pathNode.getGraphics();
		g.setColor(Color.red);
		g.fillOval(0, 0, 30, 30);
		g.dispose();
	}
	
	private static void openImage() {
		openNode = ImageProccess.createCompatiableImage(40, 40);
		Graphics g = openNode.getGraphics();
		g.setColor(Color.yellow);
		g.fillOval(0, 0, 40, 40);
		g.dispose();
	}
	
	private static void closedImage() {
		closedNode = ImageProccess.createCompatiableImage(40, 40);
		Graphics g = closedNode.getGraphics();
		g.setColor(Color.magenta);
		g.fillOval(0, 0, 40, 40);
		g.dispose();
	}
	
	public static void draw() {
		if (openNodes != null) {
			for (Node n : openNodes) {
				gameBoard.draw(openNode, n.relativeX - 20, n.relativeY - 20);
			}
		}
		
		if (closedNodes != null) {
			for (Node n : closedNodes) {
				gameBoard.draw(closedNode, n.relativeX - 20, n.relativeY - 20);
			}
		}
		
		if (path != null) {
			for (Node n : path.steps) {
				gameBoard.draw(pathNode, n.relativeX - 15, n.relativeY - 15);
			}
		}
	}
}
