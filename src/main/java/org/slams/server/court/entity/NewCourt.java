package org.slams.server.court.entity;

import lombok.*;
import org.slams.server.common.BaseEntity;

import javax.persistence.*;
import java.time.LocalDateTime;
import java.util.UUID;

@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor
@Entity
@Table(name = "new_court")
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
    private String medialUrl;

    @Column(nullable = false, name = "basket_count")
    private int basketCount;

    @Enumerated(EnumType.STRING)
    private Texture texture;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private Status status;

    @Builder
    public NewCourt(LocalDateTime createdAt, LocalDateTime updateAt,
                    Long id, String name, double latitude, double longitude, String image,
                    int basketCount, Texture texture, Status status) {
        super(createdAt, updateAt);
        this.id = id;
        this.name = name;
        this.latitude = latitude;
        this.longitude = longitude;
        this.image = image;
        this.basketCount = basketCount;
        this.texture = texture;
        this.status = status;
    }

    @PrePersist
    public void prePersist() {
        this.status = this.status == null ? Status.READY : this.status;
    }

    public void acceptNewCourt(){
        this.status = Status.ACCEPT;
    }

    public void denyNewCourt(){
        this.status = Status.DENY;
    }

}
