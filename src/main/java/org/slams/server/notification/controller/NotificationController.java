package org.slams.server.notification.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;

/**
 * Created by yunyun on 2021/12/09.
 */

@RestController
@RequestMapping(value = "/api/v1/alarms", consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
@RequiredArgsConstructor
public class NotificationController {

//    private final AlarmService alarmService;
//
//    @PostMapping
//    public ApiResponse<String> save(@Valid @RequestBody final AlarmRequest alarmDto){
//        return ApiResponse.ok("");
//    }
//
//    @GetMapping("/{userId}")
//    public ApiResponse<List<Alarm>> findByUserId(@PathVariable final long userId,
//                                                 @RequestParam final CursorRequest cursorRequest){
//        return ApiResponse.ok(alarmService.findAllByUserId(userId));
//    }
}
