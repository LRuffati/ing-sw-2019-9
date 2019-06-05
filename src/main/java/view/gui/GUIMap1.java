package view.gui;

import gamemanager.ParserConfiguration;

import javax.imageio.ImageIO;
import javax.swing.*;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

public class GUIMap1 extends JPanel {

    private static final String SRC = ParserConfiguration.parsePath("GuiDirectoryPath") + "/";

    private BufferedImage emptyMap;
    private BufferedImage ammoTile;
    private JPanel panel;
    private Graphics g;

    public GUIMap1(String path){
        try {
            emptyMap = ImageIO.read(new File(path));
        } catch (IOException e) {
            System.out.println("Errore su file da aprire.");
        }

        JFrame frame = new JFrame("label");
        Image imgMap = emptyMap.getScaledInstance(emptyMap.getWidth()/2,emptyMap.getHeight()/2,Image.SCALE_SMOOTH);
        ImageIcon iconMap = new ImageIcon(imgMap);


        JLabel label = new JLabel(iconMap);
        setGreenAmmoTile(label);
        setYellowAmmoTile(label);
        frame.add(label);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.pack();
        frame.setVisible(true);
    }

    public static void main(String[] args){
        new GUIMap1(SRC + "GUImap1.png");
    }

    private void setGreenAmmoTile(JLabel label){
        try {
            ammoTile = ImageIO.read(new File(SRC + "greenAmmo.png"));
        } catch (IOException e) {
            System.out.println("Errore su file da aprire.");
        }
        Image imgAmmo = ammoTile.getScaledInstance(ammoTile.getWidth()/2,ammoTile.getHeight()/2,Image.SCALE_SMOOTH);
        ImageIcon iconAmmo = new ImageIcon(imgAmmo);
        JButton ammoButton = new JButton();
        ammoButton.setIcon(iconAmmo);
        //come aggiungere azione a bottone:
        ammoButton.addActionListener(e -> System.out.println("prova bottone ammotile stanza verde"));
        ammoButton.setContentAreaFilled( false );
        ammoButton.setBorder( null );
        ammoButton.setBounds(iconAmmo.getIconWidth(),iconAmmo.getIconHeight(),0,0);
        label.setLayout(new FlowLayout(FlowLayout.LEFT,300,62));
        label.add(ammoButton);
    }

    private void setYellowAmmoTile(JLabel label){
        try {
            ammoTile = ImageIO.read(new File(SRC + "yellowAmmo.png"));
        } catch (IOException e) {
            System.out.println("Errore su file da aprire.");
        }
        Image imgAmmo = ammoTile.getScaledInstance(ammoTile.getWidth()/2,ammoTile.getHeight()/2,Image.SCALE_SMOOTH);
        ImageIcon iconAmmo = new ImageIcon(imgAmmo);
        JButton ammoButton = new JButton();
        ammoButton.setIcon(iconAmmo);
        //come aggiungere azione a bottone:
        ammoButton.addActionListener(e -> System.out.println("prova bottone ammotile stanza gialla"));
        ammoButton.setContentAreaFilled( false );
        ammoButton.setBorder( null );
        ammoButton.setBounds(iconAmmo.getIconWidth(),iconAmmo.getIconHeight(),0,0);
        label.setLayout(new FlowLayout(FlowLayout.LEADING,164,161));
        label.add(ammoButton);
    }





}
