package viewclasses;

import java.io.Serializable;
import java.util.List;

public class TileListView implements TargetView, Serializable {

    private List<TileView> tileList;

    public TileListView(List<TileView> tileList){
        this.tileList = tileList;
    }

    public List<TileView> getTileList() {
        return tileList;
    }
}
