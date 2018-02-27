/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package chess;

import java.util.ArrayList;

/**
 *
 * @author ashir basu
 */
abstract class chessPiece {
    protected
    pieceColor pieceC;
    pieceType pieceT;
    cellColor baseCellColor;
    cellColor hoverCellColor;
    cellColor hoverInvalidCellColor;
    cellColor selectedCellColor;
    cellColor movableSelectedCellColor;
    ArrayList<pieceMove> movesAllowed = new ArrayList<>();
    ArrayList<specialAction> specialMoves = new ArrayList<>();
    
    cell currentCell;
    
    abstract void setMoves();
    void onMove(){};
    chessPiece(pieceColor pieceC, cell cell){
        this.pieceC=pieceC;
        if (pieceC.name().equals("blue")){
            baseCellColor = cellColor.blue;
        }
        else{
            baseCellColor = cellColor.yellow;
        }
        
        hoverCellColor = cellColor.green;
        hoverInvalidCellColor=cellColor.red;
        movableSelectedCellColor = cellColor.red;
        
        currentCell = cell;
        
        setMoves();
    }
    
    public chessPiece(chessPiece c, cell cell) {
        this.pieceC = c.pieceC;
        this.pieceT = c.pieceT;
        
        if (pieceC.name().equals("blue")){
            baseCellColor = cellColor.blue;
        }
        else{
            baseCellColor = cellColor.yellow;
        }
        
        hoverCellColor = cellColor.green;
        hoverInvalidCellColor=cellColor.red;
        movableSelectedCellColor = cellColor.red;
        currentCell = cell;
        for (pieceMove i : c.movesAllowed){
            movesAllowed.add(new pieceMove(i));
        }
        for (specialAction i: c.specialMoves){
            if (i.type == specialActionType.pawnDouble){
                specialMoves.add(new pawnDouble((pawn) this));
            }
            else if (i.type == specialActionType.castle)
            {
                specialMoves.add(new castleRight((king) this, (castle) i));
            }
        }
        
    }
    
    void setCell(cell cell){
        currentCell = cell;
    }
}

class pawn extends chessPiece{
    boolean hasMoved = false;
    int elPassantableOn = 0;
    pawn (pieceColor pieceC, cell cell){
        super(pieceC, cell);
        pieceT=pieceType.pawn;
    }
    
    public pawn(pawn c, cell cell) {
        super(c, cell);
        hasMoved = c.hasMoved;
    }
    @Override
    void setMoves(){
        movesAllowed.add(new pieceMove(0,1,moveType.cannotCapture));
        movesAllowed.add(new pieceMove(1,1,moveType.onlyCapture));
        movesAllowed.add(new pieceMove(-1,1,moveType.onlyCapture));
        specialMoves.add(new pawnDouble(this));
    }
    @Override
    void onMove(){
        hasMoved=true;
    }
}

class rook extends chessPiece{
    boolean hasMoved = false;
    rook (pieceColor pieceC, cell cell){
        super(pieceC, cell);
        pieceT=pieceType.rook;
    }

    public rook(rook c, cell cell) {
        super(c, cell);
    }
    
    @Override
    void setMoves(){
        movesAllowed.add(new pieceMove(0,1,moveType.recursive));
        movesAllowed.add(new pieceMove(0,-1,moveType.recursive));
        movesAllowed.add(new pieceMove(1,0,moveType.recursive));
        movesAllowed.add(new pieceMove(-1,0,moveType.recursive));
    }
    @Override
    void onMove(){
        hasMoved = true;
    }
}
class bishop extends chessPiece{
    bishop (pieceColor pieceC, cell cell){
        super(pieceC, cell);
        pieceT=pieceType.bishop;
    }
    
    public bishop(bishop c, cell cell) {
        super(c, cell);
    }
    
    @Override
    void setMoves(){
        movesAllowed.add(new pieceMove(1,1,moveType.recursive));
        movesAllowed.add(new pieceMove(1,-1,moveType.recursive));
        movesAllowed.add(new pieceMove(-1,1,moveType.recursive));
        movesAllowed.add(new pieceMove(-1,-1,moveType.recursive));
    }
}
class knight extends chessPiece{
    knight (pieceColor pieceC, cell cell){
        super(pieceC, cell);
        pieceT=pieceType.knight;
    }
    
