package org.example.Entities;
import java.awt.image.BufferedImage;

public class Entity {
    ;
    private BufferedImage image;
    private int tileX, tileY;

    public Entity(int tileX, int tileY, BufferedImage img) {
        this.image = img;
        this.tileX = tileX;
        this.tileY = tileY;
    }

    public BufferedImage getImage() {
        return image;
    }

    public void setImage(BufferedImage image) {
        this.image = image;
    }

    public int getTileX() {
        return tileX;
    }

    public void setTileX(int tileX) {
        this.tileX = tileX;
    }

    public int getTileY() {
        return tileY;
    }

    public void setTileY(int tileY) {
        this.tileY = tileY;
    }
}
