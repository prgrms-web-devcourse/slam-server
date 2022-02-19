package org.slams.server.court.entity;

import lombok.*;
import org.slams.server.common.BaseEntity;
import org.slams.server.user.entity.User;

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

    private String image;

    @Column(nullable = false, name = "basket_count")
    private int basketCount;

    @Enumerated(EnumType.STRING)
    private Texture texture;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private Status status;

    @ManyToOne(fetch = FetchType.LAZY, cascade = CascadeType.ALL)
    @JoinColumn(name = "proposer_id", nullable = false, referencedColumnName = "id")
    private User proposer;

    @ManyToOne(fetch = FetchType.LAZY, cascade = CascadeType.ALL)
    @JoinColumn(name = "supervisor_id", referencedColumnName = "id")
    private User supervisor;

    @Builder
    public NewCourt(LocalDateTime createdAt, LocalDateTime updateAt,
                    Long id, String name, double latitude, double longitude, String image,
                    int basketCount, Texture texture, Status status, User proposer) {
        super(createdAt, updateAt);
        this.id = id;
        this.name = name;
        this.latitude = latitude;
        this.longitude = longitude;
        this.image = image;
        this.basketCount = basketCount;
        this.texture = texture;
        this.status = status;
        this.proposer = proposer;
    }

    @PrePersist
    public void prePersist() {
        this.status = this.status == null ? Status.READY : this.status;
    }

    public void acceptNewCourt(User supervisor){
        this.status = Status.ACCEPT;
        this.supervisor = supervisor;
    }

    public void denyNewCourt(User supervisor){
        this.status = Status.DENY;
        this.supervisor = supervisor;
    }

}
