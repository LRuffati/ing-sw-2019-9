package network.socket.messages;

import java.util.List;

public class PickRequest  implements Request{

    public final String choiceId;
    public final List<Integer> choices;

    public PickRequest(int type, String chooserId, List<Integer> choice){
        this.choiceId = chooserId;
        this.choices = choice;
    }

    @Override
    public Response handle(RequestHandler handler) {
        return handler.handle(this);
    }
}
