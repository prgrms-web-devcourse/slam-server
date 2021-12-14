package org.slams.server.notification.controller;

import lombok.RequiredArgsConstructor;
import org.slams.server.common.api.ApiResponse;
import org.slams.server.common.api.CursorPageResponse;
import org.slams.server.notification.dto.CursorRequest;
import org.slams.server.notification.dto.NotificationRequest;
import org.slams.server.notification.dto.NotificationResponse;
import org.slams.server.notification.service.NotificationService;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;

/**
 * Created by yunyun on 2021/12/09.
 */

@RestController
@RequestMapping(value = "/api/v1/notification", consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
@RequiredArgsConstructor
public class NotificationController {

    private final NotificationService notificationService;

    @PostMapping
    public ApiResponse<String> save(@Valid @RequestBody final NotificationRequest notificationRequest){
        return ApiResponse.ok(null);
    }

    @GetMapping("/{userId}")
    public CursorPageResponse<List<NotificationResponse>> findByUserId(@PathVariable final long userId,
                                                                       @RequestParam final CursorRequest cursorRequest){
        List<NotificationResponse> notificationResponseList = notificationService.findAllByUserId(userId, cursorRequest);
        return new CursorPageResponse<>(
                notificationResponseList,
                notificationResponseList.get(notificationResponseList.size()-1).getNotificationId()
        );
    }
}
