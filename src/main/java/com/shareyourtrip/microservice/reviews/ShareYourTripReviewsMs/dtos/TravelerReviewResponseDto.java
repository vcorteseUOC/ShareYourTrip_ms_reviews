package com.shareyourtrip.microservice.reviews.ShareYourTripReviewsMs.dtos;

import com.shareyourtrip.microservice.reviews.ShareYourTripReviewsMs.entitites.ReviewType;
import lombok.Builder;
import lombok.Getter;

import java.time.LocalDateTime;

@Getter
@Builder
public class TravelerReviewResponseDto {

    private Long id;
    private Long bookingRequestId;
    private Long reviewerTravelerId;
    private Long reviewedHostId;
    private Integer rating;
    private String comment;
    private LocalDateTime createdAt;
    private ReviewType reviewType;
}
