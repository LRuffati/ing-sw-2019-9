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

import static java.awt.Image.SCALE_SMOOTH;

public class GUIMap1 extends JPanel {

    private static final String SRC = ParserConfiguration.parsePath("GuiDirectoryPath") + "/";

    private BufferedImage emptyMap;
    private BufferedImage tile;
    private Map<JButton, Coord> coordinate;
    private BufferedImage scoreBoard;
    private JFrame frame;
    private BufferedImage pg;

    public GUIMap1(){

        this.frame = new JFrame("frame");
        if(Toolkit.getDefaultToolkit().getScreenSize().getHeight() == 1080.0) {
            frame.setSize(683,920);
        } else if(Toolkit.getDefaultToolkit().getScreenSize().getHeight() == 768.0){
            frame.setSize(392,542);
        }

        frame.setResizable(false);
        //frame.getContentPane().add(panel);

        JLabel label = new JLabel();


        setFirstTile(label,"src/resources/gui/GUImap1/GUImap1_1.png", Color.GREEN);
        setFollowingTile(label,"src/resources/gui/GUImap1/GUImap1_2.png", Color.YELLOW);
        setFollowingTile(label,"src/resources/gui/GUImap1/GUImap1_3.png", Color.YELLOW);
        setFollowingTile(label,"src/resources/gui/GUImap1/GUImap1_4.png", Color.BLUE);
        setFollowingTile(label,"src/resources/gui/GUImap1/GUImap1_5.png", Color.YELLOW);
        setFollowingTile(label,"src/resources/gui/GUImap1/GUImap1_6.png", Color.YELLOW);
        setFollowingTile(label,"src/resources/gui/GUImap1/GUImap1_7.png", Color.BLUE);
        setFollowingTile(label,"src/resources/gui/GUImap1/GUImap1_8.png", Color.RED);
        setFollowingTile(label,"src/resources/gui/GUImap1/GUImap1_9.png", Color.WHITE);
        setFollowingTile(label,"src/resources/gui/GUImap1/GUImap1_10.png", Color.BLUE);
        setFollowingTile(label,"src/resources/gui/GUImap1/GUImap1_11.png", Color.RED);
        setFollowingTile(label,"src/resources/gui/GUImap1/GUImap1_12.png", null);

        System.out.println(Toolkit.getDefaultToolkit().getScreenSize().getHeight());


        //panel.setSize(682,920);
        setLayout(new GridLayout());
        add(label);

        label.setSize(new Dimension(682,920));
        setBackground(Color.YELLOW);
        //panel.setSize(681,920);
        //frame.getContentPane().add(panel);
        frame.add(this);
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
            imgAmmo = tile.getScaledInstance(tile.getWidth()/2, tile.getHeight()/2, SCALE_SMOOTH);
        } else if (Toolkit.getDefaultToolkit().getScreenSize().getHeight() == 768.0) {
            double tilewid = 0.58737 * (tile.getWidth() / 2.00000);
            double tilehid = 0.588 * (tile.getHeight() / 2.00000);
            imgAmmo = tile.getScaledInstance((int) tilewid, (int) tilehid, SCALE_SMOOTH);
        }
        ImageIcon iconAmmo = new ImageIcon(imgAmmo);
        JButton ammoButton = new JButton();
        ammoButton.setIcon(iconAmmo);
        //come aggiungere azione a bottone:
        ammoButton.addActionListener(e -> {
            System.out.println("prova bottone ammotile stanza " + tileColor.toString());
            /*try {
                pg = ImageIO.read(new File("src/resources/gui/pgTest.png"));
            } catch (IOException ignored) {
            }

            Image pgscal = pg.getScaledInstance(pg.getWidth()/5,pg.getHeight()/5,SCALE_SMOOTH);
            ImageIcon pgIcon = new ImageIcon(pgscal);
            ammoButton.setIcon(pgIcon);
            */
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
            imgAmmo = tile.getScaledInstance(tile.getWidth()/2, tile.getHeight()/2, SCALE_SMOOTH);
        } else if (Toolkit.getDefaultToolkit().getScreenSize().getHeight() == 768.0) {
            double tilewid = 0.58737 * (tile.getWidth() / 2.00000);
            double tilehid = 0.588 * (tile.getHeight() / 2.00000);
            imgAmmo = tile.getScaledInstance((int) tilewid, (int) tilehid, SCALE_SMOOTH);
        }
        ImageIcon iconAmmo = new ImageIcon(imgAmmo);
        JButton ammoButton = new JButton();
        ammoButton.setIcon(iconAmmo);
        //come aggiungere azione a bottone:
        ammoButton.addActionListener(e -> System.out.println("prova bottone ammotile stanza " + tileColor.toString()));
        ammoButton.setContentAreaFilled( false );
        ammoButton.setBorder( null );
        ammoButton.setBounds(iconAmmo.getIconWidth(),iconAmmo.getIconHeight(),500,0);
        label.setLayout(new GridLayout(4,3,0,0));
        label.add(ammoButton);
    }

}
