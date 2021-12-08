package org.slams.server.court.controller;


import lombok.extern.slf4j.Slf4j;
import org.slams.server.court.service.CourtService;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@Slf4j
@RestController
@RequestMapping(value = "/api/v1/courts")
public class CourtController {

    public final CourtService courtService;

    public CourtController(CourtService courtService) {
        this.courtService=courtService;
    }


}
