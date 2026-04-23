package com.shareyourtrip.microservice.reviews.ShareYourTripReviewsMs.repositories;

import com.shareyourtrip.microservice.reviews.ShareYourTripReviewsMs.entitites.TravelerReview;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface TravelerReviewRepository extends JpaRepository<TravelerReview, Long> {

    Optional<TravelerReview> findByBookingRequestId(Long bookingRequestId);

    boolean existsByBookingRequestId(Long bookingRequestId);

    List<TravelerReview> findByReviewedHostId(Long reviewedHostId);

    List<TravelerReview> findByReviewerTravelerId(Long reviewerTravelerId);
}