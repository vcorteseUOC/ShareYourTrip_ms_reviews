package com.shareyourtrip.microservice.reviews.ShareYourTripReviewsMs.entitites;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Table;
import jakarta.persistence.UniqueConstraint;
import lombok.*;
import lombok.experimental.SuperBuilder;

@Getter
@Setter
@SuperBuilder
@NoArgsConstructor
@EqualsAndHashCode(callSuper = true)
@Entity
@Table(
        name = "host_reviews",
        schema = "public",
        uniqueConstraints = {
                @UniqueConstraint(name = "host_reviews_booking_request_id_key", columnNames = "booking_request_id")
        }
)
public class HostReview extends Review {

    @Column(name = "host_id", nullable = false)
    private Long reviewerHostId;

    @Column(name = "traveler_id", nullable = false)
    private Long reviewedTravelerId;

    @Column(name = "accommodation_id", nullable = false)
    private Long accommodationId;

    @Override
    public Long getReviewerUserId() {
        return reviewerHostId;
    }

    @Override
    public Long getReviewedUserId() {
        return reviewedTravelerId;
    }

    @Override
    public ReviewType getReviewType() {
        return ReviewType.HOST_TO_TRAVELER;
    }
}
