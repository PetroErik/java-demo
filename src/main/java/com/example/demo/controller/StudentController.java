package com.example.demo.controller;

import com.example.demo.entity.Course;
import com.example.demo.entity.CourseRegistration;
import com.example.demo.entity.Student;
import com.example.demo.request.StudentRequestBody;
import com.example.demo.response.ReadAllCoursesResponse;
import com.example.demo.service.CourseRegistrationService;
import com.example.demo.service.StudentService;
import jakarta.validation.Valid;
import java.util.List;
import java.util.Map;
import lombok.AllArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@AllArgsConstructor
@RequestMapping("/student")
public class StudentController {
  private StudentService studentService;
  private CourseRegistrationService courseRegistrationService;

  @PostMapping(path="")
  public ResponseEntity<Student> create(@Valid @RequestBody StudentRequestBody requestBody) {
    return ResponseEntity.ok(studentService.createStudent(requestBody.getName(), requestBody.getEmail()));
  }

  @GetMapping(path="/{studentId}")
  public ResponseEntity<Student> read(@PathVariable Integer studentId) {
    return ResponseEntity.ok(studentService.readStudent(studentId));
  }

  @GetMapping(path="/{studentId}/courses")
  public ResponseEntity<ReadAllCoursesResponse> readAllCourses(@PathVariable Integer studentId) {
    Student student = studentService.readStudent(studentId);
    List<Course> courseList = courseRegistrationService.readAllRegistrationByStudent(studentId)
        .stream().map(CourseRegistration::getCourse).toList();

    return ResponseEntity.ok(new ReadAllCoursesResponse(student, courseList));
  }

  @PutMapping(path="/{studentId}")
  public ResponseEntity<Student> update(
      @PathVariable Integer studentId,
      @Valid @RequestBody StudentRequestBody requestBody
  ) {
    return ResponseEntity.ok(studentService.updateStudent(studentId, requestBody.getName(), requestBody.getEmail()));
  }

  @DeleteMapping(path="/{studentId}")
  public ResponseEntity<Map<String, Boolean>> delete(@PathVariable Integer studentId) {
    courseRegistrationService.deleteRegistrationByStudent(studentId);
    studentService.deleteStudent(studentId);

    return ResponseEntity.ok(Map.of("success", true));
  }
}
