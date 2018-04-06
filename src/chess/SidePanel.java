package chess;

/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

/**
 *
 * @author pranav
 */

import java.awt.Color;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.Font;
import java.io.File;
import java.io.InputStream;
import javax.swing.BoxLayout;
import javax.swing.JLabel;
import javax.swing.JPanel;

public class SidePanel extends JPanel {
    
    JLabel bluePlayer = new JLabel("Blue");
    JLabel yellowPlayer = new JLabel("Yellow");
    JPanel bluePanel = new JPanel();
    JPanel yellowPanel = new JPanel();
    Color panelColor = new Color(170, 170, 170);
    Font panelFont = new Font("Verdana", Font.PLAIN, 18);

        ChessTimer blueTimer;
        ChessTimer yellowTimer ;
    public SidePanel(int size, String bPlayer, String yPlayer) {
        blueTimer = new ChessTimer(size);
        yellowTimer = new ChessTimer(size);
        
        this.setLayout(new FlowLayout(FlowLayout.CENTER, 20, 20));
        bluePlayer.setText(bPlayer);
        yellowPlayer.setText(yPlayer);
        BoxLayout blueLayout = new BoxLayout(bluePanel, BoxLayout.Y_AXIS);
        BoxLayout yellowLayout = new BoxLayout(yellowPanel, BoxLayout.Y_AXIS); 
        bluePanel.setLayout(blueLayout);
        yellowPanel.setLayout(yellowLayout);
        bluePlayer.setFont(panelFont);
        bluePlayer.setForeground(panelColor);
        yellowPlayer.setForeground(panelColor);
        yellowPlayer.setFont(panelFont);
        bluePanel.add(bluePlayer);
        yellowPanel.add(yellowPlayer);
        this.add(yellowPanel);
        this.add(bluePanel);
        bluePanel.setBackground(Color.black);
        yellowPanel.setBackground(Color.black);
        bluePanel.add(blueTimer);
        yellowPanel.add(yellowTimer);
        this.setBackground(Color.black);
        this.setPreferredSize(new Dimension(size * 4, size * 8));
        bluePanel.setPreferredSize(new Dimension(size * 4, size * 4));
        yellowPanel.setPreferredSize(new Dimension(size * 4, size * 4));
    }
    
}
