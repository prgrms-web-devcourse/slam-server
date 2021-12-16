package org.slams.server.notification.dto.response;

import lombok.Builder;
import lombok.Getter;
import org.slams.server.court.entity.Texture;

import static com.google.common.base.Preconditions.checkArgument;
import static org.apache.commons.lang3.StringUtils.isNotEmpty;

/**
 * Created by yunyun on 2021/12/14.
 */

@Getter
public class CourtInfo {
    private final Long id;
    private final String name;
    private final double latitude;
    private final double longitude;
    private final String image;
    private final int basketCount;
    private final Texture texture;

    @Builder
    public CourtInfo(
            Long id,
            String name,
            double latitude,
            double longitude,
            String image,
            int basketCount,
            Texture texture
    ){
        checkArgument(id != null, "userId는 null을 허용하지 않습니다.");
        checkArgument(isNotEmpty(name), "농구장는 빈값을 허용하지 않습니다.");
        checkArgument(texture != null, "texture는 null을 허용하지 않습니다.");
        checkArgument(basketCount >= 0, "골대 개수는 0이상만 가능합니다.");

        this.id = id;
        this.name = name;
        this.latitude = latitude;
        this.longitude = longitude;
        this.image = image;
        this.basketCount = basketCount;
        this.texture = texture;
    }
}
