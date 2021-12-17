package org.slams.server.court.dto.response;

import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.slams.server.court.entity.NewCourt;
import org.slams.server.court.entity.Status;
import org.slams.server.court.entity.Texture;

import java.time.LocalDateTime;

@Getter
@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class NewCourtResponse {

	private Long newCourtId;
	private String courtName;
	private double latitude;
	private double longitude;
	private String image;
	private Texture texture;
	private int basketCount;
	private Status status;
	private LocalDateTime createdAt;
	private LocalDateTime updatedAt;

	private NewCourtResponse(Long newCourtId, String courtName, double latitude, double longitude,
							 String image, Texture texture, int basketCount, Status status,
							 LocalDateTime createdAt, LocalDateTime updatedAt) {
		this.newCourtId = newCourtId;
		this.courtName = courtName;
		this.latitude = latitude;
		this.longitude = longitude;
		this.image = image;
		this.texture = texture;
		this.basketCount = basketCount;
		this.status = status;
		this.createdAt = createdAt;
		this.updatedAt = updatedAt;
	}

	public static NewCourtResponse toResponse(NewCourt newCourt){
		return new NewCourtResponse(newCourt.getId(), newCourt.getName(), newCourt.getLatitude(), newCourt.getLongitude(),
			newCourt.getImage(), newCourt.getTexture(), newCourt.getBasketCount(), newCourt.getStatus(),
			newCourt.getCreatedAt(), newCourt.getUpdateAt());
	}

}
