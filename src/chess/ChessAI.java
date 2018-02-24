/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package chess;

import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author student
 */
public class ChessAI extends Thread {
    pieceColor moveOn;
    chessPlayer mainPlayer;
    int depth;
    ChessAI(pieceColor c, chessPlayer main, int depth){
        moveOn = c;
        mainPlayer=main;
        this.depth = depth;
    }
    public void run (){
        while (true){
            try {
                sleep(500);
            } catch (InterruptedException ex) {
                Logger.getLogger(ChessAI.class.getName()).log(Level.SEVERE, null, ex);
            }
            if (mainPlayer.currentBoard.currentTurn == moveOn)
                aiMove();
        }
    }
    private void aiMove() {
        System.out.println("start");
        chessPlayer temp = mainPlayer.AI;
        cell[][] tempBoard;
        double bestMove = -9999.0;
        cell bestStart = null, bestEnd = null;
        boolean isMaximisingPlayer = true;
        
        for (cell[] j:mainPlayer.currentBoard.cellGrid){
            for (cell k: j){
                for (cell i:k.attacking) {
                    if (k.currentPiece.pieceC==temp.currentBoard.currentTurn)
                        continue;
                    temp.currentBoard.duplicate(mainPlayer.currentBoard);
                    tempBoard = temp.currentBoard.cellGrid;
                    temp.doMove(tempBoard[k.posY][k.posX], tempBoard[i.posY][i.posX]);
                    double value = minimax(temp, depth-1, -10000.0, 10000.0, !isMaximisingPlayer);
                    if(value > bestMove) {
                        bestMove = value;
                        bestStart = mainPlayer.currentBoard.cellGrid[k.posY][k.posX];
                        bestEnd = mainPlayer.currentBoard.cellGrid[i.posY][i.posX];
                    }
                }
            }
        }
        System.out.println("stop");
        mainPlayer.moveHandler(bestStart);
        try {
                sleep(700);
            } catch (InterruptedException ex) {
                Logger.getLogger(ChessAI.class.getName()).log(Level.SEVERE, null, ex);
            }
        mainPlayer.moveHandler(bestEnd);
    }
    
    private double minimax(chessPlayer current, int depth, double alpha, double beta, boolean isMaximisingPlayer){
        
        if (depth==0){
            return -evalBoard(current);
        }
        else{
            chessPlayer temp = current.AI;
            cell[][] tempBoard;
            current.setValidMoves();
            if(isMaximisingPlayer) {
                double bestMove = -9999.0;
                
                for (cell[] j:current.currentBoard.cellGrid) {
                    for (cell k: j) {
                        for (cell i : k.attacking){
                            if (k.currentPiece.pieceC==current.currentBoard.currentTurn)
                                continue;
                            
                            if (depth==1){
                                i.currentPiece = k.currentPiece;
                                k.currentPiece = null;
                                i.currentPiece.setCell(i);
                                
                                bestMove = Math.max(bestMove, -evalBoard(current));
                                
                                k.currentPiece = i.currentPiece;
                                i.currentPiece = null;
                                k.currentPiece.setCell(k);
                                System.out.println(depth);
                                
                            }
                            else{
                                temp.currentBoard.duplicate(current.currentBoard);
                                tempBoard = temp.currentBoard.cellGrid;
//                                dispBoard(current.currentBoard.cellGrid);
                                temp.doMove(tempBoard[k.posY][k.posX], tempBoard[i.posY][i.posX]);
                                bestMove = Math.max(bestMove, minimax(temp, depth-1, alpha, beta, !isMaximisingPlayer));
                            }
                            alpha = Math.max(alpha, bestMove);
                            if (beta <= alpha){
                                return bestMove;
                            }
                        }
                    }
                }
                
                return bestMove;
            } else {
                double bestMove = 9999.0;
                
                for (cell[] j:current.currentBoard.cellGrid)
                    for (cell k: j) {
                        for (cell i:k.attacking){
                            if (k.currentPiece.pieceC==current.currentBoard.currentTurn)
                                continue;
                            if (depth==1){
                                i.currentPiece = k.currentPiece;
                                k.currentPiece = null;
                                i.currentPiece.setCell(i);
                                
                                bestMove = Math.max(bestMove, -evalBoard(current));
                                
                                k.currentPiece = i.currentPiece;
                                i.currentPiece = null;
                                k.currentPiece.setCell(k);
                                
                            }
                            else{
                            temp.currentBoard.duplicate(current.currentBoard);
                            tempBoard = temp.currentBoard.cellGrid;
                            temp.doMove(tempBoard[k.posY][k.posX], tempBoard[i.posY][i.posX]);
                            bestMove = Math.min(bestMove, minimax(temp, depth-1, alpha, beta, !isMaximisingPlayer));
                            }
                            beta = Math.min(beta, bestMove);
                            if (beta <= alpha)
                                return bestMove;
                        }
                    }
                return bestMove;
            }
        }
    }
    
    private double evalBoard(chessPlayer current) {
        return Math.random();
    }
    
    final void dispBoard(cell[][] game) {
        for(int i = 0; i < game.length; i++) {
            for(int j = 0; j < game.length; j++) {
                if(game[i][j].currentPiece != null)
                    System.out.print("" + game[i][j].currentPiece.pieceT.toString().charAt(0) + "\t");
                else System.out.print("____\t");
            }
            System.out.println();
        }
        System.out.println();
    }
    
}
