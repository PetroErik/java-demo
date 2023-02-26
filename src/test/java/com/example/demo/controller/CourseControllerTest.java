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
@ContextConfiguration(classes = {CourseController.class})
public class CourseControllerTest {
  @MockBean
  private CourseService courseService;
  @MockBean
  private CourseRegistrationService courseRegistrationService;

  @Autowired
  private CourseController courseController;

  @Test
  void create_AllParametersProvided_ServiceCalledWithParameters() {
    String name = "test";
    Integer credit = 11;

    courseController.create(new CourseRequestBody(name, credit));

    verify(courseService, times(1)).createCourse(name, credit);
  }

  @Test
  void read_AllParametersProvided_ServiceCalledWithParameters() {
    Integer id = 1;

    courseController.read(id);

    verify(courseService, times(1)).readCourse(id);
  }

  @Test
  void update_AllParametersProvided_ServiceCalledWithParameters() {
    Integer id = 1;
    String name = "name";
    Integer credit = 11;
    Course expectedCourse = new Course(id, name, credit);

    courseController.update(id, new CourseRequestBody(expectedCourse.getName(), expectedCourse.getCredit()));

    verify(courseService, times(1)).updateCourse(id, name, credit);
  }

  @Test
  void delete_AllParametersProvided_ServiceCalledWithParameters() {
    Integer id = 1;

    courseController.delete(id);

    verify(courseRegistrationService, times(1)).deleteRegistrationByCourse(id);
    verify(courseService, times(1)).deleteCourse(id);
  }
}
