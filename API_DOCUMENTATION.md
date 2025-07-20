# Marketing Follow API Server - Documentation

## Project Overview
- **Language**: Kotlin 1.9.25
- **Framework**: Spring Boot 3.5.6
- **ORM**: Jetbrains Exposed (DAO Pattern)
- **Database**: MariaDB
- **Build Tool**: Gradle (Kotlin DSL)
- **Java Version**: 17

---

## Package Structure

```
org.example.marketingfollowapiserver/
├── config/                    # Configuration classes
│   ├── DatabaseInitializer.kt
│   └── SchemaInitializer.kt
├── controller/                # REST API endpoints
│   └── FollowController.kt
├── dto/                       # Data Transfer Objects
│   ├── BaseDateEntity.kt
│   ├── FollowAdvertiserEntity.kt
│   ├── FollowAdvertiserMetadata.kt
│   ├── SaveFollowAdvertiser.kt
│   ├── MSABusinessErrorResponse.kt
│   ├── FollowOrSwitchApiRequest.kt
│   ├── FollowOrSwitchResult.kt
│   ├── FollowOrSwitchResponseFromServer.kt
│   ├── GetFollowersApiRequest.kt
│   ├── GetFollowersResult.kt
│   ├── GetFollowersResponseFromServer.kt
│   ├── GetFollowingApiRequest.kt
│   ├── GetFollowingResult.kt
│   └── GetFollowingResponseFromServer.kt
├── enums/                     # Enum classes
│   ├── FollowStatus.kt
│   └── MSAServiceErrorCode.kt
├── exception/                 # Exception classes
│   ├── MSAServerException.kt
│   ├── NotFoundFollowAdvertiserException.kt
│   ├── SaveFailedForDatabaseException.kt
│   └── GlobalExceptionHandler.kt
├── repository/                # Data access layer
│   └── FollowAdvertiserRepository.kt
├── service/                   # Business logic layer
│   └── FollowService.kt
└── table/                     # Exposed table definitions
    ├── BaseDateLongIdTable.kt
    └── FollowAdvertisersTable.kt
```

---

## DTO Flow Architecture

### Layer Communication Flow

```
CLIENT
  ↓ [FollowOrSwitchApiRequest]
CONTROLLER
  ↓ [advertiserId, influencerId]
SERVICE
  ↓ [SaveFollowAdvertiser / parameters]
REPOSITORY
  ↓ [Entity operations]
DATABASE
  ↑ [FollowAdvertiserMetadata]
REPOSITORY
  ↑ [FollowOrSwitchResult]
SERVICE
  ↑ [FollowOrSwitchResponseFromServer]
CONTROLLER
  ↑
CLIENT
```

### DTO Naming Conventions

| Layer | Direction | Pattern | Example |
|-------|-----------|---------|---------|
| Controller → Client | OUT | `[Method-Domain]ResponseFromServer` | `FollowOrSwitchResponseFromServer` |
| Client → Controller | IN | `[Method-Domain]ApiRequest` | `FollowOrSwitchApiRequest` |
| Service → Controller | OUT | `[Method-Domain]Result` | `FollowOrSwitchResult` |
| Repository → Service | OUT | `[EntityName]Metadata` | `FollowAdvertiserMetadata` |
| Service → Repository | IN (save) | `Save[EntityName]` | `SaveFollowAdvertiser` |

---

## API Endpoints

### 1. Follow or Switch Status

**Endpoint**: `POST /api/follow/or-switch`

**Description**: Creates a new follow relationship or toggles existing status (FOLLOW ↔ UNFOLLOW)

**Request Body**:
```json
{
  "advertiserId": "uuid-string",
  "influencerId": "uuid-string"
}
```

**Response**:
```json
{
  "httpStatus": "OK",
  "msaServiceErrorCode": "OK",
  "errorMessage": null,
  "logics": "FollowController.followOrSwitch",
  "result": {
    "followAdvertiserMetadata": {
      "id": 1,
      "advertiserId": "uuid-string",
      "influencerId": "uuid-string",
      "followStatus": "FOLLOW",
      "createdAt": 1234567890,
      "lastModifiedAt": 1234567890
    },
    "wasExisting": false,
    "previousStatus": null
  }
}
```

**Business Logic**:
- If relationship doesn't exist → Create with `FOLLOW` status
- If relationship exists → Toggle status (FOLLOW → UNFOLLOW or UNFOLLOW → FOLLOW)

---

### 2. Get Followers

**Endpoint**: `GET /api/follow/followers?advertiserId={uuid}`

**Description**: Retrieves all influencers following a specific advertiser

**Query Parameters**:
- `advertiserId` (required): UUID of the advertiser

**Response**:
```json
{
  "httpStatus": "OK",
  "msaServiceErrorCode": "OK",
  "errorMessage": null,
  "logics": "FollowController.getFollowers",
  "result": {
    "followers": [
      {
        "id": 1,
        "advertiserId": "advertiser-uuid",
        "influencerId": "influencer-uuid-1",
        "followStatus": "FOLLOW",
        "createdAt": 1234567890,
        "lastModifiedAt": 1234567890
      }
    ]
  }
}
```

