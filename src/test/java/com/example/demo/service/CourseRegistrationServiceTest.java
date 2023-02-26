package com.example.demo.service;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import com.example.demo.entity.Course;
import com.example.demo.entity.CourseRegistration;
import com.example.demo.entity.Student;
import com.example.demo.repository.CourseRegistrationRepository;
import java.util.List;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit.jupiter.SpringExtension;

@ExtendWith(SpringExtension.class)
@ContextConfiguration(classes = {CourseRegistrationService.class})
public class CourseRegistrationServiceTest {
  @MockBean
  private CourseRegistrationRepository courseRegistrationRepository;
  @MockBean
  private StudentService studentService;
  @MockBean
  private CourseService courseService;

  @Autowired
  private CourseRegistrationService courseRegistrationService;


  @Test
  void register_AllParametersProvided_SuccessReturned() {
    int studentId = 1;
    int courseId = 2;
    Student student = new Student(studentId, "student", "email");
    Course course = new Course(courseId, "course", 11);
    CourseRegistration courseRegistration = new CourseRegistration(student, course);
    CourseRegistration expectedRegistration = new CourseRegistration(3, student, course);

    when(studentService.readStudent(studentId)).thenReturn(student);
    when(courseService.readCourse(courseId)).thenReturn(course);
    when(courseRegistrationRepository.save(courseRegistration)).thenReturn(expectedRegistration);

    CourseRegistration actualRegistration = courseRegistrationService.register(studentId, courseId);

    verify(courseRegistrationRepository).save(courseRegistration);

    assertEquals(expectedRegistration, actualRegistration);
  }

  @Test
  void readAllRegistrationByStudent_Perfect_RepositoryCalled() {
    int studentId = 1;
    Student student = new Student(studentId, "student", "email");

    CourseRegistration expectedRegistration = new CourseRegistration();
    when(studentService.readStudent(studentId)).thenReturn(student);
    when(courseRegistrationRepository.getAllByStudent(student)).thenReturn(List.of(expectedRegistration));

    List<CourseRegistration> actualRegistration = courseRegistrationService.readAllRegistrationByStudent(studentId);

    verify(courseRegistrationRepository).getAllByStudent(student);

    assertEquals(List.of(expectedRegistration), actualRegistration);
  }

  @Test
  void deleteRegistration_Perfect_RepositoryCalled() {
    int courseId = 1;
    courseRegistrationService.deleteRegistration(courseId);

    verify(courseRegistrationRepository).deleteById(courseId);
  }

  @Test
  void deleteRegistrationByStudent_Perfect_RepositoryCalled() {
    int studentId = 1;
    Student student = new Student(studentId, "student", "email");

    when(studentService.readStudent(studentId)).thenReturn(student);

    courseRegistrationService.deleteRegistrationByStudent(studentId);

    verify(courseRegistrationRepository).deleteAllByStudent(student);
  }

  @Test
  void deleteRegistrationByCourse_Perfect_RepositoryCalled() {
    int courseId = 1;
    Course course = new Course(courseId, "course", 11);

    when(courseService.readCourse(courseId)).thenReturn(course);

    courseRegistrationService.deleteRegistrationByCourse(courseId);

    verify(courseRegistrationRepository).deleteAllByCourse(course);
  }
}
