package org.slams.server.user.entity;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.DynamicUpdate;
import org.slams.server.common.BaseEntity;

import javax.persistence.*;

/**
 * Created by yunyun on 2021/12/03.
 */
@Entity
@Builder
@Getter
@NoArgsConstructor
@AllArgsConstructor
@DynamicUpdate
@Table(name="user")
public class User extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name="email", nullable = false, length = 50, unique = true)
    private String email;

    @Column(name="nickname", nullable = false, length = 30)
    private String nickname;

    @Column(name="profile_image")
    private String profileImage;

    @Column(name="description", length = 100)
    private String description;

    @Column(name="role", nullable = false)
    private String role;

    @Column(name="skill")
    private Skill skill;

    @Column(name="position")
    private Position position;

}