package com.shareyourtrip.microservice.reviews.ShareYourTripReviewsMs.services;

import com.shareyourtrip.microservice.reviews.ShareYourTripReviewsMs.dtos.HostReviewRequestDto;
import com.shareyourtrip.microservice.reviews.ShareYourTripReviewsMs.dtos.HostReviewResponseDto;
import com.shareyourtrip.microservice.reviews.ShareYourTripReviewsMs.dtos.ReviewUpdateDto;
import com.shareyourtrip.microservice.reviews.ShareYourTripReviewsMs.entitites.HostReview;
import com.shareyourtrip.microservice.reviews.ShareYourTripReviewsMs.entitites.ReviewType;
import com.shareyourtrip.microservice.reviews.ShareYourTripReviewsMs.exceptions.ReviewAlreadyExistsException;
import com.shareyourtrip.microservice.reviews.ShareYourTripReviewsMs.exceptions.ReviewNotFoundException;
import com.shareyourtrip.microservice.reviews.ShareYourTripReviewsMs.repositories.HostReviewRepository;
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
@DisplayName("HostReviewServiceImpl - Tests unitarios")
class HostReviewServiceImplTest {

    @Mock
    private HostReviewRepository hostReviewRepository;

    @InjectMocks
    private HostReviewServiceImpl hostReviewService;

    private HostReview sampleReview;
    private HostReviewRequestDto sampleRequest;

    @BeforeEach
    void setUp() {
        sampleReview = HostReview.builder()
                .id(1L)
                .bookingRequestId(100L)
                .reviewerHostId(200L)
                .reviewedTravelerId(300L)
                .accommodationId(10L)
                .rating(4)
                .comment("Buen viajero")
                .createdAt(LocalDateTime.now())
                .build();

        sampleRequest = new HostReviewRequestDto();
        sampleRequest.setBookingRequestId(100L);
        sampleRequest.setReviewerHostId(200L);
        sampleRequest.setReviewedTravelerId(300L);
        sampleRequest.setAccommodationId(10L);
        sampleRequest.setRating(4);
        sampleRequest.setComment("Buen viajero");
    }

    @Nested
    @DisplayName("create")
    class Create {

        @Test
        @DisplayName("Debe crear review correctamente")
        void shouldCreateReview() {
            when(hostReviewRepository.existsByBookingRequestId(100L)).thenReturn(false);
            when(hostReviewRepository.save(any(HostReview.class))).thenAnswer(invocation -> {
                HostReview r = invocation.getArgument(0);
                ReflectionTestUtils.setField(r, "id", 1L);
                ReflectionTestUtils.setField(r, "createdAt", LocalDateTime.now());
                return r;
            });

            HostReviewResponseDto result = hostReviewService.create(sampleRequest);

            assertThat(result).isNotNull();
            assertThat(result.getBookingRequestId()).isEqualTo(100L);
            assertThat(result.getRating()).isEqualTo(4);
            assertThat(result.getReviewType()).isEqualTo(ReviewType.HOST_TO_TRAVELER);
            verify(hostReviewRepository).save(any(HostReview.class));
        }

        @Test
        @DisplayName("Debe lanzar ReviewAlreadyExistsException si ya existe")
        void shouldThrowWhenAlreadyExists() {
            when(hostReviewRepository.existsByBookingRequestId(100L)).thenReturn(true);

            assertThatThrownBy(() -> hostReviewService.create(sampleRequest))
                    .isInstanceOf(ReviewAlreadyExistsException.class)
                    .hasMessageContaining("100");

            verify(hostReviewRepository, never()).save(any());
        }
    }

    @Nested
    @DisplayName("getById")
    class GetById {

        @Test
        @DisplayName("Debe retornar DTO cuando existe")
        void shouldReturnDto() {
            when(hostReviewRepository.findById(1L)).thenReturn(Optional.of(sampleReview));

            HostReviewResponseDto result = hostReviewService.getById(1L);

            assertThat(result).isNotNull();
            assertThat(result.getId()).isEqualTo(1L);
            assertThat(result.getRating()).isEqualTo(4);
        }

        @Test
        @DisplayName("Debe lanzar ReviewNotFoundException cuando no existe")
        void shouldThrowWhenNotFound() {
            when(hostReviewRepository.findById(99L)).thenReturn(Optional.empty());

            assertThatThrownBy(() -> hostReviewService.getById(99L))
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
            when(hostReviewRepository.findByBookingRequestId(100L)).thenReturn(Optional.of(sampleReview));

            HostReviewResponseDto result = hostReviewService.getByBookingRequestId(100L);

            assertThat(result).isNotNull();
            assertThat(result.getBookingRequestId()).isEqualTo(100L);
        }

        @Test
        @DisplayName("Debe lanzar ReviewNotFoundException cuando no existe")
        void shouldThrowWhenNotFound() {
            when(hostReviewRepository.findByBookingRequestId(99L)).thenReturn(Optional.empty());

            assertThatThrownBy(() -> hostReviewService.getByBookingRequestId(99L))
                    .isInstanceOf(ReviewNotFoundException.class)
                    .hasMessageContaining("99");
        }
    }

    @Nested
    @DisplayName("getByReviewedTravelerId")
    class GetByReviewedTravelerId {

