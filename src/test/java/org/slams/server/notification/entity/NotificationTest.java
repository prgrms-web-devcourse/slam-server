//package org.slams.server.notification.entity;
//
//import org.junit.jupiter.api.Assertions;
//import org.junit.jupiter.api.Test;
//import org.slams.server.court.entity.Court;
//import org.slams.server.court.entity.Texture;
//
//import static org.hamcrest.MatcherAssert.assertThat;
//import static org.hamcrest.CoreMatchers.*;
///**
// * Created by yunyun on 2021/12/07.
// */
//class NotificationTest {
//
//    /** request로 받은 정보를 활용하여, 팔로우 정보를 담은 알람 테스트 **/
//    @Test
//    void request로_받은_정보를_활용하여_팔로우_정보를_담은_알람_엔터티를_생성할_수_있다(){
//
//        // Given, When
//        Notification alarmCreated = Notification.createNotificationForFollowing(
//                null,
//                1L,
//                "flora"
//        );
//
//
//        // Then
//        assertThat(alarmCreated.getNotificationType(), is(NotificationType.FOLLOWING_ALARM));
//        assertThat(alarmCreated.getContent(), containsString("팔로우"));
//        assertThat(alarmCreated.getUserId(), is(1L));
//    }
//
//    @Test
//    void request로_받은_정보를_활용하여_팔로우_정보를_담은_알람에서_userId는_0이상이어야_한다(){
//        //Given, When
//        var msg =Assertions.assertThrows(IllegalArgumentException.class, () -> {
//            Notification.createNotificationForFollowing(
//                    null,
//                    -1L,
//                    "flora"
//            );
//        });
//        // Then
//        assertThat(msg.getMessage(), containsString("0미만은 허용하지 않습니다"));
//    }
//
//
//    @Test
//    void request로_받은_정보를_활용하여_팔로우_정보를_담은_알람에서_nickname은_빈값을_허용하지_않는다(){
//        //Given, When
//        var msg = Assertions.assertThrows(IllegalArgumentException.class, () -> {
//            Notification.createNotificationForFollowing(
//                    null,
//                    1L,
//                    ""
//            );
//        });
//
//        // Then
//        assertThat(msg.getMessage(), containsString("빈값을 허용하지 않습니다"));
//    }
//
//    @Test
//    void request로_받은_정보를_활용하여_팔로우_정보를_담은_알람에서_Content_내용을_수정할_수_있다(){
//        //Given
//        Notification alarmCreated = Notification.createNotificationForFollowing(
//                null,
//                1L,
//                "flora"
//        );
//
//        //When
//        alarmCreated.updateContent("메시지 수정 완료");
//
//        // Then
//        assertThat(alarmCreated.getContent(), containsString("수정"));
//    }
//
//    /** request로 받은 정보를 활용하여, 농구 인원 급구 정보를 담은 알람 테스트 **/
//
//    @Test
//    void request로_받은_정보를_활용하여_농구_인원_급구_정보를_담은_알람_엔터티를_생성할_수_있다(){
//        // Given, When
//        Court court = new Court(1L, "잠실농구장", 132.2304, 209.102387, "http://test-image", 1, Texture.TEST);
//        Notification alarmCreated = Notification.createNotificationForLoudSpeaker(
//                10L,
//                1L,
//                13,
//                "잠실농구장");
//
//        // Then
//        assertThat(alarmCreated.getNotificationType(), is(NotificationType.LOUDSPEAKER));
//        assertThat(alarmCreated.getContent(), containsString("잠실농구장"));
//        assertThat(alarmCreated.getUserId(), is(1L));
//        assertThat(alarmCreated.getCourtId(), is(10L));
//    }
//
//    @Test
//    void request로_받은_정보를_활용하여_농구_인원_급구_정보를_담은_알람에서_userId는_0이상이어야_한다(){
//        //Given, When
//        var msg =Assertions.assertThrows(IllegalArgumentException.class, () -> {
//            Court court = new Court(1L, "잠실농구장", 132.2304, 209.102387, "http://test-image", 1, Texture.ASPHALT);
//            Notification.createNotificationForLoudSpeaker(
//                    10L,
//                    -1L,
//                    13,
//                    "잠실농구장");
//        });
//        // Then
//        assertThat(msg.getMessage(), containsString("0미만은 허용하지 않습니다"));
//    }
//
//
//    @Test
//    void request로_받은_정보를_활용하여_농구_인원_급구_정보를_담은_알람에서_경기시작시간은_0이상24이하여야한다(){
//        //Given, When
//        var msg = Assertions.assertThrows(IllegalArgumentException.class, () -> {
//            Court court = new Court(1L, "잠실농구장", 132.2304, 209.102387, "http://test-image", 1, Texture.ASPHALT);
//            Notification.createNotificationForLoudSpeaker(
//                    10L,
//                    1L,
//                    25,
//                    "잠실농구장");
//        });
//
//        // Then
//        assertThat(msg.getMessage(), containsString("0이상 24시이하만"));
//    }
//
//    @Test
//    void request로_받은_정보를_활용하여_농구_인원_급구_정보를_담은_알람에서_농구장이름은_빈값을_허용하지_않는다(){
//        //Given, When
//        var msg = Assertions.assertThrows(IllegalArgumentException.class, () -> {
//            Court court = new Court(1L, "잠실농구장", 132.2304, 209.102387, "http://test-image", 1, Texture.TEST);
//            Notification.createNotificationForLoudSpeaker(
//                    10L,
//                    1L,
//                    13,
//                    "");
//        });
//        // Then
//        assertThat(msg.getMessage(), containsString("빈값을 허용하지 않습니다"));
//    }
//
//    @Test
//    void request로_받은_정보를_활용하여_농구_인원_급구_정보를_담은_알람에서_Content_내용을_수정할_수_있다(){
//        //Given
//        Court court = new Court(1L, "잠실농구장", 132.2304, 209.102387, "http://test-image", 1, Texture.TEST);
//        Notification alarmCreated = Notification.createNotificationForLoudSpeaker(
//                10L,
//                1L,
//                13,
//                "잠실농구장");
//
//        //When
//        alarmCreated.updateContent("메시지 수정 완료");
//
//        // Then
//        assertThat(alarmCreated.getContent(), containsString("수정"));
//    }
//
//    /** db에서 추출한 정보를 활용하여, 팔로우 정보를 담은 알람 테스트 **/
//    @Test
//    void db에서_추출한_정보를_활용하여_팔로우_정보를_담은_알람_엔터티를_생성할_수_있다(){
//
//        // Given, When
//        Notification alarmCreated = Notification.builder()
//                .id(1L)
//                .courtId(null)
//                .userId(1L)
//                .notificationType(NotificationType.FOLLOWING_ALARM)
//                .content("flora가 당신을 팔로우하였습니다.")
//                .build();
//
//
//        // Then
//        assertThat(alarmCreated.getNotificationType(), is(NotificationType.FOLLOWING_ALARM));
//        assertThat(alarmCreated.getContent(), containsString("팔로우"));
//        assertThat(alarmCreated.getUserId(), is(1L));
//    }
//
//    @Test
//    void db에서_추출한_정보를_활용하여_팔로우_정보를_담은_알람에서_userId는_0이상이어야_한다(){
//        //Given, When
//        var msg =Assertions.assertThrows(IllegalArgumentException.class, () -> {
//            Notification.builder()
//                    .id(1L)
//                    .courtId(null)
//                    .userId(-1L)
//                    .notificationType(NotificationType.FOLLOWING_ALARM)
//                    .content("flora가 당신을 팔로우하였습니다.")
//                    .build();
//        });
//        // Then
//        assertThat(msg.getMessage(), containsString("0미만은 허용하지 않습니다"));
//    }
//
//
//    @Test
//    void db에서_추출한_정보를_활용하여_팔로우_정보를_담은_알람에서_content은_빈값을_허용하지_않는다(){
//        //Given, When
//        var msg = Assertions.assertThrows(IllegalArgumentException.class, () -> {
//            Notification.builder()
//                    .id(1L)
//                    .courtId(null)
//                    .userId(1L)
//                    .notificationType(NotificationType.FOLLOWING_ALARM)
//                    .content("")
//                    .build();
//        });
//
//        // Then
//        assertThat(msg.getMessage(), containsString("빈값을 허용하지 않습니다"));
//    }
//
//    @Test
//    void db에서_추출한_정보를_활용하여_팔로우_정보를_담은_알람에서_alarmType은_null을_허용하지_않는다(){
//        //Given, When
//        var msg = Assertions.assertThrows(IllegalArgumentException.class, () -> {
//            Notification.builder()
//                    .id(1L)
//                    .courtId(null)
//                    .userId(1L)
//                    .notificationType(null)
//                    .content("")
//                    .build();
//        });
//
//        // Then
//        assertThat(msg.getMessage(), containsString("null을 허용하지 않습니다"));
//    }
//
//    @Test
//    void db에서_추출한_정보를_활용하여_팔로우_정보를_담은_알람에서_Content_내용을_수정할_수_있다(){
//        //Given
//        Notification alarmCreated = Notification.builder()
//                .id(1L)
//                .courtId(null)
//                .userId(1L)
//                .notificationType(NotificationType.FOLLOWING_ALARM)
//                .content("flora가 당신을 팔로우하였습니다.")
//                .build();
//
//        //When
//        alarmCreated.updateContent("메시지 수정 완료");
//
//        // Then
//        assertThat(alarmCreated.getContent(), containsString("수정"));
//    }
//
//
//    /** db에서 추출한 정보를 활용하여, 농구 인원 급구 정보를 담은 알람 테스트 **/
//
//    @Test
//    void db에서_추출한_정보를_활용하여_농구_인원_급구_정보를_담은_알람_엔터티를_생성할_수_있다(){
//        // Given, When
//        Court court = new Court(1L, "잠실농구장", 132.2304, 209.102387, "http://test-image", 1, Texture.TEST);
//        Notification alarmCreated = Notification.builder()
//                .id(1L)
//                .courtId(10L)
//                .userId(1L)
//                .notificationType(NotificationType.LOUDSPEAKER)
//                .content("13시에 시작하는 잠실농구장에서 사람을 구합니다.")
//                .build();
//
//        // Then
//        assertThat(alarmCreated.getNotificationType(), is(NotificationType.LOUDSPEAKER));
//        assertThat(alarmCreated.getContent(), containsString("잠실농구장"));
//        assertThat(alarmCreated.getUserId(), is(1L));
//        assertThat(alarmCreated.getCourtId(), is(10L));
//    }
//
//    @Test
//    void db에서_추출한_정보를_활용하여_농구_인원_급구_정보를_담은_알람에서_userId는_0이상이어야_한다(){
//        //Given, When
//        var msg =Assertions.assertThrows(IllegalArgumentException.class, () -> {
//            Court court = new Court(1L, "잠실농구장", 132.2304, 209.102387, "http://test-image", 1, Texture.TEST);
//            Notification.builder()
//                    .id(1L)
//                    .courtId(10L)
//                    .userId(-1L)
//                    .notificationType(NotificationType.LOUDSPEAKER)
//                    .content("13시에 시작하는 잠실농구장에서 사람을 구합니다.")
//                    .build();
//        });
//        // Then
//        assertThat(msg.getMessage(), containsString("0미만은 허용하지 않습니다"));
//    }
//
//
//    @Test
//    void db에서_추출한_정보를_활용하여_농구_인원_급구_정보를_담은_알람에서_court정보는_null을_허용_하지_않는다(){
//        //Given, When
//        var msg = Assertions.assertThrows(IllegalArgumentException.class, () -> {
//            Notification.builder()
//                    .id(1L)
//                    .courtId(null)
//                    .userId(1L)
//                    .notificationType(NotificationType.LOUDSPEAKER)
//                    .content("13시에 시작하는 잠실농구장에서 사람을 구합니다.")
//                    .build();
//        });
//
//        // Then
//        assertThat(msg.getMessage(), containsString("null을 허용하지 않습니다"));
//    }
//
//    @Test
//    void db에서_추출한_정보를_활용하여_농구_인원_급구_정보를_담은_알람에서_AlarmType은_null을_허용하지_않는다(){
//        //Given, When
//        var msg = Assertions.assertThrows(IllegalArgumentException.class, () -> {
//            Court court = new Court(1L, "잠실농구장", 132.2304, 209.102387, "http://test-image", 1, Texture.TEST);
//            Notification.builder()
//                    .id(1L)
//                    .courtId(10L)
//                    .userId(1L)
//                    .notificationType(null)
//                    .content("13시에 시작하는 잠실농구장에서 사람을 구합니다.")
//                    .build();
//        });
//        // Then
//        assertThat(msg.getMessage(), containsString("null을 허용하지 않습니다"));
//    }
//
//    @Test
//    void db에서_추출한_정보를_활용하여_농구_인원_급구_정보를_담은_알람에서_Content_내용을_수정할_수_있다(){
//        //Given
//        Court court = new Court(1L, "잠실농구장", 132.2304, 209.102387, "http://test-image", 1, Texture.TEST);
//        Notification alarmCreated = Notification.builder()
//                .id(1L)
//                .courtId(10L)
//                .userId(1L)
//                .notificationType(NotificationType.LOUDSPEAKER)
//                .content("13시에 시작하는 잠실농구장에서 사람을 구합니다.")
//                .build();
//
//        //When
//        alarmCreated.updateContent("메시지 수정 완료");
//
//        // Then
//        assertThat(alarmCreated.getContent(), containsString("수정"));
//    }
//
//}