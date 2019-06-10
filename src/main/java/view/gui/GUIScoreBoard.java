package view.gui;

import javax.imageio.ImageIO;
import javax.swing.*;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

public class GUIScoreBoard{

    private BufferedImage scoreBoardBuffered;
    private JFrame scoreBoardFrame;
    private JLabel scoreBoardLabel;
    private Image scoreBoard;

    public GUIScoreBoard(){
        scoreBoardFrame = new JFrame("Scoreboard");
        try {
            scoreBoardBuffered = ImageIO.read(new File("src/resources/gui/ScoreBoard.png"));
        } catch (IOException e) {
            e.printStackTrace();
        }

        scoreBoard = scoreBoardBuffered.getScaledInstance(scoreBoardBuffered.getWidth()/2,scoreBoardBuffered.getHeight()/2,Image.SCALE_SMOOTH);
        scoreBoardLabel = new JLabel(new ImageIcon(scoreBoard));
        scoreBoardLabel.setLayout(new FlowLayout());
        scoreBoardFrame.add(scoreBoardLabel);
        scoreBoardFrame.setVisible(true);
        scoreBoardFrame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        scoreBoardFrame.pack();
        setSkull(Color.green,1);
    }

    /**
     * Fills with a colored oval a skull on the ScoreBoard.
     * @param color indicates the player.
     * @param i the index of the skull where to draw.
     */
    public void setSkull(Color color,int i){
        Graphics g = scoreBoardLabel.getGraphics();
        g.setColor(color);
        g.fillOval((30 + 108 * i)/2,91/2,60/2,95/2);
        g.dispose();

    }


    public static void main(String[] args){
        GUIScoreBoard sb = new GUIScoreBoard();
    }
}
