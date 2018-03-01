/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package chess;

/**
 *
 * @author ashir basu
 */

import java.awt.GridLayout;
import javax.swing.JPanel;
import java.util.ArrayList;

public class chessBoard extends JPanel{ 
    
    private boardState mainBoard;
    private chessPlayer mainBoardHandler;
    ChessAiHandler blueAI;
    ChessAiHandler yellowAI;
    int AIDepth = 3;
    public chessBoard(int cellSize) {
        mainBoard = new boardState(cellSize, playerType.human, playerType.ai);
        mainBoardHandler = new chessPlayer(mainBoard, true, 0);
        GridLayout grid = new GridLayout(8,8,0,0);
        this.setLayout(grid);
        
        for (int i=0;i<8;i++){
            for (int j=0;j<8;j++){
                this.add(mainBoard.cellGrid[i][j]);
                setButtonConfig(mainBoard.cellGrid[i][j]);
            }
        }
        mainBoardHandler.calculateAttacks();
        mainBoardHandler.setValidMoves();
        yellowAI = new ChessAiHandler(pieceColor.yellow, mainBoardHandler, AIDepth, 8);
        yellowAI.start();
        this.setVisible(true);
    }   
    
    final void setButtonConfig(cell current){
        current.addMouseListener(new java.awt.event.MouseAdapter() {
            @Override
            public void mouseEntered(java.awt.event.MouseEvent evt) {
                if (!mainBoard.pieceSelected){
                    if (current.currentPiece==null) {
                        current.setHoverColor();
                    } else if (current.currentPiece.pieceC == mainBoard.currentTurn) {
                        if (mainBoardHandler.setMovableUnselected(current)!=0) {
                            current.setHoverColor();
                        } else {
                            current.setHoverInvalidColor();
                        }
                    } else {
                        current.setHoverInvalidColor();
                    }
                }
            }
            @Override
            public void mouseExited(java.awt.event.MouseEvent evt) {
                if (!mainBoard.pieceSelected){
                    current.setBaseColor();
                    if (current.currentPiece!=null){
                        if (current.currentPiece.pieceC == mainBoard.currentTurn) {
                            mainBoardHandler.unsetMovable(current);
                        }
                    }
                }
            }
        });
    }
}
    
