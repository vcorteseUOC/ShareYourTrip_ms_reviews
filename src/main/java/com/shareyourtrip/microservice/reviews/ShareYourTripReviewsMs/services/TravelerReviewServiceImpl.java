package com.shareyourtrip.microservice.reviews.ShareYourTripReviewsMs.services;

import com.shareyourtrip.microservice.reviews.ShareYourTripReviewsMs.dtos.AccommodationRatingDto;
import com.shareyourtrip.microservice.reviews.ShareYourTripReviewsMs.dtos.ReviewUpdateDto;
import com.shareyourtrip.microservice.reviews.ShareYourTripReviewsMs.dtos.TravelerRatingDto;
import com.shareyourtrip.microservice.reviews.ShareYourTripReviewsMs.dtos.TravelerReviewRequestDto;
import com.shareyourtrip.microservice.reviews.ShareYourTripReviewsMs.dtos.TravelerReviewResponseDto;
import com.shareyourtrip.microservice.reviews.ShareYourTripReviewsMs.entitites.TravelerReview;
import com.shareyourtrip.microservice.reviews.ShareYourTripReviewsMs.exceptions.ReviewAlreadyExistsException;
import com.shareyourtrip.microservice.reviews.ShareYourTripReviewsMs.exceptions.ReviewNotFoundException;
import com.shareyourtrip.microservice.reviews.ShareYourTripReviewsMs.integration.BookingClient;
import com.shareyourtrip.microservice.reviews.ShareYourTripReviewsMs.mappers.ReviewMapper;
import com.shareyourtrip.microservice.reviews.ShareYourTripReviewsMs.repositories.TravelerReviewRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Transactional
public class TravelerReviewServiceImpl implements TravelerReviewService {

    @Autowired
    private TravelerReviewRepository travelerReviewRepository;

    @Autowired
    private BookingClient bookingClient;

    @Override
    public TravelerReviewResponseDto create(TravelerReviewRequestDto request) {
        if (travelerReviewRepository.existsByBookingRequestId(request.getBookingRequestId())) {
            throw new ReviewAlreadyExistsException(
                    "A traveler review already exists for bookingRequestId=" + request.getBookingRequestId()
            );
        }

        TravelerReview review = TravelerReview.builder()
                .bookingRequestId(request.getBookingRequestId())
                .reviewerTravelerId(request.getReviewerTravelerId())
                .reviewedHostId(request.getReviewedHostId())
                .rating(request.getRating())
                .comment(request.getComment())
                .build();

        return ReviewMapper.toDTO(travelerReviewRepository.save(review));
    }

    @Override
    @Transactional(readOnly = true)
    public TravelerReviewResponseDto getById(Long id) {
        TravelerReview review = travelerReviewRepository.findById(id)
                .orElseThrow(() -> new ReviewNotFoundException(
                        "Traveler review not found with id=" + id
                ));

        return ReviewMapper.toDTO(review);
    }

    @Override
    @Transactional(readOnly = true)
    public TravelerReviewResponseDto getByBookingRequestId(Long bookingRequestId) {
        TravelerReview review = travelerReviewRepository.findByBookingRequestId(bookingRequestId)
                .orElseThrow(() -> new ReviewNotFoundException(
                        "Traveler review not found with bookingRequestId=" + bookingRequestId
                ));

        return ReviewMapper.toDTO(review);
    }

    @Override
    @Transactional(readOnly = true)
    public List<TravelerReviewResponseDto> getByReviewedHostId(Long hostId) {
        return travelerReviewRepository.findByReviewedHostId(hostId)
                .stream()
                .map(ReviewMapper::toDTO)
                .toList();
    }

    @Override
    @Transactional(readOnly = true)
    public List<TravelerReviewResponseDto> getByReviewerTravelerId(Long travelerId) {
        return travelerReviewRepository.findByReviewerTravelerId(travelerId)
                .stream()
                .map(ReviewMapper::toDTO)
                .toList();
    }

    @Override
    public TravelerReviewResponseDto update(Long id, ReviewUpdateDto request) {
        TravelerReview review = travelerReviewRepository.findById(id)
                .orElseThrow(() -> new ReviewNotFoundException(
                        "Traveler review not found with id=" + id
                ));

        if (request.getRating() != null) {
            review.setRating(request.getRating());
        }

        if (request.getComment() != null) {
            review.setComment(request.getComment());
        }

        return ReviewMapper.toDTO(travelerReviewRepository.save(review));
    }

    @Override
    public void delete(Long id) {
        TravelerReview review = travelerReviewRepository.findById(id)
                .orElseThrow(() -> new ReviewNotFoundException(
                        "Traveler review not found with id=" + id
                ));

        travelerReviewRepository.delete(review);
    }

