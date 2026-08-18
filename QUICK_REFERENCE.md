# Exception Handling - Quick Reference Card

## 🎯 When to Use Which Exception

### ResourceNotFoundException (404)
```java
// Student not found
throw new ResourceNotFoundException("Student not found with id: " + id);

// Student deleted
throw new ResourceNotFoundException("Student has been deleted");

// Roll number not found
throw new ResourceNotFoundException("No student found with roll number: " + rollNo);
```

### DuplicateEmailException (409)
```java
// Duplicate email
throw new DuplicateEmailException("Email '" + email + "' already exists");

// Duplicate roll number
throw new DuplicateEmailException("Roll number '" + rollNo + "' already exists");
```

### InvalidStudentException (400)
```java
// Business validation failure
throw new InvalidStudentException("Student age must be between 7 and 100");

// Invalid state
throw new InvalidStudentException("Cannot update deleted student");
```

## 📋 Response Status Codes

| Code | Meaning | When |
|------|---------|------|
| 200 | OK | Successful GET, PUT, DELETE |
| 201 | Created | Successful POST |
| 204 | No Content | Successful soft delete (PATCH) |
| 400 | Bad Request | Validation errors, malformed JSON |
| 404 | Not Found | Resource doesn't exist, endpoint not found |
| 405 | Method Not Allowed | Wrong HTTP method |
| 409 | Conflict | Duplicate email/rollNo, constraint violations |
| 500 | Server Error | Unexpected errors |

## 🔍 Common Error Scenarios

### Scenario 1: Getting a Non-Existent Student
**Request:**
```bash
GET /api/students/999999
```
**Response:**
```json
{
  "timestamp": "2026-08-18 13:15:45",
  "status": 404,
  "error": "Resource Not Found",
  "message": "Student not found with id: 999999",
  "path": "/api/students/999999"
}
```

### Scenario 2: Creating Student with Invalid Data
**Request:**
```bash
POST /api/students/create-student
{
  "name": "",
  "email": "not-an-email",
  "age": 200,
  "rollNo": -5
}
```
**Response:**
```json
{
  "timestamp": "2026-08-18 13:15:45",
  "status": 400,
  "error": "Validation Failed",
  "message": "Invalid input data provided",
  "path": "/api/students/create-student",
  "details": [
    "name: Name should be 2 character long",
    "email: Invalid email format",
    "age: must be less than or equal to 100",
    "rollNo: must be greater than or equal to 1"
  ]
}
```

### Scenario 3: Creating Student with Duplicate Email
**Request:**
```bash
POST /api/students/create-student
{
  "name": "Jane Doe",
  "email": "existing@email.com",
  "age": 20,
  "rollNo": 12345
}
```
**Response:**
```json
{
  "timestamp": "2026-08-18 13:15:45",
  "status": 409,
  "error": "Duplicate Resource",
  "message": "A student with email 'existing@email.com' already exists",
  "path": "/api/students/create-student"
}
```

### Scenario 4: Malformed JSON
**Request:**
```bash
POST /api/students/create-student
{
  "name": "John Doe"
  "email": "john@example.com"
}
```
**Response:**
```json
{
  "timestamp": "2026-08-18 13:15:45",
  "status": 400,
  "error": "Malformed JSON Request",
  "message": "Request body is not readable or malformed. Please check your JSON format.",
  "path": "/api/students/create-student"
}
```

## 🛠️ Implementation Patterns

### Pattern 1: Find or Throw
```java
Student student = studentRepository.findById(id)
    .orElseThrow(() -> new ResourceNotFoundException("Student not found with id: " + id));
```

### Pattern 2: Check State Then Throw
```java
if(student.getDelete()){
    throw new ResourceNotFoundException("Student has been deleted");
}
```

### Pattern 3: Catch and Re-throw
```java
try {
    student = studentRepository.save(student);
} catch (DataIntegrityViolationException e) {
    if (e.getMessage().contains("email")) {
        throw new DuplicateEmailException("Email already exists");
    }
    throw e;
}
```

## 📦 Exception Class Structure

```java
// Custom Exception Template
public class YourException extends RuntimeException {
    
    public YourException(String message) {
        super(message);
    }

    public YourException(String message, Throwable cause) {
        super(message, cause);
    }
}
```

## 🔧 Testing Checklist

- [ ] Test 404: Non-existent student ID
- [ ] Test 404: Deleted student access
- [ ] Test 404: Invalid roll number
- [ ] Test 400: Empty/null required fields
- [ ] Test 400: Invalid email format
- [ ] Test 400: Age out of range
- [ ] Test 400: Negative roll number
- [ ] Test 400: Malformed JSON
- [ ] Test 409: Duplicate email
- [ ] Test 409: Duplicate roll number
- [ ] Test 405: Wrong HTTP method
- [ ] Test 404: Non-existent endpoint

## 🎓 Best Practices

### DO ✅
- Use specific exception types
- Include relevant IDs/values in messages
- Return appropriate HTTP status codes
- Log internal errors
- Keep error messages user-friendly
- Use consistent error response format

### DON'T ❌
- Expose sensitive data in error messages
- Return stack traces to clients
- Use generic exceptions for everything
- Ignore exceptions silently
- Return 200 OK for errors
- Include implementation details in messages

## 🚀 Quick Start Commands

### Run Application
```bash
.\mvnw.cmd spring-boot:run
```

### Build
```bash
.\mvnw.cmd clean install
```

### Compile Only
```bash
.\mvnw.cmd clean compile
```

## 📚 File Locations

```
src/main/java/com/CurdDemo/curdDemo/exceptions/
├── GlobalExceptionHandler.java     # Main exception handler
├── ErrorResponse.java               # Error response structure
├── ResourceNotFoundException.java   # 404 errors
├── DuplicateEmailException.java    # 409 errors
├── InvalidStudentException.java    # 400 errors
└── README.md                        # Detailed documentation
```

## 💡 Tips

1. **Always be specific**: Instead of "Error occurred", say "Student not found with id: 123"
2. **Use context**: Include IDs, emails, or values that help identify the issue
3. **Think from user perspective**: What would help them fix the problem?
4. **Log everything important**: Use logger for debugging, not printStackTrace()
5. **Test edge cases**: Null values, negative numbers, empty strings, etc.

## 🔗 Related Documentation

- [Complete Documentation](src/main/java/com/CurdDemo/curdDemo/exceptions/README.md)
- [Exception Flow Diagram](EXCEPTION_FLOW_DIAGRAM.md)
- [Implementation Summary](EXCEPTION_HANDLING_SUMMARY.md)

---

**Last Updated:** August 18, 2026  
**Version:** 1.0  
**Status:** ✅ Production Ready
