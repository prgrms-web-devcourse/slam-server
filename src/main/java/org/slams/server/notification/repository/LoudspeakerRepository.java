package org.slams.server.notification.repository;

import org.slams.server.notification.entity.LoudSpeaker;
import org.springframework.data.jpa.repository.JpaRepository;

/**
 * Created by yunyun on 2021/12/15.
 */
public interface LoudspeakerRepository extends JpaRepository<LoudSpeaker, Long> {

}
