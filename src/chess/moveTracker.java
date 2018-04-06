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
    boardState previousBoard;
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
        current.next = null;
        current.previous = null;
        currentBoard= new boardState(current.cellGrid[0][0].size, current.playerBlue, current.playerYellow);
        currentBoard.duplicate(current);
    }
    void addBoard(boardState current){
        temp = current;
        active = true;
    }
    void update(){
        previousBoard = currentBoard;
        currentBoard = temp;
        currentBoard.boardNumber++;        
        temp = new boardState(currentBoard.cellGrid[0][0].size, currentBoard.playerBlue, currentBoard.playerYellow);
        temp.duplicate(currentBoard);
        currentBoard = temp;
        currentBoard.next = null;
        currentBoard.previous = previousBoard;
        previousBoard.next = currentBoard;
        active = false;
        for (;temp!=null;temp = temp.previous){
            System.out.print(temp.boardNumber + "->");
        }
        System.out.println();
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
