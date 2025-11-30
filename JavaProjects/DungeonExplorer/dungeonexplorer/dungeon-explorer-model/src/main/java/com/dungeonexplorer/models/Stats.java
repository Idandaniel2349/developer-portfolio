package com.dungeonexplorer.models;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
public class Stats {
    private int health;

    private int attack;

    private int defense;

    public Stats(){
        health = 1;
        attack = 1;
        defense = 1;
    }

    public void addToHealth(int value){
        health += value;
    }

    public void addToAttack(int value){
        attack += value;
    }

    public void addToDefense(int value){
        defense += value;
    }

    public void decrementToHealth(int value){
        // no damage taken
        if(defense >= value){
            return;
        }

        health = Math.max(0, health - (value - defense));
    }

    public void decrementToAttack(int value){
        attack = Math.max(0, attack - value);
    }

    public void decrementToDefense(int value){
        defense = Math.max(0, defense - value);
    }
}
