# Exception Handling Flow Diagram

## System Architecture

```
┌─────────────────────────────────────────────────────────────────────────────┐
│                              CLIENT REQUEST                                  │
└─────────────────────────────────────────────────────────────────────────────┘
                                      │
                                      ▼
┌─────────────────────────────────────────────────────────────────────────────┐
│                         StudentController                                    │
│  • @GetMapping                                                              │
│  • @PostMapping                                                             │
│  • @PutMapping                                                              │
│  • @DeleteMapping                                                           │
│  • @PatchMapping                                                            │
└─────────────────────────────────────────────────────────────────────────────┘
                                      │
                                      ▼
┌─────────────────────────────────────────────────────────────────────────────┐
│                          StudentService                                      │
│  • getAllStudents()                                                         │
│  • getStudentDetailsbyId()        ──► ResourceNotFoundException (404)      │
│  • getDetailsByRollNo()           ──► ResourceNotFoundException (404)      │
│  • createStudent()                ──► DuplicateEmailException (409)        │
│  • updateStudent()                ──► ResourceNotFoundException (404)      │
│  • deleteStudentbyId()            ──► DuplicateEmailException (409)        │
│  • markDeleteStudent()            ──► ResourceNotFoundException (404)      │
└─────────────────────────────────────────────────────────────────────────────┘
                                      │
                                      ▼
┌─────────────────────────────────────────────────────────────────────────────┐
│                        StudentRepository (JPA)                               │
│  • findById()                                                               │
│  • findByRollNo()                                                           │
│  • save()                         ──► DataIntegrityViolationException      │
│  • deleteById()                                                             │
│  • findByisDeleteFalse()                                                    │
└─────────────────────────────────────────────────────────────────────────────┘
                                      │
                    ╔═════════════════╩══════════════════╗
                    ▼                                    ▼
        ┌───────────────────┐              ┌───────────────────────┐
        │   SUCCESS PATH    │              │   EXCEPTION PATH      │
        │                   │              │                       │
        │  Return Entity    │              │  Throw Exception      │
        └───────────────────┘              └───────────────────────┘
                    │                                    │
                    │                                    ▼
                    │              ┌─────────────────────────────────────────┐
                    │              │   GlobalExceptionHandler                │
                    │              │   (@RestControllerAdvice)               │
                    │              │                                         │
                    │              │  @ExceptionHandler methods:             │
                    │              │  • ResourceNotFoundException            │
                    │              │  • DuplicateEmailException              │
                    │              │  • InvalidStudentException              │
                    │              │  • MethodArgumentNotValidException      │
                    │              │  • DataIntegrityViolationException      │
                    │              │  • HttpMessageNotReadableException      │
                    │              │  • HttpRequestMethodNotSupportedException│
                    │              │  • NoResourceFoundException             │
                    │              │  • Exception (catch-all)                │
                    │              └─────────────────────────────────────────┘
                    │                                    │
                    │                                    ▼
                    │              ┌─────────────────────────────────────────┐
                    │              │         ErrorResponse Object            │
                    │              │  • timestamp                            │
                    │              │  • status (HTTP code)                   │
                    │              │  • error (error type)                   │
                    │              │  • message (description)                │
                    │              │  • path (request URI)                   │
                    │              │  • details (optional field errors)      │
                    │              └─────────────────────────────────────────┘
                    │                                    │
                    ▼                                    ▼
┌─────────────────────────────────────────────────────────────────────────────┐
│                       JSON RESPONSE TO CLIENT                                │
│                                                                              │
│  SUCCESS (2xx):                    ERROR (4xx/5xx):                         │
│  {                                 {                                        │
│    "id": 1,                          "timestamp": "2026-08-18 13:15:45",   │
│    "name": "John Doe",               "status": 404,                        │
│    "email": "john@ex.com",           "error": "Resource Not Found",        │
│    "age": 20,                        "message": "Student not found...",    │
│    "rollNo": 12345,                  "path": "/api/students/999"           │
│    "message": "Success!"           }                                        │
│  }                                                                          │
└─────────────────────────────────────────────────────────────────────────────┘
```

## Exception Type Mapping

```
┌──────────────────────────────────────────────────────────────────────────┐
│                        EXCEPTION → HTTP STATUS                            │
├──────────────────────────────────────────────────────────────────────────┤
│                                                                           │
│  CLIENT ERRORS (4xx)                                                      │
│  ├─ ResourceNotFoundException          → 404 NOT_FOUND                   │
│  ├─ InvalidStudentException            → 400 BAD_REQUEST                 │
│  ├─ MethodArgumentNotValidException    → 400 BAD_REQUEST                 │
│  ├─ HttpMessageNotReadableException    → 400 BAD_REQUEST                 │
│  ├─ HttpRequestMethodNotSupportedException → 405 METHOD_NOT_ALLOWED      │
│  ├─ DuplicateEmailException            → 409 CONFLICT                    │
│  └─ DataIntegrityViolationException    → 409 CONFLICT                    │
│                                                                           │
│  SERVER ERRORS (5xx)                                                      │
│  └─ Exception (catch-all)              → 500 INTERNAL_SERVER_ERROR       │
│                                                                           │
└──────────────────────────────────────────────────────────────────────────┘
```

