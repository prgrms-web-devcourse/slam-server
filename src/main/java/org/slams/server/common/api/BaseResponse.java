package org.slams.server.common.api;

import lombok.AllArgsConstructor;
import lombok.Getter;

import java.time.LocalDateTime;

@Getter
@AllArgsConstructor
public abstract class BaseResponse {
    private final LocalDateTime createdAt;
    private final LocalDateTime updatedAt;
}

