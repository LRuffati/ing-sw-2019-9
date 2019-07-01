package view.gui;

import viewclasses.PowerUpView;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.Objects;

public class PUCard {

    private BufferedImage card;
    private PowerUpView pu;

    public PUCard(PowerUpView pu){
        this.pu = pu;
        linkPUToCard();
    }

    private void linkPUToCard(){
        String path = "src/main/resources/gui/Cards/AD_powerups_IT_02.png";
        switch (pu.type()){
            case TAGBACKGRANADE:
                switch (pu.ammo()){
                    case YELLOW:
                        path = "src/main/resources/gui/Cards/AD_powerups_IT_024.png";
                        break;

                    case BLUE:
                        path = "src/main/resources/gui/Cards/AD_powerups_IT_022.png";
                        break;

                    case RED:
                        path = "src/main/resources/gui/Cards/AD_powerups_IT_023.png";
                        break;
                }
                break;

            case NEWTON:
                switch (pu.ammo()){
                    case YELLOW:
                        path = "src/main/resources/gui/Cards/AD_powerups_IT_0210.png";
                        break;

                    case BLUE:
                        path = "src/main/resources/gui/Cards/AD_powerups_IT_028.png";
                        break;

                    case RED:
                        path = "src/main/resources/gui/Cards/AD_powerups_IT_029.png";
                        break;
                }
                break;

            case TELEPORTER:
                switch (pu.ammo()){
                    case YELLOW:
                        path = "src/main/resources/gui/Cards/AD_powerups_IT_0213.png";
                        break;

                    case BLUE:
                        path = "src/main/resources/gui/Cards/AD_powerups_IT_0211.png";
                        break;

                    case RED:
                        path = "src/main/resources/gui/Cards/AD_powerups_IT_0212.png";
                        break;
                }
                break;

            case TARGETINGSCOPE:
                switch (pu.ammo()){
                    case YELLOW:
                        path = "src/main/resources/gui/Cards/AD_powerups_IT_027.png";
                        break;

                    case BLUE:
                        path = "src/main/resources/gui/Cards/AD_powerups_IT_025.png";
                        break;

                    case RED:
                        path = "src/main/resources/gui/Cards/AD_powerups_IT_026.png";
                        break;
                }
                break;

            default:
                path = "src/main/resources/gui/Cards/AD_powerups_IT_02.png";
                break;
        }
        try {
            card = ImageIO.read(new File(path));
        } catch (IOException e) {
            System.out.println("Problema con lettura dell'immagine della carta " + pu.type() + pu.ammo().toString());
        }
    }


    public BufferedImage getCard() {
        return card;
    }
}
