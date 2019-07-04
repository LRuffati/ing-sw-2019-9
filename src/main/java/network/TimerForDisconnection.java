package network;

import java.util.*;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * This class contains all the methods needed to check and handle forced disconnections
 */
public class TimerForDisconnection {
    private static Map<TimerTask, String> timerMap = new HashMap<>();
    private static Map<String, Timer> tokenToTimerMap = new HashMap<>();
    private static Map<Timer, TimerTask> timerTimerTaskMap = new HashMap<>();
    private static Map<TimerTask, Integer> numOfErrors = new HashMap<>();

    private static Logger logger = Logger.getLogger(TimerForDisconnection.class.getSimpleName());

    private TimerForDisconnection(){}

    /**
     * This method adds a new Timer for the player linked with the Token.
     * If the timer fails to receive news from the client for a certain amount of time the player is logged out
         */
    public static void add(String token) {

        TimerTask repeatedTask = new TimerTask() {
            @Override
            public void run() {
                try {
                    numOfErrors.put(this, numOfErrors.get(this) + 1);

                    /*
                    int num = numOfErrors.get(this);
                    if (num >= 2) {
                        logger.log(Level.INFO, "NumOfErrors\t" + num + "\t\t" + Database.get().getUserByToken(token).getUsername());
                    }
                    */

                    if (numOfErrors.get(this) == 10) {
                        Database.get().logout(timerMap.get(this));
                        //stop(timerMap.get(this));
                    }
                }
                catch (NullPointerException e) {
                    logger.log(Level.INFO, e.getMessage());
                }
            }
        };

        Timer timer = new Timer("timerForDisconnection");
        timerMap.put(repeatedTask, token);
        tokenToTimerMap.put(token, timer);
        timerTimerTaskMap.put(timer, repeatedTask);
        numOfErrors.put(repeatedTask, 0);
        timer.schedule(repeatedTask, 300,500);
    }

    /**
     * Reset the counter of the timer. Called when a ping response is received
     */
    public static void reset(String token) {
        for(Map.Entry entry : timerMap.entrySet()) {
            if(entry.getValue().equals(token)) {
                numOfErrors.replace((TimerTask)entry.getKey(), 0);
                return;
            }
        }
        /*
        try {
            tokenToTimerMap.get(token).cancel();

            for(Map.Entry entry : timerMap.entrySet()) {
                if(entry.getValue().equals(token)) {
                    timerMap.remove((TimerTask)entry.getKey());
                    numOfErrors.remove((TimerTask)entry.getKey());
                    break;
                }
            }

            tokenToTimerMap.remove(token);
            add(token);
        }
        catch (IllegalStateException e) {
            e.printStackTrace();
        }
        */
    }

    /**
     * When the client logs out his timer is stopped
     */
    public static void stop(String token) {
        if(tokenToTimerMap.containsKey(token)) {
            logger.log(Level.INFO, "Stopping the timer");
            timerTimerTaskMap.get(tokenToTimerMap.get(token)).cancel();
            tokenToTimerMap.get(token).cancel();
        }
        else
            logger.log(Level.INFO, "NoTimerForDisconnection");

        TimerTask toBeDeleted = timerTimerTaskMap.get(tokenToTimerMap.get(token));
        numOfErrors.remove(toBeDeleted);
        timerMap.remove(toBeDeleted);
        timerTimerTaskMap.remove(tokenToTimerMap.get(token));
        tokenToTimerMap.remove(token);
    }
}
