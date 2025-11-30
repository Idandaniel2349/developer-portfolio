package com.dungeonexplorer.services;

import com.dungeonexplorer.models.*;
import com.dungeonexplorer.services.implementations.GameEngineService;
import com.dungeonexplorer.services.interfaces.*;
import com.dungeonexplorer.services.results.FightEnum;
import com.dungeonexplorer.services.results.FightResult;
import com.dungeonexplorer.services.results.MoveEnum;
import com.dungeonexplorer.services.results.MoveResult;
import junit.framework.Assert;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.*;

@ExtendWith(MockitoExtension.class)
public class GameEngineServiceTest {

    @Mock
    private IMovementService movementService;
    @Mock
    private ICombatService combatService;
    @Mock
    private ILootService lootService;
    @Mock
    private IInventoryService inventoryService;
    @Mock
    private IDungeonService dungeonService;

    @InjectMocks
    private GameEngineService gameEngineService;

    @Test
    void MovePlayerTest_moved() {
        Player player = new Player();
        Room room = new Room();
        Item item = new Item(UUID.randomUUID(), "health_potion","Health Potion", "Potion that heals", ItemType.HEALING, 1);
        Loot loot = new Loot(UUID.randomUUID(),item, 1, 1);
        List<Loot> loots = new ArrayList<>();
        loots.add(loot);
        room.setLoot(loots);
        room.setId("room0");
        room.setExitRoom(false);
        player.setCurrentFloorNumber(1);
        player.setCurrentRoomId(room.getId());

        Dungeon dungeon = new Dungeon();
        Map<String, Room> roomMap = new HashMap<>();
        roomMap.put("room0", room);
        dungeon.setRoomById(roomMap);

        Floor floor = new Floor();
        floor.setFloorNumber(1);
        List<String> roomIds = new ArrayList<>();
        roomIds.add(room.getId());
        floor.setRoomIds(roomIds);
        List<Floor> floors = new ArrayList<>();
        floors.add(floor);
        dungeon.setFloors(floors);

        Mockito.when(movementService.movePlayer(player,"north", dungeon)).thenReturn(true);
        Mockito.when(lootService.generateLoot(player, room.getLoot())).thenReturn(loots);
        Mockito.doNothing().when(inventoryService).addItem(player,item,1);
        Mockito.when(dungeonService.getFloorByNumber(0, dungeon)).thenReturn(floor);
        Mockito.when(dungeonService.getRoomById("room0", dungeon)).thenReturn(room);

        MoveResult result = gameEngineService.movePlayer(player, "north", dungeon);

        Assert.assertEquals(MoveEnum.MOVED, result.getMoveEnum());
        Assert.assertEquals(1, result.getLootReceived().size());
        Assert.assertTrue(room.isVisited());
        Mockito.verify(movementService).movePlayer(player, "north", dungeon);
        Mockito.verify(lootService).generateLoot(player, room.getLoot());
        Mockito.verify(inventoryService).addItem(player,item,1);
    }

    @Test
    void MovePlayerTest_gameWon() {
        Player player = new Player();
        Room room = new Room();
        Item item = new Item(UUID.randomUUID(), "health_potion","Health Potion", "Potion that heals", ItemType.HEALING, 1);
        Loot loot = new Loot(UUID.randomUUID(),item, 1, 1);
        List<Loot> loots = new ArrayList<>();
        loots.add(loot);
        room.setLoot(loots);
        room.setId("room0");
        room.setExitRoom(true);
        player.setCurrentRoomId(room.getId());

        Dungeon dungeon = new Dungeon();
        Map<String, Room> roomMap = new HashMap<>();
        roomMap.put("room0", room);
        dungeon.setRoomById(roomMap);

        List<String> rooms = new ArrayList<>();
        rooms.add(room.getId());

        Floor floor1 = new Floor(1, 1,room.getId(), room.getId(),rooms, true);
        player.setCurrentFloorNumber(floor1.getFloorNumber());
        List<Floor> floors = new ArrayList<>();
        floors.add(floor1);
        dungeon.setFloors(floors);

        Mockito.when(dungeonService.getFloorByNumber(0, dungeon)).thenReturn(floor1);
        Mockito.when(dungeonService.getRoomById("room0", dungeon)).thenReturn(room);

        MoveResult result = gameEngineService.movePlayer(player, "exit", dungeon);

        Assert.assertEquals(MoveEnum.GAME_WON, result.getMoveEnum());
        Assert.assertEquals(0, result.getLootReceived().size());
        Assert.assertNotNull(result.getLootReceived());
    }

