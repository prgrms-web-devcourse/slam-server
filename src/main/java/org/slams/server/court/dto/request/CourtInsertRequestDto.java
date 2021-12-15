package org.slams.server.court.dto.request;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import org.slams.server.court.entity.Court;
import org.slams.server.court.entity.NewCourt;
import org.slams.server.court.entity.Status;
import org.slams.server.court.entity.Texture;

import javax.validation.constraints.*;
import javax.validation.constraints.NotNull;


import static com.google.common.base.Preconditions.checkArgument;
import static org.apache.commons.lang3.StringUtils.isAnyEmpty;

@Getter
@Builder
public class CourtInsertRequestDto {

    @NotNull(message ="name은 필수 값입니다.")
//    @Max(value=100,message = "name은 50글자 미만입니다.")
    private String name;
    @Positive(message = "위도는 0이상값입니다.")
    private double latitude;
    @Positive(message = "경도는 0이상값입니다.")
    private double longitude;
    private String image;
    private Texture texture;
    @Min(value=0,message = "농구골대는 0이상값입니다.")
    private int basketCount;
    private Status status;

    private String mediaUrl;

    public void setMediaUrl(String mediaUrl){
        this.mediaUrl = mediaUrl;
    }



    // requestDto -> Entity
    public NewCourt insertRequestDtoToEntity(CourtInsertRequestDto requestDto) {
        return NewCourt.builder()
                .name(requestDto.getName())
                .latitude(requestDto.getLatitude())
                .longitude(requestDto.getLongitude())
                .medialUrl(requestDto.getMediaUrl())
                .texture(requestDto.getTexture())
                .basketCount(requestDto.getBasketCount())
                .status(requestDto.getStatus())
                .build();
    }


}
