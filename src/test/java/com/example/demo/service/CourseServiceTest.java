package com.example.demo.service;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import com.example.demo.entity.Course;
import com.example.demo.repository.CourseRepository;
import java.util.Optional;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit.jupiter.SpringExtension;

@ExtendWith(SpringExtension.class)
@ContextConfiguration(classes = {CourseService.class})
public class CourseServiceTest {
  @MockBean
  private CourseRepository courseRepository;

  @Autowired
  private CourseService courseService;


  @Test
  void createCourse_AllParametersProvided_CourseReturned() {
    String name = "test";
    Integer credit = 11;
    Course course = new Course(name, credit);

    Course expectedCourse = new Course(1, name, credit);

    when(courseRepository.save(course)).thenReturn(expectedCourse);

    Course actualCourse = courseService.createCourse(name, credit);

    verify(courseRepository).save(course);

    assertEquals(expectedCourse, actualCourse);
  }

  @Test
  void readCourse_CourseExists_CourseReturned() {
    int id = 1;
    Course course = new Course(id, "name", 11);

    when(courseRepository.findById(id)).thenReturn(Optional.of(course));

    Course actualCourse = courseService.readCourse(id);

    verify(courseRepository).findById(id);

    assertEquals(course, actualCourse);
  }

  @Test
  void readCourse_CourseNotExists_ExceptionThrown() {
    int id = 1;

    when(courseRepository.findById(id)).thenReturn(Optional.empty());

    EmptyResultDataAccessException exception = assertThrows(
        EmptyResultDataAccessException.class,
        () -> courseService.readCourse(id)
    );

    verify(courseRepository).findById(id);

    assertEquals("Course not exists with id: 1", exception.getMessage());
  }

  @Test
  void updateCourse_CourseExists_CourseReturned() {
    int id = 1;
    String name = "name";
    Integer credit = 11;
    Course course = new Course(id, name, credit);

    when(courseRepository.findById(id)).thenReturn(Optional.of(course));
    when(courseRepository.save(course)).thenReturn(course);

    Course actualCourse = courseService.updateCourse(id, name, credit);

    verify(courseRepository).findById(id);
    verify(courseRepository).save(course);

    assertEquals(course, actualCourse);
  }

  @Test
  void updateCourse_CourseNotExists_ExceptionThrown() {
    int id = 1;
    String name = "name";
    Integer credit = 11;

    when(courseRepository.findById(id)).thenReturn(Optional.empty());

    EmptyResultDataAccessException exception = assertThrows(
        EmptyResultDataAccessException.class,
        () -> courseService.updateCourse(id, name, credit)
    );

    verify(courseRepository).findById(id);
    verify(courseRepository, never()).save(any());

    assertEquals("Course not exists with id: 1", exception.getMessage());
  }

  @Test
  void deleteCourse_IdProvided_CourseDeleted() {
    int id = 1;

    courseService.deleteCourse(id);

    verify(courseRepository).deleteById(id);
  }
}
