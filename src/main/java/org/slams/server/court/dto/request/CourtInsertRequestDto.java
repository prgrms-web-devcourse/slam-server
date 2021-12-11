package org.slams.server.court.dto.request;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import org.slams.server.court.entity.Court;
import org.slams.server.court.entity.NewCourt;
import org.slams.server.court.entity.Status;
import org.slams.server.court.entity.Texture;

@Getter
@AllArgsConstructor
@Builder
public class CourtInsertRequestDto {

    private String name;
    private double latitude;
    private double longitude;
    private String image;
    private Texture texture;
    private int basketCount;
    private Status status;

    // requestDto -> Entity
    public NewCourt insertRequestDtoToEntity(CourtInsertRequestDto requestDto) {
        return NewCourt.builder()
                .name(requestDto.getName())
                .latitude(requestDto.getLatitude())
                .longitude(requestDto.getLongitude())
                .image(requestDto.getImage())
                .texture(requestDto.getTexture())
                .basketCount(requestDto.getBasketCount())
                .status(requestDto.getStatus())
                .build();
    }
}
