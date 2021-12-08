package org.slams.server.court.dto.request;

import org.slams.server.court.entity.Court;

public class CourtInsertRequestDto {

    private String name;
    private String email;
    private String picture;
    private Double temperature;
    private String address;
    private String phoneNumber;


    // requestDto -> Entity
    public Court insertRequestDtoToEntity(UserInsertRequestDto requestDto) {
        return User.builder()
                .name(requestDto.getName())
                .email(requestDto.getEmail())
                .picture(requestDto.getPicture())
                .role(requestDto.getRole())
                .temperature(requestDto.getTemperature())
                .address(requestDto.getAddress())
                .phoneNumber(requestDto.getPhoneNumber())
                .build();
    }
}
