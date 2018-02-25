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

import java.awt.Component;
import java.awt.Dimension;
import java.awt.Graphics;
import java.util.ArrayList;
import javax.swing.Icon;
import javax.swing.ImageIcon;
import javax.swing.JButton;
public class cell extends JButton{
    boolean placeable = false;
    
    private
    cellColor baseCellColor = cellColor.grey;
    cellColor hoverCellColor = cellColor.white;
    cellColor movableSelectedCellColor = cellColor.green;
    cellColor movableUnselectedCellColor = cellColor.white;
    chessPiece currentPiece;
    public ArrayList<cell> attacking ;
    boardState currentBoard;
    int size;
    public 
    ArrayList<cell> attackedBy ;
    
    public  int posX, posY;
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
        attacking = new ArrayList<cell>();
        attackedBy = new ArrayList<cell>();
        attacking.clear();
        attackedBy.clear();
        setBaseColor();
        
    }
    
    public void duplicate(cell c) {
        attacking.clear();
        attackedBy.clear();
        if(!c.attacking.isEmpty()){
            for (cell i: c.attacking){
                attacking.add(currentBoard.cellGrid[i.posY][i.posX]);
            }
        }
        if(!c.attackedBy.isEmpty()){
            for (cell i: c.attackedBy){
                attackedBy.add(currentBoard.cellGrid[i.posY][i.posX]);
            }
        }
        
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
                    System.out.println("lolwa yeh kya hua lmao");
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
        
        for (cell i: attacking){
            i.attackedBy.remove(this);
        }
        attacking.clear();
        if (list!=null&&!list.isEmpty()){
            attacking.addAll(list);
        }
    
        for (cell i: attacking){
            i.attackedBy.add(this);
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
    
    private class chessCellIcon implements Icon{
        private ImageIcon cell;
        private ImageIcon piece;
        private Boolean pieceSet = false;
        chessCellIcon(cellColor cellC, pieceType pieceT, pieceColor pieceC, int size){
            String cellIcon = "/tile_" + cellC.name()+".png";
            cell = new ImageIcon(new ImageIcon(getClass().getResource(cellIcon)).getImage().getScaledInstance(size, size, java.awt.Image.SCALE_SMOOTH));
        
            if (!pieceT.name().equals("NULL")){
                pieceSet=true;
                String pieceIcon = "/"+pieceT.name()+"_"+pieceC.toString()+".png";
                piece = new ImageIcon(new ImageIcon(getClass().getResource(pieceIcon)).getImage().getScaledInstance(size, size, java.awt.Image.SCALE_SMOOTH));
            }
        }
        @Override
        public int getIconHeight() {
            if (pieceSet)
                return Math.max(cell.getIconHeight(), piece.getIconHeight());
            else
                return cell.getIconHeight();
        }
        @Override
        public int getIconWidth() {
            if (pieceSet)
                return Math.max(cell.getIconWidth(), piece.getIconWidth());
            else
                return cell.getIconWidth();
        }
        @Override
        public void paintIcon(Component c, Graphics g, int x, int y) {
            cell.paintIcon(c, g, x, y);
            if (pieceSet)
                piece.paintIcon(c, g, x, y);
        }
    }
}