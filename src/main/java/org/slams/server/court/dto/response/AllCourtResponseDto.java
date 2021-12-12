package org.slams.server.court.dto.response;

import lombok.*;
import org.slams.server.common.api.BaseResponse;
import org.slams.server.court.entity.Court;
import org.slams.server.court.entity.Texture;

@Getter
@EqualsAndHashCode
public class AllCourtResponseDto extends BaseResponse {

    private String name;
    private double latitude;
    private double longitude;
    private String image;
    private Texture texture;
    private int basketCount;

    public AllCourtResponseDto(Court court) {
        super(court.getCreatedAt(), court.getUpdateAt());
        name=court.getName();
        latitude=court.getLatitude();
        longitude=court.getLongitude();
        image=court.getImage();
        texture=court.getTexture();
        basketCount=court.getBasketCount();
    }




}
