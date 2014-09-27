package com.panamana.sharetaxi.lines.objects;

import com.panamana.sharetaxi.lines.LINES;

/**
 * this is the object that holds the line and the direction of the line
 * @author naama
 *
 */
public class LineDirectionPair {
	public String line;
	public String direction;

	public LineDirectionPair(String line, String direction) {
		super();
		this.line = line;
		this.direction = direction;
	}
	
	public static LineDirectionPair LINE4NORTH = new LineDirectionPair(LINES.LINE4, "North");
	public static LineDirectionPair LINE4SOUTH = new LineDirectionPair(LINES.LINE4, "South");
	public static LineDirectionPair LINE4ANORTH = new LineDirectionPair(LINES.LINE4a, "North");
	public static LineDirectionPair LINE4ASOUTH = new LineDirectionPair(LINES.LINE4a, "South");
	public static LineDirectionPair LINE5NORTH = new LineDirectionPair(LINES.LINE5, "North");
	public static LineDirectionPair LINE5SOUTH = new LineDirectionPair(LINES.LINE5, "South");
	
	public String getLine() {
		return line;
	}

	public String getDirection() {
		return direction;
	}
	
	public static LineDirectionPair getPair(String line,String direction) {
		LineDirectionPair otherPair = new LineDirectionPair(line, direction);
		if (otherPair.equals(LINE4NORTH)) return LINE4NORTH;
		if (otherPair.equals(LINE4SOUTH)) return LINE4SOUTH;
		if (otherPair.equals(LINE4ANORTH)) return LINE4ANORTH;
		if (otherPair.equals(LINE4ASOUTH)) return LINE4ASOUTH;
		if (otherPair.equals(LINE5NORTH)) return LINE5NORTH;
		if (otherPair.equals(LINE5SOUTH)) return LINE5SOUTH;
		return null;
	}
	
	public Boolean equals(LineDirectionPair pair) {
		if (pair.getLine().equals(this.line) &&
				pair.getDirection().equals(this.direction)) return true;
		return false;
	}
}