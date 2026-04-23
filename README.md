# ShareYourTrip_ms_reviews

Microservicio de gestión de reseñas para la plataforma ShareYourTrip.

## Descripción

Este microservicio es responsable de la gestión de reseñas en la plataforma ShareYourTrip. Implementa dos tipos de reseñas: reseñas de anfitriones (host reviews) donde los anfitriones evalúan a los viajeros, y reseñas de viajeros (traveler reviews) donde los viajeros evalúan a los anfitriones y sus alojamientos.

## Características Principales

- **Gestión de reseñas de anfitriones**: Anfitriones pueden evaluar a viajeros después de una estancia
- **Gestión de reseñas de viajeros**: Viajeros pueden evaluar anfitriones y alojamientos
- **Sistema de calificación**: Escala de 1 a 5 estrellas
- **Validación de origen**: Filtro que valida peticiones provenientes del API Gateway
- **CRUD completo**: Creación, lectura, actualización y eliminación de reseñas
- **Búsqueda por usuario**: Consulta de reseñas por usuario evaluado o evaluador

## Arquitectura

```
API Gateway (8080)
   ↓ (valida JWT + añade headers X-User-Id, X-User-Roles)
Reviews Microservice (8084)
   ↓ (valida header X-User-Id)
Procesa la petición
```

## Modelo de Datos

### Entidad HostReview

| Campo | Tipo | Descripción |
|-------|------|-------------|
| id | Long | Identificador único de la reseña |
| bookingRequestId | Long | ID de la solicitud de reserva asociada |
| reviewerHostId | Long | ID del anfitrión que escribe la reseña |
| reviewedTravelerId | Long | ID del viajero evaluado |
| accommodationId | Long | ID del alojamiento |
| rating | Integer | Calificación (1-5) |
| comment | Text | Comentario de la reseña |
| createdAt | LocalDateTime | Fecha de creación |
| updatedAt | LocalDateTime | Fecha de última actualización |

### Entidad TravelerReview

| Campo | Tipo | Descripción |
|-------|------|-------------|
| id | Long | Identificador único de la reseña |
| bookingRequestId | Long | ID de la solicitud de reserva asociada |
| reviewerTravelerId | Long | ID del viajero que escribe la reseña |
| reviewedHostId | Long | ID del anfitrión evaluado |
| accommodationId | Long | ID del alojamiento |
| rating | Integer | Calificación (1-5) |
| comment | Text | Comentario de la reseña |
| createdAt | LocalDateTime | Fecha de creación |
| updatedAt | LocalDateTime | Fecha de última actualización |

## Endpoints

### Reseñas de Anfitriones (Host Reviews)

#### Crear reseña de anfitrión
```http
POST /host-reviews
Content-Type: application/json
Authorization: Bearer {{token}}

{
  "bookingRequestId": 1,
  "reviewerHostId": 1,
  "reviewedTravelerId": 2,
  "accommodationId": 1,
  "rating": 5,
  "comment": "Excellent guest, very respectful!"
}
```

**Respuesta (201):**
```json
{
  "id": 1,
  "bookingRequestId": 1,
  "reviewerHostId": 1,
  "reviewedTravelerId": 2,
  "accommodationId": 1,
  "rating": 5,
  "comment": "Excellent guest, very respectful!",
  "createdAt": "2024-07-10T10:00:00",
  "updatedAt": "2024-07-10T10:00:00"
}
```

#### Obtener reseña de anfitrión por ID
```http
GET /host-reviews/{id}
Authorization: Bearer {{token}}
```

**Respuesta (200):**
```json
{
  "id": 1,
  "bookingRequestId": 1,
  "reviewerHostId": 1,
  "reviewedTravelerId": 2,
  "accommodationId": 1,
  "rating": 5,
  "comment": "Excellent guest, very respectful!",
  "createdAt": "2024-07-10T10:00:00",
  "updatedAt": "2024-07-10T10:00:00"
}
```

#### Obtener reseña de anfitrión por solicitud de reserva
```http
GET /host-reviews/booking/{bookingRequestId}
Authorization: Bearer {{token}}
```

**Respuesta (200):**
```json
{
  "id": 1,
  "bookingRequestId": 1,
  "reviewerHostId": 1,
  "reviewedTravelerId": 2,
  "accommodationId": 1,
  "rating": 5,
  "comment": "Excellent guest, very respectful!",
  "createdAt": "2024-07-10T10:00:00",
  "updatedAt": "2024-07-10T10:00:00"
}
```

#### Obtener reseñas de anfitrión por viajero evaluado
```http
GET /host-reviews/reviewed-traveler/{travelerId}
Authorization: Bearer {{token}}
```

