/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package chess;

import java.util.ArrayList;

/**
 *
 * @author student
 */
public class chessMoveFinder {
    
    public boardState currentBoard;
    
    chessMoveFinder(boardState board){
        currentBoard = board;
    }
     
    final  void calculateAttacks(){
        for (int i=0;i<8;i++){
            for (int j=0;j<8;j++){
                currentBoard.cellGrid[i][j].setAttacking(populateMoves(currentBoard.cellGrid[i][j]));
            }
        }
    }
    
    
    
    final  ArrayList<cell> populateMoves(cell current){
        ArrayList<cell> moves = new ArrayList<>();    
        if (current.currentPiece==null) {
            return null;
        } else {
            
            for (int i=0; i<current.currentPiece.specialMoves.size(); i++){
                moves.addAll(findPossibleMoves(current, current.currentPiece.specialMoves.get(i)));
            }
            for (int i=0; i<current.currentPiece.movesAllowed.size(); i++){
                moves.addAll(findPossibleMoves(current, current.currentPiece.movesAllowed.get(i)));
            }
        }
        if (moves.isEmpty())
            return null;
        else
            return moves;
    }
    
    
    
    
    final  ArrayList<cell> findPossibleMoves(cell current, specialAction currentMove){
        if (currentMove.validateAction()){
            return findPossibleMoves(current, (pieceMove) currentMove);
        }        
        return new ArrayList<cell>();
    }
    
    final  ArrayList<cell> findPossibleMoves(cell current, pieceMove currentMove){
        boolean blue = (current.currentPiece.pieceC.name().equals("blue"));
        int x=current.posX, y=current.posY;
        ArrayList<cell> moves = new ArrayList<>();    
        if (currentMove.type == moveType.recursive){
            while (true){
                x += currentMove.x;
                y -= currentMove.y * (blue?1:-1);
                if (x<8 && y<8 && x>=0 && y>=0) {
                    if (currentBoard.cellGrid[y][x].currentPiece==null) {
                        moves.add(currentBoard.cellGrid[y][x]);
                    } else{
                        if(currentBoard.cellGrid[y][x].currentPiece.pieceC!=currentBoard.currentTurn) {
                            moves.add(currentBoard.cellGrid[y][x]);
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
                    if (currentBoard.cellGrid[y][x].currentPiece==null) {
                        moves.add(currentBoard.cellGrid[y][x]);
                    } else{
                        if(currentBoard.cellGrid[y][x].currentPiece.pieceC!=currentBoard.currentTurn) {
                            moves.add(currentBoard.cellGrid[y][x]);
                        }
                    }
                } else if (currentMove.type == moveType.cannotCapture && currentBoard.cellGrid[y][x].currentPiece==null) {
                    if (currentBoard.cellGrid[y][x].currentPiece==null) {
                        moves.add(currentBoard.cellGrid[y][x]);
                    } else{
                        if(currentBoard.cellGrid[y][x].currentPiece.pieceC!=currentBoard.currentTurn) {
                            moves.add(currentBoard.cellGrid[y][x]);
                        }
                    }
                } else if (currentMove.type == moveType.onlyCapture && currentBoard.cellGrid[y][x].currentPiece!=null) {
                    if (currentBoard.cellGrid[y][x].currentPiece==null) {
                        moves.add(currentBoard.cellGrid[y][x]);
                    } else {
                        if(currentBoard.cellGrid[y][x].currentPiece.pieceC!=currentBoard.currentTurn) {
                            moves.add(currentBoard.cellGrid[y][x]);
                        }
                    }
                }
            }
        }
        return moves;
    }  
    
    
    final  void moveHandler(cell current){
        if (currentBoard.pieceSelected) {
            if (current==currentBoard.selectedCell) {
                current.setHoverColor();
                chessBoard.setMovableUnselected(current);
                currentBoard.pieceSelected=false;
                currentBoard.selectedCell=null;
            }
            else if(current.getPlaceable()){
                
		specialAction action = wasSpecialAction(current, currentBoard.selectedCell);
                current.currentPiece = currentBoard.selectedCell.currentPiece;
                
                chessBoard.unsetMovable(currentBoard.selectedCell);
                currentBoard.selectedCell.setAttacking(null);
                currentBoard.selectedCell.currentPiece = null;
                currentBoard.selectedCell.setBaseColor();
                current.currentPiece.setCell(current);
                
                current.setHoverInvalidColor();
                
                
                currentBoard.pieceSelected=false;
                currentBoard.selectedCell=null;
                current.currentPiece.onMove();
                
                
                if (currentBoard.currentTurn == pieceColor.blue) {
                    currentBoard.currentTurn  = pieceColor.yellow;                    
                } else {
                    currentBoard.currentTurn  = pieceColor.blue;
                }
                
                if (action!=null)   action.postClick();
                calculateAttacks();
            }
        } else{
            currentBoard.pieceSelected=true;
            currentBoard.selectedCell=current;
            chessBoard.setMovableSelected(current);
        }
    }
    
    final  specialAction wasSpecialAction(cell current, cell selectedCell){
        for (int i=0; i<selectedCell.currentPiece.specialMoves.size(); i++){
                if(findPossibleMoves(selectedCell, selectedCell.currentPiece.specialMoves.get(i)).contains(current)){
                    return selectedCell.currentPiece.specialMoves.get(i);
                }
            }
        return null;
    }
}
