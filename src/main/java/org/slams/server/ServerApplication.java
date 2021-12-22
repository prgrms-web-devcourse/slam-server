package org.slams.server;

import org.slams.server.common.query.DummyCourtQuery;
import org.slams.server.court.repository.DummyCourtRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;

@EnableJpaAuditing
@SpringBootApplication
public class ServerApplication implements CommandLineRunner {

    private final DummyCourtQuery dummyCourtQuery;

    public ServerApplication(DummyCourtQuery dummyCourtQuery){
        this.dummyCourtQuery = dummyCourtQuery;
    }

    public static void main(String[] args) {
        SpringApplication.run(ServerApplication.class, args);
    }


    // court 데이터 미리 저장
    @Override
    public void run(String... args) throws Exception {
//        dummyCourtQuery.insert();
//        dummyCourtQuery.insertExcel();
//        dummyCourtQuery.insertNotificationDummy(1L, 20);

    }
}
