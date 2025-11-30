package com.dungeonexplorer.mappers;

import com.dungeonexplorer.dtos.*;
import com.dungeonexplorer.models.*;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.factory.Mappers;

import java.util.List;

@Mapper
public interface GameMapper {

    GameMapper instance = Mappers.getMapper(GameMapper.class);

    GameSessionDTO toDTO(GameSession gameSession);

    PlayerDTO toDTO(Player player);

    StatsDTO toDTO(Stats stats);

    InventoryDTO toDTO(Inventory inventory);

    DungeonDTO toDTO(Dungeon dungeon);
    
    FloorDTO toDTO(Floor floor);

    RoomDTO toDTO(Room room);

    EnemyDTO toDTO(Enemy enemy);

    LootDTO toDTO(Loot loot);

    ItemStackDTO toDTO(ItemStack itemStack);

    ItemDTO toDTO(Item item);

    List<EnemyDTO> enemiesToDTOList(List<Enemy> enemies);
    List<RoomDTO> roomsToDTOList(List<Room> rooms);
    List<FloorDTO> floorsToDTOList(List<Floor> floors);
}
