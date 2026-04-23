package com.shareyourtrip.microservice.reviews.ShareYourTripReviewsMs.controllers;

import com.shareyourtrip.microservice.reviews.ShareYourTripReviewsMs.dtos.HostReviewRequestDto;
import com.shareyourtrip.microservice.reviews.ShareYourTripReviewsMs.dtos.HostReviewResponseDto;
import com.shareyourtrip.microservice.reviews.ShareYourTripReviewsMs.dtos.ReviewUpdateDto;
import com.shareyourtrip.microservice.reviews.ShareYourTripReviewsMs.services.HostReviewService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/host-reviews")
@RequiredArgsConstructor
public class HostReviewController {

    private final HostReviewService hostReviewService;

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public HostReviewResponseDto create(@Valid @RequestBody HostReviewRequestDto request) {
        return hostReviewService.create(request);
    }

    @GetMapping("/{id}")
    public HostReviewResponseDto getById(@PathVariable Long id) {
        return hostReviewService.getById(id);
    }

    @GetMapping("/booking/{bookingRequestId}")
    public HostReviewResponseDto getByBookingRequestId(@PathVariable Long bookingRequestId) {
        return hostReviewService.getByBookingRequestId(bookingRequestId);
    }

    @GetMapping("/reviewed-traveler/{travelerId}")
    public List<HostReviewResponseDto> getByReviewedTravelerId(@PathVariable Long travelerId) {
        return hostReviewService.getByReviewedTravelerId(travelerId);
    }

    @GetMapping("/reviewer-host/{hostId}")
    public List<HostReviewResponseDto> getByReviewerHostId(@PathVariable Long hostId) {
        return hostReviewService.getByReviewerHostId(hostId);
    }

    @PatchMapping("/{id}")
    public HostReviewResponseDto update(@PathVariable Long id,
                                     @Valid @RequestBody ReviewUpdateDto request) {
        return hostReviewService.update(id, request);
    }

    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void delete(@PathVariable Long id) {
        hostReviewService.delete(id);
    }
}
