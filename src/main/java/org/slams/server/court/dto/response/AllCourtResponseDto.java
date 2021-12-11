package org.slams.server.court.dto.response;

import lombok.AllArgsConstructor;
import lombok.Getter;
import org.slams.server.common.api.BaseResponse;
import org.slams.server.court.entity.Court;
import org.slams.server.court.entity.Texture;

@Getter
public class AllCourtResponseDto extends BaseResponse {

    private String name;
    private double latitude;
    private double longitude;
    private String image;
    private Texture texture;
    private int basketCount;

    public AllCourtResponseDto(Court court) {
        super(court.getCreatedAt(), court.getUpdateAt());
        name = user.getName();
        email = user.getEmail();
        picture = user.getPicture();
        role = user.getRole();
        temperature = user.getTemperature();
        address = user.getAddress();
        phoneNumber = user.getPhoneNumber();
    }




}
