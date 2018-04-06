/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package chess;

import java.awt.Color;
import java.awt.FlowLayout;
import static java.lang.Thread.sleep;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.JFrame;
import javax.swing.JPanel;

/**
 *
 * @author pranav
 */
public class ChessGame extends JFrame implements Runnable {

    JPanel mainPanel = new JPanel(new FlowLayout(FlowLayout.CENTER, 5, 5));
    int size;
    chessBoard board;
    SidePanel panel;
    pieceColor currentTurn;
    public ChessGame(int size, String bluePlayer, String yellowPlayer, chessBoard here) {
        this.size = size;
        this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        this.setResizable(false);
        mainPanel.setBackground(Color.black);
        mainPanel.add(board = here);
        mainPanel.add(panel = new SidePanel(size, bluePlayer, yellowPlayer));
        this.add(mainPanel);
        this.pack();		
        this.setLocationRelativeTo(null);
        this.setVisible(true);
    }
    @Override
    public void run(){
        while (true){
            if (currentTurn != board.mainBoard.currentTurn){
        currentTurn = board.mainBoard.currentTurn;
        if (currentTurn == pieceColor.blue){
            panel.blueTimer.startTimer();
            panel.yellowTimer.pauseTimer();
        }
        else if (currentTurn == pieceColor.yellow){
            panel.blueTimer.pauseTimer();
            panel.yellowTimer.startTimer();
        }
        else{
            panel.blueTimer.pauseTimer();
            panel.yellowTimer.pauseTimer();
        }
            }
        try {
                sleep(50);
            } catch (InterruptedException ex) {
                Logger.getLogger(ChessAI.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
    }
}
