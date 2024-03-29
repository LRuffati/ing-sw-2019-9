package view.gui;

import gamemanager.GameBuilder;
import viewclasses.GameMapView;

import javax.imageio.ImageIO;
import javax.swing.*;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;

public class GameFrame extends JFrame {

    private GUIMap map;
    private GUIScoreBoard scoreBoard;
    private OutputBox outputBox;
    private BufferedImage background;
    private Framework framework;
    private PlayerCardsPanel playerCardsPanel;

    public GameFrame(GameMapView gmv, Framework framework, int mapNum){
        this.framework = framework;

        try {
            this.background = ImageIO.read(new File("src/resources/gui/background.png"));
        } catch (IOException e) {
            System.out.println("Problemi nello sfondo");
        }

        setContentPane(new Background(background));

        pickMap(gmv, mapNum);

        try {
            this.scoreBoard = new GUIScoreBoard();
        } catch (IOException e) {
            System.out.println("Problemi nella Scoreboard");
        }

        this.playerCardsPanel = new PlayerCardsPanel(gmv);

        this.outputBox = new OutputBox();


        JPanel scorePlusBox = new JPanel();
        scorePlusBox.setLayout(new GridLayout(0,1));
        scorePlusBox.add(scoreBoard);
        scorePlusBox.add(outputBox);

        getContentPane().setLayout(new GridLayout(1,0));
        getContentPane().add(map);
        JPanel sndPanel = new JPanel();
        sndPanel.setLayout(new GridLayout(0,1));
        //sndPanel.add(scoreBoard);
        //sndPanel.add(new PlayerCardsPanel(gmv));
        //sndPanel.add(outputBox);
        sndPanel.add(scorePlusBox);
        sndPanel.add(playerCardsPanel.getPlayerBoard());
        sndPanel.add(playerCardsPanel.getWeaponCards());


        add(sndPanel);
        if(Toolkit.getDefaultToolkit().getScreenSize().getHeight()==1080){
            setSize(1538,1045);
        } else {
            setSize(1085,743);
        }
        setVisible(true);
        setResizable(true);
        setDefaultCloseOperation(EXIT_ON_CLOSE);
    }

    public static void main(String[] args) throws FileNotFoundException {
        GameBuilder game = new GameBuilder(5);

        GameMapView mv = game.getMap().generateView(game.getActorList().get(0).pawnID());

        new GameFrame(mv, null,1);
    }

    private void pickMap(GameMapView gmv, int mapNum){
        switch (mapNum){

            case 2:
                this.map = new GUIMap2(gmv, framework);
                break;

            case 3:
                this.map = new GUIMap3(gmv, framework);
                break;

            case 4:
                this.map = new GUIMap4(gmv, framework);
                break;
            case 1:

            default:
                this.map = new GUIMap1(gmv, framework);
                break;
        }
    }

    public OutputBox getOutputBox() {
        return outputBox;
    }

    public GUIMap getMap() {
        return map;
    }

    public void setOutputBox(OutputBox outputBox) {
        this.outputBox = outputBox;
    }
}

class Background extends JComponent {
    private Image image;
    Background(Image image) {
        this.image = image;
    }
    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        g.drawImage(image, 0, 0, this);
    }
}
