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
public class boardState {
    protected pieceColor currentTurn = pieceColor.blue;
    protected cell selectedCell = null;
    protected boolean pieceSelected = false;
    protected playerType playerBlue;
    protected playerType playerYellow;
    
    public cell[][] cellGrid = new cell[8][8]; 
    protected cell kingYellow;
    protected cell kingBlue;
    protected boolean isCheckOnBlue = false;
    protected boolean isCheckOnYellow = false;
    protected int moveNumber = 0;
    
    boardState(int cellSize, playerType player1, playerType player2){
        playerBlue = player1;
        playerYellow = player2;
        for (int i=0;i<8;i++){
            for (int j=0;j<8;j++){
                cellGrid[i][j] = new cell(j, i, cellSize, this);
            }
        }
        setInitialConfig();
    }
    
    void duplicate(boardState previousBoard){
        isCheckOnBlue = previousBoard.isCheckOnBlue;
        isCheckOnYellow = previousBoard.isCheckOnYellow;
        currentTurn = previousBoard.currentTurn;
        pieceSelected = false;
        moveNumber = previousBoard.moveNumber;
        playerBlue = previousBoard.playerBlue;
        playerYellow = previousBoard.playerYellow;
        selectedCell = null;
        
        for (int i=0;i<8;i++){
            for (int j=0;j<8;j++){
                cellGrid[i][j].duplicate(previousBoard.cellGrid[i][j]);
            }
        }
            
        kingYellow = cellGrid[previousBoard.kingYellow.posY][previousBoard.kingYellow.posX];
        kingBlue = cellGrid[previousBoard.kingBlue.posY][previousBoard.kingBlue.posX];
    }
    
    
    
    
    
    final void setInitialConfig(){
        for (int i=0;i<8;i++){
            cellGrid[1][i].setPiece(new pawn(pieceColor.yellow, cellGrid[1][i]));
            cellGrid[6][i].setPiece(new pawn(pieceColor.blue, cellGrid[6][i]));
        }
        
        cellGrid[0][0].setPiece(new rook(pieceColor.yellow, cellGrid[0][0]));
        cellGrid[0][1].setPiece(new knight(pieceColor.yellow, cellGrid[0][1]));
        cellGrid[0][2].setPiece(new bishop(pieceColor.yellow, cellGrid[0][2]));
        cellGrid[0][3].setPiece(new queen(pieceColor.yellow, cellGrid[0][3]));
        cellGrid[0][4].setPiece(new king(pieceColor.yellow, cellGrid[0][4]));
        kingYellow=cellGrid[0][4];
        cellGrid[0][5].setPiece(new bishop(pieceColor.yellow, cellGrid[0][5]));
        cellGrid[0][6].setPiece(new knight(pieceColor.yellow, cellGrid[0][6]));
        cellGrid[0][7].setPiece(new rook(pieceColor.yellow, cellGrid[0][7]));
        
        
        cellGrid[7][0].setPiece(new rook(pieceColor.blue, cellGrid[7][0]));
        cellGrid[7][1].setPiece(new knight(pieceColor.blue, cellGrid[7][1]));
        cellGrid[7][2].setPiece(new bishop(pieceColor.blue, cellGrid[7][2]));
        cellGrid[7][3].setPiece(new queen(pieceColor.blue, cellGrid[7][3]));
        cellGrid[7][4].setPiece(new king(pieceColor.blue, cellGrid[7][4]));
        kingBlue = cellGrid[7][4];
        cellGrid[7][5].setPiece(new bishop(pieceColor.blue, cellGrid[7][5]));
        cellGrid[7][6].setPiece(new knight(pieceColor.blue, cellGrid[7][6]));
        cellGrid[7][7].setPiece(new rook(pieceColor.blue, cellGrid[7][7]));
    }
}
