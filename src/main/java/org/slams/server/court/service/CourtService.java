package org.slams.server.court.service;

import org.slams.server.court.dto.request.CourtInsertRequestDto;
import org.slams.server.court.entity.Court;
import org.slams.server.court.repository.CourtRepository;
import org.slams.server.court.repository.UserTempRepository;
import org.slams.server.user.entity.User;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class CourtService {

    private final CourtRepository courtRepository;
    private final UserTempRepository userTempRepository;



    public CourtService(CourtRepository courtRepository, UserTempRepository userTempRepository) {
        this.courtRepository=courtRepository;
        this.userTempRepository=userTempRepository;
    }


    @Transactional
    public void insert(CourtInsertRequestDto request, Long id) {
        // user검색후 없으면 반환
        User user = getUser(id);
        Court court = request.insertRequestDtoToEntity(request, user);

        post.addPost(user);

        Post insertedPost = postRepository.save(post);
        return new PostDetailResponse(insertedPost);
    }


    @Transactional
    public User getUser(Long userId) {
        return userTempRepository.findById(userId)
                .orElseThrow(() -> new NotFoundException(ErrorMessage.NOT_EXIST_MEMBER));
    }







}
