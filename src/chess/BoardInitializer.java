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
public class BoardInitializer extends Thread{
    boolean ready;
    chessBoard board;
    int size;
    @Override
    public void run(){
        board = new chessBoard(size);
        ready = true;
    }
    BoardInitializer(int size){
        this.size = size;
    }
}
