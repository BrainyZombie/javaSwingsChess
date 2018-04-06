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
 * @author pranav tharoor
 */
public class ChessAI extends Thread {
    pieceColor moveOn;
    chessPlayer mainPlayer;
    int depth;
    cell from, to;
    candidateMove moveStore;
    ChessAI(pieceColor c, chessPlayer main, int depth){
        moveOn = c;
        mainPlayer=main;
        this.depth = depth;
    }
    public void setMove(cell from, cell to, candidateMove store){
        this.from = from;
        this.to = to;
        moveStore = store;
    }
    public void run(){
        chessPlayer temp = mainPlayer.AI;
        cell[][] tempBoard;
        boolean isMaximisingPlayer = true;
        temp.currentBoard.duplicate(mainPlayer.currentBoard);
        tempBoard = temp.currentBoard.cellGrid;
        temp.doMove(tempBoard[from.posY][from.posX], tempBoard[to.posY][to.posX]);
        moveStore.score = minimax(temp, depth-1, -10000.0, 10000.0, !isMaximisingPlayer);
        moveStore.from=from;
        moveStore.to=to;
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
                        if (k.currentPiece==null||k.currentPiece.pieceC==temp.currentBoard.currentTurn)
                            continue;
                        cell[] attacks = k.attacking.toArray(new cell[k.attacking.size()]);
                        for (cell i : attacks){
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
                
                for (cell[] j:current.currentBoard.cellGrid) {
                    for (cell k: j) {
                        if (k.currentPiece==null||k.currentPiece.pieceC==temp.currentBoard.currentTurn)
                            continue;
                        cell[] attacks = k.attacking.toArray(new cell[k.attacking.size()]);
                        for (cell i:attacks){
                            if (depth==1){
                                i.currentPiece = k.currentPiece;
                                k.currentPiece = null;
                                i.currentPiece.setCell(i);
                                
                                bestMove = Math.min(bestMove, evalBoard(current));
                                
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
                }
                return bestMove;
            }
        }
    }
    
    private double evalBoard(chessPlayer current) {
        double value = 0.0;
        cell[][] game = current.currentBoard.cellGrid;
        pieceColor currTurn = current.currentBoard.currentTurn;
        for(cell i[] : game)
            for(cell j: i)
                if(j.currentPiece != null)
                    value += getPieceValue(j.currentPiece.pieceC, j.currentPiece.pieceT, j.posX, j.posY, currTurn);
        return value;
            
    }
    
    final double getPieceValue(pieceColor color, pieceType type, int x, int y, pieceColor currTurn) {
        double value = 0.0;
        switch(type) {
            case pawn:
                value += 10 + (color != currTurn ? PawnTable[x * 8 + y] : PawnTable[63 - x * 8 - y]);
                break;
            case rook:
                value += 50 + (color != currTurn ? RookTable[x * 8 + y] : RookTable[63 - x * 8 - y]);
                break;
            case knight:
                value += 30 + (color != currTurn ? KnightTable[x * 8 + y] : KnightTable[63 - x * 8 - y]);
                break;
            case bishop:
                value += 30 + (color != currTurn ? BishopTable[x * 8 + y] : BishopTable[63 - x * 8 - y]);
                break;
            case queen:
                value += 90 + (color != currTurn ? QueenTable[x * 8 + y] : QueenTable[63 - x * 8 - y]);
                break;
            case king:
                value += 900 + (color != currTurn ? KingTable[x * 8 + y] : KingTable[63 - x * 8 - y]);
                break;
        }
        return color != currTurn ? value : -value; 
    }

    private static double[] PawnTable = new double[]
    {
        0.0,  0.0,  0.0,  0.0,  0.0,  0.0,  0.0,  0.0,
        5.0,  5.0,  5.0,  5.0,  5.0,  5.0,  5.0,  5.0,
        1.0,  1.0,  2.0,  3.0,  3.0,  2.0,  1.0,  1.0,
        0.5,  0.5,  1.0,  2.5,  2.5,  1.0,  0.5,  0.5,
        0.0,  0.0,  0.0,  2.0,  2.0,  0.0,  0.0,  0.0,
        0.5, -0.5, -1.0,  0.0,  0.0, -1.0, -0.5,  0.5,
        0.5,  1.0, 1.0,  -2.0, -2.0,  1.0,  1.0,  0.5,
        0.0,  0.0,  0.0,  0.0,  0.0,  0.0,  0.0,  0.0
    };
    private static double[] KnightTable = new double[]
    {
        -5.0, -4.0, -3.0, -3.0, -3.0, -3.0, -4.0, -5.0,
        -4.0, -2.0,  0.0,  0.0,  0.0,  0.0, -2.0, -4.0,
        -3.0,  0.0,  1.0,  1.5,  1.5,  1.0,  0.0, -3.0,
        -3.0,  0.5,  1.5,  2.0,  2.0,  1.5,  0.5, -3.0,
        -3.0,  0.0,  1.5,  2.0,  2.0,  1.5,  0.0, -3.0,
        -3.0,  0.5,  1.0,  1.5,  1.5,  1.0,  0.5, -3.0,
        -4.0, -2.0,  0.0,  0.5,  0.5,  0.0, -2.0, -4.0,
        -5.0, -4.0, -3.0, -3.0, -3.0, -3.0, -4.0, -5.0
    };

    private static double[] BishopTable = new double[]
    {
        -2.0, -1.0, -1.0, -1.0, -1.0, -1.0, -1.0, -2.0,
        -1.0,  0.0,  0.0,  0.0,  0.0,  0.0,  0.0, -1.0,
        -1.0,  0.0,  0.5,  1.0,  1.0,  0.5,  0.0, -1.0,
        -1.0,  0.5,  0.5,  1.0,  1.0,  0.5,  0.5, -1.0,
        -1.0,  0.0,  1.0,  1.0,  1.0,  1.0,  0.0, -1.0,
        -1.0,  1.0,  1.0,  1.0,  1.0,  1.0,  1.0, -1.0,
        -1.0,  0.5,  0.0,  0.0,  0.0,  0.0,  0.5, -1.0,
        -2.0, -1.0, -1.0, -1.0, -1.0, -1.0, -1.0, -2.0
    };

    private static double[] KingTable = new double[]
    {
        -3.0, -4.0, -4.0, -5.0, -5.0, -4.0, -4.0, -3.0,
        -3.0, -4.0, -4.0, -5.0, -5.0, -4.0, -4.0, -3.0,
        -3.0, -4.0, -4.0, -5.0, -5.0, -4.0, -4.0, -3.0,
        -3.0, -4.0, -4.0, -5.0, -5.0, -4.0, -4.0, -3.0,
        -2.0, -3.0, -3.0, -4.0, -4.0, -3.0, -3.0, -2.0,
        -1.0, -2.0, -2.0, -2.0, -2.0, -2.0, -2.0, -1.0,
         2.0,  2.0,  0.0,  0.0,  0.0,  0.0,  2.0,  2.0,
         2.0,  3.0,  1.0,  0.0,  0.0,  1.0,  3.0,  2.0
    };

//    private static short[] KingTableEndGame = new short[]
//    {
//        -50,-40,-30,-20,-20,-30,-40,-50,
//        -30,-20,-10,  0,  0,-10,-20,-30,
//        -30,-10, 20, 30, 30, 20,-10,-30,
//        -30,-10, 30, 40, 40, 30,-10,-30,
//        -30,-10, 30, 40, 40, 30,-10,-30,
//        -30,-10, 20, 30, 30, 20,-10,-30,
//        -30,-30,  0,  0,  0,  0,-30,-30,
//        -50,-30,-30,-30,-30,-30,-30,-50
//    };

    private static double[] RookTable = new double[]
    {
         0.0,  0.0,  0.0,  0.0,  0.0,  0.0,  0.0,  0.0,
         0.5,  1.0,  1.0,  1.0,  1.0,  1.0,  1.0,  0.5,
        -0.5,  0.0,  0.0,  0.0,  0.0,  0.0,  0.0, -0.5,
        -0.5,  0.0,  0.0,  0.0,  0.0,  0.0,  0.0, -0.5,
        -0.5,  0.0,  0.0,  0.0,  0.0,  0.0,  0.0, -0.5,
        -0.5,  0.0,  0.0,  0.0,  0.0,  0.0,  0.0, -0.5,
        -0.5,  0.0,  0.0,  0.0,  0.0,  0.0,  0.0, -0.5,
         0.0,   0.0, 0.0,  0.5,  0.5,  0.0,  0.0,  0.0
    };
    
    private static double[] QueenTable = new double[]
    {
        -2.0, -1.0, -1.0, -0.5, -0.5, -1.0, -1.0, -2.0,
        -1.0,  0.0,  0.0,  0.0,  0.0,  0.0,  0.0, -1.0,
        -1.0,  0.0,  0.5,  0.5,  0.5,  0.5,  0.0, -1.0,
        -0.5,  0.0,  0.5,  0.5,  0.5,  0.5,  0.0, -0.5,
         0.0,  0.0,  0.5,  0.5,  0.5,  0.5,  0.0, -0.5,
        -1.0,  0.5,  0.5,  0.5,  0.5,  0.5,  0.0, -1.0,
        -1.0,  0.0,  0.5,  0.0,  0.0,  0.0,  0.0, -1.0,
        -2.0, -1.0, -1.0, -0.5, -0.5, -1.0, -1.0, -2.0
    };
    
}
