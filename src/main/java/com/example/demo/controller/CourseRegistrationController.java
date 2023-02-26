package com.example.demo.controller;

import com.example.demo.entity.CourseRegistration;
import com.example.demo.service.CourseRegistrationService;
import java.util.Map;
import lombok.AllArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@AllArgsConstructor
@RequestMapping("/register")
public class CourseRegistrationController {
  private CourseRegistrationService courseRegistrationService;

  @PostMapping(path="/student/{studentId}/course/{courseId}")
  public ResponseEntity<CourseRegistration> register(@PathVariable Integer studentId, @PathVariable Integer courseId) {
    return ResponseEntity.ok(courseRegistrationService.register(studentId, courseId));
  }

  @DeleteMapping(path="/{registerId}")
  public ResponseEntity<Map<String, Boolean>> delete(@PathVariable Integer registerId) {
    courseRegistrationService.deleteRegistration(registerId);

    return ResponseEntity.ok(Map.of("success", true));
  }

  @DeleteMapping(path="/student/{studentId}")
  public ResponseEntity<Map<String, Boolean>> deleteByStudent(@PathVariable Integer studentId) {
    courseRegistrationService.deleteRegistrationByStudent(studentId);

    return ResponseEntity.ok(Map.of("success", true));
  }

  @DeleteMapping(path="/course/{courseId}")
  public ResponseEntity<Map<String, Boolean>> deleteByCourse(@PathVariable Integer courseId) {
    courseRegistrationService.deleteRegistrationByCourse(courseId);

    return ResponseEntity.ok(Map.of("success", true));
  }
}
