package com.dungeonexplorer.models;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.HashMap;
import java.util.Map;

@Data
@AllArgsConstructor
public class Inventory {
    // item template id to Item stack
    private Map<String, ItemStack> items;

    public Inventory(){
        items = new HashMap<>();
    }

    public void addItem(Item item, int quantity) {
        if(items.containsKey(item.getTemplateId())){
            ItemStack itemStack = items.get(item.getTemplateId());
            itemStack.addToQuantity(quantity);
        }
        else{
            ItemStack itemStack = new ItemStack();
            itemStack.setItem(item);
            itemStack.setQuantity(quantity);
            items.put(item.getTemplateId(), itemStack);
        }
    }

    public void removeItem(Item item, int quantity) {
        if (items.containsKey(item.getTemplateId())) {
            String itemTemplateId = item.getTemplateId();
            int currentQuantity = items.get(itemTemplateId).getQuantity();
            if (currentQuantity <= quantity) {
                items.remove(itemTemplateId);
            } else {
                items.get(itemTemplateId).setQuantity(currentQuantity - quantity);
            }
        }
    }

    public boolean hasItem(String itemTemplateId) {
        return items.containsKey(itemTemplateId);
    }

    public int getItemQuantity(Item item){
        if(items.containsKey(item.getTemplateId())){
            return items.get(item.getTemplateId()).getQuantity();
        }
        return 0;
    }

    public Item getItem(String itemTemplateId){
        return items.get(itemTemplateId).getItem();
    }
}
