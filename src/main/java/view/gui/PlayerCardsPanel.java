package view.gui;

import viewclasses.GameMapView;

import javax.swing.*;
import java.awt.*;
import java.io.IOException;

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

        setLayout(new BorderLayout());
        add(playerBoard,BorderLayout.NORTH);
        add(weaponCards);
    }
}
