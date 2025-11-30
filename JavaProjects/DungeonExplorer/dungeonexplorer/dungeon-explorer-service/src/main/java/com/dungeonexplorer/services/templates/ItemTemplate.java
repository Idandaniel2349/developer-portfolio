package com.dungeonexplorer.services.templates;

import com.dungeonexplorer.models.ItemType;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.UUID;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class ItemTemplate {
    private String id;

    private String name;

    private String description;

    private ItemType type;

    private int value;
}
