package org.example.Entities;

import org.example.help.Type;
import org.example.graphics.Tile;

import java.awt.image.BufferedImage;
import java.util.ArrayList;
import java.util.Random;

public class Enemy extends Entity{

    private Type type;
    private ArrayList<Tile> legalMoves;
    private int moveDelay = 0;
    private int nextMove;
    private boolean moved = false;

    public Enemy(int tileX, int tileY, Type type, BufferedImage img) {
        super(tileX, tileY, img);

        this.legalMoves = new ArrayList<>();
        this.type = type;
    }

    public Type getType() {
        return type;
    }

    public int getRandomTile(){
        loadLegalMoves(type);
        checkLoadedMoves(legalMoves);

        Random random = new Random();
        return random.nextInt(legalMoves.size());
    }

    public void move(){
        setTileX(legalMoves.get(nextMove).getX());
        setTileY(legalMoves.get(nextMove).getY());
        moveDelay = 1;
    }

    private void checkLoadedMoves(ArrayList<Tile> loadedMoves){
        ArrayList<Tile> tilesToRemove = new ArrayList<>();

        for (Tile tile: loadedMoves){
            if (tile.getX() < 0 || tile.getX() > 7){
                tilesToRemove.add(tile);
            }
            if (tile.getY() < 0 || tile.getY() > 7){
                tilesToRemove.add(tile);
            }
        }

        for (Tile tile: tilesToRemove){
            loadedMoves.remove(tile);
        }
    }

    public int getNextMoveX(){
        return getLegalMoves().get(getNextMove()).getX();
    }

    public int getNextMoveY(){
        return getLegalMoves().get(getNextMove()).getY();
    }

    private void loadLegalMoves(Type type) {
        legalMoves = new ArrayList<>();

        int x = getTileX();
        int y = getTileY();

        switch (type) {
            case PAWN -> {
                legalMoves.add(new Tile(x + 1, y + 1));
                legalMoves.add(new Tile(x - 1, y - 1));
                legalMoves.add(new Tile(x - 1, y + 1));
                legalMoves.add(new Tile(x + 1, y - 1));

            }

            case KNIGHT -> {
                legalMoves.add(new Tile(x + 1, y + 2));
                legalMoves.add(new Tile(x + 1, y - 2));

                legalMoves.add(new Tile(x - 1, y + 2));
                legalMoves.add(new Tile(x - 1, y - 2));

                legalMoves.add(new Tile(x + 2, y + 1));
                legalMoves.add(new Tile(x - 2, y + 1));

                legalMoves.add(new Tile(x + 2, y - 1));
                legalMoves.add(new Tile(x - 2, y - 1));
            }

            case BISHOP -> {
                for (int multiplier = 1; multiplier <= 7; multiplier++) {
                    legalMoves.add(new Tile(x + multiplier, y + multiplier));
                    legalMoves.add(new Tile(x - multiplier, y + multiplier));
                    legalMoves.add(new Tile(x - multiplier, y - multiplier));
                    legalMoves.add(new Tile(x + multiplier, y - multiplier));
                }
            }

            case ROOK -> {
                for (int multiplierX = 1; multiplierX <= 7; multiplierX++) {

                    legalMoves.add(new Tile(x + multiplierX, y));
                    legalMoves.add(new Tile(x - multiplierX, y));
                }
                for (int multiplierY = 1; multiplierY <= 7; multiplierY++) {

                    legalMoves.add(new Tile(x, y - multiplierY));
                    legalMoves.add(new Tile(x, y + multiplierY));
                }
            }

            case QUEEN -> {
                for (int multiplierX = 1; multiplierX <= 7; multiplierX++) {

                    legalMoves.add(new Tile(x + multiplierX, y));
                    legalMoves.add(new Tile(x - multiplierX, y));

                }
                for (int multiplierY = 1; multiplierY <= 7; multiplierY++) {

                    legalMoves.add(new Tile(x, y - multiplierY));
                    legalMoves.add(new Tile(x, y + multiplierY));
                }

                for (int multiplier = 1; multiplier <= 7; multiplier++) {
                    legalMoves.add(new Tile(x + multiplier, y + multiplier));
                    legalMoves.add(new Tile(x - multiplier, y + multiplier));
                    legalMoves.add(new Tile(x - multiplier, y - multiplier));
                    legalMoves.add(new Tile(x + multiplier, y - multiplier));
                }
            }
        }
    }

    public int getNextMove() {
        return nextMove;
    }

    public void setNextMove(int nextMove) {
        this.nextMove = nextMove;
    }

    public ArrayList<Tile> getLegalMoves() {
        return legalMoves;
    }

    public int getMoveDelay() {
        return moveDelay;
    }

    public void setMoveDelay(int moveDelay) {
        this.moveDelay = moveDelay;
    }

    public void setMoved(boolean b) {
        this.moved = b;
    }

    public boolean moved() {
        return this.moved;
    }
}
