package com.CurdDemo.curdDemo.service;

import java.time.LocalDateTime;
import java.util.List;

import com.CurdDemo.curdDemo.annotation.TrackExecutionTime;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.stereotype.Service;

import com.CurdDemo.curdDemo.DTO.RequestStudentDTO;
import com.CurdDemo.curdDemo.DTO.ResponseStudentDTO;
import com.CurdDemo.curdDemo.entity.Student;
import com.CurdDemo.curdDemo.exceptions.DuplicateEmailException;
import com.CurdDemo.curdDemo.exceptions.ResourceNotFoundException;
import com.CurdDemo.curdDemo.repository.StudentRepository;

@Service
public class StudentServiceImpl implements StudentService {
    private final StudentRepository studentRepository;
    public StudentServiceImpl(StudentRepository studentRepository){
        this.studentRepository=studentRepository;
    }

    @Override
    public List<ResponseStudentDTO> getAllStudents(){

        List<Student>students=studentRepository.findByisDeleteFalse();
        return students.stream()
                .map(this::maptoDTO)
                .toList();
    }
    @Override

    public ResponseStudentDTO createStudent(RequestStudentDTO requestStudentDTO){
        try {
            Student student = mapToEntity(requestStudentDTO);
            ResponseStudentDTO responseStudentDTO = maptoDTO(student);
            responseStudentDTO.setCreatedAt(LocalDateTime.now());
            responseStudentDTO.setMessage("Student Created Successfully!!!!! ♥️♥️♥️♥️");
            return responseStudentDTO;
        } catch (DataIntegrityViolationException e) {
            // Handle duplicate email or rollNo
            if (e.getMessage().contains("email")) {
                throw new DuplicateEmailException("A student with email '" + requestStudentDTO.getEmail() + "' already exists");
            } else if (e.getMessage().contains("rollNo") || e.getMessage().contains("roll_no")) {
                throw new DuplicateEmailException("A student with roll number '" + requestStudentDTO.getRollNo() + "' already exists");
            }
            throw e; // Re-throw if it's a different constraint violation
        }
    }

    @Override
    public ResponseStudentDTO getStudentDetailsbyId(Long Id){
        Student student = studentRepository.findById(Id)
                .orElseThrow(() -> new ResourceNotFoundException("Student not found with id: " + Id));

        if(student.getDelete()){
            throw new ResourceNotFoundException("Student with id " + Id + " has been deleted");
        }

        return maptoDTO(student);
    }

    @Override
    @TrackExecutionTime
    public Student getDetailsByRollNo(Long rollno){
        return studentRepository.findByRollNo(rollno)
                .orElseThrow(() -> new ResourceNotFoundException("No student found with roll number: " + rollno));
    }
    @Override
    public ResponseStudentDTO updateStudent(Long id, Student updatedStudent) {
        Student student = studentRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Student not found with id: " + id));

        if(student.getDelete()){
            throw new ResourceNotFoundException("Student with id " + id + " has been deleted and cannot be updated");
        }

        try {
            student.setName(updatedStudent.getName());
            student.setAge(updatedStudent.getAge());
            student.setEmail(updatedStudent.getEmail());
            student.setRollNo(updatedStudent.getRollNo());
            student = studentRepository.save(student);
            return maptoDTO(student);
        } catch (DataIntegrityViolationException e) {
            // Handle duplicate email or rollNo during update
            if (e.getMessage().contains("email")) {
                throw new DuplicateEmailException("Email '" + updatedStudent.getEmail() + "' is already in use by another student");
            } else if (e.getMessage().contains("rollNo") || e.getMessage().contains("roll_no")) {
                throw new DuplicateEmailException("Roll number '" + updatedStudent.getRollNo() + "' is already in use by another student");
            }
            throw e;
        }
    }

    @Override
    public Student deleteStudentbyId(Long id){
        Student student = studentRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Student not found with id: " + id));

        if(student.getDelete()){
            throw new ResourceNotFoundException("Student with id " + id + " has already been deleted");
        }

        studentRepository.deleteById(id);
        return student;
    }

    public void markDeleteStudent(Long id){
        Student student = studentRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Student not found with id: " + id));

        if(student.getDelete()){
            throw new ResourceNotFoundException("Student with id " + id + " has already been marked as deleted");
        }

        student.setDelete(true);
        studentRepository.save(student);
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
