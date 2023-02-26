package com.example.demo.controller;

import static org.junit.jupiter.api.Assertions.assertEquals;

import com.example.demo.Helper;
import com.example.demo.entity.Course;
import com.example.demo.entity.Student;
import com.example.demo.response.ReadAllCoursesResponse;
import com.fasterxml.jackson.core.JsonProcessingException;
import java.util.Objects;
import java.util.stream.Stream;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.context.SpringBootTest.WebEnvironment;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

@SpringBootTest(webEnvironment = WebEnvironment.RANDOM_PORT)
public class StudentControllerIT {
  @Value(value = "${local.server.port}")
  private int port;
  @Autowired
  private TestRestTemplate restTemplate;
  @Autowired
  private Helper helper;

  @Test
  public void create_AllParametersProvided_StudentReturnedInResponse() throws JsonProcessingException {
    String expectedName = "name";
    String expectedEmail = "email";

    Student actualStudent = helper.insertStudent(restTemplate, port, expectedName, expectedEmail);

    assertEquals(expectedName, actualStudent.getName());
    assertEquals(expectedEmail, actualStudent.getEmail());
  }

  @ParameterizedTest(name = "{0}")
  @MethodSource("invalidRequestProvider")
  public void create_InvalidRequests_ErrorMessageReturned(String desc, String requestBody, String responseMessage) {
    String response = restTemplate.postForObject(
        "http://localhost:" + port + "/student",
        new HttpEntity<>(requestBody, helper.loginForTest(restTemplate, port)),
        String.class
    );

    assertEquals(responseMessage, response);
  }

  @Test
  public void read_StudentExists_StudentReturnedInResponse() throws JsonProcessingException {
    String expectedName = "name";
    String expectedEmail = "email";

    Student insertedStudent = helper.insertStudent(restTemplate, port, expectedName, expectedEmail);

    ResponseEntity<String> response = this.restTemplate.exchange(
        "http://localhost:" + port + "/student/" + insertedStudent.getId(), HttpMethod.GET,
        new HttpEntity<>(helper.loginForTest(restTemplate, port)),
        String.class
    );

    Student actualStudent = helper.getObjectFromResponse(response.getBody(), Student.class);

    assertEquals(expectedName, actualStudent.getName());
    assertEquals(expectedEmail, actualStudent.getEmail());
  }

  @Test
  public void read_StudentNotExists_ErrorReturned() {
    int notExistingId = 999;

    ResponseEntity<String> response = this.restTemplate.exchange(
        "http://localhost:" + port + "/student/" + notExistingId, HttpMethod.GET,
        new HttpEntity<>(helper.loginForTest(restTemplate, port)),
        String.class
    );

    assertEquals("{\"message\":\"Student not exists with id: " + notExistingId + "\"}", response.getBody());
  }

  @Test
  public void readAllCourses_StudentExistsWithOneCourse_AllDataReturnedInResponse() throws JsonProcessingException {
    String expectedStudentName = "name";
    String expectedStudentEmail = "email";
    String expectedCourseName = "courseName";
    Integer expectedCourseCredit = 123;

    Student insertedStudent = helper.insertStudent(restTemplate, port, expectedStudentName, expectedStudentEmail);
    Course insertedCourse = helper.insertCourse(restTemplate, port, expectedCourseName, expectedCourseCredit);
    registerStudentForCourse(insertedStudent.getId(), insertedCourse.getId());

    ResponseEntity<String> response = this.restTemplate.exchange(
        "http://localhost:" + port + "/student/" + insertedStudent.getId() + "/courses", HttpMethod.GET,
        new HttpEntity<>(helper.loginForTest(restTemplate, port)),
        String.class
    );

    ReadAllCoursesResponse readAllCoursesResponse = helper.getObjectFromResponse(response.getBody(), ReadAllCoursesResponse.class);

    assertEquals(expectedStudentName, readAllCoursesResponse.getStudent().getName());
    assertEquals(expectedStudentEmail, readAllCoursesResponse.getStudent().getEmail());
    assertEquals(expectedCourseName, readAllCoursesResponse.getCourseList().get(0).getName());
    assertEquals(expectedCourseCredit, readAllCoursesResponse.getCourseList().get(0).getCredit());
  }

