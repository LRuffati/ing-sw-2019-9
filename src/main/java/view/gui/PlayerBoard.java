package view.gui;

import board.GameMap;
import gamemanager.GameBuilder;
import player.Actor;
import viewclasses.ActorView;
import viewclasses.GameMapView;

import javax.imageio.ImageIO;
import javax.swing.*;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.geom.AffineTransform;
import java.awt.image.AffineTransformOp;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.List;

public class PlayerBoard extends JPanel {

    private BufferedImage[] normalBoards = new BufferedImage[5];
    private BufferedImage[] frenzyBoards = new BufferedImage[5];
    private BufferedImage yourNormalBoard;
    private BufferedImage yourFrenzyBoard;
    private BufferedImage toPaint;
    private BufferedImage nextPlayerBuffered;
    private ImageIcon nextPlayerIcon;
    private JButton changeBoard;

    public PlayerBoard(GameMapView gmv) throws IOException {
        nextPlayerBuffered = ImageIO.read(ClassLoader.getSystemResourceAsStream("gui/PlayerBoards/nextPlayer.png"));
        nextPlayerIcon = new ImageIcon(nextPlayerBuffered);
        initializeBoards(gmv.players());
        //setYourBoards(gmv.you().color());
        changeBoard = new JButton(nextPlayerIcon);
        changeBoard.setContentAreaFilled(false);
        //drawBoard(yourNormalBoard);
        add(changeBoard);
    }

    private void setYourBoards(Color color){
        switch(color.toString()){
            case("java.awt.Color[r=0,g=0,b=255]"):
                yourNormalBoard = normalBoards[0];
                yourFrenzyBoard = frenzyBoards[0];
                break;

            case("java.awt.Color[r=128,g=128,b=128]"):
                yourNormalBoard = normalBoards[1];
                yourFrenzyBoard = frenzyBoards[1];
                break;

            case("java.awt.Color[r=0,g=255,b=0]"):
                yourNormalBoard = normalBoards[2];
                yourFrenzyBoard = frenzyBoards[2];
                break;

            case("java.awt.Color[r=255,g=175,b=175]"):
                yourNormalBoard = normalBoards[3];
                yourFrenzyBoard = frenzyBoards[3];
                break;

            case("java.awt.Color[r=255,g=255,b=0]"):
                yourNormalBoard = normalBoards[4];
                yourFrenzyBoard = frenzyBoards[4];
                break;

            default:
                break;

        }
    }

    void setYourBoards(int i) {
        yourNormalBoard = normalBoards[i];
        yourFrenzyBoard = frenzyBoards[i];
    }

    private void initializeBoards(List<ActorView> players) throws IOException {
        int i = 0;
        for(ActorView actorView : players) {
            switch (actorView.colorString()) {
                case "blue":
                    normalBoards[i] = ImageIO.read(ClassLoader.getSystemResourceAsStream("gui/PlayerBoards/blueN.png"));
                    frenzyBoards[i] = ImageIO.read(ClassLoader.getSystemResourceAsStream("gui/PlayerBoards/blueF.png"));
                    i++;
                    break;
                case "green":
                    normalBoards[i] = ImageIO.read(ClassLoader.getSystemResourceAsStream("gui/PlayerBoards/greenN.png"));
                    frenzyBoards[i] = ImageIO.read(ClassLoader.getSystemResourceAsStream("gui/PlayerBoards/greenF.png"));
                    i++;
                    break;
                case "gray":
                    normalBoards[i] = ImageIO.read(ClassLoader.getSystemResourceAsStream("gui/PlayerBoards/grayN.png"));
                    frenzyBoards[i] = ImageIO.read(ClassLoader.getSystemResourceAsStream("gui/PlayerBoards/grayF.png"));
                    i++;
                    break;
                case "pink":
                    normalBoards[i] = ImageIO.read(ClassLoader.getSystemResourceAsStream("gui/PlayerBoards/pinkN.png"));
                    frenzyBoards[i] = ImageIO.read(ClassLoader.getSystemResourceAsStream("gui/PlayerBoards/pinkF.png"));
                    i++;
                    break;
                case "yellow":
                    normalBoards[i] = ImageIO.read(ClassLoader.getSystemResourceAsStream("gui/PlayerBoards/yellowN.png"));
                    frenzyBoards[i] = ImageIO.read(ClassLoader.getSystemResourceAsStream("gui/PlayerBoards/yellowF.png"));
                    i++;
                    break;

                    default:
                        break;
            }
        }

        if(Toolkit.getDefaultToolkit().getScreenSize().getHeight() == 768.0){
            for(int j=0; j<=i; j++) {
                if(normalBoards[j]!=null) {
                    normalBoards[j] = scaleImage(normalBoards[j]);
                    frenzyBoards[j] = scaleImage(frenzyBoards[j]);
                }
            }

        }
    }

    private BufferedImage scaleImage(BufferedImage toScale){
        int w = toScale.getWidth();
        int h = toScale.getHeight();
        BufferedImage scaled = new BufferedImage(w, h, BufferedImage.TYPE_INT_ARGB);
        AffineTransform at = new AffineTransform();
        at.scale(0.711458, 0.711111);
        AffineTransformOp scaleOp =
                new AffineTransformOp(at, AffineTransformOp.TYPE_BILINEAR);
        return scaleOp.filter(toScale, scaled);
    }

    public void drawBoard(BufferedImage image){
        toPaint = image;
        Graphics g = image.getGraphics();
        g.drawImage(image, 0, 0, this);
        g.dispose();
    }

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);

        g.drawImage(toPaint, 0, 0, this);
        repaint();

    }

    public JButton getChangeBoard() {
        return changeBoard;
    }

    public BufferedImage[] getNormalBoards() {
        return normalBoards;
    }

    public BufferedImage getYourNormalBoard() {
        return yourNormalBoard;
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

        PlayerBoard mainPanel = null;
        try {
            mainPanel = new PlayerBoard(gmv);
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
        SwingUtilities.invokeLater(PlayerBoard::createAndShowGui);
    }
}
