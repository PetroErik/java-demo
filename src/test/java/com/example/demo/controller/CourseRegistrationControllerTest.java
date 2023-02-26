package com.example.demo.controller;

import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;

import com.example.demo.entity.Course;
import com.example.demo.request.CourseRequestBody;
import com.example.demo.service.CourseRegistrationService;
import com.example.demo.service.CourseService;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit.jupiter.SpringExtension;

@ExtendWith(SpringExtension.class)
@ContextConfiguration(classes = {CourseRegistrationController.class})
public class CourseRegistrationControllerTest {
  @MockBean
  private CourseRegistrationService courseRegistrationService;

  @Autowired
  private CourseRegistrationController courseRegistrationController;

  @Test
  void register_AllParametersProvided_ServiceCalledWithParameters() {
    int studentId = 1;
    int courseId = 2;

    courseRegistrationController.register(studentId, courseId);

    verify(courseRegistrationService, times(1)).register(studentId, courseId);
  }

  @Test
  void delete_AllParametersProvided_ServiceCalledWithParameters() {
    Integer id = 1;

    courseRegistrationController.delete(id);

    verify(courseRegistrationService, times(1)).deleteRegistration(id);
  }

  @Test
  void deleteByStudent_AllParametersProvided_ServiceCalledWithParameters() {
    Integer id = 1;

    courseRegistrationController.deleteByStudent(id);

    verify(courseRegistrationService, times(1)).deleteRegistrationByStudent(id);
  }

  @Test
  void deleteByCourse_AllParametersProvided_ServiceCalledWithParameters() {
    Integer id = 1;

    courseRegistrationController.deleteByCourse(id);

    verify(courseRegistrationService, times(1)).deleteRegistrationByCourse(id);
  }
}
