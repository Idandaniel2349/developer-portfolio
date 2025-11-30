package com.dungeonexplorer.dtos;

import lombok.Data;

import java.util.Map;

@Data
public class InventoryDTO {
    private Map<String, ItemStackDTO> items;
}
