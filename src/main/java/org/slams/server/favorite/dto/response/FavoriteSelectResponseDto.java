package org.slams.server.favorite.dto.response;

import lombok.Getter;
import org.slams.server.common.api.BaseResponse;
import org.slams.server.court.entity.Court;
import org.slams.server.favorite.entity.Favorite;

import java.util.Optional;

@Getter
public class FavoriteSelectResponseDto extends BaseResponse {


    private Long favoriteId;
    private Long courtId;
    private String courtName;
    private double latitude;
    private double longitude;



    public FavoriteSelectResponseDto(Favorite favorite) {
        super(favorite.getCreatedAt(),favorite.getUpdateAt());
        favoriteId=favorite.getId();
        courtId=favorite.getCourt().getId();
        courtName=favorite.getCourt().getName();
        latitude=favorite.getCourt().getLatitude();
        longitude=favorite.getCourt().getLongitude();
    }


}
