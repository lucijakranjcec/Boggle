package hr.tvz.boggle.network;

import hr.tvz.boggle.model.GameMove;
import hr.tvz.boggle.util.GameMoveUtils;

public abstract class GameMoveThread {

    private static Boolean gameMoveFileAccessInProgress = false;

    protected synchronized void saveNewGameMoveToFile(GameMove gameMove) {
        while(gameMoveFileAccessInProgress) {
            try {
                wait();
            } catch (InterruptedException e) {
                throw new RuntimeException(e);
            }
        }

        gameMoveFileAccessInProgress = true;

        GameMoveUtils.saveNewGameMove(gameMove);

        gameMoveFileAccessInProgress = false;

        notifyAll();
    }

    protected synchronized GameMove getLastGameMoveFromFile() {
        while(gameMoveFileAccessInProgress) {
            try {
                wait();
            } catch (InterruptedException e) {
                throw new RuntimeException(e);
            }
        }

        gameMoveFileAccessInProgress = true;

        GameMove lastGameMove = GameMoveUtils.getNewGameMove();

        gameMoveFileAccessInProgress = false;

        notifyAll();

        return lastGameMove;
    }

}
