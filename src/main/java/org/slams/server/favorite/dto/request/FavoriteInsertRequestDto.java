package org.slams.server.favorite.dto.request;

import lombok.*;
import org.slams.server.court.dto.request.CourtInsertRequestDto;
import org.slams.server.court.entity.Court;
import org.slams.server.favorite.entity.Favorite;
import org.slams.server.user.entity.User;

@Getter
@AllArgsConstructor
@NoArgsConstructor
public class FavoriteInsertRequestDto {

    Long courtId;


//    @Builder
//    public Favorite insertRequestDtoToEntity(User user, Court court) {
//        return Favorite.builder()
//                .user(user)
//                .court(court)
//                .build();
//    }

}
