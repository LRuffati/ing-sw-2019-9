package board;

import genericItems.Tuple;

/**
 * The absolute coordinate of a Tile.
 * (0,0) is the top left Tile
 */
public class Coord {
    private Tuple<Integer, Integer> pos;
    public Coord(int x, int y){
         pos = new Tuple<>(x,y);
    }

    /**
     *
     * @return The x coordinate of the tile
     */
    protected Integer getX(){
        return pos.x;
    }
    /**
     *
     * @return The y coordinate of the tile
     */
    protected Integer getY(){
        return pos.y;
    }
}
