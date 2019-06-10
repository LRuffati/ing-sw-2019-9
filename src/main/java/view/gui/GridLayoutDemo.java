package view.gui;

import javax.imageio.ImageIO;
import javax.swing.*;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

import static java.awt.Image.SCALE_SMOOTH;

public class GridLayoutDemo {

    BufferedImage tile;

    public GridLayoutDemo(){
        JFrame frame = new JFrame("frame");
        frame.setSize(681,920);
        frame.setResizable(true);

        JLabel label = new JLabel();
        label.setLayout(new GridLayout(0,3));


        setFollowingTile(label,"src/resources/gui/GUImap1/GUImap1_1.png", Color.YELLOW);
        setFollowingTile(label,"src/resources/gui/GUImap1/GUImap1_2.png", Color.YELLOW);
        setFollowingTile(label,"src/resources/gui/GUImap1/GUImap1_3.png", Color.YELLOW);
        setFollowingTile(label,"src/resources/gui/GUImap1/GUImap1_4.png", Color.YELLOW);
        setFollowingTile(label,"src/resources/gui/GUImap1/GUImap1_5.png", Color.YELLOW);
        setFollowingTile(label,"src/resources/gui/GUImap1/GUImap1_6.png", Color.YELLOW);


        frame.add(label);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        frame.setVisible(true);
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
            int input = JOptionPane.showConfirmDialog(label, "You want to move here?", "asd", JOptionPane.OK_CANCEL_OPTION, JOptionPane.INFORMATION_MESSAGE);
            if(input == JOptionPane.OK_OPTION) System.out.println("Player    moved from    to    ");
        });
        ammoButton.setContentAreaFilled( false );
        ammoButton.setBorder( null );
        ammoButton.setBounds(iconAmmo.getIconWidth(),iconAmmo.getIconHeight(),0,0);
        label.add(ammoButton);
    }

    public static void main(String[] args){
        new GridLayoutDemo();
    }
}
