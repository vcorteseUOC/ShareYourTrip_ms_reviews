package com.shareyourtrip.microservice.reviews.ShareYourTripReviewsMs.services;

import com.shareyourtrip.microservice.reviews.ShareYourTripReviewsMs.dtos.AccommodationRatingDto;
import com.shareyourtrip.microservice.reviews.ShareYourTripReviewsMs.dtos.ReviewUpdateDto;
import com.shareyourtrip.microservice.reviews.ShareYourTripReviewsMs.dtos.TravelerRatingDto;
import com.shareyourtrip.microservice.reviews.ShareYourTripReviewsMs.dtos.TravelerReviewRequestDto;
import com.shareyourtrip.microservice.reviews.ShareYourTripReviewsMs.dtos.TravelerReviewResponseDto;
import com.shareyourtrip.microservice.reviews.ShareYourTripReviewsMs.entitites.ReviewType;
import com.shareyourtrip.microservice.reviews.ShareYourTripReviewsMs.entitites.TravelerReview;
import com.shareyourtrip.microservice.reviews.ShareYourTripReviewsMs.exceptions.ReviewAlreadyExistsException;
import com.shareyourtrip.microservice.reviews.ShareYourTripReviewsMs.exceptions.ReviewNotFoundException;
import com.shareyourtrip.microservice.reviews.ShareYourTripReviewsMs.integration.BookingClient;
import com.shareyourtrip.microservice.reviews.ShareYourTripReviewsMs.repositories.TravelerReviewRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDateTime;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.Optional;

import org.springframework.test.util.ReflectionTestUtils;

import static org.assertj.core.api.Assertions.*;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
@DisplayName("TravelerReviewServiceImpl - Tests unitarios")
class TravelerReviewServiceImplTest {

    @Mock
    private TravelerReviewRepository travelerReviewRepository;

    @Mock
    private BookingClient bookingClient;

    @InjectMocks
    private TravelerReviewServiceImpl travelerReviewService;

    private TravelerReview sampleReview;
    private TravelerReviewRequestDto sampleRequest;

    @BeforeEach
    void setUp() {
        sampleReview = TravelerReview.builder()
                .id(1L)
                .bookingRequestId(100L)
                .reviewerTravelerId(300L)
                .reviewedHostId(200L)
                .rating(5)
                .comment("Gran anfitrión")
                .createdAt(LocalDateTime.now())
                .build();

        sampleRequest = new TravelerReviewRequestDto();
        sampleRequest.setBookingRequestId(100L);
        sampleRequest.setReviewerTravelerId(300L);
        sampleRequest.setReviewedHostId(200L);
        sampleRequest.setRating(5);
        sampleRequest.setComment("Gran anfitrión");
    }

    @Nested
    @DisplayName("create")
    class Create {

        @Test
        @DisplayName("Debe crear review correctamente")
        void shouldCreateReview() {
            when(travelerReviewRepository.existsByBookingRequestId(100L)).thenReturn(false);
            when(travelerReviewRepository.save(any(TravelerReview.class))).thenAnswer(invocation -> {
                TravelerReview r = invocation.getArgument(0);
                ReflectionTestUtils.setField(r, "id", 1L);
                ReflectionTestUtils.setField(r, "createdAt", LocalDateTime.now());
                return r;
            });

            TravelerReviewResponseDto result = travelerReviewService.create(sampleRequest);

            assertThat(result).isNotNull();
            assertThat(result.getBookingRequestId()).isEqualTo(100L);
            assertThat(result.getRating()).isEqualTo(5);
            assertThat(result.getReviewType()).isEqualTo(ReviewType.TRAVELER_TO_HOST);
        }

        @Test
        @DisplayName("Debe lanzar ReviewAlreadyExistsException si ya existe")
        void shouldThrowWhenAlreadyExists() {
            when(travelerReviewRepository.existsByBookingRequestId(100L)).thenReturn(true);

            assertThatThrownBy(() -> travelerReviewService.create(sampleRequest))
                    .isInstanceOf(ReviewAlreadyExistsException.class)
                    .hasMessageContaining("100");

            verify(travelerReviewRepository, never()).save(any());
        }
    }

    @Nested
    @DisplayName("getById")
    class GetById {

