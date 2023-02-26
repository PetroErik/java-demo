package com.example.demo.service;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import com.example.demo.entity.Student;
import com.example.demo.repository.StudentRepository;
import java.util.Optional;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit.jupiter.SpringExtension;

@ExtendWith(SpringExtension.class)
@ContextConfiguration(classes = {StudentService.class})
public class StudentServiceTest {
  @MockBean
  private StudentRepository studentRepository;

  @Autowired
  private StudentService studentService;


  @Test
  void createStudent_AllParametersProvided_StudentReturned() {
    String name = "test";
    String email = "test@example.com";
    Student entity = new Student(name, email);

    Student expectedStudent = new Student(1, name, email);

    when(studentRepository.save(entity)).thenReturn(expectedStudent);

    Student actualStudent = studentService.createStudent(name, email);

    verify(studentRepository).save(entity);

    assertEquals(expectedStudent, actualStudent);
  }

  @Test
  void readStudent_StudentExists_StudentReturned() {
    int id = 1;
    Student student = new Student(id, "name", "email");

    when(studentRepository.findById(id)).thenReturn(Optional.of(student));

    Student actualStudent = studentService.readStudent(id);

    verify(studentRepository).findById(id);

    assertEquals(student, actualStudent);
  }

  @Test
  void readStudent_StudentNotExists_ExceptionThrown() {
    int id = 1;

    when(studentRepository.findById(id)).thenReturn(Optional.empty());

    EmptyResultDataAccessException exception = assertThrows(
        EmptyResultDataAccessException.class,
        () -> studentService.readStudent(id)
    );

    verify(studentRepository).findById(id);

    assertEquals("Student not exists with id: 1", exception.getMessage());
  }

  @Test
  void updateStudent_StudentExists_StudentReturned() {
    int id = 1;
    String name = "name";
    String email = "email";
    Student student = new Student(id, name, email);

    when(studentRepository.findById(id)).thenReturn(Optional.of(student));
    when(studentRepository.save(student)).thenReturn(student);

    Student actualStudent = studentService.updateStudent(id, name, email);

    verify(studentRepository).findById(id);
    verify(studentRepository).save(student);

    assertEquals(student, actualStudent);
  }

  @Test
  void updateStudent_StudentNotExists_ExceptionThrown() {
    int id = 1;
    String name = "name";
    String email = "email";

    when(studentRepository.findById(id)).thenReturn(Optional.empty());

    EmptyResultDataAccessException exception = assertThrows(
        EmptyResultDataAccessException.class,
        () -> studentService.updateStudent(id, name, email)
    );

    verify(studentRepository).findById(id);
    verify(studentRepository, never()).save(any());

    assertEquals("Student not exists with id: 1", exception.getMessage());
  }

  @Test
  void deleteStudent_IdProvided_StudentDeleted() {
    int id = 1;

    studentService.deleteStudent(id);

    verify(studentRepository).deleteById(id);
  }
}
