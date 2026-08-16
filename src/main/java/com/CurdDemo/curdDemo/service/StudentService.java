package com.CurdDemo.curdDemo.service;

import com.CurdDemo.curdDemo.entity.Student;
import com.CurdDemo.curdDemo.repository.StudentRepository;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.NoSuchElementException;
import java.util.Optional;

@Service
public class StudentService {
    private final StudentRepository studentRepository;
    public StudentService(StudentRepository studentRepository){
        this.studentRepository=studentRepository;
    }

    public List<Student> getAllStudents(){

        return studentRepository.findAll();

    }

    public Student createStudent(Student student){

        System.out.println("Call from Student Service 💀💀 !!!!");
        return studentRepository.save(student);
    }

    public Student getStudentDetailsbyId(Long Id){
        return studentRepository.findById(Id).orElseThrow(()->new NoSuchElementException("Student not found"));
    }

    public Student getDetailsByRollNo(Long rollno){
        return studentRepository.findByRollNo(rollno).orElseThrow(()->new NoSuchElementException("No student fount with this roll number"));
    }

    public Student updateStudent(Long id, Student updatedStudent) {

        Student student = studentRepository.findById(id)
                .orElseThrow(() ->
                        new NoSuchElementException("Student not found"));

        student.setName(updatedStudent.getName());
        student.setAge(updatedStudent.getAge());
        student.setEmail(updatedStudent.getEmail());
        student.setRollNo(updatedStudent.getRollNo());

        return studentRepository.save(student);
    }

    public  Student deleteStudentbyId(Long id){
        Student student = studentRepository.findById(id)
                .orElseThrow(() ->
                        new NoSuchElementException("Student not found"));

        studentRepository.deleteById(id);
        return student;
    }

}
