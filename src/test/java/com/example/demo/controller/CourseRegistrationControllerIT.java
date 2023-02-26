package com.example.demo.controller;

import static org.junit.jupiter.api.Assertions.assertEquals;

import com.example.demo.Helper;
import com.example.demo.entity.Course;
import com.example.demo.entity.CourseRegistration;
import com.example.demo.entity.Student;
import com.fasterxml.jackson.core.JsonProcessingException;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.context.SpringBootTest.WebEnvironment;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;

@SpringBootTest(webEnvironment = WebEnvironment.RANDOM_PORT)
public class CourseRegistrationControllerIT {
  @Value(value = "${local.server.port}")
  private int port;
  @Autowired
  private TestRestTemplate restTemplate;
  @Autowired
  private Helper helper;

  @Test
  public void register_BothStudentAndCourseExists_CourseRegistrationReturned() throws JsonProcessingException {
    String studentName = "student";
    String studentEmail = "email";
    String courseName = "course";
    int courseCredit = 123;

    CourseRegistration courseRegistration = helper.insertCourseRegistration(restTemplate, port, studentName, studentEmail,
        courseName, courseCredit);

    assertEquals(studentName, courseRegistration.getStudent().getName());
    assertEquals(studentEmail, courseRegistration.getStudent().getEmail());
    assertEquals(courseName, courseRegistration.getCourse().getName());
    assertEquals(courseCredit, courseRegistration.getCourse().getCredit());
  }

  @Test
  public void register_StudentNotExists_ErrorReturned() throws JsonProcessingException {
    int notExistingStudentId = 999;
    Course insertedCourse = helper.insertCourse(restTemplate, port, "course", 123);

    String response = this.restTemplate.postForObject(
        "http://localhost:" + port + "/register/student/" + notExistingStudentId
            + "/course/" + insertedCourse.getId(),
        new HttpEntity<>(helper.loginForTest(restTemplate, port)),
        String.class
    );

    assertEquals("{\"message\":\"Student not exists with id: " + notExistingStudentId + "\"}", response);
  }

  @Test
  public void register_CourseNotExists_ErrorReturned() throws JsonProcessingException {
    Student insertedStudent = helper.insertStudent(restTemplate, port, "student", "email");
    int notExistingCourseId = 999;

    String response = this.restTemplate.postForObject(
        "http://localhost:" + port + "/register/student/" + insertedStudent.getId()
            + "/course/" + notExistingCourseId,
        new HttpEntity<>(helper.loginForTest(restTemplate, port)),
        String.class
    );

    assertEquals("{\"message\":\"Course not exists with id: " + notExistingCourseId + "\"}", response);
  }

  @Test
  public void delete_RegistrationExists_SuccessReturned() throws JsonProcessingException {
    CourseRegistration courseRegistration = helper.insertCourseRegistration(restTemplate, port, "student", "email",
        "course", 123);

    ResponseEntity<String> response = this.restTemplate.exchange(
        "http://localhost:" + port + "/register/" + courseRegistration.getId(),
        HttpMethod.DELETE,
        new HttpEntity<>(helper.loginForTest(restTemplate, port)),
        String.class
    );

    assertEquals("{\"success\":true}", response.getBody());
  }

  @Test
  public void deleteByStudent_StudentExists_SuccessReturned() throws JsonProcessingException {
    Student insertedStudent = helper.insertStudent(restTemplate, port, "student", "email");

    ResponseEntity<String> response = this.restTemplate.exchange(
        "http://localhost:" + port + "/register/student/" + insertedStudent.getId(),
        HttpMethod.DELETE,
        new HttpEntity<>(helper.loginForTest(restTemplate, port)),
        String.class
    );

    assertEquals("{\"success\":true}", response.getBody());
  }

  @Test
  public void deleteByStudent_StudentNotExists_ErrorReturned() {
    int notExistingStudentId = 999;

    ResponseEntity<String> response = this.restTemplate.exchange(
        "http://localhost:" + port + "/register/student/" + notExistingStudentId,
        HttpMethod.DELETE,
        new HttpEntity<>(helper.loginForTest(restTemplate, port)),
        String.class
    );

    assertEquals("{\"message\":\"Student not exists with id: " + notExistingStudentId + "\"}", response.getBody());
  }


  @Test
  public void deleteByCourse_CourseExists_SuccessReturned() throws JsonProcessingException {
    Course insertedCourse = helper.insertCourse(restTemplate, port, "course", 123);

    ResponseEntity<String> response = this.restTemplate.exchange(
        "http://localhost:" + port + "/register/course/" + insertedCourse.getId(),
        HttpMethod.DELETE,
        new HttpEntity<>(helper.loginForTest(restTemplate, port)),
        String.class
    );

    assertEquals("{\"success\":true}", response.getBody());
  }

  @Test
  public void deleteByCourse_CourseNotExists_ErrorReturned() {
    int notExistingCourseId = 999;
    ResponseEntity<String> response = this.restTemplate.exchange(
        "http://localhost:" + port + "/register/course/" + notExistingCourseId,
        HttpMethod.DELETE,
        new HttpEntity<>(helper.loginForTest(restTemplate, port)),
        String.class
    );

    assertEquals("{\"message\":\"Course not exists with id: " + notExistingCourseId + "\"}", response.getBody());
  }
}