    public knight(knight c, cell cell) {
        super(c, cell);
    }
    
    @Override
    void setMoves(){
        movesAllowed.add(new pieceMove(1,2,moveType.single));
        movesAllowed.add(new pieceMove(-1,2,moveType.single));
        movesAllowed.add(new pieceMove(2,1,moveType.single));
        movesAllowed.add(new pieceMove(2,-1,moveType.single));
        movesAllowed.add(new pieceMove(-2,1,moveType.single));
        movesAllowed.add(new pieceMove(-2,-1,moveType.single));
        movesAllowed.add(new pieceMove(1,-2,moveType.single));
        movesAllowed.add(new pieceMove(-1,-2,moveType.single));
    }
}

class queen extends chessPiece{
    queen (pieceColor pieceC, cell cell){
        super(pieceC, cell);
        pieceT=pieceType.queen;
    }
    
    public queen(queen c, cell cell) {
        super(c, cell);
    }
    
    @Override
    void setMoves(){
        movesAllowed.add(new pieceMove(0,1,moveType.recursive));
        movesAllowed.add(new pieceMove(0,-1,moveType.recursive));
        movesAllowed.add(new pieceMove(1,0,moveType.recursive));
        movesAllowed.add(new pieceMove(-1,0,moveType.recursive));
        movesAllowed.add(new pieceMove(1,1,moveType.recursive));
        movesAllowed.add(new pieceMove(1,-1,moveType.recursive));
        movesAllowed.add(new pieceMove(-1,1,moveType.recursive));
        movesAllowed.add(new pieceMove(-1,-1,moveType.recursive));
    }
}


class king extends chessPiece{
    boolean hasMoved = false;
    king (pieceColor pieceC, cell cell){
        super(pieceC, cell);
        pieceT=pieceType.king;
    }
    
    public king(king c, cell cell) {
        super(c, cell);
        hasMoved = c.hasMoved;
        
    }
    
    @Override
    void setMoves(){
        movesAllowed.add(new pieceMove(0,1,moveType.single));
        movesAllowed.add(new pieceMove(0,-1,moveType.single));
        movesAllowed.add(new pieceMove(1,0,moveType.single));
        movesAllowed.add(new pieceMove(-1,0,moveType.single));
        movesAllowed.add(new pieceMove(1,1,moveType.single));
        movesAllowed.add(new pieceMove(1,-1,moveType.single));
        movesAllowed.add(new pieceMove(-1,1,moveType.single));
        movesAllowed.add(new pieceMove(-1,-1,moveType.single));
        specialMoves.add(new castleRight(this));
        specialMoves.add(new castleLeft(this));
    }
    
    @Override
    void onMove(){
        hasMoved=true;
        if (pieceC==pieceColor.blue)
            currentCell.currentBoard.kingBlue = currentCell;
        else
            currentCell.currentBoard.kingYellow = currentCell;   
    }
}

