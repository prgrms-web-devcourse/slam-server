package org.slams.server.favorite.dto.response;

import lombok.Builder;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import org.slams.server.common.api.BaseResponse;
import org.slams.server.favorite.entity.Favorite;

@Getter
public class FavoriteInsertResponseDto extends BaseResponse {

    private Long favoriteId;
    private Long userId;
    private Long courtId;
    private String courtName;


    //Entity -> ResponseDto
    public FavoriteInsertResponseDto(Favorite favorite) {
        super(favorite.getCreatedAt(),favorite.getUpdateAt());
        favoriteId=favorite.getId();
        courtName=favorite.getCourt().getName();
        courtId=favorite.getCourt().getId();
        userId=favorite.getUser().getId();
    }


    

}
