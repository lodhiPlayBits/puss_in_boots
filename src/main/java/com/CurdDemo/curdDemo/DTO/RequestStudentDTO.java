package com.CurdDemo.curdDemo.DTO;

import jakarta.validation.constraints.*;

public class RequestStudentDTO {
    @NotBlank
    @Size(min=0, max=50, message="Name should be 2 character long")
    private String name;
    @NotNull(message = "Age is required")
    @Min(value=7)
    @Max(value=100)
    private int age;

    @NotNull(message = "Roll number required")
    @Min(value=1)
    private Long rollNo;

    @NotBlank
    @Email(message="Invalid email format")
    private String email;
    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public Long getRollNo() {
        return rollNo;
    }

    public void setRollNo(Long rollNo) {
        this.rollNo = rollNo;
    }

    public int getAge() {
        return age;
    }

    public void setAge(int age) {
        this.age = age;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }


}
