package view.gui;

import board.Coord;
import gamemanager.ParserConfiguration;

import javax.imageio.ImageIO;
import javax.swing.*;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.Map;
import java.util.concurrent.atomic.AtomicInteger;

public class GUIMap1 extends JPanel {

    private static final String SRC = ParserConfiguration.parsePath("GuiDirectoryPath") + "/";

    private BufferedImage emptyMap;
    private BufferedImage tile;
    private Graphics g;
    private Map<JButton, Coord> coordinate;

    public GUIMap1(){

        try {
            tile = ImageIO.read(new File(SRC + "greenAmmo.png"));
        } catch (IOException e) {
            System.out.println("Errore su file da aprire.");
        }

        JFrame frame = new JFrame("frame");
        JPanel panel = new JPanel();
        if(Toolkit.getDefaultToolkit().getScreenSize().getHeight() == 1080.0) {
            frame.setSize(681,920);
        } else if(Toolkit.getDefaultToolkit().getScreenSize().getHeight() == 768.0){
            frame.setSize(400,541);
        }

        frame.setResizable(true);
        //frame.getContentPane().add(panel);

        JLabel label = new JLabel();
        setFirstTile(label,SRC + "firstMap/tile1.png", Color.GREEN);
        setFollowingTile(label,"src/resources/gui/firstMap/tile2.png", Color.YELLOW);
        setFollowingTile(label,"src/resources/gui/firstMap/tile3.png", Color.YELLOW);
        setFollowingTile(label,"src/resources/gui/firstMap/tile4.png", Color.BLUE);
        setFollowingTile(label,"src/resources/gui/firstMap/tile5.png", Color.YELLOW);
        setFollowingTile(label,"src/resources/gui/firstMap/tile6.png", Color.YELLOW);
        setFollowingTile(label,"src/resources/gui/firstMap/tile7.png", Color.BLUE);
        setFollowingTile(label,"src/resources/gui/firstMap/tile8.png", Color.RED);
        setFollowingTile(label,"src/resources/gui/firstMap/tile9.png", Color.WHITE);
        setFollowingTile(label,"src/resources/gui/firstMap/tile10.png", Color.BLUE);
        setFollowingTile(label,"src/resources/gui/firstMap/tile11.png", Color.RED);
        setFollowingTile(label,"src/resources/gui/firstMap/tile12.png", null);

        System.out.println(Toolkit.getDefaultToolkit().getScreenSize().getHeight());



        //panel.setLayout(new FlowLayout(FlowLayout.LEADING,0,0));
        panel.add(label);
        panel.setBackground(Color.YELLOW);
        //panel.setSize(681,920);
        //frame.getContentPane().add(panel);
        frame.add(label);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        //DO NOT USE pack() !!!
        frame.setVisible(true);
    }

    public static void main(String[] args){
        new GUIMap1();
    }

    private void setFollowingTile(JLabel label, String path, Color tileColor){
        try {
            tile = ImageIO.read(new File(path));
        } catch (IOException e) {
            System.out.println("Errore su file da aprire.");
        }
        Image imgAmmo = tile;
        if(Toolkit.getDefaultToolkit().getScreenSize().getHeight() == 1080.0) {
            imgAmmo = tile.getScaledInstance(tile.getWidth()/2, tile.getHeight()/2,Image.SCALE_SMOOTH);
        } else if (Toolkit.getDefaultToolkit().getScreenSize().getHeight() == 768.0) {
            double tilewid = 0.58737 * (tile.getWidth() / 2.00000);
            double tilehid = 0.588 * (tile.getHeight() / 2.00000);
            imgAmmo = tile.getScaledInstance((int) tilewid, (int) tilehid, Image.SCALE_SMOOTH);
        }
        ImageIcon iconAmmo = new ImageIcon(imgAmmo);
        JButton ammoButton = new JButton();
        ammoButton.setIcon(iconAmmo);
        //come aggiungere azione a bottone:
        ammoButton.addActionListener(e -> {
            System.out.println("prova bottone ammotile stanza " + tileColor.toString());
            int input = JOptionPane.showConfirmDialog(label, "You want to move here?", "asd", JOptionPane.OK_CANCEL_OPTION, JOptionPane.INFORMATION_MESSAGE);
            if(input == JOptionPane.OK_OPTION) System.out.println("Player    moved from    to    ");
        });
        ammoButton.setContentAreaFilled( false );
        ammoButton.setBorder( null );
        ammoButton.setBounds(iconAmmo.getIconWidth(),iconAmmo.getIconHeight(),0,0);
        label.add(ammoButton);
    }

    private void setFirstTile(JLabel label,String path, Color tileColor){
        try {
            tile = ImageIO.read(new File(path));
        } catch (IOException e) {
            System.out.println("Errore su file da aprire.");
        }
        Image imgAmmo = tile;
        if(Toolkit.getDefaultToolkit().getScreenSize().getHeight() == 1080.0) {
            imgAmmo = tile.getScaledInstance(tile.getWidth()/2, tile.getHeight()/2,Image.SCALE_SMOOTH);
        } else if (Toolkit.getDefaultToolkit().getScreenSize().getHeight() == 768.0) {
            double tilewid = 0.58737 * (tile.getWidth() / 2.00000);
            double tilehid = 0.588 * (tile.getHeight() / 2.00000);
            imgAmmo = tile.getScaledInstance((int) tilewid, (int) tilehid, Image.SCALE_SMOOTH);
        }
        ImageIcon iconAmmo = new ImageIcon(imgAmmo);
        JButton ammoButton = new JButton();
        ammoButton.setIcon(iconAmmo);
        //come aggiungere azione a bottone:
        ammoButton.addActionListener(e -> System.out.println("prova bottone ammotile stanza " + tileColor.toString()));
        ammoButton.setContentAreaFilled( false );
        ammoButton.setBorder( null );
        ammoButton.setBounds(iconAmmo.getIconWidth(),iconAmmo.getIconHeight(),500,0);
        label.setLayout(new FlowLayout(FlowLayout.LEADING,0,0));
        label.add(ammoButton);
    }





}
