package org.slams.server.court.dto.response;

import lombok.Getter;
import org.slams.server.common.api.BaseResponse;
import org.slams.server.court.entity.Court;
import org.slams.server.court.entity.Texture;

@Getter
public class CourtDetailResponseDto extends BaseResponse {


    private String courtName;
    private double latitude;
    private double longitude;
    private String image;
    private Texture texture;
    private int basketCount;
    private Long courtReservation;

    public CourtDetailResponseDto(Court court, Long courtReservation) {
        super(court.getCreatedAt(), court.getUpdateAt());
        courtName=court.getName();
        latitude=court.getLatitude();
        longitude=court.getLongitude();
        image=court.getImage();
        texture=court.getTexture();
        basketCount=court.getBasketCount();
        this.courtReservation=courtReservation;
    }




}
