package org.slams.server.notification.dto;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

/**
 * Created by yunyun on 2021/12/15.
 */

@Getter
@RequiredArgsConstructor
public class SocketRequest {
    private final String message;
    private final Long id;

}