        @Test
        @DisplayName("Debe retornar DTO cuando existe")
        void shouldReturnDto() {
            when(travelerReviewRepository.findById(1L)).thenReturn(Optional.of(sampleReview));

            TravelerReviewResponseDto result = travelerReviewService.getById(1L);

            assertThat(result).isNotNull();
            assertThat(result.getId()).isEqualTo(1L);
        }

        @Test
        @DisplayName("Debe lanzar ReviewNotFoundException cuando no existe")
        void shouldThrowWhenNotFound() {
            when(travelerReviewRepository.findById(99L)).thenReturn(Optional.empty());

            assertThatThrownBy(() -> travelerReviewService.getById(99L))
                    .isInstanceOf(ReviewNotFoundException.class)
                    .hasMessageContaining("99");
        }
    }

    @Nested
    @DisplayName("getByBookingRequestId")
    class GetByBookingRequestId {

        @Test
        @DisplayName("Debe retornar DTO cuando existe")
        void shouldReturnDto() {
            when(travelerReviewRepository.findByBookingRequestId(100L)).thenReturn(Optional.of(sampleReview));

            TravelerReviewResponseDto result = travelerReviewService.getByBookingRequestId(100L);

            assertThat(result).isNotNull();
            assertThat(result.getBookingRequestId()).isEqualTo(100L);
        }

        @Test
        @DisplayName("Debe lanzar ReviewNotFoundException cuando no existe")
        void shouldThrowWhenNotFound() {
            when(travelerReviewRepository.findByBookingRequestId(99L)).thenReturn(Optional.empty());

            assertThatThrownBy(() -> travelerReviewService.getByBookingRequestId(99L))
                    .isInstanceOf(ReviewNotFoundException.class);
        }
    }

    @Nested
    @DisplayName("getByReviewedHostId")
    class GetByReviewedHostId {

        @Test
        @DisplayName("Debe retornar reviews del host reseñado")
        void shouldReturnReviews() {
            when(travelerReviewRepository.findByReviewedHostId(200L)).thenReturn(List.of(sampleReview));

            List<TravelerReviewResponseDto> result = travelerReviewService.getByReviewedHostId(200L);

            assertThat(result).hasSize(1);
            assertThat(result.get(0).getReviewedHostId()).isEqualTo(200L);
        }

        @Test
        @DisplayName("Debe retornar lista vacía si no hay reviews")
        void shouldReturnEmptyList() {
            when(travelerReviewRepository.findByReviewedHostId(999L)).thenReturn(Collections.emptyList());

            List<TravelerReviewResponseDto> result = travelerReviewService.getByReviewedHostId(999L);

            assertThat(result).isEmpty();
        }
    }

    @Nested
    @DisplayName("getByReviewerTravelerId")
    class GetByReviewerTravelerId {

        @Test
        @DisplayName("Debe retornar reviews del viajero reseñador")
        void shouldReturnReviews() {
            when(travelerReviewRepository.findByReviewerTravelerId(300L)).thenReturn(List.of(sampleReview));

            List<TravelerReviewResponseDto> result = travelerReviewService.getByReviewerTravelerId(300L);

            assertThat(result).hasSize(1);
            assertThat(result.get(0).getReviewerTravelerId()).isEqualTo(300L);
        }
    }

    @Nested
    @DisplayName("update")
    class Update {

        @Test
        @DisplayName("Debe actualizar rating y comment")
        void shouldUpdateRatingAndComment() {
            ReviewUpdateDto updateDto = new ReviewUpdateDto();
            updateDto.setRating(3);
            updateDto.setComment("Regular");

            when(travelerReviewRepository.findById(1L)).thenReturn(Optional.of(sampleReview));
            when(travelerReviewRepository.save(any(TravelerReview.class)))
                    .thenAnswer(invocation -> invocation.getArgument(0));

            travelerReviewService.update(1L, updateDto);

            assertThat(sampleReview.getRating()).isEqualTo(3);
            assertThat(sampleReview.getComment()).isEqualTo("Regular");
        }

        @Test
        @DisplayName("Debe actualizar solo comment si rating es null")
        void shouldUpdateOnlyComment() {
            ReviewUpdateDto updateDto = new ReviewUpdateDto();
            updateDto.setComment("Actualizado");

            when(travelerReviewRepository.findById(1L)).thenReturn(Optional.of(sampleReview));
            when(travelerReviewRepository.save(any(TravelerReview.class)))
                    .thenAnswer(invocation -> invocation.getArgument(0));

            travelerReviewService.update(1L, updateDto);

            assertThat(sampleReview.getRating()).isEqualTo(5); // sin cambios
            assertThat(sampleReview.getComment()).isEqualTo("Actualizado");
        }

