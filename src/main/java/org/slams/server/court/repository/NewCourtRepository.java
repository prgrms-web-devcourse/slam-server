package org.slams.server.court.repository;

import org.slams.server.court.entity.NewCourt;
import org.slams.server.court.entity.Status;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface NewCourtRepository extends JpaRepository<NewCourt, Long> {

	// 사용자가 추가한 농구장 조회(무한 스크롤 - 최초)
	@Query("SELECT n FROM NewCourt n WHERE n.status IN :statusList order by n.id desc")
	List<NewCourt> findByStatusOrderByIdDesc(@Param("statusList") List<Status> statusList, Pageable pageable);

	// 사용자가 추가한 농구장 조회(무한 스크롤)
	@Query("SELECT n FROM NewCourt n WHERE n.status IN :statusList and n.id < :lastId order by n.id desc")
	List<NewCourt> findByStatusLessThanIdOrderByIdDesc(
		@Param("statusList") List<Status> statusList, @Param("lastId") Long lastId, Pageable pageable);

}
