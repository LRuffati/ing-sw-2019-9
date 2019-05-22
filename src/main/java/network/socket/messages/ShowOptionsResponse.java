package network.socket.messages;

import actions.ActionTemplate;
import actions.targeters.targets.Targetable;
import genericitems.Tuple;
import genericitems.Tuple3;
import viewclasses.TargetView;
import viewclasses.WeaponView;

import java.util.List;

public class ShowOptionsResponse implements Response {

    public final int type;

    public final Tuple3<
            Tuple<Boolean, List<TargetView>>,
            List<WeaponView>,
            Tuple<Boolean, List<ActionTemplate>>
            > result;
    //todo: ActionTemplateView?

    public ShowOptionsResponse(int type,
                               Tuple<Boolean,List<TargetView>> target,
                               List<WeaponView> weapon,
                               Tuple<Boolean,List<ActionTemplate>> action){
        result = new Tuple3<>(target, weapon, action);
        this.type = type;
    }

    @Override
    public void handle(ResponseHandler handler) {
        handler.handle(this);
    }
}
