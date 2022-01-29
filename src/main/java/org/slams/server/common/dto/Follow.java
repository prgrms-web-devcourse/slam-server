package org.slams.server.common.dto;

/**
 * Created by yunyun on 2022/01/24.
 */

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.Builder;

@JsonInclude(JsonInclude.Include.NON_NULL)
public class Follow extends BaseDto{
    private User creator;
    private User receiver;

    @Builder
    public Follow(
            User creator,
            User receiver
    ){
        this.creator = creator;
        this.receiver = receiver;
    }

}