**Respuesta (200):**
```json
[
  {
    "id": 1,
    "bookingRequestId": 1,
    "reviewerHostId": 1,
    "reviewedTravelerId": 2,
    "accommodationId": 1,
    "rating": 5,
    "comment": "Excellent guest, very respectful!",
    "createdAt": "2024-07-10T10:00:00",
    "updatedAt": "2024-07-10T10:00:00"
  }
]
```

#### Obtener reseñas de anfitrión por anfitrión evaluador
```http
GET /host-reviews/reviewer-host/{hostId}
Authorization: Bearer {{token}}
```

**Respuesta (200):**
```json
[
  {
    "id": 1,
    "bookingRequestId": 1,
    "reviewerHostId": 1,
    "reviewedTravelerId": 2,
    "accommodationId": 1,
    "rating": 5,
    "comment": "Excellent guest, very respectful!",
    "createdAt": "2024-07-10T10:00:00",
    "updatedAt": "2024-07-10T10:00:00"
  }
]
```

#### Actualizar reseña de anfitrión
```http
PATCH /host-reviews/{id}
Content-Type: application/json
Authorization: Bearer {{token}}

{
  "rating": 4,
  "comment": "Updated comment"
}
```

**Respuesta (200):**
```json
{
  "id": 1,
  "bookingRequestId": 1,
  "reviewerHostId": 1,
  "reviewedTravelerId": 2,
  "accommodationId": 1,
  "rating": 4,
  "comment": "Updated comment",
  "createdAt": "2024-07-10T10:00:00",
  "updatedAt": "2024-07-10T11:00:00"
}
```

#### Eliminar reseña de anfitrión
```http
DELETE /host-reviews/{id}
Authorization: Bearer {{token}}
```

**Respuesta (204):** No content

### Reseñas de Viajeros (Traveler Reviews)

#### Crear reseña de viajero
```http
POST /traveler-reviews
Content-Type: application/json
Authorization: Bearer {{token}}

{
  "bookingRequestId": 1,
  "reviewerTravelerId": 2,
  "reviewedHostId": 1,
  "accommodationId": 1,
  "rating": 5,
  "comment": "Great host, beautiful apartment!"
}
```

**Respuesta (201):**
```json
{
  "id": 1,
  "bookingRequestId": 1,
  "reviewerTravelerId": 2,
  "reviewedHostId": 1,
  "accommodationId": 1,
  "rating": 5,
  "comment": "Great host, beautiful apartment!",
  "createdAt": "2024-07-10T10:00:00",
  "updatedAt": "2024-07-10T10:00:00"
}
```

#### Obtener reseña de viajero por ID
```http
GET /traveler-reviews/{id}
Authorization: Bearer {{token}}
```

**Respuesta (200):**
```json
{
  "id": 1,
  "bookingRequestId": 1,
  "reviewerTravelerId": 2,
  "reviewedHostId": 1,
  "accommodationId": 1,
  "rating": 5,
  "comment": "Great host, beautiful apartment!",
  "createdAt": "2024-07-10T10:00:00",
  "updatedAt": "2024-07-10T10:00:00"
}
```

#### Obtener reseña de viajero por solicitud de reserva
```http
GET /traveler-reviews/booking/{bookingRequestId}
Authorization: Bearer {{token}}
```

**Respuesta (200):**
```json
{
  "id": 1,
  "bookingRequestId": 1,
  "reviewerTravelerId": 2,
  "reviewedHostId": 1,
  "accommodationId": 1,
  "rating": 5,
  "comment": "Great host, beautiful apartment!",
  "createdAt": "2024-07-10T10:00:00",
  "updatedAt": "2024-07-10T10:00:00"
}
```

#### Obtener reseñas de viajero por anfitrión evaluado
```http
GET /traveler-reviews/reviewed-host/{hostId}
Authorization: Bearer {{token}}
```

**Respuesta (200):**
```json
[
  {
    "id": 1,
    "bookingRequestId": 1,
    "reviewerTravelerId": 2,
    "reviewedHostId": 1,
    "accommodationId": 1,
    "rating": 5,
    "comment": "Great host, beautiful apartment!",
    "createdAt": "2024-07-10T10:00:00",
    "updatedAt": "2024-07-10T10:00:00"
  }
]
```

#### Obtener reseñas de viajero por viajero evaluador
```http
GET /traveler-reviews/reviewer-traveler/{travelerId}
Authorization: Bearer {{token}}
```

**Respuesta (200):**
```json
[
  {
    "id": 1,
    "bookingRequestId": 1,
    "reviewerTravelerId": 2,
    "reviewedHostId": 1,
    "accommodationId": 1,
    "rating": 5,
    "comment": "Great host, beautiful apartment!",
    "createdAt": "2024-07-10T10:00:00",
    "updatedAt": "2024-07-10T10:00:00"
  }
]
```

