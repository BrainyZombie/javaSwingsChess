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
        
        movesAllowed.addAll(c.movesAllowed);
        specialMoves.addAll(c.specialMoves);
        
        currentCell = cell;
    }
    
    void setCell(cell cell){
        currentCell = cell;
    }
}

class pawn extends chessPiece{
    boolean hasMoved = false;
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
    
    
    
    
    private class pawnDouble extends specialAction{
        pawn current;
        pawnDouble(pawn curr){
            super(0,2,moveType.cannotCapture);
            current=curr;
        }
        boolean validateAction(){
            
            if (current.pieceC.toString() == "blue"){
                if (current.currentCell.posY - 1 < 0) return false;
                if ( !current.hasMoved && currentCell.currentBoard.cellGrid[current.currentCell.posY - 1][current.currentCell.posX].currentPiece==null)
                    return true;
                else
                    return false;
            }
            else{
                if (current.currentCell.posY + 1 > 7) return false;
                if (currentCell.currentBoard.cellGrid[current.currentCell.posY + 1][current.currentCell.posX].currentPiece==null && !current.hasMoved)
                    return true;
                else
                    return false;
            }
        }
        @Override
        void postClick(){}   
    }

}

class rook extends chessPiece{
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

