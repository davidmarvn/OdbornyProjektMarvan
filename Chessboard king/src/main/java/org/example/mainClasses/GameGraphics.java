package org.example.mainClasses;

import org.example.Entities.Enemy;
import org.example.Entities.Player;
import org.example.graphics.ChessBoard;

import javax.swing.*;
import java.awt.*;

public class GameGraphics extends JFrame {

    public GameLogic logic;

    private Player player;
    private ChessBoard chessBoard;
    private Collectable collectable;
    private KeyListener keyListener;

    public GameGraphics(GameLogic logic) {
        this.logic = logic;
        this.keyListener = new KeyListener(logic);
        this.chessBoard = logic.getChessBoard();
        this.collectable = logic.getCollectable();
        this.player = logic.getPlayer();

        setSize(logic.getScreenSize());
        setResizable(false);
        setLocationRelativeTo(null);
        add(new Draw());

        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setVisible(true);
        addKeyListener(keyListener);
        addKeyListener(this.logic);
    }

    public void updateGraphics(GameLogic logic) {
        this.logic = logic;
        repaint();
    }

    public void drawScore(Graphics g){

        g.setColor(Color.BLACK);
        g.setFont(new Font("Arial", Font.BOLD, 120));
        g.drawString(String.valueOf(logic.score), 0, 100);
    }

    public void drawCollectable(Graphics g){
        int centreX = chessBoard.getX() + (collectable.getX() * logic.TILE_LENGTH) + logic.TILE_LENGTH / 4;
        int centreY =  chessBoard.getY() + (collectable.getY() * logic.TILE_LENGTH) + logic.TILE_LENGTH / 4;

        g.setColor(collectable.getColor());
        g.fillOval(centreX, centreY, logic.TILE_LENGTH / 2, logic.TILE_LENGTH / 2);
    }

    public void drawChessBoard(Graphics g){
        g.drawImage(chessBoard.getImage(), chessBoard.getX(), chessBoard.getY(), chessBoard.getWidth(), chessBoard.getWidth(), null);
    }

    public void drawPlayer(Graphics g){
        g.drawImage(player.getImage(), chessBoard.getX() + (player.getTileX() * logic.TILE_LENGTH), chessBoard.getY() + (player.getTileY() * logic.TILE_LENGTH), logic.TILE_LENGTH, logic.TILE_LENGTH,null);
    }

    private void drawEnemies(Graphics g) {
        for (Enemy enemy: logic.getEnemies()){

            g.drawImage(enemy.getImage(), chessBoard.getX() + (enemy.getTileX() * logic.TILE_LENGTH), chessBoard.getY() + (enemy.getTileY() * logic.TILE_LENGTH), logic.TILE_LENGTH, logic.TILE_LENGTH, null);
        }
    }

    private void drawNextMoves(Graphics g){
        for (Enemy enemy: logic.getEnemies()){
            if (enemy.getMoveDelay() > logic.FINAL_UPS  * logic.getGameSpeedMultiplier()){

                int nextMoveX = enemy.getNextMoveX();
                int nextMoveY = enemy.getNextMoveY();

                g.setColor(Color.RED);
                g.fillRect(chessBoard.getX() + nextMoveX  * logic.TILE_LENGTH, chessBoard.getY() + nextMoveY  * logic.TILE_LENGTH, logic.TILE_LENGTH, logic.TILE_LENGTH);
            }
        }
    }

    public class Draw extends JPanel{

        public void paintComponent(Graphics g) {
            super.paintComponent(g);

            if (logic.isGameCanRun()) {
                drawChessBoard(g);
                drawNextMoves(g);
                drawCollectable(g);
                drawEnemies(g);
                drawPlayer(g);
                drawScore(g);
            }else {
                drawMenu(g);
                drawDifficulty(g);
            }
        }
    }

    private void drawDifficulty(Graphics g) {
        g.setColor(Color.BLACK);
        g.setFont(new Font("Franchise", Font.BOLD, 90));
        g.drawString(String.valueOf(logic.getDifficulty()), 0, 75);
    }

    private void drawMenu(Graphics g) {
        g.drawImage(logic.getMenu().getImg(), 0, 0, logic.getScreenSize().width, logic.getScreenSize().height, this);
    }
}
