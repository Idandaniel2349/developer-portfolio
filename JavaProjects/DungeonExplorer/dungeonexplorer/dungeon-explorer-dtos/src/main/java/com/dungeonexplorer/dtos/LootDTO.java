package com.dungeonexplorer.dtos;

import lombok.Data;

@Data
public class LootDTO {
    private ItemDTO item;
    private int quantity;
}
