package com.example.demo.service;

import com.example.demo.entity.Student;
import com.example.demo.repository.StudentRepository;
import java.util.Optional;
import lombok.AllArgsConstructor;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.stereotype.Service;

@Service
@AllArgsConstructor
public class StudentService {
  private StudentRepository studentRepository;

  public Student createStudent(String name, String email) {
    return studentRepository.save(new Student(name, email));
  }

  public Student readStudent(Integer id) {
    return studentRepository.findById(id)
        .orElseThrow(() -> new EmptyResultDataAccessException("Student not exists with id: " + id, 1));
  }

  public Student updateStudent(Integer id, String name, String email) {
    Student student = readStudent(id);
    student.setName(name);
    student.setEmail(email);

    return studentRepository.save(student);
  }

  public void deleteStudent(Integer id) {
    studentRepository.deleteById(id);
  }
}
