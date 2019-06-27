package view.gui;

import board.GameMap;
import gamemanager.GameBuilder;
import player.Actor;
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
        initializeBoards();
        setYourBoards(gmv.you().color());
        changeBoard = new JButton(nextPlayerIcon);
        changeBoard.setContentAreaFilled(false);
        drawBoard(yourNormalBoard);
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

    private void initializeBoards() throws IOException {
        normalBoards[0] = ImageIO.read(ClassLoader.getSystemResourceAsStream("gui/PlayerBoards/blueN.png"));
        normalBoards[1] = ImageIO.read(ClassLoader.getSystemResourceAsStream("gui/PlayerBoards/grayN.png"));
        normalBoards[2] = ImageIO.read(ClassLoader.getSystemResourceAsStream("gui/PlayerBoards/greenN.png"));
        normalBoards[3] = ImageIO.read(ClassLoader.getSystemResourceAsStream("gui/PlayerBoards/pinkN.png"));
        normalBoards[4] = ImageIO.read(ClassLoader.getSystemResourceAsStream("gui/PlayerBoards/yellowN.png"));
        frenzyBoards[0] = ImageIO.read(ClassLoader.getSystemResourceAsStream("gui/PlayerBoards/blueF.png"));
        frenzyBoards[1] = ImageIO.read(ClassLoader.getSystemResourceAsStream("gui/PlayerBoards/grayF.png"));
        frenzyBoards[2] = ImageIO.read(ClassLoader.getSystemResourceAsStream("gui/PlayerBoards/greenF.png"));
        frenzyBoards[3] = ImageIO.read(ClassLoader.getSystemResourceAsStream("gui/PlayerBoards/pinkF.png"));
        frenzyBoards[4] = ImageIO.read(ClassLoader.getSystemResourceAsStream("gui/PlayerBoards/yellowF.png"));
        if(Toolkit.getDefaultToolkit().getScreenSize().getHeight() == 768.0){
            normalBoards[0] = scaleImage(normalBoards[0]);
            normalBoards[1] = scaleImage(normalBoards[1]);
            normalBoards[2] = scaleImage(normalBoards[2]);
            normalBoards[3] = scaleImage(normalBoards[3]);
            normalBoards[4] = scaleImage(normalBoards[4]);
            frenzyBoards[0] = scaleImage(frenzyBoards[0]);
            frenzyBoards[1] = scaleImage(frenzyBoards[1]);
            frenzyBoards[2] = scaleImage(frenzyBoards[2]);
            frenzyBoards[3] = scaleImage(frenzyBoards[3]);
            frenzyBoards[4] = scaleImage(frenzyBoards[4]);

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
