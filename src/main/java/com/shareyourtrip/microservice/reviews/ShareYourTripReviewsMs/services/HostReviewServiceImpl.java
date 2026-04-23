package com.shareyourtrip.microservice.reviews.ShareYourTripReviewsMs.services;

import com.shareyourtrip.microservice.reviews.ShareYourTripReviewsMs.dtos.HostReviewRequestDto;
import com.shareyourtrip.microservice.reviews.ShareYourTripReviewsMs.dtos.HostReviewResponseDto;
import com.shareyourtrip.microservice.reviews.ShareYourTripReviewsMs.dtos.ReviewUpdateDto;
import com.shareyourtrip.microservice.reviews.ShareYourTripReviewsMs.entitites.HostReview;
import com.shareyourtrip.microservice.reviews.ShareYourTripReviewsMs.exceptions.ReviewAlreadyExistsException;
import com.shareyourtrip.microservice.reviews.ShareYourTripReviewsMs.exceptions.ReviewNotFoundException;
import com.shareyourtrip.microservice.reviews.ShareYourTripReviewsMs.mappers.ReviewMapper;
import com.shareyourtrip.microservice.reviews.ShareYourTripReviewsMs.repositories.HostReviewRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
@Transactional
public class HostReviewServiceImpl implements HostReviewService {

    @Autowired
    private HostReviewRepository hostReviewRepository;

    @Override
    public HostReviewResponseDto create(HostReviewRequestDto request) {
        if (hostReviewRepository.existsByBookingRequestId(request.getBookingRequestId())) {
            throw new ReviewAlreadyExistsException(
                    "A host review already exists for bookingRequestId=" + request.getBookingRequestId()
            );
        }

        HostReview savedReview = hostReviewRepository.save(ReviewMapper.toEntity(request));
        return ReviewMapper.toDTO(savedReview);
    }

    @Override
    @Transactional(readOnly = true)
    public HostReviewResponseDto getById(Long id) {
        HostReview review = hostReviewRepository.findById(id)
                .orElseThrow(() -> new ReviewNotFoundException(
                        "Host review not found with id=" + id
                ));

        return ReviewMapper.toDTO(review);
    }

    @Override
    @Transactional(readOnly = true)
    public HostReviewResponseDto getByBookingRequestId(Long bookingRequestId) {
        HostReview review = hostReviewRepository.findByBookingRequestId(bookingRequestId)
                .orElseThrow(() -> new ReviewNotFoundException(
                        "Host review not found with bookingRequestId=" + bookingRequestId
                ));

        return ReviewMapper.toDTO(review);
    }

    @Override
    @Transactional(readOnly = true)
    public List<HostReviewResponseDto> getByReviewedTravelerId(Long travelerId) {
        return hostReviewRepository.findByReviewedTravelerId(travelerId)
                .stream()
                .map(ReviewMapper::toDTO)
                .toList();
    }

    @Override
    @Transactional(readOnly = true)
    public List<HostReviewResponseDto> getByReviewerHostId(Long hostId) {
        return hostReviewRepository.findByReviewerHostId(hostId)
                .stream()
                .map(ReviewMapper::toDTO)
                .toList();
    }

    @Override
    public HostReviewResponseDto update(Long id, ReviewUpdateDto request) {
        HostReview review = hostReviewRepository.findById(id)
                .orElseThrow(() -> new ReviewNotFoundException(
                        "Host review not found with id=" + id
                ));

        if (request.getRating() != null) {
            review.setRating(request.getRating());
        }

        if (request.getComment() != null) {
            review.setComment(request.getComment());
        }

        return ReviewMapper.toDTO(hostReviewRepository.save(review));
    }

    @Override
    public void delete(Long id) {
        HostReview review = hostReviewRepository.findById(id)
                .orElseThrow(() -> new ReviewNotFoundException(
                        "Host review not found with id=" + id
                ));

        hostReviewRepository.delete(review);
    }
}
