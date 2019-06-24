package view.gui;

import board.Coord;
import gamemanager.GameBuilder;
import uid.DamageableUID;
import uid.TileUID;
import viewclasses.GameMapView;
import viewclasses.TargetView;

import javax.swing.*;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.image.BufferedImage;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class PlayerCardsPanel extends JPanel {

    private WeaponCards weaponCards;
    private PlayerBoard playerBoard;



    public PlayerCardsPanel(GameMapView gmv){
        weaponCards = new WeaponCards(gmv);
        try {
            playerBoard = new PlayerBoard(gmv);
        } catch (IOException e) {
            System.out.println("Problema con PlayerBoard");
        }

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
                for(int i = 0; i< playerBoard.getNormalBoards().length; i++){
                    if(playerBoard.getNormalBoards()[i].equals(playerBoard.getYourNormalBoard())){
                        if(i!=playerBoard.getNormalBoards().length-1){
                            playerBoard.drawBoard(playerBoard.getNormalBoards()[i+1]);
                        } else playerBoard.drawBoard(playerBoard.getNormalBoards()[0]);
                    }
                }
            }
        });
    }

    public static void main(String[] args) throws FileNotFoundException {
        GameBuilder game = new GameBuilder(5);

        GameMapView mv = game.getMap().generateView(game.getActorList().get(0).pawnID());

        java.util.List<TargetView> targetViewList = new ArrayList<>();
        java.util.List<TileUID> tileUIDS = new ArrayList<>();
        tileUIDS.add(mv.getTiles().get(new Coord(1,0)).uid());
        List<DamageableUID> damageableUIDS = new ArrayList<>();
        targetViewList.add(new TargetView(null,damageableUIDS,tileUIDS));
        targetViewList.add(new TargetView(null,damageableUIDS,tileUIDS));

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
