package view.gui;

import board.GameMap;
import gamemanager.GameBuilder;
import player.Actor;
import viewclasses.ActorView;
import viewclasses.GameMapView;
import viewclasses.WeaponView;

import javax.imageio.ImageIO;
import javax.swing.*;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;

public class WeaponCards extends JPanel {

    private BufferedImage[] showedWeaponCards = new BufferedImage[3];
    private GameMapView gmv;
    private Map<ActorView, BufferedImage[]> cardMap;
    private JButton[] weaponButtons = new JButton[3];

    public WeaponCards(GameMapView gmv){
        cardMap = new HashMap<>();
        this.gmv = gmv;
        setPlayersWeaponCards();
        setYourShowedWeaponCards();

        setLayout(new GridLayout(1,3));

        for (int i = 0; i < 3; i++) {
            ImageIcon icon = new ImageIcon(showedWeaponCards[i]);
            JButton btn = new JButton(icon);

            btn.setContentAreaFilled( false );
            btn.setBorder( null );
            btn.setBounds(icon.getIconWidth(),icon.getIconHeight(),0,0);
            weaponButtons[i] = btn;

            add(btn);

        }
    }

    public void showCard(ActorView actorView) {
        showedWeaponCards = cardMap.get(actorView);
        weaponButtons[0].setIcon(new ImageIcon(cardMap.get(actorView)[0]));
        weaponButtons[1].setIcon(new ImageIcon(cardMap.get(actorView)[1]));
        weaponButtons[2].setIcon(new ImageIcon(cardMap.get(actorView)[2]));
    }

    private void setYourShowedWeaponCards(){
        int i = 0;
        for(WeaponView weapon : gmv.you().unloadedWeapon()){
            showedWeaponCards[i] = linkWeaponToCard(weapon);
            i++;
        }
        for(WeaponView weapon : gmv.you().loadedWeapon()){
            showedWeaponCards[i] = linkWeaponToCard(weapon);
            i++;
        }
        for(;i<3;i++){
            try {
                showedWeaponCards[i] = ImageIO.read(Objects.requireNonNull(ClassLoader.getSystemResourceAsStream("gui/Cards/AD_weapons_IT_02.png")));
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    /**
     * This method sets the cards on the other players hand.
     */
    private void setPlayersWeaponCards(){
        for(ActorView player : gmv.players()) {
            BufferedImage[] playerCards = new BufferedImage[3];
            int i = 0;
            for (WeaponView weapon : player.unloadedWeapon()) {
                playerCards[i] = linkWeaponToCard(weapon);
                i++;
            }
            for(WeaponView weapon : player.loadedWeapon()){
                playerCards[i] = linkWeaponToCard(weapon);
                i++;
            }
            while(i<3) {
                playerCards[i] = linkWeaponToCard(new WeaponView());
                i++;
            }
            cardMap.put(player,playerCards);
        }
    }

    public BufferedImage[] getShowedWeaponCards() {
        return showedWeaponCards;
    }

    private BufferedImage linkWeaponToCard(WeaponView weapon){
        String path;
        switch(weapon.name().toLowerCase()){
            case("martello ionico"):
                path = "gui/Cards/AD_weapons_IT_022.png";
                break;

            case("spada ionica"):
                path = "gui/Cards/AD_weapons_IT_023.png";
                break;

            case("cyberguanto"):
                path = "gui/Cards/AD_weapons_IT_024.png";
                break;

            case("fucile a pompa"):
                path = "gui/Cards/AD_weapons_IT_025.png";
                break;

            case("fucile laser"):
                path = "gui/Cards/AD_weapons_IT_026.png";
                break;

            case("zx-2"):
                path = "gui/Cards/AD_weapons_IT_027.png";
                break;

            case("onda d'urto"):
                path = "gui/Cards/AD_weapons_IT_028.png";
                break;

            case("cannone vortex"):
                path = "gui/Cards/AD_weapons_IT_029.png";
                break;

            case("razzo termico"):
                path = "gui/Cards/AD_weapons_IT_0210.png";
                break;

            case("lanciarazzi"):
                path = "gui/Cards/AD_weapons_IT_0211.png";
                break;

            case("lanciagranate"):
                path = "gui/Cards/AD_weapons_IT_0212.png";
                break;

            case("lanciafiamme"):
                path = "gui/Cards/AD_weapons_IT_0213.png";
                break;

            case("vulcanizzatore"):
                path = "gui/Cards/AD_weapons_IT_0214.png";
                break;

            case("raggio solare"):
                path = "gui/Cards/AD_weapons_IT_0215.png";
                break;

            case("torpedine"):
                path = "gui/Cards/AD_weapons_IT_0216.png";
                break;

            case("raggio traente"):
                path = "gui/Cards/AD_weapons_IT_0217.png";
                break;

            case("fucile di precisione"):
                path = "gui/Cards/AD_weapons_IT_0218.png";
                break;

            case("fucile al plasma"):
                path = "gui/Cards/AD_weapons_IT_0219.png";
                break;

            case("mitragliatrice"):
                path = "gui/Cards/AD_weapons_IT_0220.png";
                break;

            case("distruttore"):
                path = "gui/Cards/AD_weapons_IT_0221.png";
                break;

            case("falce protonica"):
                path = "gui/Cards/AD_weapons_IT_0222.png";
                break;

            case("sega elettrica"):
                path = "gui/Cards/AD_weapons_IT_0226.png";
                break;

            case("laser pesante"):
                path = "gui/Cards/AD_weapons_IT_0227.png";
                break;


            default:
                path = "gui/Cards/AD_weapons_IT_02.png";
                break;
        }
        try {
            return ImageIO.read(Objects.requireNonNull(ClassLoader.getSystemResourceAsStream(path)));
        } catch (IOException e) {
            System.out.println("Problema con lettura dell'immagine della carta " + weapon.name());
        }
        return null;
    }

    private static void createAndShowGui() {

        GameBuilder builder = null;
        builder = new GameBuilder(
                    null, null, null, null, 5);

        GameMap map = builder.getMap();
        List<Actor> actorList = builder.getActorList();
        GameMapView gmv = map.generateView(actorList.get(0).pawn().damageableUID);

        WeaponCards mainPanel = null;
        mainPanel = new WeaponCards(gmv);

        JFrame frame = new JFrame("Drawing Panel");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.getContentPane().add(mainPanel);
        frame.pack();

        frame.setSize(new Dimension(1000,1000));
        frame.setLocationByPlatform(true);
        frame.setVisible(true);
    }

    public Map<ActorView, BufferedImage[]> getCardMap() {
        return cardMap;
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(WeaponCards::createAndShowGui);
    }
}
