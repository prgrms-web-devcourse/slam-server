package org.slams.server.chat.dto.response;

import lombok.Builder;
import lombok.Getter;

/**
 * Created by yunyun on 2021/12/18.
 */

@Getter
public class DeleteUserChatRoomResponse {

    private final Long courtId;

    @Builder
    public DeleteUserChatRoomResponse(Long courtId){
        this.courtId = courtId;
    }
}