    @Test
    void MovePlayerTest_floorTransition() {
        Player player = new Player();
        Room room = new Room();
        Item item = new Item(UUID.randomUUID(), "health_potion","Health Potion", "Potion that heals", ItemType.HEALING, 1);
        Loot loot = new Loot(UUID.randomUUID(),item, 1, 1);
        List<Loot> loots = new ArrayList<>();
        loots.add(loot);
        room.setLoot(loots);
        room.setId("room0");
        room.setExitRoom(true);
        player.setCurrentRoomId(room.getId());

        Dungeon dungeon = new Dungeon();
        Map<String, Room> roomMap = new HashMap<>();
        roomMap.put("room0", room);
        dungeon.setRoomById(roomMap);

        List<String> rooms = new ArrayList<>();
        rooms.add(room.getId());

        Floor floor1 = new Floor(1,1,room.getId(), room.getId(), rooms, true);
        player.setCurrentFloorNumber(floor1.getFloorNumber());
        Floor floor2 = new Floor(2,1,room.getId(), room.getId(), rooms, false);
        List<Floor> floors = new ArrayList<>();
        floors.add(floor1);
        floors.add(floor2);
        dungeon.setFloors(floors);

        Mockito.when(lootService.generateLoot(player, room.getLoot())).thenReturn(loots);
        Mockito.doNothing().when(inventoryService).addItem(player,item,1);
        Mockito.when(dungeonService.getFloorByNumber(0, dungeon)).thenReturn(floor1);
        Mockito.when(dungeonService.getRoomById("room0", dungeon)).thenReturn(room);
        Mockito.when(dungeonService.getStartingRoom(1, dungeon)).thenReturn(room);

        MoveResult result = gameEngineService.movePlayer(player, "exit", dungeon);

        Assert.assertEquals(MoveEnum.FLOOR_TRANSITION, result.getMoveEnum());
        Assert.assertEquals(1, result.getLootReceived().size());

        Room currentRoom = dungeon.getRoomById().get(player.getCurrentRoomId());
        Assert.assertTrue(currentRoom.isVisited());
        Mockito.verify(lootService).generateLoot(player, room.getLoot());
        Mockito.verify(inventoryService).addItem(player,item,1);
    }

    @Test
    void MovePlayerTest_invalidMove() {
        Player player = new Player();
        player.setCurrentFloorNumber(1);

        Room room = new Room("room0", "Room0", "");
        room.setExitRoom(false);

        player.setCurrentRoomId(room.getId());

        Dungeon dungeon = new Dungeon();

        Floor floor = new Floor();

        floor.setBossDefeated(false);

        List<String> roomsIds = new ArrayList<>();
        roomsIds.add(room.getId());
        floor.setRoomIds(roomsIds);

        List<Floor> floors = new ArrayList<>();
        floors.add(floor);

        dungeon.setFloors(floors);

        Map<String, Room> roomMap = new HashMap<>();
        roomMap.put(room.getId(), room);
        dungeon.setRoomById(roomMap);

        Mockito.when(movementService.movePlayer(player, "north", dungeon)).thenReturn(false);
        Mockito.when(dungeonService.getFloorByNumber(0, dungeon)).thenReturn(floor);
        Mockito.when(dungeonService.getRoomById("room0", dungeon)).thenReturn(room);

        MoveResult result = gameEngineService.movePlayer(player, "north", dungeon);

        Assert.assertEquals(MoveEnum.INVALID_MOVE, result.getMoveEnum());
        Assert.assertEquals(0, result.getLootReceived().size());
        Mockito.verifyNoInteractions(lootService);
        Mockito.verifyNoInteractions(inventoryService);
    }

