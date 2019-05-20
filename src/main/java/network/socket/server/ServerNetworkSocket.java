package network.socket.server;

import network.Database;
import network.ObjectMap;
import network.Player;
import network.ServerInterface;
import network.socket.messages.*;
import network.exception.InvalidLoginException;

import javax.xml.crypto.Data;
import java.awt.image.DataBuffer;

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
        // clientHandler.stop();
    }


    //ServerInterface methods

    @Override
    public void sendUpdate(String str) {
        clientHandler.respond(new TextResponse(str));
    }


    //RequestHandler methods

    @Override
    public Response handle(RegisterRequest request) {
        try {
            String token = Database.get().login(this, request.username, request.color);
            player = Database.get().getUserByToken(token);
        } catch (InvalidLoginException e) {
            return new TextResponse("ERROR: " + e.getMessage());
        }
        return new RegisterResponse(player.getToken());
    }

    @Override
    public Response handle(ReconnectRequest request) {
        if(request.token == null)
            return new ReconnectResponse(false);
        String tokenFromDb = Database.get().login(this, request.token);
        return new ReconnectResponse(tokenFromDb.equals(request.token));
    }

    @Override
    public Response handle(CloseRequest request) {
        System.out.println("Richiesta di uscita\t" + Database.get().getUserByToken(request.token).getUsername());
        Database.get().logout(this);
        clientHandler.stop();
        return new CloseResponse();
    }

    @Override
    public Response handle(MirrorRequest request) {
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
}
