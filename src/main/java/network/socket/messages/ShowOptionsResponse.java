package network.socket.messages;

import genericitems.Tuple;
import genericitems.Tuple3;
import viewclasses.ActionView;
import viewclasses.TargetView;
import viewclasses.WeaponView;

import java.util.List;

public class ShowOptionsResponse implements Response {

    public final int type;

    public final Tuple3<
            Tuple<Boolean, List<TargetView>>,
            List<WeaponView>,
            Tuple<Boolean, List<ActionView>>
            > result;

    public ShowOptionsResponse(int type,
                               Tuple<Boolean,List<TargetView>> target,
                               List<WeaponView> weapon,
                               Tuple<Boolean,List<ActionView>> action){
        result = new Tuple3<>(target, weapon, action);
        this.type = type;
    }

    @Override
    public void handle(ResponseHandler handler) {
        handler.handle(this);
    }
}
