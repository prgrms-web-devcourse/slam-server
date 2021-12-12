package org.slams.server.notification.dto;

import com.fasterxml.jackson.databind.PropertyNamingStrategy;
import com.fasterxml.jackson.databind.annotation.JsonNaming;
import lombok.Getter;

/**
 * Created by yunyun on 2021/12/09.
 */

@Getter
@JsonNaming(PropertyNamingStrategy.class)
public class CursorRequest {

    private final int size;
    private final Long lastId;
    private final boolean isFirst;

    public CursorRequest(int size, Long lastId, boolean isFirst){
        this.size = size;
        this.lastId = lastId;
        this.isFirst = isFirst;
    }

}
