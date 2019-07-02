package view.gui;

import viewclasses.ActorView;
import viewclasses.GameMapView;

import javax.swing.*;
import java.awt.*;
import java.security.InvalidParameterException;

class TileButton extends JButton {

    private ActorView[] playersToPaint = new ActorView[5];

    TileButton(){
        super();
    }

    public void paintPlayers(GameMapView gmv){
        int i = 0;
        for(ActorView player : gmv.players()){
            playersToPaint[i] = player;
            i++;
            repaint();
        }

    }

    @Override
    public void paintComponent(Graphics g){
        super.paintComponent(g);

        for(int i = 0; playersToPaint[i] != null; i++){
            try {
                g.setColor(playersToPaint[i].color());
                if(Toolkit.getDefaultToolkit().getScreenSize().getHeight() == 1080.0) {
                    g.fillOval(50+30 * i, 100, 30, 30);
                } else if (Toolkit.getDefaultToolkit().getScreenSize().getHeight() == 768.0) {
                    g.fillOval(50+12 * i, 100, 12, 12);
                }

                repaint();
            } catch (InvalidParameterException ignored){ }
        }
    }
}