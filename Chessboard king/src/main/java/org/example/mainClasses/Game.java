package org.example.mainClasses;

public class Game {

    private final GameLogic logic;
    private final  GameGraphics gameGraphics;

    public Game() {

        this.logic = new GameLogic();
        logic.initialize();
        this.gameGraphics = new GameGraphics(logic);

        run();
    }


    public void run() {
        double timePerFrame = logic.SECOND_IN_NANO / logic.FINAL_FPS;
        double timePerUpdate = logic.SECOND_IN_NANO / logic.FINAL_UPS;

        long previousTime = System.nanoTime();
        double deltaU = 0;
        double deltaF = 0;

        int frames = 0;
        int updates = 0;
        long lastCheck = System.currentTimeMillis();

        while (true) {
            long currentTime = System.nanoTime();

            deltaU += (currentTime - previousTime) / timePerUpdate;
            deltaF += (currentTime - previousTime) / timePerFrame;

            previousTime = currentTime;

            if (deltaU >= 1) {
                if (logic.isGameCanRun()) {
                    logic.update();
                    updates++;
                }
                deltaU--;
            }

            if (deltaF >= 1) {
                gameGraphics.updateGraphics(logic);
                frames++;
                deltaF--;

            }

            if (System.currentTimeMillis() - lastCheck >= logic.SECOND_IN_MILI) {
                lastCheck = System.currentTimeMillis();
                System.out.println(frames + "      " + updates);
                frames = 0;
                updates = 0;
            }
        }
    }
}
