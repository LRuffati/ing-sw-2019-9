package network;

import java.util.*;

public class TimerForDisconnection {
    private static Map<TimerTask, String> timerMap = new HashMap<>();
    private static Map<String, Timer> tokenToTimerMap = new HashMap<>();
    private static Map<Timer, TimerTask> timerTimerTaskMap = new HashMap<>();
    private static Map<TimerTask, Integer> numOfErrors = new HashMap<>();

    private TimerForDisconnection(){}

    public static void add(String token) {

        TimerTask repeatedTask = new TimerTask() {
            @Override
            public void run() {
                try {
                    numOfErrors.put(this, numOfErrors.get(this) + 1);

                    int num = numOfErrors.get(this);
                    if (num >= 2) {
                        System.out.println("NumOfErrors\t" + num + "\t\t" + Database.get().getUserByToken(token).getUsername());
                    }

                    if (numOfErrors.get(this) == 10) {
                        Database.get().logout(timerMap.get(this));
                        //stop(timerMap.get(this));
                    }
                }
                catch (NullPointerException e) {
                    System.out.println("NULLPOINTER\t\t" + Database.get().getUserByToken(token).getUsername());
                    e.printStackTrace();
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

    public static void stop(String token) {
        if(tokenToTimerMap.containsKey(token)) {
            System.out.println("Stopping the timer");
            timerTimerTaskMap.get(tokenToTimerMap.get(token)).cancel();
            tokenToTimerMap.get(token).cancel();
        }
        else System.out.println("NoTimerForDisconnection");

        TimerTask toBeDeleted = timerTimerTaskMap.get(tokenToTimerMap.get(token));
        numOfErrors.remove(toBeDeleted);
        timerMap.remove(toBeDeleted);
        timerTimerTaskMap.remove(tokenToTimerMap.get(token));
        tokenToTimerMap.remove(token);
    }
}
