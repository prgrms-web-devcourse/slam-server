package org.slams.server.court.dto.request;

import lombok.AllArgsConstructor;
import lombok.Getter;
import org.slams.server.court.entity.Court;
import org.slams.server.court.entity.Texture;

@Getter
@AllArgsConstructor
public class CourtInsertRequestDto {

    private String name;
    private double latitude;
    private double longitude;
    private String image;
    private Texture texture;
    private int basketCount;

    // requestDto -> Entity
    public Court insertRequestDtoToEntity(CourtInsertRequestDto requestDto, Long id) {
        return Court.builder()
                .name(requestDto.getName())
                .latitude(requestDto.getLatitude())
                .longitude(requestDto.getLongitude())
                .image(requestDto.getImage())
                .texture(requestDto.getTexture())
                .basketCount(requestDto.getBasketCount())
                .build();
    }
}
