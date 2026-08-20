package com.CurdDemo.curdDemo.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Component;

import com.CurdDemo.curdDemo.DTO.RequestStudentDTO;
import com.CurdDemo.curdDemo.DTO.ResponseStudentDTO;
import com.CurdDemo.curdDemo.entity.Student;


@Component
public class ExecutionTimeService implements StudentService {
    private LoggingDecorator loggingDecorator;

    public ExecutionTimeService(  @Qualifier("loggingDecorator")  LoggingDecorator loggingDecorator){
        this.loggingDecorator=loggingDecorator;
    }

    @Override
    public ResponseStudentDTO createStudent(RequestStudentDTO requestStudentDTO) {
        return null;
    }

    @Override
    public List<ResponseStudentDTO> getAllStudents() {
        long start=System.currentTimeMillis();
        List<ResponseStudentDTO> result = loggingDecorator.getAllStudents();
        long end=System.currentTimeMillis();
        System.out.println(end-start);

        return result;
    }

    @Override
    public ResponseStudentDTO getStudentDetailsbyId(Long Id) {
        long start = System.currentTimeMillis();
        ResponseStudentDTO result = loggingDecorator.getStudentDetailsbyId(Id);
        long end = System.currentTimeMillis();
        System.out.println("Execution time: " + (end - start) + " ms");
        return result;
    }

    @Override
    public void markDeleteStudent(Long id) {

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
}
