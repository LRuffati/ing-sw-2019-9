package network;

import java.util.*;

public class TimerForDisconnection {
    private static Map<TimerTask, String> timerMap = new HashMap<>();
    private static Map<String, Timer> tokenToTimerMap = new HashMap<>();
    private static Map<Timer, TimerTask> timerTimerTaskMap = new HashMap<>();

    private TimerForDisconnection(){}

    public static void add(String token) {
        TimerTask repeatedTask = new TimerTask() {
            @Override
            public void run() {
                Database.get().logout(timerMap.get(this));
            }
        };

        Timer timer = new Timer("timerForDisconnection");
        timerMap.put(repeatedTask, token);
        tokenToTimerMap.put(token, timer);
        timerTimerTaskMap.put(timer, repeatedTask);
        timer.schedule(repeatedTask, 2000);
    }

    public static void reset(String token) {
        tokenToTimerMap.get(token).cancel();
        tokenToTimerMap.get(token).schedule(timerTimerTaskMap.get(tokenToTimerMap.get(token)), 200);
    }

    public static void stop(String token) {
        if(tokenToTimerMap.containsKey(token))
            tokenToTimerMap.get(token).cancel();
    }
}
