package org.slams.server.follow.service;

import org.junit.jupiter.api.*;
import org.slams.server.follow.entity.Follow;
import org.slams.server.follow.repository.FollowRepository;
import org.slams.server.user.entity.Position;
import org.slams.server.user.entity.Proficiency;
import org.slams.server.user.entity.Role;
import org.slams.server.user.entity.User;
import org.slams.server.user.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

@TestInstance(TestInstance.Lifecycle.PER_CLASS)
@Transactional
@SpringBootTest
class FollowServiceTest {

	@Autowired
	private FollowService followService;

	@Autowired
	private FollowRepository followRepository;

	@Autowired
	private UserRepository userRepository;

	private User user1, user2;

	@BeforeAll
	void setUp(){
		user1 =User.of("12345", "jelly@email.com", "jelly", "s3에 저장한 이미지 url",
			"한줄 소개", Role.USER, Proficiency.BEGINNER, List.of(Position.PG));
		user2 = User.of("34820", "choco@email.com", "choco", "s3에 저장한 이미지 url",
			"한줄 소개", Role.USER, Proficiency.MASTER, List.of(Position.C));
	}

	@AfterAll
	void tearDown(){
		userRepository.deleteAll();
	}

	@Test
	void follow(){
		// given
		List<User> users = userRepository.saveAll(List.of(user1, user2));

		// when
		followService.follow(users.get(0).getId(), users.get(1).getId());

		// then
		assertThat(followRepository.count()).isEqualTo(1);
	}

	@Test
	void unfollow(){
		// given
		List<User> users = userRepository.saveAll(List.of(user1, user2));

		followRepository.save(Follow.of(users.get(1), users.get(0)));

		// when
		followService.unfollow(users.get(1).getId(), users.get(0).getId());

		// then
		assertThat(followRepository.count()).isEqualTo(0);
	}

}