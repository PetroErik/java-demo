package com.example.demo.controller;

import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;

import com.example.demo.entity.Student;
import com.example.demo.request.StudentRequestBody;
import com.example.demo.service.CourseRegistrationService;
import com.example.demo.service.StudentService;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit.jupiter.SpringExtension;

@ExtendWith(SpringExtension.class)
@ContextConfiguration(classes = {StudentController.class})
public class StudentControllerTest {
  @MockBean
  private StudentService studentService;
  @MockBean
  private CourseRegistrationService courseRegistrationService;

  @Autowired
  private StudentController studentController;

  @Test
  void create_AllParametersProvided_ServiceCalledWithParameters() {
    String name = "test";
    String email = "test@example.com";

    studentController.create(new StudentRequestBody(name, email));

    verify(studentService, times(1)).createStudent(name, email);
  }

  @Test
  void read_AllParametersProvided_ServiceCalledWithParameters() {
    Integer id = 1;

    studentController.read(id);

    verify(studentService, times(1)).readStudent(id);
  }

  @Test
  void readAllCourses_AllParametersProvided_ServiceCalledWithParameters() {
    Integer id = 1;

    studentController.readAllCourses(id);

    verify(studentService, times(1)).readStudent(id);
    verify(courseRegistrationService, times(1)).readAllRegistrationByStudent(id);
  }

  @Test
  void update_AllParametersProvided_ServiceCalledWithParameters() {
    Integer id = 1;
    String name = "name";
    String email = "email";
    Student expectedStudent = new Student(id, name, email);

    studentController.update(id, new StudentRequestBody(expectedStudent.getName(), expectedStudent.getEmail()));

    verify(studentService, times(1)).updateStudent(id, name, email);
  }

  @Test
  void delete_AllParametersProvided_ServiceCalledWithParameters() {
    Integer id = 1;

    studentController.delete(id);

    verify(courseRegistrationService, times(1)).deleteRegistrationByStudent(id);
    verify(studentService, times(1)).deleteStudent(id);
  }
}
