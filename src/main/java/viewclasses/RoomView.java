package viewclasses;

import board.Room;

import java.awt.*;

public class RoomView {
    private Room room;

    public RoomView(Room room){
        this.room = room;
    }

    public Color getColor(){
        return room.getColor();
    }
}
