/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package chess;

import java.util.ArrayList;
import javax.swing.JOptionPane;

/**
 *
 * @author student
 */
public class chessPlayer {
    
    public boardState currentBoard;
    chessPlayer testBoard;
    chessPlayer AI;
    moveTracker tracker;
    
    chessPlayer(boardState board, boolean createTestBoard, int AIDepth){
        currentBoard = board;
        
        if (createTestBoard){
            testBoard = new chessPlayer(new boardState(currentBoard.cellGrid[0][0].size, currentBoard.playerBlue, currentBoard.playerYellow), false, 0);
        }
        
        if (AIDepth>1){
            AI = new chessPlayer(new boardState(currentBoard.cellGrid[0][0].size, currentBoard.playerBlue, currentBoard.playerYellow), true, AIDepth -1);
        }
        else if (AIDepth == 1){
             AI = new chessPlayer(new boardState(currentBoard.cellGrid[0][0].size, currentBoard.playerBlue, currentBoard.playerYellow), false, 0);
        }
        
        for (cell[] i : currentBoard.cellGrid){
            for (cell j : i){
                j.addMouseListener(new java.awt.event.MouseAdapter() {    
                    @Override
                    public void mousePressed(java.awt.event.MouseEvent evt){
                        cellClicked(j);
                    }
                });
            }
        }
        calculateAttacks();
    }
    
    
    private void cellClicked(cell current){
        
        if (!currentBoard.pieceSelected && current.currentPiece!=null){
            if (currentBoard.currentTurn == pieceColor.blue && currentBoard.playerBlue == playerType.ai) return;
            if (currentBoard.currentTurn == pieceColor.yellow && currentBoard.playerYellow == playerType.ai) return;
            if (current.currentPiece.pieceC != currentBoard.currentTurn){
                current.setBaseColor();
                JOptionPane.showMessageDialog(null,"It is " + currentBoard.currentTurn.name()+"'s turn");
                return;
            }
           else if (current.attacking.isEmpty()){
                current.setBaseColor();
                return;                        
            }
            else{
                moveHandler(current);
            }
        }
        else if (currentBoard.pieceSelected){
            moveHandler(current);
        }
    }
     
    
    
    final  void calculateAttacks(){
        for (int i=0;i<8;i++){
            for (int j=0;j<8;j++){
                currentBoard.cellGrid[i][j].setAttacking(populateMoves(currentBoard.cellGrid[i][j]));
            }
        }
         
    }
    
    final void setValidMoves(){
        currentBoard.mate = true;
        ArrayList<cell> moves = new ArrayList<>();
        for (int i=0;i<8;i++){
            for (int j=0;j<8;j++){
                if (currentBoard.cellGrid[i][j].currentPiece!=null&&currentBoard.cellGrid[i][j].currentPiece.pieceC == currentBoard.currentTurn){
                    moves.clear();
                    moves.addAll(getValidMoves(i,j, testBoard));
                    currentBoard.cellGrid[i][j].setAttacking(moves);
                    if (!moves.isEmpty()){
                       currentBoard.mate = false;
                    }
                }
            }
        }
    }
    
