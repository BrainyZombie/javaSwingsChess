/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package chess;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import static java.lang.Thread.sleep;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextField;

/**
 *
 * @author pranav
 */
public class Menu extends JFrame {

    JLabel test = new JLabel("Menu");
    JPanel mainPanel = new JPanel(new FlowLayout(FlowLayout.CENTER, 5, 5));
    int size;
    ChessGame game;
    JTextField bluePlayerName = new JTextField();
    JTextField yellowPlayerName = new JTextField();
    JButton startButton = new JButton("Start");
    ListenForButton buttonClicked = new ListenForButton();
    chessBoard board;
    BoardInitializer init;
    public Menu(int size) {
        init = new BoardInitializer(size);
        init.start();
        this.size = size;
        this.setSize(new Dimension(200, 120));
	this.setResizable(false);
	this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        bluePlayerName.setPreferredSize(new Dimension(90, 30));
        yellowPlayerName.setPreferredSize(new Dimension(90, 30));
        mainPanel.add(bluePlayerName);
        mainPanel.add(yellowPlayerName);
        mainPanel.add(startButton);
        startButton.addActionListener(buttonClicked);
        this.add(mainPanel);
        this.setLocationRelativeTo(null);
        this.setVisible(true);
    }
    
    private void startGame(String bluePlayer, String yellowPlayer) {
        while (!init.ready){
            try {
                sleep(50);
            } catch (InterruptedException ex) {
                Logger.getLogger(ChessAI.class.getName()).log(Level.SEVERE, null, ex);
            }
        };
        
        game = new ChessGame(size, bluePlayer, yellowPlayer, init.board);
        new Thread(game).start();
        this.setVisible(false);
    }
    
    class ListenForButton implements ActionListener {

	@Override
    	public void actionPerformed(ActionEvent e) {

            if(e.getSource() == startButton)
                startGame(bluePlayerName.getText(), yellowPlayerName.getText());
	}
        
    }
    
}