        @Test
        @DisplayName("Debe lanzar ReviewNotFoundException si no existe")
        void shouldThrowWhenNotFound() {
            ReviewUpdateDto updateDto = new ReviewUpdateDto();
            updateDto.setRating(1);

            when(travelerReviewRepository.findById(99L)).thenReturn(Optional.empty());

            assertThatThrownBy(() -> travelerReviewService.update(99L, updateDto))
                    .isInstanceOf(ReviewNotFoundException.class);
        }
    }

    @Nested
    @DisplayName("delete")
    class Delete {

        @Test
        @DisplayName("Debe eliminar review existente")
        void shouldDeleteReview() {
            when(travelerReviewRepository.findById(1L)).thenReturn(Optional.of(sampleReview));

            travelerReviewService.delete(1L);

            verify(travelerReviewRepository).delete(sampleReview);
        }

        @Test
        @DisplayName("Debe lanzar ReviewNotFoundException si no existe")
        void shouldThrowWhenNotFound() {
            when(travelerReviewRepository.findById(99L)).thenReturn(Optional.empty());

            assertThatThrownBy(() -> travelerReviewService.delete(99L))
                    .isInstanceOf(ReviewNotFoundException.class);

            verify(travelerReviewRepository, never()).delete(any());
        }
    }

    @Nested
    @DisplayName("getAverageRatingsByAccommodationIds")
    class GetAverageRatingsByAccommodationIds {

        @Test
        @DisplayName("Debe calcular rating promedio por alojamiento")
        void shouldCalculateAverageRatings() {
            TravelerReview review1 = TravelerReview.builder()
                    .id(1L).bookingRequestId(101L).reviewerTravelerId(300L).reviewedHostId(200L)
                    .rating(4).createdAt(LocalDateTime.now()).build();
            TravelerReview review2 = TravelerReview.builder()
                    .id(2L).bookingRequestId(102L).reviewerTravelerId(301L).reviewedHostId(200L)
                    .rating(5).createdAt(LocalDateTime.now()).build();

            when(bookingClient.getBookingRequestIdsByAccommodationIds(List.of(10L)))
                    .thenReturn(Map.of(10L, List.of(101L, 102L)));
            when(travelerReviewRepository.findByBookingRequestIdIn(List.of(101L, 102L)))
                    .thenReturn(List.of(review1, review2));

            Map<Long, AccommodationRatingDto> result =
                    travelerReviewService.getAverageRatingsByAccommodationIds(List.of(10L));

            assertThat(result).hasSize(1);
            assertThat(result.get(10L).getAverageRating()).isEqualTo(4.5);
            assertThat(result.get(10L).getReviewCount()).isEqualTo(2);
        }

        @Test
        @DisplayName("Debe retornar 0.0 cuando no hay bookings para el alojamiento")
        void shouldReturnZeroWhenNoBookings() {
            when(bookingClient.getBookingRequestIdsByAccommodationIds(List.of(10L)))
                    .thenReturn(Map.of(10L, Collections.emptyList()));

            Map<Long, AccommodationRatingDto> result =
                    travelerReviewService.getAverageRatingsByAccommodationIds(List.of(10L));

            assertThat(result.get(10L).getAverageRating()).isEqualTo(0.0);
            assertThat(result.get(10L).getReviewCount()).isEqualTo(0);
        }

        @Test
        @DisplayName("Debe retornar 0.0 cuando hay bookings pero no reviews")
        void shouldReturnZeroWhenNoReviews() {
            when(bookingClient.getBookingRequestIdsByAccommodationIds(List.of(10L)))
                    .thenReturn(Map.of(10L, List.of(101L)));
            when(travelerReviewRepository.findByBookingRequestIdIn(List.of(101L)))
                    .thenReturn(Collections.emptyList());

            Map<Long, AccommodationRatingDto> result =
                    travelerReviewService.getAverageRatingsByAccommodationIds(List.of(10L));

            assertThat(result.get(10L).getAverageRating()).isEqualTo(0.0);
            assertThat(result.get(10L).getReviewCount()).isEqualTo(0);
        }

