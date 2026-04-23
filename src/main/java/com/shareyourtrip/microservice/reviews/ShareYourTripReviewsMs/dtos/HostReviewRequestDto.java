package com.shareyourtrip.microservice.reviews.ShareYourTripReviewsMs.dtos;

import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class HostReviewRequestDto {

    @NotNull
    private Long bookingRequestId;

    @NotNull
    private Long reviewerHostId;

    @NotNull
    private Long reviewedTravelerId;

    @NotNull
    private Long accommodationId;

    @NotNull
    @Min(1)
    @Max(5)
    private Integer rating;

    private String comment;
}
