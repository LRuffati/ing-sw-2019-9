package view.gui;

import javax.imageio.ImageIO;
import javax.swing.*;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

public class GUIMap extends JPanel {
    private Framework controller;
    private BufferedImage emptyMap;

    public GUIMap(){
        try {
            this.emptyMap = ImageIO.read(new File("src/resources/firstMap.png"));
        } catch (IOException e) {
            System.out.println("Errore su file da aprire.");
        }
    }

    public Component addMapToLabel(){
        JLabel label = new JLabel(new ImageIcon(emptyMap));
        return add(label);
    }

    public static void main(String[] str){
        GUIMap gui = new GUIMap();
        Component component = gui.addMapToLabel();
    }
}
