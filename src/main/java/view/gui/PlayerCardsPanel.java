package view.gui;

import board.Coord;
import gamemanager.GameBuilder;
import uid.DamageableUID;
import uid.TileUID;
import viewclasses.ActorView;
import viewclasses.GameMapView;
import viewclasses.TargetView;
import viewclasses.WeaponView;

import javax.swing.*;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class PlayerCardsPanel extends JPanel {

    private WeaponCards weaponCards;
    private PlayerBoard playerBoard;

    private List<ActorView> actorViews;
    private ActorView currActor;

    public PlayerCardsPanel(GameMapView gmv){
        actorViews = gmv.players();
        currActor = gmv.you();

        weaponCards = new WeaponCards(gmv);
        try {
            playerBoard = new PlayerBoard(gmv);
        } catch (IOException e) {
            System.out.println("Problema con PlayerBoard");
        }
        playerBoard.drawBoard(playerBoard.getNormalBoards()[actorViews.indexOf(currActor)]);
        weaponCards.showCard(currActor);

        changeWeaponsWithPlayer();
        setLayout(new GridLayout(1,0));
        add(playerBoard);
        add(weaponCards);
    }

    public void changeWeaponsWithPlayer(){
        playerBoard.getChangeBoard().addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                super.mouseClicked(e);

                int pos = actorViews.indexOf(currActor);
                currActor = actorViews.get((pos+1)%actorViews.size());

                weaponCards.showCard(currActor);
                playerBoard.drawBoard(playerBoard.getNormalBoards()[(pos+1)%actorViews.size()]);
                playerBoard.setYourBoards((pos+1)%actorViews.size());
            }
        });
    }

    public static void main(String[] args) throws FileNotFoundException {
        GameBuilder game = new GameBuilder(3);

        GameMapView mv = game.getMap().generateView(game.getActorList().get(0).pawnID());

        WeaponView w11 = new WeaponView();
        WeaponView w12 = new WeaponView();
        WeaponView w21 = new WeaponView();
        WeaponView w22 = new WeaponView();
        WeaponView w23 = new WeaponView();

        w11.setName("martello ionico");
        w12.setName("lanciagranate");
        mv.players().get(0).setColorString("yellow");
        mv.players().get(0).setUnloadedWeapon(List.of(w11));
        mv.players().get(0).setLoadedWeapon(List.of(w12));

        w21.setName("lanciarazzi");
        w22.setName("cannone vortex");
        w23.setName("fucile al plasma");
        mv.players().get(1).setColorString("green");
        mv.players().get(1).setUnloadedWeapon(List.of(w21, w22, w23));

        mv.players().get(2).setColorString("gray");


        java.util.List<TargetView> targetViewList = new ArrayList<>();
        java.util.List<TileUID> tileUIDS = new ArrayList<>();
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

        PlayerCardsPanel playerCardsPanel = new PlayerCardsPanel(mv);

        frame.add(playerCardsPanel);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        //DO NOT USE pack() !!!
        frame.setVisible(true);

    }
}
