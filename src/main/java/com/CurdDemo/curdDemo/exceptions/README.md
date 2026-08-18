# Exception Handling Guide

## Overview
This package contains a comprehensive exception handling system for the Student CRUD application using Spring Boot's `@RestControllerAdvice` pattern.

## Exception Structure

### Custom Exceptions

#### 1. **ResourceNotFoundException**
- **Extends:** `RuntimeException`
- **HTTP Status:** `404 NOT_FOUND`
- **Use Case:** When a student or resource is not found in the database
- **Example:**
  ```java
  throw new ResourceNotFoundException("Student not found with id: " + id);
  ```

#### 2. **DuplicateEmailException**
- **Extends:** `RuntimeException`
- **HTTP Status:** `409 CONFLICT`
- **Use Case:** When attempting to create/update a student with an email or roll number that already exists
- **Example:**
  ```java
  throw new DuplicateEmailException("Email 'john@example.com' already exists");
  ```

#### 3. **InvalidStudentException**
- **Extends:** `RuntimeException`
- **HTTP Status:** `400 BAD_REQUEST`
- **Use Case:** When student data fails business validation rules
- **Example:**
  ```java
  throw new InvalidStudentException("Student age must be between 7 and 100");
  ```

### Spring Framework Exceptions (Handled)

#### 4. **MethodArgumentNotValidException**
- **HTTP Status:** `400 BAD_REQUEST`
- **Triggered By:** Bean validation failures (e.g., `@NotBlank`, `@Email`, `@Min`, `@Max`)
- **Response:** Returns field-specific validation errors

#### 5. **DataIntegrityViolationException**
- **HTTP Status:** `409 CONFLICT`
- **Triggered By:** Database constraint violations (unique constraints, foreign keys)
- **Response:** Provides user-friendly messages for email/rollNo duplicates

#### 6. **HttpMessageNotReadableException**
- **HTTP Status:** `400 BAD_REQUEST`
- **Triggered By:** Malformed JSON in request body
- **Response:** Clear message about JSON format issues

#### 7. **HttpRequestMethodNotSupportedException**
- **HTTP Status:** `405 METHOD_NOT_ALLOWED`
- **Triggered By:** Using wrong HTTP method (e.g., POST instead of GET)
- **Response:** Lists supported methods

#### 8. **NoResourceFoundException**
- **HTTP Status:** `404 NOT_FOUND`
- **Triggered By:** Accessing non-existent endpoints
- **Response:** Clear message about endpoint not found

#### 9. **Exception (Global Catch-all)**
- **HTTP Status:** `500 INTERNAL_SERVER_ERROR`
- **Triggered By:** Any unhandled exception
- **Response:** Generic error message, logs full stack trace

## Error Response Format

All exceptions return a standardized `ErrorResponse` object:

```json
{
  "timestamp": "2024-01-15 10:30:45",
  "status": 404,
  "error": "Resource Not Found",
  "message": "Student not found with id: 123",
  "path": "/api/students/123",
  "details": []  // Optional, used for validation errors
}
```

### Validation Error Response Example

```json
{
  "timestamp": "2024-01-15 10:30:45",
  "status": 400,
  "error": "Validation Failed",
  "message": "Invalid input data provided",
  "path": "/api/students/create-student",
  "details": [
    "name: Name should be 2 character long",
    "email: Invalid email format",
    "age: must be greater than or equal to 7"
  ]
}
```

## Implementation Details

### GlobalExceptionHandler
The `@RestControllerAdvice` class that intercepts and handles all exceptions globally. Key features:

- **Centralized Error Handling:** All exceptions are processed in one place
- **Consistent Response Format:** All errors follow the same JSON structure
- **Proper HTTP Status Codes:** Each exception maps to appropriate status codes
- **Logging:** Critical errors are logged for debugging
- **User-Friendly Messages:** Technical details are hidden from end users

### How It Works

1. **Exception Thrown:** Service layer throws a custom or Spring exception
2. **Intercepted:** `GlobalExceptionHandler` catches the exception
3. **Processed:** Appropriate handler method processes the exception
4. **Response Created:** `ErrorResponse` object is created with proper HTTP status
5. **Returned:** Formatted JSON error response is sent to client

## Usage in Service Layer

### Example: Student Service

```java
// Resource Not Found
public ResponseStudentDTO getStudentDetailsbyId(Long Id){
    Student student = studentRepository.findById(Id)
            .orElseThrow(() -> new ResourceNotFoundException("Student not found with id: " + Id));
    
    if(student.getDelete()){
        throw new ResourceNotFoundException("Student with id " + Id + " has been deleted");
    }
    
    return maptoDTO(student);
}

// Duplicate Email/RollNo
public ResponseStudentDTO createStudent(RequestStudentDTO requestStudentDTO){
    try {
        Student student = mapToEntity(requestStudentDTO);
        // ... save and return
    } catch (DataIntegrityViolationException e) {
        if (e.getMessage().contains("email")) {
            throw new DuplicateEmailException("Email already exists");
        }
        throw e;
    }
}
```

## Best Practices

1. **Specific Exception Types:** Use the most specific exception that matches the error scenario
2. **Descriptive Messages:** Include relevant information (IDs, values) in exception messages
3. **Don't Expose Sensitive Data:** Never include passwords, tokens, or internal system details in error messages
4. **Log Internal Errors:** Use logger for 500 errors to help with debugging
5. **Consistent Naming:** Follow the pattern `[Action][Entity]Exception` (e.g., `DuplicateEmailException`)

## Testing Error Scenarios

### 1. Resource Not Found (404)
```bash
GET /api/students/999999
```

### 2. Validation Errors (400)
```bash
POST /api/students/create-student
Content-Type: application/json

{
  "name": "",
  "email": "invalid-email",
  "age": 5,
  "rollNo": -1
}
```

### 3. Duplicate Email (409)
```bash
POST /api/students/create-student
Content-Type: application/json

{
  "name": "John Doe",
  "email": "existing@email.com",  # Already exists
  "age": 20,
  "rollNo": 12345
}
```

### 4. Malformed JSON (400)
```bash
POST /api/students/create-student
Content-Type: application/json

{
  "name": "John Doe",
  "email": "john@example.com"
  "age": 20  # Missing comma
}
```

### 5. Wrong HTTP Method (405)
```bash
POST /api/students  # Should be GET
```

## Future Enhancements

Consider adding:
- **Custom validation annotations** for complex business rules
- **Exception hierarchy** for related exceptions
- **Internationalization (i18n)** for multi-language error messages
- **Detailed logging** with request correlation IDs
- **API documentation** using Swagger/OpenAPI for error responses
