package com.shareyourtrip.microservice.reviews.ShareYourTripReviewsMs.services;

import com.shareyourtrip.microservice.reviews.ShareYourTripReviewsMs.dtos.ReviewUpdateDto;
import com.shareyourtrip.microservice.reviews.ShareYourTripReviewsMs.dtos.TravelerReviewRequestDto;
import com.shareyourtrip.microservice.reviews.ShareYourTripReviewsMs.dtos.TravelerReviewResponseDto;
import com.shareyourtrip.microservice.reviews.ShareYourTripReviewsMs.entitites.TravelerReview;
import com.shareyourtrip.microservice.reviews.ShareYourTripReviewsMs.exceptions.ReviewAlreadyExistsException;
import com.shareyourtrip.microservice.reviews.ShareYourTripReviewsMs.exceptions.ReviewNotFoundException;
import com.shareyourtrip.microservice.reviews.ShareYourTripReviewsMs.mappers.ReviewMapper;
import com.shareyourtrip.microservice.reviews.ShareYourTripReviewsMs.repositories.TravelerReviewRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
@Transactional
public class TravelerReviewServiceImpl implements TravelerReviewService {

    @Autowired
    private final TravelerReviewRepository travelerReviewRepository;

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
}
