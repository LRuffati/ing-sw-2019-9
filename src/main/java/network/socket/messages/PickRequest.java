package network.socket.messages;

import java.util.List;

public class PickRequest  implements Request{

    public final int type;
    public final String chooserId;
    public final int[] choice;

    public PickRequest(int type, String chooserId, List<Integer> choice){
        this.type = type;
        this.chooserId = chooserId;
        this.choice = choice.stream().mapToInt(Integer::intValue).toArray();
    }

    @Override
    public Response handle(RequestHandler handler) {
        return handler.handle(this);
    }
}
