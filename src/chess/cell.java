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

import java.awt.Dimension;
import java.util.ArrayList;
import javax.swing.JButton;
public class cell extends JButton{
    double aggressiveness =0.75;
    boolean mouseOver=false;
    boolean placeable = false;
    
    private
    cellColor baseCellColor = cellColor.grey;
    cellColor hoverCellColor = cellColor.white;
    cellColor movableSelectedCellColor = cellColor.green;
    cellColor movableUnselectedCellColor = cellColor.white;
    chessPiece currentPiece;
    ArrayList<cell> attacking;
    boardState currentBoard;
    int size;
    public 
    ArrayList<cell> attackedBy = new ArrayList<>();
    
    public
            int posX, posY;
    public cell(int posX, int posY, int size, boardState board){
        super("");
        this.size = size;
        this.setPreferredSize(new Dimension(size, size));
        this.setFocusable(false);
        this.setBorderPainted(false);
        this.setBorder(null);
        currentPiece = null;
        this.posX=posX;
        this.posY=posY;
        
        currentBoard=board;
        
        setBaseColor();
        
    }
    
    public cell(cell c, boardState board) {
        currentBoard = board;
        size = c.size;
        this.mouseOver = c.mouseOver;
        this.placeable = c.placeable;
        this.attacking = new ArrayList<cell>();
        if(c.attacking != null)
            this.attacking.addAll(c.attacking);
        this.attackedBy = new ArrayList<cell>();
        if(c.attackedBy != null)
            this.attackedBy.addAll(c.attackedBy);
        this.posX = c.posX;
        this.posY = c.posY;
        
        if(c.currentPiece != null)
            switch(c.currentPiece.pieceT)  {
                case rook:
                    this.currentPiece = new rook((rook)c.currentPiece, this);
                    break;
                case knight:
                    this.currentPiece = new knight((knight)c.currentPiece, this);
                    break;
                case bishop:
                    this.currentPiece = new bishop((bishop)c.currentPiece, this);
                    break;
                case queen:
                    this.currentPiece = new queen((queen)c.currentPiece, this);
                    break;
                case king:
                    this.currentPiece = new king((king)c.currentPiece, this);
                    break;
                case pawn:
                    this.currentPiece = new pawn((pawn)c.currentPiece, this);
                    break;
                default:
                    this.currentPiece = null;
            }
        else
            this.currentPiece = null;

    }
    
    final void setPiece(chessPiece piece){
        currentPiece=piece; 
        setBaseColor();
    }
    
    final void setBaseColor(){
        if (currentPiece==null){
            this.setIcon(new chessCellIcon(baseCellColor,pieceType.NULL,pieceColor.NULL, size));       
        }
        else{
            this.setIcon(new chessCellIcon(currentPiece.baseCellColor, currentPiece.pieceT, currentPiece.pieceC, size));
        }
    }
    final void setHoverColor(){
        if (currentPiece==null){
            this.setIcon(new chessCellIcon(hoverCellColor,pieceType.NULL,pieceColor.NULL, size));       
        }
        else{
            this.setIcon(new chessCellIcon(currentPiece.hoverCellColor, currentPiece.pieceT, currentPiece.pieceC, size));
        }
    }
    final void setHoverInvalidColor(){
        this.setIcon(new chessCellIcon(currentPiece.hoverInvalidCellColor, currentPiece.pieceT, currentPiece.pieceC, size));
    }
    
    final void setMovableUnselectedColor(){
        if (currentPiece==null){
            this.setIcon(new chessCellIcon(movableUnselectedCellColor,pieceType.NULL,pieceColor.NULL, size));       
        }
        else{
            this.setIcon(new chessCellIcon(movableUnselectedCellColor, currentPiece.pieceT, currentPiece.pieceC, size));
        }
    }
    
    final void setMovableSelectedColor(){
        if (currentPiece==null){
            this.setIcon(new chessCellIcon(movableSelectedCellColor,pieceType.NULL,pieceColor.NULL, size));       
        }
        else{
            this.setIcon(new chessCellIcon(currentPiece.movableSelectedCellColor, currentPiece.pieceT, currentPiece.pieceC, size));
        }
    }
    
    void setAttacking(ArrayList<cell> list){
        if (attacking!=null) {
            for (cell i: attacking){
                i.attackedBy.remove(this);
            }
        }
        attacking = list;
        if (attacking!=null){
            for (cell i: attacking){
                i.attackedBy.add(this);
            }
        }
    }
    
    final void setPlaceable() {
        this.placeable = true;
    }
    
    final void unsetPlaceable() {
        this.placeable = false;
    }
    
    final boolean getPlaceable() {
        return this.placeable;
    }
}