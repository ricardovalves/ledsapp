package com.led.home.db;

public class Color {

    private int color;
    private String colorRepresentation;
    private int id;

    private boolean isDefaultColor;

    public Color(int color, String colorRepresentation, boolean isDefaultColor) {
        this(-1, color, colorRepresentation, isDefaultColor);
    }
    public Color(int id, int color, String colorRepresentation, boolean isDefaultColor) {
        this.id = id;
        this.color = color;
        this.colorRepresentation = colorRepresentation;

        this.isDefaultColor = isDefaultColor;
    }

    public String getColorRepresentation() {
        return colorRepresentation;
    }

    public void setColorRepresentation(String colorRepresentation) {
        this.colorRepresentation = colorRepresentation;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getColor() {
        return color;
    }

    public void setColor(int color) {
        this.color = color;
    }

    public boolean getIsDefaultColor() {
        return isDefaultColor;
    }

    public void setIsDefaultColor(boolean isDefaultColor) {
        this.isDefaultColor = isDefaultColor;
    }
}
