package com.CurdDemo.curdDemo.repository;

import com.CurdDemo.curdDemo.entity.Student;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface StudentRepository extends JpaRepository<Student,Long> {

    Optional<Student>findByRollNo(Long rollNumber);
    List<Student> findByisDeleteFalse();


}
