package com.CurdDemo.curdDemo.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.CurdDemo.curdDemo.DTO.RequestStudentDTO;
import com.CurdDemo.curdDemo.DTO.ResponseStudentDTO;
import com.CurdDemo.curdDemo.entity.Student;
import com.CurdDemo.curdDemo.service.StudentService;

import jakarta.validation.Valid;

@RestController
@RequestMapping("/api/students")


public class StudentController {

    private final StudentService studentService;

    public StudentController(@Qualifier("executionTimeService") StudentService studentService){
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
        return ResponseEntity.ok(s);
    }

    @PostMapping("/create-student")
    public ResponseEntity<ResponseStudentDTO> createStudent(@Valid @RequestBody RequestStudentDTO requestStudentDTO){
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
