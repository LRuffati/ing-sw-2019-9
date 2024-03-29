package board;

import genericitems.Tuple4;
import uid.RoomUID;
import uid.TileUID;

import java.awt.*;
import java.lang.reflect.Field;
import java.util.*;
import java.util.List;

/**
 * Builds a GameMap from a file
 */
class ParserMap {

    private static int length;
    private static int width;
    private static List<TileUID> tile;
    private static List<RoomUID> roomOfTile;
    private static List<Room> room = new ArrayList<>();
    private static List<Integer> excluded = new ArrayList<>();
    private static List<Map<Direction, NeighTile>> allNeight;
    private static List<Integer> spawnPoint;

    private static boolean normalMode;

    private ParserMap(){}

    /**
     * @param path The Path of the Map file
     * @return a GameMap built upon the path file
     */
    static Tuple4<Map<RoomUID, Room>, Map<TileUID, Tile>, List<TileUID>, Coord> parseMap(boolean normalMode1, String path) {

        normalMode = normalMode1;

        String str;
        Scanner scanner;
        scanner = new Scanner(ClassLoader.getSystemResourceAsStream(path));

        while(scanner.hasNext()){
            str = scanner.nextLine();
            switch (str){
                case "Size":
                    readSize(scanner);
                    break;
                case "Excluded":
                    readExcluded(scanner);
                    break;
                case "Room":
                    readRoom(scanner);
                    break;
                case "Door":
                    readDoor(scanner);
                    break;
                case "Spawn":
                    readSpawn(scanner);
                    break;
                default:
                    break;
            }
        }
        scanner.close();

        return buildMap();
    }

    private static void readSize(Scanner scanner){
        length = scanner.nextInt();
        width = scanner.nextInt();
        tile = new ArrayList<>(length*width);
        roomOfTile = Arrays.asList(new RoomUID[length*width]);
        for(int i = 0; i<length*width; i++){
            tile.add(new TileUID());
        }
    }

    private static void readExcluded(Scanner scanner){
        excluded.add(index(scanner.nextInt(), scanner.nextInt()));
    }

    private static void readRoom(Scanner scanner){
        Scanner sLine;
        int value;
        RoomUID rUid = new RoomUID();
        Set<TileUID> tiles = new HashSet<>();
        String str = scanner.nextLine();
        while(!str.startsWith("Color")){
            sLine = new Scanner(str);
            value = index(sLine.nextInt(), sLine.nextInt());
            tiles.add(tile.get(value));
            roomOfTile.set(value, rUid);
            sLine.close();
            str = scanner.nextLine();
        }

        Color color;
        try {
            Field field = Class.forName("java.awt.Color").getField(scanner.nextLine());
            color = (Color)field.get(null);
        } catch (Exception e) {
            color = null; // Not defined
        }
        room.add(new Room(rUid, tiles, color));
    }

    private static void readDoor(Scanner scanner){
        Scanner sLine;
        allNeight = new ArrayList<>();

        for(int i=0; i<length*width; i++){
            Map<Direction, NeighTile> neight = new EnumMap<>(Direction.class);

            addElem(neight, i, -length, Direction.UP);
            addElem(neight, i, -1, Direction.LEFT);
            addElem(neight, i, +length, Direction.DOWN);
            addElem(neight, i, +1, Direction.RIGHT);

            allNeight.add(neight);
        }

        String str = scanner.nextLine();
        int[] co = new int[4];
        while(!str.startsWith(".")){
            sLine = new Scanner(str);
            co[0] = sLine.nextInt();
            co[1] = sLine.nextInt();
            co[2] = sLine.nextInt();
            co[3] = sLine.nextInt();
            sLine.close();
            str = scanner.nextLine();


            Map<Direction, NeighTile> m;
            m = allNeight.get(index(co[0],co[1]));
            m.put(getDirection(co[0],co[1],co[2],co[3]), new NeighTile(tile.get(index(co[2],co[3])),true));
            allNeight.set(index(co[0],co[1]), m);

            m = allNeight.get(index(co[2],co[3]));
            m.put(getDirection(co[2],co[3],co[0],co[1]), new NeighTile(tile.get(index(co[0],co[1])),true));
            allNeight.set(index(co[2],co[3]), m);
        }
    }

    private static void readSpawn(Scanner scanner) {
        spawnPoint = new ArrayList<>();
        Scanner sLine;
        String str = scanner.nextLine();
        while (!str.startsWith(".")) {
            sLine = new Scanner(str);
            spawnPoint.add(index(sLine.nextInt(), sLine.nextInt()));
            sLine.close();
            str = scanner.nextLine();
        }
    }



    private static void addElem(Map<Direction, NeighTile> neight, int index, int diff, Direction d){
        int val = index+diff;
        if(val>=0 && val<length*width && !excluded.contains(val) && !excluded.contains(index)) {
            if (roomOfTile.get(index) == roomOfTile.get(val)) {
                neight.put(d, new NeighTile(tile.get(val), true));
            }
            else{
                neight.put(d, new NeighTile(tile.get(val), false));
            }
        }
    }


    private static int index(int x, int y){
        return x*length + y;
    }

    private static Direction getDirection(int x1, int y1, int x2, int y2){
        if(x1==x2 && y1>y2)   return Direction.LEFT;
        if(x1==x2 && y1<y2)   return Direction.RIGHT;
        if(x1<x2 && y1==y2)   return Direction.DOWN;
        if(x1>x2 && y1==y2)   return Direction.UP;
        return null;
    }


    private static Tuple4<Map<RoomUID, Room>, Map<TileUID, Tile>, List<TileUID>, Coord> buildMap(){
        List<Tile> tileObj = new ArrayList<>();

        boolean isSpawn;
        for(int i=0; i<tile.size(); i++) {
            isSpawn = spawnPoint.contains(i);
            if(!normalMode && spawnPoint.contains(i))
                tileObj.add(new DominationPointTile(null, roomOfTile.get(i), tile.get(i), allNeight.get(i)));
            else
                tileObj.add(new Tile(null, roomOfTile.get(i), tile.get(i), allNeight.get(i), isSpawn));
        }


        Map<RoomUID, Room> roomUIDMap =  new HashMap<>();
        Map<TileUID, Tile> tileUIDMap = new HashMap<>();
        for(Room r:room){
            roomUIDMap.put(r.getRoomID(), r);
        }
        for(int i=0; i<tile.size(); i++){
            if(!excluded.contains(i))
                tileUIDMap.put(tile.get(i) , tileObj.get(i));
        }

        return new Tuple4<>(roomUIDMap, tileUIDMap, tile, new Coord(length,width));
    }
}
