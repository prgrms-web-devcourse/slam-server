package org.slams.server.notification.controller;

import lombok.RequiredArgsConstructor;
import org.slams.server.common.api.CursorPageResponse;
import org.slams.server.common.api.TokenGetId;
import org.slams.server.notification.dto.response.NotificationResponse;
import org.slams.server.notification.service.NotificationService;
import org.slams.server.user.oauth.jwt.Jwt;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import java.util.List;

/**
 * Created by yunyun on 2021/12/09.
 */

@RestController
@RequestMapping(value = "/api/v1/notification", consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
@RequiredArgsConstructor
public class NotificationController {

    private final NotificationService notificationService;
    private final Jwt jwt;


    @GetMapping
    public CursorPageResponse<List<NotificationResponse>> findByUserId(@RequestParam final CursorRequest cursorRequest,
                                                                       HttpServletRequest request){
        List<NotificationResponse> notificationResponseList = notificationService.findAllByUserId(new TokenGetId(request,jwt).getUserId(), cursorRequest);
        return new CursorPageResponse<>(
                notificationResponseList,
                notificationResponseList.get(notificationResponseList.size()-1).getNotificationId()
        );
    }
}
