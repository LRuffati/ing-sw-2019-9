package network.socket.server;

import controller.GameMode;
import gamemanager.GameBuilder;
import genericitems.Tuple;
import network.*;
import network.exception.InvalidTokenException;
import network.socket.messages.*;
import network.exception.InvalidLoginException;
import network.socket.messages.notify.*;
import controller.controllermessage.ControllerMessage;
import viewclasses.GameMapView;

/**
 * This class handles all the methods called by the Server(implemented in {@link ServerInterface Serverinterface})
 * and the Requests called by the clients (implemented in {@link RequestHandler RequestHandler})
 */
public class ServerNetworkSocket implements RequestHandler, ServerInterface {

    private final ClientHandler clientHandler;
    private String token;
    private Player player;

    public ServerNetworkSocket(ClientHandler clientHandler){
        this.clientHandler = clientHandler;
        //TODO: stop command
    }


    private boolean checkConnection(String token) {
        return Database.get().getConnectedTokens().contains(token);
    }

    @Override
    public void setToken(String token) {
        this.token = token;
    }


    //ServerInterface methods

    @Override
    public void sendUpdate(String str) {
        clientHandler.respond(new TextResponse(str));
    }

    @Override
    public void sendException(Exception exception) {
        clientHandler.respond(new ExceptionResponse(exception));
    }

    @Override
    public void ping() {
        clientHandler.respond(new PingRequest());
    }

    @Override
    public void nofifyMap(GameMapView gameMap) {
        clientHandler.respond(new NotifyMap(gameMap));
    }



    @Override
    public void onTimer(int ms) {
        if(checkConnection(token))
            clientHandler.respond(new OnTimer(ms));
    }

    @Override
    public void onStarting(String map, GameMode gameMode) {
        if(checkConnection(token))
            clientHandler.respond(new OnStarting(map, gameMode));
    }

    @Override
    public void onDisconnection(Player player, int numOfPlayer, boolean lostTurn) {
        if(checkConnection(token))
            clientHandler.respond(new OnConnection(player, false, numOfPlayer, lostTurn));
    }

    @Override
    public void onConnection(Player player, int numOfPlayer) {
        if(checkConnection(token))
            clientHandler.respond(new OnConnection(player, true, numOfPlayer,false));
    }

    @Override
    public void onWinner(String winner, int winnerPoints, int yourPoints) {
        if(checkConnection(token))
            clientHandler.respond(new OnWinner(winner, winnerPoints, yourPoints));
    }


    //RequestHandler methods


    @Override
    public Response handle(PingResponse response) {
        TimerForDisconnection.reset(player.getToken());
        return null;
    }

    @Override
    public Response handle(RegisterRequest request) {
        String token;
        try {
            token = Database.get().login(this, false, request.username, request.password, request.color);
            player = Database.get().getUserByToken(token);
            return new RegisterResponse(token);
        } catch (InvalidLoginException e) {
            return new RegisterResponse(e.wrongUsername, e.wrongColor);
        }
    }

    @Override
    public Response handle(ReconnectRequest request) {
        if(request.username == null || request.password == null)
            return new ReconnectResponse(true);
        try {
            String tokenFromDb = Database.get().login(this, false, request.username, request.password);
            player = Database.get().getUserByToken(tokenFromDb);
            boolean isStarted = Database.get().getMainController().isGameStarted();
            return new ReconnectResponse(tokenFromDb, isStarted, new Tuple<>(GameBuilder.get().getMapName(), GameBuilder.get().getGameMode()));
        }
        catch (InvalidLoginException e) {
            return new RegisterResponse(e.wrongUsername, false);
        }
    }

    @Override
    public Response handle(CloseRequest request) {
        System.out.println("Richiesta di uscita\t" + Database.get().getUserByToken(request.token).getUsername());
        if(!checkConnection(request.token))
            return new ExceptionResponse(new InvalidTokenException());
        Database.get().logout(request.token);


        clientHandler.stop();

        /*new java.util.Timer().schedule(
                new java.util.TimerTask() {
                    @Override
                    public void run() {
                        System.out.println("5 sec");
                        clientHandler.stop();
                    }
                },
                5000
        );*/

        return new CloseResponse();
    }

    @Override
    public Response handle(MirrorRequest request) {
        if(!checkConnection(request.token)) {
            return new ExceptionResponse(new InvalidTokenException());
        }
        System.out.println("Request di mirror\t" + request.num);

        return new MirrorResponse(request.num);
    }

    @Override
    public Response handle(PollRequest request) {
        ControllerMessage msg = Database.get().getControllerByToken(token).getInstruction();

        msg = ObjectMap.get().poll(token, msg);
        return new PollResponse(msg);
    }

    @Override
    public Response handle(ModeRequest request) {
        Database.get().getMainController().modeRequest(request.normalMode);
        return null;
    }

    @Override
    public Response handle(PickRequest request) {
        return new PickResponse(ObjectMap.get().pick(token, request.choiceId, request.choices));
    }

    @Override
    public Response handle(GetMapRequest request) {
        return new GetMapResponse(Database.get().getControllerByToken(token).sendMap());
    }
}
