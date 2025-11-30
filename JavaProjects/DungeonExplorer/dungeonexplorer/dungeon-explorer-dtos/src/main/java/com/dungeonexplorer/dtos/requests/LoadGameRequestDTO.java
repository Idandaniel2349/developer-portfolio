package com.dungeonexplorer.dtos.requests;

import lombok.Data;

import java.util.UUID;

@Data
public class LoadGameRequestDTO {
    UUID sessionId;
}
