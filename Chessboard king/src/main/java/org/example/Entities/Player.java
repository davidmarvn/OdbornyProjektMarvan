package org.example.Entities;

import org.example.help.Direction;

import java.awt.image.BufferedImage;

public class Player extends Entity{

    private boolean moved = false;


    public Player(int tileX, int tileY, BufferedImage img) {
        super(tileX, tileY, img);
    }

    public boolean isMoved() {
        return moved;
    }

    public void setMoved(boolean moved) {
        this.moved = moved;
    }

    public boolean checkMove(Direction d){
        switch (d){
            case RIGHT:
                if (!isMoved()){
                    if (getTileX() + 1 <= 7){
                        return true;
                    }else {
                        return false;
                    }
                }
            case LEFT:
                if (!isMoved()){
                    if (getTileX() - 1 >= 0){
                        return true;
                    }else {
                        return false;
                    }
                }
            case UP:
                if (!isMoved()){
                    if (getTileY() - 1 >= 0){
                        return true;
                    }else {
                        return false;
                    }
                }
            case DOWN:
                if (!isMoved()){
                    if (getTileY() + 1 <= 7){
                        return true;
                    }else {
                        return false;
                    }
                }

        }
        return false;
    }
    public void move(Direction d){
        switch (d){
            case UP -> setTileY(getTileY() - 1);
            case DOWN -> setTileY(getTileY() + 1);
            case LEFT -> setTileX(getTileX() - 1);
            case RIGHT -> setTileX(getTileX() + 1);
        }
    }
}
