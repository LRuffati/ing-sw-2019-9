package board;

import genericitems.Tuple;

/**
 * The absolute coordinate of a Tile.
 * (0,0) is the top left Tile
 */
public class Coord {
    private Tuple<Integer, Integer> pos;
    public Coord(int down, int left){
         pos = new Tuple<>(down, left);
    }

    /**
     *
     * @return The x coordinate of the tile
     */
    public Integer getX(){
        return pos.x;
    }
    /**
     *
     * @return The y coordinate of the tile
     */
    public Integer getY(){
        return pos.y;
    }
}