---

### 3. Get Following

**Endpoint**: `GET /api/follow/following?influencerId={uuid}`

**Description**: Retrieves all advertisers that an influencer is following

**Query Parameters**:
- `influencerId` (required): UUID of the influencer

**Response**:
```json
{
  "httpStatus": "OK",
  "msaServiceErrorCode": "OK",
  "errorMessage": null,
  "logics": "FollowController.getFollowing",
  "result": {
    "following": [
      {
        "id": 1,
        "advertiserId": "advertiser-uuid-1",
        "influencerId": "influencer-uuid",
        "followStatus": "FOLLOW",
        "createdAt": 1234567890,
        "lastModifiedAt": 1234567890
      }
    ]
  }
}
```

---

## Database Schema

### FollowAdvertisersTable

```sql
CREATE TABLE follow_advertisers (
    id BIGINT PRIMARY KEY AUTO_INCREMENT,
    advertiser_id VARCHAR(36) NOT NULL,
    influencer_id VARCHAR(36) NOT NULL,
    follow_status ENUM('FOLLOW', 'UNFOLLOW') NOT NULL,
    created_at BIGINT NOT NULL,
    last_modified_at BIGINT NOT NULL,
    UNIQUE KEY unique_follow (advertiser_id, influencer_id)
);
```

**Constraints**:
- Primary Key: `id`
- Unique Index: `(advertiser_id, influencer_id)` - Prevents duplicate relationships
- Follow direction: Influencer → Advertiser only

---

## Error Handling

### MSAServiceErrorCode

| Code Range | Category | Examples |
|------------|----------|----------|
| 0~ | Success | `OK(0)` |
| 10000~ | Authentication & Authorization | `INVALID_TOKEN(10001)`, `EXPIRED_TOKEN(10002)` |
| 20000~ | Permission & Role | `FORBIDDEN(20001)` |
| 30000~ | User Information | `USER_NOT_FOUND(30001)`, `ADVERTISER_NOT_FOUND(30002)` |
| 40000~ | Entity Not Found | `NOT_FOUND_FOLLOW_ADVERTISER(40000)` |
| 50000~ | Server Errors | `SAVE_FAILED_FOR_DATABASE(50000)`, `INTERNAL_SERVER_ERROR(50099)` |

### Exception Hierarchy

```
RuntimeException
  └── MSAServerException
       ├── NotFoundFollowAdvertiserException
       └── SaveFailedForDatabaseException
```

All exceptions are caught by `GlobalExceptionHandler` and returned as `MSABusinessErrorResponse`.

---

## Configuration

### application.yml

```yaml
spring:
  datasource:
    url: jdbc:mariadb://localhost:3306/follow_db
    username: root
    password: password
    driver-class-name: org.mariadb.jdbc.Driver

server:
  port: 8080
```

**Before running, ensure**:
1. MariaDB is installed and running
2. Database `follow_db` is created: `CREATE DATABASE follow_db;`
3. Update credentials in `application.yml`

---

## Build & Run

### Build
```bash
./gradlew build
```

### Run
```bash
./gradlew bootRun
```

### Test Endpoints

**Create Follow Relationship**:
```bash
curl -X POST http://localhost:8080/api/follow/or-switch \
  -H "Content-Type: application/json" \
  -d '{
    "advertiserId": "advertiser-123",
    "influencerId": "influencer-456"
  }'
```

**Get Followers**:
```bash
curl "http://localhost:8080/api/follow/followers?advertiserId=advertiser-123"
```

**Get Following**:
```bash
curl "http://localhost:8080/api/follow/following?influencerId=influencer-456"
```

---

## Architecture Highlights

### Auto-Update Mechanism
- `BaseDateEntityAutoUpdate` automatically updates `lastModifiedAt` on entity updates via `EntityHook`

### Transaction Management
- All database operations wrapped in `transaction { }` blocks
- Automatic rollback on exceptions

### Logging
- `kotlin-logging` integrated throughout all layers
- Debug-level logging for database operations
- Info-level logging for business operations

### Type Safety
- Kotlin's null safety enforced throughout
- Enum-based status management prevents invalid states
- UUID represented as String for flexibility

---

## Business Rules

1. **Follow Direction**: Only Influencer → Advertiser (not bidirectional)
2. **Unique Constraint**: One relationship per (advertiser, influencer) pair
3. **Status Toggle**: Existing relationships toggle between FOLLOW/UNFOLLOW
4. **Timestamps**: Automatically managed by base classes
5. **Query Filtering**: Only returns `FOLLOW` status records (excludes `UNFOLLOW`)

---

## Future Enhancements

- [ ] Add pagination for follower/following lists
- [ ] Implement bulk follow/unfollow operations
- [ ] Add follow statistics endpoints
- [ ] Implement real UUID validation
- [ ] Add authentication/authorization
- [ ] Add caching layer (Redis)
- [ ] Add API rate limiting
