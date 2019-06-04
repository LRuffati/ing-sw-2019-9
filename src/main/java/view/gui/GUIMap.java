package view.gui;

import javax.imageio.ImageIO;
import javax.swing.*;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

public class GUIMap extends JPanel {
    private static BufferedImage emptyMap;

    public GUIMap(String path){
        try {
            emptyMap = ImageIO.read(new File(path));
        } catch (IOException e) {
            System.out.println("Errore su file da aprire.");
        }
        JLabel label = getLabel();
        Image dEmptyMap = emptyMap.getScaledInstance(label.getWidth(),label.getHeight(),Image.SCALE_SMOOTH);
        ImageIcon mapIcon = new ImageIcon(dEmptyMap);

    }

    private JLabel getLabel(){
        JLabel label = new JLabel();
        label.setBounds(339,518,339,518);
        return label;
    }


}