        @Test
        @DisplayName("Debe retornar lista de reviews del viajero reseñado")
        void shouldReturnReviews() {
            when(hostReviewRepository.findByReviewedTravelerId(300L)).thenReturn(List.of(sampleReview));

            List<HostReviewResponseDto> result = hostReviewService.getByReviewedTravelerId(300L);

            assertThat(result).hasSize(1);
            assertThat(result.get(0).getReviewedTravelerId()).isEqualTo(300L);
        }

        @Test
        @DisplayName("Debe retornar lista vacía si no hay reviews")
        void shouldReturnEmptyList() {
            when(hostReviewRepository.findByReviewedTravelerId(999L)).thenReturn(Collections.emptyList());

            List<HostReviewResponseDto> result = hostReviewService.getByReviewedTravelerId(999L);

            assertThat(result).isEmpty();
        }
    }

    @Nested
    @DisplayName("getByReviewerHostId")
    class GetByReviewerHostId {

        @Test
        @DisplayName("Debe retornar lista de reviews del anfitrión reseñador")
        void shouldReturnReviews() {
            when(hostReviewRepository.findByReviewerHostId(200L)).thenReturn(List.of(sampleReview));

            List<HostReviewResponseDto> result = hostReviewService.getByReviewerHostId(200L);

            assertThat(result).hasSize(1);
            assertThat(result.get(0).getReviewerHostId()).isEqualTo(200L);
        }
    }

    @Nested
    @DisplayName("update")
    class Update {

        @Test
        @DisplayName("Debe actualizar rating y comment")
        void shouldUpdateRatingAndComment() {
            ReviewUpdateDto updateDto = new ReviewUpdateDto();
            updateDto.setRating(5);
            updateDto.setComment("Excelente viajero");

            when(hostReviewRepository.findById(1L)).thenReturn(Optional.of(sampleReview));
            when(hostReviewRepository.save(any(HostReview.class)))
                    .thenAnswer(invocation -> invocation.getArgument(0));

            HostReviewResponseDto result = hostReviewService.update(1L, updateDto);

            assertThat(sampleReview.getRating()).isEqualTo(5);
            assertThat(sampleReview.getComment()).isEqualTo("Excelente viajero");
        }

        @Test
        @DisplayName("Debe actualizar solo rating si comment es null")
        void shouldUpdateOnlyRating() {
            ReviewUpdateDto updateDto = new ReviewUpdateDto();
            updateDto.setRating(2);

            when(hostReviewRepository.findById(1L)).thenReturn(Optional.of(sampleReview));
            when(hostReviewRepository.save(any(HostReview.class)))
                    .thenAnswer(invocation -> invocation.getArgument(0));

            hostReviewService.update(1L, updateDto);

            assertThat(sampleReview.getRating()).isEqualTo(2);
            assertThat(sampleReview.getComment()).isEqualTo("Buen viajero"); // sin cambios
        }

        @Test
        @DisplayName("Debe lanzar ReviewNotFoundException si no existe")
        void shouldThrowWhenNotFound() {
            ReviewUpdateDto updateDto = new ReviewUpdateDto();
            updateDto.setRating(3);

            when(hostReviewRepository.findById(99L)).thenReturn(Optional.empty());

            assertThatThrownBy(() -> hostReviewService.update(99L, updateDto))
                    .isInstanceOf(ReviewNotFoundException.class);
        }
    }

    @Nested
    @DisplayName("delete")
    class Delete {

        @Test
        @DisplayName("Debe eliminar review existente")
        void shouldDeleteReview() {
            when(hostReviewRepository.findById(1L)).thenReturn(Optional.of(sampleReview));

            hostReviewService.delete(1L);

            verify(hostReviewRepository).delete(sampleReview);
        }

        @Test
        @DisplayName("Debe lanzar ReviewNotFoundException si no existe")
        void shouldThrowWhenNotFound() {
            when(hostReviewRepository.findById(99L)).thenReturn(Optional.empty());

            assertThatThrownBy(() -> hostReviewService.delete(99L))
                    .isInstanceOf(ReviewNotFoundException.class);

            verify(hostReviewRepository, never()).delete(any());
        }
    }

    @Nested
    @DisplayName("getExistsByBookingRequestIds")
    class GetExistsByBookingRequestIds {

        @Test
        @DisplayName("Debe retornar mapa con true para IDs que tienen review")
        void shouldReturnTrueForExistingReviews() {
            when(hostReviewRepository.findAll()).thenReturn(List.of(sampleReview));

            Map<Long, Boolean> result = hostReviewService.getExistsByBookingRequestIds(List.of(100L, 200L));

            assertThat(result).hasSize(2);
            assertThat(result.get(100L)).isTrue();
            assertThat(result.get(200L)).isFalse();
        }

        @Test
        @DisplayName("Debe retornar mapa vacío para lista vacía")
        void shouldReturnEmptyMapForEmptyList() {
            Map<Long, Boolean> result = hostReviewService.getExistsByBookingRequestIds(Collections.emptyList());

            assertThat(result).isEmpty();
        }

        @Test
        @DisplayName("Debe retornar mapa vacío para lista null")
        void shouldReturnEmptyMapForNullList() {
            Map<Long, Boolean> result = hostReviewService.getExistsByBookingRequestIds(null);

            assertThat(result).isEmpty();
        }
    }
}
