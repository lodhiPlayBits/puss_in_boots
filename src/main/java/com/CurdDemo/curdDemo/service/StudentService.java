package com.CurdDemo.curdDemo.service;

import com.CurdDemo.curdDemo.DTO.RequestStudentDTO;
import com.CurdDemo.curdDemo.DTO.ResponseStudentDTO;
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

    public List<ResponseStudentDTO> getAllStudents(){

        List<Student>students=studentRepository.findByisDeleteFalse();
        return students.stream()
                .map(this::maptoDTO)
                .toList();
    }

    public ResponseStudentDTO createStudent(RequestStudentDTO requestStudentDTO){
        Student student=mapToEntity(requestStudentDTO);
        ResponseStudentDTO responseStudentDTO= maptoDTO(student);
        responseStudentDTO.setMessage("Student Created Successfully!!!!! ♥️♥️♥️♥️");
        return responseStudentDTO;
    }

    public ResponseStudentDTO getStudentDetailsbyId(Long Id){
        Student student=studentRepository.findById(Id)
                .orElseThrow(()->new NoSuchElementException("Student not found"));
        if(student.getDelete()){
            throw new NoSuchElementException("Student Not Exist");
        }

        return maptoDTO(student);
    }

    public Student getDetailsByRollNo(Long rollno){
        return studentRepository.findByRollNo(rollno).orElseThrow(()->new NoSuchElementException("No student fount with this roll number"));
    }

    public ResponseStudentDTO updateStudent(Long id, Student updatedStudent) {

        Student student = studentRepository.findById(id)
                .orElseThrow(() ->
                        new NoSuchElementException("Student not found"));

        if(student.getDelete()){
            throw new NoSuchElementException("Student Not Exist");
        }


        student.setName(updatedStudent.getName());
        student.setAge(updatedStudent.getAge());
        student.setEmail(updatedStudent.getEmail());
        student.setRollNo(updatedStudent.getRollNo());
        student=studentRepository.save(student);
        return maptoDTO(student);
    }

    public  Student deleteStudentbyId(Long id){
        Student student = studentRepository.findById(id)
                .orElseThrow(() ->
                        new NoSuchElementException("Student not found"));

        if(student.getDelete()){
            throw new NoSuchElementException("Student Not Exist");
        }

        studentRepository.deleteById(id);
        return student;
    }

    public void markDeleteStudent(Long id){
        Student student=studentRepository.findById(id)
                .orElseThrow(()->new NoSuchElementException(("Student Not found")));
        student.setDelete(true);
        studentRepository.save(student);
        return ;

    }


    private Student mapToEntity(RequestStudentDTO requestStudentDTO){
        Student student=new Student();
        student.setName(requestStudentDTO.getName());
        student.setEmail(requestStudentDTO.getEmail());
        student.setRollNo(requestStudentDTO.getRollNo());
        student.setAge(requestStudentDTO.getAge());
        studentRepository.save(student);
        return student;
    }

    private ResponseStudentDTO maptoDTO(Student student){
        ResponseStudentDTO responseStudentDTO=new ResponseStudentDTO();
        responseStudentDTO.setName(student.getName());
        responseStudentDTO.setEmail(student.getEmail());
        responseStudentDTO.setAge(student.getAge());
        responseStudentDTO.setRollNo(student.getRollNo());
        responseStudentDTO.setId(student.getId());
//        responseStudentDTO.setMessage("student created Successfully!!!!! ♥️♥️♥️");
        return responseStudentDTO;
    }


}
