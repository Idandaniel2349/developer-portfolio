package com.dungeonexplorer.dtos;

import com.dungeonexplorer.models.ItemType;
import lombok.Data;

import java.util.UUID;

@Data
public class ItemDTO {
    private UUID id;
    private String templateId;
    private String name;
    private String description;
    private ItemType type;
    private int value;
}
