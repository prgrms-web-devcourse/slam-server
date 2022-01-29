package org.slams.server.common.query;

import org.apache.poi.openxml4j.exceptions.InvalidFormatException;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.slams.server.chat.service.ChatroomMappingService;
import org.slams.server.court.dto.request.CourtDummyExcelDto;
import org.slams.server.court.entity.Court;
import org.slams.server.court.entity.Texture;
import org.slams.server.court.repository.DummyCourtRepository;
import org.slams.server.notification.dto.request.LoudspeakerNotificationRequest;
import org.slams.server.notification.service.NotificationService;
import org.slams.server.reservation.entity.Reservation;
import org.slams.server.reservation.repository.ReservationRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.core.io.ClassPathResource;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.ResourceUtils;

import java.io.IOException;
import java.io.InputStream;

import java.net.URL;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

@Component
//@ConfigurationProperties(prefix = "excel")
public class DummyCourtQuery {
    private final Logger logger = LoggerFactory.getLogger(this.getClass());

    private final DummyCourtRepository dummyCourtRepository;
    private final ChatroomMappingService chatroomMappingService;
    private final NotificationService notificationService;
    private final ReservationRepository reservationRepository;

    private final String excelFileName = "dummyCourt.xlsx";

    public DummyCourtQuery(
            DummyCourtRepository dummyCourtRepository,
            ChatroomMappingService chatroomMappingService,
            NotificationService notificationService,
            ReservationRepository reservationRepository
    ){
        this.chatroomMappingService = chatroomMappingService;
        this.dummyCourtRepository = dummyCourtRepository;
        this.notificationService = notificationService;
        this.reservationRepository = reservationRepository;
    }

//    @Value("${excel.file}")
//    String efile;


    // 상대 경로


    // 여기에서 엑셀 읽고 더미데이터 넣기

    @Transactional
    public void insert() {
        final List<Court> details= Arrays.asList(
                new Court("잠실한강공원농구장",37.5177740347208,127.082348760182,"https://team14-slam.s3.ap-northeast-2.amazonaws.com/court_dummy/court1.jpg",2,Texture.ASPHALT),
                new Court("뚝섬농구장",37.5277871954486,127.077891070963,"https://team14-slam.s3.ap-northeast-2.amazonaws.com/court_dummy/court2.jpg",3,Texture.RUBBER),
                new Court("광진구 헤이 집 농구장",37.6012425773731,127.074164786264,"https://team14-slam.s3.ap-northeast-2.amazonaws.com/court_dummy/court3.jpg",4,Texture.CONCRETE),
                new Court("헤이집1",55.00,201.00,"https://team14-slam.s3.ap-northeast-2.amazonaws.com/court_dummy/court1.jpg",2,Texture.ASPHALT),
                new Court("헤이집2",45.00,299.00,"https://team14-slam.s3.ap-northeast-2.amazonaws.com/court_dummy/court2.jpg",3,Texture.RUBBER),
                new Court("헤이집3",99.00,345.00,"https://team14-slam.s3.ap-northeast-2.amazonaws.com/court_dummy/court3.jpg",4,Texture.CONCRETE)
                );

        List<Court> saveDetail=dummyCourtRepository.saveAll(details);

        // chat Room Service 넣기
        details.stream().map(Court::getId).forEach(chatroomMappingService::saveChatRoom);

    }

    @Transactional
    public void insertExcel() throws IOException, InvalidFormatException {

//        URL res = getClass().getClassLoader().getResource(excelFileName);

        List<Court> dataList = new ArrayList<>();

        try (
//                FileInputStream file = new FileInputStream(dumpDataLocation);
                InputStream file = new ClassPathResource(excelFileName).getInputStream();

        ) {


            XSSFWorkbook workbook = new XSSFWorkbook(file);
            Sheet worksheet = workbook.getSheetAt(0);

            logger.info("workSet" + worksheet.getPhysicalNumberOfRows());

            for (int i = 1; i < worksheet.getPhysicalNumberOfRows(); i++) {

                Row row = worksheet.getRow(i);

                CourtDummyExcelDto data = new CourtDummyExcelDto();

                data.setName(row.getCell(3).getStringCellValue());

                data.setLongitude(row.getCell(1).getNumericCellValue());
                data.setLatitude(row.getCell(2).getNumericCellValue());

                data.setBasketCount(2);

                dataList.add(data.insertRequestDtoToEntity(data));

            }
            dummyCourtRepository.saveAll(dataList);
            dataList.stream().map(Court::getId).forEach(chatroomMappingService::saveChatRoom);
        } catch (Exception e) {

            logger.info(e.getMessage());
            logger.error("insert dumy data ERROR");
            insert();
        }
    }

    public void insertNotificationDummy(Long userId, int dataSize){
        LoudspeakerNotificationRequest request = new LoudspeakerNotificationRequest(1L, LocalDateTime.now(), LocalDateTime.now().plusHours(2L), 1L);

        int index = 0;
        while (index < dataSize){
            notificationService.saveForLoudSpeakerNotification(request, userId, userId);
            index = index + 1;
        }

    }

}
