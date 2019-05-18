package network.socket.server;

import network.Database;
import network.Player;
import network.ServerInterface;
import network.socket.messages.*;
import rmi.exceptions.InvalidLoginException;

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
    public Response handle(CloseRequest request) {
        System.out.println("Richiesta di uscita\t" + request.value);
        Database.get().logout(this);
        clientHandler.stop();
        return new CloseResponse();
    }

    @Override
    public Response handle(MirrorRequest request) {
        System.out.println("Request di mirror\t" + request.num);
        return new MirrorResponse(request.num);
    }
}
