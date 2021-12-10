package org.slams.server.alarm.repository;

import org.slams.server.alarm.entity.Alarm;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * Created by yunyun on 2021/12/08.
 */

public interface AlarmRepository extends JpaRepository<Alarm, Long> {
    @Query("select a from Alarm a where a.userId=:userId order by a.createdAt")
    List<Alarm> findAllByUserId(@Param("userId") Long userId);
}
