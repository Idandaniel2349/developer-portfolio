package com.dungeonexplorer.services.implementations;

import com.dungeonexplorer.models.Item;
import com.dungeonexplorer.models.ItemType;
import com.dungeonexplorer.models.Player;
import com.dungeonexplorer.services.interfaces.IInventoryService;
import org.springframework.stereotype.Service;

@Service
public class InventoryService implements IInventoryService {

    @Override
    public void addItem(Player player, Item item, int quantity) {
        player.getInventory().addItem(item, quantity);
    }

    @Override
    public void removeItem(Player player, Item item, int quantity) {
        player.getInventory().removeItem(item, quantity);
    }

    @Override
    public void useItem(Player player, String templateId) {
        if(player.getInventory().hasItem(templateId)){
            Item item = player.getInventory().getItem(templateId);

            switch(item.getType()){
                case HEALING:
                    player.getStats().addToHealth(item.getValue());
                    break;
                case ATTACK:
                    player.getStats().addToAttack(item.getValue());
                    break;
                case DEFENSE:
                    player.getStats().addToDefense(item.getValue());
                    break;
                default:
                    break;
            }

            player.getInventory().removeItem(item, 1);
        }

    }
}
