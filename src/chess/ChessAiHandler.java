/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package chess;

import static java.lang.Thread.sleep;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author ashir basu
 */
public class ChessAiHandler extends Thread{
 
    pieceColor moveOn;
    chessPlayer mainPlayer;
    int depth;
    int numThreads;
    ChessAI[] aiThread;
    chessPlayer threadPlayer[];
        candidateMove[] moveList;
        candidateMove bestMove;
        int numMoves = 0;
        ChessAiHandler(pieceColor c, chessPlayer main, int depth, int numThreads){
        moveOn = c;
        mainPlayer=main;
        this.depth = depth;
        this.numThreads = numThreads;
        aiThread = new ChessAI[numThreads];
        threadPlayer = new chessPlayer[numThreads];
        
        chessPlayerCreator[] temp = new chessPlayerCreator[numThreads];
        for (int i=0;i<numThreads;i++){
            (temp[i] = new chessPlayerCreator(threadPlayer, mainPlayer, i, depth)).start();
        }
        for (int i=0;i<numThreads; i++){
            try {
                temp[i].join();
            } catch (InterruptedException ex) {
                Logger.getLogger(ChessAiHandler.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
    }
    public void run (){
        while (true){
            try {
                sleep(1000);
            } catch (InterruptedException ex) {
                Logger.getLogger(ChessAI.class.getName()).log(Level.SEVERE, null, ex);
            }
            if (mainPlayer.currentBoard.currentTurn == moveOn){
                aiMove();
                mainPlayer.resetColors();
            }
        }
    }
    
    void aiMove(){
        moveList = new candidateMove[numThreads];
        bestMove = new candidateMove();
        bestMove.score=-9999;
        mainPlayer.calculateAttacks();
        for (int i=0;i<numThreads;i++){
            moveList[i] = new candidateMove();
            moveList[i].score = -9999.0;
        }
        for (cell[] j:mainPlayer.currentBoard.cellGrid){
            for (cell k: j){
                if (k.currentPiece==null||k.currentPiece.pieceC!=mainPlayer.currentBoard.currentTurn)
                        continue;
                cell[] attacks = k.attacking.toArray(new cell[k.attacking.size()]);
                for (cell i:attacks){
                    aiThread[numMoves] = new ChessAI(moveOn,threadPlayer[numMoves], depth);
                    aiThread[numMoves].mainPlayer.currentBoard.duplicate(mainPlayer.currentBoard);
                    aiThread[numMoves].setMove(k, i, moveList[numMoves]);
                    aiThread[numMoves].start();
                    
                    numMoves++;
                    if (numMoves==numThreads){
                        threads();
                        numMoves=0;
                    }
                }
            }
        }
                threads();
        mainPlayer.currentBoard.cellGrid[bestMove.to.posY][bestMove.to.posX].setMovableSelectedColor();
        mainPlayer.currentBoard.cellGrid[bestMove.from.posY][bestMove.from.posX].setMovableUnselectedColor();
        try {
                sleep(700);
            } catch (InterruptedException ex) {
            }
        mainPlayer.moveHandler(mainPlayer.currentBoard.cellGrid[bestMove.from.posY][bestMove.from.posX], mainPlayer.currentBoard.cellGrid[bestMove.to.posY][bestMove.to.posX]);
        System.gc();
    }
    
    synchronized void threads(){
        try {
                            for (int m=0;m<numMoves;m++){
                                aiThread[m].join();
                            }
                        }
                        catch (InterruptedException ex) {
                                Logger.getLogger(ChessAiHandler.class.getName()).log(Level.SEVERE, null, ex);
                        }
                        for (int m=0; m<numMoves; m++){
                            if (bestMove.score<=moveList[m].score){
                                bestMove.from = moveList[m].from;
                                bestMove.to = moveList[m].to;
                                bestMove.score = moveList[m].score;
                            }
                        }
                        if (bestMove.from==null){
                            for (int m=0; m<numMoves; m++){
                            if (bestMove.score<=moveList[m].score){
                                bestMove.from = moveList[m].from;
                                bestMove.to = moveList[m].to;
                                bestMove.score = moveList[m].score;
                            }
                        }
                        }
    }
}

class chessPlayerCreator extends Thread{
    chessPlayer[] playerTarget;
    chessPlayer mainPlayer;
    int depth;
    int index;
    chessPlayerCreator(chessPlayer[] ref, chessPlayer main, int ind, int dep){
        playerTarget = ref;
        index=ind;
        depth = dep;
        mainPlayer = main;
    }
    public void run(){
        playerTarget[index] = new chessPlayer(new boardState(1, mainPlayer.currentBoard.playerBlue, mainPlayer.currentBoard.playerYellow), true, depth);
    }
}



 




        