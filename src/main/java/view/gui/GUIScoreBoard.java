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
    private static Color FINAL_DRAWING_COLOR;

    private List<Integer> skullIndex = new ArrayList<>();

    private BufferedImage scoreBoardBuffered;
    private Image img;
    private Point startPt = null;
    private Point endPt = null;
    private Point currentPt = null;
    private int prefW;
    private int prefH;

    public GUIScoreBoard() throws IOException {
        scoreBoardBuffered = ImageIO.read(new File("src/resources/gui/ScoreBoardScaled.png"));
        img = scoreBoardBuffered.getScaledInstance(scoreBoardBuffered.getWidth()/3,scoreBoardBuffered.getHeight()/3,Image.SCALE_SMOOTH);

        setSkullIndex(8,Color.red);
        setSkullIndex(6, Color.YELLOW);
        setSkullIndex(3, Color.YELLOW);
        setSkullIndex(4, Color.YELLOW);

        Graphics g = scoreBoardBuffered.getGraphics();
        g.drawImage(scoreBoardBuffered, 0, 0, this);
        g.dispose();

    }

    public void setSkullIndex(int i, Color color){
        this.skullIndex.add(i);
        DRAWING_COLOR.add(color);
        FINAL_DRAWING_COLOR = color;
    }

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        if (scoreBoardBuffered != null) {
            g.drawImage(scoreBoardBuffered, 0, 0, this);
        }

        for(int i = 0; i<skullIndex.size(); i++) {
            g.setColor(DRAWING_COLOR.get(i));

            g.fillOval((33 + 108 * skullIndex.get(i)) / 2, 91 / 2, 61 / 2, 95 / 2);
        }

    }

    @Override
    public Dimension getPreferredSize() {
        return new Dimension(prefW, prefH);
    }

    public void drawToBackground() {
        Graphics g = scoreBoardBuffered.getGraphics();
        g.setColor(FINAL_DRAWING_COLOR);
        g.drawRect((30 + 108)/2,91/2,60/2,95/2);
        g.dispose();

        startPt = null;
        repaint();
    }

    private class MyMouseAdapter extends MouseAdapter {
        @Override
        public void mouseDragged(MouseEvent mEvt) {
            currentPt = mEvt.getPoint();
            GUIScoreBoard.this.repaint();
        }

        @Override
        public void mouseReleased(MouseEvent mEvt) {
            endPt = mEvt.getPoint();
            currentPt = null;
            drawToBackground();
        }

        @Override
        public void mousePressed(MouseEvent mEvt) {
            startPt = mEvt.getPoint();
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
        frame.setSize(new Dimension(500,145));
        frame.setLocationByPlatform(true);
        frame.setVisible(true);
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(GUIScoreBoard::createAndShowGui);
    }
}