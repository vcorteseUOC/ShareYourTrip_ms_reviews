package com.shareyourtrip.microservice.reviews.ShareYourTripReviewsMs.entitites;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Table;
import jakarta.persistence.UniqueConstraint;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.experimental.SuperBuilder;

@Getter
@Setter
@SuperBuilder
@NoArgsConstructor
@EqualsAndHashCode(callSuper = true)
@Entity
@Table(
        name = "traveler_reviews",
        schema = "public",
        uniqueConstraints = {
                @UniqueConstraint(name = "traveler_reviews_booking_request_id_key", columnNames = "booking_request_id")
        }
)
public class TravelerReview extends Review {

    @Column(name = "traveler_id", nullable = false)
    private Long reviewerTravelerId;

    @Column(name = "host_id", nullable = false)
    private Long reviewedHostId;

    @Override
    public Long getReviewerUserId() {
        return reviewerTravelerId;
    }

    @Override
    public Long getReviewedUserId() {
        return reviewedHostId;
    }

    @Override
    public ReviewType getReviewType() {
        return ReviewType.TRAVELER_TO_HOST;
    }
}