    @Test
    void MovePlayerTest_floorBossNotDefeated(){
        Player player = new Player();
        player.setCurrentFloorNumber(1);

        Room room = new Room("room0", "Room0", "");
        room.setExitRoom(true);

        player.setCurrentRoomId(room.getId());

        Dungeon dungeon = new Dungeon();

        Floor floor = new Floor();

        floor.setBossDefeated(false);

        List<String> roomsIds = new ArrayList<>();
        roomsIds.add(room.getId());
        floor.setRoomIds(roomsIds);

        List<Floor> floors = new ArrayList<>();
        floors.add(floor);

        dungeon.setFloors(floors);

        Map<String, Room> roomMap = new HashMap<>();
        roomMap.put(room.getId(), room);
        dungeon.setRoomById(roomMap);

        Mockito.when(dungeonService.getFloorByNumber(0, dungeon)).thenReturn(floor);
        Mockito.when(dungeonService.getRoomById("room0", dungeon)).thenReturn(room);

        MoveResult result = gameEngineService.movePlayer(player, "exit", dungeon);

        Assert.assertEquals(MoveEnum.FLOOR_BOSS_NOT_DEFEATED, result.getMoveEnum());
        Assert.assertEquals(0, result.getLootReceived().size());
        Mockito.verifyNoInteractions(lootService);
        Mockito.verifyNoInteractions(inventoryService);
    }

    @Test
    public void fightEnemy_playerDefeated(){
        Player player = Mockito.mock(Player.class);
        Enemy enemy = new Enemy();
        enemy.setId(UUID.randomUUID());
        enemy.setTemplateId("goblin");
        enemy.setName("Goblin");
        enemy.setDefeated(false);

        Stats stats = new Stats();
        stats.setHealth(0);

        Room room = new Room();
        room.setId("floor1_room1");
        List<Enemy> enemies = new ArrayList<>();
        enemies.add(enemy);
        room.setEnemies(enemies);

        Map<String, Room> roomById = new HashMap<>();
        roomById.put("floor1_room1", room);

        Dungeon dungeon = new Dungeon();
        dungeon.setRoomById(roomById);

        Mockito.when(player.getStats()).thenReturn(stats);

        Mockito.doNothing().when(combatService).fight(player,enemy);

        FightResult result = gameEngineService.fightEnemy(player,enemy, dungeon);

        Assert.assertEquals(FightEnum.PLAYER_DEFEATED, result.getFightEnum());
        Assert.assertEquals(0, result.getLootReceived().size());
        Mockito.verify(combatService).fight(player, enemy);
        Mockito.verifyNoInteractions(lootService);
        Mockito.verifyNoInteractions(inventoryService);
    }

    @Test
    void fightEnemy_enemyNotDefeated(){
        Player player = new Player("Player 1");

        Enemy enemy = new Enemy();
        enemy.setId(UUID.randomUUID());
        enemy.setTemplateId("goblin");
        enemy.setName("Goblin");
        enemy.setDefeated(false);


        Room room = new Room();
        room.setId("floor1_room1");
        List<Enemy> enemies = new ArrayList<>();
        enemies.add(enemy);
        room.setEnemies(enemies);

        Map<String, Room> roomById = new HashMap<>();
        roomById.put("floor1_room1", room);

        Dungeon dungeon = new Dungeon();
        dungeon.setRoomById(roomById);

        player.setCurrentRoomId(room.getId());

        Mockito.doNothing().when(combatService).fight(player,enemy);

        FightResult result = gameEngineService.fightEnemy(player,enemy, dungeon);

        Assert.assertEquals(FightEnum.ONGOING, result.getFightEnum());
        Assert.assertEquals(0, result.getLootReceived().size());
        Mockito.verify(combatService).fight(player, enemy);
        Mockito.verifyNoInteractions(lootService);
        Mockito.verifyNoInteractions(inventoryService);
        Assert.assertEquals(0, player.getXp());

    }

