package org.slams.server.follow.entity;

import org.slams.server.common.BaseEntity;
import org.slams.server.user.entity.User;

import javax.persistence.*;

/**
 * Created by yunyun on 2021/12/03.
 */
@Entity
@Table(name="follow")
public class Follow extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY, cascade = CascadeType.ALL)
    @JoinColumn(name = "follower_id", nullable = false, referencedColumnName = "id")
    private User follower;

    @ManyToOne(fetch = FetchType.LAZY, cascade = CascadeType.ALL)
    @JoinColumn(name = "following_id", nullable = false, referencedColumnName = "id")
    private User following;

}