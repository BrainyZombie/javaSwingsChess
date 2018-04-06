/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package gameSuite;
import chess.Menu;
import java.awt.Dimension;
import java.awt.Toolkit;

/**
 *
 * @author ashir basu
 */
public class Chess {

    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) {
        Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();
        double width = screenSize.getWidth();
        double height = screenSize.getHeight();
        double size = Math.min(width, height);
        size /= 10;
        Menu menu = new Menu((int)size);
    }
    
}
