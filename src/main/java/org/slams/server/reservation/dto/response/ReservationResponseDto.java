package org.slams.server.reservation.dto.response;


import lombok.Builder;
import lombok.Getter;
import org.slams.server.common.api.BaseResponse;
import org.slams.server.follow.entity.Follow;
import org.slams.server.user.entity.User;

import javax.swing.text.html.Option;
import java.util.Optional;

@Getter
public class ReservationResponseDto extends BaseResponse {
    private Long followId;
    private Long userId;
    private String nickname;
    private Boolean isFollowed;
    private String profileImage;


    // follower -> 내가 한것
    // following -> 당하는 사람
    public ReservationResponseDto(User joinUser, Boolean isFollowed, Follow following){
        super(joinUser.getCreatedAt(), joinUser.getUpdateAt());
        userId=joinUser.getId();
        nickname=joinUser.getNickname();
        profileImage=joinUser.getProfileImage();
        followId=following.getId();
        this.isFollowed=isFollowed;
    }

    public ReservationResponseDto(User joinUser, Boolean isFollowed){
        super(joinUser.getCreatedAt(), joinUser.getUpdateAt());
        userId=joinUser.getId();
        nickname=joinUser.getNickname();
        profileImage=joinUser.getProfileImage();
        followId=null;
        this.isFollowed=isFollowed;
    }


}
