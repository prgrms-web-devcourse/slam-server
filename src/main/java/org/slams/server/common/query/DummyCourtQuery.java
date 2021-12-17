package org.slams.server.common.query;

import lombok.RequiredArgsConstructor;
import org.slams.server.court.entity.Court;
import org.slams.server.court.entity.Texture;
import org.slams.server.court.repository.DummyCourtRepository;
import org.springframework.stereotype.Component;

import java.util.Arrays;
import java.util.List;

@Component
@RequiredArgsConstructor
public class DummyCourtQuery {

    private final DummyCourtRepository dummyCourtRepository;


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
    }


}
