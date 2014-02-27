package net.xuset.tSquare.math;


public class DegreeMath {
	
	public static double roundDegreeToNearestFourthCircle(double degree) {
		degree = simplifyDegrees(degree);
		int fourths = (int) (degree / 90);
		return fourths * 90.0;
	}
	
	public static int getQuadrant(double x, double y) {
		return DegreeMath.getQuadrant(x, y, 0.0, 0.0);
	}
	public static int getQuadrant(double x, double y, double relativeX, double relativeY) {
		if (x - relativeX > 0) {
			if (y - relativeY > 0)
				return 4;
			if (y - relativeY < 0)
				return 1;
		}
		if (x - relativeX < 0) {
			if (y - relativeY > 0)
				return 3;
			if (y - relativeY < 0)
				return 2;
		}
		return 0;
	}
	
	public static double simplifyDegrees(double degrees) {
		int multiple = (int)degrees / 360;
		degrees = degrees - multiple * 360;
		if (degrees < 0)
			degrees = 360 + degrees;
		return degrees;
	}
	
	/*public static Point randomEdgeCordinates() { /// UNUSED [pending deletion]
		int random = (int)(Math.random()*5);
		Point p = new Point(0,0);
		if (random == 1) { // top
			p.x = (int)(Math.random()*Game.gameBoard.width);
		}
		if (random == 2) { //right
			p.x = Game.gameBoard.width;
			p.y = (int)(Math.random()*Game.gameBoard.height);
		}
		if (random == 3) { // bottom
			p.x = (int)(Math.random()*Game.gameBoard.width);
			p.y = Game.gameBoard.height;
		}
		if (random == 4) { //left
			p.y = (int)(Math.random()*Game.gameBoard.height);
		}
		return p;
	}*/
	
	/*private int[] getSortedIndexs(ArrayList<Node> list) {  //swap sort
	int[] listArray = new int[list.size()];
	int[] indexArray = new int[list.size()];
	for (int x = 0; x < list.size(); x++) 
		indexArray[x] = x;
	for (int x = 0; x < list.size(); x++)
		listArray[x] = list.get(x).f;
	for (int step = 0; step < list.size() - 1; step++) {
		int smallestValue = listArray[step];
		int smallestValueIndex = step;
		for (int i = step + 1; i < list.size(); i++) {
			if (listArray[i] < smallestValue) {
				smallestValue = listArray[i];
				smallestValueIndex = i;
			}
		}
		int tempA = smallestValue;
		int tempB = listArray[step];
		int tempIndexA = smallestValueIndex;
		int tempIndexB = step;
		listArray[tempIndexA] = tempB;
		listArray[tempIndexB] = tempA;
		tempA = indexArray[tempIndexA];
		tempB = indexArray[tempIndexB];
		indexArray[tempIndexA] = tempB;
		indexArray[tempIndexB] = tempA;
	}
	return indexArray;
}*/
}
