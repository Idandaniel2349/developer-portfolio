package com.dungeonexplorer.models;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class ItemStack {
    private Item item;
    private int quantity;

    public void addToQuantity(int quantityToAdd){
        quantity += quantityToAdd;
    }
}