    @Override
    @Transactional(readOnly = true)
    public Map<Long, AccommodationRatingDto> getAverageRatingsByAccommodationIds(List<Long> accommodationIds) {
        if (accommodationIds == null || accommodationIds.isEmpty()) {
            return new HashMap<>();
        }

        // Obtener booking_request_ids por accommodation_ids del microservicio de bookings
        Map<Long, List<Long>> bookingIdsByAccommodation = bookingClient.getBookingRequestIdsByAccommodationIds(accommodationIds);

        // Obtener todas las reviews para todos los booking_request_ids
        List<Long> allBookingIds = bookingIdsByAccommodation.values().stream()
                .flatMap(List::stream)
                .toList();

        if (allBookingIds.isEmpty()) {
            // Si no hay bookings, devolver ratings con 0 para todos los accommodations
            return accommodationIds.stream()
                    .collect(Collectors.toMap(
                            id -> id,
                            id -> AccommodationRatingDto.builder()
                                    .accommodationId(id)
                                    .averageRating(0.0)
                                    .reviewCount(0)
                                    .build()
                    ));
        }

        List<TravelerReview> reviews = travelerReviewRepository.findByBookingRequestIdIn(allBookingIds);

        // Agrupar reviews por booking_request_id
        Map<Long, TravelerReview> reviewsByBookingId = reviews.stream()
                .collect(Collectors.toMap(
                        TravelerReview::getBookingRequestId,
                        review -> review,
                        (existing, replacement) -> existing
                ));

        // Calcular rating promedio por accommodation
        Map<Long, AccommodationRatingDto> result = new HashMap<>();
        for (Long accommodationId : accommodationIds) {
            List<Long> bookingIds = bookingIdsByAccommodation.getOrDefault(accommodationId, List.of());
            
            if (bookingIds.isEmpty()) {
                result.put(accommodationId, AccommodationRatingDto.builder()
                        .accommodationId(accommodationId)
                        .averageRating(0.0)
                        .reviewCount(0)
                        .build());
            } else {
                List<Integer> ratings = bookingIds.stream()
                        .map(reviewsByBookingId::get)
                        .filter(review -> review != null)
                        .map(TravelerReview::getRating)
                        .toList();

                if (ratings.isEmpty()) {
                    result.put(accommodationId, AccommodationRatingDto.builder()
                            .accommodationId(accommodationId)
                            .averageRating(0.0)
                            .reviewCount(0)
                            .build());
                } else {
                    double average = ratings.stream()
                            .mapToInt(Integer::intValue)
                            .average()
                            .orElse(0.0);

                    result.put(accommodationId, AccommodationRatingDto.builder()
                            .accommodationId(accommodationId)
                            .averageRating(average)
                            .reviewCount(ratings.size())
                            .build());
                }
            }
        }

        return result;
    }

    @Override
    @Transactional(readOnly = true)
    public Map<Long, TravelerRatingDto> getAverageRatingsByTravelerIds(List<Long> travelerIds) {
        if (travelerIds == null || travelerIds.isEmpty()) {
            return new HashMap<>();
        }

        // Obtener todas las reviews para los travelerIds
        List<TravelerReview> reviews = travelerReviewRepository.findByReviewerTravelerIdIn(travelerIds);

        // Agrupar reviews por reviewer_traveler_id
        Map<Long, List<TravelerReview>> reviewsByTravelerId = reviews.stream()
                .collect(Collectors.groupingBy(TravelerReview::getReviewerTravelerId));

        // Calcular rating promedio por traveler
        Map<Long, TravelerRatingDto> result = new HashMap<>();
        for (Long travelerId : travelerIds) {
            List<TravelerReview> travelerReviews = reviewsByTravelerId.getOrDefault(travelerId, List.of());
            
            if (travelerReviews.isEmpty()) {
                result.put(travelerId, TravelerRatingDto.builder()
                        .travelerId(travelerId)
                        .averageRating(0.0)
                        .reviewCount(0)
                        .build());
            } else {
                List<Integer> ratings = travelerReviews.stream()
                        .map(TravelerReview::getRating)
                        .toList();

                double average = ratings.stream()
                        .mapToInt(Integer::intValue)
                        .average()
                        .orElse(0.0);

                result.put(travelerId, TravelerRatingDto.builder()
                        .travelerId(travelerId)
                        .averageRating(average)
                        .reviewCount(ratings.size())
                        .build());
            }
        }

        return result;
    }

    @Override
    @Transactional(readOnly = true)
    public Map<Long, Boolean> getExistsByBookingRequestIds(List<Long> bookingRequestIds) {
        if (bookingRequestIds == null || bookingRequestIds.isEmpty()) {
            return new HashMap<>();
        }

        List<TravelerReview> reviews = travelerReviewRepository.findByBookingRequestIdIn(bookingRequestIds);

        Map<Long, Boolean> result = new HashMap<>();
        for (Long bookingId : bookingRequestIds) {
            boolean exists = reviews.stream()
                    .anyMatch(review -> review.getBookingRequestId().equals(bookingId));
            result.put(bookingId, exists);
        }

        return result;
    }
}
