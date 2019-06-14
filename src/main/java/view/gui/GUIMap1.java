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
import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.*;
import java.util.List;

import static java.awt.Image.SCALE_SMOOTH;

public class GUIMap1 extends JPanel implements GUIMap{

    private static final String SRC = ParserConfiguration.parsePath("GuiDirectoryPath") + "/";

    private BufferedImage emptyMap;
    private BufferedImage tile;
    private Map<JButton, Coord> buttonCoord;
    private BufferedImage scoreBoard;
    private static JFrame frame;
    private BufferedImage pg;
    private GameMapView gmv;

    public GUIMap1(GameMapView gmv){

        this.gmv = gmv;

        JLabel label = new JLabel();

        this.buttonCoord = new HashMap<>();

        setTile(label,"src/resources/gui/GUImap1/GUImap1_1.png", Color.GREEN,new Coord(0,0));
        setTile(label,"src/resources/gui/GUImap1/GUImap1_2.png", Color.YELLOW,new Coord(1,0));
        setTile(label,"src/resources/gui/GUImap1/GUImap1_3.png", Color.YELLOW,new Coord(2,0));
        setTile(label,"src/resources/gui/GUImap1/GUImap1_4.png", Color.BLUE,new Coord(0,1));
        setTile(label,"src/resources/gui/GUImap1/GUImap1_5.png", Color.YELLOW,new Coord(1,1));
        setTile(label,"src/resources/gui/GUImap1/GUImap1_6.png", Color.YELLOW,new Coord(2,1));
        setTile(label,"src/resources/gui/GUImap1/GUImap1_7.png", Color.BLUE,new Coord(0,2));
        setTile(label,"src/resources/gui/GUImap1/GUImap1_8.png", Color.RED,new Coord(1,2));
        setTile(label,"src/resources/gui/GUImap1/GUImap1_9.png", Color.WHITE,new Coord(2,2));
        setTile(label,"src/resources/gui/GUImap1/GUImap1_10.png", Color.BLUE,new Coord(0,3));
        setTile(label,"src/resources/gui/GUImap1/GUImap1_11.png", Color.RED,new Coord(1,3));
        setTile(label,"src/resources/gui/GUImap1/GUImap1_12.png", null,new Coord(2,3));

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
        Collection<TileUID> tileUIDS = new ArrayList<>();
        tileUIDS.add(mv.getTiles().get(new Coord(1,0)).uid());
        Collection<DamageableUID> damageableUIDS = new ArrayList<>();
        targetViewList.add(new TargetView(null,damageableUIDS,tileUIDS));

        frame = new JFrame("frame");
        if(Toolkit.getDefaultToolkit().getScreenSize().getHeight() == 1080.0) {
            frame.setSize(777,1046);
        } else if(Toolkit.getDefaultToolkit().getScreenSize().getHeight() == 768.0){
            frame.setSize(392,542);
        }

        frame.setResizable(false);

        GUIMap1 guiMap1 = new GUIMap1(mv);

        frame.add(guiMap1);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        //DO NOT USE pack() !!!
        frame.setVisible(true);

        guiMap1.clickableButton(targetViewList,"come stai?");
    }


    private void setTile(JLabel label, String path, Color tileColor, Coord coord){
        try {
            tile = ImageIO.read(new File(path));
        } catch (IOException e) {
            System.out.println("Errore su file da aprire.");
        }
        Image imgAmmo = tile;
        if(Toolkit.getDefaultToolkit().getScreenSize().getHeight() == 1080.0) {
            double tilewid = tile.getWidth() / 1.75238;
            double tilehid = tile.getHeight() / 1.75238;
            imgAmmo = tile.getScaledInstance((int) tilewid, (int) tilehid, SCALE_SMOOTH);
        } else if (Toolkit.getDefaultToolkit().getScreenSize().getHeight() == 768.0) {
            double tilewid = 0.58737 * (tile.getWidth() / 1.75238);
            double tilehid = 0.588 * (tile.getHeight() / 1.75238);
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
            //int input = JOptionPane.showConfirmDialog(label, "You want to move here?", "asd", JOptionPane.OK_CANCEL_OPTION, JOptionPane.INFORMATION_MESSAGE);

            //if(input == JOptionPane.OK_OPTION) System.out.println("Player    moved from    to    ");
        });
        ammoButton.setContentAreaFilled( false );
        ammoButton.setBorder( null );
        ammoButton.setBounds(iconAmmo.getIconWidth(),iconAmmo.getIconHeight(),0,0);
        label.setLayout(new GridLayout(4,3,0,0));
        label.add(ammoButton);
        buttonCoord.put(ammoButton,coord);
    }

    /**
     * Return a list of possible coordinates to choice as target for any kind of action.
     * @param targets is the list of the possible targets.
     * @return a list of selectable coordinates.
     */
    private List<Coord> getTargetCoordinates(List<TargetView> targets){
        Collection<TileUID> tiles;
        Collection<DamageableUID> actors;
        List<Coord> coords = new ArrayList<>();

        for(TargetView target: targets){
            tiles = target.getTileUIDList();
            actors = target.getDamageableUIDList();
            for(TileView tw : gmv.allTiles()){
                if(tiles.contains(tw.uid())) coords.add(gmv.getCoord(tw));
            }
            for(ActorView av : gmv.players()){
                if(actors.contains(av.uid())) coords.add(gmv.getCoord(av.position()));
            }
        }
        return coords;
    }

    /**
     * Simple method to set a button based on the action to be applied.
     * @param question based on the kind of action to be executed.
     */
    //TODO to be better implemented
    public void clickableButton(List<TargetView> targets, String question){
        List<Coord> coords = getTargetCoordinates(targets);
        GUIMap1 asd = this;
        for(Coord coord : coords){
            for(Map.Entry<JButton,Coord> entry : buttonCoord.entrySet()){
                if(entry.getValue().equals(coord)) entry.getKey().addMouseListener(new MouseAdapter() {
                    @Override
                    public void mouseClicked(MouseEvent e) {
                        super.mouseClicked(e);
                        JOptionPane.showConfirmDialog(asd, question, "asd", JOptionPane.OK_CANCEL_OPTION, JOptionPane.INFORMATION_MESSAGE);
                    }
                });
            }
        }
    }
}
