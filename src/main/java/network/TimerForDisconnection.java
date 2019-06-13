package network;

import javax.xml.crypto.Data;
import java.util.*;

public class TimerForDisconnection {
    private static Map<TimerTask, String> timerMap = new HashMap<>();
    private static Map<String, Timer> tokenToTimerMap = new HashMap<>();
    private static Map<TimerTask, Integer> numOfErrors = new HashMap<>();

    private TimerForDisconnection(){}

    public static void add(String token) {
        TimerTask repeatedTask = new TimerTask() {
            @Override
            public void run() {
                numOfErrors.put(this, numOfErrors.get(this) + 1);

                //System.out.println(Database.get().getUserByToken(timerMap.get(this)).getUsername());

                int num = numOfErrors.get(this);
                if(num>=2) {
                    System.out.print("NumOfErrors\t" + num + "\t\t");
                    System.out.println(Database.get().getUserByToken(timerMap.get(this)).getUsername());
                }

                if (numOfErrors.get(this) >= 10) {
                    Database.get().logout(timerMap.get(this));
                    stop(timerMap.get(this));
                }
            }
        };

        Timer timer = new Timer("timerForDisconnection");
        timerMap.put(repeatedTask, token);
        tokenToTimerMap.put(token, timer);
        numOfErrors.put(repeatedTask, 0);
        timer.schedule(repeatedTask, 300,300);
        System.out.println("timer\t"+timer+"\n"+"timertask\t"+repeatedTask+"\n"+"token\t"+token+"\n");
    }

    public static void reset(String token) {
        for(Map.Entry entry : timerMap.entrySet()) {
            if(entry.getValue().equals(token)) {
                System.out.println("Reset di\t"+Database.get().getUserByToken(token).getUsername() + "\t\t" + timerMap.get((TimerTask)entry.getKey()).equals(token));
                numOfErrors.replace((TimerTask)entry.getKey(), 0);
                return;
            }
        }
        System.out.println("niente reset");
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
        if(tokenToTimerMap.containsKey(token)){
            System.out.println("Stopping the timer");
            tokenToTimerMap.get(token).cancel();
        }
        else System.out.println("NoTimerForDisconnection");

        for(Map.Entry entry : timerMap.entrySet()) {
            if(entry.getValue().equals(token)) {
                numOfErrors.remove(entry.getKey());
                timerMap.remove(entry.getKey());
                return;
            }
        }
    }
}
