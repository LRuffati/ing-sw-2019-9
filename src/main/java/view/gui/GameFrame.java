package view.gui;

import gamemanager.GameBuilder;
import gamemanager.Scoreboard;
import viewclasses.GameMapView;

import javax.swing.*;
import java.awt.*;
import java.io.FileNotFoundException;
import java.io.IOException;

public class GameFrame extends JFrame {

    //TODO sostituire GUIMap1 con interfaccia GUIMap
    private GUIMap1 map;
    private GUIScoreBoard scoreBoard;
    private OutputBox outputBox;

    public GameFrame(GameMapView gmv){
        this.map = new GUIMap1(gmv);
        try {
            this.scoreBoard = new GUIScoreBoard();
        } catch (IOException e) {
            System.out.println("Problemi nella Scoreboard");
        }

        this.outputBox = new OutputBox();

        getContentPane().setLayout(new GridLayout(1,0));
        getContentPane().add(map);
        add(scoreBoard);
        add(outputBox);
        setSize(1920,1080);
        setVisible(true);
        setResizable(true);
        setExtendedState(JFrame.MAXIMIZED_BOTH);
        setDefaultCloseOperation(EXIT_ON_CLOSE);
    }

    public static void main(String[] args) throws FileNotFoundException {
        GameBuilder game = new GameBuilder(5);

        GameMapView mv = game.getMap().generateView(game.getActorList().get(0).pawnID());

        new GameFrame(mv);
    }

    public OutputBox getOutputBox() {
        return outputBox;
    }

    public GUIMap1 getMap() {
        return map;
    }
}
