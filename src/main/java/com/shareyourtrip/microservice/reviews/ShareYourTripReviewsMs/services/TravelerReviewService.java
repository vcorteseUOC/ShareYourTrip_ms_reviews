package com.shareyourtrip.microservice.reviews.ShareYourTripReviewsMs.services;

import com.shareyourtrip.microservice.reviews.ShareYourTripReviewsMs.dtos.AccommodationRatingDto;
import com.shareyourtrip.microservice.reviews.ShareYourTripReviewsMs.dtos.ReviewUpdateDto;
import com.shareyourtrip.microservice.reviews.ShareYourTripReviewsMs.dtos.TravelerRatingDto;
import com.shareyourtrip.microservice.reviews.ShareYourTripReviewsMs.dtos.TravelerReviewRequestDto;
import com.shareyourtrip.microservice.reviews.ShareYourTripReviewsMs.dtos.TravelerReviewResponseDto;

import java.util.List;
import java.util.Map;

public interface TravelerReviewService {
    TravelerReviewResponseDto create(TravelerReviewRequestDto request);

    TravelerReviewResponseDto getById(Long id);

    TravelerReviewResponseDto getByBookingRequestId(Long bookingRequestId);

    List<TravelerReviewResponseDto> getByReviewedHostId(Long hostId);

    List<TravelerReviewResponseDto> getByReviewerTravelerId(Long travelerId);

    TravelerReviewResponseDto update(Long id, ReviewUpdateDto request);

    void delete(Long id);

    Map<Long, AccommodationRatingDto> getAverageRatingsByAccommodationIds(List<Long> accommodationIds);

    Map<Long, TravelerRatingDto> getAverageRatingsByTravelerIds(List<Long> travelerIds);

    Map<Long, Boolean> getExistsByBookingRequestIds(List<Long> bookingRequestIds);
}
