package view.gui;

import javax.imageio.ImageIO;
import javax.swing.*;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

public class GUIMap extends JPanel {
    private BufferedImage emptyMap;
    private GraphicsConfiguration gc;
    private JFrame frame;
    private JLabel label;
    private JPanel panel;

    public GUIMap(String path){
        try {
            emptyMap = ImageIO.read(new File(path));
        } catch (IOException e) {
            System.out.println("Errore su file da aprire.");
        }

        frame = new JFrame("label");
        Image imgMap = emptyMap.getScaledInstance(604,800,Image.SCALE_SMOOTH);
        ImageIcon iconMap = new ImageIcon(imgMap);

        label = new JLabel(iconMap);
        panel = new JPanel();
        panel.add(label);
        frame.add(panel);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.pack();
        frame.setVisible(true);
    }

    public static void main(String[] args){
        new GUIMap("src/resources/GUImap1.png");
    }
}
