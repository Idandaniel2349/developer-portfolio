package com.dungeonexplorer.models;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.UUID;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class Item {
    private UUID id;

    private String templateId;

    private String name;

    private String description;

    private ItemType type;

    private int value;
}
