package org.slams.server.chat.dto.response.subDto;

import lombok.Builder;
import lombok.Getter;

/**
 * Created by yunyun on 2021/12/18.
 */

@Getter
public class Creator {
    private final  Long id;
    private final String nickname;
    private final String profileImage;

    @Builder
    public Creator(
            Long id,
            String nickname,
            String profileImage){
        this.id = id;
        this.nickname = nickname;
        this.profileImage = profileImage;
    }
}
