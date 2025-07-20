# MSA Server - Complete DTO Flow Summary

## Follow API Server Architecture

This document provides a complete overview of the Data Transfer Object (DTO) flow across all layers of the Marketing Follow API Server.

---

## 📊 Complete DTO Flow Diagram

```
┌─────────────────────────────────────────────────────────────────────────────┐
│                              CLIENT APPLICATION                             │
└─────────────────────────────────────────────────────────────────────────────┘
                                       │
                                       │ HTTP Request
                                       ↓
┌─────────────────────────────────────────────────────────────────────────────┐
│                          CONTROLLER LAYER                                   │
│ ━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━ │
│                                                                             │
│ 📥 INPUT (from Client):                                                     │
│    • FollowOrSwitchApiRequest                                               │
│      ├─ advertiserId: String                                                │
│      └─ influencerId: String                                                │
│                                                                             │
│ 📤 OUTPUT (to Client):                                                      │
│    • FollowOrSwitchResponseFromServer (extends MSABusinessErrorResponse)    │
│      ├─ httpStatus: HttpStatus                                              │
│      ├─ msaServiceErrorCode: MSAServiceErrorCode                            │
│      ├─ errorMessage: String?                                               │
│      ├─ logics: String?                                                     │
│      └─ result: FollowOrSwitchResult?                                       │
│                                                                             │
│ 🔧 Responsibilities:                                                         │
│    - Receive HTTP requests                                                  │
│    - Validate request format                                                │
│    - Decompose ApiRequest into parameters                                   │
│    - Call Service layer methods                                             │
│    - Wrap Service Result in ResponseFromServer                              │
│    - Return HTTP response                                                   │
└─────────────────────────────────────────────────────────────────────────────┘
                                       │
                                       │ Decomposed Parameters
                                       ↓
┌─────────────────────────────────────────────────────────────────────────────┐
│                            SERVICE LAYER                                    │
│ ━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━ │
│                                                                             │
│ 📥 INPUT (from Controller):                                                 │
│    • Raw parameters (decomposed from ApiRequest)                            │
│      ├─ advertiserId: String                                                │
│      └─ influencerId: String                                                │
│                                                                             │
│ 📤 OUTPUT (to Controller):                                                  │
│    • FollowOrSwitchResult                                                   │
│      ├─ followAdvertiserMetadata: FollowAdvertiserMetadata                  │
│      ├─ wasExisting: Boolean                                                │
│      └─ previousStatus: FollowStatus?                                       │
│                                                                             │
│ 🔧 Responsibilities:                                                         │
│    - Implement business logic                                               │
│    - Check if relationship exists                                           │
│    - Decide whether to create new or switch existing                        │
│    - Call Repository methods                                                │
│    - Transform Metadata into Result objects                                 │
│    - Handle business exceptions                                             │
└─────────────────────────────────────────────────────────────────────────────┘
                                       │
                                       │ SaveDTO or Query Parameters
                                       ↓
┌─────────────────────────────────────────────────────────────────────────────┐
│                          REPOSITORY LAYER                                   │
│ ━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━ │
│                                                                             │
│ 📥 INPUT (from Service):                                                    │
│                                                                             │
│    For SAVE operations:                                                     │
│    • SaveFollowAdvertiser.of(...)                                           │
│      ├─ advertiserId: String                                                │
│      ├─ influencerId: String                                                │
│      └─ followStatus: FollowStatus                                          │
│                                                                             │
│    For SWITCH operations:                                                   │
│    • followAdvertiserEntity: FollowAdvertiserEntity                         │
│                                                                             │
│    For FIND operations:                                                     │
│    • advertiserId: String  OR  influencerId: String                         │
│                                                                             │
│ 📤 OUTPUT (to Service):                                                     │
│    • FollowAdvertiserMetadata (domain object)                               │
│      ├─ id: Long                                                            │
│      ├─ advertiserId: String                                                │
│      ├─ influencerId: String                                                │
│      ├─ followStatus: FollowStatus                                          │
│      ├─ createdAt: Long                                                     │
│      └─ lastModifiedAt: Long                                                │
│                                                                             │
│    • List<FollowAdvertiserMetadata> (for batch queries)                     │
│                                                                             │
│ 🔧 Responsibilities:                                                         │
│    - Execute database operations via Exposed                                │
│    - Convert Entity to Metadata (domain object)                             │
│    - Handle database exceptions                                             │
│    - Manage transactions                                                    │
└─────────────────────────────────────────────────────────────────────────────┘
                                       │
                                       │ Exposed DAO Operations
                                       ↓
┌─────────────────────────────────────────────────────────────────────────────┐
│                       DATABASE (MariaDB via Exposed)                        │
│ ━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━ │
│                                                                             │
│ 📦 Entity: FollowAdvertiserEntity (extends BaseDateEntity)                  │
│                                                                             │
│ 📊 Table: FollowAdvertisersTable (extends BaseDateLongIdTable)              │
│    ├─ id: BIGINT (Primary Key, Auto-increment)                              │
│    ├─ advertiser_id: VARCHAR(36)                                            │
│    ├─ influencer_id: VARCHAR(36)                                            │
│    ├─ follow_status: ENUM('FOLLOW', 'UNFOLLOW')                             │
│    ├─ created_at: BIGINT (timestamp)                                        │
│    ├─ last_modified_at: BIGINT (timestamp, auto-updates)                    │
│    └─ UNIQUE(advertiser_id, influencer_id)                                  │
│                                                                             │
│ 🔧 Features:                                                                 │
│    - Unique constraint prevents duplicate relationships                     │
│    - Auto-timestamps via BaseDateLongIdTable                                │
│    - Auto-update lastModifiedAt via EntityHook                              │
│    - Enum-based status (type-safe)                                          │
└─────────────────────────────────────────────────────────────────────────────┘
```

