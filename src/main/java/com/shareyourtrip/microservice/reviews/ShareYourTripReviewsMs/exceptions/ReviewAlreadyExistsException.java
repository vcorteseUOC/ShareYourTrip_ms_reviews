package com.shareyourtrip.microservice.reviews.ShareYourTripReviewsMs.exceptions;

public class ReviewAlreadyExistsException extends RuntimeException {
    public ReviewAlreadyExistsException(String message) {
        super(message);
    }
}
