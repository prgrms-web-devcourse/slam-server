package org.slams.server.court.dto.request;

import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
public class RequestParamVo {


    private String date;
    private String time;
    private List<String> latitude;
    private List<String> longitude;


    @Override public String toString() {
        return "RequestParamVo{"
                + "date='"
                + date
                + '\''
                + ", time='"
                + time
                + '\''
                + ", latitude="
                + latitude
                + ", longitude="
                + longitude
                +'}';
    }







}
