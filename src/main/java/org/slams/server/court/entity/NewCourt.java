package org.slams.server.court.entity;

import lombok.*;
import org.slams.server.common.BaseEntity;

import javax.persistence.*;

/**
 * Created by dongsung on 2021/12/03.
 */

@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor
@Entity
@Builder
@Table(name = "newCourt")
public class NewCourt extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name="id")
    private Long id;

    @Column(nullable = false,length=50)
    private String name;

    @Column(nullable = false)
    private double latitude;

    @Column(nullable = false)
    private double longitude;

    @Column(nullable = false,length=255)
    private String image;

    @Column(nullable = false, name = "basket_count")
    private int basketCount;

    @Enumerated(EnumType.STRING)
    private Texture texture;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private Status status;
}
