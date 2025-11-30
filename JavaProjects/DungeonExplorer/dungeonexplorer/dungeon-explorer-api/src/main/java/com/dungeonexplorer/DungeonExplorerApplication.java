package com.dungeonexplorer;

import com.dungeonexplorer.services.config.DungeonConfig;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.context.properties.EnableConfigurationProperties;

@SpringBootApplication
@EnableConfigurationProperties(DungeonConfig.class)
public class DungeonExplorerApplication
{
    public static void main( String[] args ) {SpringApplication.run(DungeonExplorerApplication.class, args);}
}
