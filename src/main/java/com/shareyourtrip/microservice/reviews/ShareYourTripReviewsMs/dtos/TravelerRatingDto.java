package com.shareyourtrip.microservice.reviews.ShareYourTripReviewsMs.dtos;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class TravelerRatingDto {
    private Long travelerId;
    private Double averageRating;
    private Integer reviewCount;
}