        @Test
        @DisplayName("Debe retornar mapa vacío para lista vacía de IDs")
        void shouldReturnEmptyMapForEmptyList() {
            Map<Long, AccommodationRatingDto> result =
                    travelerReviewService.getAverageRatingsByAccommodationIds(Collections.emptyList());

            assertThat(result).isEmpty();
        }

        @Test
        @DisplayName("Debe retornar mapa vacío para lista null")
        void shouldReturnEmptyMapForNullList() {
            Map<Long, AccommodationRatingDto> result =
                    travelerReviewService.getAverageRatingsByAccommodationIds(null);

            assertThat(result).isEmpty();
        }
    }

    @Nested
    @DisplayName("getAverageRatingsByTravelerIds")
    class GetAverageRatingsByTravelerIds {

        @Test
        @DisplayName("Debe calcular rating promedio por viajero")
        void shouldCalculateAverageRatings() {
            TravelerReview review1 = TravelerReview.builder()
                    .id(1L).bookingRequestId(100L).reviewerTravelerId(300L).reviewedHostId(200L)
                    .rating(4).createdAt(LocalDateTime.now()).build();
            TravelerReview review2 = TravelerReview.builder()
                    .id(2L).bookingRequestId(101L).reviewerTravelerId(300L).reviewedHostId(201L)
                    .rating(5).createdAt(LocalDateTime.now()).build();

            when(travelerReviewRepository.findByReviewerTravelerIdIn(List.of(300L)))
                    .thenReturn(List.of(review1, review2));

            Map<Long, TravelerRatingDto> result =
                    travelerReviewService.getAverageRatingsByTravelerIds(List.of(300L));

            assertThat(result).hasSize(1);
            assertThat(result.get(300L).getAverageRating()).isEqualTo(4.5);
            assertThat(result.get(300L).getReviewCount()).isEqualTo(2);
        }

        @Test
        @DisplayName("Debe retornar 0.0 cuando el viajero no tiene reviews")
        void shouldReturnZeroWhenNoReviews() {
            when(travelerReviewRepository.findByReviewerTravelerIdIn(List.of(999L)))
                    .thenReturn(Collections.emptyList());

            Map<Long, TravelerRatingDto> result =
                    travelerReviewService.getAverageRatingsByTravelerIds(List.of(999L));

            assertThat(result.get(999L).getAverageRating()).isEqualTo(0.0);
            assertThat(result.get(999L).getReviewCount()).isEqualTo(0);
        }

        @Test
        @DisplayName("Debe retornar mapa vacío para lista vacía")
        void shouldReturnEmptyMapForEmptyList() {
            Map<Long, TravelerRatingDto> result =
                    travelerReviewService.getAverageRatingsByTravelerIds(Collections.emptyList());

            assertThat(result).isEmpty();
        }

        @Test
        @DisplayName("Debe retornar mapa vacío para lista null")
        void shouldReturnEmptyMapForNullList() {
            Map<Long, TravelerRatingDto> result =
                    travelerReviewService.getAverageRatingsByTravelerIds(null);

            assertThat(result).isEmpty();
        }
    }

    @Nested
    @DisplayName("getExistsByBookingRequestIds")
    class GetExistsByBookingRequestIds {

        @Test
        @DisplayName("Debe retornar mapa con true para IDs con review")
        void shouldReturnTrueForExistingReviews() {
            when(travelerReviewRepository.findByBookingRequestIdIn(List.of(100L, 200L)))
                    .thenReturn(List.of(sampleReview));

            Map<Long, Boolean> result = travelerReviewService.getExistsByBookingRequestIds(List.of(100L, 200L));

            assertThat(result).hasSize(2);
            assertThat(result.get(100L)).isTrue();
            assertThat(result.get(200L)).isFalse();
        }

        @Test
        @DisplayName("Debe retornar mapa vacío para lista vacía")
        void shouldReturnEmptyMapForEmptyList() {
            Map<Long, Boolean> result = travelerReviewService.getExistsByBookingRequestIds(Collections.emptyList());

            assertThat(result).isEmpty();
        }

        @Test
        @DisplayName("Debe retornar mapa vacío para lista null")
        void shouldReturnEmptyMapForNullList() {
            Map<Long, Boolean> result = travelerReviewService.getExistsByBookingRequestIds(null);

            assertThat(result).isEmpty();
        }
    }
}
