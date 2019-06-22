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
import java.util.List;
import java.util.Map;
import java.util.Objects;

public class WeaponCards extends JPanel {

    private BufferedImage[] showedWeaponCards = new BufferedImage[3];
    private GameMapView gmv;
    private Map<ActorView, BufferedImage[]> cardMap;

    public WeaponCards(GameMapView gmv) throws IOException {
        this.gmv = gmv;
        setPlayersWeaponCards();
        setYourShowedWeaponCards();

    }

    private void setYourShowedWeaponCards(){
        int i = 0;
        for(WeaponView weapon : gmv.you().getUnloadedWeapon()){
            showedWeaponCards[i] = linkWeaponToCard(weapon);
            i++;
        }
        for(WeaponView weapon : gmv.you().getLoadedWeapon()){
            showedWeaponCards[i] = linkWeaponToCard(weapon);
            i++;
        }
    }

    /**
     * This method sets the cards on the other players hand.
     * @throws IOException
     */
    private void setPlayersWeaponCards() throws IOException {
        for(ActorView player : gmv.players()) {
            BufferedImage[] playerCards = new BufferedImage[3];
            int i = 0;
            if (player.equals(gmv.you())) {
                for (WeaponView weapon : gmv.you().getUnloadedWeapon()) {
                    playerCards[i] = linkWeaponToCard(weapon);
                    i++;
                }
                for (WeaponView weapon : gmv.you().getLoadedWeapon()) {
                    playerCards[i] = linkWeaponToCard(weapon);
                    i++;
                }

            } else {

                for (WeaponView weapon : gmv.you().getUnloadedWeapon()) {
                    playerCards[i] = linkWeaponToCard(weapon);
                    i++;
                }
                for(;i<3;i++){
                    playerCards[i] = ImageIO.read(Objects.requireNonNull(ClassLoader.getSystemResourceAsStream("gui/Cards/AD_weapons_IT_02.png")));
                }
            }
            cardMap.put(player,playerCards);
        }
    }

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);

        for(BufferedImage image : showedWeaponCards){
            g.drawImage(image, 0, 0, this);
        }

        repaint();

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
        try {
            builder = new GameBuilder(
                    null, null, null, null, 5);
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }

        GameMap map = builder.getMap();
        List<Actor> actorList = builder.getActorList();
        GameMapView gmv = map.generateView(actorList.get(0).pawn().damageableUID);

        WeaponCards mainPanel = null;
        try {
            mainPanel = new WeaponCards(gmv);
        } catch (IOException e) {
            e.printStackTrace();
        }

        JFrame frame = new JFrame("Drawing Panel");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.getContentPane().add(mainPanel);
        frame.pack();

        frame.setSize(new Dimension(1000,1000));
        frame.setLocationByPlatform(true);
        frame.setVisible(true);
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(WeaponCards::createAndShowGui);
    }
}