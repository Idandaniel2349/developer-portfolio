package com.dungeonexplorer.dtos.requests;

import lombok.Data;

import java.util.UUID;

@Data
public class UseItemRequestDTO {
    UUID sessionId;
    String itemTemplateId;
}
