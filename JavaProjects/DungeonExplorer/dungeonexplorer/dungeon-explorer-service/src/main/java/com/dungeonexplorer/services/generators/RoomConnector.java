package com.dungeonexplorer.services.generators;

import com.dungeonexplorer.models.Floor;
import com.dungeonexplorer.models.Room;

import java.util.*;

/**
 * Connects all rooms on a floor by creating a valid navigable graph.
 * <p>
 * Builds a backbone path from start to exit, assigns coordinates,
 * prevents room overlap, and adds extra connections.
 */
public class RoomConnector {

    private final Random random;

    public RoomConnector(Random random){
        this.random = random;
    }

    public void connectRooms(Floor floor, Map<String, Room> roomById){
        // from coordinate to room ids
        Map<Coordinate, String> coordinateRoomIdsMap = new HashMap<>();

        List<String> backboneIds = new ArrayList<>();
        String startingRoomId = floor.getStartingRoomId();;
        String exitRoomId = floor.getExitRoomId();

        backboneIds.add(startingRoomId);

        backboneIds.addAll(shuffleMiddleRooms(floor));

        backboneIds.add(exitRoomId);

        // set starting room coordinates
        Room startinRoom = roomById.get(startingRoomId);
        startinRoom.setX(0);
        startinRoom.setY(0);
        coordinateRoomIdsMap.put(new Coordinate(0,0), startingRoomId);

        // set backbone path
        for(int i=0; i< backboneIds.size() - 1; i++){
            Room currentRoom = roomById.get(backboneIds.get(i));
            // 0 - north, 1- east, 2 - south, 3 - west
            int randomNumber = random.nextInt(4);
            String direction = getDirection(randomNumber);
            String nextRoomId = backboneIds.get(i+1);
            Coordinate coordinate = getNextCoordinates(currentRoom,direction);

            while(currentRoom.getExits().get(direction)!=null || coordinateRoomIdsMap.containsKey(coordinate)){
                randomNumber = random.nextInt(4);
                direction = getDirection(randomNumber);
                coordinate = getNextCoordinates(currentRoom,direction);
            }

            coordinateRoomIdsMap.put(coordinate, nextRoomId);
            Room nextRoom = roomById.get(nextRoomId);
            nextRoom.setX(coordinate.x());
            nextRoom.setY(coordinate.y());
            currentRoom.getExits().put(direction, nextRoom.getId());
            nextRoom.getExits().put(getOppositeDirection(direction), currentRoom.getId());
        }

        // add extra directions
        addExtraDirections(floor, coordinateRoomIdsMap, roomById);
    }

    private List<String> shuffleMiddleRooms(Floor floor){
        List<String> middleRoomIds = new ArrayList<>();
        for(String roomId : floor.getRoomIds()){
            if(!Objects.equals(roomId, floor.getStartingRoomId()) && !Objects.equals(roomId, floor.getExitRoomId())){
                middleRoomIds.add(roomId);
            }
        }
        Collections.shuffle(middleRoomIds);
        return middleRoomIds;
    }

    private String getDirection(int num){
        return switch (num) {
            case 0 -> "north";
            case 1 -> "east";
            case 2 -> "south";
            case 3 -> "west";
            default -> "unknown";
        };
    }

    private String getOppositeDirection(String direction){
        return switch(direction){
            case "north" -> "south";
            case "east" -> "west";
            case "south" -> "north";
            case "west" -> "east";
            default ->  "unknown";
        };
    }

    private Coordinate getNextCoordinates(Room currentRoom, String direction){
        return switch(direction){
            case "north" -> new Coordinate(currentRoom.getX(),currentRoom.getY()+1);
            case "east" -> new Coordinate(currentRoom.getX()+1,currentRoom.getY());
            case "south" -> new Coordinate(currentRoom.getX(),currentRoom.getY()-1);
            case "west" -> new Coordinate(currentRoom.getX()-1,currentRoom.getY());
            default -> new Coordinate(0,0);
        };
    }

    private void addExtraDirections(Floor floor, Map<Coordinate, String> coordinateRoomMap, Map<String, Room> roomById){
        for(String roomId : floor.getRoomIds()){
            Room room = roomById.get(roomId);

            int x = room.getX();
            int y = room.getY();
            if(coordinateRoomMap.containsKey(new Coordinate(x, y+1)) && !room.getExits().containsKey("north")){
                String exitRoomId = coordinateRoomMap.get(new Coordinate(x, y+1));
                room.getExits().put("north", exitRoomId);

                Room exitRoom = roomById.get(exitRoomId);
                exitRoom.getExits().put("south", room.getId());
            }
            if(coordinateRoomMap.containsKey(new Coordinate(x+1, y)) && !room.getExits().containsKey("east")){
                String exitRoomId = coordinateRoomMap.get(new Coordinate(x+1, y));
                room.getExits().put("east", exitRoomId);

                Room exitRoom = roomById.get(exitRoomId);
                exitRoom.getExits().put("west", room.getId());
            }
            if(coordinateRoomMap.containsKey(new Coordinate(x, y-1)) && !room.getExits().containsKey("south")){
                String exitRoomId = coordinateRoomMap.get(new Coordinate(x, y-1));
                room.getExits().put("south", exitRoomId);

                Room exitRoom = roomById.get(exitRoomId);
                exitRoom.getExits().put("north", room.getId());
            }
            if(coordinateRoomMap.containsKey(new Coordinate(x-1, y)) && !room.getExits().containsKey("west")){
                String exitRoomId = coordinateRoomMap.get(new Coordinate(x-1, y));
                room.getExits().put("west", exitRoomId);

                Room exitRoom = roomById.get(exitRoomId);
                exitRoom.getExits().put("east", room.getId());
            }
        }
    }
}
