package org.slams.server.common.query;

import lombok.RequiredArgsConstructor;
import org.apache.poi.openxml4j.exceptions.InvalidFormatException;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.slams.server.chat.service.ChatroomMappingService;
import org.slams.server.court.dto.request.CourtDummyExcelDto;
import org.slams.server.court.entity.Court;
import org.slams.server.court.entity.Texture;
import org.slams.server.court.repository.DummyCourtRepository;
import org.springframework.core.io.ClassPathResource;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.io.FileInputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

@Component
@RequiredArgsConstructor
//@ConfigurationProperties(prefix = "excel")
public class DummyCourtQuery {

    private final DummyCourtRepository dummyCourtRepository;
    private final ChatroomMappingService chatroomMappingService;

//    @Value("${excel.file}")
//    String efile;


    // 상대 경로


    // 여기에서 엑셀 읽고 더미데이터 넣기

    @Transactional
    public void insert() {
        final List<Court> details= Arrays.asList(
                new Court("잠실한강공원농구장",127.082348760182,37.5177740347208,"https://team14-slam.s3.ap-northeast-2.amazonaws.com/court_dummy/court1.jpg",2,Texture.ASPHALT),
                new Court("뚝섬농구장",127.077891070963,37.5277871954486,"https://team14-slam.s3.ap-northeast-2.amazonaws.com/court_dummy/court2.jpg",3,Texture.RUBBER),
                new Court("광진구 헤이 집 농구장",127.074164786264,37.6012425773731,"https://team14-slam.s3.ap-northeast-2.amazonaws.com/court_dummy/court3.jpg",4,Texture.CONCRETE),
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
        List<Court> dataList = new ArrayList<>();
        ClassPathResource resource = new ClassPathResource("test2.xlsx");

        if (resource.exists()) {
            FileInputStream file=new FileInputStream(resource.getFile());
            XSSFWorkbook workbook = new XSSFWorkbook(file);
            Sheet worksheet = workbook.getSheetAt(0);

            System.out.println("workSet"+worksheet.getPhysicalNumberOfRows());

            for (int i = 1; i < worksheet.getPhysicalNumberOfRows(); i++) {

                Row row = worksheet.getRow(i);

                CourtDummyExcelDto data = new CourtDummyExcelDto();

                data.setName(row.getCell(0).getStringCellValue());
                data.setLongitude(row.getCell(2).getNumericCellValue());
                data.setLatitude(row.getCell(3).getNumericCellValue());
                data.setBasketCount(row.getCell(4).getCellType());

                dataList.add(data.insertRequestDtoToEntity(data));

            }
            dummyCourtRepository.saveAll(dataList);

            file.close();
        }



    }




}
