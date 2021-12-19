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

    public static void main(String[] args) {
        SpringApplication.run(ServerApplication.class, args);
    }


    // court 데이터 미리 저장
    @Autowired
    private DummyCourtQuery dummyCourtQuery;
    @Override
    public void run(String... args) throws Exception {
//        dummyCourtQuery.insert();
        dummyCourtQuery.insertExcel();

    }
}
