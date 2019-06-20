package view.gui;

import javax.imageio.ImageIO;
import javax.swing.*;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

public class PlayerBoard extends JPanel {

    private BufferedImage[] normalBoards = new BufferedImage[5];
    private BufferedImage[] frenzyBoards = new BufferedImage[5];
    private BufferedImage yourNormalBoard;
    private BufferedImage yourFrenzyBoard;
    private BufferedImage toPaint;
    private BufferedImage nextPlayerBuffered;
    private ImageIcon nextPlayerIcon;
    private JButton changeBoard;

    public PlayerBoard(Color color) throws IOException {
        nextPlayerBuffered = ImageIO.read(new File("src/resources/gui/PlayerBoards/nextPlayer.png"));
        nextPlayerIcon = new ImageIcon(nextPlayerBuffered);
        initializeBoards();
        setYourBoards(color);
        changeBoard = new JButton(nextPlayerIcon);
        changeBoard.setContentAreaFilled(false);
        setChangeBoard();
        drawBoard(yourNormalBoard);

        add(changeBoard);
    }

    private void setChangeBoard(){
        changeBoard.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                super.mouseClicked(e);
                for(int i = 0; i< normalBoards.length; i++){
                    if(normalBoards[i].equals(yourNormalBoard)){
                        if(i!=normalBoards.length-1){
                            drawBoard(normalBoards[i+1]);
                        } else drawBoard(normalBoards[0]);
                    }
                }
            }
        });
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

    private void initializeBoards() throws IOException {
        normalBoards[0] = ImageIO.read(new File("src/resources/gui/PlayerBoards/blueN.png"));
        normalBoards[1] = ImageIO.read(new File("src/resources/gui/PlayerBoards/grayN.png"));
        normalBoards[2] = ImageIO.read(new File("src/resources/gui/PlayerBoards/greenN.png"));
        normalBoards[3] = ImageIO.read(new File("src/resources/gui/PlayerBoards/pinkN.png"));
        normalBoards[4] = ImageIO.read(new File("src/resources/gui/PlayerBoards/yellowN.png"));
        frenzyBoards[0] = ImageIO.read(new File("src/resources/gui/PlayerBoards/blueF.png"));
        frenzyBoards[1] = ImageIO.read(new File("src/resources/gui/PlayerBoards/grayF.png"));
        frenzyBoards[2] = ImageIO.read(new File("src/resources/gui/PlayerBoards/greenF.png"));
        frenzyBoards[3] = ImageIO.read(new File("src/resources/gui/PlayerBoards/pinkF.png"));
        frenzyBoards[4] = ImageIO.read(new File("src/resources/gui/PlayerBoards/yellowF.png"));
    }

    private void drawBoard(BufferedImage image){
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


    private static void createAndShowGui() {
        PlayerBoard mainPanel = null;
        try {
            mainPanel = new PlayerBoard(Color.BLUE);
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
        SwingUtilities.invokeLater(PlayerBoard::createAndShowGui);
    }

}
