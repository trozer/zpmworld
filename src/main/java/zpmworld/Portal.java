package zpmworld;

import java.awt.Color;

public class Portal {

	private Field blue;
	private Field yellow;
	private Field red;
	private Field green;

	public Portal() {

	}

	public Field getBlue() {
		return blue;
	}

	public void setBlue(Field blue) {
		this.blue = blue;
	}

	public Field getYellow() {
		return yellow;
	}

	public void setYellow(Field yellow) {
		this.yellow = yellow;
	}

	public Field getRed() {
		return red;
	}

	public void setRed(Field red) {
		this.red = red;
	}

	public Field getGreen() {
		return green;
	}

	public void setGreen(Field green) {
		this.green = green;
	}

	public boolean amIPortal(Field field) {
        if(field == null)   return false;
        if(blue != null && blue.equals(field)) return true;
        if(yellow != null && yellow.equals(field)) return true;
        if(red != null && red.equals(field)) return true;
        if(green != null && green.equals(field)) return true;
		return false;
	}

	public void createPortal(Field field, Color color) {
		if (color == Color.BLUE) {
			blue = field;
		} else if (color == Color.YELLOW) {
			yellow = field;
		} else if (color == Color.RED) {
			red = field;
		} else if (color == Color.GREEN) {
			green = field;
		}
	}

	public Field getPair(Field field) {
		if (blue != null && blue.equals(field)) {
			return yellow;
		} else if (yellow != null && yellow.equals(field)) {
			return blue;
		} else if (red != null && red.equals(field)) {
			return green;
		} else if (green != null && green.equals(field)) {
			return red;
		}
		return null;
	}
	
	public Color getColor(Field field){
		if (blue != null && blue.equals(field)) {
			return Color.BLUE;
		} else if (yellow != null && yellow.equals(field)) {
			return Color.YELLOW;
		} else if (red != null && red.equals(field)) {
			return Color.RED;
		} else if (green != null && green.equals(field)) {
			return Color.GREEN;
		}
		return null;
	}
}