    final ArrayList<cell> getValidMoves(int y, int x, chessPlayer testBoard){
        if (currentBoard.cellGrid[y][x].attacking == null || currentBoard.cellGrid[y][x].currentPiece == null)
            return new ArrayList<>();
        ArrayList<cell> validMoves = new ArrayList<>();
        
        
        for (cell i:currentBoard.cellGrid[y][x].attacking){
            testBoard.currentBoard.duplicate(currentBoard);
            testBoard.doMove(testBoard.currentBoard.cellGrid[y][x], testBoard.currentBoard.cellGrid[i.posY][i.posX]);
            testBoard.detectCheck();
            if (!(testBoard.currentBoard.currentTurn==pieceColor.yellow?testBoard.currentBoard.isCheckOnBlue:testBoard.currentBoard.isCheckOnYellow)){
                validMoves.add(i);
            }
        }
        return validMoves;
    }
    
    
    final  ArrayList<cell> populateMoves(cell current){
        ArrayList<cell> moves = new ArrayList<>();    
        if (current.currentPiece==null) {
            return moves;
        } else {
            
            for (int i=0; i<current.currentPiece.specialMoves.size(); i++){
                moves.addAll(findPossibleMoves(current, current.currentPiece.specialMoves.get(i)));
            }
            for (int i=0; i<current.currentPiece.movesAllowed.size(); i++){
                moves.addAll(findPossibleMoves(current, current.currentPiece.movesAllowed.get(i)));
            }
        }
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
                        if(currentBoard.cellGrid[y][x].currentPiece.pieceC!=current.currentPiece.pieceC) {
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
                    } else if(currentBoard.cellGrid[y][x].currentPiece.pieceC!=current.currentPiece.pieceC) {
                            moves.add(currentBoard.cellGrid[y][x]);
                    }
                } else if (currentMove.type == moveType.cannotCapture) {
                    if (currentBoard.cellGrid[y][x].currentPiece==null) {
                        moves.add(currentBoard.cellGrid[y][x]);
                    }
                } else if (currentMove.type == moveType.onlyCapture && currentBoard.cellGrid[y][x].currentPiece!=null) {
                    if(currentBoard.cellGrid[y][x].currentPiece.pieceC!=current.currentPiece.pieceC) {
                            moves.add(currentBoard.cellGrid[y][x]);
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
                setMovableUnselected(current);
                currentBoard.pieceSelected=false;
                currentBoard.selectedCell=null;
            }
            else if(current.attackedBy.contains(currentBoard.selectedCell)){
                if (currentBoard.playerBlue != playerType.ai && currentBoard.playerYellow != playerType.ai)
                    updatePreviousBoardStates();
                unsetMovable(currentBoard.selectedCell);
		doMove(currentBoard.selectedCell, current);
                currentBoard.pieceSelected=false;
                currentBoard.selectedCell=null;
                setValidMoves();
                detectCheck();
                if (currentBoard.mate){
                    if ((currentBoard.currentTurn==pieceColor.blue?currentBoard.isCheckOnBlue:currentBoard.isCheckOnYellow))
                        JOptionPane.showMessageDialog(null,"CheckMate detected on " + currentBoard.currentTurn.toString());
                    else
                        JOptionPane.showMessageDialog(null,"StaleMate detected on " + currentBoard.currentTurn.toString());
                }
                else if(currentBoard.isCheckOnYellow)
                    JOptionPane.showMessageDialog(null,"Yellow in check");
                else if(currentBoard.isCheckOnBlue)
                    JOptionPane.showMessageDialog(null,"Blue in check");
                resetColors();
            }
        } else{
            currentBoard.pieceSelected=true;
            currentBoard.selectedCell=current;
            setMovableSelected(current);
        }
    }
    final  void moveHandler(cell from, cell to){
        updatePreviousBoardStates();
	doMove(from, to);
        setValidMoves();
        detectCheck();
        if (currentBoard.mate){
            if ((currentBoard.currentTurn==pieceColor.blue?currentBoard.isCheckOnBlue:currentBoard.isCheckOnYellow)){
                String a = currentBoard.currentTurn.name();
                stopGame();
                JOptionPane.showMessageDialog(null,"CheckMate detected on " + a);
            }
            else{
                String a = currentBoard.currentTurn.name();
                stopGame();
                JOptionPane.showMessageDialog(null,"StaleMate detected on " + a);
            }
        }
        else if(currentBoard.isCheckOnYellow)
            JOptionPane.showMessageDialog(null,"Yellow in check");
        else if(currentBoard.isCheckOnBlue)
            JOptionPane.showMessageDialog(null,"Blue in check");
        resetColors();
    }
    final void doMove(cell moveFrom, cell moveTo){
        specialAction action = wasSpecialAction(moveTo, moveFrom);
        moveTo.currentPiece = moveFrom.currentPiece;
        
        moveFrom.setAttacking(new ArrayList<>());
        moveFrom.currentPiece = null;
        moveTo.currentPiece.setCell(moveTo);
        
        
                
        moveTo.currentPiece.onMove();
                
        if (action!=null)   action.postClick();
        
        if (currentBoard.currentTurn == pieceColor.blue) {
            currentBoard.currentTurn  = pieceColor.yellow;                    
        } else {
            currentBoard.currentTurn  = pieceColor.blue;
        }
        currentBoard.moveNumber++;
        calculateAttacks();
    }
    
    final  specialAction wasSpecialAction(cell current, cell selectedCell){
        if (selectedCell.currentPiece==null)
            System.out.println("wtf");
        for (int i=0; i<selectedCell.currentPiece.specialMoves.size(); i++){
                if(findPossibleMoves(selectedCell, selectedCell.currentPiece.specialMoves.get(i)).contains(current)){
                    return selectedCell.currentPiece.specialMoves.get(i);
                }
            }
        return null;
    }
    
    final void detectCheck(){
        currentBoard.isCheckOnBlue = !(currentBoard.kingBlue.attackedBy.isEmpty());
        currentBoard.isCheckOnYellow = !(currentBoard.kingYellow.attackedBy.isEmpty());
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
    
    
    final  void unsetMovable(cell current){
        if (current.attacking==null || current.attacking.isEmpty()) {
            return;
        }
        for (cell i : current.attacking){
            i.setBaseColor();
        }
    }
    
    
    final void setMovableSelected(cell current){
        ArrayList<cell> moves = current.attacking;
        if (moves==null) {
            return;
        }
        for (int i=0;i<moves.size();i++){
            moves.get(i).setMovableSelectedColor();
        }
        current.setMovableUnselectedColor();
    }
    
    final void resetColors(){
        for (cell[] i: currentBoard.cellGrid){
            for (cell j: i){
                j.setBaseColor();
            }
        }
    }
    
    final void updatePreviousBoardStates(){
            tracker.addBoard(currentBoard);
    }
    
    final void stopGame(){
        currentBoard.currentTurn = pieceColor.NULL;
    }
}