  @Test
  public void readAllCourses_StudentNotExists_ErrorReturned() {
    int notExistingId = 999;

    ResponseEntity<String> response = this.restTemplate.exchange(
        "http://localhost:" + port + "/student/" + notExistingId + "/courses", HttpMethod.GET,
        new HttpEntity<>(helper.loginForTest(restTemplate, port)),
        String.class
    );

    assertEquals("{\"message\":\"Student not exists with id: " + notExistingId + "\"}", response.getBody());
  }

  @Test
  public void update_StudentExists_UpdatedStudentReturnedInResponse() throws JsonProcessingException {
    String updatedName = "name_updated";
    String updatedEmail = "email_updated";

    Student insertedStudent = helper.insertStudent(restTemplate, port, "name", "email");

    ResponseEntity<Student> response = this.restTemplate.exchange(
        "http://localhost:" + port + "/student/" + insertedStudent.getId(),
        HttpMethod.PUT,
        new HttpEntity<>("{\"name\":\"" + updatedName + "\", \"email\":\"" + updatedEmail + "\"}", helper.loginForTest(restTemplate, port)),
        Student.class
    );

    assertEquals(updatedName, Objects.requireNonNull(response.getBody()).getName());
    assertEquals(updatedEmail, response.getBody().getEmail());
  }

  @Test
  public void update_StudentNotExists_BadRequestReturned() {
    ResponseEntity<String> response = this.restTemplate.exchange(
        "http://localhost:" + port + "/student/" + 999,
        HttpMethod.PUT,
        new HttpEntity<>("{\"name\":\"name_updated\", \"email\":\"email_updated\"}", helper.loginForTest(restTemplate, port)),
        String.class
    );

    assertEquals("{\"message\":\"Student not exists with id: 999\"}",
        response.getBody());
    assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode());
  }

  @ParameterizedTest(name = "{0}")
  @MethodSource("invalidRequestProvider")
  public void update_InvalidRequests_ErrorMessageReturned(String desc, String requestBody, String responseMessage) {
    ResponseEntity<String> response = this.restTemplate.exchange(
        "http://localhost:" + port + "/student/" + 999,
        HttpMethod.PUT,
        new HttpEntity<>(requestBody, helper.loginForTest(restTemplate, port)),
        String.class
    );

    assertEquals(responseMessage, response.getBody());
  }

  @Test
  public void delete_StudentExists_SuccessReturned() throws JsonProcessingException {
    Student student = helper.insertStudent(restTemplate, port,"name", "email");

    ResponseEntity<String> response = this.restTemplate.exchange(
        "http://localhost:" + port + "/student/" + student.getId(),
        HttpMethod.DELETE,
        new HttpEntity<>("", helper.loginForTest(restTemplate, port)),
        String.class
    );

    assertEquals("{\"success\":true}", response.getBody());
  }

  @Test
  public void delete_StudentNotExists_BadRequestReturned() {
    ResponseEntity<String> response = this.restTemplate.exchange(
        "http://localhost:" + port + "/student/" + 999,
        HttpMethod.DELETE,
        new HttpEntity<>("", helper.loginForTest(restTemplate, port)),
        String.class
    );

    assertEquals("{\"message\":\"Student not exists with id: 999\"}",
        response.getBody());
    assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode());
  }

  private void registerStudentForCourse(Integer studentId, Integer courseId) {
    this.restTemplate.postForObject(
        "http://localhost:" + port + "/register/student/" + studentId + "/course/" + courseId,
        new HttpEntity<>("", helper.loginForTest(restTemplate, port)),
        String.class
    );
  }

  private static Stream<Arguments> invalidRequestProvider() {
    return Stream.of(
        Arguments.of(
            "name missing",
            "{\"email\": \"email@example.com\"}",
            "{\"message\":\"name must be specified, name must not be empty\"}"
        ),
        Arguments.of(
            "email missing",
            "{\"name\": \"example name\"}",
            "{\"message\":\"email must be specified, email must not be empty\"}"
        ),
        Arguments.of(
            "name cannot be empty",
            "{\"name\": \"\", \"email\": \"email@example.com\"}",
            "{\"message\":\"name must not be empty\"}"
        ),
        Arguments.of(
            "email cannot be empty",
            "{\"name\": \"example name\", \"email\": \"\"}",
            "{\"message\":\"email must not be empty\"}"
        ),
        Arguments.of(
            "invalid json",
            "{invalid}",
            "{\"message\":\"Invalid JSON in request body\"}"
        )
    );
  }
}
