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

    public GUIMap1(){


        try {
            ammoTile = ImageIO.read(new File(SRC + "greenAmmo.png"));
        } catch (IOException e) {
            System.out.println("Errore su file da aprire.");
        }

        JFrame frame = new JFrame("label");


        JLabel label = new JLabel();
        setGreenAmmoTile(label,"src/resources/gui/firstMap/tile1.png");
        setYellowAmmoTile(label);
        setGreenAmmoTile(label,"src/resources/gui/firstMap/tile3.png");
        setGreenAmmoTile(label,"src/resources/gui/firstMap/tile4.png");
        setGreenAmmoTile(label,"src/resources/gui/firstMap/tile5.png");
        setGreenAmmoTile(label,"src/resources/gui/firstMap/tile6.png");
        setGreenAmmoTile(label,"src/resources/gui/firstMap/tile7.png");
        setGreenAmmoTile(label,"src/resources/gui/firstMap/tile8.png");
        setGreenAmmoTile(label,"src/resources/gui/firstMap/tile9.png");
        setGreenAmmoTile(label,"src/resources/gui/firstMap/tile10.png");
        setGreenAmmoTile(label,"src/resources/gui/firstMap/tile11.png");
        setGreenAmmoTile(label,"src/resources/gui/firstMap/tile12.png");

        frame.add(label);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.pack();
        frame.setVisible(true);
    }

    public static void main(String[] args){
        new GUIMap1();
    }

    private void setGreenAmmoTile(JLabel label, String path){
        try {
            ammoTile = ImageIO.read(new File(path));
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
        //label.setLayout(new FlowLayout(FlowLayout.LEFT,300,62));
        label.add(ammoButton);
    }

    private void setYellowAmmoTile(JLabel label){
        try {
            ammoTile = ImageIO.read(new File("src/resources/gui/firstMap/tile2.png"));
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
        ammoButton.setBounds(iconAmmo.getIconWidth(),iconAmmo.getIconHeight(),500,0);
        label.setLayout(new FlowLayout(FlowLayout.LEADING,0,0));
        label.add(ammoButton);
    }





}
