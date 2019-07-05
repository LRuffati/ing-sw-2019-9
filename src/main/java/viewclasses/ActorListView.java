package viewclasses;

import java.io.Serializable;
import java.util.List;

/**
 * Unused class
 */
public class ActorListView implements Serializable {

    private List<ActorView> actorView;

    public ActorListView(List<ActorView> actorView){
        this.actorView = actorView;
    }

    public List<ActorView> getActorList() {
        return actorView;
    }
}