#### Actualizar reseña de viajero
```http
PATCH /traveler-reviews/{id}
Content-Type: application/json
Authorization: Bearer {{token}}

{
  "rating": 4,
  "comment": "Updated comment"
}
```

**Respuesta (200):**
```json
{
  "id": 1,
  "bookingRequestId": 1,
  "reviewerTravelerId": 2,
  "reviewedHostId": 1,
  "accommodationId": 1,
  "rating": 4,
  "comment": "Updated comment",
  "createdAt": "2024-07-10T10:00:00",
  "updatedAt": "2024-07-10T11:00:00"
}
```

#### Eliminar reseña de viajero
```http
DELETE /traveler-reviews/{id}
Authorization: Bearer {{token}}
```

**Respuesta (204):** No content

## Seguridad

### Validación de Origen

El microservicio implementa un filtro `GatewayAuthenticationFilter` que:
- Valida la presencia del header `X-User-Id` enviado por el API Gateway
- Rechaza peticiones directas sin este header (401)

### Configuración Spring Security

- Spring Security configurado con `permitAll()` (validación manual)
- CSRF deshabilitado
- Filtro `GatewayAuthenticationFilter` añadido antes de `UsernamePasswordAuthenticationFilter`

## Configuración

### application.yaml

```yaml
server:
  port: 8084
  servlet:
    context-path: /

spring:
  application:
    name: share-your-trip-reviews
  datasource:
    url: jdbc:postgresql://localhost:54320/product
    username: product
    password: product
    driver-class-name: org.postgresql.Driver
  jpa:
    hibernate:
      ddl-auto: update
    show-sql: true
    properties:
      hibernate:
        dialect: org.hibernate.dialect.PostgreSQLDialect
        format_sql: true
```

## Tecnologías

- Spring Boot 4.0.5
- Spring Data JPA
- Spring Security
- Spring Boot Starter Web
- PostgreSQL
- Lombok
- Jakarta Validation API

## DTOs

### HostReviewRequestDto
```java
{
  "bookingRequestId": 1,
  "reviewerHostId": 1,
  "reviewedTravelerId": 2,
  "accommodationId": 1,
  "rating": 5,
  "comment": "Excellent guest!"
}
```

### HostReviewResponseDto
```java
{
  "id": 1,
  "bookingRequestId": 1,
  "reviewerHostId": 1,
  "reviewedTravelerId": 2,
  "accommodationId": 1,
  "rating": 5,
  "comment": "Excellent guest!",
  "createdAt": "2024-07-10T10:00:00",
  "updatedAt": "2024-07-10T10:00:00"
}
```

### TravelerReviewRequestDto
```java
{
  "bookingRequestId": 1,
  "reviewerTravelerId": 2,
  "reviewedHostId": 1,
  "accommodationId": 1,
  "rating": 5,
  "comment": "Great host!"
}
```

### TravelerReviewResponseDto
```java
{
  "id": 1,
  "bookingRequestId": 1,
  "reviewerTravelerId": 2,
  "reviewedHostId": 1,
  "accommodationId": 1,
  "rating": 5,
  "comment": "Great host!",
  "createdAt": "2024-07-10T10:00:00",
  "updatedAt": "2024-07-10T10:00:00"
}
```

### ReviewUpdateDto
```java
{
  "rating": 4,
  "comment": "Updated comment"
}
```

## Sistema de Calificación

| Calificación | Descripción |
|--------------|-------------|
| 1 | Muy malo |
| 2 | Malo |
| 3 | Regular |
| 4 | Bueno |
| 5 | Excelente |

## Flujo de Reseñas

1. **Reserva completada**: Una vez que se completa una reserva aceptada, ambas partes pueden dejar reseñas
2. **Reseña de anfitrión**: El anfitrión evalúa al viajero (comportamiento, respeto, etc.)
3. **Reseña de viajero**: El viajero evalúa al anfitrión y el alojamiento (calidad, limpieza, ubicación, etc.)
4. **Unicidad**: Solo puede haber una reseña por solicitud de reserva para cada tipo
5. **Actualización**: Las reseñas pueden actualizarse pero no duplicarse

## Cómo Ejecutar

### Compilar
```bash
mvnw.cmd clean package -DskipTests
```

### Ejecutar
```bash
java -jar target/*.jar
```

El servicio estará disponible en `http://localhost:8084`

## Notas Importantes

- El microservicio debe recibir peticiones únicamente a través del API Gateway
- Las reseñas están vinculadas a solicitudes de reserva aceptadas
- La calificación debe estar entre 1 y 5
- Solo se puede crear una reseña por solicitud de reserva para cada tipo
- Las reseñas ayudan a construir la reputación de usuarios y alojamientos
- El servicio no calcula promedios de calificación (esto debe hacerse en el servicio que consume las reseñas)