---

## 📋 DTO Naming Convention Summary

| **DTO Type** | **Naming Pattern** | **Example** | **Location** | **Purpose** |
|--------------|-------------------|-------------|--------------|-------------|
| **API Request** | `[Method-Domain]ApiRequest` | `FollowOrSwitchApiRequest` | Controller IN | Client → Server request payload |
| **API Response** | `[Method-Domain]ResponseFromServer` | `FollowOrSwitchResponseFromServer` | Controller OUT | Server → Client response payload |
| **Service Result** | `[Method-Domain]Result` | `FollowOrSwitchResult` | Service → Controller | Business logic result |
| **Domain Metadata** | `[EntityName]Metadata` | `FollowAdvertiserMetadata` | Repository → Service | Domain object (not DB entity) |
| **Save DTO** | `Save[EntityName]` | `SaveFollowAdvertiser` | Service → Repository | Data for creating new records |
| **Entity** | `[EntityName]Entity` | `FollowAdvertiserEntity` | Repository (Exposed) | Database entity (ORM) |
| **Table** | `[EntityName]sTable` | `FollowAdvertisersTable` | Repository (Exposed) | Database table definition |

---

## 🔄 Complete Flow Examples

### Example 1: Creating a New Follow Relationship

```
1. CLIENT sends POST request:
   {
     "advertiserId": "adv-123",
     "influencerId": "inf-456"
   }

2. CONTROLLER receives FollowOrSwitchApiRequest
   → Extracts: advertiserId = "adv-123", influencerId = "inf-456"
   → Calls: followService.followOrSwitch(advertiserId, influencerId)

3. SERVICE checks if relationship exists
   → Calls: repository.findByAdvertiserIdAndInfluencerId("adv-123", "inf-456")
   → Returns: null (not found)

   → Creates SaveFollowAdvertiser DTO:
     {
       advertiserId: "adv-123",
       influencerId: "inf-456",
       followStatus: FOLLOW
     }

   → Calls: repository.save(saveFollowAdvertiser)

4. REPOSITORY creates new entity
   → Exposed creates new FollowAdvertiserEntity
   → Auto-sets: createdAt, lastModifiedAt
   → Saves to database
   → Converts Entity to FollowAdvertiserMetadata
   → Returns: FollowAdvertiserMetadata(id=1, advertiserId="adv-123", ...)

5. SERVICE receives FollowAdvertiserMetadata
   → Creates FollowOrSwitchResult:
     {
       followAdvertiserMetadata: {...},
       wasExisting: false,
       previousStatus: null
     }
   → Returns: FollowOrSwitchResult

6. CONTROLLER receives FollowOrSwitchResult
   → Wraps in FollowOrSwitchResponseFromServer:
     {
       httpStatus: OK,
       msaServiceErrorCode: OK,
       errorMessage: null,
       logics: "FollowController.followOrSwitch",
       result: {...}
     }
   → Returns HTTP 200 with response body

7. CLIENT receives JSON response
```

---

### Example 2: Switching an Existing Relationship

```
1. CLIENT sends POST request (same as Example 1)

2. CONTROLLER → SERVICE (same flow)

3. SERVICE checks if relationship exists
   → Calls: repository.findByAdvertiserIdAndInfluencerId("adv-123", "inf-456")
   → Returns: FollowAdvertiserEntity (found! with status = FOLLOW)

   → Saves previousStatus = FOLLOW

   → Calls: repository.switchFollowStatus(entity)

4. REPOSITORY toggles status
   → Changes entity.followStatus from FOLLOW to UNFOLLOW
   → EntityHook auto-updates lastModifiedAt
   → Converts Entity to FollowAdvertiserMetadata
   → Returns: FollowAdvertiserMetadata (with UNFOLLOW status)

5. SERVICE receives updated FollowAdvertiserMetadata
   → Creates FollowOrSwitchResult:
     {
       followAdvertiserMetadata: {..., followStatus: UNFOLLOW},
       wasExisting: true,
       previousStatus: FOLLOW
     }
   → Returns: FollowOrSwitchResult

6. CONTROLLER → CLIENT (same wrap and return flow)
```

