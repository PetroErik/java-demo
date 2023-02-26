package com.example.demo.controller;

import com.example.demo.entity.Course;
import com.example.demo.request.CourseRequestBody;
import com.example.demo.service.CourseRegistrationService;
import com.example.demo.service.CourseService;
import jakarta.validation.Valid;
import java.util.Map;
import lombok.AllArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

// TODO: JWT auth
@RestController
@AllArgsConstructor
@RequestMapping("/course")
public class CourseController {
  private CourseService courseService;
  private CourseRegistrationService courseRegistrationService;

  @PostMapping(path="")
  public ResponseEntity<Course> create(@Valid @RequestBody CourseRequestBody requestBody) {
    return ResponseEntity.ok(courseService.createCourse(requestBody.getName(), requestBody.getCredit()));
  }

  @GetMapping(path="/{courseId}")
  public ResponseEntity<Course> read(@PathVariable Integer courseId) {
    return ResponseEntity.ok(courseService.readCourse(courseId));
  }

  @PutMapping(path="/{courseId}")
  public ResponseEntity<Course> update(
      @PathVariable Integer courseId,
      @Valid @RequestBody CourseRequestBody requestBody
  ) {
    return ResponseEntity.ok(courseService.updateCourse(courseId, requestBody.getName(), requestBody.getCredit()));
  }

  @DeleteMapping(path="/{courseId}")
  public ResponseEntity<Map<String, Boolean>> delete(@PathVariable Integer courseId) {
    courseRegistrationService.deleteRegistrationByCourse(courseId);
    courseService.deleteCourse(courseId);

    return ResponseEntity.ok(Map.of("success", true));
  }
}
