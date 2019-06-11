package view.gui;

import java.awt.*;
import java.awt.event.*;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

import javax.imageio.ImageIO;
import javax.swing.*;

@SuppressWarnings("serial")
public class GUIScoreBoard extends JPanel {
    private static final Color DRAWING_COLOR = new Color(255, 100, 200);
    private static final Color FINAL_DRAWING_COLOR = Color.red;

    private int skullIndex;

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

        Graphics g = scoreBoardBuffered.getGraphics();
        g.drawImage(scoreBoardBuffered, 0, 0, this);
        g.dispose();

    }

    public void setSkullIndex(int i){
        this.skullIndex = i;
    }

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        if (scoreBoardBuffered != null) {
            g.drawImage(scoreBoardBuffered, 0, 0, this);
        }

        g.setColor(DRAWING_COLOR);

        g.fillOval((30 + 108 * skullIndex)/2,91/2,60/2,95/2);

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
        frame.setSize(new Dimension(1000,250));
        frame.setLocationByPlatform(true);
        frame.setVisible(true);
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(GUIScoreBoard::createAndShowGui);
    }
}