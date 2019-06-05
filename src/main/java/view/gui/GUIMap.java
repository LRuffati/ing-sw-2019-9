package view.gui;

import javax.imageio.ImageIO;
import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

public class GUIMap extends JPanel {
    private BufferedImage emptyMap;
    private BufferedImage ammoTile;
    private JButton ammoButton;
    private GraphicsConfiguration gc;
    private JFrame frame;
    private JLabel label;
    private JPanel panel;
    private Graphics g;

    public GUIMap(String path){
        try {
            emptyMap = ImageIO.read(new File(path));
        } catch (IOException e) {
            System.out.println("Errore su file da aprire.");
        }

        try {
            ammoTile = ImageIO.read(new File("src/resources/greenAmmo.png"));
        } catch (IOException e) {
            System.out.println("Errore su file da aprire.");
        }

        frame = new JFrame("label");
        Image imgMap = emptyMap.getScaledInstance(emptyMap.getWidth()/2,emptyMap.getHeight()/2,Image.SCALE_SMOOTH);
        ImageIcon iconMap = new ImageIcon(imgMap);
        Image imgAmmo = ammoTile.getScaledInstance(ammoTile.getWidth()/2,ammoTile.getHeight()/2,Image.SCALE_SMOOTH);
        ImageIcon iconAmmo = new ImageIcon(imgAmmo);

        ammoButton = new JButton();
        ammoButton.setIcon(iconAmmo);
        //come aggiungere azione a bottone:
        ammoButton.addActionListener(e -> System.out.println("prova bottone ammotile stanza verde"));
        ammoButton.setContentAreaFilled( false );
        ammoButton.setBorder( null );
        ammoButton.setBounds(iconAmmo.getIconWidth(),iconAmmo.getIconHeight(),0,0);


        label = new JLabel(iconMap);
        label.setLayout(new FlowLayout(FlowLayout.LEFT,140,62));
        label.add(ammoButton);
        frame.add(label);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.pack();
        frame.setVisible(true);
    }

    public static void main(String[] args){
        new GUIMap("src/resources/GUImap1.png");
    }
}
