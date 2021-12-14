package org.slams.server.court.dto.response;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import org.slams.server.common.api.BaseResponse;
import org.slams.server.court.dto.request.CourtInsertRequestDto;
import org.slams.server.court.entity.NewCourt;
import org.slams.server.court.entity.Status;
import org.slams.server.court.entity.Texture;


@Getter
@EqualsAndHashCode
public class CourtInsertResponseDto extends BaseResponse {

    private String name;
    private double latitude;
    private double longitude;
    private String image;
    private Texture texture;
    private int basketCount;
    private Status status;
    private Long newCourtId;

    // Entity -> dto
    public CourtInsertResponseDto(NewCourt newCourt) {
        super(newCourt.getCreatedAt(), newCourt.getUpdateAt());
                newCourtId=newCourt.getId();
                name=newCourt.getName();
                latitude=newCourt.getLatitude();
                longitude=newCourt.getLongitude();
                image=newCourt.getImage();
                texture=newCourt.getTexture();
                basketCount=newCourt.getBasketCount();
                status=newCourt.getStatus();
    }
}
