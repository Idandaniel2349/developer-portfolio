package com.dungeonexplorer.services.mappers.interfaces.templates;

import com.dungeonexplorer.models.Item;
import com.dungeonexplorer.services.templates.ItemTemplate;

import java.util.List;

public interface IMapItemTemplateListToItemList {
    List<Item> map(List<ItemTemplate> itemTemplateList);
}
