package com.dungeonexplorer.services.templates;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Registry responsible for loading and providing access to all predefined templates:
 * Each template is stored in a map keyed by its template ID for quick lookup.
 * This registry is used by the generators.
 */
@Service
public class TemplateRegistry {

    private final Map<String, EnemyTemplate> enemyTemplateMap;
    private final Map<String, ItemTemplate> itemTemplateMap;
    private final Map<String, EnemyTemplate> bossTemplateMap;

    public TemplateRegistry() throws IOException {
        List<ItemTemplate> itemsTemplates;
        List<EnemyTemplate> enemyTemplates;
        List<EnemyTemplate> bossTemplates;
        enemyTemplateMap = new HashMap<>();
        itemTemplateMap = new HashMap<>();
        bossTemplateMap = new HashMap<>();

        ObjectMapper mapper = new ObjectMapper();

        // handle item templates
        try(InputStream is = getClass().getClassLoader().getResourceAsStream("templates/items.json")){
            itemsTemplates = mapper.readValue(is, new TypeReference<List<ItemTemplate>>() {});
        }catch (IOException e){
            throw new RuntimeException("Failed to load items", e);
        }

        for(ItemTemplate item : itemsTemplates){
            itemTemplateMap.put(item.getId(), item);
        }


        // handle enemy templates
        try(InputStream is = getClass().getClassLoader().getResourceAsStream("templates/enemies.json")){
            enemyTemplates = mapper.readValue(is, new TypeReference<List<EnemyTemplate>>() {});
        }catch (IOException e){
            throw new RuntimeException("Failed to load enemies", e);
        }

        for(EnemyTemplate enemy : enemyTemplates){
            enemyTemplateMap.put(enemy.getId(), enemy);
        }

        // handle boss templates
        try(InputStream is = getClass().getClassLoader().getResourceAsStream("templates/bosses.json")){
            bossTemplates = mapper.readValue(is, new TypeReference<List<EnemyTemplate>>() {});
        }catch (IOException e){
            throw new RuntimeException("Failed to load bosses", e);
        }

        for(EnemyTemplate boss : bossTemplates){
            bossTemplateMap.put(boss.getId(), boss);
        }
    }

    public EnemyTemplate getEnemyById(String id){
        return enemyTemplateMap.get(id);
    }

    public ItemTemplate getItemById(String id){
        return itemTemplateMap.get(id);
    }

    public EnemyTemplate getBossById(String id){
        return bossTemplateMap.get(id);
    }

    public List<EnemyTemplate> getEnemieslist(){
        return new ArrayList<>(enemyTemplateMap.values());
    }

    public List<ItemTemplate> getItemsList(){
        return new ArrayList<>(itemTemplateMap.values());
    }

    public List<EnemyTemplate> getBosseslist(){
        return new ArrayList<>(bossTemplateMap.values());
    }
}
