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
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import java.util.ArrayList;

public class chessBoard extends JPanel{ 
    
    private boardState mainBoard;
    private chessMoveFinder mainBoardHandler;
    
    public chessBoard(int cellSize) {
        mainBoard = new boardState(cellSize);
        mainBoardHandler = new chessMoveFinder(mainBoard);
        GridLayout grid = new GridLayout(8,8,0,0);
        this.setLayout(grid);
        
        for (int i=0;i<8;i++){
            for (int j=0;j<8;j++){
                this.add(mainBoard.cellGrid[i][j]);
                setButtonConfig(mainBoard.cellGrid[i][j]);
            }
        }
        mainBoardHandler.calculateAttacks();
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
                        if (setMovableUnselected(current)!=0) {
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
                            unsetMovable(current);
                        }
                    }
                }
            }
            @Override
            public void mousePressed(java.awt.event.MouseEvent evt){
                if (!mainBoard.pieceSelected && current.currentPiece!=null){
                    if (current.currentPiece.pieceC != mainBoard.currentTurn){
                        current.setBaseColor();
                        JOptionPane.showMessageDialog(null,"It is "+mainBoard.currentTurn.name()+"'s turn");
                        return;
                    }
                    else if (current.attacking==null){
                        current.setBaseColor();
                        JOptionPane.showMessageDialog(null,"This piece has no valid moves");
                        return;                        
                    }
                    else{
                        mainBoardHandler.moveHandler(current);
                    }
                }
                else if (mainBoard.pieceSelected){
                    mainBoardHandler.moveHandler(current);
                }
            }
        });
    }
    
    final static int setMovableUnselected(cell current){
        ArrayList<cell> moves = current.attacking;
        if (moves==null) {
            return 0;
        }
        for (int i=0;i<moves.size();i++){
            moves.get(i).setMovableUnselectedColor();
        }
        return moves.size();
    }
    
    
    final static void unsetMovable(cell current){
        ArrayList<cell> moves = current.attacking;
        if (moves==null) {
            return;
        }
        for (int i=0;i<moves.size();i++){
            moves.get(i).setBaseColor();
            moves.get(i).unsetPlaceable();
        }
    }
    
    
    final static int setMovableSelected(cell current){
        ArrayList<cell> moves = current.attacking;
        if (moves==null) {
            return 0;
        }
        for (int i=0;i<moves.size();i++){
            moves.get(i).setMovableSelectedColor();
            moves.get(i).setPlaceable();
        }
        current.setMovableUnselectedColor();
        return moves.size();
    }
}
    
