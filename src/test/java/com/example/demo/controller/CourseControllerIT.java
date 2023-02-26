package com.example.demo.controller;

import static org.junit.jupiter.api.Assertions.assertEquals;

import com.example.demo.Helper;
import com.example.demo.entity.Course;
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
public class CourseControllerIT {

  @Value(value = "${local.server.port}")
  private int port;

  @Autowired
  private TestRestTemplate restTemplate;

  @Autowired
  private Helper helper;

  @Test
  public void create_AllParametersProvided_CourseReturnedInResponse() throws JsonProcessingException {
    String expectedName = "name";
    Integer expectedCredit = 11;

    Course actualCourse = helper.insertCourse(restTemplate, port, expectedName, expectedCredit);

    assertEquals(expectedName, actualCourse.getName());
    assertEquals(expectedCredit, actualCourse.getCredit());
  }

  @ParameterizedTest(name = "{0}")
  @MethodSource("invalidRequestProvider")
  public void create_InvalidRequests_ErrorMessageReturned(String desc, String requestBody, String responseMessage) {
    String response = restTemplate.postForObject(
        "http://localhost:" + port + "/course",
        new HttpEntity<>(requestBody, helper.loginForTest(restTemplate, port)),
        String.class
    );

    assertEquals(responseMessage, response);
  }

  @Test
  public void read_CourseExists_CourseReturnedInResponse() throws JsonProcessingException {
    String expectedName = "name";
    Integer expectedCredit = 11;

    Course insertedCourse = helper.insertCourse(restTemplate, port, expectedName, expectedCredit);

    ResponseEntity<String> response = restTemplate.exchange(
        "http://localhost:" + port + "/course/" + insertedCourse.getId(), HttpMethod.GET,
        new HttpEntity<>(helper.loginForTest(restTemplate, port)), String.class);

    Course actualCourse = helper.getObjectFromResponse(response.getBody(), Course.class);

    assertEquals(expectedName, actualCourse.getName());
    assertEquals(expectedCredit, actualCourse.getCredit());
  }

  @Test
  public void read_CourseNotExists_ErrorReturned() {
    int notExistingId = 999;
    ResponseEntity<String> response = restTemplate.exchange(
        "http://localhost:" + port + "/course/" + notExistingId, HttpMethod.GET,
        new HttpEntity<>(helper.loginForTest(restTemplate, port)), String.class);

    assertEquals("{\"message\":\"Course not exists with id: " + notExistingId + "\"}", response.getBody());
  }

  @Test
  public void update_CourseExists_UpdatedCourseReturnedInResponse() throws JsonProcessingException {
    String updatedName = "name_updated";
    Integer updatedCredit = 999;

    Course insertedCourse = helper.insertCourse(restTemplate, port, "name", 11);

    ResponseEntity<Course> response = this.restTemplate.exchange(
        "http://localhost:" + port + "/course/" + insertedCourse.getId(),
        HttpMethod.PUT,
        new HttpEntity<>("{\"name\":\"" + updatedName + "\", \"credit\":" + updatedCredit + "}",
            helper.loginForTest(restTemplate, port)),
        Course.class
    );

    assertEquals(updatedName, Objects.requireNonNull(response.getBody()).getName());
    assertEquals(updatedCredit, response.getBody().getCredit());
  }

  @Test
  public void update_CourseNotExists_BadRequestReturned() {
    ResponseEntity<String> response = this.restTemplate.exchange(
        "http://localhost:" + port + "/course/" + 999,
        HttpMethod.PUT,
        new HttpEntity<>("{\"name\":\"name_updated\", \"credit\":11}", helper.loginForTest(restTemplate, port)),
        String.class
    );

    assertEquals("{\"message\":\"Course not exists with id: 999\"}",
        response.getBody());
    assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode());
  }

  @ParameterizedTest(name = "{0}")
  @MethodSource("invalidRequestProvider")
  public void update_InvalidRequests_ErrorMessageReturned(String desc, String requestBody, String responseMessage) {
    ResponseEntity<String> response = this.restTemplate.exchange(
        "http://localhost:" + port + "/course/" + 999,
        HttpMethod.PUT,
        new HttpEntity<>(requestBody, helper.loginForTest(restTemplate, port)),
        String.class
    );

    assertEquals(responseMessage, response.getBody());
  }

  @Test
  public void delete_CourseExists_SuccessReturned() throws JsonProcessingException {
    Course course = helper.insertCourse(restTemplate, port, "name", 11);

    ResponseEntity<String> response = this.restTemplate.exchange(
        "http://localhost:" + port + "/course/" + course.getId(),
        HttpMethod.DELETE,
        new HttpEntity<>("", helper.loginForTest(restTemplate, port)),
        String.class
    );

    assertEquals("{\"success\":true}", response.getBody());
  }

  @Test
  public void delete_CourseNotExists_BadRequestReturned() {
    ResponseEntity<String> response = this.restTemplate.exchange(
        "http://localhost:" + port + "/course/" + 999,
        HttpMethod.DELETE,
        new HttpEntity<>("", helper.loginForTest(restTemplate, port)),
        String.class
    );

    assertEquals("{\"message\":\"Course not exists with id: 999\"}",
        response.getBody());
    assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode());
  }


  private static Stream<Arguments> invalidRequestProvider() {
    return Stream.of(
        Arguments.of(
            "name missing",
            "{\"credit\": 123}",
            "{\"message\":\"name must be specified, name must not be empty\"}"
        ),
        Arguments.of(
            "credit missing",
            "{\"name\": \"example name\"}",
            "{\"message\":\"credit must be specified\"}"
        ),
        Arguments.of(
            "name cannot be empty",
            "{\"name\": \"\", \"credit\": 123}",
            "{\"message\":\"name must not be empty\"}"
        ),
        Arguments.of(
            "invalid json",
            "{invalid}",
            "{\"message\":\"Invalid JSON in request body\"}"
        )
    );
  }
}
