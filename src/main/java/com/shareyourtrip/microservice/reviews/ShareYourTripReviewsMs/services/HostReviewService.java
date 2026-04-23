package com.shareyourtrip.microservice.reviews.ShareYourTripReviewsMs.services;

import com.shareyourtrip.microservice.reviews.ShareYourTripReviewsMs.dtos.HostReviewRequestDto;
import com.shareyourtrip.microservice.reviews.ShareYourTripReviewsMs.dtos.HostReviewResponseDto;
import com.shareyourtrip.microservice.reviews.ShareYourTripReviewsMs.dtos.ReviewUpdateDto;

import java.util.List;

public interface HostReviewService {
    HostReviewResponseDto create(HostReviewRequestDto request);

    HostReviewResponseDto getById(Long id);

    HostReviewResponseDto getByBookingRequestId(Long bookingRequestId);

    List<HostReviewResponseDto> getByReviewedTravelerId(Long travelerId);

    List<HostReviewResponseDto> getByReviewerHostId(Long hostId);

    HostReviewResponseDto update(Long id, ReviewUpdateDto request);

    void delete(Long id);
}