    @Test
    void fightEnemy_enemyDefeated(){
        Player player = new Player("Player 1");
        Enemy enemy = new Enemy();
        enemy.setId(UUID.randomUUID());
        enemy.setTemplateId("goblin");
        enemy.setName("Goblin");
        enemy.setXp(4);
        enemy.setDefeated(true);

        Item item = new Item(UUID.randomUUID(), "health_potion","Health Potion", "Potion that heals", ItemType.HEALING, 1);
        Loot loot = new Loot(UUID.randomUUID(),item, 1, 1);
        List<Loot> loots = new ArrayList<>();
        loots.add(loot);

        enemy.setLoot(loots);
        Room room = new Room("floor1_room1", "Room1", "A dungeon room");
        List<Enemy> enemies = new ArrayList<>();
        enemies.add(enemy);
        room.setEnemies(enemies);
        player.setCurrentRoomId(room.getId());

        Map<String, Room> roomById = new HashMap<>();
        roomById.put("floor1_room1", room);

        Dungeon dungeon = new Dungeon();
        dungeon.setRoomById(roomById);

        Floor floor = new Floor();
        List<String> roomIds = new ArrayList<>();
        roomIds.add(room.getId());
        floor.setRoomIds(roomIds);
        List<Floor> floors = new ArrayList<>();
        floors.add(floor);
        dungeon.setFloors(floors);

        Mockito.doNothing().when(combatService).fight(player,enemy);
        Mockito.when(lootService.generateLoot(player, enemy.getLoot())).thenReturn(loots);
        Mockito.when(dungeonService.getRoomById("floor1_room1", dungeon)).thenReturn(room);

        FightResult result = gameEngineService.fightEnemy(player,enemy, dungeon);

        Assert.assertEquals(FightEnum.ENEMY_DEFEATED, result.getFightEnum());
        Assert.assertEquals(loots, result.getLootReceived());
        Mockito.verify(combatService).fight(player, enemy);
        Mockito.verify(lootService).generateLoot(player, loots);
        Mockito.verify(inventoryService).addItem(player, loot.getItem(), loot.getQuantity());
        Assert.assertEquals(4, player.getXp());
        Assert.assertEquals(1, player.getLevel());
    }

    @Test
    void fightEnemy_floorBossDefeated(){
        Player player = new Player("Player 1");
        player.setCurrentFloorNumber(1);
        Enemy enemy = new Enemy();
        enemy.setId(UUID.randomUUID());
        enemy.setTemplateId("dragon");
        enemy.setName("Dragon");
        enemy.setXp(3);
        enemy.setBoss(true);
        enemy.setDefeated(true);

        Item item = new Item(UUID.randomUUID(), "health_potion","Health Potion", "Potion that heals", ItemType.HEALING, 1);
        Loot loot = new Loot(UUID.randomUUID(),item, 1, 1);
        List<Loot> loots = new ArrayList<>();
        loots.add(loot);

        enemy.setLoot(loots);
        Room room = new Room("floor1_room1", "Room1", "A dungeon room");
        room.setBossRoom(true);
        List<Enemy> enemies = new ArrayList<>();
        enemies.add(enemy);
        room.setEnemies(enemies);
        player.setCurrentRoomId(room.getId());

        Map<String, Room> roomById = new HashMap<>();
        roomById.put("floor1_room1", room);

        Dungeon dungeon = new Dungeon();
        dungeon.setRoomById(roomById);

        Floor floor = new Floor();
        floor.setFloorNumber(1);
        floor.setNumberOfRooms(1);
        floor.setBossDefeated(false);
        List<String> roomIds = new ArrayList<>();
        roomIds.add(room.getId());
        floor.setRoomIds(roomIds);
        List<Floor> floors = new ArrayList<>();
        floors.add(floor);
        dungeon.setFloors(floors);

        Mockito.doNothing().when(combatService).fight(player,enemy);
        Mockito.when(lootService.generateLoot(player, enemy.getLoot())).thenReturn(loots);
        Mockito.when(dungeonService.getFloorByNumber(0, dungeon)).thenReturn(floor);
        Mockito.when(dungeonService.getRoomById("floor1_room1", dungeon)).thenReturn(room);

        FightResult result = gameEngineService.fightEnemy(player,enemy, dungeon);

        Assert.assertEquals(FightEnum.ENEMY_DEFEATED, result.getFightEnum());
        Assert.assertEquals(loots, result.getLootReceived());
        Assert.assertTrue(floor.isBossDefeated());
        Mockito.verify(combatService).fight(player, enemy);
        Mockito.verify(lootService).generateLoot(player, loots);
        Mockito.verify(inventoryService).addItem(player, loot.getItem(), loot.getQuantity());


    }


}
