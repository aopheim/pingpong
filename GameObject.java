package no.erikgustafsson.tiospill.pingpong;

import java.awt.Color;
import java.awt.Point;

public class GameObject {
	
	private Point position;		// Top left position of the object
	private Color[][] graphics;
	private int minX, maxX, minY, maxY; 		// defining the rectangle the object is allowed to move in
	private int width, height;		// width and height of the object
	private boolean isMovingLeft, isMovingRight, isMovingDiagonally, isMovingDown, isMovingUp;
	private PingPong game;
	
	
	
	public GameObject(Point point, Color[][] colors, int minX, int maxX, int minY, int maxY, PingPong game) {
		System.out.println("minX: " + minX + ", maxX: " + maxX + ", minY: " + minY + ", maxY: " + maxY);
		position = point;
		setGraphics(colors);
		// Setting valid area the object can move inside
		setMinX(minX);
		setMaxX(maxX);
		setMinY(minY);
		setMaxY(maxY);
		isMovingLeft = false;
		isMovingRight = false;
		height = colors.length;
		width = colors[1].length;
		this.game = game;
	}
	
	
	public int getHeight() {
		return this.height;
	}
	
	public int getWidth() {
		return this.width;
	}
	
	public boolean isMovingRight() {
		return isMovingRight;
	}
	
	public boolean isMovingLeft() {
		return isMovingLeft;
	}
	
	public void setMovingLeft(boolean isMovingLeft) {
		this.isMovingLeft = isMovingLeft;
		this.isMovingRight = !isMovingLeft;
	}
	
	public void setMovingRight(boolean isMovingRight) {
		this.isMovingRight = isMovingRight;
		this.isMovingLeft = !isMovingRight;
	}
	
	public void setMinX(int minX) {
		this.minX = minX;
	}

	public void setMaxX(int maxX) {
		this.maxX = maxX;
	}

	public void setMinY(int minY) {
		this.minY = minY;
	}

	public void setMaxY(int maxY) {
		this.maxY = maxY;
	}

	public void setGraphics(Color[][] colors ) {
		graphics = colors;
	}
	
	public Color[][] getGraphics(){
		return this.graphics;
	}
	
	public void up() {
		if (!(position.y - 1 < minY)) { 
			position.y = position.y - 1;		
			setMovingUp(true);
		}
	}
	
	public boolean isMovingDown() {
		return isMovingDown;
	}

	public void setMovingDown(boolean isMovingDown) {
		this.isMovingDown = isMovingDown;
		this.isMovingUp = !isMovingDown;
	}
	
	public boolean isMovingUp() {
		return isMovingUp;
	}
	
	public void setMovingUp(boolean isMovingUp) {
		this.isMovingUp = isMovingUp;
		this.isMovingDown = !isMovingUp;
	}

	public void down() {
		if (!(position.y + height > maxY)) {
			position.y = position.y + 1;		
			setMovingDown(true);
		}
		else {
			System.out.println("cant go down");
			System.out.println("height: " + height + "maxY: " + maxY + "current pos: " + position.y);
		}
	}
	
	public void left() {
		if (!(position.x - 1 < minX)) {
			position.x = position.x - 1;
			setMovingLeft(true);
		}
	}
	
	public void right() {
		if (!(position.x + width  > maxX)) {
			position.x = position.x + 1;
			setMovingRight(true);
		}
	}
	
	public void setPosition(Point point) {
		if (point.x < 0 || point.y < 0) {
			throw new IllegalArgumentException("Can not set negative position");
		}
		if (point.x >= 30 || point.y >= 30) {
			throw new IllegalArgumentException("Can not set position > 30");
		}
		this.position = point;
	}
	

	
	public Point getPosition() {
		return this.position;
	}
}