## Request Lifecycle Example

### Example 1: Successful Request

```
1. Client → GET /api/students/1
2. Controller → studentService.getStudentDetailsbyId(1)
3. Service → studentRepository.findById(1)
4. Repository → Returns Optional<Student>
5. Service → Maps to ResponseStudentDTO
6. Controller → Returns ResponseEntity.ok(dto)
7. Client ← 200 OK with student data
```

### Example 2: Student Not Found

```
1. Client → GET /api/students/999999
2. Controller → studentService.getStudentDetailsbyId(999999)
3. Service → studentRepository.findById(999999)
4. Repository → Returns Optional.empty()
5. Service → Throws ResourceNotFoundException
6. GlobalExceptionHandler → Catches exception
7. GlobalExceptionHandler → Creates ErrorResponse (404)
8. Client ← 404 NOT_FOUND with error JSON
```

### Example 3: Validation Failure

```
1. Client → POST /api/students/create-student
   Body: { "name": "", "email": "invalid", "age": 5 }
2. Controller → @Valid annotation validates RequestStudentDTO
3. Spring → Detects validation errors
4. Spring → Throws MethodArgumentNotValidException
5. GlobalExceptionHandler → Catches exception
6. GlobalExceptionHandler → Extracts field errors
7. GlobalExceptionHandler → Creates ErrorResponse (400) with details
8. Client ← 400 BAD_REQUEST with validation errors
```

### Example 4: Duplicate Email

```
1. Client → POST /api/students/create-student
   Body: { "email": "existing@email.com", ... }
2. Controller → studentService.createStudent(dto)
3. Service → studentRepository.save(student)
4. Repository → Throws DataIntegrityViolationException (unique constraint)
5. Service → Catches exception, checks message
6. Service → Throws DuplicateEmailException
7. GlobalExceptionHandler → Catches exception
8. GlobalExceptionHandler → Creates ErrorResponse (409)
9. Client ← 409 CONFLICT with duplicate error message
```

## Best Practices Applied

✅ **Separation of Concerns**
   - Controller: HTTP handling
   - Service: Business logic & custom exceptions
   - Repository: Data access
   - GlobalExceptionHandler: Error handling

✅ **Consistent Error Format**
   - Every error uses ErrorResponse
   - Timestamp included for debugging
   - Request path tracked

✅ **Semantic HTTP Status Codes**
   - 404: Resource not found
   - 400: Bad request/validation
   - 409: Conflict (duplicates)
   - 405: Wrong HTTP method
   - 500: Server error

✅ **User-Friendly Messages**
   - Clear, actionable error descriptions
   - No technical stack traces exposed
   - Field-level validation details

✅ **Logging & Debugging**
   - Critical errors logged with SLF4J
   - Stack traces captured server-side
   - Request correlation possible

✅ **Graceful Degradation**
   - Catch-all handler for unexpected errors
   - No 500 errors without logging
   - System remains stable

## Coverage Matrix

| Scenario | Exception | Status | Handled By |
|----------|-----------|--------|------------|
| Student not found | ResourceNotFoundException | 404 | Service layer |
| Invalid student ID | ResourceNotFoundException | 404 | Service layer |
| Deleted student access | ResourceNotFoundException | 404 | Service layer |
| Duplicate email | DuplicateEmailException | 409 | Service layer |
| Duplicate roll number | DuplicateEmailException | 409 | Service layer |
| Invalid email format | MethodArgumentNotValidException | 400 | Bean validation |
| Age out of range | MethodArgumentNotValidException | 400 | Bean validation |
| Missing required field | MethodArgumentNotValidException | 400 | Bean validation |
| Malformed JSON | HttpMessageNotReadableException | 400 | Spring |
| Wrong HTTP method | HttpRequestMethodNotSupportedException | 405 | Spring |
| Invalid endpoint | NoResourceFoundException | 404 | Spring |
| Database constraint | DataIntegrityViolationException | 409 | Spring + Service |
| Unexpected error | Exception | 500 | Global handler |

## Conclusion

This exception handling system provides:
- **Complete coverage** of all error scenarios
- **Consistent API responses** for better client integration
- **Easy maintenance** with centralized handling
- **Production-ready** error management
- **Developer-friendly** with clear messages and logging
