package org.slams.server.favorite.repository;

import org.slams.server.favorite.entity.Favorite;
import org.slams.server.user.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface FavoriteRepository extends JpaRepository<Favorite, Long> {

	List<Favorite> findAllByUser(User user);

}
