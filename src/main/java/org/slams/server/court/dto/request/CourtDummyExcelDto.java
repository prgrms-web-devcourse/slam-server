package org.slams.server.court.dto.request;

import lombok.Getter;
import lombok.Setter;
import org.slams.server.court.entity.Court;
import org.slams.server.court.entity.NewCourt;
import org.slams.server.court.entity.Status;
import org.slams.server.court.entity.Texture;

import javax.validation.constraints.Min;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Positive;

@Getter
@Setter
public class CourtDummyExcelDto {


    private String name;
    private double latitude;
    private double longitude;
    private Texture texture;
    private int basketCount;


    public Court insertRequestDtoToEntity(CourtDummyExcelDto requestDto) {
        return Court.builder()
                .name(requestDto.getName())
                .latitude(requestDto.getLatitude())
                .longitude(requestDto.getLongitude())
                .texture(requestDto.getTexture())
                .basketCount(requestDto.getBasketCount())
                .build();
    }

}