class pawnDouble extends specialAction{
        pawn current;
        pawnDouble(pawn curr){
            super(0,2,moveType.cannotCapture);
            current=curr;
            type = specialActionType.pawnDouble;
        }
        boolean validateAction(){
            
            if (current.pieceC.toString() == "blue"){
                if (current.currentCell.posY - 1 < 0) return false;
                if ( !current.hasMoved && current.currentCell.currentBoard.cellGrid[current.currentCell.posY - 1][current.currentCell.posX].currentPiece==null)
                    return true;
                else
                    return false;
            }
            else{
                if (current.currentCell.posY + 1 > 7) return false;
                if (current.currentCell.currentBoard.cellGrid[current.currentCell.posY + 1][current.currentCell.posX].currentPiece==null && !current.hasMoved)
                    return true;
                else
                    return false;
            }
        }
        @Override
        void postClick(){
            current.elPassantableOn = current.currentCell.currentBoard.moveNumber + 1;
        }   
    }
    abstract class castle extends specialAction{
        king current;
        cell castleWith;
        cell castleTo;
        cell castleFrom;
        castle(int x, int y, moveType move, king curr){
            super(x,y,move);
            type = specialActionType.castle;
            current = curr;
        }
        castle(int x, int y, moveType move, king curr, castle previous){
            super(x,y,move);
            type = specialActionType.castle;
            current = curr;
            castleWith = current.currentCell.currentBoard.cellGrid[previous.castleWith.posY][previous.castleWith.posX];
            castleTo = current.currentCell.currentBoard.cellGrid[previous.castleTo.posY][previous.castleTo.posX];
            castleFrom = current.currentCell.currentBoard.cellGrid[previous.castleFrom.posY][previous.castleFrom.posX];
        }
        @Override
        void postClick(){
            castleTo.currentPiece = castleWith.currentPiece;
            castleWith.currentPiece.setCell(castleTo);
            castleWith.currentPiece = null;
            castleTo.currentPiece.onMove();
            castleTo.setBaseColor();
            castleWith.setBaseColor();
        }
    }
    
    class castleRight extends castle{
        
        castleRight(king curr){
            super(2,0,moveType.single, curr);
            castleWith = current.currentCell.currentBoard.cellGrid[current.currentCell.posY][current.currentCell.posX+3];
            castleTo = current.currentCell.currentBoard.cellGrid[current.currentCell.posY][current.currentCell.posX+1];
            castleFrom = current.currentCell;
        }
        castleRight(king curr, castle previous){
            super(2,0,moveType.single, curr, previous);      
        }
        boolean validateAction(){
            if (current.hasMoved){
                return false;
            }
            if (castleWith.currentPiece == null){
                return false;
            }
            if (castleWith.currentPiece.pieceT!= pieceType.rook || castleWith.currentPiece.pieceC != current.pieceC){
                return false;
            }
            
            if (((rook)castleWith.currentPiece).hasMoved){
                return false;
            }
            for (int i=1; i< castleWith.posX - castleFrom.posX; i++){
                if (castleFrom.currentBoard.cellGrid[castleFrom.posY][castleFrom.posX+i].currentPiece!=null){
                return false;
            }
            }
            if (checkIfDifferent(castleFrom)){
                return false;
            }
            if (checkIfDifferent(castleTo)){
                return false;
            }
            if (checkIfDifferent(current.currentCell.currentBoard.cellGrid[current.currentCell.posY][current.currentCell.posX+2])){
                return false;
            }
            
            return true;
        }
        
        boolean checkIfDifferent(cell check){
            for (cell i : check.attackedBy){
                if (i.currentPiece.pieceC != current.pieceC){
                    return true;
                }
            }
            return false;
        }
    }
    
    class castleLeft extends castle{
        castleLeft(king curr){
            super(-2,0,moveType.single, curr);
            castleWith = current.currentCell.currentBoard.cellGrid[current.currentCell.posY][current.currentCell.posX-4];
            castleTo = current.currentCell.currentBoard.cellGrid[current.currentCell.posY][current.currentCell.posX-1];
            castleFrom = current.currentCell;
        }
        
        
        castleLeft(king curr, castle previous){
            super(-2,0,moveType.single, curr, previous);
        }
        boolean validateAction(){
            if (current.hasMoved){
                return false;
            }
            if (castleWith.currentPiece == null){
                return false;
            }
            if (castleWith.currentPiece.pieceT!= pieceType.rook || castleWith.currentPiece.pieceC != current.pieceC){
                return false;
            }
            if (((rook)castleWith.currentPiece).hasMoved){
                return false;
            }
            for (int i=1; i< castleFrom.posX - castleWith.posX; i++){
                if (castleFrom.currentBoard.cellGrid[castleFrom.posY][castleFrom.posX-i].currentPiece!=null){
                    return false;
                }
            }
            if (checkIfDifferent(castleFrom)){
                return false;
            }
            if (checkIfDifferent(castleTo)){
                return false;
            }
            if (checkIfDifferent(current.currentCell.currentBoard.cellGrid[current.currentCell.posY][current.currentCell.posX-2])){
                return false;
            }
            
            return true;
        }
        
        boolean checkIfDifferent(cell check){
            for (cell i : check.attackedBy){
                if (i.currentPiece.pieceC != current.pieceC){
                    return true;
                }
            }
            return false;
        }
        
        
    }   