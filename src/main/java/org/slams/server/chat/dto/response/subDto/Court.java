package org.slams.server.chat.dto.response.subDto;

import lombok.Builder;
import lombok.Getter;

/**
 * Created by yunyun on 2021/12/18.
 */

@Getter
public class Court {
    private final Long id;
    private final String name;

    @Builder
    public Court(
            Long id,
            String name){
        this.id = id;
        this.name = name;
    }
}
