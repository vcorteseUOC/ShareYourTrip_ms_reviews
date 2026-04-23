package com.shareyourtrip.microservice.reviews.ShareYourTripReviewsMs.services;

import com.shareyourtrip.microservice.reviews.ShareYourTripReviewsMs.dtos.ReviewUpdateDto;
import com.shareyourtrip.microservice.reviews.ShareYourTripReviewsMs.dtos.TravelerReviewRequestDto;
import com.shareyourtrip.microservice.reviews.ShareYourTripReviewsMs.dtos.TravelerReviewResponseDto;

import java.util.List;

public interface TravelerReviewService {
    TravelerReviewResponseDto create(TravelerReviewRequestDto request);

    TravelerReviewResponseDto getById(Long id);

    TravelerReviewResponseDto getByBookingRequestId(Long bookingRequestId);

    List<TravelerReviewResponseDto> getByReviewedHostId(Long hostId);

    List<TravelerReviewResponseDto> getByReviewerTravelerId(Long travelerId);

    TravelerReviewResponseDto update(Long id, ReviewUpdateDto request);

    void delete(Long id);
}
