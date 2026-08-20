package com.CurdDemo.curdDemo.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Component;

import com.CurdDemo.curdDemo.DTO.RequestStudentDTO;
import com.CurdDemo.curdDemo.DTO.ResponseStudentDTO;
import com.CurdDemo.curdDemo.entity.Student;

@Component

public class LoggingDecorator implements StudentService {

    StudentService studentService;
    public LoggingDecorator(   @Qualifier("studentServiceImpl")  StudentService studentService){
        this.studentService=studentService;
    }

    @Override
    public ResponseStudentDTO createStudent(RequestStudentDTO requestStudentDTO) {
        return null;
    }

    @Override
    public List<ResponseStudentDTO> getAllStudents() {
        LoggingServiceUtil.logStart(
                "StudentServiceImpl", "createStudent");
        List<ResponseStudentDTO>result=studentService.getAllStudents();
        LoggingServiceUtil.logEnd(
                "StudentServiceImpl", "createStudent");

        return result;

    }

    @Override
    public ResponseStudentDTO getStudentDetailsbyId(Long Id) {
        LoggingServiceUtil.logStart("StudentServiceImpl", "getStudentDetailsbyId");
        ResponseStudentDTO result = studentService.getStudentDetailsbyId(Id);
        LoggingServiceUtil.logEnd("StudentServiceImpl", "getStudentDetailsbyId");
        return result;
    }

    @Override
    public Student getDetailsByRollNo(Long rollno) {
        return null;
    }

    @Override
    public ResponseStudentDTO updateStudent(Long id, Student updatedStudent) {
        return null;
    }

    @Override
    public Student deleteStudentbyId(Long id) {
        return null;
    }
    @Override
    public void markDeleteStudent(Long id) {

    }

}