---

## 🎯 Key Architectural Principles

### 1. **Clear Layer Separation**
- Each layer has specific DTO types
- No mixing of DTOs across layers
- Controller never sees Entity objects
- Service never sees ApiRequest objects

### 2. **DTO Transformation Flow**
```
ApiRequest → Parameters → SaveDTO/Entity → Metadata → Result → ResponseFromServer
```

### 3. **Naming Consistency**
- Always use `.of()` factory methods for DTO creation
- Always use `fromEntity()` for Entity → Metadata conversion
- Always suffix with purpose: `Request`, `Result`, `Response`, `Metadata`, `Entity`

### 4. **Error Handling Uniformity**
- All responses inherit from `MSABusinessErrorResponse`
- All exceptions inherit from `MSAServerException`
- GlobalExceptionHandler catches all exceptions
- Error responses follow same structure as success responses

### 5. **Type Safety**
- Kotlin null safety enforced
- Enums prevent invalid states
- Immutable DTOs (data classes)
- Explicit parameter names in `.of()` calls

---

## 📚 All DTOs by Layer

### Controller Layer
- **IN**: `FollowOrSwitchApiRequest`, `GetFollowersApiRequest`, `GetFollowingApiRequest`
- **OUT**: `FollowOrSwitchResponseFromServer`, `GetFollowersResponseFromServer`, `GetFollowingResponseFromServer`

### Service Layer
- **OUT**: `FollowOrSwitchResult`, `GetFollowersResult`, `GetFollowingResult`

### Repository Layer
- **IN**: `SaveFollowAdvertiser`, entity references, query parameters
- **OUT**: `FollowAdvertiserMetadata`, `List<FollowAdvertiserMetadata>`

### Common/Shared
- **Base**: `MSABusinessErrorResponse`, `BaseDateEntity`, `BaseDateEntityAutoUpdate`
- **Domain**: `FollowAdvertiserMetadata`
- **Entity**: `FollowAdvertiserEntity`

---

## 🔍 Complete Method Signatures

### FollowController
```kotlin
fun followOrSwitch(request: FollowOrSwitchApiRequest): FollowOrSwitchResponseFromServer
fun getFollowers(advertiserId: String): GetFollowersResponseFromServer
fun getFollowing(influencerId: String): GetFollowingResponseFromServer
```

### FollowService
```kotlin
fun followOrSwitch(advertiserId: String, influencerId: String): FollowOrSwitchResult
fun getFollowersByAdvertiserId(advertiserId: String): GetFollowersResult
fun getFollowingByInfluencerId(influencerId: String): GetFollowingResult
```

### FollowAdvertiserRepository
```kotlin
fun save(saveFollowAdvertiser: SaveFollowAdvertiser): FollowAdvertiserMetadata
fun switchFollowStatus(entity: FollowAdvertiserEntity): FollowAdvertiserMetadata
fun findByAdvertiserIdAndInfluencerId(advertiserId: String, influencerId: String): FollowAdvertiserEntity?
fun findFollowersByAdvertiserId(advertiserId: String): List<FollowAdvertiserMetadata>
fun findFollowingByInfluencerId(influencerId: String): List<FollowAdvertiserMetadata>
```

---

## ✅ Implementation Checklist

- [x] Base classes (BaseDateLongIdTable, BaseDateEntity, BaseDateEntityAutoUpdate)
- [x] Configuration (DatabaseInitializer, SchemaInitializer)
- [x] Enums (FollowStatus, MSAServiceErrorCode)
- [x] Table definition (FollowAdvertisersTable)
- [x] Entity (FollowAdvertiserEntity)
- [x] All DTOs (Request, Response, Result, Metadata, Save)
- [x] Exceptions (MSAServerException, specific exceptions, GlobalExceptionHandler)
- [x] Repository layer (FollowAdvertiserRepository)
- [x] Service layer (FollowService)
- [x] Controller layer (FollowController)
- [x] Application configuration (application.yml)

---

## 🚀 Ready to Run!

All layers have been implemented following MSA best practices:
- ✅ Clean separation of concerns
- ✅ Consistent naming conventions
- ✅ Type-safe DTOs throughout
- ✅ Proper exception handling
- ✅ Auto-managed timestamps
- ✅ Transaction management
- ✅ Comprehensive logging

**Next Steps**: Configure MariaDB and run `./gradlew bootRun`!
