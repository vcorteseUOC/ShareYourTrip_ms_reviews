package com.shareyourtrip.microservice.reviews.ShareYourTripReviewsMs.controllers;

import com.shareyourtrip.microservice.reviews.ShareYourTripReviewsMs.dtos.AccommodationRatingDto;
import com.shareyourtrip.microservice.reviews.ShareYourTripReviewsMs.dtos.ReviewUpdateDto;
import com.shareyourtrip.microservice.reviews.ShareYourTripReviewsMs.dtos.TravelerRatingDto;
import com.shareyourtrip.microservice.reviews.ShareYourTripReviewsMs.dtos.TravelerReviewRequestDto;
import com.shareyourtrip.microservice.reviews.ShareYourTripReviewsMs.dtos.TravelerReviewResponseDto;
import com.shareyourtrip.microservice.reviews.ShareYourTripReviewsMs.services.TravelerReviewService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.util.Arrays;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/traveler-reviews")
@RequiredArgsConstructor
public class TravelerReviewController {

    private final TravelerReviewService travelerReviewService;

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public TravelerReviewResponseDto create(@Valid @RequestBody TravelerReviewRequestDto request) {
        return travelerReviewService.create(request);
    }

    @GetMapping("/{id}")
    public TravelerReviewResponseDto getById(@PathVariable Long id) {
        return travelerReviewService.getById(id);
    }

    @GetMapping("/booking/{bookingRequestId}")
    public TravelerReviewResponseDto getByBookingRequestId(@PathVariable Long bookingRequestId) {
        return travelerReviewService.getByBookingRequestId(bookingRequestId);
    }

    @GetMapping("/reviewed-host/{hostId}")
    public List<TravelerReviewResponseDto> getByReviewedHostId(@PathVariable Long hostId) {
        return travelerReviewService.getByReviewedHostId(hostId);
    }

    @GetMapping("/reviewer-traveler/{travelerId}")
    public List<TravelerReviewResponseDto> getByReviewerTravelerId(@PathVariable Long travelerId) {
        return travelerReviewService.getByReviewerTravelerId(travelerId);
    }

    @GetMapping("/accommodation-ratings")
    public Map<Long, AccommodationRatingDto> getAverageRatingsByAccommodationIds(@RequestParam("accommodationIds") String accommodationIdsStr) {
        List<Long> accommodationIds = Arrays.stream(accommodationIdsStr.split(","))
                .map(String::trim)
                .map(Long::parseLong)
                .toList();
        return travelerReviewService.getAverageRatingsByAccommodationIds(accommodationIds);
    }

    @GetMapping("/traveler-ratings")
    public Map<Long, TravelerRatingDto> getAverageRatingsByTravelerIds(@RequestParam("travelerIds") String travelerIdsStr) {
        List<Long> travelerIds = Arrays.stream(travelerIdsStr.split(","))
                .map(String::trim)
                .map(Long::parseLong)
                .toList();
        return travelerReviewService.getAverageRatingsByTravelerIds(travelerIds);
    }

    @GetMapping("/exists")
    public Map<Long, Boolean> getExistsByBookingRequestIds(@RequestParam("bookingRequestIds") String bookingRequestIdsStr) {
        List<Long> bookingRequestIds = Arrays.stream(bookingRequestIdsStr.split(","))
                .map(String::trim)
                .map(Long::parseLong)
                .toList();
        return travelerReviewService.getExistsByBookingRequestIds(bookingRequestIds);
    }

    @PatchMapping("/{id}")
    public TravelerReviewResponseDto update(@PathVariable Long id,
                                            @Valid @RequestBody ReviewUpdateDto request) {
        return travelerReviewService.update(id, request);
    }

    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void delete(@PathVariable Long id) {
        travelerReviewService.delete(id);
    }
}
