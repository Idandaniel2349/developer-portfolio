package com.dungeonexplorer.services.persistence;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.UUID;

@Entity
@Table(name = "game_saves")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class GameSaveEntity {
    @Id
    private UUID sessionId;

    private String playerName;
    private LocalDateTime savedAt;

    @Column(columnDefinition = "TEXT")
    private String jsonData;
}
