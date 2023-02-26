package com.example.demo.service;

import com.example.demo.entity.Course;
import com.example.demo.entity.CourseRegistration;
import com.example.demo.repository.CourseRegistrationRepository;
import com.example.demo.repository.CourseRepository;
import java.util.List;
import java.util.Optional;
import lombok.AllArgsConstructor;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@AllArgsConstructor
public class CourseRegistrationService {
  private CourseRegistrationRepository courseRegistrationRepository;
  private StudentService studentService;
  private CourseService courseService;

  public CourseRegistration register(Integer studentId, Integer courseId) {
    return courseRegistrationRepository.save(
        new CourseRegistration(studentService.readStudent(studentId), courseService.readCourse(courseId))
    );
  }

  public List<CourseRegistration> readAllRegistrationByStudent(Integer studentId) {
    return courseRegistrationRepository.getAllByStudent(studentService.readStudent(studentId));
  }

  public void deleteRegistration(Integer id) {
    courseRegistrationRepository.deleteById(id);
  }

  public void deleteRegistrationByStudent(Integer studentId) {
    courseRegistrationRepository.deleteAllByStudent(studentService.readStudent(studentId));
  }

  public void deleteRegistrationByCourse(Integer courseId) {
    courseRegistrationRepository.deleteAllByCourse(courseService.readCourse(courseId));
  }
}
