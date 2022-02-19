package org.slams.server.common.dto;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.Builder;
import lombok.Getter;
import org.slams.server.court.entity.Texture;
import org.slams.server.notification.common.ValidationMessage;

import static com.google.common.base.Preconditions.checkArgument;
import static org.apache.commons.lang3.StringUtils.isNotEmpty;

/**
 * Created by yunyun on 2022/01/24.
 */
@Getter
@JsonInclude(JsonInclude.Include.NON_NULL)
public class Court {
    private final Long id;
    private final String name;
    private final double latitude;
    private final double longitude;
    private final String image;
    private final int basketCount;
    private final Texture texture;

    @Builder
    public Court(
            Long id,
            String name,
            double latitude,
            double longitude,
            String image,
            int basketCount,
            Texture texture
    ){
        checkArgument(id != null, ValidationMessage.NOTNULL_ID);
        checkArgument(isNotEmpty(name), ValidationMessage.NOT_EMPTY_NAME);
        checkArgument(basketCount > 0, ValidationMessage.MORE_THAN_ONE_BASKET_COUNT);

        this.id = id;
        this.name = name;
        this.latitude = latitude;
        this.longitude = longitude;
        this.image = image;
        this.basketCount = basketCount;
        this.texture = texture;
    }
}
