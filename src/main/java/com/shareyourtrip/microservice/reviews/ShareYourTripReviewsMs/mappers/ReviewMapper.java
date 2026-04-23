package com.shareyourtrip.microservice.reviews.ShareYourTripReviewsMs.mappers;

import com.shareyourtrip.microservice.reviews.ShareYourTripReviewsMs.dtos.HostReviewRequestDto;
import com.shareyourtrip.microservice.reviews.ShareYourTripReviewsMs.dtos.HostReviewResponseDto;
import com.shareyourtrip.microservice.reviews.ShareYourTripReviewsMs.dtos.TravelerReviewResponseDto;
import com.shareyourtrip.microservice.reviews.ShareYourTripReviewsMs.entitites.HostReview;
import com.shareyourtrip.microservice.reviews.ShareYourTripReviewsMs.entitites.TravelerReview;

public class ReviewMapper {

    public static HostReviewResponseDto toDTO(HostReview review) {
        return HostReviewResponseDto.builder()
                .id(review.getId())
                .bookingRequestId(review.getBookingRequestId())
                .reviewerHostId(review.getReviewerHostId())
                .reviewedTravelerId(review.getReviewedTravelerId())
                .accommodationId(review.getAccommodationId())
                .rating(review.getRating())
                .comment(review.getComment())
                .createdAt(review.getCreatedAt())
                .reviewType(review.getReviewType())
                .build();
    }

    public static TravelerReviewResponseDto toDTO(TravelerReview review) {
        return TravelerReviewResponseDto.builder()
                .id(review.getId())
                .bookingRequestId(review.getBookingRequestId())
                .reviewerTravelerId(review.getReviewerTravelerId())
                .reviewedHostId(review.getReviewedHostId())
                .rating(review.getRating())
                .comment(review.getComment())
                .createdAt(review.getCreatedAt())
                .reviewType(review.getReviewType())
                .build();
    }


    public static HostReview toEntity(HostReviewRequestDto request) {

        return HostReview.builder()
                .bookingRequestId(request.getBookingRequestId())
                .reviewerHostId(request.getReviewerHostId())
                .reviewedTravelerId(request.getReviewedTravelerId())
                .accommodationId(request.getAccommodationId())
                .rating(request.getRating())
                .comment(request.getComment())
                .build();
    }
}