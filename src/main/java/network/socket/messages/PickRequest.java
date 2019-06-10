package network.socket.messages;

import java.util.ArrayList;
import java.util.List;

public class PickRequest implements Request{

    public final String choiceId;
    public final List<Integer> choices;

    public PickRequest(String chooserId, List<Integer> choice){
        this.choiceId = chooserId;
        this.choices = new ArrayList<>(choice);
    }

    @Override
    public Response handle(RequestHandler handler) {
        return handler.handle(this);
    }
}
