package com.panamana.sharetaxi.model.utils;

public class DirectionalVector {

	private float x_direction;
	private float y_direction;
	private float z_direction;
	
	public DirectionalVector(float x_direction, float y_direction, float z_direction) {
		super();
		this.x_direction = x_direction;
		this.y_direction = y_direction;
		this.z_direction = z_direction;
	}

	public static DirectionalVector crossProduct(DirectionalVector a, DirectionalVector b) {
		float cx = a.getY_direction()*b.getZ_direction()-a.getZ_direction()*b.getY_direction();
		float cy = a.getX_direction()*b.getZ_direction()-a.getZ_direction()*b.getX_direction();
		float cz = a.getX_direction()*b.getY_direction()-a.getY_direction()*b.getX_direction();
		DirectionalVector c = new DirectionalVector(cx, cy, cz);
//		c = c.NormalizeVector();
		return c;
	}
	
	public static float scalarProduct (DirectionalVector a, DirectionalVector b) {
		float xSum = a.getX_direction() * b.getX_direction() ;
		float ySum = a.getY_direction() * b.getY_direction() ;
		float zSum = a.getZ_direction() * b.getZ_direction() ;
		return xSum + ySum + zSum;
	}
	
	@Override
	public String toString() {
		return "DirectionalVector [x_direction=" + x_direction
				+ ", y_direction=" + y_direction + ", z_direction="
				+ z_direction + "]";
	}

	public Position stretchVector (float scalar) {
		Position result = new Position(this.getX_direction() * scalar, 
									   this.getY_direction() * scalar,
									   this.getZ_direction() * scalar);
		return result;
	}
	
	/*
	 * calculates the direction from a to b (b-a)
	 */
	public static DirectionalVector calcDirection(Position a, Position b) {
		DirectionalVector c = null; 
		if ( a!= null && b!=null) {
			float cx = b.getX()-a.getX();
			float cy = b.getY()-a.getY();
			float cz = b.getZ()-a.getZ();
			c = new DirectionalVector(cx, cy, cz);
		}
		return c;
	}
	
	public float getVectorSize() {
		float tx=this.getX_direction();
		float ty=this.getY_direction();
		float tz=this.getZ_direction();
		float norm=(float) Math.sqrt(tx*tx+ty*ty+tz*tz);
		return norm;
	}
	
	public DirectionalVector NormalizeVector() {
		float norm=this.getVectorSize();
		float tx_new = this.getX_direction()/norm;
		float ty_new = this.getY_direction()/norm;
		float tz_new = this.getZ_direction()/norm;
		DirectionalVector result = new DirectionalVector(tx_new, ty_new, tz_new);
		return result;
		
	}
	
	public float getX_direction() {
		return x_direction;
	}

	public void setX_direction(float x_direction) {
		this.x_direction = x_direction;
	}

	public float getY_direction() {
		return y_direction;
	}

	public void setY_direction(float y_direction) {
		this.y_direction = y_direction;
	}

	public float getZ_direction() {
		return z_direction;
	}

	public void setZ_direction(float z_direction) {
		this.z_direction = z_direction;
	}

	public float getAngleFromNorth() {
		return (float)(Math.acos(scalarProduct(
				new DirectionalVector(x_direction, y_direction, 0).NormalizeVector(), 
				new DirectionalVector(1, 0, 0)))); 
	}
	
	
	
}
