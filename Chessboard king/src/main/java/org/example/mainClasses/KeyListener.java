package org.example.mainClasses;

import java.awt.event.KeyEvent;

import static org.example.help.Direction.*;

public class KeyListener implements java.awt.event.KeyListener {

    private GameLogic logic;

    public KeyListener(GameLogic logic) {
        this.logic = logic;
    }

    @Override
    public void keyTyped(KeyEvent e) {

    }

    @Override
    public void keyPressed(KeyEvent e) {
        switch (e.getKeyCode()){
            case KeyEvent.VK_W ->{
              logic.movePlayer(UP);
            }
            case KeyEvent.VK_S -> {
                logic.movePlayer(DOWN);
            }
            case KeyEvent.VK_A -> {
                logic.movePlayer(LEFT);
            }
            case KeyEvent.VK_D -> {
                logic.movePlayer(RIGHT);
            }
            case KeyEvent.VK_ESCAPE -> System.exit(0);
        }
    }

    @Override
    public void keyReleased(KeyEvent e) {
        switch (e.getKeyCode()){
            case KeyEvent.VK_A, KeyEvent.VK_W, KeyEvent.VK_S, KeyEvent.VK_D:{
                logic.getPlayer().setMoved(false);
            }
        }
    }
}
