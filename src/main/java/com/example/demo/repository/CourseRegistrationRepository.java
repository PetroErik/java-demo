package com.example.demo.repository;

import com.example.demo.entity.Course;
import com.example.demo.entity.CourseRegistration;
import com.example.demo.entity.Student;
import java.util.List;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

@Repository
public interface CourseRegistrationRepository extends CrudRepository<CourseRegistration, Integer> {
  List<CourseRegistration> getAllByStudent(Student student);
  @Transactional
  void deleteAllByStudent(Student student);
  @Transactional
  void deleteAllByCourse(Course course);
}
