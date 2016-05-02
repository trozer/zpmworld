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
		return (blue == field || yellow == field || red == field || green == field);
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
		if (blue == field) {
			return yellow;
		} else if (yellow == field) {
			return blue;
		} else if (red == field) {
			return green;
		} else if (green == field) {
			return red;
		}
		return null;
	}
	
	public String getColor(Field field){
		if (blue == field) {
			return "kék";
		} else if (yellow == field) {
			return "sárga";
		} else if (red == field) {
			return "piros";
		} else if (green == field) {
			return "zöld";
		}
		return "";
	}
}
