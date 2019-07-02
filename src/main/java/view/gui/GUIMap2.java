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

public class GUIMap2 extends JPanel implements GUIMap{

    private static final String SRC = ParserConfiguration.parsePath("GuiDirectoryPath") + "/";

    private BufferedImage emptyMap;
    private BufferedImage tile;
    private Map<JButton, Coord> buttonCoord;
    private BufferedImage scoreBoard;
    private BufferedImage pg;
    private GameMapView gmv;
    private Framework framework;

    public GUIMap2(GameMapView gmv, Framework framework){
        this.framework = framework;

        this.gmv = gmv;

        JLabel label = new JLabel();

        this.buttonCoord = new HashMap<>();

        setTile(label,"gui/GUImap2/GUImap2_1.png", Color.YELLOW,new Coord(0,0));
        setTile(label,"gui/GUImap2/GUImap2_2.png", Color.YELLOW,new Coord(1,0));
        setTile(label,"gui/GUImap2/GUImap2_3.png", null,new Coord(2,0));
        setTile(label,"gui/GUImap2/GUImap2_4.png", Color.WHITE,new Coord(0,1));
        setTile(label,"gui/GUImap2/GUImap2_5.png", Color.PINK,new Coord(1,1));
        setTile(label,"gui/GUImap2/GUImap2_6.png", Color.BLUE,new Coord(2,1));
        setTile(label,"gui/GUImap2/GUImap2_7.png", Color.WHITE,new Coord(0,2));
        setTile(label,"gui/GUImap2/GUImap2_8.png", Color.PINK,new Coord(1,2));
        setTile(label,"gui/GUImap2/GUImap2_9.png", Color.BLUE,new Coord(2,2));
        setTile(label,"gui/GUImap2/GUImap2_10.png", Color.WHITE,new Coord(0,3));
        setTile(label,"gui/GUImap2/GUImap2_11.png", Color.RED,new Coord(1,3));
        setTile(label,"gui/GUImap2/GUImap2_12.png", Color.RED,new Coord(2,3));

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

    /**
     * Return a list of possible coordinates to choice as target for any kind of action.
     * @param targets is the list of the possible targets.
     * @return a list of selectable coordinates.
     */
    private List<Coord> getTargetCoordinates(List<TargetView> targets){
        List<TileUID> tiles;
        List<DamageableUID> actors;
        List<Coord> coords = new ArrayList<>();

        for(TargetView target: targets){
            tiles = target.getTileUIDList();
            actors = target.getDamageableUIDList();
            if(!tiles.isEmpty()) {
                for (TileView tw : gmv.allTiles()) {
                    if (tiles.contains(tw.uid()))
                        coords.add(gmv.getCoord(tw));
                }
            }
            if(!actors.isEmpty()) {
                for (ActorView av : gmv.players()) {
                    if (actors.contains(av.uid())) coords.add(gmv.getCoord(av.position()));
                }
            }
        }
        return coords;
    }



    /**
     * Simple method to set a button based on the action to be applied.
     * @param question based on the kind of action to be executed.
     */
    public void clickableButton(List<TargetView> targets, String question, boolean single, boolean optional){
        List<Coord> coords = getTargetCoordinates(targets);
        List<Integer> indexList = new ArrayList<>();
        final int[] j = {0};
        for(Coord coord : coords){
            for(Map.Entry<JButton,Coord> entry : buttonCoord.entrySet()){
                if(entry.getValue().equals(coord)) entry.getKey().addMouseListener(new MouseAdapter() {
                    @Override
                    public void mouseClicked(MouseEvent e) {
                        super.mouseClicked(e);
                        String[] names = new String[100];
                        int i = 0;
                        for(TargetView target : targets){
                            if(!target.getDamageableUIDList().isEmpty()){
                                for(ActorView player : gmv.players()){
                                    if(target.getDamageableUIDList().contains(player.uid())&&gmv.getCoord(player.position()).equals(coord)){
                                        System.out.println(player.name());
                                        names[i] = player.name();
                                        i++;
                                    }
                                }
                                if(i!=0) {
                                    names[i] = "Stop";
                                    i++;
                                    names[i] = "Rollback";
                                    i++;
                                    names[i] = "Reset";

                                    String[] namesBeta = new String[i];

                                    System.arraycopy(names, 0, namesBeta, 0, i);


                                    Integer choice;
                                    boolean flag = true;
                                    while (flag) {

                                        choice = JOptionPane.showOptionDialog(null, "Choose your next Target!", "TARGET", JOptionPane.DEFAULT_OPTION,
                                                JOptionPane.INFORMATION_MESSAGE, null, namesBeta, namesBeta[0]);

                                        if (choice.equals(i - 2)) {
                                            if (indexList.isEmpty() && optional) {
                                                flag = false;
                                            } else if (indexList.isEmpty()) {
                                                JOptionPane.showMessageDialog(null, "You must choose at least one Target!", "ERROR", JOptionPane.ERROR_MESSAGE);
                                            }
                                        } else if (choice.equals(i - 1) && !indexList.isEmpty()) {
                                            indexList.remove(indexList.size() - 1);
                                        } else if (choice.equals(i)) {
                                            indexList.clear();
                                        } else {
                                            if (!indexList.contains(choice)) {
                                                indexList.add(choice);
                                            } else {
                                                JOptionPane.showMessageDialog(null, "You must choose a different Target", "ERROR", JOptionPane.ERROR_MESSAGE);
                                            }
                                        }
                                    }
                                }
                            } else {
                                int choice;
                                choice = JOptionPane.showConfirmDialog(null,"Are you sure?","CONFIRM", JOptionPane.OK_CANCEL_OPTION);
                                if(choice==1){
                                    indexList.add(i);
                                    j[0]++;
                                }
                                break;
                            }
                        }
                        framework.pick(indexList);
                    }
                });
            }
        }
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
