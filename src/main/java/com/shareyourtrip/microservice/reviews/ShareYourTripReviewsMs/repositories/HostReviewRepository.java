package com.shareyourtrip.microservice.reviews.ShareYourTripReviewsMs.repositories;

import com.shareyourtrip.microservice.reviews.ShareYourTripReviewsMs.entitites.HostReview;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface HostReviewRepository extends JpaRepository<HostReview, Long> {

    Optional<HostReview> findByBookingRequestId(Long bookingRequestId);

    boolean existsByBookingRequestId(Long bookingRequestId);

    List<HostReview> findByReviewedTravelerId(Long reviewedTravelerId);

    List<HostReview> findByReviewerHostId(Long reviewerHostId);
}