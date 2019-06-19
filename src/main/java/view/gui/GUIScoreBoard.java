package view.gui;

import java.awt.*;
import java.awt.event.*;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import javax.imageio.ImageIO;
import javax.swing.*;

@SuppressWarnings("serial")
public class GUIScoreBoard extends JPanel {
    private static List<Color> DRAWING_COLOR = new ArrayList<>();

    private List<Integer> skullIndex = new ArrayList<>();

    private BufferedImage scoreBoardBuffered;


    public GUIScoreBoard() throws IOException {

        scoreBoardBuffered = ImageIO.read(new File("src/resources/gui/ScoreBoardScaled.png"));

        setSkullIndex(0,Color.red);
        setSkullIndex(1,Color.green);
        setSkullIndex(7,Color.yellow);

        Graphics g = scoreBoardBuffered.getGraphics();
        g.drawImage(scoreBoardBuffered, 0, 0, this);
        g.dispose();
        setLayout(new BorderLayout());
    }

    public void setSkullIndex(int i, Color color){
        this.skullIndex.add(i);
        DRAWING_COLOR.add(color);
    }

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        if (scoreBoardBuffered != null) {
            g.drawImage(scoreBoardBuffered, 0, 0, this);
        }

        for(int i = 0; i<skullIndex.size(); i++) {
            g.setColor(DRAWING_COLOR.get(i));

            //TODO modify this with new size of scoreboard image
            g.fillOval((50 + 161 * skullIndex.get(i)) / 2, 140 / 2, 80 / 2, 150 / 2);
        }

    }


    private static void createAndShowGui() {
        GUIScoreBoard mainPanel = null;
        try {
            mainPanel = new GUIScoreBoard();
        } catch (IOException e) {
            e.printStackTrace();
            System.exit(-1);
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
        SwingUtilities.invokeLater(GUIScoreBoard::createAndShowGui);
    }
}