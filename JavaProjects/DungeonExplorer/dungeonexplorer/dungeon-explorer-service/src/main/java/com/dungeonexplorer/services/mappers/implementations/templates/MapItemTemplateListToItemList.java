package com.dungeonexplorer.services.mappers.implementations.templates;

import com.dungeonexplorer.models.Item;
import com.dungeonexplorer.services.mappers.interfaces.templates.IMapItemTemplateListToItemList;
import com.dungeonexplorer.services.templates.ItemTemplate;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

public class MapItemTemplateListToItemList implements IMapItemTemplateListToItemList {
    @Override
    public List<Item> map(List<ItemTemplate> itemTemplateList) {
        List<Item> itemList = new ArrayList<>();
        for(ItemTemplate itemTemplate : itemTemplateList){
            Item item = new Item();
            item.setId(UUID.randomUUID());
            item.setTemplateId(itemTemplate.getId());
            item.setName(itemTemplate.getName());
            item.setDescription(itemTemplate.getDescription());
            item.setType(itemTemplate.getType());
            item.setValue(itemTemplate.getValue());

            itemList.add(item);
        }
        return itemList;
    }
}
