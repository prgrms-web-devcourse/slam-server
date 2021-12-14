package org.slams.server.user.dto.request;

import lombok.*;
import org.slams.server.user.entity.Position;
import org.slams.server.user.entity.Proficiency;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import java.util.List;

@Builder
@Getter
@NoArgsConstructor(access = AccessLevel.PRIVATE)
@AllArgsConstructor
public class ExtraUserInfoRequest {

	private static final int MAX_LENGTH_NICKNAME = 15;
	private static final int MAX_DESCRIPTION_LENGTH = 50;

	@NotBlank
	@Size(message = "닉네임은 15자 이내로 작성해주세요.", max = MAX_LENGTH_NICKNAME)
	private String nickname;

	@Size(message = "한줄 소개는 50자 이내로 작성해주세요.", max = MAX_DESCRIPTION_LENGTH)
	private String description;

	@NotNull
	private Proficiency proficiency;

	@NotNull
	private List<Position> positions;

}
