package org.slams.server.notification.common;

/**
 * Created by yunyun on 2022/01/24.
 */
public enum ValidationMessage {
    // common
    NOTNULL_ID("Id는 NULL 을 허용하지 않습니다."),

    // user
    NOTNULL_USERID("UserId는 NULL 을 허용하지 않습니다."),
    NOTNULL_USER("User 정보는 NULL 을 허용하지 않습니다."),
    NOT_EMPTY_NICKNAME("nickname은 빈값을 허용하지 않습니다."),

    // notification
    NOTNULL_NOTIFICATION_TYPE("Notification Type 정보는 NULL 을 허용하지 않습니다."),

    // court
    NOTNULL_COURT("경기장 정보는 NULL 을 허용하지 않습니다."),
    NOTNULL_START_TIME("경기 시작 시간의 정보는 NULL 을 허용하지 않습니다."),
    NOTNULL_END_TIME("경기 끝 시간의 정보는 NULL 을 허용하지 않습니다."),
    NOT_EMPTY_NAME("경기장 이름은 빈값을 허용하지 않습니다."),

    MORE_THAN_ONE_BASKET_COUNT("골대의 개수는 1개 이상이어야 합니다.")
    ;


    private String message;

    ValidationMessage(String message) {
        this.message = message;
    }

    public String getMessage() {
        return message;
    }
}