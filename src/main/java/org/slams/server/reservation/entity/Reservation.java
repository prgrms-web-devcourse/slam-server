package org.slams.server.reservation.entity;

import org.slams.server.common.BaseEntity;
import org.slams.server.court.entity.Court;
import org.slams.server.user.entity.User;

import javax.persistence.*;
import java.time.LocalDateTime;

/**
 * Created by yunyun on 2021/12/03.
 */

@Entity
@Table(name="reservation")
public class Reservation extends BaseEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name="id")
    private Long id;

    @ManyToOne(fetch=FetchType.LAZY)
    @JoinColumn(name = "court_id", nullable = false)
    private Court court;

    @ManyToOne(fetch=FetchType.LAZY)
    @JoinColumn(name = "user_id", nullable = false)
    private User user;

    @Column(nullable = false)
    private LocalDateTime startTime;

    @Column(nullable = false)
    private LocalDateTime endTime;

    @Column(nullable = false)
    private boolean hasBall;
}
