package com.dungeonexplorer.models;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Map;
import java.util.UUID;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class Player {
    private UUID id;

    private String name;

    private Stats stats;

    private int level = 1;

    private int xp = 0;

    private static final int BASE_XP = 5;

    private Inventory inventory;

    private int currentFloorNumber;

    private String currentRoomId;

    public Player( String name){
        this.id = UUID.randomUUID();
        this.name = name;
        stats = new Stats();
        inventory = new Inventory();
    }

    public void addXp(int amount){
        xp += amount;
        checkLevelUp();
    }

    public void checkLevelUp(){
        int xpForNextLevel = level * BASE_XP;
        while( xp >= xpForNextLevel){
            xp -= xpForNextLevel;
            level++;
            levelUpStats();
            xpForNextLevel = level * BASE_XP;
        }
    }

    public void levelUpStats(){
        stats.addToHealth(2);
        stats.addToAttack(1);
        stats.addToDefense(1);
    }
}
