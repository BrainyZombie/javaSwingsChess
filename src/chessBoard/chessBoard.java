/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package chessBoard;

/**
 *
 * @author ashir basu
 */

import java.awt.GridLayout;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import java.util.ArrayList;

public class chessBoard extends JPanel{ 
    
    private int cellSize;
    private pieceColor currentTurn = pieceColor.blue;
    private cell selectedCell = null;
    private boolean pieceSelected = false;
    
    public chessBoard(int cellSize) {
    
        GridLayout grid = new GridLayout(8,8,0,0);
        this.setLayout(grid);
        global.size=cellSize;
        
        for (int i=0;i<8;i++){
            for (int j=0;j<8;j++){
                global.cellGrid[i][j] = new cell(j,i);
                this.add(global.cellGrid[i][j]);
                setButtonConfig(global.cellGrid[i][j]);
            }
        }
        setInitialConfig();
        calculateAttacks();
        this.setVisible(true);
    }   
    
    final void setButtonConfig(cell current){
        current.addMouseListener(new java.awt.event.MouseAdapter() {
            @Override
            public void mouseEntered(java.awt.event.MouseEvent evt) {
                if (!pieceSelected){
                    if (current.currentPiece==null) {
                        current.setHoverColor();
                    } else if (current.currentPiece.pieceC == currentTurn) {
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
                if (!pieceSelected){
                    current.setBaseColor();
                    if (current.currentPiece!=null){
                        if (current.currentPiece.pieceC == currentTurn) {
                            unsetMovable(current);
                        }
                    }
                }
            }
            @Override
            public void mousePressed(java.awt.event.MouseEvent evt){
                if (!pieceSelected && current.currentPiece!=null){
                    if (current.currentPiece.pieceC != currentTurn){
                        current.setBaseColor();
                        JOptionPane.showMessageDialog(null,"It is "+currentTurn.name()+"'s turn");
                        return;
                    }
                    else if (current.attacking==null){
                        current.setBaseColor();
                        JOptionPane.showMessageDialog(null,"This piece has no valid moves");
                        return;                        
                    }
                    else{
                        moveHandler(current);
                    }
                }
                else if (pieceSelected){
                    moveHandler(current);
                }
            }
        });
    }
    
    final void setInitialConfig(){
        for (int i=0;i<8;i++){
            global.cellGrid[1][i].setPiece(new pawn(pieceColor.yellow, global.cellGrid[1][i]));
            global.cellGrid[6][i].setPiece(new pawn(pieceColor.blue, global.cellGrid[6][i]));
        }
        
        
        global.cellGrid[0][0].setPiece(new rook(pieceColor.yellow, global.cellGrid[0][0]));
        global.cellGrid[0][1].setPiece(new knight(pieceColor.yellow, global.cellGrid[0][1]));
        global.cellGrid[0][2].setPiece(new bishop(pieceColor.yellow, global.cellGrid[0][2]));
        global.cellGrid[0][3].setPiece(new queen(pieceColor.yellow, global.cellGrid[0][3]));
        global.cellGrid[0][4].setPiece(new king(pieceColor.yellow, global.cellGrid[0][4]));
        global.cellGrid[0][5].setPiece(new bishop(pieceColor.yellow, global.cellGrid[0][5]));
        global.cellGrid[0][6].setPiece(new knight(pieceColor.yellow, global.cellGrid[0][6]));
        global.cellGrid[0][7].setPiece(new rook(pieceColor.yellow, global.cellGrid[0][7]));
        
        
        global.cellGrid[7][0].setPiece(new rook(pieceColor.blue, global.cellGrid[7][0]));
        global.cellGrid[7][1].setPiece(new knight(pieceColor.blue, global.cellGrid[7][0]));
        global.cellGrid[7][2].setPiece(new bishop(pieceColor.blue, global.cellGrid[7][0]));
        global.cellGrid[7][3].setPiece(new queen(pieceColor.blue, global.cellGrid[7][0]));
        global.cellGrid[7][4].setPiece(new king(pieceColor.blue, global.cellGrid[7][0]));
        global.cellGrid[7][5].setPiece(new bishop(pieceColor.blue, global.cellGrid[7][0]));
        global.cellGrid[7][6].setPiece(new knight(pieceColor.blue, global.cellGrid[7][0]));
        global.cellGrid[7][7].setPiece(new rook(pieceColor.blue, global.cellGrid[7][0]));
        
    }
    
    final void calculateAttacks(){
        for (int i=0;i<8;i++){
            for (int j=0;j<8;j++){
                global.cellGrid[i][j].setAttacking(populateMoves(global.cellGrid[i][j]));
            }
        }
    }
    
    final int setMovableUnselected(cell current){
        ArrayList<cell> moves = current.attacking;
        if (moves==null) {
            return 0;
        }
        for (int i=0;i<moves.size();i++){
            moves.get(i).setMovableUnselectedColor();
        }
        return moves.size();
    }
    
    
    final void unsetMovable(cell current){
        ArrayList<cell> moves = current.attacking;
        if (moves==null) {
            return;
        }
        for (int i=0;i<moves.size();i++){
            moves.get(i).setBaseColor();
            moves.get(i).unsetPlaceable();
        }
    }
    
    
    final int setMovableSelected(cell current){
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
    
    final void moveHandler(cell current){
        if (pieceSelected) {
            if (current==selectedCell) {
                current.setHoverColor();
                setMovableUnselected(current);
                pieceSelected=false;
                selectedCell=null;
            }
            else if(current.getPlaceable()){
                specialAction action = wasSpecialAction(current, selectedCell);
                
                current.currentPiece = selectedCell.currentPiece;
                
                unsetMovable(selectedCell);
                selectedCell.setAttacking(null);
                selectedCell.currentPiece = null;
                selectedCell.setBaseColor();
                current.currentPiece.setCell(current);
                
                current.setHoverInvalidColor();
                
                
                pieceSelected=false;
                selectedCell=null;
                current.currentPiece.onMove();
                
                
                if (currentTurn.name().equals("blue"))
                    currentTurn  = pieceColor.yellow;
                else
                    currentTurn  = pieceColor.blue;
                
                if (action!=null)   action.postClick();
                calculateAttacks();
            }
        } else{
            pieceSelected=true;
            selectedCell=current;
            setMovableSelected(current);
        }
    }
    
    final specialAction wasSpecialAction(cell current, cell selectedCell){
        for (int i=0; i<selectedCell.currentPiece.specialMoves.size(); i++){
                if(findMoves(selectedCell, selectedCell.currentPiece.specialMoves.get(i)).contains(current)){
                    return selectedCell.currentPiece.specialMoves.get(i);
                }
            }
        return null;
    }
    
    final ArrayList<cell> populateMoves(cell current){
        ArrayList<cell> moves = new ArrayList<>();    
        if (current.currentPiece==null) {
            return null;
        } else {
            
            for (int i=0; i<current.currentPiece.specialMoves.size(); i++){
                moves.addAll(findMoves(current, current.currentPiece.specialMoves.get(i)));
            }
            for (int i=0; i<current.currentPiece.movesAllowed.size(); i++){
                moves.addAll(findMoves(current, current.currentPiece.movesAllowed.get(i)));
            }
        }
        if (moves.isEmpty())
            return null;
        else
            return moves;
    }
    final ArrayList<cell> findMoves(cell current, specialAction currentMove){
        if (currentMove.validateAction()){
            return findMoves(current, (pieceMove) currentMove);
        }        
        return new ArrayList<cell>();
    }
    final ArrayList<cell> findMoves(cell current, pieceMove currentMove){
        boolean blue = (current.currentPiece.pieceC.name().equals("blue"));
        int x=current.posX, y=current.posY;
        ArrayList<cell> moves = new ArrayList<>();    
        if (currentMove.type == moveType.recursive){
            while (true){
                x += currentMove.x;
                y -= currentMove.y * (blue?1:-1);
                if (x<8 && y<8 && x>=0 && y>=0) {
                    if (global.cellGrid[y][x].currentPiece==null) {
                        moves.add(global.cellGrid[y][x]);
                    } else{
                        if(global.cellGrid[y][x].currentPiece.pieceC!=currentTurn) {
                            moves.add(global.cellGrid[y][x]);
                        }
                        break;
                    }
                }
                else {
                    break;
                }   
            }
        }
        else{
            x += currentMove.x;
            y -= currentMove.y * (blue?1:-1);
            if (x<8 && y<8 && x>=0 && y>=0) {
                if (currentMove.type == moveType.single) {
                    if (global.cellGrid[y][x].currentPiece==null) {
                        moves.add(global.cellGrid[y][x]);
                    } else{
                        if(global.cellGrid[y][x].currentPiece.pieceC!=currentTurn) {
                            moves.add(global.cellGrid[y][x]);
                        }
                    }
                } else if (currentMove.type == moveType.cannotCapture && global.cellGrid[y][x].currentPiece==null) {
                    if (global.cellGrid[y][x].currentPiece==null) {
                        moves.add(global.cellGrid[y][x]);
                    } else{
                        if(global.cellGrid[y][x].currentPiece.pieceC!=currentTurn) {
                            moves.add(global.cellGrid[y][x]);
                        }
                    }
                } else if (currentMove.type == moveType.onlyCapture && global.cellGrid[y][x].currentPiece!=null) {
                    if (global.cellGrid[y][x].currentPiece==null) {
                        moves.add(global.cellGrid[y][x]);
                    } else {
                        if(global.cellGrid[y][x].currentPiece.pieceC!=currentTurn) {
                            moves.add(global.cellGrid[y][x]);
                        }
                    }
                }
            }
        }
        return moves;
    }  
}