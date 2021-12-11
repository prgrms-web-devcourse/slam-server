package org.slams.server.court.entity;

import lombok.*;
import org.slams.server.common.BaseEntity;

import javax.persistence.*;
import org.slams.server.court.entity.*;
import org.slams.server.reservation.entity.Reservation;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by dongsung on 2021/12/03.
 */

@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor
@Entity
@Builder
@Table(name = "court")
public class Court extends BaseEntity {
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


    //Court - Reservation 양방향 매핑
    @OneToMany(mappedBy = "court")
    private List<Reservation> reservations=new ArrayList<>();

    public void addReservation(Reservation reservation) {
        reservations.add(reservation);
    }
    public void removeReservation(Reservation reservation) {
        reservations.remove(reservation);
    }

}
