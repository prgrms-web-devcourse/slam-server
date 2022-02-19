package org.slams.server.notification.dto.request;

import lombok.Getter;
import lombok.Setter;

/**
 * Created by yunyun on 2021/12/16.
 */

@Getter
public class UpdateIsClickedStatusRequest {
    private final boolean status;

    public UpdateIsClickedStatusRequest(boolean status){
        this.status = status;
    }
}
