package com.hostelms.service;

import com.hostelms.model.Student;
import com.hostelms.repository.StudentRepository;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * Business logic layer for Student. Controllers never talk to the
 * repository directly - they go through this service.
 */
@Service
public class StudentService {

    private final StudentRepository studentRepository;

    public StudentService(StudentRepository studentRepository) {
        this.studentRepository = studentRepository;
    }

    public List<Student> findAll() {
        return studentRepository.findAll();
    }

    public Student findById(Long id) {
        return studentRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Student not found with id " + id));
    }

    public Student save(Student student) {
        return studentRepository.save(student);
    }

    public void deleteById(Long id) {
        studentRepository.deleteById(id);
    }
}
