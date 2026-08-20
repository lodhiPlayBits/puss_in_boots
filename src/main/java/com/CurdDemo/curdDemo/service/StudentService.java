package com.CurdDemo.curdDemo.service;

import com.CurdDemo.curdDemo.DTO.RequestStudentDTO;
import com.CurdDemo.curdDemo.DTO.ResponseStudentDTO;
import com.CurdDemo.curdDemo.entity.Student;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;

import java.util.List;
@Component
public interface StudentService {


    ResponseStudentDTO createStudent(RequestStudentDTO requestStudentDTO);
    List<ResponseStudentDTO> getAllStudents();
    ResponseStudentDTO getStudentDetailsbyId(Long Id);
    Student getDetailsByRollNo(Long rollno);
    ResponseStudentDTO updateStudent(Long id, Student updatedStudent);

    Student deleteStudentbyId(Long id);

    void markDeleteStudent(Long id);




}