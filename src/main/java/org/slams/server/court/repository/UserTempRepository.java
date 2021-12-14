package org.slams.server.court.repository;

import org.slams.server.court.entity.Court;
import org.slams.server.user.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;

public interface UserTempRepository extends JpaRepository<User, Long> {
}
