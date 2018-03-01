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
    ChessAiHandler(pieceColor c, chessPlayer main, int depth, int numThreads){
        moveOn = c;
        mainPlayer=main;
        this.depth = depth;
        this.numThreads = numThreads;
        aiThread = new ChessAI[numThreads];
        threadPlayer = new chessPlayer[numThreads];
        for (int i=0;i<numThreads;i++){
            System.out.println("y");
            threadPlayer[i] = new chessPlayer(new boardState(1, mainPlayer.currentBoard.playerBlue, mainPlayer.currentBoard.playerYellow), true, depth);
        }
    }
    public void run (){
        while (true){
            try {
                sleep(500);
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
        int numMoves = 0;
        moveList = new candidateMove[numThreads];
        bestMove = new candidateMove();
        for (int i=0;i<numThreads;i++){
            moveList[i] = new candidateMove();
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
                        numMoves=0;
                        threads();
                    }
                }
        
            }
        }
        threads();
        if (bestMove.from==null)    return;
        mainPlayer.moveHandler(mainPlayer.currentBoard.cellGrid[bestMove.from.posY][bestMove.from.posX]);
        try {
                sleep(700);
            } catch (InterruptedException ex) {
            }
        mainPlayer.moveHandler(mainPlayer.currentBoard.cellGrid[bestMove.to.posY][bestMove.to.posX]);
        mainPlayer.currentBoard.cellGrid[bestMove.to.posY][bestMove.to.posX].setBaseColor();
        System.gc();
    }
    
    void threads(){
        try {
                            for (int m=0;m<numThreads;m++){
                                aiThread[m].join();
                            }
                        }
                        catch (InterruptedException ex) {
                                Logger.getLogger(ChessAiHandler.class.getName()).log(Level.SEVERE, null, ex);
                        }
                        int maxScoring = 0;
                        for (int m=1; m<numThreads; m++){
//                            aiThread[m] = moveList[m].thread;
                            if (moveList[maxScoring].score<moveList[m].score)
                                maxScoring=m;
                        }
                        bestMove = moveList[maxScoring];
//                        System.out.println(bestMove.score);
    }
}



 




        