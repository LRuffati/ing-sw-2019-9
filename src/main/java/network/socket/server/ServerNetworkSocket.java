package network.socket.server;

import network.*;
import network.exception.InvalidTokenException;
import network.socket.messages.*;
import network.exception.InvalidLoginException;
import viewclasses.GameMapView;

import java.rmi.RemoteException;


/**
 * This class handles all the methods called by the Server(implemented in ServerInterface)
 * and the Requests called by the clients (implemented in RequestHandler)
 */
public class ServerNetworkSocket implements RequestHandler, ServerInterface {

    private final ClientHandler clientHandler;
    private Player player;

    public ServerNetworkSocket(ClientHandler clientHandler){
        this.clientHandler = clientHandler;
        //TODO: stop command
    }


    private boolean checkConnection(String token) {
        return Database.get().getConnectedTokens().contains(token);
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
        Database.get().quit(request.token);


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
    public Response handle(PickRequest request) {
        String choosedId = request.chooserId;
        int[] choice = request.choice;
        if(choice.length < 1) {
            //todo: return error
        }
        switch (request.type){
            case 0:
                return new PickResponse(0, ObjectMap.get().pickTarg(choosedId, choice[0]));
            case 1:
                return new PickResponse(1, ObjectMap.get().pickWeapon(choosedId, choice));
            case 2:
                return new PickResponse(2, ObjectMap.get().pickAction(choosedId, choice[0]));

                default:
                    //todo: return error
        }
        return new PickResponse(-1, null);
    }

    @Override
    public Response handle(ShowOptionsRequest request) {
        if(!checkConnection(request.token))
            return new ExceptionResponse(new InvalidTokenException());
        String choosedId = request.chooserId;
        switch (request.type){
            case 0:
                return new ShowOptionsResponse(0, ObjectMap.get().showOptionsTarget(choosedId), null, null);
            case 1:
                return new ShowOptionsResponse(1, null, ObjectMap.get().showOptionsWeapon(choosedId), null);
            case 2:
                return new ShowOptionsResponse(2, null, null, ObjectMap.get().showOptionsAction(choosedId));

                default:
                    //todo: return error
        }
        return new ShowOptionsResponse(-1,null,null,null);
    }


    @Override
    public Response handle(GetMapRequest request) {
        return new GetMapResponse(ObjectMap.get().showGameMap(request.gameMapId));
    }
}
