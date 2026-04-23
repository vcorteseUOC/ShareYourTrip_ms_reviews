package com.shareyourtrip.microservice.reviews.ShareYourTripReviewsMs.exceptions;

public class ReviewNotFoundException extends RuntimeException {

    public ReviewNotFoundException(String message) {
        super(message);
    }
}