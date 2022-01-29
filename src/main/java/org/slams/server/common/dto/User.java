package org.slams.server.common.dto;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.Builder;
import org.slams.server.notification.common.ValidationMessage;
import org.slams.server.user.entity.Position;
import org.slams.server.user.entity.Proficiency;

import java.util.List;

import static com.google.common.base.Preconditions.checkArgument;
import static org.apache.commons.lang3.StringUtils.isNotEmpty;

/**
 * Created by yunyun on 2022/01/24.
 */

@JsonInclude(JsonInclude.Include.NON_NULL)
public class User extends BaseDto{

    private Long id;
    private String nickname;
    private String profileImage;
    private String description;
    private Proficiency proficiency;
    private List<Position> positions;

    @Builder
    public User(
            Long id,
            String nickname,
            String profileImage,
            String description,
            Proficiency proficiency,
            List<Position> positions
    ){
        checkArgument(id != null, ValidationMessage.NOTNULL_ID);
        checkArgument(isNotEmpty(nickname), ValidationMessage.NOT_EMPTY_NICKNAME);

        this.id = id;
        this.nickname = nickname;
        this.profileImage = profileImage;
        this.description = description;
        this.positions = positions;

    }
}
