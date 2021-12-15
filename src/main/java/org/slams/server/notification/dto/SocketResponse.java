package org.slams.server.notification.dto;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

/**
 * Created by yunyun on 2021/12/15.
 */

@Getter
@RequiredArgsConstructor
public class SocketResponse {
    private final Long status;
    private final String message;
}
