package org.slams.server.notification.entity;

/**
 * Created by yunyun on 2021/12/06.
 */
public enum NotificationMessageFormat {
    FOLLOWING_ALARM("{}가 당신을 팔로우하였습니다."),
    LOUDSPEAKER("{}시에 시작하는 {}에서 사람을 구합니다.");

    private String messageFormat;

    NotificationMessageFormat(String messageFormat){
        this.messageFormat = messageFormat;
    }

    public String getMessageFormat() {
        return messageFormat;
    }
}
