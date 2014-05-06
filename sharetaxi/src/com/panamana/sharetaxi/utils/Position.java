package com.panamana.sharetaxi.utils;

public class Position {

	private float x_pos;
	private float y_pos;
	private float z_pos;
	
	public float getX() {
		return x_pos;
	}

	public void setX (float x_pos) {
		this.x_pos = x_pos;
	}

	public float getY () {
		return y_pos;
	}

	public void setY (float y_pos) {
		this.y_pos = y_pos;
	}

	public float getZ () {
		return z_pos;
	}

	public void setZ (float z_pos) {
		this.z_pos = z_pos;
	}

	public Position(float x_pos, float y_pos, float z_pos) {
		super();
		this.x_pos = x_pos;
		this.y_pos = y_pos;
		this.z_pos = z_pos;
	}
	
	public static Position addPositions (Position ... positions) {
		float x_pos = 0;
		float y_pos = 0;
		float z_pos = 0;
		for (Position position: positions) {
			x_pos += position.getX();
			y_pos += position.getY();
			z_pos += position.getZ();
		}
		Position result = new Position(x_pos, y_pos, z_pos);
		return result;
	}

	public static float distancePfromVectorAB(Position p, Position a, Position b) {
		float distance;
		distance = (DirectionalVector.crossProduct(
				DirectionalVector.calcDirection(a, p),
				DirectionalVector.calcDirection(b, p))).getVectorSize()
				/ (DirectionalVector.calcDirection(a, b).getVectorSize());
		return distance;
	}

	@Override
	public String toString() {
		return "Position [x_pos=" + x_pos + ", y_pos=" + y_pos + ", z_pos="
				+ z_pos + "]";
	}
	
	public boolean equals(Position pos) {
		if (pos == null) {
			return false;
		}
		return (this.getX() == pos.getX() &&
			this.getY() == pos.getY() &&
			this.getZ() == pos.getZ()) ;
	}
}
