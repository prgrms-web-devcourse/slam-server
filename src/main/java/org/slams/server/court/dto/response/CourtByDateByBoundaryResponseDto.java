package org.slams.server.court.dto.response;

import lombok.EqualsAndHashCode;
import lombok.Getter;
import org.slams.server.common.api.BaseResponse;
import org.slams.server.court.entity.Court;

@Getter
@EqualsAndHashCode
public class CourtByDateByBoundaryResponseDto extends BaseResponse {

    private Long courtId;
    private String courtName;
    private double latitude;
    private double longitude;
    private Long courtReservation;


    public CourtByDateByBoundaryResponseDto(Court court, Long courtReservation) {
        super(court.getCreatedAt(), court.getUpdateAt());
        courtId=court.getId();
        courtName=court.getName();
        latitude=court.getLatitude();
        longitude=court.getLongitude();
        this.courtReservation=courtReservation;
    }


}
