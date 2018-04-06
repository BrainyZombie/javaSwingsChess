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
import javax.swing.JButton;

public class chessBoard extends JPanel{ 
    
    boardState mainBoard;
    private chessPlayer mainPlayer;
    ChessAiHandler blueAI;
    ChessAiHandler yellowAI;
    int AIDepth = 3;
    int aiThread = 4;
    public chessBoard(int cellSize) {
        mainBoard = new boardState(cellSize, playerType.human, playerType.ai);
        mainPlayer = new chessPlayer(mainBoard, true, 0);
        mainPlayer.tracker = new moveTracker(mainBoard);
        mainPlayer.tracker.start();
        GridLayout grid = new GridLayout(8,9,0,0);
        this.setLayout(grid);
        
        for (int i=0;i<8;i++){
            for (int j=0;j<8;j++){
                this.add(mainBoard.cellGrid[i][j]);
                setButtonConfig(mainBoard.cellGrid[i][j]);
            }
            JButton temp = new JButton();
            this.add (temp);
            if (i<4){
                temp.addMouseListener(new java.awt.event.MouseAdapter() {
                    @Override
                    public void mousePressed(java.awt.event.MouseEvent evt){
                        doUndo();
                        System.out.println("undo");
                        mainPlayer.resetColors();
                    }
                });
            }
            else{
                
                temp.addMouseListener(new java.awt.event.MouseAdapter() {
                    @Override
                    public void mousePressed(java.awt.event.MouseEvent evt){
                        doRedo();
                        System.out.println("redo");
                        mainPlayer.resetColors();
                    }
                });
            }
        }
        yellowAI = new ChessAiHandler(pieceColor.yellow, mainPlayer, AIDepth, aiThread);
//        blueAI = new ChessAiHandler(pieceColor.blue, mainPlayer, AIDepth, aiThread);
//        blueAI.start();
        yellowAI.start();
        this.setVisible(true);
    }   
    
    
    final boolean doUndo(){
        if (mainPlayer.tracker.previousBoard == null)
            return false;
        mainPlayer.tracker.currentBoard = mainPlayer.tracker.previousBoard;
        mainPlayer.tracker.previousBoard = mainPlayer.tracker.previousBoard.previous;
        mainBoard.duplicate(mainPlayer.tracker.currentBoard);
        return true;
    }
    final boolean doRedo(){
        if (mainPlayer.tracker.currentBoard.next == null)
            return false;
        
        mainPlayer.tracker.previousBoard = mainPlayer.tracker.currentBoard;
        mainPlayer.tracker.currentBoard = mainPlayer.tracker.currentBoard.next;
        mainBoard.duplicate(mainPlayer.tracker.currentBoard);
        return true;
    }
    
    final void setButtonConfig(cell current){
        current.addMouseListener(new java.awt.event.MouseAdapter() {
            @Override
            public void mouseEntered(java.awt.event.MouseEvent evt) {
                if (!mainBoard.pieceSelected){
                    if (current.currentPiece==null) {
                        current.setHoverColor();
                    } else if (current.currentPiece.pieceC == mainBoard.currentTurn) {
                        if (mainPlayer.setMovableUnselected(current)!=0) {
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
                            mainPlayer.unsetMovable(current);
                        }
                    }
                }
            }
        });
    }
}
    
