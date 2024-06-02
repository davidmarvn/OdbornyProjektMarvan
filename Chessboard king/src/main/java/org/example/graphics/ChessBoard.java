package org.example.graphics;

import java.awt.image.BufferedImage;


public class ChessBoard {

    private BufferedImage image;
    private int x;
    private int y;
    private int width;


    public ChessBoard(BufferedImage image) {
        this.image = image;

    }
    public BufferedImage getImage() {
        return image;
    }

    public int getX() {
        return x;
    }

    public int getY() {
        return y;
    }

    public int getWidth() {
        return width;
    }

    public void setX(int x) {
        this.x = x;
    }

    public void setY(int y) {
        this.y = y;
    }

    public void setWidth(int width) {
        this.width = width;
    }
}
