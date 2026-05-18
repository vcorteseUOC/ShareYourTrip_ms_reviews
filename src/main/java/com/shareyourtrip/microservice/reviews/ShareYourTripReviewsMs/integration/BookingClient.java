package com.shareyourtrip.microservice.reviews.ShareYourTripReviewsMs.integration;

import com.shareyourtrip.microservice.reviews.ShareYourTripReviewsMs.configuration.FeignConfig;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.List;
import java.util.Map;

@FeignClient(
        name = "booking-service",
        url = "${services.bookings.base-url}",
        configuration = FeignConfig.class
)
public interface BookingClient {

    @GetMapping("/booking-requests/accommodations")
    Map<Long, List<Long>> getBookingRequestIdsByAccommodationIds(@RequestParam("accommodationIds") List<Long> accommodationIdsStr);
}
