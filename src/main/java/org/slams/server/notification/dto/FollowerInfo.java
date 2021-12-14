package org.slams.server.notification.dto;

import lombok.Builder;
import lombok.Getter;

import static com.google.common.base.Preconditions.checkArgument;
import static org.apache.commons.lang3.StringUtils.isNotEmpty;

/**
 * Created by yunyun on 2021/12/14.
 */

@Getter
public class FollowerInfo {
    private final Long userId;
    private final String userNickname;
    private final String userImage;

    @Builder
    public FollowerInfo(
            Long userId,
            String userNickname,
            String userImage
    ){
        checkArgument(userId != null, "userId는 null을 허용하지 않습니다.");
        checkArgument(isNotEmpty(userNickname), "userNickname는 빈값을 허용하지 않습니다.");

        this.userId = userId;
        this.userNickname = userNickname;
        this.userImage = userImage;
    }
}
