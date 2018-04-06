/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package chess;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.Font;
import java.time.Duration;
import java.util.Timer;
import java.util.TimerTask;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.JLabel;
import javax.swing.JPanel;

/**
 *
 * @author pranav
 */
public class ChessTimer extends JPanel {
    
    Timer timer = new Timer();
    int secondsLeft = 20 * 60;
    JLabel time = new JLabel("20:00");
    Color timerColor = new Color(170, 170, 170);
    Font timerFont = new Font("Verdana", Font.PLAIN, 35);
    Duration duration;
            
    public ChessTimer(int size) {
        this.setLayout(new FlowLayout(FlowLayout.CENTER, 20, 40));
        this.setBackground(Color.black);
        time.setFont(timerFont);
        time.setForeground(timerColor);
        this.setPreferredSize(new Dimension(size * 3, size * 3));
        this.add(time);
    }
    
    public void startTimer() {
        timer = new Timer();
        timer.scheduleAtFixedRate(new TimerTask() {
            public void run() {
                duration = Duration.ofSeconds(secondsLeft);
                if (--secondsLeft < 0)
                    timer.cancel();
                else
                    time.setText("" + duration.toMinutes() + ":" + (secondsLeft - duration.toMinutes() * 60 + 1));
            }
        }, 0, 1000);
    }
    
    public void pauseTimer() {
        timer.cancel();
    }
    
}
