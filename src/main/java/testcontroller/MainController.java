package testcontroller;

import actions.effects.Effect;
import gamemanager.GameBuilder;
import gamemanager.ParserConfiguration;
import network.Database;
import network.NetworkBuilder;
import network.Player;
import network.ServerInterface;
import player.Actor;

import java.awt.*;
import java.io.FileNotFoundException;
import java.util.Iterator;
import java.util.List;
import java.util.Timer;
import java.util.TimerTask;


public class MainController {

    private int numOfPlayer;

    private Timer timerForStarting;
    private boolean timerRunning = false;
    private boolean gameStarted = false;

    private List<SlaveController> slaveControllerList;

    /**
     *
     * @param responsible the SlaveController which started it all (redundant)
     * @param effects (the list of effects)
     * @param onResolved (When I finish the list of effects run this function)
     *                   If I call a slave controller function in between then I will have to
     *                   call it and pass a runnable that will:
     * @return
     */
    public void resolveEffect(SlaveController responsible, List<Effect> effects,
                                 Runnable onResolved){
        if (effects.isEmpty()){
            new Thread(onResolved).start();
        } else {
            Effect first = effects.get(0);
            List<Effect> next = effects.subList(1, effects.size());
            Runnable nextOnRes = new Runnable() {
                @Override
                public void run() {
                    MainController.this.resolveEffect(responsible, next, onResolved);
                }
            };
            (new Thread(new Runnable() {
                @Override
                public void run() {
                   first.mergeInGameMap(responsible, nextOnRes);
                }
            })).start();
        }
    }

}
