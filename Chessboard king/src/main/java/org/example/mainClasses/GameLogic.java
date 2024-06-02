package org.example.mainClasses;

import org.example.graphics.Menu;
import org.example.help.Difficulty;
import org.example.Entities.Enemy;
import org.example.Entities.Player;
import org.example.graphics.ChessBoard;
import org.example.help.Direction;
import org.example.help.Type;

import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class GameLogic implements KeyListener {

    final double SECOND_IN_NANO = 1000000000.0;
    final double SECOND_IN_MILI = 1000.0;

    final int FINAL_FPS = 120;
    final int FINAL_UPS = 200;
    final int BOARD_MIN = 0;
    final int BOARD_MAX = 7;
    final int NUMBER_OF_TILES = 8;
    final int GAME_GRAPHIC_MULTIPLIER = 3;
    final int TILE_IN_PIXEL = 28;
    final int TILE_LENGTH = TILE_IN_PIXEL * GAME_GRAPHIC_MULTIPLIER;

    private Player player;
    private ArrayList<Enemy> enemies;
    private ArrayList<Type> types;
    private org.example.graphics.Menu menu;
    private Random random;
    private ChessBoard chessBoard;
    private Difficulty difficulty = Difficulty.EASY;
    private Dimension screenSize;
    private Collectable collectable;

    private int spawnDelay;
    private int collectableSpawnDelay;
    private int enemyLimit;
    private int fastSpawnEnemies;
    private double gameSpeedMultiplier;
    private double scoreMultiplier;
    public double score;
    private boolean gameCanRun = false;




    public GameLogic() {
        initialize();

    }

    public void initialize(){
        menu = new org.example.graphics.Menu(loadImage("menuImage.png"));
        chessBoard = new ChessBoard(loadImage("background.png"));
        screenSize = Toolkit.getDefaultToolkit().getScreenSize();
        chessBoard.setWidth(TILE_LENGTH * NUMBER_OF_TILES);
        chessBoard.setX((int) ((screenSize.getWidth() - chessBoard.getWidth()) / 2));
        chessBoard.setY((int) ((screenSize.getHeight() - chessBoard.getWidth()) / 2));

        random = new Random();
        enemies = new ArrayList<>();
        types = new ArrayList<>();
        player = new Player(0,0, assignImage(1, 1));
        collectable = new Collectable(random.nextInt(8), random.nextInt(8));

        loadTypes();
        setImages();
    }

    private void loadDifficultySettings(Difficulty difficulty) {
        switch (difficulty){
            case EASY ->{
                gameSpeedMultiplier = 1.8;
                scoreMultiplier = 1.0;
                enemyLimit = 30;
            }
            case NORMAL -> {
                gameSpeedMultiplier = 1.4;
                scoreMultiplier = 1.5;
                enemyLimit = 40;
            }
            case HARD -> {
                gameSpeedMultiplier = 1.2;
                scoreMultiplier = 2.0;
                enemyLimit = 50;
            }
        }
        fastSpawnEnemies = enemyLimit / 5;
    }

    private void loadTypes() {

        addTypes(Type.PAWN, 4);
        addTypes(Type.KNIGHT, 3);
        addTypes(Type.BISHOP, 3);
        addTypes(Type.ROOK, 2);
        addTypes(Type.QUEEN, 2);
    }

    public void update(){
        checkMate();

        int enemyCount = enemies.size();
        if (enemyCount < fastSpawnEnemies){
            spawnEnemy();
        }
        if (enemyCount < enemyLimit){
            spawnEnemy();
        }
        spawnCollectable();
        moveEnemies();

    }

    private void spawnCollectable() {
        collectableSpawnDelay++;

        if (collectableSpawnDelay >= 3 * FINAL_UPS || collected()){
            collectableSpawnDelay = 0;

            int newX = random.nextInt(8);
            int newY = random.nextInt(8);
            int value = random.nextInt(3);

            collectable.setX(newX);
            collectable.setY(newY);

            switch (value){
                case 0:
                    collectable.setValue(3);
                    collectable.setColor(Color.GREEN);
                    break;

                case 1:
                    collectable.setValue(5);
                    collectable.setColor(Color.BLUE);
                    break;

                case 2:
                    collectable.setValue(10);
                    collectable.setColor(Color.orange);
                    break;
            }
        }
    }

    private boolean collected(){
        if (player.getTileX() == collectable.getX() && player.getTileY() == collectable.getY()){
            addScore(collectable.getValue());
            return true;
        }
        return false;
    }

    private void moveEnemies() {
        for (Enemy enemy: enemies){
            enemy.setMoveDelay(enemy.getMoveDelay() + 1);

            if (enemy.getMoveDelay() >= FINAL_UPS  * gameSpeedMultiplier && !enemy.moved() && enemy.getMoveDelay() < FINAL_UPS * 2  * gameSpeedMultiplier){
                enemy.setNextMove(enemy.getRandomTile());
                enemy.setMoved(true);
            }

            if (enemy.getMoveDelay() >= 2 * FINAL_UPS  * gameSpeedMultiplier){
                enemy.move();
                enemy.setMoved(false);
            }
        }
    }

    private void spawnEnemy() {
        spawnDelay += 1;
        if (spawnDelay >= 1.5 * FINAL_UPS * gameSpeedMultiplier){
            spawnDelay = 0;
            spawnNewEnemy();
        }
    }

    public void addTypes(Type type, int count){
        for (int i = 0; i < count; i++)
            types.add(type);
    }

    public void movePlayer(Direction d){

        if (checkCollision(d)){
            if (player.checkMove(d)){
                player.move(d);
                score += 1 * scoreMultiplier;
                player.setMoved(true);
            }
        }
    }

    public void spawnNewEnemy() {
        int x = getRandomTileCoord();
        int y = getRandomTileCoord();
        boolean canSpawn = true;

        Type type = getRandomType();
        Enemy e = new Enemy(x, y, type, imageByType(type));

        for (Enemy enemy: enemies){
            if (!enemies.isEmpty()){
                if (e.getTileX() == enemy.getTileX() && e.getTileY() == enemy.getTileY()){
                    canSpawn = false;
                } else if (e.getTileX() == player.getTileX() && e.getTileY() == player.getTileY()) {
                    canSpawn = false;
                } else {
                    canSpawn = true;
                }
            }
        }
        if (canSpawn){
            enemies.add(e);
        }else {
            spawnNewEnemy();
        }
        setImages();
    }

    public void checkMate(){
        for (Enemy enemy: enemies){
            if (enemy.getTileX() == player.getTileX() && enemy.getTileY() == player.getTileY()){
                setGameCanRun(false);
                enemies = new ArrayList<>();
                score = 0;
            }
        }
    }

    public boolean checkCollision(Direction d){
        for (Enemy enemy: enemies){
            switch (d){
                case UP ->{
                    if (player.getTileX() == enemy.getTileX()){
                        if (player.getTileY() - 1 == enemy.getTileY()){
                            return false;
                        }
                    }
                }
                case DOWN -> {
                    if (player.getTileX() == enemy.getTileX()) {
                        if (player.getTileY() + 1 == enemy.getTileY()) {
                            return false;
                        }
                    }
                }
                case LEFT -> {
                    if (player.getTileY() == enemy.getTileY()) {
                        if (player.getTileX() - 1 == enemy.getTileX()) {
                            return false;
                        }
                    }
                }
                case RIGHT -> {
                    if (player.getTileY() == enemy.getTileY()) {
                        if (player.getTileX() + 1 == enemy.getTileX()) {
                            return false;
                        }
                    }
                }
            }
        }
        return true;
    }

    public void setImages(){
        player.setImage(assignImage(1, 1));

        for (Enemy enemy: enemies) {
            switch (enemy.getType()) {
                case PAWN -> enemy.setImage(assignImage(0, 0));
                case KNIGHT -> enemy.setImage(assignImage(4, 0));
                case BISHOP -> enemy.setImage(assignImage(0, 1));
                case ROOK -> enemy.setImage(assignImage(2, 0));
                case QUEEN -> enemy.setImage(assignImage(1, 0));
            }
        }
    }

    public BufferedImage assignImage(int positionX, int positionY){
        BufferedImage sprite = loadImage("mainSprite.png");
        BufferedImage img = null;

        for (int j = 0; j <= positionY; j++){
            for (int i = 0; i <= positionX; i++){
                img = sprite.getSubimage(TILE_IN_PIXEL * i, TILE_IN_PIXEL * j, TILE_IN_PIXEL, TILE_IN_PIXEL);
            }
        }
        return img;
    }

    public  BufferedImage loadImage(String imageName){
        BufferedImage img;
        try{
            img = ImageIO.read((getClass().getResource("/" + imageName)));
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        return img;
    }

    private BufferedImage imageByType(Type type) {
        switch (type) {
            case PAWN -> {
                return assignImage(0, 0);
            }
            case KNIGHT -> {
                return assignImage(4, 0);
            }
            case BISHOP -> {
                return assignImage(0, 1);
            }
            case ROOK -> {
                return assignImage(2, 0);
            }
            case QUEEN -> {
                return assignImage(1, 0);
            }
        }
        return null;
    }

    @Override
    public void keyTyped(KeyEvent e) {

    }

    public void keyPressed(KeyEvent e) {
        if (!isGameCanRun()){
            switch (e.getKeyCode()){
                case KeyEvent.VK_SPACE -> {
                    loadDifficultySettings(difficulty);
                    setGameCanRun(true);
                }
                case KeyEvent.VK_A -> setDifficulty(Difficulty.EASY);
                case KeyEvent.VK_S -> setDifficulty(Difficulty.NORMAL);
                case KeyEvent.VK_D -> setDifficulty(Difficulty.HARD);
            }
        }
    }

    @Override
    public void keyReleased(KeyEvent e) {

    }

    public Difficulty getDifficulty() {
        return difficulty;
    }

    public void setDifficulty(Difficulty difficulty) {
        this.difficulty = difficulty;
    }
    public void addScore(int addedScore){
        score += addedScore;
    }

    private Type getRandomType() {
        return types.get(random.nextInt(types.size()));
    }


    public int getRandomTileCoord(){
        return random.nextInt(BOARD_MIN, BOARD_MAX);
    }

    public ChessBoard getChessBoard() {
        return chessBoard;
    }

    public List<Enemy> getEnemies() {
        return enemies;
    }

    public Dimension getScreenSize() {
        return this.screenSize;
    }

    public Collectable getCollectable() {
        return collectable;
    }

    public boolean isGameCanRun() {
        return gameCanRun;
    }

    public Menu getMenu() {
        return menu;
    }
    public Player getPlayer() {
        return player;
    }
    public void setGameCanRun(boolean gameCanRun) {
        this.gameCanRun = gameCanRun;
    }

    public double getGameSpeedMultiplier() {
        return gameSpeedMultiplier;
    }
}
