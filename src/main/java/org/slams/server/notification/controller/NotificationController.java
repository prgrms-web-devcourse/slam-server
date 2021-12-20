package org.slams.server.notification.controller;

import lombok.RequiredArgsConstructor;
import org.slams.server.common.api.ApiResponse;
import org.slams.server.common.api.CursorPageRequest;
import org.slams.server.common.api.CursorPageResponse;
import org.slams.server.common.api.TokenGetId;
import org.slams.server.notification.dto.request.UpdateIsClickedStatusRequest;
import org.slams.server.notification.dto.response.NotificationResponse;
import org.slams.server.notification.service.NotificationService;
import org.slams.server.user.oauth.jwt.Jwt;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import java.util.List;

/**
 * Created by yunyun on 2021/12/09.
 */

@RestController
@RequestMapping(value = "/api/v1/notifications")
@RequiredArgsConstructor
public class NotificationController {

    private final NotificationService notificationService;
    private final Jwt jwt;


    @GetMapping
    public ResponseEntity<CursorPageResponse<List<NotificationResponse>>> findByUserId(
            CursorPageRequest cursorRequest,
            HttpServletRequest request){

        Long userId = new TokenGetId(request,jwt).getUserId();
        List<NotificationResponse> notificationResponseList = notificationService.findAllByUserId(userId, cursorRequest);

        return ResponseEntity.ok(new CursorPageResponse<>(
                notificationResponseList,
                notificationService.findNotificationLastId(userId, cursorRequest)
        ));
    }

    @PutMapping("/isClicked")
    public ResponseEntity<Void> updateIsClicked(HttpServletRequest request){
        Long userId = new TokenGetId(request,jwt).getUserId();
        notificationService.updateIsClickedStatus(new UpdateIsClickedStatusRequest(true), userId);
        return ResponseEntity.status(HttpStatus.ACCEPTED).build();
    }
}
