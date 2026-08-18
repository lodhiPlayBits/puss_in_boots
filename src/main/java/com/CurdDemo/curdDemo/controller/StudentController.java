package com.CurdDemo.curdDemo.controller;


import com.CurdDemo.curdDemo.DTO.RequestStudentDTO;
import com.CurdDemo.curdDemo.DTO.ResponseStudentDTO;
import com.CurdDemo.curdDemo.entity.Student;
import com.CurdDemo.curdDemo.repository.StudentRepository;
import com.CurdDemo.curdDemo.service.StudentService;
import org.springframework.http.HttpStatus;
import org.springframework.http.HttpStatusCode;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/api/students")


public class StudentController {

    private final StudentService studentService;

    public StudentController(StudentService studentService){
        this.studentService=studentService;
    }

    @GetMapping
    public ResponseEntity<List<ResponseStudentDTO>> getAllStudents(){
        List<ResponseStudentDTO>students=studentService.getAllStudents();

        return ResponseEntity.ok(students);
    }

    @GetMapping("/{id}")
    public ResponseEntity<ResponseStudentDTO>getStudentDetailsbyId(@PathVariable Long id){
        ResponseStudentDTO s=studentService.getStudentDetailsbyId(id);
        return ResponseEntity.ok(s);
    }

    @GetMapping("/rollno/{rollno}")
    public ResponseEntity<Student>getDetailsByRollNo(@PathVariable Long rollno){
        Student s=studentService.getDetailsByRollNo(rollno);
        return ResponseEntity
                .status(HttpStatus.FOUND)
                .body(s);
    }

    @PostMapping("/create-student")
    public ResponseEntity<ResponseStudentDTO> createStudent(@RequestBody RequestStudentDTO requestStudentDTO){
       ResponseStudentDTO responseStudentDTO =studentService.createStudent(requestStudentDTO);
       return ResponseEntity
               .status(HttpStatus.CREATED)
               .body(responseStudentDTO);
    }
    @PutMapping("/{id}")
    public ResponseEntity<ResponseStudentDTO>updateStudent(@PathVariable Long id, @RequestBody Student body){
        ResponseStudentDTO s=studentService.updateStudent(id,body);
        return ResponseEntity.ok(s);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Student>deleteStudentDetailsbyId(@PathVariable Long id){
        Student s=studentService.deleteStudentbyId(id);
        return ResponseEntity.ok(s);
    }

    @PatchMapping("/{id}")
    public ResponseEntity<Void> deleteStudetnDetailsbyIdSoft(@PathVariable Long id){
        studentService.markDeleteStudent(id);
        return ResponseEntity.noContent().build();
    }



}
