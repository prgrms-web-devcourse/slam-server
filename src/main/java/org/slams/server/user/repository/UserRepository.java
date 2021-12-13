package org.slams.server.user.repository;

import org.slams.server.user.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.Optional;

public interface UserRepository extends JpaRepository<User, Long> {

	Optional<User> findBySocialId(String socialId);

}
