package com.example.demo.service;

import com.example.demo.entity.Course;
import com.example.demo.entity.Student;
import com.example.demo.repository.CourseRepository;
import com.example.demo.repository.StudentRepository;
import java.util.Optional;
import lombok.AllArgsConstructor;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.stereotype.Service;

@Service
@AllArgsConstructor
public class CourseService {
  private CourseRepository courseRepository;

  public Course createCourse(String name, Integer credit) {
    return courseRepository.save(new Course(name, credit));
  }

  public Course readCourse(Integer id) {
    return courseRepository.findById(id)
        .orElseThrow(() -> new EmptyResultDataAccessException("Course not exists with id: " + id, 1));
  }

  public Course updateCourse(Integer id, String name, Integer credit) {
    Course course = readCourse(id);
    course.setName(name);
    course.setCredit(credit);

    return courseRepository.save(course);
  }

  public void deleteCourse(Integer id) {
    courseRepository.deleteById(id);
  }
}
