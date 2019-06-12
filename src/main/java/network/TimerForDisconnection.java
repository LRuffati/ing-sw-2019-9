package network;

import java.util.*;

public class TimerForDisconnection {
    private static Map<TimerTask, String> timerMap = new HashMap<>();
    private static Map<String, Timer> tokenToTimerMap = new HashMap<>();

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
        timer.schedule(repeatedTask, 200);
    }

    public static void reset(String token) {
        try {
            tokenToTimerMap.get(token).cancel();

            for(Map.Entry entry : timerMap.entrySet()) {
                if(entry.getValue().equals(token)) {
                    timerMap.remove(entry.getKey());
                    break;
                }
            }

            tokenToTimerMap.remove(token);
            add(token);
        }
        catch (IllegalStateException e) {
            e.printStackTrace();
        }
    }

    public static void stop(String token) {
        if(tokenToTimerMap.containsKey(token))
            tokenToTimerMap.get(token).cancel();
    }
}
