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
public class moveTracker extends Thread{
    boardState temp;
    boardState currentBoard;
    boolean active = false;
    @Override
    public void run(){
        while (true){
            if (active)
                update();
            try {
                sleep(10);
            } catch (InterruptedException ex) {
            }
        }
    }
    moveTracker(boardState current){
        currentBoard= current;
        update();
    }
    void addBoard(boardState current){
        currentBoard= current;
        active = true;
    }
    void update(){
        currentBoard.boardNumber++;
        currentBoard.next = null;
        temp = new boardState(currentBoard.cellGrid[0][0].size, currentBoard.playerBlue, currentBoard.playerYellow);
        temp.duplicate(currentBoard);
        if (temp.previous != null)
            temp.previous.next = temp;
        currentBoard.previous = temp;
        currentBoard = temp;
        active = false;
        
    }
    
    
    
    // dev
    final synchronized void dispBoard(cell[][] game) {
        for(int i = 0; i < game.length; i++) {
            for(int j = 0; j < game.length; j++) {
                if(game[i][j].currentPiece != null)
                    System.out.print("" + game[i][j].currentPiece.pieceT.toString().charAt(0) + "\t");
                else System.out.print("____\t");
            }
            System.out.println();
        }
        System.out.println();
    }
    
}
