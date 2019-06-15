package network.socket.server;

import network.*;
import network.exception.InvalidTokenException;
import network.socket.messages.*;
import network.exception.InvalidLoginException;
import network.socket.messages.notify.*;
import testcontroller.controllerclient.ControllerMessageClient;
import testcontroller.controllermessage.ControllerMessage;
import testcontroller.controllerstates.SlaveControllerState;
import viewclasses.GameMapView;


/**
 * This class handles all the methods called by the Server(implemented in ServerInterface)
 * and the Requests called by the clients (implemented in RequestHandler)
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
    public void onStarting(String map) {
        if(checkConnection(token))
            clientHandler.respond(new OnStarting(map));
    }

    @Override
    public void onDisconnection(Player player) {
        if(checkConnection(token))
            clientHandler.respond(new OnConnection(player, false));
    }

    @Override
    public void onConnection(Player player) {
        if(checkConnection(token))
            clientHandler.respond(new OnConnection(player, true));
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
            token = Database.get().login(this, request.username, request.password, request.color);
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
            String tokenFromDb = Database.get().login(this, request.username, request.password);
            player = Database.get().getUserByToken(tokenFromDb);
            return new ReconnectResponse(tokenFromDb);
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
        //TODO: notify controller
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
        if(!msg.type().equals(SlaveControllerState.WAIT))
            msg = ObjectMap.get().init(msg);
        else
            msg = new ControllerMessageClient(msg, null);
        return new PollResponse(msg);
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
