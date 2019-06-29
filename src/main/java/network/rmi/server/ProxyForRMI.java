package network.rmi.server;

import controller.GameMode;
import network.Database;
import network.Player;
import network.ServerInterface;
import viewclasses.GameMapView;

import java.rmi.RemoteException;
import java.rmi.server.UnicastRemoteObject;
import java.util.logging.Level;
import java.util.logging.Logger;

public class ProxyForRMI extends UnicastRemoteObject implements ServerInterface {

    private transient ServerInterface remoteObject;
    private transient Logger logger = Logger.getLogger(getClass().getSimpleName());
    private transient ServerInterface thisObj = ProxyForRMI.this;

    public ProxyForRMI(ServerInterface remoteObject1) throws RemoteException{
        remoteObject = remoteObject1;
    }

    @Override
    public void setToken(String token) {
        Thread thread = new Thread() {
            @Override
            public void run() {
                try {
                    remoteObject.setToken(token);
                }
                catch (RemoteException e){
                    logger.log(Level.INFO, "setToken", e.getMessage());
                    Database.get().logout(thisObj);
                }
                Thread.currentThread().interrupt();
            }
        };
        thread.start();
    }

    @Override
    public void sendUpdate(String str) throws RemoteException {
        Thread thread = new Thread() {
            @Override
            public void run() {
                try {
                    remoteObject.sendUpdate(str);
                }
                catch (RemoteException e){
                    logger.log(Level.INFO, "sendUpdate", e.getMessage());
                    Database.get().logout(thisObj);
                }
                Thread.currentThread().interrupt();
            }
        };
        thread.start();
    }

    @Override
    public void sendException(Exception exception) {
        Thread thread = new Thread() {
            @Override
            public void run() {
                try {
                    remoteObject.sendException(exception);
                }
                catch (RemoteException e){
                    logger.log(Level.INFO, "sendExc", e.getMessage());
                    Database.get().logout(thisObj);
                }
                Thread.currentThread().interrupt();
            }
        };
        thread.start();
    }

    @Override
    public void ping() {
        Thread thread = new Thread() {
            @Override
            public void run() {
                try {
                    remoteObject.ping();
                }
                catch (RemoteException e){
                    logger.log(Level.INFO, "ping", e.getMessage());
                    //Database.get().logout(thisObj);
                }
                Thread.currentThread().interrupt();
            }
        };
        thread.start();
    }

    @Override
    public void nofifyMap(GameMapView gameMap) {
        Thread thread = new Thread() {
            @Override
            public void run() {
                try {
                    remoteObject.nofifyMap(gameMap);
                }
                catch (RemoteException e){
                    logger.log(Level.INFO, "notifymap", e.getMessage());
                    Database.get().logout(thisObj);
                }
                Thread.currentThread().interrupt();
            }
        };
        thread.start();
    }

    @Override
    public void onConnection(Player player, int numOfPlayer) {
        Thread thread = new Thread() {
            @Override
            public void run() {
                try {
                    remoteObject.onConnection(player, numOfPlayer);
                }
                catch (RemoteException e){
                    logger.log(Level.INFO, "connection", e.getMessage());
                    Database.get().logout(thisObj);
                }
                Thread.currentThread().interrupt();
            }
        };
        thread.start();

    }

    @Override
    public void onDisconnection(Player player, int numOfPlayer) {
        Thread thread = new Thread() {
            @Override
            public void run() {
                try {
                    remoteObject.onDisconnection(player, numOfPlayer);
                }
                catch (RemoteException e){
                    logger.log(Level.INFO, "ondisconnection", e.getMessage());
                    Database.get().logout(thisObj);
                }
                Thread.currentThread().interrupt();
            }
        };
        thread.start();
    }

    @Override
    public void onStarting(String map, GameMode gameMode) {
        Thread thread = new Thread() {
            @Override
            public void run() {
                try {
                    remoteObject.onStarting(map, gameMode);
                }
                catch (RemoteException e){
                    logger.log(Level.INFO, e.getMessage());
                    Database.get().logout(thisObj);
                }
                Thread.currentThread().interrupt();
            }
        };
        thread.start();
    }

    @Override
    public void onTimer(int ms) {
        Thread thread = new Thread() {
            @Override
            public void run() {
                try {
                    remoteObject.onTimer(ms);
                }
                catch (RemoteException e){
                    logger.log(Level.INFO, "onTimer", e.getMessage());
                    Database.get().logout(thisObj);
                }
                Thread.currentThread().interrupt();
            }
        };
        thread.start();

    }

    @Override
    public void onWinner(String winner, int winnerPoints, int yourPoint) {
        Thread thread = new Thread() {
            @Override
            public void run() {
                try {
                    remoteObject.onWinner(winner, winnerPoints, yourPoint);
                }
                catch (RemoteException e){
                    logger.log(Level.INFO, "onWinner", e.getMessage());
                    Database.get().logout(thisObj);
                }
                Thread.currentThread().interrupt();
            }
        };
        thread.start();
    }

    @Override
    public boolean equals(Object obj) {
        return super.equals(obj);
    }

    @Override
    public int hashCode() {
        return super.hashCode();
    }
}
