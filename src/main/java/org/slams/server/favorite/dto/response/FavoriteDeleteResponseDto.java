package org.slams.server.favorite.dto.response;

import lombok.Getter;

@Getter
public class FavoriteDeleteResponseDto {

    private Long favoriteId;


    public FavoriteDeleteResponseDto(Long favoriteId) {
        this.favoriteId=favoriteId;
    }
}
