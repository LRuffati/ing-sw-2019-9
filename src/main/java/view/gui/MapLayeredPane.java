package view.gui;

import javax.imageio.ImageIO;
import javax.swing.*;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

public class MapLayeredPane {

    private static JFrame window = new JFrame("game");
    private JLabel titlesetLayer1[] = new JLabel[1];
    private JLabel titlesetLayer2[] = new JLabel[1];
    private JLabel titlesetLayer3[] = new JLabel[1];
    private JLabel titlesetLayer4[] = new JLabel[0];
    private JLabel titlesetLayer5[] = new JLabel[0];
    private JLabel characters[] = new JLabel[2];

    public MapLayeredPane(){
        window.setDefaultCloseOperation( JFrame.EXIT_ON_CLOSE );
        window.setLocationRelativeTo(null);
        window.setResizable(true);

        // draw layer5 (on the layer4)

        // draw layer4 (on the layer3)

        // draw layer3 (on the characters)
        JButton yellowbutton = new JButton(new ImageIcon("src/resources/gui/yellowAmmo.png"));
        titlesetLayer3[0] = new JLabel();
        titlesetLayer3[0].add(yellowbutton);
        titlesetLayer3[0].setBounds(130, 120, 126, 160);
        window.add(titlesetLayer3[0]);

        // draw the charaters
        characters[1] = new JLabel(new ImageIcon("src/resources/gui/redAmmo.png"));
        characters[1].setBounds(600, 500, 100, 100);
        window.add(characters[1]);

        characters[0] = new JLabel(new ImageIcon("src/resources/gui/redAmmo.png"));
        characters[0].setBounds(100, 100, 100, 100);
        window.add(characters[0]);

        // draw layer2 (under the characters)
        titlesetLayer2[0] = new JLabel(new ImageIcon("src/resources/gui/blueAmmo.png"));
        titlesetLayer2[0].setBounds(570, 400, 126, 260);
        window.add(titlesetLayer2[0]);


        titlesetLayer1[0] = new JLabel(new ImageIcon("src/resources/gui/GUImap1.png"));
        titlesetLayer1[0].setBounds(1000,1000,1000,1000);
        window.add(titlesetLayer1[0]);

        /*
        titlesetLayer1[numberSquareX - 1][numberSquareY - 1] = new JLabel(new ImageIcon("grass_1.png"));
        titlesetLayer1[numberSquareX - 1][numberSquareY - 1].setBounds(numberSquareX * 20, numberSquareY * 20, 20, 20);
        window.add(titlesetLayer1[numberSquareX - 1][numberSquareY - 1]);
         */
        window.setVisible(true);
    }

    public static void main(String[] args){
        new MapLayeredPane();
    }
}
