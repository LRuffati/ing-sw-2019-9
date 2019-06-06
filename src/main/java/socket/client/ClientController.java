package socket.client;

import board.Coord;
import socket.commands.*;
import viewclasses.ActorView;

import java.util.stream.StreamSupport;


/**
 * CLIENT-SIDE controller
 *
 * It holds a reference to the view for sending sudden responses.
 * It holds a reference to the networking layer.
 */
public class ClientController implements ResponseHandler {

    private final Client client;
    private Thread receiver;

    //TODO: reference to View

    public ClientController(Client client){
        this.client = client;
        //this.view = new view()
    }


    public void startMessaging(){
        // start a receiver thread
        receiver = new Thread(
                () -> {
                    Response response;
                    do {
                        response = client.nextResponse();
                        if (response != null) {
                            response.handle(this);
                        }
                    } while (response != null);
                }
        );
        receiver.start();
    }

    public void run() {
        client.request(new MapRequest());
        client.nextResponse().handle(this);

        client.request(new Request1("Messaggio"));
        client.nextResponse().handle(this);
        client.request(new Request1("Messaggio"));
        client.nextResponse().handle(this);
        client.request(new Request1("Messaggio"));
        client.nextResponse().handle(this);
        client.request(new Request2());
        client.nextResponse().handle(this);
    }

    @Override
    public void handle(Response1 response) {
        //System.out.println(response.message());
    }

    @Override
    public void handle(Response2 response) {
        System.out.println("Sono uscito");
    }

    @Override
    public void handle(MapResponse response) {
        System.out.println(response.grabbableUID.equals(response.gameMapView.getPosition(new Coord(1,1)).ammoCard().uid()));
    }
}
