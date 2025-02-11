package hr.tvz.boggle.network;

import hr.tvz.boggle.model.GameMove;

public class SaveNewGameMoveThread extends GameMoveThread implements Runnable {

    private GameMove gameMove;

    public SaveNewGameMoveThread(GameMove newGameMove){
        this.gameMove = newGameMove;
    }

    @Override
    public void run() {
        saveNewGameMoveToFile(gameMove);
    }
}
