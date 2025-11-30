package com.dungeonexplorer.dtos;

import lombok.Data;

@Data
public class ItemStackDTO {
    private ItemDTO item;
    private int quantity;
}
