package com.shareyourtrip.microservice.reviews.ShareYourTripReviewsMs.controllers;

import com.shareyourtrip.microservice.reviews.ShareYourTripReviewsMs.dtos.ReviewUpdateDto;
import com.shareyourtrip.microservice.reviews.ShareYourTripReviewsMs.dtos.TravelerReviewRequestDto;
import com.shareyourtrip.microservice.reviews.ShareYourTripReviewsMs.dtos.TravelerReviewResponseDto;
import com.shareyourtrip.microservice.reviews.ShareYourTripReviewsMs.services.TravelerReviewService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/traveler-reviews")
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
