package view.gui;

import board.Coord;
import gamemanager.GameBuilder;
import gamemanager.ParserConfiguration;
import uid.DamageableUID;
import uid.TileUID;
import viewclasses.ActorView;
import viewclasses.GameMapView;
import viewclasses.TargetView;
import viewclasses.TileView;

import javax.imageio.ImageIO;
import javax.swing.*;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.image.BufferedImage;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.*;
import java.util.List;

import static java.awt.Image.SCALE_SMOOTH;

public class GUIMap3 extends GUIMap {

    private static final String SRC = ParserConfiguration.parsePath("GuiDirectoryPath") + "/";

    private BufferedImage emptyMap;
    private BufferedImage tile;
    private Map<JButton, Coord> buttonCoord;
    private BufferedImage scoreBoard;
    private BufferedImage pg;
    private GameMapView gmv;
    private Framework framework;

    public GUIMap3(GameMapView gmv, Framework framework){
        this.framework = framework;

        this.gmv = gmv;

        JLabel label = new JLabel();

        this.buttonCoord = new HashMap<>();

        setTile(label,"gui/GUImap3/GUImap3_1.png", null,new Coord(0,0));
        setTile(label,"gui/GUImap3/GUImap3_2.png", Color.YELLOW,new Coord(1,0));
        setTile(label,"gui/GUImap3/GUImap3_3.png", Color.YELLOW,new Coord(2,0));
        setTile(label,"gui/GUImap3/GUImap3_4.png", Color.BLUE,new Coord(0,1));
        setTile(label,"gui/GUImap3/GUImap3_5.png", Color.PINK,new Coord(1,1));
        setTile(label,"gui/GUImap3/GUImap3_6.png", Color.WHITE,new Coord(2,1));
        setTile(label,"gui/GUImap3/GUImap3_7.png", Color.BLUE,new Coord(0,2));
        setTile(label,"gui/GUImap3/GUImap3_8.png", Color.PINK,new Coord(1,2));
        setTile(label,"gui/GUImap3/GUImap3_9.png", Color.WHITE,new Coord(2,2));
        setTile(label,"gui/GUImap3/GUImap3_10.png", Color.BLUE,new Coord(0,3));
        setTile(label,"gui/GUImap3/GUImap3_11.png", Color.RED,new Coord(1,3));
        setTile(label,"gui/GUImap3/GUImap3_12.png", null,new Coord(2,3));

        System.out.println(Toolkit.getDefaultToolkit().getScreenSize().getHeight());

        setLayout(new BorderLayout());
        add(label);

        label.setSize(new Dimension(682,920));
        setBackground(Color.YELLOW);

    }

    public static void main(String[] args) throws FileNotFoundException {
        GameBuilder game = new GameBuilder(5);

        GameMapView mv = game.getMap().generateView(game.getActorList().get(0).pawnID());

        List<TargetView> targetViewList = new ArrayList<>();
        List<TileUID> tileUIDS = new ArrayList<>();
        tileUIDS.add(mv.getTiles().get(new Coord(1,0)).uid());
        List<DamageableUID> damageableUIDS = new ArrayList<>();
        targetViewList.add(new TargetView(null,damageableUIDS,tileUIDS,false));
        targetViewList.add(new TargetView(null,damageableUIDS,tileUIDS,false));

        JFrame frame = new JFrame("frame");
        if(Toolkit.getDefaultToolkit().getScreenSize().getHeight() == 1080.0) {
            frame.setSize(777,1046);
        } else if(Toolkit.getDefaultToolkit().getScreenSize().getHeight() == 768.0){
            frame.setSize(542,744);
        }

        frame.setResizable(true);

        GUIMap4 guiMap2 = new GUIMap4(mv, null);

        frame.add(guiMap2);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        //DO NOT USE pack() !!!
        frame.setVisible(true);

        guiMap2.clickableButton(targetViewList,"come stai?",true,false);
    }


    private void setTile(JLabel label, String path, Color tileColor, Coord coord){
        try {
            tile = ImageIO.read(ClassLoader.getSystemResourceAsStream(path));
        } catch (IOException e) {
            System.out.println("Errore su file da aprire.");
        }
        Image imgAmmo = tile;
        if(Toolkit.getDefaultToolkit().getScreenSize().getHeight() == 1080.0) {
            double tilewid = (tile.getWidth() / 1.75238);
            double tilehid = (tile.getHeight() / 1.75238);
            imgAmmo = tile.getScaledInstance((int) tilewid, (int) tilehid, SCALE_SMOOTH);
        } else if (Toolkit.getDefaultToolkit().getScreenSize().getHeight() == 768.0) {
            double tilewid = 0.711458 * (tile.getWidth() / 1.75238);
            double tilehid = 0.711111 * (tile.getHeight() / 1.75238);
            imgAmmo = tile.getScaledInstance((int) tilewid, (int) tilehid, SCALE_SMOOTH);
        }
        ImageIcon iconAmmo = new ImageIcon(imgAmmo);
        JButton ammoButton = new JButton();
        ammoButton.setIcon(iconAmmo);
        //come aggiungere azione a bottone:
        ammoButton.addActionListener(e -> {
            System.out.println("prova bottone ammotile stanza " + tileColor.toString());

        });
        ammoButton.setContentAreaFilled( false );
        ammoButton.setBorder( null );
        ammoButton.setBounds(iconAmmo.getIconWidth(),iconAmmo.getIconHeight(),0,0);
        label.setLayout(new GridLayout(4,3,0,0));
        label.add(ammoButton);
        buttonCoord.put(ammoButton,coord);
    }







    private static boolean isNotNumeric(String str) {
        try {
            Integer.parseInt(str);
            return false;
        } catch(NumberFormatException e){
            return true;
        }
    }
}
